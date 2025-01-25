import java.io.*;
import java.util.*;

// Class to store details of individual expenses
class Expense {
    private String category;
    private String description;
    private double amount;
    private Date date;

    public Expense(String category, String description, double amount, Date date) {
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Category: " + category + ", Description: " + description + ", Amount: " + amount + ", Date: " + date;
    }
}

// Class to manage and process all expenses
class ExpenseManager {
    private List<Expense> expenses;
    private static final String FILE_NAME = "expenses.txt";

    public ExpenseManager() {
        expenses = new ArrayList<>();
        loadExpenses();
    }

    // Add a new expense
    public void addExpense(String category, String description, double amount, Date date) {
        Expense expense = new Expense(category, description, amount, date);
        expenses.add(expense);
        saveExpenses();
    }

    // Display all expenses
    public void displayAllExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
        } else {
            for (Expense expense : expenses) {
                System.out.println(expense);
            }
        }
    }

    // Calculate total expenses for a given period
    public double calculateTotalExpenses(Date startDate, Date endDate) {
        double total = 0;
        for (Expense expense : expenses) {
            if (!expense.getDate().before(startDate) && !expense.getDate().after(endDate)) {
                total += expense.getAmount();
            }
        }
        return total;
    }

    // Save expenses to a file
    private void saveExpenses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Expense expense : expenses) {
                writer.write(expense.getCategory() + "," + expense.getDescription() + "," + expense.getAmount() + "," + expense.getDate().getTime());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving expenses: " + e.getMessage());
        }
    }

    // Load expenses from a file
    private void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String category = parts[0];
                String description = parts[1];
                double amount = Double.parseDouble(parts[2]);
                Date date = new Date(Long.parseLong(parts[3]));
                expenses.add(new Expense(category, description, amount, date));
            }
        } catch (FileNotFoundException e) {
            // File does not exist, no action needed
        } catch (IOException e) {
            System.err.println("Error loading expenses: " + e.getMessage());
        }
    }
}

public class DailyExpenseTracker {
    public static void main(String[] args) {
        ExpenseManager manager = new ExpenseManager();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nDaily Expense Tracker");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Total Expenses for a Period");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    System.out.print("Enter date (yyyy-mm-dd): ");
                    scanner.nextLine(); // Consume newline
                    String dateInput = scanner.nextLine();
                    Date date;
                    try {
                        date = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateInput);
                    } catch (Exception e) {
                        System.out.println("Invalid date format.");
                        break;
                    }
                    manager.addExpense(category, description, amount, date);
                    System.out.println("Expense added successfully.");
                    break;
                case 2:
                    manager.displayAllExpenses();
                    break;
                case 3:
                    try {
                        System.out.print("Enter start date (yyyy-mm-dd): ");
                        Date startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());
                        System.out.print("Enter end date (yyyy-mm-dd): ");
                        Date endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());
                        double total = manager.calculateTotalExpenses(startDate, endDate);
                        System.out.println("Total expenses: " + total);
                    } catch (Exception e) {
                        System.out.println("Invalid date format.");
                    }
                    break;
                case 4:
                    System.out.println("Exiting. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
