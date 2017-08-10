package rgups.railvideo.model.indicators;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dmitry on 19.07.2017.
 */
public class Channel {

    @JsonProperty(value = "name")
    String name;

    @JsonProperty(value = "min_value")
    Double min;

    @JsonProperty(value = "max_value")
    Double max;

    Double avg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    @JsonProperty(value = "average_value")
    public Double getAvg() {
        return avg;
    }

    @JsonProperty(value = "average_value")
    public void setAvg(Double avg) {
        this.avg = avg;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "name='" + name + '\'' +
                ", min=" + min +
                ", max=" + max +
                ", avg=" + avg +
                '}';
    }
}
