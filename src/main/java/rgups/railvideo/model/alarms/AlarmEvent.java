package rgups.railvideo.model.alarms;

import org.springframework.context.ApplicationEvent;

/**
 * Created by Dmitry on 28.07.2017.
 */
public class AlarmEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AlarmEvent(UiAlarm source) {
        super(source);
    }
}
