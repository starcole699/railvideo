package rgups.railvideo.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Dmitry on 16.07.2017.
 */
@Entity
@Table(name="NOTIFICATION")
public class Notification {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="CAPTURE_TIME", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date captureTime;

    @Column(name="RECORD_TIME", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordTime;

    @Column(name="LEVEL", nullable=false)
    private Integer level;

    @Column(name="SOURCE_TYPE", nullable=false)
    private Integer sourceType;


}
