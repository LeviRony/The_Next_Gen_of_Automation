package apiTest;

import io.qameta.allure.Step;

import java.io.*;

public class File {
    @Step
    public static void read(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("שגיאה בקריאת קובץ: " + e.getMessage());
        }
    }
@Step
    public static void write(String[] args) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
            bw.write("Hello, Java!");
            bw.newLine();
            bw.write("Second line");
        } catch (IOException e) {
            System.out.println("שגיאה בכתיבת קובץ: " + e.getMessage());
        }
    }
}
