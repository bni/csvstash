package csvstash.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ColumnType {
    @JsonProperty("columnName")
    private String columnName;

    @JsonProperty("columnType")
    private String columnType;

    // Default constructor required by Jackson
    public ColumnType() {}

    public ColumnType(String columnName, String columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    String getColumnName() {
        return columnName;
    }

    String getColumnType() {
        return columnType;
    }
}
