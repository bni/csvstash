package csvstash;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;

public class CSVStash {
    private void parse(String csvFileName) {
        try {
            CSVReader reader = new CSVReader(new FileReader(csvFileName));
            String [] nextLine;

            int i = 0;
            while ((nextLine = reader.readNext()) != null) {
                System.out.println(nextLine[0] + "," + nextLine[1] + "," + nextLine[2]);

                if (i > 2) {
                    return;
                }

                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CSVStash().parse(args[0]);
    }
}
