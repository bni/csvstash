package csvstash;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import csvstash.config.StashConfig;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class CSVStash {
    private Map<String, String> headerColumnTypes;

    private void stash(StashConfig stashConfig) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(stashConfig.getCsvFile()));

        Connection conn = null;
        try {
            conn = getConnection(stashConfig);

            processLines(stashConfig, reader, conn);
        } catch (SQLException e) {
            System.out.println("Error getting connection: " + e.getMessage());
        } finally {
            if (conn != null) {try {conn.close();} catch (SQLException e) {e.getErrorCode();}}
        }
    }

    private void processLines(StashConfig stashConfig, CSVReader reader, Connection conn) throws IOException {
        String[] line;

        int i = 0;
        while ((line = reader.readNext()) != null) {
            if (i == 0) {
                createTable(stashConfig, line, conn);
            } else {
                insertRow(stashConfig, line, conn);
            }

            i++;
        }
    }

    private void createTable(StashConfig stashConfig, String[] line, Connection conn) {
        executeStatement(generateCreateTableStatement(stashConfig, line), conn);
    }

    String generateCreateTableStatement(StashConfig stashConfig, String[] header) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + stashConfig.getTable() + " (";

        headerColumnTypes = new LinkedHashMap<>();

        for (String columnName : header) {
            String columnType = stashConfig.getColumnTypes().get(columnName);

            if (columnType != null) {
                headerColumnTypes.put(columnName, columnType);
            } else {
                headerColumnTypes.put(columnName, StashConfig.DEFAULT_COLUMN_TYPE);
            }

            createTableStatement += columnName + " " + headerColumnTypes.get(columnName) + ", ";
        }

        return createTableStatement.replaceAll(", $", ");");
    }

    private void insertRow(StashConfig stashConfig, String[] line, Connection conn) {
        executeStatement(generateInsertRowStatement(stashConfig, line), conn);
    }

    String generateInsertRowStatement(StashConfig stashConfig, String[] line) {
        String insertStatement = "INSERT INTO " + stashConfig.getTable() + " (" +
            generateColumnSpecification() + ") VALUES (";

        insertStatement += generateValues(line);

        return insertStatement;
    }

    private String generateColumnSpecification() {
        String columnSpecification = "";
        for (Map.Entry<String, String> entry : headerColumnTypes.entrySet()) {
            columnSpecification += entry.getKey() + ", ";
        }
        columnSpecification = columnSpecification.replaceAll(", $", "");
        return columnSpecification;
    }

    private String generateValues(String[] line) {
        String values = "";

        int i = 0;
        for (String value : line) {
            String columnType = (String)headerColumnTypes.values().toArray()[i];

            String hyphen = "";
            if (columnType.contains("CHAR") || columnType.contains("DATE")) {
                hyphen = "'";
            }

            values += hyphen + value + hyphen + ", ";

            i++;
        }

        return values.replaceAll(", $", ");");
    }

    private Connection getConnection(StashConfig stashConfig) throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + stashConfig.getHost() +
            "/" + stashConfig.getDatabase() + "?" +
            "user=" + stashConfig.getUser() + "&" +
            "password=" + stashConfig.getPass() + "&" +
            "serverTimezone=UTC&" +
            "useSSL=false");
    }

    private void executeStatement(String statement, Connection conn) {
        Statement stmt = null;

        try {
            stmt = conn.createStatement();

            stmt.executeUpdate(statement);
        } catch (SQLException e) {
            System.out.println("Error executing statement: " + e.getMessage());
        } finally {
            if (stmt != null) {try {stmt.close();} catch (SQLException e) {e.getErrorCode();}}
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("usage: csvstash config.json sample.csv");

            return;
        }

        try {
            StashConfig stashConfig = new ObjectMapper().readValue(new File(args[0]), StashConfig.class);

            stashConfig.setCsvFile(args[1]);

            new CSVStash().stash(stashConfig);
        } catch (IOException e) {
            System.out.println("Error reading csv file: " + e.getMessage());
        }
    }
}
