package at.htl.franklynserver.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExamineeTest {
    @Test
    void add_examinee_simple_success() {
        Examinee examinee = new Examinee();
        examinee.firstName = "Tamara";
        examinee.lastName = "Melcher";

        assertEquals("Tamara", examinee.firstName);
        assertEquals("Melcher", examinee.lastName);
    }
}