package rgups.railvideo.model.alarms;

import rgups.railvideo.model.SavedImage;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Dmitry on 27.07.2017.
 */
@Entity
@Table(name="ALARMS")
public class DbAlarm {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="TIME", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column(name="LEVEL", nullable=false)
    String level;

    @Column(name="TYPE", nullable=false)
    String type;

    @Column(name="DESCR")
    String descr;

    @Column(name="DETAILS", columnDefinition = "TEXT")
    String details;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinTable(name = "ALARM_IMAGES",
            joinColumns = @JoinColumn(name = "alarm_id"),
            inverseJoinColumns = @JoinColumn(name = "img_id")
    )
    List<SavedImage> images = new ArrayList<>();


}
