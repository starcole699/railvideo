package rgups.railvideo.model.indicators;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Dmitry on 27.07.2017.
 */
public class SensorsDataCache {

    int bufferSize = 20;

    Map<ChannelHeader, SortedSet<ChannelValue>> cache = new TreeMap<>();

    public synchronized void addValues(List<FlatSensorData> dataList) {
        Map<ChannelHeader, SortedSet<ChannelValue>> newData = new TreeMap<>();
        for (Map.Entry<ChannelHeader, SortedSet<ChannelValue>> entry : cache.entrySet()){
            ChannelHeader key = entry.getKey();
            SortedSet<ChannelValue> values = entry.getValue();
            SortedSet<ChannelValue> newValues = new TreeSet<ChannelValue>(values);
            if (newValues.size() >= bufferSize) {
                newValues.remove(newValues.first());
            }
            newData.put(key, newValues);
        }

        for (FlatSensorData data : dataList){
            Object[] splitted = data.splitHeaderAndData();
            ChannelHeader newKey = (ChannelHeader)(splitted[0]);
            ChannelValue newValue = (ChannelValue)(splitted[1]);
            SortedSet<ChannelValue> valuesCache = newData.get(newKey);
            if (null == valuesCache) {
                valuesCache = new TreeSet<ChannelValue>();
                newData.put(newKey, valuesCache);
            }
            valuesCache.add(newValue);
        }

        cache = newData;
    }

    public Map<ChannelHeader, SortedSet<ChannelValue>> getCache() {
        return cache;
    }
}
