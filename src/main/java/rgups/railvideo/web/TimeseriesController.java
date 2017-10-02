package rgups.railvideo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rgups.railvideo.repositories.AlarmRepo;

@RestController
@RequestMapping("/ts")
public class TimeseriesController {

    @Autowired
    AlarmRepo alarmRepo;


}
