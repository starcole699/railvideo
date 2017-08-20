package rgups.railvideo.proc.model;

import rgups.railvideo.core.RvMat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Dmitry on 30.06.2017.
 */
public class ImageProcContext {

    List<Action> actions = new ArrayList<>();

    List<Action> changes = new ArrayList<>();

    ReentrantLock lock = new ReentrantLock();

    Map<String, Object> data = new HashMap<>();

    public void lock(){
        lock.lock();
    }

    public void release(){
        lock.unlock();
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public List<Action> getChanges() {
        return changes;
    }

    public void setChanges(List<Action> changes) {
        this.changes = changes;
    }

    public void addChange(Action action) {
        lock();
        action.context = this;
        changes.add(action);
        addAction(action);
        release();
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public static class Action {
        private volatile ImageProcContext context;
        public final long when = System.currentTimeMillis();
        public final String by;
        public final String type;

        Map<String, Object> data = new HashMap<>();

        public final List<String> acceptedBy = new ArrayList<>();

        public Action(String by, String type){
            this.by = by;
            this.type = type;
        }

        public boolean wasAcceptedBy(String processorId){
            return acceptedBy.contains(processorId);
        }

        public void addAcceptedBy(String processorId) {
            if (wasAcceptedBy(processorId)){
                throw new IllegalStateException("Action");
            }
            acceptedBy.add(processorId);
        }


        public void putData(String key, Object value){
            data.put(key, value);
        }

        public Object getData(String key){
            return data.get(key);
        }

        @Override
        public String toString(){
            return "Action@" + by + "-" + type + "(" + context + ")";
        }

        public ImageProcContext getContext() {
            return context;
        }

        public void setContext(ImageProcContext context) {
            this.context = context;
        }

        public RvMat getImageData(){
            for (Object o : data.values()){
                if (o instanceof RvMat) {
                    return (RvMat)o;
                }
            }
            throw new IllegalStateException("No image data in action.");
        }
    }
}
