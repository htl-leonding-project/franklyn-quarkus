package at.htl.entity.dto;

public record ScreenshotAngularDto(
                                   Long examId,
                                   Long examineeId,
                                   String image,
                                   Long screenshotId
) {
}
