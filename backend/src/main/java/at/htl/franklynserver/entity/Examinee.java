package at.htl.franklynserver.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

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
}
