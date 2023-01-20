package at.htl.entity.dto;

import java.util.HashMap;
import java.util.List;

public record ShowExamDto(
        String title,
        String date,
        List<String> examiners,
        List<String> forms,
        String startTime,
        String nrOfStudents,
        String ongoing,
        String pin,
        Long id,
        boolean isToday,
        boolean canBeEdited,
        boolean canBeDeleted,
        int interval

) {
}
