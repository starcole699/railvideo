package rgups.railvideo.core.memdata;

import javafx.util.Pair;
import rgups.railvideo.utils.RvRuntimeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SensorsMinMaxTracker<T extends Number & Comparable<T>> {

    private static final String MIN = "_min";
    private static final String MAX = "_max";

    int bufSize = 256;

    long bufDuration = 1000 * 60 * 60;

    Map<String, RankedTimeSeries<T>> data = new ConcurrentHashMap<>();

    Class<T> valueClass;

    Comparator<T> comp = new Comparator<T>() {
        public int compare(T a, T b) { return a.compareTo(b); }
    };

    public SensorsMinMaxTracker(Class<T> valueClass) {
        this.valueClass = valueClass;
    }

    public SensorsMinMaxTracker(Class<T> valueClass, int bufSize, long bufDuration) {
        this.valueClass = valueClass;
        this.bufSize = bufSize;
        this.bufDuration = bufDuration;
    }

    public void addValue(String sensorId, long ts, T value) {
        addValue(sensorId, ts, value, MIN);
        addValue(sensorId, ts, value, MAX);
    }

    public void addValues(String sensorId, long ts, T value1, T value2) {
        addValue(sensorId, ts, min(value1, value2), MIN);
        addValue(sensorId, ts, min(value1, value2), MAX);
    }

    public Pair<T, Pair<Long, Long>> getMaxDiff(String sensorId) {
        Pair<T, Long> minPair = getMinPair(sensorId);
        Pair<T, Long> maxPair = getMaxPair(sensorId);
        if ((minPair == null)&&(maxPair == null)) {
            return null;
        }

        if ((minPair == null)||(maxPair == null)) {
            Pair<T, Long> p = (Pair<T, Long>)firstNotNull(minPair, maxPair);
            return new Pair<T, Pair<Long, Long>>(p.getKey(), new Pair<Long, Long>(p.getValue(), p.getValue()));
        }

        try {
            BigDecimal diff = new BigDecimal(maxPair.getKey().toString())
                    .subtract(new BigDecimal(minPair.getKey().toString())).abs();
            Constructor<T> constr = valueClass.getConstructor(String.class);
            T diffVal = constr.newInstance(diff.toString());
            return new Pair<T, Pair<Long, Long>>(diffVal, new Pair<Long, Long>(minPair.getValue(), maxPair.getValue()));
        } catch (Exception  e) {
            throw new RvRuntimeException("Can't find min max pair. ", e);
        }
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
                    records.setBufSize(bufSize);
                    records.setBufPeriod(bufDuration);
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

    public T min(T a, T b) { return a.compareTo(b) < 0 ? a : b; }
    public T max(T a, T b) { return a.compareTo(b) > 0 ? a : b; }
}
