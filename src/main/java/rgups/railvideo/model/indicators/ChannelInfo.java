package rgups.railvideo.model.indicators;

import java.util.List;
import java.util.Map;

/**
 * Created by Dmitry on 22.07.2017.
 */
public class ChannelInfo {

    String id;

    String sensorId;

    Map<String, Object> parameters;

    Map<Long, List<Double>> data;
}
