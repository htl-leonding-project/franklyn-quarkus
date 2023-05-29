package at.htl.entity;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserGroupTest {

    @Test
    void simpleSuccess_addForm() {
        UserGroup form = new UserGroup("SYP1-Test", "2022");

        assertEquals("SYP1-Test", form.title);
        assertEquals("2022", form.year);

        form.title = "NVS2-Test";

        assertEquals("NVS2-Test", form.title);
    }
}