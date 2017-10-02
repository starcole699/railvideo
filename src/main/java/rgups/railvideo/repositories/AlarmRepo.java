package rgups.railvideo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import rgups.railvideo.model.SdbChannelInfo;
import rgups.railvideo.model.alarms.DbAlarm;

import java.util.Date;
import java.util.List;

@Repository
@RepositoryRestResource(collectionResourceRel = "alarms", path = "alarms")
public interface AlarmRepo extends JpaRepository<DbAlarm, Long> {

    List<DbAlarm> findByTimeAfterOrderByTime(@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")@Param("time") Date time);
    List<DbAlarm> findByTimeIsGreaterThan(@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")@Param("time") Date time);
}
