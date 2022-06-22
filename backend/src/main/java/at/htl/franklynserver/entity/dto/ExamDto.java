package at.htl.franklynserver.entity.dto;

import java.util.List;

public record ExamDto(
        String title,
        String date,
        List<String> examinerIds,
        List<String> formIds,
        String startTime,
        String endTime
) {
}
