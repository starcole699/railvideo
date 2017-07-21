package rgups.railvideo.proc.sensors;

import org.springframework.context.ApplicationEvent;
import rgups.railvideo.model.indicators.FlatSensorData;

/**
 * Created by Dmitry on 20.07.2017.
 */
public class SensorEvent  extends ApplicationEvent {


    final FlatSensorData data;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public SensorEvent(Object source, FlatSensorData data) {
        super(source);
        this.data = data;
    }

    public FlatSensorData getData() {
        return data;
    }
}
