package at.htl.entity.dto;

import at.htl.entity.User;

public record ExamParticipantDTO (User user, String ip) {
}
