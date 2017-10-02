package rgups.railvideo.model.alarms;

import rgups.railvideo.proc.ImageHistoryKeeper;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dmitry on 27.07.2017.
 */
public class UiAlarm extends DbAlarm {

    Double value;

    String alarmUid;

    public UiAlarm(){

    }

    public UiAlarm(Date time, String level, String type, String header) {
        super(time, level, type, header);
    }

    public DbAlarm asDbAlarm(){
        return new DbAlarm(this);
    }

    List<ImageHistoryKeeper.HistoryRecord> historyImages = new ArrayList<>();

    public List<ImageHistoryKeeper.HistoryRecord> getHistoryImages() {
        return historyImages;
    }

    public void setHistoryImages(List<ImageHistoryKeeper.HistoryRecord> historyImages) {
        this.historyImages = historyImages;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getAlarmUid() {
        return alarmUid;
    }

    public void setAlarmUid(String alarmUid) {
        this.alarmUid = alarmUid;
    }

    public void setUidFrom(Object...o){
        String str = Arrays.stream(o)
                .map(x -> x.toString())
                .sorted()
                .collect(Collectors.joining("__"));
        setAlarmUid(str);
    }

    public String createUid(Object...items){
        String uid = Arrays.stream(items).map(x -> x.toString()).sorted().collect(Collectors.joining("--"));
        this.alarmUid = uid;
        return uid;
    }

    @Override
    public String toString() {
        return "UiAlarm{" +
                "value=" + value +
                ", alarmUid='" + alarmUid + '\'' +
                ", level='" + level + '\'' +
                ", type='" + type + '\'' +
                ", header='" + header + '\'' +
                ", descr='" + descr + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
