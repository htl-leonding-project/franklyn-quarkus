package at.htl.entity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ExamTest {

    @Test
    void createExam_simpleSuccess() {
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

        assertEquals(1L, exam.id);
        assertNotEquals(2, exam.compression);
        assertEquals("12345", exam.pin);
        assertTrue(exam.examiners.get(0).isAdmin);
        assertEquals(LocalDateTime.of(2022, 3, 26, 11, 45), exam.startTime);
        assertEquals(LocalDateTime.of(2022, 3, 26, 13, 45), exam.endTime);
        assertEquals("4AHIF", exam.schoolClasses.get(0).title);

    }
}