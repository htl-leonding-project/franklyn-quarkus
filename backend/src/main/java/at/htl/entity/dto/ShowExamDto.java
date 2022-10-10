package at.htl.entity.dto;

public record ShowExamDto(
        String title,
        String date,
        String secondTeacher,
        String form,
        String startTime,
        String nrOfStudents,
        boolean ongoing
) {
}
