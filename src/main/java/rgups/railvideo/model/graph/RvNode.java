package rgups.railvideo.model.graph;

/**
 * Created by Dmitry on 05.07.2017.
 */
public class RvNode {
    public String id;
    public String label;
    public String arrows = "to";

    public RvNode(String id, String label) {
        this.id = id;
        this.label = label;
    }
}
