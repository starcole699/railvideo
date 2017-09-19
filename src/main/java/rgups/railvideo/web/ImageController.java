package rgups.railvideo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rgups.railvideo.model.SavedImage;
import rgups.railvideo.repositories.ImagesRepo;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/img")
public class ImageController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ImagesRepo imagesRepo;

    @Value("${rv.image_storage_root}")
    String saveRootPath;

    Map<String, String> mediaMap = new HashMap<>();
    {
        mediaMap.put("gif", MediaType.IMAGE_GIF_VALUE);
        mediaMap.put("jpg", MediaType.IMAGE_JPEG_VALUE);
        mediaMap.put("jpeg", MediaType.IMAGE_JPEG_VALUE);
        mediaMap.put("png", MediaType.IMAGE_PNG_VALUE);
    }

    @RequestMapping(value = "{id}",  method = RequestMethod.GET)
    public void ShowItemFrame(HttpServletResponse response, @PathVariable("id") String id) throws IOException {

        try {
            long img_id = Long.parseLong(id);
        } catch (NumberFormatException e) {
            LOG.error("Bad image id: ", e);
            response.setStatus(404);
            return;
        }

        SavedImage img = null;

        try {
            img = imagesRepo.getOne(Long.parseLong(id));
        } catch (EntityNotFoundException e) {
            LOG.error("Image query error: ", e);
        }

        if (null == img) {
            response.setStatus(404);
            return;
        }

        File file = new File(saveRootPath + File.separator + img.getFileName() + "." + img.getExtension());
        LOG.info("Returning image: " + img + "\n from parh: " + file.getAbsolutePath());
        if (!file.exists()) {
            LOG.error("File not found: " + file.getAbsolutePath());
            response.setStatus(404);
            return;
        }

        String mimeType = mediaMap.get(img.getExtension());
        if (null == mimeType) {
            mimeType= URLConnection.guessContentTypeFromName(file.getName());
        }

        if (null == mimeType) {
            LOG.error("Can't determine mime type for: " + img);
            response.setStatus(404);
            return;
        }

        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
        response.setContentLength((int)file.length());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
}
