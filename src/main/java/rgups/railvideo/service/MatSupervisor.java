package rgups.railvideo.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import rgups.railvideo.proc.MatBearer;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Created by Dmitry on 16.08.2017.
 */
@Service
public class MatSupervisor implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(MatSupervisor.class);

    ApplicationContext applicationContext;

    static MatSupervisor defaultInstance;

    WeakHashMap<Mat, Long> matRefsMap = new WeakHashMap<>();

    public MatSupervisor() {
        defaultInstance = this;
    }

    public synchronized void releaseFreeMats() {
        Map<String, MatBearer> bearers = applicationContext.getBeansOfType(MatBearer.class);
        Set<Mat> matsToKeep = new HashSet<>();
        for (MatBearer bearer : bearers.values()){
            matsToKeep.addAll(bearer.getBearingMats());
        }
        LOG.info("Mats to keep: " + matsToKeep);

        Iterator<Mat> matIter = matRefsMap.keySet().iterator();
        while (matIter.hasNext()) {
            Mat matInMap = matIter.next();
            if( !matsToKeep.contains(matInMap)){
                LOG.info("Releasing mat: " + matInMap);
                matInMap.release();
                matIter.remove();
            }
        }
    }


    public synchronized void registerMat(Mat mat) {
        LOG.info("New mat registered: " + mat);
        matRefsMap.put(mat, mat.getNativeObjAddr());
    }

    public static void registerMatDefault(Mat mat){
        defaultInstance.registerMat(mat);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
