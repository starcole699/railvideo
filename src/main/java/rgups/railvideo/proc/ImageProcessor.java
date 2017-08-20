package rgups.railvideo.proc;

import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import rgups.railvideo.core.RvMat;
import rgups.railvideo.core.flow.RailvideoEvent;
import rgups.railvideo.proc.model.ImageProcContext;
import rgups.railvideo.proc.model.RvFlowProperty;
import rgups.railvideo.ui.ImageProcessorFrame;
import rgups.railvideo.ui.ProcessorFrameController;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Dmitry on 29.06.2017.
 */
@RvFlowItem
@ManagedResource
public class ImageProcessor implements ProcessorFrameController, MatBearer, BeanNameAware {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    TaskExecutor taskExecutor;

    String type = this.getClass().getSimpleName();

    /**
     * List of modification events this processor interested in.
     */
    @RvFlowProperty
    List<String> accepts = new ArrayList<>();

    @RvFlowProperty
    volatile String processType = "";

    @RvFlowProperty
    volatile String name = this.getClass().getSimpleName();

    ImageProcessorFrame imageProcessorFrame;

    AtomicLong cnt = new AtomicLong(0);

    RvMat currentImage;

    @Override
    public void activateFrame() {
        if (null==imageProcessorFrame) {
            imageProcessorFrame = new ImageProcessorFrame(this);
        }
    }

    @Override
    public void onFrameClosed() {
        imageProcessorFrame.dispose();
        imageProcessorFrame = null;
    }


    public void showImageOnFrame(RvMat img, RailvideoEvent event) {
        setCurrentImage(img);
        if (null != imageProcessorFrame) {
            imageProcessorFrame.updateImage(img, event);
        }
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

//    @EventListener(condition="this.getChangesToProcess(event).size() > 0")
    @EventListener
    public void acceptEvent(RailvideoEvent event) {
        ImageProcContext context = event.getContext();
        context.lock();
        try {
            List<ImageProcContext.Action> changes = getChangesToProcess(event);

            List<FutureTask<Boolean>> tasks = new ArrayList<>();
            for (ImageProcContext.Action change : changes) {
                change.addAcceptedBy(getProcName());
                FutureTask<Boolean> task = new FutureTask<>(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        try {
                            processAsync(change, event);
                            return true;
                        } catch (Exception e) {
                            LOG.error("Processing by " + getProcName() + " failed. ", e);
                            return false;
                        } finally {
                            event.decrementActionsCount();
                        }
                    }
                });
                tasks.add(task);
            }
            for (FutureTask<Boolean> task : tasks) {
                event.incrementActionsCount();
                taskExecutor.execute(task);
            }
        } finally {
            context.release();
        }
    }

    void processAsync(ImageProcContext.Action action, RailvideoEvent event) {

    }


    public boolean shouldProcess(RailvideoEvent event) {
        return getChangesToProcess(event).size() > 0;
    }


    List<ImageProcContext.Action> getChangesToProcess(RailvideoEvent event){
        ImageProcContext context = event.getContext();
        context.lock();
        List<ImageProcContext.Action> ret = new ArrayList<>();
        for (ImageProcContext.Action change : context.getActions()){
            if (accepts.contains(change.by + "->" + change.type) && !change.wasAcceptedBy(getProcName())) {
                ret.add(change);
            }
        }
        context.release();
        return ret;
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }

    @ManagedAttribute
    public String getProcName(){
        return name + "->" + processType;
    }


    protected RailvideoEvent newRailvideoEvent(){
        RailvideoEvent event = new RailvideoEvent(this, cnt.get(), "");
        return event;
    }

    protected ImageProcContext.Action newAction() {
        ImageProcContext.Action action = new ImageProcContext.Action(name, getProcessType());
        return action;
    }

    void publishEvent(RailvideoEvent event, ImageProcContext.Action change) {
        event.addChange(change);
        applicationEventPublisher.publishEvent(event);
    }

    @ManagedAttribute
    public List<String> getAccepts() {
        return accepts;
    }

    @ManagedAttribute
    public void setAccepts(List<String> accepts) {
        this.accepts = accepts;
    }

    public void addAccept(String accept) {
        accepts.add(accept);
    }

    @ManagedAttribute
    public String getName() {
        return name;
    }

    public RvMat getCurrentImage() {
        return currentImage;
    }

    void setCurrentImage(RvMat img){
        currentImage = img;
    }

    public String getType() {
        return type;
    }

    public static BufferedImage matToBufferedImage(RvMat matrix) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (matrix.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = matrix.channels() * matrix.cols() * matrix.rows();
        byte[] bufferBytes = new byte[bufferSize];

        matrix.get(0, 0, bufferBytes); // get all the pixels
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(bufferBytes, 0, targetPixels, 0, bufferBytes.length);
        return image;
    }

    @Override
    public List<Mat> getBearingMats() {
        ArrayList<Mat> ret = new ArrayList<Mat>();
        if (null != currentImage) {
            ret.add(currentImage);
        }
        return ret;
    }
}
