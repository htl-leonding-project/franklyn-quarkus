package at.htl.entity.dto;

import at.htl.entity.Exam;
import at.htl.entity.Examinee;

public record ScreenshotAngularDto(
                                   Long examId,
                                   Long examineeId,
                                   String image,
                                   Long screenshotId
) {
}
