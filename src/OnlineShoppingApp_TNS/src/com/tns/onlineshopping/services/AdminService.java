package com.tns.onlineshopping.services;

//===== com/tns/onlineshopping/services/AdminService.java =====


import com.tns.onlineshopping.entities.Admin;
import com.tns.onlineshopping.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminService {
 public boolean createAdmin(Admin a) {
     String sqlUser = "INSERT INTO User(userId, username, email, role) VALUES (?, ?, ?, 'ADMIN')";
     String sqlAdmin = "INSERT INTO Admin(userId) VALUES (?)";
     try (Connection conn = DBConnection.getConnection()) {
         conn.setAutoCommit(false);
         try (PreparedStatement ps1 = conn.prepareStatement(sqlUser);
              PreparedStatement ps2 = conn.prepareStatement(sqlAdmin)) {
             ps1.setInt(1, a.getUserId());
             ps1.setString(2, a.getUsername());
             ps1.setString(3, a.getEmail());
             ps1.executeUpdate();

             ps2.setInt(1, a.getUserId());
             ps2.executeUpdate();
             conn.commit();
             return true;
         } catch (SQLException ex) {
             conn.rollback();
             System.out.println("Error creating admin: " + ex.getMessage());
             return false;
         } finally {
             conn.setAutoCommit(true);
         }
     } catch (SQLException e) {
         System.out.println("DB connection error: " + e.getMessage());
         return false;
     }
 }

 public List<Admin> getAllAdmins() {
     List<Admin> list = new ArrayList<>();
     String sql = "SELECT u.userId, u.username, u.email FROM User u JOIN Admin a ON u.userId = a.userId";
     try (Connection conn = DBConnection.getConnection();
          Statement st = conn.createStatement();
          ResultSet rs = st.executeQuery(sql)) {
         while (rs.next()) {
             list.add(new Admin(rs.getInt(1), rs.getString(2), rs.getString(3)));
         }
     } catch (SQLException e) {
         System.out.println("Error fetching admins: " + e.getMessage());
     }
     return list;
 }
}

