package rgups.railvideo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rgups.railvideo.model.indicators.Statistics;

import java.util.Map;

/**
 * Created by Dmitry on 19.07.2017.
 */
@RestController
@RequestMapping("/sensors")
public class SensorsController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/stats",
            method = RequestMethod.POST
    //        produces = MediaType.APPLICATION_JSON_VALUE,
    //        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> addNewWorker(@RequestBody Statistics statistics) {
        LOG.info("Accepted statistics: " + statistics);
        return new ResponseEntity<>("Thanks", HttpStatus.OK);
    }
}
