package com.tns.onlineshopping.services;


//===== com/tns/onlineshopping/services/ProductService.java =====


import com.tns.onlineshopping.entities.Product;
import com.tns.onlineshopping.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

 public boolean addProduct(Product p) {
     String sql = "INSERT INTO Product(productId, name, price, stockQuantity) VALUES (?, ?, ?, ?)";
     try (Connection conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql)) {
         ps.setInt(1, p.getProductId());
         ps.setString(2, p.getName());
         ps.setDouble(3, p.getPrice());
         ps.setInt(4, p.getStockQuantity());
         ps.executeUpdate();
         return true;
     } catch (SQLException e) {
         System.out.println("Error adding product: " + e.getMessage());
         return false;
     }
 }

 public boolean removeProduct(int productId) {
     String sql = "DELETE FROM Product WHERE productId = ?";
     try (Connection conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql)) {
         ps.setInt(1, productId);
         int rows = ps.executeUpdate();
         return rows > 0;
     } catch (SQLException e) {
         System.out.println("Error removing product: " + e.getMessage());
         return false;
     }
 }

 public List<Product> getAllProducts() {
     String sql = "SELECT productId, name, price, stockQuantity FROM Product";
     List<Product> list = new ArrayList<>();
     try (Connection conn = DBConnection.getConnection();
          Statement st = conn.createStatement();
          ResultSet rs = st.executeQuery(sql)) {
         while (rs.next()) {
             list.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getInt(4)));
         }
     } catch (SQLException e) {
         System.out.println("Error fetching products: " + e.getMessage());
     }
     return list;
 }

 public Product getProductById(int productId) {
     String sql = "SELECT productId, name, price, stockQuantity FROM Product WHERE productId = ?";
     try (Connection conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql)) {
         ps.setInt(1, productId);
         try (ResultSet rs = ps.executeQuery()) {
             if (rs.next()) {
                 return new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getInt(4));
             }
         }
     } catch (SQLException e) {
         System.out.println("Error getting product: " + e.getMessage());
     }
     return null;
 }

 public boolean updateStock(int productId, int newQty) {
     String sql = "UPDATE Product SET stockQuantity = ? WHERE productId = ?";
     try (Connection conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql)) {
         ps.setInt(1, newQty);
         ps.setInt(2, productId);
         int rows = ps.executeUpdate();
         return rows > 0;
     } catch (SQLException e) {
         System.out.println("Error updating stock: " + e.getMessage());
         return false;
     }
 }
}

