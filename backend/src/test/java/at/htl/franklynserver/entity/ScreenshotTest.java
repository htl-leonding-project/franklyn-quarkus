package at.htl.franklynserver.entity;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ScreenshotTest {

    @Test
    void simpleSuccess_addScreenshot() {
        //Timestamp timestamp = Timestamp.valueOf(
                //LocalDateTime.now()).getTime();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        Screenshot screenshot = new Screenshot(timestamp, 1L,
                1L, 1L, Resolution.HD, 30);

        assertEquals(timestamp, screenshot.timestamp);
        assertEquals(1L, screenshot.screenshotNumber);
        assertEquals(1L, screenshot.examId);
        assertEquals(1L, screenshot.examineeId);
        assertEquals(Resolution.HD, screenshot.resolution);
        assertEquals(30, screenshot.compression);

        screenshot.compression = 50;

        assertEquals(50, screenshot.compression);
    }
}