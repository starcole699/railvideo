package rgups.railvideo.proc;

import com.google.common.collect.EvictingQueue;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import rgups.railvideo.core.RvMat;
import rgups.railvideo.core.flow.RailvideoEvent;
import rgups.railvideo.proc.model.ImageProcContext;
import rgups.railvideo.proc.model.RvFlowProperty;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Dmitry on 21.08.2017.
 */
@Component
@RvFlowItem
@ManagedResource
public class ImageHistoryKeeper extends ImageProcessor {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @RvFlowProperty
    Integer bufSize = 5;

    @RvFlowProperty
    Long minInterval = 500L;

    volatile long lastUpdate = 0L;

    //EvictingQueue<HistoryRecord> imgQ = EvictingQueue.create(bufSize);
    List<HistoryRecord> imgQ = new LinkedList<HistoryRecord>();

    Lock imgQLock = new ReentrantLock();

    {
        processType = "HISTORY_STORED";
    }

    @Override
    void processAsync(ImageProcContext.Action action, RailvideoEvent event) {
        long time = System.currentTimeMillis();
        if (time - lastUpdate < minInterval) {
            return;
        }
        imgQLock.lock();
        try{
            Mat img = action.getImageData().cloneAsMat();
            String name = event.getCaptureId();
            if (imgQ.size() >= bufSize) {
                HistoryRecord oldRec = imgQ.get(0);
                oldRec.mat.release();
                imgQ.remove(0);
            }
            imgQ.add(new HistoryRecord(img, name, event.getTimestamp()));
            lastUpdate = time;
        } finally {
            imgQLock.unlock();
        }
    }


    @Override
    public List<Mat> getBearingMats() {
        return super.getBearingMats();
        /*
        imgQLock.lock();
        try{
            List<Mat> ret = super.getBearingMats();
            imgQ.forEach((hr)->{ret.add(hr.mat);});
            return ret;
        } finally {
            imgQLock.unlock();
        }*/
    }

    public List<HistoryRecord> getHistoryCopy(){
        imgQLock.lock();
        try{
            List<HistoryRecord> ret = new ArrayList<>();
            imgQ.forEach((hr)->{
                Mat matClone;
                if (hr.mat instanceof  RvMat) {
                    matClone = ((RvMat) hr.mat).cloneAsMat();
                } else {
                    matClone = hr.mat.clone();
                }
                ret.add(new HistoryRecord(matClone, hr.name, hr.time));
            });
            return ret;
        } finally {
            imgQLock.unlock();
        }
    }

    public Integer getBufSize() {
        return bufSize;
    }

    public void setBufSize(Integer bufSize) {
        this.bufSize = bufSize;
    }

    public Long getMinInterval() {
        return minInterval;
    }

    public void setMinInterval(Long minInterval) {
        this.minInterval = minInterval;
    }

    public class HistoryRecord {
        public Mat mat;
        public String name;
        public Long time;

        HistoryRecord(Mat mat, String name, Long time){
            this.mat = mat;
            this.name = name;
            this.time = time;
        }
    }
}
