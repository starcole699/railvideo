package rgups.railvideo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rgups.railvideo.model.SavedImage;
import rgups.railvideo.model.alarms.AlarmEvent;
import rgups.railvideo.model.alarms.DbAlarm;
import rgups.railvideo.model.alarms.UiAlarm;
import rgups.railvideo.proc.ImageHistoryKeeper;
import rgups.railvideo.proc.model.RvFlowProperty;
import rgups.railvideo.proc.sensors.SensorEvent;
import rgups.railvideo.repositories.AlarmRepo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AlarmService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ImageStorageService imageStorageService;

    @Autowired
    ImageHistoryKeeper imageHistoryKeeper;

    @Autowired
    AlarmRepo alarmRepo;

    @RvFlowProperty
    String savePath = "alarms";

    @RvFlowProperty
    String format = "jpeg";

    @Value("${rv.alarms.alarm_lifetime_sec:3600}")
    long alarmLifetimeSec;

    Map<String, AlarmTrack> aliveAlarms = new ConcurrentHashMap<>();

    static class AlarmTrack{
        String uid;
        long time;
        double value;

        AlarmTrack(UiAlarm alarm){
            this.uid = (alarm.getType() + "_" + alarm.getAlarmUid()).intern();
            this.time = alarm.getTime().getTime();
            this.value = alarm.getValue();
        }
    }


    public UiAlarm createAlarm(Date time, String level, String type, String header) {
        UiAlarm ret = new UiAlarm(time, level, type, header);
        ret.setHistoryImages(null);
        return ret;
    }

    public UiAlarm createAlarmWithCurrentImages(Date time, String level, String type, String header) {
        UiAlarm ret = new UiAlarm(time, level, type, header);
        ret.setHistoryImages(imageHistoryKeeper.getHistoryCopy());
        return ret;
    }

    @EventListener
    @Transactional
    public void acceptAlarmEvent(AlarmEvent event) {
        LOG.info("Alarm accepted");
        UiAlarm alarm = (UiAlarm)event.getSource();

        if (!updateAliveAlarms(alarm)) {
            LOG.info("Alarm already active: " + alarm);
            return;
        }

        if (null == alarm.getHistoryImages()) {
            alarm.setHistoryImages(imageHistoryKeeper.getHistoryCopy());
        }
        LOG.info("Saving alarm: " + alarm);
        saveUiAlarm(alarm);
    }

    @Transactional
    public DbAlarm saveUiAlarm(UiAlarm alarm) {
        List<ImageHistoryKeeper.HistoryRecord> imgHistory = alarm.getHistoryImages();
        try {
            List<SavedImage> savedImages = new ArrayList<>();
            for (ImageHistoryKeeper.HistoryRecord hr : imgHistory) {
                savedImages.add(imageStorageService.saveOrFindMat(hr.mat, savePath, hr.name, "", format, hr.time));
            }

            DbAlarm dbAlarm = alarm.asDbAlarm();
            dbAlarm.setImages(savedImages);

            alarmRepo.saveAndFlush(dbAlarm);

            return dbAlarm;
        } finally {
            for (ImageHistoryKeeper.HistoryRecord hr : imgHistory) {
                hr.mat.release();
            }
        }
    }

    private boolean updateAliveAlarms(UiAlarm alarm){
        AlarmTrack track;
        synchronized (this) {
            track = new AlarmTrack(alarm);
        }
        synchronized (track.uid){
            AlarmTrack existingTrack = aliveAlarms.get(track.uid);
            if ((null == existingTrack) ||
                    (System.currentTimeMillis() - existingTrack.time > alarmLifetimeSec * 1000) ||
                    (existingTrack.value < track.value)){
                aliveAlarms.put(track.uid, track);
                return true;
            }
            return false;
        }
    }
}
