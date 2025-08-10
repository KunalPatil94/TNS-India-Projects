package OnlineShoppingApp_TNS.src.com.tns.onlineshopping.entities;

//===== com/tns/onlineshopping/entities/Customer.java =====



public class Customer extends User {
 private String address;

 public Customer() { super(); }
 public Customer(int userId, String username, String email, String address) {
     super(userId, username, email);
     this.address = address;
 }

 public String getAddress() { return address; }

 @Override
 public String toString() {
     return "User ID: " + userId + ", Username: " + username + ", Email: " + email + ", Address: " + address;
 }
}
