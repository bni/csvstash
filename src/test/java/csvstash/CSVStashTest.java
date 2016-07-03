package csvstash;

import csvstash.config.ColumnType;
import csvstash.config.StashConfig;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVStashTest {
    private static final String[] INPUT_HEADER = {
        "col1",
        "col2",
        "col3",
        "col4"
    };

    private static final String[] INPUT_LINE = {
        "1",
        "TEST",
        "some medium long string",
        "3.14159"
    };

    private static final String EXPECTED_CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS test (" +
        "col1 INT NOT NULL, " +
        "col2 CHAR(4) NOT NULL, " +
        "col3 VARCHAR(255), " +
        "col4 DOUBLE);";

    private static final String EXPECTED_INSERT_STATEMENT = "INSERT INTO test (" +
        "col1, " +
        "col2, " +
        "col3, " +
        "col4" +
        ") VALUES (" +
        "1, " +
        "'TEST', " +
        "'some medium long string', " +
        "3.14159);";

    @Test
    public void generateStatements() throws Exception {
        CSVStash csvStash = new CSVStash();

        StashConfig stashConfig = new StashConfig("test", generateDummyColumnTypes());

        assertEquals(
            "Create table statement is not correct",
            EXPECTED_CREATE_TABLE_STATEMENT,
            csvStash.generateCreateTableStatement(stashConfig, INPUT_HEADER));

        assertEquals(
            "Insert statement is not correct",
            EXPECTED_INSERT_STATEMENT,
            csvStash.generateInsertRowStatement(stashConfig, INPUT_LINE));
    }

    private List<ColumnType> generateDummyColumnTypes() {
        // Intentionally leave out col3 here
        List<ColumnType> columnTypes = new ArrayList<>();
        columnTypes.add(new ColumnType("col1", "INT NOT NULL"));
        columnTypes.add(new ColumnType("col2", "CHAR(4) NOT NULL"));
        columnTypes.add(new ColumnType("col4", "DOUBLE"));

        return columnTypes;
    }
}
