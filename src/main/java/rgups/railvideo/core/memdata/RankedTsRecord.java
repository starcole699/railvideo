package rgups.railvideo.core.memdata;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RankedTsRecord<T> implements Comparable<RankedTsRecord<T>>{

    public long time;
    public T val;

    int listSize = 0;

    public RankedTsRecord<T> nextTop;

    Comparator<T> comp;

    public RankedTsRecord(long time, T val, Comparator<T> comp){
        this.time = time;
        this.val = val;
        this.comp = comp;
    }

    @Override
    public int compareTo(@NotNull RankedTsRecord<T> o) {
        return comp.compare(val, o.val);
    }

    public boolean isGt(@NotNull RankedTsRecord<T> o) {
        return compareTo(o) > 0;
    }

    public boolean isLt(@NotNull RankedTsRecord<T> o) {
        return compareTo(o) < 0;
    }

    public boolean isGE(@NotNull RankedTsRecord<T> o) {
        return compareTo(o) >= 0;
    }

    public boolean isLE(@NotNull RankedTsRecord<T> o) {
        return compareTo(o) <= 0;
    }

    public boolean isEq(@NotNull RankedTsRecord<T> o) {
        return compareTo(o) == 0;
    }

    public RankedTsRecord<T> update(RankedTsRecord<T> pretender){
        if (this.isLE(pretender)){
            return pretender;
        }

        if (null == nextTop) {
            nextTop = pretender;
            listSize = nextTop.listSize + 1;
            return this;
        }

        nextTop = nextTop.update(pretender);
        listSize = nextTop.listSize + 1;
        return this;
    }

    public void offerNextTop(RankedTsRecord<T> pretender){
        if ((null == nextTop)||(nextTop.isLE(pretender))){
            nextTop = pretender;
            return;
        }

        nextTop.offerNextTop(pretender);
    }

    @Override
    public String toString() {
        return "RankedTsRecord{" +
                "time=" + time +
                ", val=" + val +
                ", listSize=" + listSize +
                '}';
    }
}
