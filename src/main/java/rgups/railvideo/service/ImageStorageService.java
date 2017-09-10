package rgups.railvideo.service;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rgups.railvideo.model.SavedImage;
import rgups.railvideo.proc.MatBearer;
import rgups.railvideo.proc.model.RvFlowProperty;
import rgups.railvideo.repositories.ImagesRepo;
import rgups.railvideo.utils.RvRuntimeException;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ImageStorageService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ImagesRepo imagesRepo;

    @RvFlowProperty
    String defaultFormat = "jpeg";

    @Value("${rv.image_storage_root}")
    String saveRootPath;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Cacheable(sync = true, cacheNames="storage_service_save_image",
            key="#path + \"_\" + #captureId + #postfix + #saveFormat + #captureTime")
    public synchronized SavedImage saveOrFindMat(Mat mat, String path, String captureId, String postfix, String saveFormat, Long captureTime) {
        String relPath = composeDbFileName(path, captureId, postfix);
        List<SavedImage> existingImg = imagesRepo.findByFileName(relPath);
        if (existingImg.size() > 0) {
            return existingImg.get(0);
        }

        String format = saveFormat;
        if ((null == saveFormat) || (saveFormat.trim().length() == 0)) {
            format = defaultFormat;
        } else {
            format = saveFormat.trim();
        }

        format = format.toLowerCase();

        SavedImage sImg = new SavedImage();
        sImg.setFileName(relPath);
        sImg.setExtension(format);
        sImg.setCaptureTime(new Date(captureTime));

        File fullPath = new File(saveRootPath, relPath + "." + format);

        saveMat(mat, fullPath.getAbsolutePath());

        imagesRepo.saveAndFlush(sImg);

        return sImg;
    }

    public String composeDbFileName(String path, String captureId, String postfix) {
        File file = new File(path, captureId + postfix);
        return file.getPath();
    }

    public void saveMat(Mat mat, String path) {
        File folder = new File(path).getParentFile();

        if (!folder.exists()) {
            if (folder.mkdirs()) {
                LOG.info("Directory created: " + folder.getAbsolutePath());
            } else {
                throw new RvRuntimeException("Can't create folder: " + folder.getAbsolutePath());
            }
        }

        LOG.info("Saving frame to: " + path);
        Imgcodecs.imwrite(path, mat);
    }
}
