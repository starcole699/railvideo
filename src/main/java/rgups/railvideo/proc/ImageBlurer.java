package rgups.railvideo.proc;

import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.stereotype.Component;
import rgups.railvideo.core.RvMat;
import rgups.railvideo.core.flow.RailvideoEvent;
import rgups.railvideo.proc.model.ImageProcContext;
import rgups.railvideo.proc.model.RvFlowProperty;

/**
 * Created by Dmitry on 03.07.2017.
 */
@Component
@DependsOn("nativeLibLoader")
@RvFlowItem
public class ImageBlurer extends ImageProcessor {

    @RvFlowProperty
    long blurSize = 7;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    {
        processType = "BLUR_IMAGE";
    }

    @Override
    void processAsync(ImageProcContext.Action action, RailvideoEvent event) {
        LOG.info("Accepted image " + event.frameN + "_" + event.captureId);
        RvMat new_img = new RvMat();
        RvMat img = action.getImageData();
        Imgproc.GaussianBlur(img, new_img, new Size(7, 7), -1);

        ImageProcContext.Action newAction = newAction();
        newAction.putData("blured", new_img);
        showImageOnFrame(new_img, event);
        publishEvent(event, newAction);
    }

    @ManagedAttribute
    public long getBlurSize() {
        return blurSize;
    }

    @ManagedAttribute
    public void setBlurSize(long blurSize) {
        this.blurSize = blurSize;
    }
}
