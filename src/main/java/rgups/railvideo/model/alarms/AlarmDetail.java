package rgups.railvideo.model.alarms;

/**
 * Created by Dmitry on 28.07.2017.
 */
public class AlarmDetail {

    String type;

    String name;

    String text;

    String data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AlarmDetail{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
