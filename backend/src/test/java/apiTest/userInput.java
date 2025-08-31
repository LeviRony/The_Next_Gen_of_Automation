package apiTest;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utilities.UserInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import static org.testng.AssertJUnit.assertEquals;
import static utilities.UserInput.*;

public class userInput {

    @Test
    public static void inputReader() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String name = getInputWithTimeout(reader, "Enter your name: ", 5, "DefaultName");
        int age = Integer.parseInt(getInputWithTimeout(reader, "Enter your age: ", 5, "30"));
        System.out.println("Hello " + name + ", you are " + age + " years old.");
    }

    @Test
    @Parameters({"name", "age"})
    public void inputReader(@Optional("DefaultName") String name,
                            @Optional("30") String ageStr) {
        int age = Integer.parseInt(ageStr);
        System.out.println("Hello " + name + ", you are " + age + " years old.");
    }

    @Test
    public void usesProvidedInput() {
        BufferedReader fake = new BufferedReader(new StringReader("Alice\n"));
        String name = UserInput.getInputWithTimeout(fake, "Enter your name: ", 5, "Default");
        assertEquals(name, "Alice");
    }

    @Test
    public void fallsBackOnTimeout() {
        // No input provided -> will timeout and return default
        BufferedReader fake = new BufferedReader(new StringReader(""));
        String name = UserInput.getInputWithTimeout(fake, "Enter your name: ", 0, "Default");
        assertEquals(name, "Default");
    }
}
