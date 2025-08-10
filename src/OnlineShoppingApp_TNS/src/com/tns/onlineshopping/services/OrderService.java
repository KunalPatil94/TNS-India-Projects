package com.tns.onlineshopping.services;

//===== com/tns/onlineshopping/services/OrderService.java =====


import com.tns.onlineshopping.entities.Order;
import com.tns.onlineshopping.entities.Product;
import com.tns.onlineshopping.entities.ProductQuantityPair;
import com.tns.onlineshopping.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
 private ProductService productService = new ProductService();

 public boolean placeOrder(int customerId, List<ProductQuantityPair> items) {
     String insertOrder = "INSERT INTO Orders(customerId, status) VALUES (?, 'Pending')";
     String insertItem = "INSERT INTO OrderItems(orderId, productId, quantity) VALUES (?, ?, ?)";
     try (Connection conn = DBConnection.getConnection()) {
         conn.setAutoCommit(false);
         try (PreparedStatement psOrder = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
              PreparedStatement psItem = conn.prepareStatement(insertItem)) {
             psOrder.setInt(1, customerId);
             psOrder.executeUpdate();
             try (ResultSet keys = psOrder.getGeneratedKeys()) {
                 if (keys.next()) {
                     int orderId = keys.getInt(1);
                     // check stock and insert items
                     for (ProductQuantityPair pq : items) {
                         Product prod = productService.getProductById(pq.getProduct().getProductId());
                         if (prod == null) throw new SQLException("Product not found: " + pq.getProduct().getProductId());
                         if (prod.getStockQuantity() < pq.getQuantity()) throw new SQLException("Insufficient stock for product " + prod.getName());
                         psItem.setInt(1, orderId);
                         psItem.setInt(2, prod.getProductId());
                         psItem.setInt(3, pq.getQuantity());
                         psItem.executeUpdate();

                         // reduce stock
                         productService.updateStock(prod.getProductId(), prod.getStockQuantity() - pq.getQuantity());
                     }
                     conn.commit();
                     return true;
                 } else throw new SQLException("Failed to create order");
             }
         } catch (SQLException ex) {
             conn.rollback();
             System.out.println("Error placing order: " + ex.getMessage());
             return false;
         } finally {
             conn.setAutoCommit(true);
         }
     } catch (SQLException e) {
         System.out.println("DB connection error: " + e.getMessage());
         return false;
     }
 }

 public List<Order> getOrdersByCustomer(int customerId) {
     List<Order> list = new ArrayList<>();
     String sql = "SELECT orderId, customerId, status FROM Orders WHERE customerId = ?";
     try (Connection conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql)) {
         ps.setInt(1, customerId);
         try (ResultSet rs = ps.executeQuery()) {
             while (rs.next()) list.add(new Order(rs.getInt(1), rs.getInt(2), rs.getString(3)));
         }
     } catch (SQLException e) {
         System.out.println("Error fetching orders: " + e.getMessage());
     }
     return list;
 }

 public List<ProductQuantityPair> getOrderItems(int orderId) {
     List<ProductQuantityPair> list = new ArrayList<>();
     String sql = "SELECT oi.productId, oi.quantity, p.name, p.price, p.stockQuantity FROM OrderItems oi JOIN Product p ON oi.productId = p.productId WHERE oi.orderId = ?";
     try (Connection conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql)) {
         ps.setInt(1, orderId);
         try (ResultSet rs = ps.executeQuery()) {
             while (rs.next()) {
                 Product p = new Product(rs.getInt(1), rs.getString(3), rs.getDouble(4), rs.getInt(5));
                 list.add(new ProductQuantityPair(p, rs.getInt(2)));
             }
         }
     } catch (SQLException e) {
         System.out.println("Error fetching order items: " + e.getMessage());
     }
     return list;
 }

 public boolean updateOrderStatus(int orderId, String newStatus) {
     String sql = "UPDATE Orders SET status = ? WHERE orderId = ?";
     try (Connection conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql)) {
         ps.setString(1, newStatus);
         ps.setInt(2, orderId);
         int rows = ps.executeUpdate();
         return rows > 0;
     } catch (SQLException e) {
         System.out.println("Error updating order status: " + e.getMessage());
         return false;
     }
 }

 public List<Order> getAllOrders() {
     List<Order> list = new ArrayList<>();
     String sql = "SELECT orderId, customerId, status FROM Orders";
     try (Connection conn = DBConnection.getConnection();
          Statement st = conn.createStatement();
          ResultSet rs = st.executeQuery(sql)) {
         while (rs.next()) list.add(new Order(rs.getInt(1), rs.getInt(2), rs.getString(3)));
     } catch (SQLException e) {
         System.out.println("Error fetching all orders: " + e.getMessage());
     }
     return list;
 }
}
