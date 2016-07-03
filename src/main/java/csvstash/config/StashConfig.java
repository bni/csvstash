package csvstash.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StashConfig {
    public static final String DEFAULT_COLUMN_TYPE = "VARCHAR(255)";

    @JsonIgnore
    private String csvFile;

    @JsonProperty("host")
    private String host;

    @JsonProperty("database")
    private String database;

    @JsonProperty("user")
    private String user;

    @JsonProperty("pass")
    private String pass;

    @JsonProperty("table")
    private String table;

    @JsonProperty("columnTypes")
    private List<ColumnType> columnTypes;

    @JsonProperty("purgeCriteria")
    private String purgeCriteria;

    // Default constructor required by Jackson
    public StashConfig() {}

    // Used from test
    public StashConfig(String table, List<ColumnType> columnTypes) {
        this.table = table;
        this.columnTypes = columnTypes;
    }

    public String getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(String csvFile) {
        this.csvFile = csvFile;
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getTable() {
        return table;
    }

    public Map<String, String> getColumnTypes() {
        return columnTypes.stream().collect(
            Collectors.toMap(ColumnType::getColumnName, ColumnType::getColumnType)
        );
    }

    public String getPurgeCriteria() {
        return purgeCriteria;
    }
}
