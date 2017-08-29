package rgups.railvideo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rgups.railvideo.model.SdbSensorInfo;

import java.util.List;

@Repository
public interface SensorInfoRepo extends JpaRepository<SdbSensorInfo, Long> {

//    @Query("select b from Bank b where b.name = :name")
//    SdbSensorInfo findByName(@Param("name") String name);

    List<SdbSensorInfo> findByName(String name);

}
