package rgups.railvideo.model;

import javax.persistence.*;

/**
 * Created by Dmitry on 22.07.2017.
 */
@Entity
@Table(name="SENSOR_ATTRS")
public class SdbSensorAttr {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name="SENSOR_NAME", nullable=false)
    private String sensorName;

    @Column(name="DESCR", nullable=true)
    private String descr;

    @Column(name="VAL", nullable=true)
    private String val;

    @Column(name="NUM_VAL", nullable=true)
    private Double num_val;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SdbSensorAttr that = (SdbSensorAttr) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (sensorName != null ? !sensorName.equals(that.sensorName) : that.sensorName != null) return false;
        if (val != null ? !val.equals(that.val) : that.val != null) return false;
        return num_val != null ? num_val.equals(that.num_val) : that.num_val == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (sensorName != null ? sensorName.hashCode() : 0);
        result = 31 * result + (val != null ? val.hashCode() : 0);
        result = 31 * result + (num_val != null ? num_val.hashCode() : 0);
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public Double getNum_val() {
        return num_val;
    }

    public void setNum_val(Double num_val) {
        this.num_val = num_val;
    }
}
