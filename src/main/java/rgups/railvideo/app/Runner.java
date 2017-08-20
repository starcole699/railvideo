package rgups.railvideo.app;

import org.opencv.core.Mat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import rgups.railvideo.core.flow.FramesAvgImageProcessor;
import java.lang.reflect.Proxy;

/**
 * Created by Dmitry on 08.06.2017.
 */
public class Runner {

    public static void main(String[] args) {
        ApplicationContext app = new SpringApplicationBuilder(Server.class).headless(false).run(args);
    }

}
