package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDateTime;

public class UserSession extends PanacheEntityBase {
    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "US_USER_ID")
    User user;

    @ManyToOne
    @JoinColumn(name = "US_EXAM_ID")
    Exam exam;

    @Column(name = "US_ROLE")
    @Enumerated(EnumType.STRING)
    public UserRole userRole;

}
