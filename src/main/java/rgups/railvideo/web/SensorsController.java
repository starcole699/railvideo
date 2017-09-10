package rgups.railvideo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rgups.railvideo.model.indicators.FlatSensorData;
import rgups.railvideo.model.indicators.Statistics;
import rgups.railvideo.proc.sensors.SensorEvent;
import rgups.railvideo.service.SensorStatsService;

import java.util.List;
import java.util.Map;

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
    public @ResponseBody Map<?, ?> getSensorsStats() {
        return sensorStatsService.getSensorsStats();
    }


}
