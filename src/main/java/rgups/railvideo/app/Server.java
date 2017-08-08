package rgups.railvideo.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import rgups.railvideo.system.NativeLibLoader;
import rgups.railvideo.web.MvcConfig;

import java.util.Arrays;
import java.util.concurrent.Executor;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

/**
 * Created by Dmitry on 29.06.2017.
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableMBeanExport
@ComponentScan({"rgups.railvideo.web",
                "rgups.railvideo.service"})
@Import({/*DefaultFlowConfig.class,*/ JpaConfiguration.class, MvcConfig.class})
@ImportResource({"classpath:/config/sensors_processors.groovy",
                 "classpath:/config/flow_config.groovy"
                })
public class Server {

    public static void main(String...args){
        System.out.println("\n\n hello from server \n\n");
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                //System.out.println(beanName);
            }

        };
    }

    @Bean
    public NativeLibLoader nativeLibLoader() {
        return new NativeLibLoader();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("railvideo-");
        executor.initialize();
        return executor;
    }

}
