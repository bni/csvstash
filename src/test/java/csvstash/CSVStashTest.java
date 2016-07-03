package csvstash;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CSVStashTest {
    @Test
    public void generateCreateTableStatement() throws Exception {
        StashInfo stashInfo = new StashInfo("", "", "", "", "test");

        String[] line = {
            "col1",
            "col2",
            "col3"
        };

        assertEquals(
            "Statement was invalid",
            "CREATE TABLE IF NOT EXISTS test (col1 VARCHAR(255), col2 VARCHAR(255), col3 VARCHAR(255));",
            new CSVStash().generateCreateTableStatement(stashInfo, line));
    }

    @Test
    public void generateInsertRowStatement() throws Exception {
        StashInfo stashInfo = new StashInfo("", "", "", "", "test");

        String[] line = {
            "val1",
            "val2",
            "val3"
        };

        assertEquals(
            "Statement was invalid",
            "INSERT INTO test VALUES ('val1', 'val2', 'val3');",
            new CSVStash().generateInsertRowStatement(stashInfo, line));
    }
}
