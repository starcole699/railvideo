package rgups.railvideo.service;

import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rgups.railvideo.proc.ImageProcessor;
import rgups.railvideo.proc.RvFlowItem;
import rgups.railvideo.proc.model.RvFlowItemDefinition;
import rgups.railvideo.proc.model.RvFlowProperty;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dmitry on 30.06.2017.
 */
@Service
public class RvItemsService {

    @Autowired
    ConfigurableApplicationContext ctx;

    public List<RvFlowItemDefinition> getFlowItemsDefinitions() {
        Map<String,Object> beans = ctx.getBeansWithAnnotation(RvFlowItem.class);
        List<RvFlowItemDefinition> ret = new ArrayList<>();
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            RvFlowItemDefinition def = new RvFlowItemDefinition();
            Object o = entry.getValue();
            def.id = entry.getKey();
            def.className = o.getClass().getName();
            Map<String, Object> attrs = new HashMap<>();
            for(Field f: o.getClass().getDeclaredFields()){
                if (f.isAnnotationPresent(RvFlowProperty.class)) {
                    String pk = f.getName();
                    f.setAccessible(true);
                    try {
                        Object pv = f.get(o);
                        attrs.put(pk, pv);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            def.properties = attrs;
            ret.add(def);
        }
        return ret;
    }

    public Map<String,Object> getFlowItems() {
        return ctx.getBeansWithAnnotation(RvFlowItem.class);
    }

    /**
     * Shows Image frame of ImageProcessor bean with specified bean id
     * @param id
     * @return
     */
    public boolean showFrameIfPossible(String id) {
        Object bean = ctx.getBean(id);
        if (null == bean) {
            return false;
        }

        if (bean instanceof ImageProcessor) {
            ((ImageProcessor)bean).activateFrame();
            return true;
        }
        return false;
    }

    public BufferedImage getCurrentImage(String id) {
        Object bean = ctx.getBean(id);
        if (null == bean) {
            throw new IllegalArgumentException("No bean named: " + id);
        }

        if (bean instanceof ImageProcessor) {
            Mat imgMat = ((ImageProcessor)bean).getCurrentImage();
            if (null == imgMat) {
                throw new IllegalStateException(id + " bean has no image to show.");
            }
            return ImageProcessor.matToBufferedImage(imgMat);
        } else {
            throw new IllegalArgumentException("Bean named: " + id + " isn't an ImageProcessor.");
        }
    }
}
