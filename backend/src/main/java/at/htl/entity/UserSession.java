package at.htl.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity(name = "F_USER_SESSION")
@NamedQueries({
        @NamedQuery(name = "UserSession.isUserPartOfExam",
                query = "from F_USER_SESSION us" +
                        " where us.exam.id = ?1 and us.user.firstName = ?2 and us.user.lastName = ?3"),
        @NamedQuery(name = "UserSession.getSessionByExamAndUserId",
                query = "FROM F_USER_SESSION us where exam.id = ?1" +
                        "and us.user.id = ?2")
})
public class UserSession {
    @Id
    @Column(name = "US_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "U_ID")
    //@MapsId("US_U_ID")
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "E_ID")
    //@MapsId("US_E_ID")
    @NotNull
    private Exam exam;

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

    @Override
    public String toString() {
        return "UserSession{" +
                "id=" + id +
                ", user=" + user +
                ", exam=" + exam +
                ", userRole=" + userRole +
                '}';
    }
}
