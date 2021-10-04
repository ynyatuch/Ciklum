package com.fedakivan;

import java.io.Console;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandHandler {
    private Connection conn;
    private QueryHandler queryHandler;

    public CommandHandler(Connection conn) {
        this.conn = conn;
        this.queryHandler = new QueryHandler(conn);
    }

    public void showView() {
        Scanner scanner = new Scanner(System.in);
        int answer;
        System.out.println("Choose view to show:");
        System.out.println("| Product Name | Product Price | Product Status | for all products - enter 1");
        System.out.println("List all products, which have been ordered at least once           - enter 2");
        System.out.println("| Order ID | Products total Price | Product Name | Products Quantity in orderEntry " +
                "| Order Created Date [YYYY-MM-DD HH:MM ] | by order Id                        - enter 3");
        System.out.println("List all orders                                                    - enter 4");
        System.out.println("Exit                                                               - enter 'Q'");
        while (true) {
            try {
                String line = scanner.nextLine();
                if (line.equals("Q")) {
                    break;
                }
                answer = Integer.parseInt(line);
                System.out.println(answer);
                if (answer > 0 && answer < 5) {
                    queryHandler.showView(answer);
                } else {
                    throw new IOException("Incorrect variant");
                }
                break;
            } catch (Exception e) {
                System.out.println("Wrong input. Try again." + e);
            }
        }
    }

    public void removeProduct() {
        Scanner scanner = new Scanner(System.in);
        int answer;
        System.out.println("Choose option:");
        System.out.println("Remove product by ID - enter 1");
        System.out.println("Remove all products  - enter 2");
        System.out.println("Exit                 - enter 'Q'");
        while (true) {
            try {
                String line = scanner.nextLine();
                if (line.equals("Q")) {
                    break;
                }
                answer = Integer.parseInt(line);
                System.out.println(answer);
                if (answer > 0 && answer < 3) {
                    switch (answer) {
                        case 1:
                            int productId;
                            while (true) {
                                try {
                                    System.out.println("Enter product ID: ");
                                    productId = Integer.parseInt(scanner.nextLine());
                                    break;
                                } catch (Exception e) {
                                    System.out.println("Wrong input. Try again");
                                }
                            }
                            queryHandler.removeProduct(productId);
                            break;
                        case 2:
                            String password;
                            while (true) {
                                try {
                                    Console console = System.console();
                                    char enteredPassword[] = console.readPassword("Enter your password:");
                                    password = String.valueOf(enteredPassword);
//                                    System.out.println("Enter your password: ");
//                                    password = scanner.nextLine();
                                    break;
                                } catch (Exception e) {
                                    System.out.println("Wrong input. Try again");
                                }
                            }
                            queryHandler.removeAllProducts(password);
                            break;
                        default:
                            throw new IOException("Unexpected variant");
                    }
                } else {
                    throw new IOException("Incorrect variant");
                }
                break;
            } catch (Exception e) {
                System.out.println("Wrong input. Try again." + e);
            }
        }
    }

    public void updateQuantity() {
        Scanner scanner = new Scanner(System.in);
        int orderId;
        int productId;
        int quantity;
        boolean check =true;
        while (check) {
            while (true) {
                try {
                    System.out.println("Enter order ID to update: ");
                    orderId = Integer.parseInt(scanner.nextLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Wrong input. Try again");
                }
            }
            while (true) {
                try {
                    System.out.println("Enter product ID to update: ");
                    productId = Integer.parseInt(scanner.nextLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Wrong input. Try again");
                }
            }
            while (true) {
                try {
                    System.out.println("Enter new quantity of order item: ");
                    quantity = Integer.parseInt(scanner.nextLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Wrong input. Try again");
                }
            }
            if (queryHandler.updateQuantity(productId, orderId, quantity)){
                check = false;
            }
            else{
                System.out.println("Update failed. Do you want to try again? (Y/N)");
                while (true) {
                    String answer2 = scanner.nextLine();
                    if (answer2.equals("Y") || answer2.equals("y")) {

                        break;
                    } else if (answer2.equals("N") || answer2.equals("n")) {
                        check = false;
                        break;

                    } else {
                        System.out.println("Wrong command. Please, try again");
                    }
                }
            }
        }
    }

    public void addProductCommand() {
        Scanner scanner = new Scanner(System.in);
        int productId;
        String productName;
        int productPrice;
        int productStatusNumber;
        product_status productStatusValue;

        while (true) {
            try {
                System.out.println("Enter product ID: ");
                productId = Integer.parseInt(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Wrong input. Try again");
            }
        }
        while (true) {
            try {
                System.out.println("Enter product name: ");
                productName = scanner.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("Wrong input. Try again");
            }
        }
        while (true) {
            try {
                System.out.println("Enter product price: ");
                productPrice = Integer.parseInt(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Wrong input. Try again");
            }
        }

        while (true) {
            try {
                System.out.println("Choose product status:");
                System.out.println("'out_of_stock': type 1");
                System.out.println("'in_stock': type 2");
                System.out.println("'running_low': type 3");
                productStatusNumber = Integer.parseInt(scanner.nextLine());
                if (productStatusNumber < 1 || productStatusNumber > 3) {
                    throw new IOException("Incorrect choice");
                } else {
                    productStatusValue = product_status.values()[productStatusNumber - 1];
                    break;
                }

            } catch (Exception e) {
                System.out.println("Wrong input. Try again");
            }
        }
        queryHandler.addProduct(productId, productName, productPrice, productStatusValue);
    }

    public void addOrderCommand() {
        Scanner scanner = new Scanner(System.in);
        int orderId;
        String orderStatus;
        List<Integer> productIds = new ArrayList<Integer>();
        while (true) {
            try {
                System.out.println("Enter order ID: ");
                orderId = Integer.parseInt(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Wrong input. Try again");
            }
        }
        while (true) {
            try {
                System.out.println("Enter order status: ");
                orderStatus = scanner.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("Wrong input. Try again");
            }
        }
        int productsCount = 0;
        boolean check = true;
        while (check) {
            try {
                System.out.println("Enter ordered product ID: ");
                String id = scanner.nextLine();
                if (queryHandler.checkIfProductIsPresent(Integer.parseInt(id))) {
                    productIds.add(Integer.parseInt(id));
                    productsCount++;
                }
                while (true) {
                    System.out.println("Do you want to add another product to order? (Y/N)");
                    String answer = scanner.nextLine();
                    if (answer.equals("Y") || answer.equals("y")) {
                        break;
                    } else if (answer.equals("N") || answer.equals("n")) {
                        if (productsCount == 0) {
                            while (true) {
                                System.out.println("You didn't add any products to order. Do you want to skip order adding? (Y/N)");
                                String answer2 = scanner.nextLine();
                                if (answer2.equals("Y") || answer2.equals("y")) {
                                    check = false;
                                    break;
                                } else if (answer2.equals("N") || answer2.equals("n")) {
                                    break;
                                } else {
                                    System.out.println("Wrong command. Please, try again");
                                }
                            }
                            if(!check){
                                break;
                            }
                        } else {
                            check = false;
                            break;
                        }
                    } else {
                        System.out.println("Wrong command. Please, try again");
                    }
                }
            } catch (Exception e) {
                System.out.println("Wrong input. Try again" + e);
                e.printStackTrace();
            }
        }
        if (productsCount > 0) {
            queryHandler.addOrder(orderId, orderStatus, productIds);
        }
    }

}
