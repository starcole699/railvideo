package rgups.railvideo.proc;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import rgups.railvideo.core.flow.RailvideoEvent;
import rgups.railvideo.proc.model.ImageProcContext;

/**
 * Created by Dmitry on 03.07.2017.
 */
public class ImageAccumulator extends ImageProcessor {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private volatile int accumulateFrames = 50;

    private volatile long framesCount = 0;

    volatile Mat acc;

    {
        processType = "ACCUMULATE_IMAGE";
    }

    @Override
    void processAsync(ImageProcContext.Action action, RailvideoEvent event) {
        double weight = 1.0/accumulateFrames;
        synchronized (this) {
            LOG.info("Accepted image " + event.frameN + "_" + event.captureId);
            Mat img = action.getImageData();
            framesCount++;

            if (null == acc) {
                acc = new Mat(img.rows(), img.cols(), CvType.CV_32FC1);
            }

            Imgproc.accumulateWeighted(img, acc, weight);

            if (framesCount > accumulateFrames) {
                ImageProcContext.Action newAction = newAction();

                Mat new_img = new Mat(img.rows(), img.cols(), CvType.CV_8UC1);
                acc.convertTo(new_img, CvType.CV_8UC1);
                newAction.putData("accumulation", new_img);
                showImageOnFrame(new_img, event);
                publishEvent(event, newAction);
            }
        }
    }

    @ManagedAttribute
    public int getAccumulateFrames() {
        return accumulateFrames;
    }

    @ManagedAttribute
    public void setAccumulateFrames(int accumulateFrames) {
        this.accumulateFrames = accumulateFrames;
    }
}
