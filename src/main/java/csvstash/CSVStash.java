package csvstash;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;

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
    private Map<String, String> completeColumnTypes;

    private void stash(StashInfo stashInfo) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(stashInfo.getCsvFile()));

        Connection conn = null;
        try {
            conn = getConnection(stashInfo);

            processLines(stashInfo, reader, conn);
        } catch (SQLException e) {
            System.out.println("Error getting connection: " + e.getMessage());
        } finally {
            if (conn != null) {try {conn.close();} catch (SQLException e) {e.getErrorCode();}}
        }
    }

    private void processLines(StashInfo stashInfo, CSVReader reader, Connection conn) throws IOException {
        String[] line;

        int i = 0;

        while ((line = reader.readNext()) != null) {
            if (i == 0) {
                createTable(stashInfo, line, conn);
            } else {
                insertRow(stashInfo, line, conn);
            }

            i++;
        }
    }

    private void createTable(StashInfo stashInfo, String[] line, Connection conn) {
        executeStatement(generateCreateTableStatement(stashInfo, line), conn);
    }

    String generateCreateTableStatement(StashInfo stashInfo, String[] header) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + stashInfo.getTable() + " (";

        completeColumnTypes = new LinkedHashMap<>();

        for (String columnName : header) {
            String columnType = stashInfo.getColumnTypes().get(columnName);

            if (columnType != null) {
                completeColumnTypes.put(columnName, columnType);
            } else {
                completeColumnTypes.put(columnName, StashInfo.DEFAULT_COLUMN_TYPE);
            }

            createTableStatement += columnName + " " + completeColumnTypes.get(columnName) + ", ";
        }

        return createTableStatement.replaceAll(", $", ");");
    }

    private void insertRow(StashInfo stashInfo, String[] line, Connection conn) {
        executeStatement(generateInsertRowStatement(stashInfo, line), conn);
    }

    String generateInsertRowStatement(StashInfo stashInfo, String[] line) {
        String columnSpecification = "";
        for (Map.Entry<String, String> entry : completeColumnTypes.entrySet()) {
            columnSpecification += entry.getKey() + ", ";
        }
        columnSpecification = columnSpecification.replaceAll(", $", "");

        String insertStatement = "INSERT INTO " + stashInfo.getTable() + " (" + columnSpecification + ") VALUES (";

        int i = 0;
        for (String value : line) {
            String columnType = (String)completeColumnTypes.values().toArray()[i];

            String hyphen = "";
            if (columnType.contains("CHAR") || columnType.contains("DATE")) {
                hyphen = "'";
            }

            insertStatement += hyphen + value + hyphen + ", ";

            i++;
        }

        return insertStatement.replaceAll(", $", ");");
    }

    private Connection getConnection(StashInfo stashInfo) throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + stashInfo.getHost() +
            "/" + stashInfo.getDatabase() + "?" +
            "user=" + stashInfo.getUser() + "&" +
            "password=" + stashInfo.getPass() + "&" +
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
            StashInfo stashInfo = new ObjectMapper().readValue(new File(args[0]), StashInfo.class);

            stashInfo.setCsvFile(args[1]);

            new CSVStash().stash(stashInfo);
        } catch (IOException e) {
            System.out.println("Error reading csv file: " + e.getMessage());
        }
    }
}
