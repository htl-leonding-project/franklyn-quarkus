package at.htl.entity.dto;

import java.util.List;

public record ShowExamDto(
        String title,
        String date,
        List<String> examinerIds,
        List<String> formIds,
        String startTime,
        String nrOfStudents,
        String ongoing,
        String pin,
        Long id
) {
}
