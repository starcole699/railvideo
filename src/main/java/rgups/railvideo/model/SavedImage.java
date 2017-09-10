package rgups.railvideo.model;

import org.opencv.core.Mat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="SAVED_IMAGE",
        indexes = {@Index(name = "SAVED_IMAGE_NAME_INDEX",  columnList="FILE_NAME")})
public class SavedImage {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="FILE_NAME")
    private String fileName;

    @Column(name="EXTENSION", length = 32)
    private String extension;

    @Column(name="CAPTURE_TIME", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date captureTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(Date captureTime) {
        this.captureTime = captureTime;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
