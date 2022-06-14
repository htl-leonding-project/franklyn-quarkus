package at.htl.franklynserver.entity;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class SchoolClassTest {

    @Test
    void simpleSuccess_addForm() {
        SchoolClass form = new SchoolClass("SYP1-Test", "2022");

        assertEquals("SYP1-Test", form.title);
        assertEquals("2022", form.year);

        form.title = "NVS2-Test";

        assertEquals("NVS2-Test", form.title);
    }
}