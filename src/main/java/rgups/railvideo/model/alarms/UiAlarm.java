package rgups.railvideo.model.alarms;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dmitry on 27.07.2017.
 */
public class UiAlarm {
    private Long time = System.currentTimeMillis();
    private String level;
    private String type;
    private String header;
    private String descr;
    private List<AlarmDetail> details = new ArrayList<>();

    public UiAlarm() {
    }

    public UiAlarm(String level, String type, String header, String descr) {
        this.level = level;
        this.type = type;
        this.header = header;
        this.descr = descr;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public List<AlarmDetail> getDetails() {
        return details;
    }

    public void setDetails(List<AlarmDetail> details) {
        this.details = details;
    }
}
