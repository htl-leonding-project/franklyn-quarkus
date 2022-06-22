package at.htl.franklynserver.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
@Table(name = "F_EXAMINEE")
public class Examinee extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "E_ID")
    public Long id;

    @Column(name = "E_FIRST_NAME")
    public String firstName;

    @Column(name = "E_LAST_NAME")
    public String lastName;

    @ManyToOne
    @JoinColumn(name = "E_EXAM_ID")
    public Exam exam;

    public Examinee(){}

    public Examinee(String firstName, String lastName, Exam exam) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.exam = exam;
    }
}
