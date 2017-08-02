package rgups.railvideo.proc;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rgups.railvideo.core.flow.RailvideoEvent;
import rgups.railvideo.proc.model.ImageProcContext;
import rgups.railvideo.proc.model.RvFlowProperty;
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

    VideoCapture capture;

    @RvFlowProperty
    String path;

    @RvFlowProperty
    String captureType = "raw";

    @RvFlowProperty
    @Value("${rv.timezone:Europe/Moscow}")
    String timezone;

    {
        processType = "NEW_IMAGE";
    }

    public ImageSource(String path) {
        this.path = path;
    }

    @PostConstruct
    public void open() {
        LOG.info("Initializing video capture from: " + path);
        capture = new VideoCapture(path);
    }

    @Override
    public void acceptEvent(RailvideoEvent event) {
        // Image source doesn't process anything.
    }

    @Scheduled(fixedDelay = 300, initialDelay = 5000)
    public void capture() {
        Mat img = new Mat();

        if (capture.isOpened()) {
            capture.read(img);
            if (!img.empty()) {
                cnt.incrementAndGet();
                LOG.info("Captured image " + cnt + " size:" + img.width() + "*" + img.height());
                RailvideoEvent event = newRailvideoEvent();
                event.setCaptureId(generateCaptureId());

                Mat new_img = new Mat();
                Imgproc.resize(img, new_img, new Size(1920, 1080), 0, 0, Imgproc.INTER_CUBIC);

                ImageProcContext.Action action = newAction();
                action.putData(RailvideoEvent.RAW_IMAGE, new_img);
                showImageOnFrame(new_img, event);
                publishEvent(event, action);
                event.waitForProcessFinish();
            } else {
                LOG.error("Captured image is empty.");
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

}
