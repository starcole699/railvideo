package rgups.railvideo.model.indicators;

/**
 * Created by Dmitry on 27.07.2017.
 */
public class ChannelValue implements Comparable<ChannelValue> {

    final Long startTime;
    final Long endTime;
    final Double minValue;
    final Double maxValue;
    final Double averageValue;

    public ChannelValue(Long startTime, Long endTime, Double minValue, Double maxValue, Double averageValue) {
        this.startTime = startTime;
        this.endTime = endTime;
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
        return "ChannelValue{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", averageValue=" + averageValue +
                '}';
    }


    @Override
    public int compareTo(ChannelValue o) {
        return endTime.compareTo(o.getEndTime());
    }
}
