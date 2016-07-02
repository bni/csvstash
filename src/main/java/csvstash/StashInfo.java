package csvstash;

class StashInfo {
    private String csvFile;
    private String host;
    private String database;
    private String user;
    private String pass;
    private String table;

    StashInfo(String csvFile, String host, String database,
            String user, String pass, String table) {
        this.csvFile = csvFile;
        this.host = host;
        this.database = database;
        this.user = user;
        this.pass = pass;
        this.table = table;
    }

    String getCsvFile() {
        return csvFile;
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
