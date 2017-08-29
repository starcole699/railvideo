package rgups.railvideo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rgups.railvideo.model.SdbChannelInfo;
import rgups.railvideo.model.SdbSensorInfo;

import java.util.List;

@Repository
public interface ChannelInfoRepo extends JpaRepository<SdbChannelInfo, Long> {

    List<SdbChannelInfo> findByName(String name);

    List<SdbChannelInfo> findByNameAndSensorInfo(String name, SdbSensorInfo sensorInfo);
}
