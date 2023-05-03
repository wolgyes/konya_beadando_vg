package org.example;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);
        InventoryDAO inventoryDAO = new InventoryDAO("inventories.json");
        inventoryDAO.readInventoryFromJson();

        System.out.println("Inventory Management System");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        while (true) {
            System.out.println("Choose an action:");
            System.out.println("1. View all items");
            System.out.println("2. Add new item");
            System.out.println("3. Update item");
            System.out.println("4. Delete item");
            System.out.println("5. Exit");


            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    SimpleDateFormat sDF = new SimpleDateFormat("yyyy.MM.dd");
                    System.out.println("All items:");
                    for (var item : inventoryDAO.getAllItems()) {
                        System.out.println(item.getId() + "\t" + item.getName() + "\t" + item.getPrice() + "\t" + sDF.format(item.getExpirationDate()) + "\n");
                    }
                    break;
                case 2:
                    System.out.println("Add new item:");
                    System.out.print("ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Price: ");
                    float price = scanner.nextFloat();
                    scanner.nextLine();
                    System.out.print("Expiration date (yyyy-mm-dd): ");
                    String expirationDateString = scanner.nextLine();
                    Date expirationDate = dateFormat.parse(expirationDateString);
                    try {
                        inventoryDAO.addItem(new InventoryItem(id, name, price, expirationDate));
                        inventoryDAO.writeInventoryToJson();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Update item:");
                    System.out.print("ID: ");
                    int updateId = scanner.nextInt();
                    InventoryItem currentItem = inventoryDAO.getItemById(updateId);
                    if (currentItem == null) {
                        System.out.println("Item not found");
                    } else {
                        scanner.nextLine(); // consume the newline character left by the previous nextInt() call
                        System.out.print("New name: ");
                        String newName = scanner.nextLine();
                        System.out.print("New price: ");
                        float newPrice = scanner.nextFloat();
                        scanner.nextLine(); // consume the newline character left by the previous nextFloat() call
                        System.out.print("New expiration date (yyyy-MM-dd): ");
                        String newExpirationDateString = scanner.nextLine();
                        Date newExpirationDate = dateFormat.parse(newExpirationDateString);
                        InventoryItem updatedItem = new InventoryItem(updateId, newName, newPrice, newExpirationDate);
                        inventoryDAO.updateItem(updatedItem);
                        inventoryDAO.writeInventoryToJson();
                    }
                    break;
                case 4:
                    System.out.println("Delete item:");
                    System.out.print("ID: ");
                    int deleteId = scanner.nextInt();
                    inventoryDAO.deleteItem(deleteId);
                    inventoryDAO.writeInventoryToJson();
                    break;
                case 5:
                    inventoryDAO.writeInventoryToJson();
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}

