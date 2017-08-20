package rgups.railvideo.proc;

import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import rgups.railvideo.core.RvMat;
import rgups.railvideo.core.flow.RailvideoEvent;
import rgups.railvideo.proc.model.ImageProcContext;
import rgups.railvideo.proc.model.RvFlowProperty;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by Dmitry on 29.06.2017.
 */
@Component
@RvFlowItem
@ManagedResource
public class ImageSaver extends ImageProcessor {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @RvFlowProperty
    String savePath = "";

    @RvFlowProperty
    long timeInterval = 10000;

    long lastSaveTime = 0;

    @RvFlowProperty
    String format = "jpeg";

    @Override
    void processAsync(ImageProcContext.Action action, RailvideoEvent event) {
        LOG.info("Accepted image " + event.frameN + "_" + event.captureId);

        long time = System.currentTimeMillis();
        if (time - lastSaveTime < timeInterval) {
            return;
        }
        lastSaveTime = time;
        RvMat img = action.getImageData();

        showImageOnFrame(img, event);

        String path = prepareImagePath(event);

        LOG.info("Saving frame to: " + path);
        Imgcodecs.imwrite(path, img);
    }


    private String prepareImagePath(RailvideoEvent event) {
        File sd = new File(savePath);
        boolean success = true;
        if (!sd.exists()) {
            if (sd.mkdirs()) {
                LOG.info("Directory created: " + sd.getAbsolutePath());
            } else {
                LOG.error("Can't create folder: " + sd.getAbsolutePath());
            }
        }

        String name = event.getCaptureId() + "_raw." + format;

        return new File(sd, name).getAbsolutePath();
    }

    @ManagedAttribute
    public String getSavePath() {
        return savePath;
    }

    @ManagedAttribute
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    @ManagedAttribute
    public long getTimeInterval() {
        return timeInterval;
    }

    @ManagedAttribute
    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    @ManagedAttribute
    public String getFormat() {
        return format;
    }

    @ManagedAttribute
    public void setFormat(String format) {
        this.format = format;
    }
}
