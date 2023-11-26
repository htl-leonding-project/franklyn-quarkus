package at.htl.entity.dto;

import at.htl.entity.ExamState;
import at.htl.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExamDto(
        String title,
        ExamState state,
        LocalDate date,
        LocalDateTime startTime,
        LocalDateTime endTime,
        int interval,
        User admin
) {

}
