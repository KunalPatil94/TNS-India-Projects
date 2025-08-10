package OnlineShoppingApp_TNS.src.com.tns.onlineshopping.application;

import OnlineShoppingApp_TNS.src.com.tns.onlineshopping.entities.*;
import OnlineShoppingApp_TNS.src.com.tns.onlineshopping.services.*;

import java.util.*;

public class OnlineShopping {
 private static final Scanner sc = new Scanner(System.in);
 private static final ProductService productService = new ProductService();
 private static final AdminService adminService = new AdminService();
 private static final CustomerService customerService = new CustomerService();
 private static final OrderService orderService = new OrderService();

 public static void main(String[] args) {
     while (true) {
         System.out.println("\nOutput\n1. Admin Menu\n2. Customer Menu\n3. Exit");
         System.out.print("Choose an option: ");
         int opt = Integer.parseInt(sc.nextLine());
         if (opt == 1) adminMenu();
         else if (opt == 2) customerMenu();
         else if (opt == 3) { System.out.println("Exiting..."); break; }
         else System.out.println("Invalid option");
     }
 }

 private static void adminMenu() {
     while (true) {
         System.out.println("\nAdmin Menu:\n1. Add Product\n2. Remove Product\n3. View Products\n4. Create Admin\n5. View Admins\n6. Update Order Status\n7. View Orders\n8. Return");
         System.out.print("Choose an option: ");
         int opt = Integer.parseInt(sc.nextLine());
         switch (opt) {
             case 1: addProduct(); break;
             case 2: removeProduct(); break;
             case 3: viewProducts(); break;
             case 4: createAdmin(); break;
             case 5: viewAdmins(); break;
             case 6: updateOrderStatus(); break;
             case 7: viewOrdersForAdmin(); break;
             case 8: System.out.println("Exiting Admin..."); return;
             default: System.out.println("Invalid option");
         }
     }
 }

 private static void addProduct() {
     System.out.print("Enter Product ID: "); int id = Integer.parseInt(sc.nextLine());
     System.out.print("Enter Product Name: "); String name = sc.nextLine();
     System.out.print("Enter Product Price: "); double price = Double.parseDouble(sc.nextLine());
     System.out.print("Enter Stock Quantity: "); int qty = Integer.parseInt(sc.nextLine());
     Product p = new Product(id, name, price, qty);
     if (productService.addProduct(p)) System.out.println("Product added successfully!");
     else System.out.println("Failed to add product.");
 }

 private static void removeProduct() {
     System.out.print("Enter Product ID to remove: "); int id = Integer.parseInt(sc.nextLine());
     if (productService.removeProduct(id)) System.out.println("Product removed successfully!");
     else System.out.println("Product not found or could not be removed");
 }

 private static void viewProducts() {
     System.out.println("Products:");
     for (Product p : productService.getAllProducts()) System.out.println(p);
 }

 private static void createAdmin() {
     System.out.print("Enter User ID: "); int id = Integer.parseInt(sc.nextLine());
     System.out.print("Enter Username: "); String username = sc.nextLine();
     System.out.print("Enter Email: "); String email = sc.nextLine();
     Admin a = new Admin(id, username, email);
     if (adminService.createAdmin(a)) System.out.println("Admin created successfully!");
     else System.out.println("Failed to create admin.");
 }

 private static void viewAdmins() {
     System.out.println("Admins:");
     for (Admin a : adminService.getAllAdmins()) System.out.println(a);
 }

 private static void updateOrderStatus() {
     System.out.print("Enter Order ID: "); int id = Integer.parseInt(sc.nextLine());
     System.out.print("Enter new status (Completed/Delivered/Cancelled): "); String status = sc.nextLine();
     if (orderService.updateOrderStatus(id, status)) System.out.println("Order updated successfully!");
     else System.out.println("Failed to update order");
 }

 private static void viewOrdersForAdmin() {
     System.out.println("Orders:");
     List<Order> orders = orderService.getAllOrders();
     for (Order o : orders) {
         System.out.print("Order ID: " + o.getOrderId() + ", Customer: " + o.getCustomerId() + ", Status: " + o.getStatus() + "\n");
         List<ProductQuantityPair> items = orderService.getOrderItems(o.getOrderId());
         for (ProductQuantityPair pq : items)
             System.out.println("Product: " + pq.getProduct().getName() + ", Quantity: " + pq.getQuantity());
     }
 }

 // ---------------- Customer Menu ----------------
 private static void customerMenu() {
     while (true) {
         System.out.println("\nCustomer Menu:\n1. Create Customer\n2. View Customers\n3. Place Order\n4. View Orders\n5. View Products\n6. Return");
         System.out.print("Choose an option: ");
         int opt = Integer.parseInt(sc.nextLine());
         switch (opt) {
             case 1: createCustomer(); break;
             case 2: viewCustomers(); break;
             case 3: placeOrder(); break;
             case 4: viewCustomerOrders(); break;
             case 5: viewProducts(); break;
             case 6: System.out.println("Exiting Customer Menu..."); return;
             default: System.out.println("Invalid option");
         }
     }
 }

 private static void createCustomer() {
     System.out.print("Enter User ID: "); int id = Integer.parseInt(sc.nextLine());
     System.out.print("Enter Username: "); String username = sc.nextLine();
     System.out.print("Enter Email: "); String email = sc.nextLine();
     System.out.print("Enter Address: "); String address = sc.nextLine();
     Customer c = new Customer(id, username, email, address);
     if (customerService.createCustomer(c)) System.out.println("Customer created successfully!");
     else System.out.println("Failed to create customer.");
 }

 private static void viewCustomers() {
     System.out.println("Customers:");
     for (Customer c : customerService.getAllCustomers()) System.out.println(c);
 }

 private static void placeOrder() {
     System.out.print("Enter Customer ID: "); int customerId = Integer.parseInt(sc.nextLine());
     List<ProductQuantityPair> items = new ArrayList<>();
     while (true) {
         System.out.print("Enter Product ID to add to order (or -1 to complete): ");
         int pid = Integer.parseInt(sc.nextLine());
         if (pid == -1) break;
         Product prod = productService.getProductById(pid);
         if (prod == null) { System.out.println("Product not found"); continue; }
         System.out.print("Enter quantity: "); int qty = Integer.parseInt(sc.nextLine());
         items.add(new ProductQuantityPair(prod, qty));
     }
     if (items.isEmpty()) { System.out.println("No items selected"); return; }
     if (orderService.placeOrder(customerId, items)) System.out.println("Order placed successfully!");
     else System.out.println("Failed to place order");
 }

 private static void viewCustomerOrders() {
     System.out.print("Enter Customer ID: "); int cid = Integer.parseInt(sc.nextLine());
     List<Order> orders = orderService.getOrdersByCustomer(cid);
     System.out.println("Orders:");
     for (Order o : orders) {
         System.out.println("Order ID: " + o.getOrderId() + ", Status: " + o.getStatus());
         List<ProductQuantityPair> items = orderService.getOrderItems(o.getOrderId());
         for (ProductQuantityPair pq : items)
             System.out.println("Product: " + pq.getProduct().getName() + ", Quantity: " + pq.getQuantity());
     }
 }
}
