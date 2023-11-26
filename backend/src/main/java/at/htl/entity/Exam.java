package at.htl.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.annotations.*;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NamedQueries({
        @NamedQuery(
                name = "Exam.findExamWithSameDateAndPIN",
                query = "select e from Exam e where e.date = :DATE and e.pin LIKE :PIN")
})

@JsonIdentityInfo(
        generator = ObjectIdGenerators.StringIdGenerator.class,
        property="e_id")
@Entity
@Table(name = "F_EXAM")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "E_ID")
    public Long id;

    @Column(name = "E_PIN")
    @NotNull
    @NotBlank
    public String pin;

    @NotNull
    @Column(name = "E_TITLE")
    public String title;

    @Column(name = "E_STATUS")
    @Enumerated(EnumType.STRING)
    public ExamState examState;

    @NotNull
    @Column(name = "E_DATE")
    public LocalDate date;

    @NotNull
    @Column(name = "E_START_TIME")
    public LocalDateTime startTime;

    @NotNull
    @Column(name = "E_END_TIME")
    public LocalDateTime endTime;

    /*@ManyToMany( cascade = {CascadeType.ALL, CascadeType.MERGE}, fetch=FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "F_USER",
            joinColumns = { @JoinColumn(name = "U_ID") },
            inverseJoinColumns = { @JoinColumn(name = "E_U_ID") })
    public List<User> users;*/

    @NotNull
    @ConfigProperty(defaultValue = "5")
    @Column(name = "E_INTERVAL")
    public int interval;

    @Column(name = "E_ADMIN_ID")
    public Long adminId;

    @NotNull
    @Min(1)
    @Max(100)
    @Column(name = "E_COMPRESSION")
    public int compression;

    @Column(name = "E_IS_DELETED")
    public boolean isDeleted = false;

    public Exam() {
    }
    public Exam(String pin,
                String title,
                ExamState state,
                LocalDate date,
                int interval,
                Long adminId) {
        this.pin = pin;
        this.title = title;
        this.examState = state;
        this.date = date;
        this.interval = interval;
        this.adminId = adminId;
    }

    public Exam(String pin,
                String title,
                ExamState state,
                LocalDate date,
                LocalDateTime startTime,
                LocalDateTime endTime,
                int interval ){
        this.pin = pin;
        this.title = title;
        this.examState = state;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.interval = interval;

    }


    @Override
    public String toString() {
        return String.format(this.title + " " + this.date + " " + this.examState);
    }

}

