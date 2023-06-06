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
        exam.startTime = LocalDateTime.of(2022, 3, 26, 11, 45);
        exam.endTime = LocalDateTime.of(2022, 3, 26, 13, 45);
        List<User> examiners = new ArrayList<User>();
        examiners.add(new User("Thomas", "Stuetz", true));
        List<UserGroup> forms = new ArrayList<UserGroup>();
        forms.add(new UserGroup("4AHIF", "2022"));
        forms.add(new UserGroup("3AHIF", "2021"));
        exam.userExaminers = examiners;
        exam.userGroups = forms;

        User examinee = new User();
        examinee.firstName = "Tamara";
        examinee.lastName = "Melcher";

        assertEquals("Tamara", examinee.firstName);
        assertEquals("Melcher", examinee.lastName);
    }
}