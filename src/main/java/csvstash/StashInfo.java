package csvstash;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

class StashInfo {
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

    // Default constructor required by Jackson
    StashInfo() {}

    // Used from test
    StashInfo(String host, String database, String user, String pass, String table) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.pass = pass;
        this.table = table;
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
}
