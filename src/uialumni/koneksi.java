/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uialumni;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ACER
 */
public class koneksi {
    
    private static Connection mysqlconfiq;
    
    public static Connection konek (){
        try {
            
            String url = "jdbc:mysql://localhost:3306/alumni_202557201044";
            
            String user = "root";
            
            String pass = "";
            
            mysqlconfiq = DriverManager.getConnection(url, user, pass);
            
        } catch (SQLException sQLException) {
            System.err.println(sQLException.getMessage());
        }
        return mysqlconfiq;
    }
    
}
