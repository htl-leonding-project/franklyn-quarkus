package at.htl.franklynserver.entity.dto;

import at.htl.franklynserver.entity.Exam;
import at.htl.franklynserver.entity.Examinee;

public record ScreenshotDto(
        Long runningNo,
        Exam exam,
        Examinee examinee
) {
}
