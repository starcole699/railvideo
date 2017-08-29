package rgups.railvideo.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;
import rgups.railvideo.proc.*;
import rgups.railvideo.proc.model.RvFlowProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry on 02.07.2017.
 */
@EnableMBeanExport
public class DefaultFlowConfig {

    @Bean
    public ImageSource rawImageSource(@Value("${rv.video.path}") String path) {
        ImageSource ret = new ImageSource(path);
        return ret;
    }

    @Bean
    public ImageSaver rawImageSaver(){
        ImageSaver ret = new ImageSaver();
        ret.setSavePath("C:\\railvideo\\frames_raw");
        ret.setTimeInterval(60000);
        ret.addAccept("rawImageSource->NEW_IMAGE");
        return ret;
    }

    @Bean
    public ImageConverter rgbToGImageConverter() {
        ImageConverter ret = new ImageConverter();
        ret.addAccept("rawImageSource->NEW_IMAGE");
        return ret;
    }

    @Bean
    public ImageSaver grayRawImageSaver(){
        ImageSaver ret = new ImageSaver();
        ret.setSavePath("C:\\railvideo\\frames_raw_gray");
        ret.setTimeInterval(60000);
        ret.addAccept("rgbToGImageConverter->CONVERT_IMAGE");
        return ret;
    }

    @Bean
    public ImageAccumulator imageAccumulator() {
        ImageAccumulator ret = new ImageAccumulator();
        ret.addAccept("rgbToGImageConverter->CONVERT_IMAGE");
        return ret;
    }

    @Bean
    public ImageHistogramEqualizer imageHistogramEqualizer() {
        ImageHistogramEqualizer ret = new ImageHistogramEqualizer();
        ret.addAccept("imageAccumulator->ACCUMULATE_IMAGE");
        return ret;
    }

    @Bean
    public ImageBlurer imageBlurer() {
        ImageBlurer ret = new ImageBlurer();
        ret.addAccept("imageHistogramEqualizer->HISTOGRAM_EQUALIZED");
        return ret;
    }

    @Bean
    public ImageCanny imageCanny() {
        ImageCanny ret = new ImageCanny();
        ret.addAccept("imageBlurer->BLUR_IMAGE");
        return ret;
    }

    @Bean
    public ImageSaver alarmImageSaver(@Value("${rv.alarm.frame_save_path:alarms}") String path){
        ImageSaver ret = new ImageSaver();
        ret.setSavePath(path);
        ret.setTimeInterval(-1);
        ret.addAccept("alarm->NEW_ALARM_FRAME");
        return ret;
    }

}
