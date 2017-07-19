package rgups.railvideo.model.indicators;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Dmitry on 19.07.2017.
 */
public class Statistics {

    @JsonProperty(value = "start_time")
    Long startTime;

    @JsonProperty(value = "end_time")
    Long endTime;

    @JsonProperty(value = "sensors")
    List<Sensor> sensors;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", sensors=" + sensors +
                '}';
    }
}
