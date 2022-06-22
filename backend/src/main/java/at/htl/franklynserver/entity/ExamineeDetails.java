package at.htl.franklynserver.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@NamedQueries({
        @NamedQuery(
                name = "ExamineeDetails.getExamineeDetailsByExamID",
                query = "select ed from ExamineeDetails ed where ed.exam.id = ?1"),
})
@Entity
@Table(name = "F_EXAMINEE_DETAILS")
public class ExamineeDetails extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ED_ID")
    public Long id;

    @ManyToOne
    @JoinColumn(name = "ED_EXAM_ID")
    public Exam exam;

    @Column(name = "ED_IS_ONLINE")
    public boolean isOnline;

    @Column(name = "ED_LAST_ONLINE")
    public LocalDateTime lastOnline;

    @Column(name = "ED_LATEST_TIMESTAMP")
    public Timestamp latestTimestamp;

    @Column(name = "ED_LATEST_SCREENSHOT_NUMBER")
    public int latestScreenshotNumber;

    @ManyToOne
    @JoinColumn(name = "ED_EXAMINEE_ID")
    public Examinee examinee;

    public ExamineeDetails(){}

    public ExamineeDetails(Exam exam,
                           boolean isOnline,
                           LocalDateTime lastOnline,
                           Timestamp latestTimestamp,
                           int latestScreenshotNumber,
                           Examinee examinee
    ) {
        this.exam = exam;
        this.isOnline = isOnline;
        this.lastOnline = lastOnline;
        this.latestTimestamp = latestTimestamp;
        this.latestScreenshotNumber = latestScreenshotNumber;
        this.examinee = examinee;
    }
}
