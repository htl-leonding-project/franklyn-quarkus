package at.htl.entity;


import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class ExamineeTest {

    @Test
    void add_examinee_simple_success() {

        Exam exam = new Exam();
        exam.id = 1L;
        exam.compression = 1;
        exam.date = LocalDate.of(2022, Month.MARCH, 26);
        exam.interval=5;
        exam.pin="12345";
        exam.examState=ExamState.IN_PREPARATION;
        exam.title="NVS-Test";
        exam.resolution=Resolution.HD;
        exam.startTime = LocalDateTime.of(2022, 3, 26, 11, 45);
        exam.endTime = LocalDateTime.of(2022, 3, 26, 13, 45);
        List<Examiner> examiners = new ArrayList<Examiner>();
        examiners.add(new Examiner("stuetz", "Thomas", "Stuetz", true));
        List<SchoolClass> forms = new ArrayList<SchoolClass>();
        forms.add(new SchoolClass("4AHIF", "2022"));
        forms.add(new SchoolClass("3AHIF", "2021"));
        exam.examiners = examiners;
        exam.schoolClasses = forms;

        Examinee examinee = new Examinee();
        examinee.firstName = "Tamara";
        examinee.lastName = "Melcher";
        examinee.exam = exam;

        assertEquals("Tamara", examinee.firstName);
        assertEquals("Melcher", examinee.lastName);
        assertEquals(exam.id, examinee.exam.id);
    }
}