package rgups.railvideo.core.memdata;

import javafx.util.Pair;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SensorsMinMaxTracker<T extends Number & Comparable<T>> {

    private static final String MIN = "_min";
    private static final String MAX = "_max";

    int bufSize = 256;

    long bufDuration = 1000 * 60 * 60;

    Map<String, RankedTimeSeries<T>> data = new ConcurrentHashMap<>();

    Class<T> valueClass;

    public SensorsMinMaxTracker(Class<T> valueClass) {
        this.valueClass = valueClass;
    }

    public void addValue(String sensorId, long ts, T value) {
        addValue(sensorId, ts, value, MIN);
        addValue(sensorId, ts, value, MAX);
    }

    public Pair<T, Pair<Long, Long>> getMaxDistance(String sensorId) {
        Pair<T, Long> minPair = getMinPair(sensorId);
        Pair<T, Long> maxPair = getMaxPair(sensorId);
        if ((minPair == null)&&(maxPair == null)) {
            return null;
        }

        if ((minPair == null)||(maxPair == null)) {
            Pair<T, Long> p = (Pair<T, Long>)firstNotNull(minPair, maxPair);
            return new Pair<T, Pair<Long, Long>>(p.getKey(), new Pair<Long, Long>(p.getValue(), p.getValue()));
        }

        Double diff = Math.abs(maxPair.getKey().doubleValue() - minPair.getKey().doubleValue());
        try {
            Constructor<T> constr = valueClass.getConstructor(Double.class);
            constr.
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return new Pair<T, Pair<Long, Long>>(p.getKey(), new Pair<Long, Long>(p.getValue(), p.getValue()));
    }

    public Pair<T, Long> getMinPair(String sensorId) {
        return getValuePair(sensorId, MIN);
    }

    public Pair<T, Long> getMaxPair(String sensorId) {
        return getValuePair(sensorId, MAX);
    }

    public Pair<T, Long> getValuePair(String sensorId, String idPrefix) {
        String key = sensorId + idPrefix;
        RankedTimeSeries<T> records = data.get(key);
        if (null == records) {
            return null;
        }
        return records.getTopPair();
    }

    private void addValue(String sensorId, long ts, T value, String idPrefix) {
        String key = sensorId + idPrefix;
        RankedTimeSeries<T> records = data.get(key);
        if (null == records) {
            synchronized (this) {
                records = data.get(key);
                if (null == records) {
                    records = new RankedTimeSeries<T>(valueClass, idPrefix == MIN);
                    data.put(key, records);
                }
            }
        }
        records.addValue(ts, value);
    }

    public int getBufSize() {
        return bufSize;
    }

    public void setBufSize(int bufSize) {
        this.bufSize = bufSize;
    }

    public long getBufDuration() {
        return bufDuration;
    }

    public void setBufDuration(long bufDuration) {
        this.bufDuration = bufDuration;
    }

    public Object firstNotNull(Object...objects){
        for (Object o : objects) {
            if (null != o) {
                return o;
            }
        }
        return null;
    }
}
