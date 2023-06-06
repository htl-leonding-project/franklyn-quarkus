package at.htl.entity.dto;

import at.htl.entity.ExamState;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ExamDto(
        String pin,
        String title,
        ExamState state,
        LocalDate date,
        LocalDateTime startTime,
        LocalDateTime endTime,
        int interval
) {
}
