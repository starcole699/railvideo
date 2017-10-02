package rgups.railvideo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rgups.railvideo.model.indicators.ChannelHeader;
import rgups.railvideo.model.indicators.ChannelValue;
import rgups.railvideo.model.indicators.FlatSensorData;
import rgups.railvideo.model.indicators.Statistics;
import rgups.railvideo.proc.sensors.SensorEvent;
import rgups.railvideo.service.SensorStatsService;

import java.util.*;

/**
 * Created by Dmitry on 19.07.2017.
 */
@RestController
@RequestMapping("/sensors")
public class SensorsController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SensorStatsService sensorStatsService;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @RequestMapping(value = "/stats",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> addSensorsReadings(@RequestBody Statistics statistics) {
        LOG.info("Accepted statistics: " + statistics);
        List<FlatSensorData> sensorsData = statistics.getFlatteredData();
        sensorStatsService.addData(sensorsData);
        for(FlatSensorData fsd : sensorsData){
            LOG.info(fsd.toString());
            SensorEvent evt = new SensorEvent(this, fsd);
            applicationEventPublisher.publishEvent(evt);
        }
        return new ResponseEntity<>("Thanks", HttpStatus.OK);
    }



    @RequestMapping(value = "/stats",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody Map<String, Object> getSensorsStats() {
        Map<ChannelHeader, SortedSet<ChannelValue>> stats = sensorStatsService.getSensorsStats();
        Map<String, Object> ret = new TreeMap<>();
        for(Map.Entry<ChannelHeader, SortedSet<ChannelValue>> entry : stats.entrySet()){
            ChannelHeader h = entry.getKey();
            SortedSet<ChannelValue> vals = entry.getValue();
            Map<String, Object> row = new HashMap();
            row.put("sensor_type", h.getSensorType());
            row.put("sensor_name", h.getSensorName());
            row.put("channel_name", h.getChannel());
            row.put("values", vals);
            ret.put(h.getSensorType() + "__" + h.getSensorName() + "__" + h.getChannel(), row);
        }
        return ret;
    }


}
