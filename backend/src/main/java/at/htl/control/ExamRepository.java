package at.htl.control;

import at.htl.entity.Exam;
import at.htl.entity.Examinee;
import at.htl.entity.Examiner;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.time.LocalDate;
import java.util.*;

@ApplicationScoped
public class ExamRepository implements PanacheRepository<Exam> {

    public String createPIN() {
        boolean pinIsValid = false;
        String pin;
        do {
            pin = String.valueOf((int) (Math.random() * (999 - 100)) + 100);
            var exams = this.find("select e from Exam e where e.pin LIKE :PIN and e.date = :DATE",
                    Parameters.with("PIN", pin),
                    Parameters.with("DATE", LocalDate.now())).list();
            if (exams.isEmpty())
                pinIsValid = true;
        } while (pinIsValid);
        return pin;
    }

    public Boolean verifyPIN(Long id, String pin) {
        var exam = this.find("select e from Exam e where e.pin LIKE :PIN and e.isDeleted = false",
                Parameters.with("PIN", pin)).firstResult();

        if (exam != null)
            return pin.equals(exam.pin);
        return false;
    }

    public Exam getExamByPin(String pin) {
        return this.find("select e from Exam e where e.pin LIKE :PIN",
                Parameters.with("PIN", pin)).firstResult();
    }

    public static List<File> deleteDirectoryOfScreenshots(String name, File root) {
        List<File> result = new ArrayList<>();
        for (File file : Objects.requireNonNull(root.listFiles())) {
            if (file.isDirectory()) {
                if (file.getName().equals(name)) {
                    result.add(file);
                }

                result.addAll(deleteDirectoryOfScreenshots(name, root));
            }
        }

        return result;
    }

    public void deleteExaminerFromExams(Long id) {
        var exams = this.listAll();
        for (Exam e : exams) {
            for (Examiner ex : e.examiners) {
                if (Objects.equals(ex.id, id) && !e.isDeleted) {
                    this.getEntityManager().remove(ex);
                }
            }
        }
    }

    public ArrayList<String> GetAllExaminersOfExam(Exam exam, Long adminId) {
        var examiners = new ArrayList<String>();
        for (Examiner examiner : exam.examiners) {
            if (examiner.id != adminId) {
                examiners.add(examiner.firstName + " " + examiner.lastName);
            }
        }
        return examiners;
    }

    public ArrayList<String> GetAllSchoolClassesOfExam(Exam exam) {
        var schoolClasses = new ArrayList<String>();
        if (exam.schoolClasses != null) {
            for (var schoolClass : exam.schoolClasses) {
                schoolClasses.add(schoolClass.title);
            }
        }
        return schoolClasses;
    }
}
