import rgups.railvideo.proc.ImageAccumulator
import rgups.railvideo.proc.ImageBlurer
import rgups.railvideo.proc.ImageCanny
import rgups.railvideo.proc.ImageConverter
import rgups.railvideo.proc.ImageHistogramEqualizer
import rgups.railvideo.proc.ImageSaver
import rgups.railvideo.proc.ImageSource

beans {

    rawImageSource(ImageSource, '${rv.video.path}'){
        capturePeriod = 5000L
    }

    rawImageSaver(ImageSaver){
        savePath = "C:\\railvideo\\frames_raw"
        timeInterval = 60000
        accepts = ["rawImageSource->NEW_IMAGE"]
    }

    rgbToGImageConverter(ImageConverter) {
        accepts = ["rawImageSource->NEW_IMAGE"]
    }

    grayRawImageSaver(ImageSaver){
        savePath = "C:\\railvideo\\frames_raw_gray"
        timeInterval = 60000
        accepts = ["rgbToGImageConverter->CONVERT_IMAGE"]
    }

    imageAccumulator(ImageAccumulator) {
         accepts = ["rgbToGImageConverter->CONVERT_IMAGE"]
        accumulateFrames = 50
    }

    imageHistogramEqualizer(ImageHistogramEqualizer) {
        accepts = ["imageAccumulator->ACCUMULATE_IMAGE"]
    }

    imageBlurer(ImageBlurer) {
        accepts = ["imageHistogramEqualizer->HISTOGRAM_EQUALIZED"]
    }

    imageCanny(ImageCanny) {
        accepts = ["imageBlurer->BLUR_IMAGE"]
    }

    alarmImageSaver(ImageSaver){
        savePath = '${rv.alarm.frame_save_path}'
        timeInterval = -1
        accepts = ["alarm->NEW_ALARM_FRAME"]
    }
}