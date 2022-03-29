package at.htl.franklynserver.entity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExamTest {

    @Test
    void createExam_simpleSuccess() {
        Exam exam = new Exam();
        exam.id = 1L;
        exam.compression = 1;
        exam.date = LocalDate.of(2022, Month.MARCH, 26);
        exam.interval=5;
        exam.pin="12345";
        exam.ongoing=false;
        exam.title="NVS-Test";
        exam.resolution=Resolution.HD;
        exam.startTime = LocalDateTime.of(2022, 3, 26, 11, 45);
        exam.endTime = LocalDateTime.of(2022, 3, 26, 13, 45);
        List<Examinee> examinees = new ArrayList<>();
        examinees.add(new Examinee("Michael", "Tran"));
        examinees.add(new Examinee("Tamara", "Melcher"));
        List<Examiner> examiners = new ArrayList<>();
        examiners.add(new Examiner("stuetz", "Thomas", "Stuetz", true));
        List<Form> forms = new ArrayList<>();
        forms.add(new Form("4AHIF", "2022"));
        exam.examineeIds = examinees;
        exam.examinerIds = examiners;
        exam.formIds = forms;

        assertEquals(1L, exam.id);
        assertNotEquals(2, exam.compression);
        assertEquals(2,exam.examineeIds.size());
        assertEquals(1, exam.examinerIds.size());
        assertEquals("12345", exam.pin);
        assertTrue(exam.examinerIds.get(0).isAdmin);
        assertEquals(LocalDateTime.of(2022, 3, 26, 11, 45), exam.startTime);
        assertEquals(LocalDateTime.of(2022, 3, 26, 13, 45), exam.endTime);
        assertEquals("4AHIF", exam.formIds.get(0).title);
        assertFalse(exam.ongoing);

    }
}