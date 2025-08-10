package com.tns.onlineshopping.services;

//===== com/tns/onlineshopping/services/CustomerService.java =====


import com.tns.onlineshopping.entities.Customer;
import com.tns.onlineshopping.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {
 public boolean createCustomer(Customer c) {
     String sqlUser = "INSERT INTO User(userId, username, email, role) VALUES (?, ?, ?, 'CUSTOMER')";
     String sqlCustomer = "INSERT INTO Customer(userId, address) VALUES (?, ?)";
     try (Connection conn = DBConnection.getConnection()) {
         conn.setAutoCommit(false);
         try (PreparedStatement ps1 = conn.prepareStatement(sqlUser);
              PreparedStatement ps2 = conn.prepareStatement(sqlCustomer)) {
             ps1.setInt(1, c.getUserId());
             ps1.setString(2, c.getUsername());
             ps1.setString(3, c.getEmail());
             ps1.executeUpdate();

             ps2.setInt(1, c.getUserId());
             ps2.setString(2, c.getAddress());
             ps2.executeUpdate();
             conn.commit();
             return true;
         } catch (SQLException ex) {
             conn.rollback();
             System.out.println("Error creating customer: " + ex.getMessage());
             return false;
         } finally {
             conn.setAutoCommit(true);
         }
     } catch (SQLException e) {
         System.out.println("DB connection error: " + e.getMessage());
         return false;
     }
 }

 public List<Customer> getAllCustomers() {
     List<Customer> list = new ArrayList<>();
     String sql = "SELECT u.userId, u.username, u.email, c.address FROM User u JOIN Customer c ON u.userId = c.userId";
     try (Connection conn = DBConnection.getConnection();
          Statement st = conn.createStatement();
          ResultSet rs = st.executeQuery(sql)) {
         while (rs.next()) {
             list.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
         }
     } catch (SQLException e) {
         System.out.println("Error fetching customers: " + e.getMessage());
     }
     return list;
 }
}
