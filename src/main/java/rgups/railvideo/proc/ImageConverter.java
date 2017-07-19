package rgups.railvideo.proc;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import rgups.railvideo.core.flow.RailvideoEvent;
import rgups.railvideo.proc.model.ImageProcContext;

/**
 * Created by Dmitry on 30.06.2017.
 */
@Component
@DependsOn("nativeLibLoader")
@RvFlowItem
public class ImageConverter extends ImageProcessor {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    {
        processType = "CONVERT_IMAGE";
    }

    @Override
    void processAsync(ImageProcContext.Action action, RailvideoEvent event) {
        LOG.info("Accepted image " + event.frameN + "_" + event.captureId);
        Mat new_img = new Mat();
        Mat img = action.getImageData();
        Imgproc.cvtColor(img, new_img, Imgproc.COLOR_BGR2GRAY);

        ImageProcContext.Action newAction = newAction();
        newAction.putData("grayscale", new_img);
        showImageOnFrame(new_img, event);
        publishEvent(event, newAction);
    }
}
