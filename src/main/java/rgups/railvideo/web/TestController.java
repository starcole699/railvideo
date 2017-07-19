package rgups.railvideo.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class TestController {

    @RequestMapping("/rest")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}