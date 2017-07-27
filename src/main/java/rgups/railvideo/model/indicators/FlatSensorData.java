package rgups.railvideo.model.indicators;

/**
 * Created by Dmitry on 19.07.2017.
 */
public class FlatSensorData {

    final Long startTime;
    final Long endTime;
    final String sensorName;
    final String sensorType;
    final String channel;
    final Double minValue;
    final Double maxValue;
    final Double averageValue;

    public FlatSensorData(Long startTime, Long endTime, String sensorName, String sensorType, String channel, Double minValue, Double maxValue, Double averageValue) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.sensorName = sensorName;
        this.sensorType = sensorType;
        this.channel = channel;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.averageValue = averageValue;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public String getSensorName() {
        return sensorName;
    }

    public String getSensorType() {
        return sensorType;
    }

    public String getChannel() {
        return channel;
    }

    public Double getMinValue() {
        return minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public Double getAverageValue() {
        return averageValue;
    }

    @Override
    public String toString() {
        return "FlatSensorData{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", sensorName='" + sensorName + '\'' +
                ", sensorType='" + sensorType + '\'' +
                ", channel='" + channel + '\'' +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", averageValue=" + averageValue +
                '}';
    }

    public Object[] splitHeaderAndData(){
        Object[] ret = new Object[2];
        ret[0] = new ChannelHeader(sensorName, sensorType, channel);
        ret[1] = new ChannelValue(startTime, endTime, minValue, maxValue, averageValue);
        return ret;
    }
}
