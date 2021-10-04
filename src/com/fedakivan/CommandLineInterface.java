package com.fedakivan;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CommandLineInterface {
    public static String[] alphabet = new String[]{"addproduct", "addorder", "updatequantity", "showview", "removeproduct", "help"};
    private static final List<String> commands = Arrays.asList(alphabet);
    public static final String DATABASE = "shop";
    public static void main(String[] args) {
        StartMessage.startMessage();
        DatabaseSource dataSource = new DatabaseSource();
        if (!dataSource.open(DATABASE)) {
            System.out.println("Can't open database");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter a command");
            if (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                if (commands.contains(command)) {
                    runCommand(command, dataSource.getConn());
                } else {
                    if (command.equals("exit") || command.equals("Q")) {
                        System.out.println("Exiting...");
                        break;
                    } else {
                        System.out.println("Wrong command. Please, try again");
                    }
                }
            }
        }
        scanner.close();
        dataSource.close();
    }

    public static void runCommand(String command, Connection conn) {
        if (command != null) {
            CommandHandler commandHandler = new CommandHandler(conn);
            Scanner scanner = new Scanner(System.in);
            switch (command) {
                case "addproduct":
                    System.out.println("Adding product to database");
                    commandHandler.addProductCommand();
                    while (true) {
                        System.out.println("Do you want to add another product? (Y/N)");
                        if (scanner.hasNextLine()) {
                            String answer = scanner.nextLine();
                            if (answer.equals("Y")) {
                                commandHandler.addProductCommand();
                            } else if (answer.equals("N")) {
                                break;
                            } else {
                                System.out.println("Wrong input. Try again");
                            }
                        }
                    }
                    break;
                case "addorder":
                    System.out.println("Adding order to database");
                    commandHandler.addOrderCommand();
                    while (true) {
                        System.out.println("Do you want to add another order? (Y/N)");
                        if (scanner.hasNextLine()) {
                            String answer = scanner.nextLine();
                            if (answer.equals("Y")) {
                                commandHandler.addOrderCommand();
                            } else if (answer.equals("N")) {
                                break;
                            } else {
                                System.out.println("Wrong input. Try again");
                            }
                        }
                    }
                    break;
                case "updatequantity":
                    System.out.println("Changing quantity of orders");
                    commandHandler.updateQuantity();
                    while (true) {
                        System.out.println("Do you want to change another quantity? (Y/N)");
                        if (scanner.hasNextLine()) {
                            String answer = scanner.nextLine();
                            if (answer.equals("Y")) {
                                commandHandler.updateQuantity();
                            } else if (answer.equals("N")) {
                                break;
                            } else {
                                System.out.println("Wrong input. Try again");
                            }
                        }
                    }
                    break;
                case "showview":
                    System.out.println("Showing view");
                    commandHandler.showView();
                    while (true) {
                        System.out.println("Do you want to show another view? (Y/N)");
                        if (scanner.hasNextLine()) {
                            String answer = scanner.nextLine();
                            if (answer.equals("Y")) {
                                commandHandler.showView();
                            } else if (answer.equals("N")) {
                                break;
                            } else {
                                System.out.println("Wrong input. Try again");
                            }
                        }
                    }
                    break;
                case "removeproduct":
                    System.out.println("Removing");
                    commandHandler.removeProduct();
                    while (true) {
                        System.out.println("Do you want to remove something else? (Y/N)");
                        if (scanner.hasNextLine()) {
                            String answer = scanner.nextLine();
                            if (answer.equals("Y")) {
                                commandHandler.removeProduct();
                            } else if (answer.equals("N")) {
                                break;
                            } else {
                                System.out.println("Wrong input. Try again");
                            }
                        }
                    }
                    break;
                case "help":
                    StartMessage.startMessage();
                    break;
                case "Q":
                case "exit":
                    System.out.println("Unexpected exit command. Exit 0");
                default:
                    System.out.println("Unexpected input. Exit 1");
                    break;
            }
        } else {
            System.out.println("Wrong command transferred to executor");
        }
    }
}