package rgups.railvideo.service;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import rgups.railvideo.core.memdata.SensorsMinMaxTracker;
import rgups.railvideo.model.SdbChannelInfo;
import rgups.railvideo.model.SdbSensorData;
import rgups.railvideo.model.SdbSensorInfo;
import rgups.railvideo.model.indicators.*;
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

    @Value("${rv.sensors.track_period_sec:3600}")
    long sensorsTrackPeriodSec;

    @Value("${rv.sensors.buffer_size:1024}")
    int sensorsBufSize;

    SensorsMinMaxTracker<Double> minMaxTracker;

    SensorsDataCache cache = new SensorsDataCache();

    public void addData(List<FlatSensorData> dataList){
        cache.addValues(dataList);

        if (null == minMaxTracker){
            synchronized (this) {
                if (null == minMaxTracker) {
                    minMaxTracker = new SensorsMinMaxTracker<>(Double.class, sensorsBufSize, sensorsTrackPeriodSec * 1000);
                }
            }
        }

        for (FlatSensorData fsd : dataList) {
            minMaxTracker.addValues(fsd.getSensorName() + "_" + fsd.getChannel(),
                    fsd.getEndTime(), fsd.getMaxValue(), fsd.getMinValue());
        }

        for (FlatSensorData fsd : dataList) {
            saveSensorReading(fsd);
        }
    }

    public Map<ChannelHeader, SortedSet<ChannelValue>> getSensorsStats() {
        Map<ChannelHeader, SortedSet<ChannelValue>> rawStat = cache.getCache();
        return rawStat;
    }

    public Pair<Double, Pair<Long, Long>> getDeltaFor(String sensorName, String channnelName) {
        Pair<Double, Pair<Long, Long>> ret = minMaxTracker.getMaxDiff(sensorName + "_" + channnelName);
        return ret;
    }

    public Pair<Double, Long> getMaxPair(String sensorName, String channnelName) {
        Pair<Double, Long> ret = minMaxTracker.getMaxPair(sensorName + "_" + channnelName);
        return ret;
    }

    public Pair<Double, Long> getMinPair(String sensorName, String channnelName) {
        Pair<Double, Long> ret = minMaxTracker.getMinPair(sensorName + "_" + channnelName);
        return ret;
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

    public long getSensorsTrackPeriodSec() {
        return sensorsTrackPeriodSec;
    }

    public void setSensorsTrackPeriodSec(long sensorsTrackPeriodSec) {
        this.sensorsTrackPeriodSec = sensorsTrackPeriodSec;
    }

    public int getSensorsBufSize() {
        return sensorsBufSize;
    }

    public void setSensorsBufSize(int sensorsBufSize) {
        this.sensorsBufSize = sensorsBufSize;
    }
}
