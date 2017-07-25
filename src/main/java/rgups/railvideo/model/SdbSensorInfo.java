package rgups.railvideo.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry on 22.07.2017.
 */
@Entity
@Table(name="SENSOR_INFO")
public class SdbSensorInfo {

    @Id
    @Column(name="NAME")
    private String name;

    @Column(name="DESCR", nullable=true)
    private String des;

    @Column(name="TYPE", nullable=true)
    private String type;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "SENSOR_NAME")
    private List<SdbSensorAttr> attrs = new ArrayList<>();


}
