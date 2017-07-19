package rgups.railvideo.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dmitry on 04.07.2017.
 */
@Entity
@Table(name="APP_USER")
public class Frame implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name="CAPTURE_ID", nullable=false)
    private String captureId;

    @Column(name="CAPTURE_TIME", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date captureTime;

    @NotEmpty
    @Column(name="path", nullable=false)
    private String path;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaptureId() {
        return captureId;
    }

    public void setCaptureId(String captureId) {
        this.captureId = captureId;
    }

    public Date getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(Date captureTime) {
        this.captureTime = captureTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
