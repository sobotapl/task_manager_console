package pl.sobotapl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;

public class TaskManager {

    static String[][] tasks;
    static String FILE_NAME = "tasks.csv";
    static String[] MENU = {"add", "remove", "list", "exit"};

    public static void main(String[] args) {
        tasks = sendDataToArray(FILE_NAME);
        Scanner sc = new Scanner(System.in);
        String choice;
        do {
            printMenu();
            parseChoice(choice = sc.nextLine());
        } while (!choice.equals("exit"));
    }

    public static void printMenu() {
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Please select an option: " + ConsoleColors.RESET);
        for (String s : MENU) {
            System.out.println(s);
        }
    }

    public static void parseChoice(String choice) {
        switch (choice) {
            case "add":
                addTask();
                break;
            case "remove":
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please select number to remove.");
                String n = scanner.nextLine();
                removeTask(tasks,Integer.parseInt(n));
                break;
            case "list":
                System.out.println("--------------------------------");
                for (int i = 0; i < tasks.length; i++) {
                    for (int j = 0; j < tasks[i].length; j++) {
                            System.out.println(i + "|" + tasks[i][j]);
                        }
                    }
                System.out.print("--------------------------------");
                break;
            case "exit":
                savaToFile(FILE_NAME, tasks);
                System.out.print(ConsoleColors.RED);
                System.out.println("Bye Bye");
                System.exit(0);
                break;
            default:
                System.out.println("Bad data input, try again!");
        }
    }

    public static String[][] sendDataToArray (String fileName) {
        Path path = Paths.get(fileName);
        if(!Files.exists(path)){
            throw new NoSuchElementException("Data unavailable");
        }
        String[][] tab = null;
        StringBuilder sb = new StringBuilder();
        try {
            List<String> strings = Files.readAllLines(path);
            tab = new String[strings.size()][strings.get(0).split(",").length];
            for (int i = 0; i < strings.size(); i++) {
                String[] split = strings.get(i).split(",");
                for (int j = 0; j < split.length; j++) {
                    tab[i][j] = split[j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tab;
    }

    public static void addTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Task description: ");
        String description = scanner.nextLine();
        System.out.println("Task deadline: ");
        String deadline = scanner.nextLine();
        System.out.println("Priority: true/false");
        String priority;
        while(!(priority = scanner.nextLine()).equals("true") || !(priority = scanner.nextLine()).equals("false")){
            System.out.println("Bad value, try again");
        }
        priority = scanner.nextLine();
        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = deadline;
        tasks[tasks.length - 1][2] = priority;
    }

    public static void removeTask(String[][] tab, int index) {
        try {
            if (index < tab.length && index > 0) {
                tasks = ArrayUtils.remove(tab, index);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("No task with that number in array");
        }
    }

    public static void savaToFile (String fileName, String[][] tab){
        Path dir = Paths.get(fileName);
        String[] lines = new String[tasks.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }
        try {
            Files.write(dir, Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



}

