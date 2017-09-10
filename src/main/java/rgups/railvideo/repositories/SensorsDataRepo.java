package rgups.railvideo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import rgups.railvideo.model.SdbSensorData;

/**
 * Created by Dmitry on 27.07.2017.
 */
@Repository
@RepositoryRestResource(collectionResourceRel = "sensors", path = "sensors")
public interface  SensorsDataRepo extends JpaRepository<SdbSensorData, Long> {
}
