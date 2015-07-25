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
import entidad.Unidadmedida;

/**
 *
 * @author cepardov
 */
public class UnidadmedidaDao {
    protected Connection getConnection() {
        return DataBaseInstance.getInstanceConnection();
    }

    public Object [][] getUnidadmedida(){
        int posid = 0;
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT count(1) as total FROM unidadmedida");
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
                    + "idUnidadmedida,"
                    + "valor"
                    + " FROM unidadmedida ORDER BY idUnidadmedida");
            try (ResultSet res = pstm.executeQuery()) {
                int increment = 0;
                while(res.next()){
                    
                    String estIdUnidadmedida = res.getString("idUnidadmedida");
                    String estValor = res.getString("valor");                    

                    data[increment][0] = estIdUnidadmedida;
                    data[increment][1] = estValor;

                    increment++;
                }
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
            }
        return data;
    }
    
    public boolean findById(Unidadmedida unidadmedida) {
        PreparedStatement getUnidadmedida;
        ResultSet result = null;
        try {
            getUnidadmedida = getConnection().prepareStatement("SELECT * FROM unidadmedida WHERE idUnidadmedida = ?");
            getUnidadmedida.setString(1, unidadmedida.getIdUnidadmedida());
            result = getUnidadmedida.executeQuery();
            if (result.next()) {
                unidadmedida.setIdUnidadmedida(result.getString("idUnidadmedida"));
                unidadmedida.setValor(result.getString("valor"));
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
    
    public boolean findByValue(Unidadmedida unidadmedida) {
        PreparedStatement getUnidadmedida;
        ResultSet result = null;
        try {
            getUnidadmedida = getConnection().prepareStatement("SELECT * FROM unidadmedida WHERE valor = ?");
            getUnidadmedida.setString(1, unidadmedida.getValor());
            result = getUnidadmedida.executeQuery();
            if (result.next()) {
                unidadmedida.setIdUnidadmedida(result.getString("idUnidadmedida"));
                unidadmedida.setValor(result.getString("valor"));
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
    
    public boolean save(Unidadmedida unidadmedida) {
        PreparedStatement saveUnidadmedida;
        try {
            saveUnidadmedida = getConnection().prepareStatement(
                    "INSERT INTO unidadmedida ("
                    + "idUnidadmedida,"
                    + "valor)"
                    + " VALUES (?, ?)");
            saveUnidadmedida.setString(1, unidadmedida.getIdUnidadmedida());
            saveUnidadmedida.setString(2, unidadmedida.getValor());
            saveUnidadmedida.executeUpdate();

            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando saveUnidadmedida(): "+se.getMessage());
            unidadmedida.setError(""+errorcod);
            return false;
        }
    }
    
    public boolean update(Unidadmedida unidadmedida) {
        PreparedStatement saveUnidadmedida;
        try {
            saveUnidadmedida = getConnection().prepareStatement("UPDATE unidadmedida SET "
                    + "idUnidadmedida=?,"
                    + "valor=?"
                    + " WHERE idUnidadmedida=?");
            saveUnidadmedida.setString(1, unidadmedida.getIdUnidadmedida());
            saveUnidadmedida.setString(2, unidadmedida.getValor());
            saveUnidadmedida.setString(3, unidadmedida.getIdUnidadmedida());
            saveUnidadmedida.executeUpdate();
            
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando updateUnidadmedida(): "+se.getMessage());
            unidadmedida.setError(""+errorcod);
            return false;
        }
    }

    public boolean delete(Unidadmedida unidadmedida) {
        PreparedStatement delUnidadmedida;
        try {

            if (unidadmedida.getIdUnidadmedida() != null) {
                delUnidadmedida = getConnection().prepareStatement(
                        "DELETE FROM unidadmedida WHERE idUnidadmedida=?");
                delUnidadmedida.setString(1, unidadmedida.getIdUnidadmedida());
                delUnidadmedida.executeUpdate();
            }
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando deleteUnidadmedida(): "+se.getMessage());
            unidadmedida.setError(""+errorcod);
            return false;
        }
    }

    public void closeConnection() {
        DataBaseInstance.closeConnection();
    }
    
}
