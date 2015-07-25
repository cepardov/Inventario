/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexion.DataBaseInstance;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import entidad.Proveedor;

/**
 *
 * @author cepardov
 */
public class ProveedorDao {
    protected Connection getConnection() {
        return DataBaseInstance.getInstanceConnection();
    }

    public Object [][] getProveedor(){
        int posid = 0;
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT count(1) as total FROM proveedor");
            try (ResultSet res = pstm.executeQuery()) {
                res.next();
                posid = res.getInt("total");
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
        }
        Object[][] data = new String[posid][4];
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT "
                    + "idProveedor,"
                    + "rut,"
                    + "nombre,"
                    + "descripcion"
                    + " FROM proveedor ORDER BY idProveedor");
            try (ResultSet res = pstm.executeQuery()) {
                int increment = 0;
                while(res.next()){
                    
                    String estIdProveedor = res.getString("idProveedor");
                    String estRut = res.getString("rut");
                    String estNombre= res.getString("nombre");
                    String estDescripcion = res.getString("descripcion");
                    

                    data[increment][0] = estIdProveedor;
                    data[increment][1] = estRut;
                    data[increment][2] = estNombre;
                    data[increment][3] = estDescripcion;

                    increment++;
                }
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
            }
        return data;
    }
    
    public boolean findById(Proveedor proveedor) {
        PreparedStatement getProveedor;
        ResultSet result = null;
        try {
            getProveedor = getConnection().prepareStatement("SELECT * FROM proveedor WHERE idProveedor = ?");
            getProveedor.setString(1, proveedor.getIdProveedor());
            result = getProveedor.executeQuery();
            if (result.next()) {
                proveedor.setIdProveedor(result.getString("idProveedor"));
                proveedor.setRut(result.getString("rut"));
                proveedor.setNombre(result.getString("nombre"));
                proveedor.setDescripcion(result.getString("descripcion"));
                result.close();
            } else {
                return false;
            }
            closeConnection();
            return true;
        } catch (SQLException se) {
            System.err.println("Se ha producido un error de BD.");
            System.err.println(se.getMessage());
            return false;
        }
    }
    
    public boolean findByName(Proveedor proveedor) {
        PreparedStatement getProveedor;
        ResultSet result = null;
        try {
            getProveedor = getConnection().prepareStatement("SELECT * FROM proveedor WHERE nombre = ?");
            getProveedor.setString(1, proveedor.getNombre());
            result = getProveedor.executeQuery();
            if (result.next()) {
                proveedor.setIdProveedor(result.getString("idProveedor"));
                proveedor.setRut(result.getString("rut"));
                proveedor.setNombre(result.getString("nombre"));
                proveedor.setDescripcion(result.getString("descripcion"));
                result.close();
            } else {
                return false;
            }
            closeConnection();
            return true;
        } catch (SQLException se) {
            System.err.println("Se ha producido un error de BD.");
            System.err.println(se.getMessage());
            return false;
        }
    }
    
    public boolean save(Proveedor proveedor) {
        PreparedStatement saveProveedor;
        try {
            saveProveedor = getConnection().prepareStatement(
                    "INSERT INTO proveedor ("
                    + "idProveedor,"
                    + "rut,"
                    + "nombre,"
                    + "descripcion)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            saveProveedor.setString(1, proveedor.getIdProveedor());
            saveProveedor.setString(2, proveedor.getRut());
            saveProveedor.setString(3, proveedor.getNombre());
            saveProveedor.setString(4, proveedor.getDescripcion());
            saveProveedor.executeUpdate();

            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando saveProveedor(): "+se.getMessage());
            proveedor.setError(""+errorcod);
            return false;
        }
    }
    
    public boolean update(Proveedor proveedor) {
        PreparedStatement saveProveedor;
        try {
            saveProveedor = getConnection().prepareStatement("UPDATE proveedor SET "
                    + "idProveedor=?,"
                    + "rut=?,"
                    + "nombre=?,"
                    + "descripcion=?"
                    + " WHERE idProveedor=?");
            saveProveedor.setString(1, proveedor.getIdProveedor());
            saveProveedor.setString(2, proveedor.getRut());
            saveProveedor.setString(3, proveedor.getNombre());
            saveProveedor.setString(4, proveedor.getDescripcion());
            saveProveedor.setString(5, proveedor.getIdProveedor());
            saveProveedor.executeUpdate();
            
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando updateProveedor(): "+se.getMessage());
            proveedor.setError(""+errorcod);
            return false;
        }
    }

    public boolean delete(Proveedor proveedor) {
        PreparedStatement delProveedor;
        try {

            if (proveedor.getIdProveedor() != null) {
                delProveedor = getConnection().prepareStatement(
                        "DELETE FROM proveedor WHERE idProveedor=?");
                delProveedor.setString(1, proveedor.getIdProveedor());
                delProveedor.executeUpdate();
            }
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando deleteProveedor(): "+se.getMessage());
            proveedor.setError(""+errorcod);
            return false;
        }
    }

    public void closeConnection() {
        DataBaseInstance.closeConnection();
    }
    
}
