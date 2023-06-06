package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class UserSession extends PanacheEntityBase {
    @Id
    private
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "US_USER_ID")
    @NotNull
    private
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "US_EXAM_ID")
    @NotNull
    private
    Exam exam;

    @Column(name = "US_ROLE")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public UserSession(User user, Exam exam, UserRole userRole) {
        this.user = user;
        this.exam = exam;
        this.userRole = userRole;
    }

    public UserSession() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
