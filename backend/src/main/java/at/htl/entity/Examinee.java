package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "F_EXAMINEE")
public class Examinee extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EE_ID")
    public Long id;

    @Column(name = "EE_FIRST_NAME")
    public String firstName;

    @Column(name = "EE_LAST_NAME")
    public String lastName;

    @ManyToOne
    @JoinColumn(name = "EE_EXAM")
    public Exam exam;

    @Column(name = "EE_IS_ONLINE")
    public boolean isOnline;

    @Column(name = "EE_LAST_ONLINE")
    public LocalDateTime lastOnline;

    public Examinee(){}

    public Examinee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Examinee(String firstName, String lastName, Exam exam) {
        this(firstName, lastName);
        this.exam = exam;
    }

    public Examinee(String firstName, String lastName, Exam exam, boolean isOnline, LocalDateTime lastOnline) {
        this(firstName, lastName, exam);
        this.isOnline = isOnline;
        this.lastOnline = lastOnline;
    }

    @Override
    public String toString() {
        return String.format(this.firstName + " " + this.lastName + " " + this.isOnline);
    }
}
