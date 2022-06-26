package at.htl.entity.dto;

import at.htl.entity.Exam;
import at.htl.entity.Examinee;

public record ScreenshotDto(
        Long runningNo,
        Exam exam,
        Examinee examinee,
        String screenshotName
) {
}
