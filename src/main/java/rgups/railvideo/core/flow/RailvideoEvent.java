package rgups.railvideo.core.flow;

import org.springframework.context.ApplicationEvent;
import rgups.railvideo.proc.model.ImageProcContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Dmitry on 28.06.2017.
 */
public class RailvideoEvent extends ApplicationEvent {
    public static final String RAW_IMAGE = "raw_image";

    public volatile String type;

    public volatile long frameN = 0;

    public volatile String captureId;

    volatile ImageProcContext context = new ImageProcContext();

    volatile AtomicInteger actionsCount = new AtomicInteger();

    CountDownLatch actionsLatch = new CountDownLatch(0);

    final Phaser actionsPhaser = new Phaser(1);

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public RailvideoEvent(Object source) {
        super(source);
    }

    public RailvideoEvent(Object source, long frameN, String type) {
        super(source);
        this.frameN = frameN;
        this.type = type;
    }

    public String getCaptureId() {
        return captureId;
    }

    public void setCaptureId(String captureId) {
        this.captureId = captureId;
    }

    public ImageProcContext getContext() {
        return context;
    }

    public int incrementActionsCount() {
        return actionsPhaser.register();
    }

    public int decrementActionsCount() {
        return actionsPhaser.arrive();
    }

    public int getActiveActions() {
        return actionsCount.get();
    }

    public void addChange(ImageProcContext.Action change) {
        context.addChange(change);
    }

    public void waitForProcessFinish(){
        actionsPhaser.arriveAndAwaitAdvance();
    }
}
