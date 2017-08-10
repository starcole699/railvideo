package rgups.railvideo.model;

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
    @Column(name="NAME", length=128)
    private String name;

    @Column(name="DESCR", nullable=true)
    private String des;

    @Column(name="TYPE", nullable=true)
    private String type;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "SENSOR_NAME")
    private List<SdbSensorAttr> attrs = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "SENSOR_NAME")
    private List<SdbChannelInfo> channels = new ArrayList<>();


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

    public List<SdbSensorAttr> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<SdbSensorAttr> attrs) {
        this.attrs = attrs;
    }

    public List<SdbChannelInfo> getChannels() {
        return channels;
    }

    public void setChannels(List<SdbChannelInfo> channels) {
        this.channels = channels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SdbSensorInfo that = (SdbSensorInfo) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return type != null ? type.equals(that.type) : that.type == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
