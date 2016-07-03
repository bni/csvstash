package csvstash;

import com.fasterxml.jackson.annotation.JsonProperty;

class ColumnType {
    @JsonProperty("columnName")
    private String columnName;

    @JsonProperty("columnType")
    private String columnType;

    // Default constructor required by Jackson
    ColumnType() {}

    ColumnType(String columnName, String columnType) {
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
