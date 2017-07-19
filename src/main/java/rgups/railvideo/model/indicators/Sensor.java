package rgups.railvideo.model.indicators;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Dmitry on 19.07.2017.
 */
public class Sensor {

    @JsonProperty(value = "sensor_name")
    String sensorName;

    @JsonProperty(value = "sensor_type")
    String sensorType;

    @JsonProperty(value = "channels")
    List<Channel> channels;

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "sensorName='" + sensorName + '\'' +
                ", sensorType='" + sensorType + '\'' +
                ", channels=" + channels +
                '}';
    }
}
