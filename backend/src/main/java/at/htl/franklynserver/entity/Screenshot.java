package at.htl.franklynserver.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@NamedQueries({

        @NamedQuery(
                name = "Screenshot.findScreenshot",
                query = "select s from Screenshot s where s.examinee.id = ?1 " +
                        "and s.runningNo = ?2"),
        @NamedQuery(
                name = "Screenshot.findLatestScreenshot",
                query = "select s from Screenshot s where s.examinee.id = ?1 " +
                        "order by s.timestamp desc"
        )
})

@Entity
@Table(name = "F_SCREENSHOT")
public class Screenshot extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ED_ID")
    public Long id;

    @NotNull
    @Column(name = "S_TIMESTAMP")
    public Timestamp timestamp;

    @NotNull
    @Column(name = "S_SCREENSHOT_NUMBER")
    public Long runningNo;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "S_EXAM_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Exam exam;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "S_EXAMINEE_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Examinee examinee;

    @NotNull
    @Column(name = "S_RESOLUTION")
    public Resolution resolution;

    @NotNull
    @Min(1)
    @Max(100)
    @Column(name = "E_COMPRESSION")
    public int compression;

    @Column(name = "S_IS_COMPRESSED")
    public IsCompressed isCompressed;

    public Screenshot() {
    }

    public Screenshot(Timestamp timestamp, Long runningNo, Exam exam, Examinee examinee,
                      Resolution resolution, int compression) {
        this.timestamp = timestamp;
        this.runningNo = runningNo;
        this.exam = exam;
        this.examinee = examinee;
        this.resolution = resolution;
        this.compression = compression;
    }
}
