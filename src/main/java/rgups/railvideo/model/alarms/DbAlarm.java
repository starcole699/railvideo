package rgups.railvideo.model.alarms;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name="DESCR", nullable=false)
    String descr;

    @Column(name="DETAILS", nullable=true)
    String details;


}
