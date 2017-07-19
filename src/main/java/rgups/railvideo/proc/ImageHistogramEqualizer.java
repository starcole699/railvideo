package rgups.railvideo.proc;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rgups.railvideo.core.flow.RailvideoEvent;
import rgups.railvideo.proc.model.ImageProcContext;

/**
 * Created by Dmitry on 13.07.2017.
 */
public class ImageHistogramEqualizer extends ImageProcessor{
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    {
        processType = "HISTOGRAM_EQUALIZED";
    }

    @Override
    void processAsync(ImageProcContext.Action action, RailvideoEvent event) {
        LOG.info("Accepted image " + event.frameN + "_" + event.captureId);
        Mat new_img = new Mat();
        Mat img = action.getImageData();
        Imgproc.equalizeHist(img, new_img);

        ImageProcContext.Action newAction = newAction();
        newAction.putData("histogram", new_img);
        showImageOnFrame(new_img, event);
        publishEvent(event, newAction);
    }
}
