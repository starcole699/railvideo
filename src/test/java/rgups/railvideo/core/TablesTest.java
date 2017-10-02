package rgups.railvideo.core;

import org.junit.Test;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.FloatColumn;
import tech.tablesaw.api.Table;

import java.util.HashMap;
import java.util.Map;

public class TablesTest {

    @Test
    public void testOne() throws InterruptedException {
        Map<Integer, String> HOSTING = new HashMap<>();
        HOSTING.put(1, "linode.com");
        HOSTING.put(2, "heroku.com");
        HOSTING.put(3, "digitalocean.com");
        HOSTING.put(4, "aws.amazon.com");

        Table table = Table.create("t");
        FloatColumn floatColumn = new FloatColumn("f1");
        DoubleColumn doubleColumn = new DoubleColumn("d1");
        table.addColumn(floatColumn);
        table.addColumn(doubleColumn);
        floatColumn.append(1.1);
        floatColumn.append(2.2);
        doubleColumn.append(11.11);
        doubleColumn.append(22.22);

        System.out.println(table.printHtml());
    }
}
