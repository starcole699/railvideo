package rgups.railvideo.proc;

import com.google.common.collect.EvictingQueue;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedResource;
import rgups.railvideo.core.RvMat;
import rgups.railvideo.core.flow.RailvideoEvent;
import rgups.railvideo.proc.model.ImageProcContext;
import rgups.railvideo.proc.model.RvFlowProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Dmitry on 21.08.2017.
 */
public class ImageHistoryKeeper extends ImageProcessor {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @RvFlowProperty
    Integer bufSize = 5;

    @RvFlowProperty
    Long minInterval = 500L;

    EvictingQueue<HistoryRecord> imgQ = EvictingQueue.create(bufSize);

    Lock imgQLock = new ReentrantLock();

    {
        processType = "HISTORY_STORED";
    }

    @Override
    void processAsync(ImageProcContext.Action action, RailvideoEvent event) {
        imgQLock.lock();
        RvMat img = action.getImageData();
        String name = event.getCaptureId();
        imgQ.add(new HistoryRecord(img, name, event.getTimestamp()));
        imgQLock.unlock();
    }


    @Override
    public List<Mat> getBearingMats() {
        List<Mat> ret = super.getBearingMats();
        imgQLock.lock();
        imgQ.forEach((hr)->{ret.add(hr.mat);});
        imgQLock.unlock();
        return ret;
    }

    public List<HistoryRecord> getHistoryCopy(){
        imgQLock.lock();
        List<HistoryRecord> ret = new ArrayList<>();
        imgQ.forEach((hr)->{
            Mat matClone;
            if (hr.mat instanceof  RvMat) {
                matClone = (RvMat)((RvMat) hr.mat).cloneAsMat();
            } else {
                matClone = hr.mat.clone();
            }
            ret.add(new HistoryRecord(matClone, hr.name, hr.time));
        });
        imgQLock.unlock();
        return ret;
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
