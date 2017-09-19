package rgups.railvideo.core.memdata;

import javafx.util.Pair;

import java.lang.reflect.ParameterizedType;
import java.util.*;

public class RankedTimeSeries<T> {

    public volatile RankedTsRecord<T> top;
    List<RankedTsRecord<T>> data = new ArrayList<>();

    Comparator<T> comp;

    private int bufSize = 256;

    private long bufPeriod = 1000 * 100;

    private long lastTs = 0;

    public static <T extends Comparable<T>> RankedTimeSeries maxFrom(long ts, T firstVal) {
        RankedTimeSeries<T> ret = new RankedTimeSeries<T>(firstVal.getClass());
        ret.addValue(ts, firstVal);
        return ret;
    }

    public <T extends Comparable<T>> RankedTimeSeries(Class<T> clazz) {
        this(clazz, false);
    }


    public <T extends Comparable<T>> RankedTimeSeries(Class<T> clazz, boolean isReverse){
        comp = new NaturalComparator();
        if (isReverse) {
            this.comp = comp.reversed();
        }
    }

    public RankedTimeSeries(Comparator<T> comp) {
        this(comp, true);
    }

    public RankedTimeSeries(Comparator<T> comp, boolean isReverse) {
        if (isReverse) {
            this.comp = comp.reversed();
        } else {
            this.comp = comp;
        }
    }

    public synchronized void addValue(long ts, T val){
        if (null == top) {
            top = new RankedTsRecord<T>(ts, val, comp);
            lastTs = ts;
            return;
        }

        if (lastTs < ts) {
            top = top.update(new RankedTsRecord<T>(ts, val, comp));
            lastTs = ts;
        } else{
            insertValue(ts, val);
        }

        if (ts - top.time > bufPeriod){
            dropExpired(ts);
        }

        if (top.listSize > bufSize) {
            dropBufferExceeded();
        }
    }

    public synchronized void insertValue(long ts, T val) {
        List<RankedTsRecord<T>> newList = new ArrayList<>(top.listSize + 5);
        newList.add(new RankedTsRecord<T>(ts, val, comp));

        RankedTsRecord<T> curRec = top;
        while (null != curRec) {
            newList.add(curRec);
            curRec = curRec.nextTop;
        }

        newList.sort(Comparator.comparing((RankedTsRecord<T> x) -> x.time).reversed());

        ListIterator<RankedTsRecord<T>> it = newList.listIterator();
        RankedTsRecord<T> prev = it.next();
        prev.listSize = 0;
        while(it.hasNext()){
            RankedTsRecord<T> cur = it.next();
            if (prev.isGE(cur)) {
                it.remove();
            } else{
                cur.nextTop = prev;
                cur.listSize = prev.listSize + 1;
                prev = cur;
            }
        }

        top = prev;
    }


    private void dropExpired(long curTime) {
        if (null == top){
            return;
        }

        while (curTime - top.time > bufPeriod) {
            top = top.nextTop;
        }
    }


    private void dropBufferExceeded() {
        if (null == top){
            return;
        }

        while (top.listSize > bufSize) {
            top = top.nextTop;
        }
    }

    public T getTopValue() {
        if (null == top){
            return null;
        }

        return top.val;
    }

    public T getTopValueSince(long startTime) {
        Pair<T, Long> pair = getTopPairSince(startTime);

        if(null == pair) {
            return null;
        }

        return pair.getKey();
    }


    public Pair<T, Long> getTopPair() {
        if (null == top){
            return null;
        }

        return new Pair(top.val, top.time);
    }


    public Pair<T, Long> getTopPairSince(long startTime) {
        RankedTsRecord<T> cur = top;
        if (null == cur){
            return null;
        }

        while((cur!=null)&&(cur.time < startTime)) {
            cur = cur.nextTop;
        }

        if(null == cur) {
            return null;
        }

        return new Pair(top.val, top.time);
    }


    class NaturalComparator<T extends Comparable<T>> implements Comparator<T> {
        public int compare(T a, T b) {
            return a.compareTo(b);
        }
    }

    public int getBufSize() {
        return bufSize;
    }

    public void setBufSize(int bufSize) {
        this.bufSize = bufSize;
    }

    public long getBufPeriod() {
        return bufPeriod;
    }

    public void setBufPeriod(long bufPeriod) {
        this.bufPeriod = bufPeriod;
    }
}
