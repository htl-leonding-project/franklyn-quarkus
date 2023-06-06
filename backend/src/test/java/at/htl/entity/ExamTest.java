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
        exam.startTime = LocalDateTime.of(2022, 3, 26, 11, 45);
        exam.endTime = LocalDateTime.of(2022, 3, 26, 13, 45);
        List<User> examiners = new ArrayList<User>();
        examiners.add(new User( "Thomas", "Stuetz", true));
        List<UserGroup> forms = new ArrayList<UserGroup>();
        forms.add(new UserGroup("4AHIF", "2022"));
        forms.add(new UserGroup("3AHIF", "2021"));
        exam.userExaminers = examiners;
        exam.userGroups = forms;

        assertEquals(1L, exam.id);
        assertNotEquals(2, exam.compression);
        assertEquals("12345", exam.pin);
        assertTrue(exam.userExaminers.get(0).isAdmin);
        assertEquals(LocalDateTime.of(2022, 3, 26, 11, 45), exam.startTime);
        assertEquals(LocalDateTime.of(2022, 3, 26, 13, 45), exam.endTime);
        assertEquals("4AHIF", exam.userGroups.get(0).title);

    }
}