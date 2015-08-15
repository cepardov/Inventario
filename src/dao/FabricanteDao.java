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
import entidad.Fabricante;

/**
 *
 * @author cepardov
 */
public class FabricanteDao {
    protected Connection getConnection() {
        return DataBaseInstance.getInstanceConnection();
    }

    public Object [][] getFabricante(){
        int posid = 0;
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT count(1) as total FROM fabricante");
            try (ResultSet res = pstm.executeQuery()) {
                res.next();
                posid = res.getInt("total");
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
        }
        Object[][] data = new String[posid][5];
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT "
                    + "idFabricante,"
                    + "identificador,"
                    + "nombre,"
                    + "descripcion,"
                    + "idProveedor"
                    + " FROM fabricante ORDER BY idFabricante");
            try (ResultSet res = pstm.executeQuery()) {
                int increment = 0;
                while(res.next()){
                    
                    String estIdFabricante = res.getString("idFabricante");
                    String estIdentificador = res.getString("identificador");
                    String estNombre= res.getString("nombre");
                    String estDescripcion = res.getString("descripcion");
                    String estIdProveedor = res.getString("idProveedor");

                    data[increment][0] = estIdFabricante;
                    data[increment][1] = estIdentificador;
                    data[increment][2] = estNombre;
                    data[increment][3] = estDescripcion;
                    data[increment][4] = estIdProveedor;

                    increment++;
                }
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
            }
        return data;
    }
    
    public boolean findById(Fabricante fabricante) {
        System.out.println("Accion");
        PreparedStatement getFabricante;
        ResultSet result = null;
        try {
            getFabricante = getConnection().prepareStatement("SELECT * FROM fabricante WHERE idFabricante = ?");
            getFabricante.setString(1, fabricante.getIdFabricante());
            result = getFabricante.executeQuery();
            if (result.next()) {
                fabricante.setIdFabricante(result.getString("idFabricante"));
                fabricante.setIdentificador(result.getString("identificador"));
                fabricante.setNombre(result.getString("nombre"));
                fabricante.setDescripcion(result.getString("descripcion"));
                fabricante.setIdProveedor(result.getString("idProveedor"));
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
    
    public boolean findByIdentificador(Fabricante fabricante) {
        PreparedStatement getFabricante;
        ResultSet result = null;
        try {
            getFabricante = getConnection().prepareStatement("SELECT * FROM fabricante WHERE identificador = ?");
            getFabricante.setString(1, fabricante.getIdentificador());
            result = getFabricante.executeQuery();
            if (result.next()) {
                fabricante.setIdFabricante(result.getString("idFabricante"));
                fabricante.setIdentificador(result.getString("identificador"));
                fabricante.setNombre(result.getString("nombre"));
                fabricante.setDescripcion(result.getString("descripcion"));
                fabricante.setIdProveedor(result.getString("idProveedor"));
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
    
    public boolean findByName(Fabricante fabricante) {
        PreparedStatement getFabricante;
        ResultSet result = null;
        try {
            getFabricante = getConnection().prepareStatement("SELECT * FROM fabricante WHERE nombre = ?");
            getFabricante.setString(1, fabricante.getNombre());
            result = getFabricante.executeQuery();
            if (result.next()) {
                fabricante.setIdFabricante(result.getString("idFabricante"));
                fabricante.setIdentificador(result.getString("identificador"));
                fabricante.setNombre(result.getString("nombre"));
                fabricante.setDescripcion(result.getString("descripcion"));
                fabricante.setIdProveedor(result.getString("idProveedor"));
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
    
    public boolean save(Fabricante fabricante) {
        PreparedStatement saveFabricante;
        try {
            saveFabricante = getConnection().prepareStatement(
                    "INSERT INTO fabricante ("
                    + "idFabricante,"
                    + "identificador,"
                    + "nombre,"
                    + "descripcion,"
                    + "idProveedor)"
                    + " VALUES (?, ?, ?, ?, ?)");
            saveFabricante.setString(1, fabricante.getIdFabricante());
            saveFabricante.setString(2, fabricante.getIdentificador());
            saveFabricante.setString(3, fabricante.getNombre());
            saveFabricante.setString(4, fabricante.getDescripcion());
            saveFabricante.setString(5, fabricante.getIdProveedor());
            saveFabricante.executeUpdate();

            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando saveFabricante(): "+se.getMessage());
            fabricante.setError(""+errorcod);
            return false;
        }
    }
    
    public boolean update(Fabricante fabricante) {
        PreparedStatement saveFabricante;
        try {
            saveFabricante = getConnection().prepareStatement("UPDATE fabricante SET "
                    + "idFabricante=?,"
                    + "identificador=?,"
                    + "nombre=?,"
                    + "descripcion=?,"
                    + "idProveedor=?"
                    + " WHERE idFabricante=?");
            saveFabricante.setString(1, fabricante.getIdFabricante());
            saveFabricante.setString(2, fabricante.getIdentificador());
            saveFabricante.setString(3, fabricante.getNombre());
            saveFabricante.setString(4, fabricante.getDescripcion());
            saveFabricante.setString(5, fabricante.getIdProveedor());
            saveFabricante.setString(6, fabricante.getIdFabricante());
            saveFabricante.executeUpdate();
            
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            switch(errorcod){
                case 1062:
                    fabricante.setError("["+errorcod+"] Entrada duplicada\n"+se.getMessage());
                    break;
                default:
                    fabricante.setError(""+errorcod);
                    break;
            }
            System.err.println("Debug: ("+errorcod+") Error ejecutando updateFabricante(): "+se.getMessage());
            
            return false;
        }
    }

    public boolean delete(Fabricante fabricante) {
        PreparedStatement delFabricante;
        try {

            if (fabricante.getIdFabricante() != null) {
                delFabricante = getConnection().prepareStatement(
                        "DELETE FROM fabricante WHERE idFabricante=?");
                delFabricante.setString(1, fabricante.getIdFabricante());
                delFabricante.executeUpdate();
            }
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando deleteFabricante(): "+se.getMessage());
            fabricante.setError(""+errorcod);
            return false;
        }
    }

    public void closeConnection() {
        DataBaseInstance.closeConnection();
    }
    
}
