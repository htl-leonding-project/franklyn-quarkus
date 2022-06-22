package at.htl.franklynserver.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExamineeDetailsTest {

    @Test
    void add_examinee_details_simple_success() {
        ExamineeDetails examineeDetails = new ExamineeDetails();
        examineeDetails.examId = 4L;
        examineeDetails.isOnline=false;
        examineeDetails.latestScreenshotNumber=1;

        Examinee examinee = new Examinee("Michael", "Tran");

        examineeDetails.examinee = examinee;

        assertEquals(1L, examineeDetails.examinee.id);
        assertNotEquals(1L, examineeDetails.examId);
        assertFalse(examineeDetails.isOnline);
        assertEquals(1,examineeDetails.latestScreenshotNumber);
    }

}