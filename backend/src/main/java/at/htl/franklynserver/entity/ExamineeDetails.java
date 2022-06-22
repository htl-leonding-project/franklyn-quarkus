package at.htl.franklynserver.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@NamedQueries({
        @NamedQuery(
                name = "ExamineeDetails.getExamineeDetailsByExamID",
                query = "select ed from ExamineeDetails ed where ed.examId = ?1"),
})
@Entity
@Table(name = "F_EXAMINEE_DETAILS")
public class ExamineeDetails extends PanacheEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "ED_ID")
//    public Long id;

    @Column(name = "ED_EXAM_ID")
    public Long examId;

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

    public ExamineeDetails(Long examId,
                           boolean isOnline,
                           LocalDateTime lastOnline,
                           Timestamp latestTimestamp,
                           int latestScreenshotNumber,
                           Examinee examinee
    ) {
        this.examId = examId;
        this.isOnline = isOnline;
        this.lastOnline = lastOnline;
        this.latestTimestamp = latestTimestamp;
        this.latestScreenshotNumber = latestScreenshotNumber;
        this.examinee = examinee;
    }
}
