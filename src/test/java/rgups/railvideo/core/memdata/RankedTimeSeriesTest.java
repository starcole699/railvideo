package rgups.railvideo.core.memdata;

import org.junit.Test;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.FloatColumn;
import tech.tablesaw.api.Table;

import java.util.Comparator;

public class RankedTimeSeriesTest {

    @Test
    public void testOne() throws InterruptedException {
        RankedTimeSeries<Double> ts1 = new RankedTimeSeries(Double.class);
        ts1.addValue(1000, 1.1);
        printTs(ts1);
        ts1.addValue(2000, 2.1);
        printTs(ts1);
        ts1.addValue(3000, 0.1);
        printTs(ts1);
        ts1.addValue(4000, 100.0);
        printTs(ts1);
        ts1.addValue(5000, 99.0);
        ts1.addValue(6000, 99.0);
        ts1.addValue(7000, 90.0);
        printTs(ts1);
        ts1.addValue(8000, 80.0);
        printTs(ts1);
        ts1.addValue(8000, 90.0);
        printTs(ts1);
    }

    @Test
    public void testWrongOrder() throws InterruptedException {
        RankedTimeSeries<Double> ts1 = new RankedTimeSeries(Double.class);
        ts1.addValue(1000, 1.1);
        printTs(ts1);
        ts1.addValue(2000, 2.1);
        printTs(ts1);
        ts1.addValue(3000, 0.1);
        printTs(ts1);
        ts1.addValue(4000, 100.0);
        printTs(ts1);
        ts1.addValue(5000, 99.0);
        ts1.addValue(6000, 99.0);
        ts1.addValue(7000, 90.0);
        printTs(ts1);
        ts1.addValue(8000, 80.0);
        printTs(ts1);
        ts1.addValue(8650, 200.0);
        printTs(ts1);
    }

    @Test
    public void testExpiration() throws InterruptedException {
        RankedTimeSeries<Double> ts1 = new RankedTimeSeries(Double.class);
        ts1.setBufPeriod(50);
        double val = 1000;
        long time = 0;
        for (int i=0; i<10; i++) {
            ts1.addValue(time += 10, val -= 1);
            printTs(ts1);
        }
    }

    public void printTs(RankedTimeSeries ts){
        RankedTsRecord top = ts.top;
        System.out.println("-------------------");
        while (null != top) {
            System.out.println(top);
            top = top.nextTop;
        }
    }
}
