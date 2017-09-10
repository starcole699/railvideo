package rgups.railvideo.core;

import org.junit.Test;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.FloatColumn;
import tech.tablesaw.api.Table;

public class TablesTest {

    @Test
    public void testOne() throws InterruptedException {
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
