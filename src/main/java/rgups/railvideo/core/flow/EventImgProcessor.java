package rgups.railvideo.core.flow;

import rgups.railvideo.core.flow.msg.ImageEvent;

import java.util.EventListener;

/**
 * Created by Dmitry on 29.05.2017.
 */
public abstract class EventImgProcessor {

    public abstract void onImg(ImageEvent evt);

}
