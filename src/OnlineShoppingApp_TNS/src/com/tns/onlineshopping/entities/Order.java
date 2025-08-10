package OnlineShoppingApp_TNS.src.com.tns.onlineshopping.entities;
//===== com/tns/onlineshopping/entities/Order.java =====

public class Order {
 private int orderId;
 private int customerId;
 private String status;

 public Order() {}
 public Order(int orderId, int customerId, String status) {
     this.orderId = orderId;
     this.customerId = customerId;
     this.status = status;
 }

 public int getOrderId() { return orderId; }
 public int getCustomerId() { return customerId; }
 public String getStatus() { return status; }

 public void setStatus(String status) { this.status = status; }
}
