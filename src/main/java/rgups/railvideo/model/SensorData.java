package rgups.railvideo.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Dmitry on 16.07.2017.
 */
@Entity
@Table(name="SENSOR_DATA")
public class SensorData {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="CAPTURE_TIME", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date captureTime;

    @Column(name="RECORD_TIME", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordTime;

    @Column(name="SENSOR_ID", nullable=false)
    private Integer sensorId;

    @Column(name="SENSOR_TYPE", nullable=false)
    private String sensorType;

    @Column(name="VAL1")
    private Double val1;

    @Column(name="VAL2")
    private Double val2;

    @Column(name="VAL3")
    private String val3;

    @Column(name="EXTRA")
    private String extra;
}
