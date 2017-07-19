package rgups.railvideo.core.flow;

import org.opencv.core.Mat;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Dmitry on 29.05.2017.
 */
public class FramesAvgImageProcessor implements ImageProcessor {

    int frames_num_to_avg = 30;

    @Override
    //@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
    public Mat process(Mat img) {
        return null;
    }

    public String processMessage(String payload) {
        return payload + " is the time.";
    }
}
