package rgups.railvideo.service;

import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rgups.railvideo.proc.MatBearer;
import rgups.railvideo.proc.model.RvFlowProperty;
import rgups.railvideo.repositories.ImagesRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageStorageService implements MatBearer {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ImagesRepo imagesRepo;

    @RvFlowProperty
    String format = "jpeg";

    @Override
    public List<Mat> getBearingMats() {
        return new ArrayList<Mat>();
    }

}
