package rgups.railvideo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rgups.railvideo.model.SavedImage;
import rgups.railvideo.model.SdbSensorInfo;

import java.util.List;

@Repository
public interface ImagesRepo extends JpaRepository<SavedImage, Long> {

    List<SavedImage> findByFileName(String fileName);

    List<SavedImage> findByFileNameIn(List<String> fileNames);
}
