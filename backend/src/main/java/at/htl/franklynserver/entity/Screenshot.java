package at.htl.franklynserver.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "F_SCREENSHOT")
public class Screenshot extends PanacheEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ED_ID")
    public Long id;

    @NotNull
    @Column(name = "S_TIMESTAMP")
    public Long timestamp;

    @NotNull
    @Column(name = "S_SCREENSHOT_NUMBER")
    public Long screenshotNumber;

    @NotNull
    @Column(name = "S_EXAM_ID")
    public Long examId;

    @NotNull
    @Column(name = "S_EXAMINEE_ID")
    public Long examineeId;

    @NotNull
    @Column(name = "S_RESOLUTION")
    public Resolution resolution;

    @NotNull
    @Min(1)
    @Max(100)
    @Column(name = "E_COMPRESSION")
    public int compression;

    @Column(name = "S_IS_COMPRESSED")
    @ConfigProperty(defaultValue = "NOT_COMPRESSED")
    public IsCompressed isCompressed;
}
