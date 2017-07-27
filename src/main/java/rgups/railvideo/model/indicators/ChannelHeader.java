package rgups.railvideo.model.indicators;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dmitry on 27.07.2017.
 */
public class ChannelHeader implements Comparable<ChannelHeader> {

    @JsonProperty(value = "sensorName")
    final String sensorName;

    @JsonProperty(value = "sensorType")
    final String sensorType;

    @JsonProperty(value = "channel")
    final String channel;

    public ChannelHeader(String sensorName, String sensorType, String channel) {
        this.sensorName = sensorName;
        this.sensorType = sensorType;
        this.channel = channel;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChannelHeader that = (ChannelHeader) o;

        if (sensorName != null ? !sensorName.equals(that.sensorName) : that.sensorName != null) return false;
        if (sensorType != null ? !sensorType.equals(that.sensorType) : that.sensorType != null) return false;
        return channel != null ? channel.equals(that.channel) : that.channel == null;

    }

    @Override
    public int hashCode() {
        int result = sensorName != null ? sensorName.hashCode() : 0;
        result = 31 * result + (sensorType != null ? sensorType.hashCode() : 0);
        result = 31 * result + (channel != null ? channel.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChannelHeader{" +
                "sensorName='" + sensorName + '\'' +
                ", sensorType='" + sensorType + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }

    public String getStrRep(){
        return sensorName + "_" + channel;
    }

    @Override
    public int compareTo(ChannelHeader o) {
        return getStrRep().compareTo(o.getStrRep());
    }
}
