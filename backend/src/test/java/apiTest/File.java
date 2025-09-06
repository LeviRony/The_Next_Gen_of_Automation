package apiTest;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.io.*;

public class File {
    @Step
    public static void read(String[] args) {
        Allure.step("Reading from a file");
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file " + e.getMessage());
        }
    }
@Step
    public static void write(String[] args) {
    Allure.step("Writing to a file");
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
            bw.write("Hello, Java!");
            bw.newLine();
            bw.write("Second line");
        } catch (IOException e) {
            System.out.println("Error writing to file" + e.getMessage());
        }
    }
}
