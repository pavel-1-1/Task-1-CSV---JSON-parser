import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);
        list.forEach(System.out::println);

        String json = listToJson(list);
        System.out.println(json);

        writeString(json);

    }

    static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> allStaff = null;
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader).withMappingStrategy(strategy).build();
            allStaff = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert allStaff != null;
        allStaff.removeIf(n -> n.id == 0);
        return allStaff;
    }

    static String listToJson(List<Employee> list) {
        return new Gson().toJson(list);
    }

    static void writeString(String json) {
        try (FileWriter fileWriter = new FileWriter("new_data.json")) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
