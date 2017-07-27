package rgups.railvideo.model;

import javax.persistence.*;

/**
 * Created by Dmitry on 27.07.2017.
 */
@Entity
@Table(name="CHANNEL_INFO")
public class SdbChannelInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name="SENSOR_NAME", nullable=false)
    private String sensorName;

    @Column(name="DESCR")
    private String descr;

}
