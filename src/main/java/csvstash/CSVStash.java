package csvstash;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CSVStash {
    private void stash(StashInfo stashInfo) {
        try {
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
        } catch (IOException e) {
            System.out.println("Error reading csv file: " + e.getMessage());
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

    String generateCreateTableStatement(StashInfo stashInfo, String[] line) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + stashInfo.getTable() + " (";

        for (String columnName : line) {
            createTableStatement += columnName + " VARCHAR(255), ";
        }

        return createTableStatement.replaceAll(", $", ");");
    }

    private void insertRow(StashInfo stashInfo, String[] line, Connection conn) {
        executeStatement(generateInsertRowStatement(stashInfo, line), conn);
    }

    String generateInsertRowStatement(StashInfo stashInfo, String[] line) {
        String insertStatement = "INSERT INTO " + stashInfo.getTable() + " VALUES (";

        for (String value : line) {
            insertStatement += "'" + value + "', ";
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
        } catch (Exception e) {
            System.out.println("Error executing statement: " + e.getMessage());
        } finally {
            if (stmt != null) {try {stmt.close();} catch (SQLException e) {e.getErrorCode();}}
        }
    }

    // ./csvstash FL_insurance_sample.csv localhost test root root insurance
    public static void main(String[] args) {
        new CSVStash().stash(new StashInfo(args[0], args[1], args[2], args[3], args[4], args[5]));
    }
}
