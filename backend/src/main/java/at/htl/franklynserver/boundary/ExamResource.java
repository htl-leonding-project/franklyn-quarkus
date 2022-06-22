package at.htl.franklynserver.boundary;

import at.htl.franklynserver.control.ExamRepository;
import at.htl.franklynserver.control.ExamineeDetailsRepository;
import at.htl.franklynserver.control.ExamineeRepository;
import at.htl.franklynserver.entity.*;
import at.htl.franklynserver.entity.dto.ExamDto;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Path("api/exams")
public class ExamResource
{

    @Inject
    ExamRepository examRepository;

    @Inject
    ExamineeDetailsRepository examineeDetailsRepository;

    @Inject
    ExamineeRepository examineeRepository;


    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Exam>> getAll(){
        return Exam.list("order by date");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ReactiveTransactional
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Exam> saveExam(ExamDto exam){
        String pin = examRepository.createPIN(LocalDate.now());
        Exam e = new Exam(
                pin,
                exam.title(),
                true,
                LocalDate.parse(exam.date()),
                LocalDateTime.parse(exam.startTime()),
                LocalDateTime.parse(exam.endTime()),
                5,
                Resolution.HD,
                1
        );

        examRepository.persist(e).subscribe().with(exam1 -> Log.info(exam1.title));
        return examRepository.findById(e.id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("examinees")
    public Uni<List<Examinee>> getExaminees(Exam exam){
        return examRepository.getExaminees(exam.id);
    }

    @PUT
    @Path("addExaminer/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Exam> addExaminerToExam(@FormParam("id") Long id, Examinee examinee){

        //validation for duplicate examiners
        return examRepository.findById(id)
                .onItem().ifNotNull()
                .transform(entity -> {
                    List<Examinee> examinees = entity.examineeIds;
                    examinees.add(examinee);
                    entity.examineeIds = examinees;
                    return entity;
                });
    }

    @DELETE
    @Path("delete/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Exam> deleteExam(@FormParam("id") Long id){
        examRepository.deleteById(id)
                .subscribe().with(e ->{Log.info(id);});
        //can only be deleted if there are no more exaxminers in it
        return examRepository.findById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("examineeDetails/{id}")
    public Uni<List<ExamineeDetails>> getExamineeDetailsWithExamineeByID(@PathParam("id") Long id){
        return examineeDetailsRepository.getExamineeDetailsWithExamineeByID(id);
    }

    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ReactiveTransactional
    public Uni<Exam> updateExam(@PathParam("id")Long id, Exam exam){
        return Panache
                .withTransaction(() -> examRepository.findById(id)
                        .onItem().ifNotNull()
                        .transform(entity -> {
                            entity.title = exam.title;
                            entity.date = exam.date;
                            entity.formIds = exam.formIds;
                            entity.examineeIds = exam.examineeIds;
                            entity.examinerIds = exam.examinerIds;
                            entity.startTime = exam.startTime;
                            entity.endTime = exam.endTime;
                            entity.ongoing = exam.ongoing;
                            entity.interval = exam.interval;
                            entity.resolution = exam.resolution;
                            entity.compression = exam.compression;
                            return entity;
                        })
                        .onFailure().recoverWithNull());
    }

    @PUT
    @Path("enroll/{id}")
    @ReactiveTransactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Exam> enrollStudentForExam(@PathParam("id") Long id, Examinee examinee){
        //show if already exists with first and last name
        Examinee examineeToPersist = new Examinee(examinee.firstName, examinee.lastName);
        return Panache
                .withTransaction(() -> examRepository.findById(id)
                        .onItem().ifNotNull()
                        .transform(entity -> {
                            //List<Examinee> examinees = entity.examineeIds;
                            //examinees.add(examineeToPersist);
                            entity.examineeIds.add(examineeToPersist);
                            return entity;
                        }));
    }

    //sends details back
    @PUT
    @Transactional
    @Path("examinee/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Uni<ExamineeDetails> removeExamineeFromExam(@PathParam("id") Long id, Long examineeId){
        return null;
    }

}
