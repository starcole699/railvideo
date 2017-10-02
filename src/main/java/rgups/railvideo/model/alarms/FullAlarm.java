package rgups.railvideo.model.alarms;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.rest.core.config.Projection;
import rgups.railvideo.model.SavedImage;
import rgups.railvideo.model.alarms.DbAlarm;

import java.util.Date;
import java.util.List;

@Projection(name = "fullAlarm", types = { DbAlarm.class })
public interface FullAlarm {

    @JsonProperty("alarm_id")
    public Long getId();
    public Date getTime();
    public String getLevel();
    public String getType();
    public String getHeader();
    public String getDescr();
    public String getDetails();
    public List<SavedImage> getImages();
}
