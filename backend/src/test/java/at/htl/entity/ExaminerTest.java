package at.htl.entity;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ExaminerTest {

    @Test
    void simpleSuccess_addExaminer() {
        User examiner = new User( "Max", "Muster", false);

        assertEquals("Max", examiner.firstName);
        assertEquals("Muster", examiner.lastName);
        assertEquals(false, examiner.isAdmin);

        examiner.isAdmin = true;

        assertEquals(true, examiner.isAdmin);
    }
}