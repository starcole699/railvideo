package rgups.railvideo.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="SAVED_IMAGE",
        indexes = {@Index(name = "SAVED_IMAGE_NAME_INDEX",  columnList="FILE_NAME")})
public class SavedImage {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="FILE_NAME")
    private String fileName;

    @Column(name="CAPTURE_TIME", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date captureTime;

}
