package rgups.railvideo.model;

import rgups.railvideo.model.indicators.FlatSensorData;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Dmitry on 16.07.2017.
 */
@Entity
@Table(name="SENSOR_DATA",
        indexes = {@Index(name = "CHANNEL_INDEX",  columnList="CHANNEL_ID")})
public class SdbSensorData {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="START_TIME", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name="END_TIME", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name="RECORD_TIME", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordTime = new Date();

    @ManyToOne
    @JoinColumn(name="CHANNEL_ID", nullable=false)
    private SdbChannelInfo channelInfo;

    @Column(name="MAX")
    private Double max;

    @Column(name="MIN")
    private Double min;

    @Column(name="AVG")
    private Double avg;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public SdbChannelInfo getChannelInfo() {
        return channelInfo;
    }

    public void setChannelInfo(SdbChannelInfo channelInfo) {
        this.channelInfo = channelInfo;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

    public Long getId() {
        return id;
    }
}
