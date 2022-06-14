package at.htl.franklynserver.entity;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ExaminerTest {

    @Test
    void simpleSuccess_addExaminer() {
        Examiner examiner = new Examiner("if180xxx", "Max", "Muster", false);

        assertEquals("if180xxx", examiner.userName);
        assertEquals("Max", examiner.firstName);
        assertEquals("Muster", examiner.lastName);
        assertEquals(false, examiner.isAdmin);

        examiner.isAdmin = true;

        assertEquals(true, examiner.isAdmin);
    }
}