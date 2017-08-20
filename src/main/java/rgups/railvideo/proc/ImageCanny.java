package rgups.railvideo.proc;

import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import rgups.railvideo.core.RvMat;
import rgups.railvideo.core.flow.RailvideoEvent;
import rgups.railvideo.proc.model.ImageProcContext;
import rgups.railvideo.proc.model.RvFlowProperty;

/**
 * Created by Dmitry on 03.07.2017.
 */
@ManagedResource(
        description="Canny Bean")
public class ImageCanny  extends ImageProcessor {

    @RvFlowProperty
    double threshold1 = 75;

    @RvFlowProperty
    double threshold2 = 120;

    @RvFlowProperty
    int apertureSize = 3;

    @RvFlowProperty
    boolean L2gradient = false;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    {
        processType = "CANNY_IMAGE";
    }

    @Override
    void processAsync(ImageProcContext.Action action, RailvideoEvent event) {
        LOG.info("Accepted image " + event.frameN + "_" + event.captureId);
        RvMat new_img = new RvMat();
        RvMat img = action.getImageData();
        Imgproc.Canny(img, new_img, threshold1, threshold2, apertureSize, L2gradient);

        ImageProcContext.Action newAction = newAction();
        newAction.putData("canny", new_img);
        showImageOnFrame(new_img, event);
        publishEvent(event, newAction);
    }

    @ManagedAttribute
    public boolean isL2gradient() {
        return L2gradient;
    }

    @ManagedAttribute
    public void setL2gradient(boolean l2gradient) {
        L2gradient = l2gradient;
    }

    @ManagedAttribute
    public int getApertureSize() {
        return apertureSize;
    }

    @ManagedAttribute
    public void setApertureSize(int apertureSize) {
        this.apertureSize = apertureSize;
    }

    @ManagedAttribute
    public double getThreshold2() {
        return threshold2;
    }

    @ManagedAttribute
    public void setThreshold2(double threshold2) {
        this.threshold2 = threshold2;
    }

    @ManagedAttribute
    public double getThreshold1() {
        return threshold1;
    }

    @ManagedAttribute
    public void setThreshold1(double threshold1) {
        this.threshold1 = threshold1;
    }
}