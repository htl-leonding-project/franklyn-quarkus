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
    @JoinColumn(name = "S_EXAMINEE_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Examinee examinee;

    @NotNull
    @Column(name = "S_RESOLUTION")
    public Resolution resolution;

    @NotNull
    @Min(1)
    @Max(100)
    @Column(name = "S_COMPRESSION")
    public int compression;

    @Column(name = "S_IS_COMPRESSED")
    public IsCompressed isCompressed;

    @Column(name = "S_PATH_OF_SCREENSHOT")
    public String pathOfScreenshot;

    public Screenshot() {
    }

    public Screenshot(Timestamp timestamp, Long runningNo, Exam exam, Examinee examinee,
                      Resolution resolution, int compression, String pathOfScreenshot) {
        this.timestamp = timestamp;
        this.runningNo = runningNo;
        //this.exam = exam;
        this.examinee = examinee;
        this.resolution = resolution;
        this.compression = compression;
        this.pathOfScreenshot = pathOfScreenshot;
    }

    public Screenshot(Timestamp timestamp, Long runningNo,
                      Resolution resolution, int compression,
                      IsCompressed isCompressed, String pathOfScreenshot) {
        this.timestamp = timestamp;
        this.runningNo = runningNo;
        this.resolution = resolution;
        this.compression = compression;
        this.isCompressed = isCompressed;
        this.pathOfScreenshot = pathOfScreenshot;
    }
}
