package rgups.railvideo.proc;

import org.opencv.core.CvType;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rgups.railvideo.core.RvMat;
import rgups.railvideo.core.flow.RailvideoEvent;
import rgups.railvideo.proc.model.ImageProcContext;
import rgups.railvideo.proc.model.RvFlowProperty;
import rgups.railvideo.service.MatSupervisor;
import rgups.railvideo.system.NativeLibLoader;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Dmitry on 29.06.2017.
 */
@Component
@DependsOn("nativeLibLoader")
@RvFlowItem
public class ImageSource extends ImageProcessor {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NativeLibLoader nativeLibLoader;

    @Autowired
    MatSupervisor matSupervisor;

    VideoCapture capture;

    @RvFlowProperty
    String path;

    @RvFlowProperty
    String captureType = "raw";

    @RvFlowProperty
    Long capturePeriod = 10L;

    @RvFlowProperty
    @Value("${rv.timezone:Europe/Moscow}")
    String timezone;

    RvMat captureImg;

    {
        processType = "NEW_IMAGE";
    }

    public ImageSource(String path) {
        this.path = path;
    }

    //@PostConstruct
    public void open() {
        LOG.info("Initializing video capture from: " + path);
        capture = new VideoCapture(path);

    }

    @Override
    public void acceptEvent(RailvideoEvent event) {
        // Image source doesn't process anything.
    }


    void flushBuffer() {
        double fps = capture.get(Videoio.CAP_PROP_FPS);
        if (0 == fps) {
            LOG.warn("Fps value is " + fps + ". Will skip buffer flush.");
            return;
        }
        LOG.info("===== Flushing buffer. Fps :  " + fps);
        double minDelayNano = 1000000000.0/fps;
        long prevT, curT, grabbed = 0;
        do {
            grabbed++;
            prevT = System.nanoTime();
            capture.grab();
            curT = System.nanoTime();
        } while (curT - prevT < minDelayNano);
        LOG.info("Buffer flushed. " + grabbed + " frames grabbed.");
    }

    @Scheduled(fixedDelay = 10, initialDelay = 500)
    public void capture() {
        try {
            Thread.sleep(capturePeriod);
        } catch (InterruptedException e) {
            LOG.error("Interrupted ", e);
        }

        if (null == capture) {
            open();
            flushBuffer();
        }

        if (null == captureImg) {
            int w = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
            int h = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
            captureImg = new RvMat(h, w, CvType.CV_8UC3);
        }

        if (capture.isOpened()) {
            long t = System.currentTimeMillis();
            capture.grab();
            LOG.info("  !!  Grab time " + (System.currentTimeMillis() - t));
            capture.retrieve(captureImg);
            if (!captureImg.empty()) {
                cnt.incrementAndGet();
                LOG.info("Captured image " + cnt + " size:" + captureImg.width() + "*" + captureImg.height());
                RailvideoEvent event = newRailvideoEvent();
                event.setCaptureId(generateCaptureId());

                RvMat new_img = captureImg;
                //Mat new_img = new Mat();
                //Imgproc.resize(img, new_img, new Size(1920, 1080), 0, 0, Imgproc.INTER_CUBIC);

                ImageProcContext.Action action = newAction();
                action.putData(RailvideoEvent.RAW_IMAGE, new_img);
                showImageOnFrame(new_img, event);
                publishEvent(event, action);
                event.waitForProcessFinish();
                matSupervisor.releaseFreeMats();
            } else {
                LOG.error("Captured image is empty.");
                capture.release();
                capture = null;
            }
        } else {
            LOG.error("Image source is not opened.");
        }
    }

    public String generateCaptureId() {
        TimeZone timezone = TimeZone.getTimeZone(this.timezone);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSSS");
        sdf.setTimeZone(timezone);
        long mills = System.currentTimeMillis();
        long nano = System.nanoTime();
        sdf.format(new Date(mills));
        return sdf.format(new Date(mills)) + "_" + nano;
    }

    public Long getCapturePeriod() {
        return capturePeriod;
    }

    public void setCapturePeriod(Long capturePeriod) {
        this.capturePeriod = capturePeriod;
    }
}
