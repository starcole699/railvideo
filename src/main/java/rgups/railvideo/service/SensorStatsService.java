package rgups.railvideo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import rgups.railvideo.model.SdbChannelInfo;
import rgups.railvideo.model.SdbSensorData;
import rgups.railvideo.model.SdbSensorInfo;
import rgups.railvideo.model.indicators.ChannelHeader;
import rgups.railvideo.model.indicators.ChannelValue;
import rgups.railvideo.model.indicators.FlatSensorData;
import rgups.railvideo.model.indicators.SensorsDataCache;
import rgups.railvideo.repositories.ChannelInfoRepo;
import rgups.railvideo.repositories.SensorInfoRepo;
import rgups.railvideo.repositories.SensorsDataRepo;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 * Created by Dmitry on 22.07.2017.
 */
@Service
public class SensorStatsService {

    @Autowired
    ChannelInfoRepo channelInfoRepo;

    @Autowired
    SensorInfoRepo sensorInfoRepo;

    @Autowired
    SensorsDataRepo sensorsDataRepo;

    @Autowired
    private ApplicationContext applicationContext;

    SensorsDataCache cache = new SensorsDataCache();

    public void addData(List<FlatSensorData> dataList){
        cache.addValues(dataList);
        for (FlatSensorData fsd : dataList) {
            saveSensorReading(fsd);
        }
    }

    public Map<?, ?> getSensorsStats() {
        Map<ChannelHeader, SortedSet<ChannelValue>> rawStat = cache.getCache();
        return rawStat;
    }

    @Transactional
    public void saveSensorReading(FlatSensorData fsd){
        SensorStatsService me = applicationContext.getBean(SensorStatsService.class);
        SdbChannelInfo chanInf = me.getOrCreateChannelInfo(fsd);
        SdbSensorData sensorData = new SdbSensorData();
        sensorData.setStartTime(new Date(fsd.getStartTime()));
        sensorData.setEndTime(new Date(fsd.getEndTime()));
        sensorData.setMin(fsd.getMinValue());
        sensorData.setMax(fsd.getMaxValue());
        sensorData.setAvg(fsd.getAverageValue());
        sensorData.setChannelInfo(chanInf);
        sensorsDataRepo.saveAndFlush(sensorData);
    }

    @Transactional
    SdbSensorInfo getOrCreateSensorInfo(FlatSensorData fsd) {
        String sensorName = fsd.getSensorName();
        List<SdbSensorInfo> infos = sensorInfoRepo.findByName(sensorName);
        if (infos.size() == 1) {
            return infos.get(0);
        }

        if (infos.size() > 1) {
            throw new RuntimeException("Multiple sensors witn name " + sensorName + " in DB.");
        }

        SdbSensorInfo newSensorInfo = new SdbSensorInfo(fsd);
        sensorInfoRepo.saveAndFlush(newSensorInfo);
        return newSensorInfo;
    }


    @Transactional
    @Cacheable(cacheNames="channels_info", key="#fsd.getSensorName() + #fsd.getChannel()")
    public SdbChannelInfo getOrCreateChannelInfo(FlatSensorData fsd) {
        SdbSensorInfo sensorInfo = getOrCreateSensorInfo(fsd);
        String channelName = fsd.getChannel();
        List<SdbChannelInfo> channels = channelInfoRepo.findByNameAndSensorInfo(channelName, sensorInfo);

        if (channels.size() == 1) {
            return channels.get(0);
        }

        if (channels.size() > 1) {
            throw new RuntimeException("Multiple channels witn name " + channelName + " for sensor " + sensorInfo.getName() + " in DB.");
        }

        SdbChannelInfo newChannel = new SdbChannelInfo();
        newChannel.setName(channelName);
        newChannel.setSensorInfo(sensorInfo);
        channelInfoRepo.saveAndFlush(newChannel);

        return newChannel;
    }
}
