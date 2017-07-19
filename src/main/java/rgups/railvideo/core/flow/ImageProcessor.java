package rgups.railvideo.core.flow;

import org.opencv.core.Mat;

/**
 * Created by Dmitry on 22.05.2017.
 */
public interface ImageProcessor {

    Mat process(Mat img);
}
