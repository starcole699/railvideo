package rgups.railvideo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import rgups.railvideo.model.SdbChannelInfo;
import rgups.railvideo.model.alarms.DbAlarm;

@Repository
@RepositoryRestResource(collectionResourceRel = "alarms", path = "alarms")
public interface AlarmRepo extends JpaRepository<DbAlarm, Long> {

}
