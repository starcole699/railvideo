package rgups.railvideo.model.alarms;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.ReflectionUtils;
import rgups.railvideo.model.SavedImage;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Dmitry on 27.07.2017.
 */
@Entity
@Table(name="ALARMS")
public class DbAlarm {

    public static final String AL_INFO = "INFO";
    public static final String AL_WARNING = "WARNING";
    public static final String AL_ERROR = "ERROR";
    public static final String AL_PANIC = "PANIC";

    public static final String AT_BUSINESS = "BUSINESS";
    public static final String AT_TECHNICAL = "TECHNICAL";

    // Angle change in time period.
    public static final String AT_ANGLE_REL_CHANGE = "AT_ANGLE_REL_CHANGE";

    // Angle change from base level.
    public static final String AT_ANGLE_ABS_CHANGE = "AT_ANGLE_ABS_CHANGE";



    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @JsonProperty("alarm_id")
    private Long id;

    @Column(name="TIME", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column(name="LEVEL", nullable=false, length = 16)
    String level;

    @Column(name="TYPE", nullable=false, length = 64)
    String type;

    @Column(name="HEADER", nullable=false, length = 128)
    String header;

    @Column(name="DESCR")
    String descr = "";

    @Column(name="DETAILS", columnDefinition = "TEXT")
    String details;

    @ManyToMany(cascade = {
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinTable(name = "ALARM_IMAGES",
            joinColumns = @JoinColumn(name = "alarm_id"),
            inverseJoinColumns = @JoinColumn(name = "img_id")
    )
    List<SavedImage> images = new ArrayList<>();

    public DbAlarm() {
    }

    public DbAlarm(Date time, String level, String type, String header) {
        this.time = time;
        this.level = level;
        this.type = type;
        this.header = header;
    }

    public DbAlarm(DbAlarm src) {
        this.time = src.time;
        this.level = src.level;
        this.type = src.type;
        this.header = src.header;
        this.descr = src.descr;
        this.details = src.details;

    }

    public Long getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    public String getLevel() {
        return level;
    }

    public String getType() {
        return type;
    }

    public String getHeader() {
        return header;
    }

    public String getDescr() {
        return descr;
    }

    public String getDetails() {
        return details;
    }

    public List<SavedImage> getImages() {
        return images;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setImages(List<SavedImage> images) {
        this.images = images;
    }

    @JsonProperty("aud")
    public Long getAud(){
        return getId();
    }
}
