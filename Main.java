import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class Main
{
     private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	public static void main(String[] args) {
        String filePath = "your_file.csv"; //file path manually

        try {
            List<EmployeeRecord> records = readRecordsFromFile(filePath);

            // Iterate over records and check conditions
            for (int i = 0; i < records.size() - 1; i++) {
                EmployeeRecord currentRecord = records.get(i);
                EmployeeRecord nextRecord = records.get(i + 1);

                // Check conditions
                if (hasWorkedFor7ConsecutiveDays(currentRecord, nextRecord)) {
                    System.out.println(currentRecord.getName() + " has worked for 7 consecutive days. Position: " + currentRecord.getPosition());
                }

                if (hasLessThan10HoursBetweenShifts(currentRecord, nextRecord) && hasMoreThan1HourBetweenShifts(currentRecord, nextRecord)) {
                    System.out.println(currentRecord.getName() + " has less than 10 hours but greater than 1 hour between shifts. Position: " + currentRecord.getPosition());
                }

                if (hasWorkedMoreThan14Hours(currentRecord)) {
                    System.out.println(currentRecord.getName() + " has worked for more than 14 hours in a single shift. Position: " + currentRecord.getPosition());
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static List<EmployeeRecord> readRecordsFromFile(String filePath) throws IOException, ParseException {
        List<EmployeeRecord> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] record = line.split("\t");

                String positionID = record[0];
                String timeIn = record[2];
                String timeOut = record[3];
                String name = record[7];

                Date timeInDate = dateFormat.parse(timeIn);
                Date timeOutDate = dateFormat.parse(timeOut);

                records.add(new EmployeeRecord(positionID, timeInDate, timeOutDate, name));
            }
        }

        return records;
    }

    private static boolean hasWorkedFor7ConsecutiveDays(EmployeeRecord currentRecord, EmployeeRecord nextRecord) {
        long days = (nextRecord.getTimeIn().getTime() - currentRecord.getTimeOut().getTime()) / (1000 * 60 * 60 * 24);
        return days == 7;
    }

    private static boolean hasLessThan10HoursBetweenShifts(EmployeeRecord currentRecord, EmployeeRecord nextRecord) {
        long hoursBetween = (nextRecord.getTimeIn().getTime() - currentRecord.getTimeOut().getTime()) / (1000 * 60 * 60);
        return hoursBetween < 10;
    }

    private static boolean hasMoreThan1HourBetweenShifts(EmployeeRecord currentRecord, EmployeeRecord nextRecord) {
        long hoursBetween = (nextRecord.getTimeIn().getTime() - currentRecord.getTimeOut().getTime()) / (1000 * 60 * 60);
        return hoursBetween > 1;
    }

    private static boolean hasWorkedMoreThan14Hours(EmployeeRecord record) {
        long hoursWorked = (record.getTimeOut().getTime() - record.getTimeIn().getTime()) / (1000 * 60 * 60);
        return hoursWorked > 14;
    }

    private static class EmployeeRecord {
        private String positionID;
        private Date timeIn;
        private Date timeOut;
        private String name;

        public EmployeeRecord(String positionID, Date timeIn, Date timeOut, String name) {
            this.positionID = positionID;
            this.timeIn = timeIn;
            this.timeOut = timeOut;
            this.name = name;
        }

        public String getPosition() {
            return positionID;
        }

        public Date getTimeIn() {
            return timeIn;
        }

        public Date getTimeOut() {
            return timeOut;
        }

        public String getName() {
            return name;
        }
    }
}
