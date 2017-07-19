package rgups.railvideo.core.flow.msg;

import java.util.EventListener;

/**
 * Created by Dmitry on 29.05.2017.
 */
public abstract class ImageEventListener {


    EventListener e;
    public abstract void onImage(ImageEvent img);
}
