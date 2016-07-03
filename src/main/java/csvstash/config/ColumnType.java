package csvstash.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ColumnType {
    @JsonProperty("columnName")
    private String columnName;

    @JsonProperty("columnType")
    private String columnType;

    @JsonProperty("equalsFilter")
    private String equalsFilter;

    // Default constructor required by Jackson
    public ColumnType() {}

    // Used from test
    public ColumnType(String columnName, String columnType, String equalsFilter) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.equalsFilter = equalsFilter;
    }

    String getColumnName() {
        return columnName;
    }

    String getColumnType() {
        return columnType;
    }

    String getEqualsFilter() {
        return equalsFilter;
    }
}
