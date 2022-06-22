package at.htl.franklynserver.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jdk.jfr.Name;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NamedQueries({
        @NamedQuery(
                name = "Exam.findExamWithSameDateAndPIN",
                query = "select e from Exam e where e.date = :DATE and e.pin LIKE :PIN")

})

@Entity
@Table(name = "F_EXAM")
public class Exam extends PanacheEntityBase {
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

    @Column(name = "E_ONGOING")
    public boolean ongoing = false;

    @OneToMany( cascade = CascadeType.ALL)
    @Size(min = 1)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "E_FORM_IDS")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public List<SchoolClass> formIds;

    @NotNull
    @Column(name = "E_DATE")
    public LocalDate date;

    @NotNull
    @Column(name = "E_START_TIME")
    public LocalDateTime startTime;

    @NotNull
    @Column(name = "E_END_TIME")
    public LocalDateTime endTime;

    @OneToMany(cascade = CascadeType.ALL)
    @Size(min = 1)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "E_EXAMINEE_IDS")
    public List<Examinee> examineeIds;

    @OneToMany(cascade = CascadeType.ALL)
    @Size(min = 1)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "E_EXAMINER_IDS")
    public List<Examiner> examinerIds;

    @NotNull
    @ConfigProperty(defaultValue = "5")
    @Column(name = "E_INTERVAL")
    public int interval;

    @NotNull
    @Column(name = "E_RESOLUTION")
    public Resolution resolution;

    @NotNull
    @Min(1)
    @Max(100)
    @Column(name = "E_COMPRESSION")
    public int compression;

    public Exam() {
    }

    public Exam(String pin,
                String title,
                boolean ongoing,
                LocalDate date,
                LocalDateTime startTime,
                LocalDateTime endTime,
                int interval,
                Resolution resolution,
                int compression) {
        this.pin = pin;
        this.title = title;
        this.ongoing = ongoing;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.interval = interval;
        this.resolution = resolution;
        this.compression = compression;
    }

    public Exam(String pin,
                String title,
                boolean ongoing,
                List<SchoolClass> formIds,
                LocalDate date,
                LocalDateTime startTime,
                LocalDateTime endTime,
                List<Examinee> examineeIds,
                List<Examiner> examinerIds,
                int interval,
                Resolution resolution,
                int compression) {
        this.pin = pin;
        this.title = title;
        this.ongoing = ongoing;
        this.formIds = formIds;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.examineeIds = examineeIds;
        this.examinerIds = examinerIds;
        this.interval = interval;
        this.resolution = resolution;
        this.compression = compression;
    }


}
