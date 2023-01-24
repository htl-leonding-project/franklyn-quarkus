package at.htl.entity.dto;

import java.util.List;

public record ExamUpdateDto(
        String title,
        boolean ongoing,
        String date,
        int interval,
        List<String> examinerIds,
        List<String> formIds

) {
}
