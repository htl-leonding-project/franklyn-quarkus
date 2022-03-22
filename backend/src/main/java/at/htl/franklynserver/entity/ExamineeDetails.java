package at.htl.franklynserver.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "F_EXAMINEE_DETAILS")
public class ExamineeDetails extends PanacheEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ED_ID")
    public Long id;

    @Column(name = "ED_EXAM_ID")
    public Long examId;

    @Column(name = "ED_EXAMINEE_ID")
    public Long examineeId;

    @Column(name = "ED_IS_ONLINE")
    public boolean isOnline;

    @Column(name = "ED_LAST_ONLINE")
    public Long lastOnline;

    @Column(name = "ED_LATEST_TIMESTAMP")
    public Long latestTimestamp;

    @Column(name = "ED_LATEST_SCREENSHOT_NUMBER")
    public int latestScreenshotNumber;

    public ExamineeDetails(){}

    public ExamineeDetails(Long examId, Long examineeId, boolean isOnline, Long lastOnline, Long latestTimestamp, int latestScreenshotNumber) {
        this.examId = examId;
        this.examineeId = examineeId;
        this.isOnline = isOnline;
        this.lastOnline = lastOnline;
        this.latestTimestamp = latestTimestamp;
        this.latestScreenshotNumber = latestScreenshotNumber;
    }
}
