package rgups.railvideo.model;

import javax.persistence.*;

/**
 * Created by Dmitry on 22.07.2017.
 */
@Entity
@Table(name="SENSOR_ATTRS")
public class SdbSensorAttr {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="NAME")
    private String name;


}
