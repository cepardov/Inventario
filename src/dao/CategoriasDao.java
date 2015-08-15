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
import entidad.Categorias;

/**
 *
 * @author cepardov
 */
public class CategoriasDao {
    protected Connection getConnection() {
        return DataBaseInstance.getInstanceConnection();
    }

    public Object [][] getCategorias(){
        int posid = 0;
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT count(1) as total FROM categorias");
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
                    + "idCategorias,"
                    + "nombre,"
                    + "descripcion,"
                    + "porcentajeDescuento"
                    + " FROM categorias ORDER BY idCategorias");
            try (ResultSet res = pstm.executeQuery()) {
                int increment = 0;
                while(res.next()){
                    
                    String estIdCategoria = res.getString("idCategorias");
                    String estNombre= res.getString("nombre");
                    String estDescripcion = res.getString("descripcion");
                    String estPorcentajeDescuento = res.getString("porcentajeDescuento");

                    data[increment][0] = estIdCategoria;
                    data[increment][1] = estNombre;
                    data[increment][2] = estDescripcion;
                    data[increment][3] = estPorcentajeDescuento;

                    increment++;
                }
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
            }
        return data;
    }
    
    public boolean findById(Categorias categorias) {
        PreparedStatement getCategorias;
        ResultSet result = null;
        try {
            getCategorias = getConnection().prepareStatement("SELECT * FROM categorias WHERE idCategorias = ?");
            getCategorias.setString(1, categorias.getIdCategoria());
            result = getCategorias.executeQuery();
            if (result.next()) {
                categorias.setIdCategoria(result.getString("idCategorias"));
                categorias.setNombre(result.getString("nombre"));
                categorias.setDecripcion(result.getString("descripcion"));
                categorias.setPorcentajeDescuento(result.getString("porcentajeDescuento"));
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
    
    public boolean findByName(Categorias categorias) {
        PreparedStatement getCategorias;
        ResultSet result = null;
        try {
            getCategorias = getConnection().prepareStatement("SELECT * FROM categorias WHERE nombre = ?");
            getCategorias.setString(1, categorias.getNombre());
            result = getCategorias.executeQuery();
            if (result.next()) {
                categorias.setIdCategoria(result.getString("idCategorias"));
                categorias.setNombre(result.getString("nombre"));
                categorias.setDecripcion(result.getString("descripcion"));
                categorias.setPorcentajeDescuento(result.getString("porcentajeDescuento"));
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
    
    public boolean save(Categorias categorias) {
        PreparedStatement saveCategorias;
        try {
            saveCategorias = getConnection().prepareStatement(
                    "INSERT INTO categorias ("
                    + "idCategorias,"
                    + "nombre,"
                    + "descripcion,"
                    + "porcentajeDescuento) "
                    + "VALUES (?, ?, ?, ?)");
            saveCategorias.setString(1, categorias.getIdCategoria());
            saveCategorias.setString(2, categorias.getNombre());
            saveCategorias.setString(3, categorias.getDecripcion());
            saveCategorias.setString(4, categorias.getPorcentajeDescuento());
            saveCategorias.executeUpdate();

            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando saveCategorias(): "+se.getMessage());
            categorias.setError(""+errorcod);
            return false;
        }
    }
    
    public boolean update(Categorias categorias) {
        PreparedStatement saveCategorias;
        try {
            saveCategorias = getConnection().prepareStatement("UPDATE categorias SET "
                    + "idCategorias=?,"
                    + "nombre=?,"
                    + "descripcion=?,"
                    + "porcentajeDescuento=?"
                    + " WHERE idCategorias=?");
            saveCategorias.setString(1, categorias.getIdCategoria());
            saveCategorias.setString(2, categorias.getNombre());
            saveCategorias.setString(3, categorias.getDecripcion());
            saveCategorias.setString(4, categorias.getPorcentajeDescuento());
            saveCategorias.setString(5, categorias.getIdCategoria());
            saveCategorias.executeUpdate();
            
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando updateCategorias(): "+se.getMessage());
            categorias.setError(""+errorcod);
            return false;
        }
    }

    public boolean delete(Categorias categorias) {
        PreparedStatement delCategorias;
        try {

            if (categorias.getIdCategoria() != null) {
                delCategorias = getConnection().prepareStatement(
                        "DELETE FROM categorias WHERE idCategorias=?");
                delCategorias.setString(1, categorias.getIdCategoria());
                delCategorias.executeUpdate();
            }
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando deleteCategorias(): "+se.getMessage());
            categorias.setError(""+errorcod);
            return false;
        }
    }

    public void closeConnection() {
        DataBaseInstance.closeConnection();
    }
    
}
