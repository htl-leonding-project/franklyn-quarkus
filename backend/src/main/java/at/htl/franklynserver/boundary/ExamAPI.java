package at.htl.franklynserver.boundary;

import at.htl.franklynserver.control.ExamRepository;
import at.htl.franklynserver.control.ExamineeDetailsRepository;
import at.htl.franklynserver.control.ExamineeRepository;
import at.htl.franklynserver.entity.Exam;
import at.htl.franklynserver.entity.Examinee;
import at.htl.franklynserver.entity.ExamineeDetails;
import at.htl.franklynserver.entity.Examiner;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("api/exams")
public class ExamAPI {

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
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Exam> saveExam(Exam exam){
        if(exam != null)
            examRepository.persist(exam);
        String pin = examRepository.createPIN(exam.date);
        return Panache
                .withTransaction(() -> examRepository.findById(exam.id)
                        .onItem().ifNotNull()
                        .transform(entity -> {
                            entity.pin = pin;
                            return entity;
                        })
                        .onFailure().recoverWithNull());
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
        Uni<Exam> exam = examRepository.findById(id);
        if(exam != null)
            examRepository.deleteById(id);
        return exam;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("examineeDetails/{id}")
    public Uni<List<ExamineeDetails>> getExamineeDetailsWithExamineeByID(@PathParam("id") Long id){
        return examineeDetailsRepository.getExamineeDetailsWithExamineeByID(id);
    }

    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Uni<Exam> updateExam(Exam exam){
        return Panache
                .withTransaction(() -> examRepository.findById(exam.id)
                        .onItem().ifNotNull()
                        .transform(entity -> {
                            entity.pin = exam.pin;
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
    @Path("enroll")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Uni<Exam> enrollStudentForExam(@FormParam("id") Long id, Examinee examinee){
        //show if already exists with first and last name
        return Panache
                .withTransaction(() -> examRepository.findById(id)
                        .onItem().ifNotNull()
                        .transform(entity -> {
                            List<Examinee> examinees = entity.examineeIds;
                            examinees.add(examinee);
                            entity.examineeIds = examinees;
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

    @DELETE
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Exam> deleteExam(Exam exam){
        //can only be deleted if there are no more exaxminers in it
        Uni<Exam> examToDelete = examRepository.findById(exam.id);
        if(examToDelete != null)
            examRepository.deleteById(exam.id);
        return examToDelete;
    }

}
