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
import entidad.Ejecutivo;

/**
 *
 * @author cepardov
 */
public class EjecutivoDao {
    protected Connection getConnection() {
        return DataBaseInstance.getInstanceConnection();
    }

    public Object [][] getEjecutivo(){
        int posid = 0;
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT count(1) as total FROM ejecutivo");
            try (ResultSet res = pstm.executeQuery()) {
                res.next();
                posid = res.getInt("total");
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
        }
        Object[][] data = new String[posid][9];
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT "
                    + "idEjecutivo,"
                    + "identificador,"
                    + "nombre,"
                    + "apellido,"
                    + "descripcion,"
                    + "correo,"
                    + "telefono,"
                    + "movil,"
                    + "idProveedor"
                    + " FROM ejecutivo ORDER BY idEjecutivo");
            try (ResultSet res = pstm.executeQuery()) {
                int increment = 0;
                while(res.next()){
                    
                    String estIdEjecutivo = res.getString("idEjecutivo");
                    String estIdentificador = res.getString("identificador");
                    String estNombre= res.getString("nombre");
                    String estApellido = res.getString("apellido");
                    String estDescripcion = res.getString("descripcion");
                    String estCorreo = res.getString("correo");
                    String estTelefono = res.getString("telefono");
                    String estMovil = res.getString("movil");
                    String estIdProveedor = res.getString("idProveedor");

                    data[increment][0] = estIdEjecutivo;
                    data[increment][1] = estIdentificador;
                    data[increment][2] = estNombre;
                    data[increment][3] = estApellido;
                    data[increment][4] = estDescripcion;
                    data[increment][5] = estCorreo;
                    data[increment][6] = estTelefono;
                    data[increment][7] = estMovil;
                    data[increment][8] = estIdProveedor;

                    increment++;
                }
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
            }
        return data;
    }
    
    public boolean findById(Ejecutivo ejecutivo) {
        PreparedStatement getEjecutivo;
        ResultSet result = null;
        try {
            getEjecutivo = getConnection().prepareStatement("SELECT * FROM ejecutivo WHERE idEjecutivo = ?");
            getEjecutivo.setString(1, ejecutivo.getIdEjecutivo());
            result = getEjecutivo.executeQuery();
            if (result.next()) {
                ejecutivo.setIdEjecutivo(result.getString("idEjecutivo"));
                ejecutivo.setIdentificador(result.getString("identificador"));
                ejecutivo.setNombre(result.getString("nombre"));
                ejecutivo.setApellido(result.getString("apellido"));
                ejecutivo.setDescripcion(result.getString("descripcion"));
                ejecutivo.setCorreo(result.getString("correo"));
                ejecutivo.setTelefono(result.getString("telefono"));
                ejecutivo.setMovil(result.getString("movil"));
                ejecutivo.setIdProveedor(result.getString("idProveedor"));
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
    
    public boolean findByName(Ejecutivo ejecutivo) {
        PreparedStatement getEjecutivo;
        ResultSet result = null;
        try {
            getEjecutivo = getConnection().prepareStatement("SELECT * FROM ejecutivo WHERE nombre = ?");
            getEjecutivo.setString(1, ejecutivo.getNombre());
            result = getEjecutivo.executeQuery();
            if (result.next()) {
                ejecutivo.setIdEjecutivo(result.getString("idEjecutivo"));
                ejecutivo.setIdentificador(result.getString("identificador"));
                ejecutivo.setNombre(result.getString("nombre"));
                ejecutivo.setApellido(result.getString("apellido"));
                ejecutivo.setDescripcion(result.getString("descripcion"));
                ejecutivo.setCorreo(result.getString("correo"));
                ejecutivo.setTelefono(result.getString("telefono"));
                ejecutivo.setMovil(result.getString("movil"));
                ejecutivo.setIdProveedor(result.getString("idProveedor"));
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
    
    public boolean findByApellido(Ejecutivo ejecutivo) {
        PreparedStatement getEjecutivo;
        ResultSet result = null;
        try {
            getEjecutivo = getConnection().prepareStatement("SELECT * FROM ejecutivo WHERE apellido = ?");
            getEjecutivo.setString(1, ejecutivo.getApellido());
            result = getEjecutivo.executeQuery();
            if (result.next()) {
                ejecutivo.setIdEjecutivo(result.getString("idEjecutivo"));
                ejecutivo.setIdentificador(result.getString("identificador"));
                ejecutivo.setNombre(result.getString("nombre"));
                ejecutivo.setApellido(result.getString("apellido"));
                ejecutivo.setDescripcion(result.getString("descripcion"));
                ejecutivo.setCorreo(result.getString("correo"));
                ejecutivo.setTelefono(result.getString("telefono"));
                ejecutivo.setMovil(result.getString("movil"));
                ejecutivo.setIdProveedor(result.getString("idProveedor"));
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
    
    public boolean save(Ejecutivo ejecutivo) {
        PreparedStatement saveEjecutivo;
        try {
            saveEjecutivo = getConnection().prepareStatement(
                    "INSERT INTO ejecutivo ("
                    + "idEjecutivo,"
                    + "identificador,"
                    + "nombre,"
                    + "apellido,"
                    + "descripcion,"
                    + "correo,"
                    + "telefono,"
                    + "movil,"
                    + "idProveedor)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            saveEjecutivo.setString(1, ejecutivo.getIdEjecutivo());
            saveEjecutivo.setString(2, ejecutivo.getIdentificador());
            saveEjecutivo.setString(3, ejecutivo.getNombre());
            saveEjecutivo.setString(4, ejecutivo.getApellido());
            saveEjecutivo.setString(5, ejecutivo.getDescripcion());
            saveEjecutivo.setString(6, ejecutivo.getCorreo());
            saveEjecutivo.setString(7, ejecutivo.getTelefono());
            saveEjecutivo.setString(8, ejecutivo.getMovil());
            saveEjecutivo.setString(9, ejecutivo.getIdProveedor());
            saveEjecutivo.executeUpdate();

            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando saveEjecutivo(): "+se.getMessage());
            ejecutivo.setError(""+errorcod);
            return false;
        }
    }
    
    public boolean update(Ejecutivo ejecutivo) {
        PreparedStatement saveEjecutivo;
        try {
            saveEjecutivo = getConnection().prepareStatement("UPDATE ejecutivo SET "
                    + "idEjecutivo=?,"
                    + "identificador=?,"
                    + "nombre=?,"
                    + "apellido=?,"
                    + "descripcion=?,"
                    + "correo=?,"
                    + "telefono=?,"
                    + "movil=?,"
                    + "idProveedor=?"
                    + " WHERE idEjecutivo=?");
            saveEjecutivo.setString(1, ejecutivo.getIdEjecutivo());
            saveEjecutivo.setString(2, ejecutivo.getIdentificador());
            saveEjecutivo.setString(3, ejecutivo.getNombre());
            saveEjecutivo.setString(4, ejecutivo.getApellido());
            saveEjecutivo.setString(5, ejecutivo.getDescripcion());
            saveEjecutivo.setString(6, ejecutivo.getCorreo());
            saveEjecutivo.setString(7, ejecutivo.getTelefono());
            saveEjecutivo.setString(8, ejecutivo.getMovil());
            saveEjecutivo.setString(9, ejecutivo.getIdProveedor());
            saveEjecutivo.setString(10, ejecutivo.getIdEjecutivo());
            saveEjecutivo.executeUpdate();
            
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando updateEjecutivo(): "+se.getMessage());
            ejecutivo.setError(""+errorcod);
            return false;
        }
    }

    public boolean delete(Ejecutivo ejecutivo) {
        PreparedStatement delEjecutivo;
        try {

            if (ejecutivo.getIdEjecutivo() != null) {
                delEjecutivo = getConnection().prepareStatement(
                        "DELETE FROM ejecutivo WHERE idEjecutivo=?");
                delEjecutivo.setString(1, ejecutivo.getIdEjecutivo());
                delEjecutivo.executeUpdate();
            }
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando deleteEjecutivo(): "+se.getMessage());
            ejecutivo.setError(""+errorcod);
            return false;
        }
    }

    public void closeConnection() {
        DataBaseInstance.closeConnection();
    }
    
}
