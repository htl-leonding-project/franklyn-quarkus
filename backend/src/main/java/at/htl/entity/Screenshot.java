package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
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
    @Column(name = "S_ID")
    public Long id;

    @NotNull
    @Column(name = "S_TIMESTAMP")
    public Timestamp timestamp;

    @NotNull
    @Column(name = "S_RUNNING_NUMBER")
    public Long runningNo;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "S_USER_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public User user;


    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "S_EXAM_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Exam exam;


    @Column(name = "S_PATH_OF_SCREENSHOT")
    public String pathOfScreenshot;

    public Screenshot() {
    }

    public Screenshot(Timestamp timestamp, Long runningNo, User user, String pathOfScreenshot) {
        this.timestamp = timestamp;
        this.runningNo = runningNo;
        this.user = user;
        this.pathOfScreenshot = pathOfScreenshot;
    }

    public Screenshot(Timestamp timestamp, Long runningNo, User user,
                      String pathOfScreenshot, Exam exam) {
        this(timestamp, runningNo, user, pathOfScreenshot);
        this.exam = exam;
    }

    /*public Screenshot(Timestamp timestamp, Long runningNo,
                      Resolution resolution, int compression,
                      IsCompressed isCompressed, String pathOfScreenshot) {
        this.timestamp = timestamp;
        this.runningNo = runningNo;
        this.resolution = resolution;
        this.compression = compression;
        this.isCompressed = isCompressed;
        this.pathOfScreenshot = pathOfScreenshot;
    }*/

    @Override
    public String toString() {
        return String.format(this.user.lastName + ": " + this.runningNo + ", " + this.pathOfScreenshot);
    }
}
