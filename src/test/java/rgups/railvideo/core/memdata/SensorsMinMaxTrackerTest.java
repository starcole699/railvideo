package rgups.railvideo.core.memdata;

import org.junit.Test;

public class SensorsMinMaxTrackerTest {

    @Test
    public void testOne() {
        SensorsMinMaxTracker<Float> tracker = new SensorsMinMaxTracker(Float.class);
        tracker.addValue("s1", 1000, 10.1f);
        tracker.addValue("s1", 1100, 3.1f);
        tracker.addValue("s1", 1200, 5.1f);
        tracker.addValue("s2", 1300, 18.1f);
        tracker.addValue("s1", 1400, 1.1f);
        Object o = tracker.getMaxDiff("s1");
        System.out.println(o);
    }
}
