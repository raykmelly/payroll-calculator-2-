package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PayrollCalculator {
    public static void main(String[] args) {
        // Attempt to read the "employees.csv" file and handle errors
        try (BufferedReader reader = new BufferedReader(new FileReader("employees.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Splits the line into readable chunks
                // id|name|hours-worked|pay-rate
                String[] split = line.split("\\|");

                // Use the information to get the values needed in the right type
                int id = Integer.parseInt(split[0]);
                String name = split[1];
                double hours = Double.parseDouble(split[2]);
                double payRate = Double.parseDouble(split[3]);

                // Create the new employee and add them to the list
                Employee employee = new Employee(id,name,hours,payRate);
                System.out.printf(
                        "| [ID]: %d | [NAME]: %s | [HOURS WORKED]: %.2f | [PAY RATE]: $%.2f%n",
                        id,name,hours,payRate
                );
            }
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Error: could not find the employee.csv file");
        } catch (IOException e) {
            System.out.println("Error: problem reading employee.csv file");
        }
    }
}