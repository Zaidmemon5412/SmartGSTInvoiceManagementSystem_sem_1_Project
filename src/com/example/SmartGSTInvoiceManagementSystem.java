package com.example;
import java.util.*;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
// Base class User
class User {
    String name;
    String email;

    User(String name, String email) {
        this.name = name;
        this.email = email;
    }
    void showUserType() {
        System.out.println("User Type: Generic User");
    }
}
// Customer class inheriting User
class Customer extends User {
    Order[] orders;
    int orderCount;

    Customer(String name, String email) {
        super(name, email);
        this.orders = new Order[10]; // Initial capacity for 10 orders
        this.orderCount = 0;
    }

    void addOrder(Order order) {
        if (orderCount >= orders.length) {
            // Expand array(number of order) if needed
            Order[] newOrders = new Order[orders.length + 10];
            for (int i = 0; i < orders.length; i++) {
                newOrders[i] = orders[i];
            }
            orders = newOrders;
        }
        orders[orderCount++] = order;
    }
    Order[] getOrders() {
        Order[] currentOrders = new Order[orderCount];
        for (int i = 0; i < orderCount; i++) {
            currentOrders[i] = orders[i];
        }
        return currentOrders;
    }
}
// Product class
class Product {
    String name;
    double price;
    double gstRate;
    double gstAmount;
    double totalPriceWithGst;
    double cgst;
    double sgst;
    int quantity;

    Product(String name, double price, double gstRate, int quantity) {
        this.name = name;
        this.price = price;
        this.gstRate = gstRate;
        this.quantity = quantity;
        calculateGST();
    }
    void calculateGST() {
        double gstAmountPerUnit = (gstRate / 100) * price;
        this.cgst = gstAmountPerUnit / 2;
        this.sgst = gstAmountPerUnit / 2;
        this.gstAmount = this.cgst + this.sgst;
        this.totalPriceWithGst = (price + gstAmount) * quantity;
    }
    String getName() {
        return name;
    }
    double getPrice() {
        return price;
    }
    double getGstRate() {
        return gstRate;
    }
    double getGstAmount() {
        return gstAmount;
    }
    double getTotalPriceWithGst() {
        return totalPriceWithGst;
    }
    double getCGST() {
        return cgst;
    }
    double getSGST() {
        return sgst;
    }
    int getQuantity() {
        return quantity;
    }
   public String toString() {
        return "Product Name: " + name + ", Price: " + price + ", Quantity: " + quantity + ", GST Rate: " + gstRate + "%, CGST: " + cgst + ", SGST: " + sgst + ", Total Price (With GST): " + totalPriceWithGst;
    }
}
// Order class
class Order {
    Date orderDate;
    Product product;
    String status;

    Order(Product product) {
        this.orderDate = new Date();
        this.product = product;
    }
    Date getOrderDate() {
        return orderDate;
    }
    Product getProduct() {
        return product;
    }
   
}
// CreditCardPayment class
class CreditCardPayment {
    String cardNumber;

    CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    void pay(double amount) {
        System.out.println("Paid " + amount + " using Credit Card ending with " + cardNumber.substring(cardNumber.length() - 4));
    }
    String getPaymentDetails() {
        return "Credit Card ending with " + cardNumber.substring(cardNumber.length() - 4);
    }
}
// PayPalPayment class
class PayPalPayment {
    String email;

    PayPalPayment(String email) {
        this.email = email;
    }
    void pay(double amount) {
        System.out.println("Paid " + amount + " using PayPal account " + email);
    }
    String getPaymentDetails() {
        return "PayPal account " + email;
    }
}
// CashPayment class
class CashPayment {
    String referenceNumber;

    CashPayment(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    void pay(double amount) {
        System.out.println("Paid " + amount + " using Cash. Reference Number: " + referenceNumber);
    }
    String getPaymentDetails() {
        return "Cash Payment - Reference Number: " + referenceNumber;
    }
}
// Invoice class
class Invoice {
    String invoiceNumber;
    Customer customer;
    Product[] products;
    int productCount;
    double totalGST;
    double grandTotal;
    String paymentDetails;

     Invoice(String invoiceNumber, Customer customer) {
        this.invoiceNumber = invoiceNumber;
        this.customer = customer;
        this.products = new Product[10]; // Initial capacity for 10 products
        this.productCount = 0;
        this.totalGST = 0.0;
        this.grandTotal = 0.0;
    }
     void addProduct(Product product) {
        if (productCount >= products.length) {
            // Expand array if needed
            Product[] newProducts = new Product[products.length + 10];
            for (int i = 0; i < products.length; i++) {
                newProducts[i] = products[i];
            }
            products = newProducts;
        }
        products[productCount++] = product;
        totalGST += product.getGstAmount();
        grandTotal += product.getTotalPriceWithGst();
    }
    void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
    void displayInvoice() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    
        System.out.println("\n=========================================");
        System.out.println("                 INVOICE                 ");
        System.out.println("=========================================");
        System.out.println(" Invoice Number   : " + invoiceNumber);
        System.out.println(" Customer Name    : " + customer.name);
        System.out.println(" Date             : " + sdf.format(new Date()));
        System.out.println("-----------------------------------------");
        System.out.println(" Product Details:");
        System.out.println("-----------------------------------------");
        System.out.println(" Product Name      Price        Quantity ");
        System.out.println("-----------------------------------------");
    
        for (int i = 0; i < productCount; i++) {
            System.out.println(" " + products[i].getName() + "       ₹" + products[i].getPrice() + "        " + products[i].getQuantity());
        }
    
        System.out.println("-----------------------------------------");
        System.out.println(" Total GST        : ₹" + totalGST);
        System.out.println(" Grand Total      : ₹" + grandTotal);
        System.out.println(" Payment Method   : " + paymentDetails);
        System.out.println("=========================================\n");
    }
    
    String getQRCodeData() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        StringBuffer sb = new StringBuffer();
        sb.append("====================================\n");
        sb.append("            INVOICE\n");
        sb.append("====================================\n");
        sb.append("Invoice Number: ").append(invoiceNumber).append("\n");
        sb.append("Customer Name: ").append(customer.name).append("\n");
        sb.append("Date: ").append(sdf.format(new Date())).append("\n");
        sb.append("------------------------------------\n");
        sb.append("Product Details:\n");
        sb.append("------------------------------------\n");
        for (int i = 0; i < productCount; i++) {
            sb.append(products[i].getName()).append(", Price: ₹").append(products[i].getPrice()).append(", Quantity: ").append(products[i].getQuantity()).append("\n");
        }
        sb.append("------------------------------------\n");
        sb.append("Including GST: ").append(totalGST).append("\n");
        sb.append("Total Amount: ").append(grandTotal).append("\n");
        sb.append("====================================\n");
        sb.append("Payment Method: ").append(paymentDetails).append("\n");
        sb.append("====================================");
        return sb.toString();
    } 
}
// Main class
public class SmartGSTInvoiceManagementSystem {
    static Invoice[] invoices = new Invoice[10]; // Initial capacity for 10 invoices
    static int invoiceCount = 0;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Invoice Management System!");
        // Admin login
        final String correctPassword = "5412";
        while (true){
            System.out.print("Enter Admin Password: ");
            String input = sc.nextLine().trim();
            if (input.equals(correctPassword)) {
                break;
            } else {
                System.out.println("Incorrect password. Please try again.");
            }
        }
        int choice;
        do {
            System.out.println("------ Main Menu ------");
            System.out.println("1. Create new invoice");
            System.out.println("2. View Past Invoices");
            System.out.println("3. Search Invoice by Invoice Number");
            System.out.println("4. Update Existing Invoice");
            System.out.println("5. Exit");
            System.out.print("Enter Your choice: ");
            choice = sc.nextInt();
            sc.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    createNewInvoice();
                    break;
                case 2:
                    viewPastInvoices();
                    break;
                case 3:
                    searchInvoiceByNumber();
                    break;
                case 4:
                    updateExistingInvoice();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    System.out.println("Thank you!!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 5);
    }
    static void createNewInvoice() {
        System.out.println("****** Create Invoice ******");
        String invoiceNumber = "";
        boolean isValid = false;

        while (!isValid) {
            System.out.print("Enter Invoice Number (e.g., INV-001): ");
            invoiceNumber = sc.nextLine().toUpperCase().trim();
            if (invoiceNumber.startsWith("INV-") && invoiceNumber.length() > 4) {
                isValid = true;
            } else {
                System.out.println("Invalid Invoice Number! It must start with 'INV-' followed by numbers (e.g., INV-001).");
            }
        }
        String customerName = "";
        while(true){
            System.out.print("Enter Customer Name: ");
            customerName = sc.nextLine().trim();
            if(customerName.length() > 0){
                break;
            }else{
                System.out.println("Customer Name cannot be empty. Please enter a valid name.");
            }
        }   
      
        String customerEmail = "";
        boolean isEmailValid = false;
        while (!isEmailValid) {
            System.out.print("Enter Customer Email: ");
            customerEmail = sc.nextLine();
            if (validateEmail(customerEmail)) {
                isEmailValid = true;
            } else {
                System.out.println("Invalid Email! Please enter a valid email address ending with '@gmail.com'.");
            }
        }

        Customer customer = new Customer(customerName, customerEmail);

        Invoice invoice = new Invoice(invoiceNumber, customer);

        System.out.print("Enter Number Of Products You Want To Add: ");
        int numberOfProducts = sc.nextInt();
        sc.nextLine();  // Consume newline

        for (int i = 0; i < numberOfProducts; i++) {
            System.out.println("Enter Details Of Product " + (i + 1) + ":");
            System.out.print("Enter Product Name: ");
            String productName = sc.nextLine();
            System.out.print("Enter Price Of Product: ");
            double price = sc.nextDouble();
            System.out.print("Enter Quantity Of Product: ");
            int quantity = sc.nextInt();
            
            // Display GST slab information before asking for GST rate
            System.out.println("GST Slabs:");
            System.out.println("5% - Basic necessities");
            System.out.println("12% - Standard goods");
            System.out.println("18% - Education and study materials");
            System.out.println("28% - Luxury items");

            double gstRate = 0.0;
            boolean isGstValid = false;
            while (!isGstValid) {
                System.out.print("Enter GST Rate in {%}: ");
                gstRate = sc.nextDouble();
                sc.nextLine();  // Consume newline
                
                if (gstRate == 5 || gstRate == 12 || gstRate == 18 || gstRate == 28) {
                    isGstValid = true;
                } else {
                    System.out.println("Invalid GST Rate! Please enter a valid GST rate from the slabs provided.");
                }
            }

            Product product = new Product(productName, price, gstRate, quantity);
            invoice.addProduct(product);
        }

        int paymentChoice = 0;
        while (paymentChoice <1 || paymentChoice >3) {
            System.out.println("Select Payment Method:");
            System.out.println("1. Credit Card");
            System.out.println("2. PayPal");
            System.out.println("3. Cash");
            System.out.print("Enter your choice: ");
            paymentChoice = sc.nextInt();
            sc.nextLine();  // Consume newline

            if (paymentChoice <1 || paymentChoice >3) {
                System.out.println("Invalid payment method selected! Please try again.");
            }
        }
        String paymentDetails = "";
        switch (paymentChoice) {
            case 1:
                String cardNumber = "";
                boolean isCardValid = false;
                while (!isCardValid) {
                    System.out.print("Enter Credit Card Number (16 digits): ");
                    cardNumber = sc.nextLine();
                    if (validateCreditCard(cardNumber)) {
                        isCardValid = true;
                    } else {
                        System.out.println("Invalid Credit Card Number! It must be exactly 16 digits.");
                    }
                }
                CreditCardPayment creditCardPayment = new CreditCardPayment(cardNumber);
                creditCardPayment.pay(invoice.grandTotal);
                paymentDetails = creditCardPayment.getPaymentDetails();
                break;
            case 2:
                String paypalEmail = "";
                boolean isPaypalEmailValid = false;
                while (!isPaypalEmailValid) {
                    System.out.print("Enter PayPal Email: ");
                    paypalEmail = sc.nextLine();
                    if (validateEmail(paypalEmail)) {
                        isPaypalEmailValid = true;
                    } else {
                        System.out.println("Invalid Email! Please enter a valid email address ending with '@gmail.com'.");
                    }
                }
                PayPalPayment payPalPayment = new PayPalPayment(paypalEmail);
                payPalPayment.pay(invoice.grandTotal);
                paymentDetails = payPalPayment.getPaymentDetails();
                break;
            case 3:
                String refNumber = "REF-" + (int)(Math.random() * 1000);
                CashPayment cashPayment = new CashPayment(refNumber);
                cashPayment.pay(invoice.grandTotal);
                paymentDetails = cashPayment.getPaymentDetails();
                break;
        }
        invoice.setPaymentDetails(paymentDetails);
        invoice.displayInvoice();
        if (invoiceCount >= invoices.length) {
            // Expand array if needed
            Invoice[] newInvoices = new Invoice[invoices.length + 10];
            for (int i = 0; i < invoices.length; i++) {
                newInvoices[i] = invoices[i];
            }
            invoices = newInvoices;
        }
        invoices[invoiceCount++] = invoice;
        // Generate QR code for the invoice
        generateQRCode(invoice.getQRCodeData(), "Invoice_" + invoiceNumber + ".png");
    }

    static boolean validateEmail(String email) {
        // Check if email ends with '@gmail.com'
        return email.endsWith("@gmail.com");
    }

    static boolean validateCreditCard(String cardNumber) {
        // Check if card number is exactly 16 digits long and all characters are digits
        if (cardNumber.length() == 16) {
            for (int i = 0; i < 16; i++) {
                if (!Character.isDigit(cardNumber.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    static void viewPastInvoices() {
        if (invoiceCount == 0) {
            System.out.println("No invoices available to display.");
        } else {
            System.out.println("--- List of Past Invoices ---");
            for (int i = 0; i < invoiceCount; i++) {
                System.out.println((i + 1) + ". Invoice Number: " + invoices[i].invoiceNumber);
            }
            System.out.print("\nEnter the invoice index (1-" + invoiceCount + ") to view details: ");
            int selectedIndex = sc.nextInt();
            sc.nextLine();  // Consume the newline character

            if (selectedIndex > 0 && selectedIndex <= invoiceCount) {
                System.out.println("\n--- Invoice Details ---");
                invoices[selectedIndex - 1].displayInvoice();
            } else {
                System.out.println("Invalid selection! Please try again.");
            }
        }
    }
    
    static void searchInvoiceByNumber() {
        if (invoiceCount == 0) {
            System.out.println("No invoices available to search.");
            return;
        }
    
        // Pehle available invoices dikhao
        System.out.println("\n--- Available Invoices ---");
        for (int i = 0; i < invoiceCount; i++) {
            System.out.println((i+1) + ". " + invoices[i].invoiceNumber);
        }
    
        // Fir search ka option do
        System.out.print("\nEnter Invoice Number to Search (e.g. INV-001): ");
        String searchNumber = sc.nextLine().trim().toUpperCase();
        
        boolean found = false;
        for (int i = 0; i < invoiceCount; i++) {
            if (invoices[i].invoiceNumber.equalsIgnoreCase(searchNumber)) {
                System.out.println("\n--- Invoice Details ---");
                invoices[i].displayInvoice();
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.out.println("Invoice '" + searchNumber + "' not found!");
        }
    }
    static void updateExistingInvoice() {
        if (invoiceCount == 0) {
            System.out.println("No invoices available to update.");
            return;
        }
        // Display all invoices
        System.out.println("--- List of Past Invoices ---");
        for (int i = 0; i < invoiceCount; i++) {
            System.out.println((i + 1) + ". Invoice Number: " + invoices[i].invoiceNumber);
        }
        // Prompt user to enter the invoice number to update
        System.out.print("Enter Invoice Number to Update: ");
        String invoiceNumber = sc.nextLine().trim().toUpperCase();
        boolean found = false;
        for (int i = 0; i < invoiceCount; i++) {
            if (invoices[i].invoiceNumber.equals(invoiceNumber)) {
                found = true;
                System.out.print("Enter Number Of Products You Want To Add: ");
                int numberOfProducts = sc.nextInt();
                sc.nextLine();  // Consume newline
                for (int j = 0; j < numberOfProducts; j++) {
                    System.out.println("Enter Details Of Product " + (j + 1) + ":");
                    System.out.print("Enter Product Name: ");
                    String productName = sc.nextLine();
                    System.out.print("Enter Price Of Product: ");
                    double price = sc.nextDouble();
                    System.out.print("Enter Quantity Of Product: ");
                    int quantity = sc.nextInt();
                    System.out.print("Enter GST Rate in {%}: ");
                    double gstRate = sc.nextDouble();
                    sc.nextLine();  // Consume newline
    
                    Product product = new Product(productName, price, gstRate, quantity);
                    invoices[i].addProduct(product);
                }
    
                invoices[i].displayInvoice();
    
                // Regenerate QR code for the updated invoice
                generateQRCode(invoices[i].getQRCodeData(), "Invoice_" + invoiceNumber + ".png");
                break;
            }
        }
        if (!found) {
            System.out.println("Invoice not found!");
        }
    }
    
    static void generateQRCode(String data, String filePath) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 350;
        int height = 350;
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
            File qrFile = new File(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", qrFile.toPath());
            System.out.println("QR Code generated and saved to " + filePath);
        } catch (WriterException | IOException e) {
            System.out.println("Could not generate QR Code: " + e.getMessage());
        }
    }
}