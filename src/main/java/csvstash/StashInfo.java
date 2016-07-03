package csvstash;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class StashInfo {
    static final String DEFAULT_COLUMN_TYPE = "VARCHAR(255)";

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

    // Default constructor required by Jackson
    StashInfo() {}

    // Used from test
    StashInfo(String table, List<ColumnType> columnTypes) {
        this.table = table;
        this.columnTypes = columnTypes;
    }

    String getCsvFile() {
        return csvFile;
    }

    void setCsvFile(String csvFile) {
        this.csvFile = csvFile;
    }

    String getHost() {
        return host;
    }

    String getDatabase() {
        return database;
    }

    String getUser() {
        return user;
    }

    String getPass() {
        return pass;
    }

    String getTable() {
        return table;
    }

    Map<String, String> getColumnTypes() {
        return columnTypes.stream().collect(
            Collectors.toMap(ColumnType::getColumnName, ColumnType::getColumnType)
        );
    }
}
