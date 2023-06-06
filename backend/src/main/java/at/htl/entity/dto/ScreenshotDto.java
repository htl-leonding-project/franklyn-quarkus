package at.htl.entity.dto;

import at.htl.entity.Exam;
import at.htl.entity.User;

import java.sql.Timestamp;

public record ScreenshotDto(
        Timestamp timestamp,
        Long runningNo,
        User user,
        String pathOfScreenshot,
        Exam exam

) {
}
