package rgups.railvideo.model;

import rgups.railvideo.model.indicators.FlatSensorData;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry on 22.07.2017.
 */
@Entity
@Table(name="SENSOR_INFO")
public class SdbSensorInfo {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="NAME", length=64)
    private String name;

    @Column(name="DESCR", nullable=true)
    private String des;

    @Column(name="TYPE", nullable=true)
    private String type;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "SENSOR_ID")
    private List<SdbChannelInfo> channels = new ArrayList<>();

    public SdbSensorInfo(){

    }

    public SdbSensorInfo(FlatSensorData fsd){
        this.name = fsd.getSensorName();
        this.type = fsd.getSensorType();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SdbChannelInfo> getChannels() {
        return channels;
    }

    public void setChannels(List<SdbChannelInfo> channels) {
        this.channels = channels;
    }
}
