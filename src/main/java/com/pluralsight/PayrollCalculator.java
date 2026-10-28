package com.pluralsight;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PayrollCalculator {
    public static void main(String[] args) {
        // Prompt the user for the name of a file to read and process, then prompt them for the
        // name of the payroll file to create.
        Scanner scanner = new Scanner(System.in);
        System.out.println("-> Enter the name of the file to process:");
        String processFile = scanner.nextLine();
        System.out.println("-> Enter the name of the file you want to create");
        System.out.println("-> Format: (ex. payroll-sept-2023.json)");
        String writingFile = scanner.nextLine();

        // Create a list of existing employees with employees.csv information
        List<Employee> employeeList = createEmployeeList(processFile);
        // Generate a payroll.json report
        createPayrollReport(employeeList,writingFile);
    }

    // Process the payroll. Creates a .csv file or rewrites it based off of
    // employee information
    public static List<Employee> createEmployeeList(String fileName) {
        // Scan the file name create employee list
        List<Employee> employeeList = new ArrayList<>();
        // Attempt to read the "employees.csv" file and handle errors
        System.out.println("[EMPLOYEE INFORMATION]");
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            line = reader.readLine(); // Skip the first line of the reader
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
                Employee employee = new Employee(id, name, hours, payRate);
                System.out.printf(
                        "id: %d, name: %s, hours-worked: %.2f, pay-rate: $%.2f%n",
                        id, name, hours, payRate
                );

                // Add employee to the list
                employeeList.add(employee);
            }
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Error: could not find the employee.csv file");
        } catch (IOException e) {
            System.out.println("Error: problem reading employee.csv file");
        }

        return employeeList;
    }

    // Checks if a fileName is a .json file
    public static boolean isJsonFile(String fileName) {
        if (fileName == null) {
            return false;
        }
        return fileName.trim().toLowerCase().endsWith(".json");
    }


    // Creates a .json payroll report file in this format: (payroll-month-year)
    public static void createPayrollReport(List<Employee> employeeList, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            if (isJsonFile(fileName)) {
                writer.write("[\n"); // .json start line
            } else {
                writer.write("id|name|gross-pay\n"); // .csv start line
            }

            for (Employee employee : employeeList) {
                // Use the employee information to get the values needed
                String id = String.valueOf(employee.getEmployeeId());
                String name = employee.getName();
                String grossPay = String.valueOf(employee.getGrossPay());

                // Check if this file is a .json or .csv to format correctly
                if (isJsonFile(fileName)) {
                    // Create a string line for information (.json format)
                    String line = "\t{\"id\": " + id + ", \"name\": " +
                            "\"" + name + "\"" + ", \"gross-pay\": " + grossPay + "},\n";
                    writer.write(line);
                } else {
                    // Create a string line for information (.csv format)
                    String line = id + "|" + name + "|" + grossPay + "\n";
                    writer.write(line);
                }
            }

            if (isJsonFile(fileName)) {
                writer.write("]\n"); // .json end line
            }
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Error: could not find " + fileName);
        } catch (IOException e) {
            System.out.println("Error: problem writing " + fileName);
        }
    }
}