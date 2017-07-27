package rgups.railvideo.service;

import org.springframework.stereotype.Service;
import rgups.railvideo.model.indicators.ChannelHeader;
import rgups.railvideo.model.indicators.ChannelValue;
import rgups.railvideo.model.indicators.FlatSensorData;
import rgups.railvideo.model.indicators.SensorsDataCache;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 * Created by Dmitry on 22.07.2017.
 */
@Service
public class SensorStatsService {

    SensorsDataCache cache = new SensorsDataCache();

    public void addData(List<FlatSensorData> dataList){
        cache.addValues(dataList);
    }

    public Map<?, ?> getSensorsStats() {
        Map<ChannelHeader, SortedSet<ChannelValue>> rawStat = cache.getCache();
        return rawStat;
    }
}
