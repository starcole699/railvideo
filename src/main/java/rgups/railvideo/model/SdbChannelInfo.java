package rgups.railvideo.model;

import javax.persistence.*;

/**
 * Created by Dmitry on 27.07.2017.
 */
@Entity
@Table(name="CHANNEL_INFO")
public class SdbChannelInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="NAME", length=64)
    private String name;

    @ManyToOne
    @JoinColumn(name="SENSOR_ID", nullable=false)
    private SdbSensorInfo sensorInfo;

    @Column(name="DESCR")
    private String descr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SdbSensorInfo getSensorInfo() {
        return sensorInfo;
    }

    public void setSensorInfo(SdbSensorInfo sensorInfo) {
        this.sensorInfo = sensorInfo;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
