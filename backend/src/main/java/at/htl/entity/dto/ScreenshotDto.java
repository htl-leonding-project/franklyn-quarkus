package at.htl.entity.dto;

import at.htl.entity.Exam;
import at.htl.entity.User;

public record ScreenshotDto(
        Long runningNo,
        Exam exam,
        User examinee,
        String screenshotName
) {
}
