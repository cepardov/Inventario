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
import entidad.Producto;

/**
 *
 * @author cepardov
 */
public class ProductoDao {
    protected Connection getConnection() {
        return DataBaseInstance.getInstanceConnection();
    }

    public Object [][] getProducto(){
        int posid = 0;
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT count(1) as total FROM producto");
            try (ResultSet res = pstm.executeQuery()) {
                res.next();
                posid = res.getInt("total");
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
        }
        Object[][] data = new String[posid][15];
        try{
            PreparedStatement pstm = getConnection().prepareStatement("SELECT "
                    + "idProducto,"
                    + "codReferencia,"
                    + "ean,"
                    + "nombre,"
                    + "descripcion,"
                    + "fechaIngreso,"
                    + "valorProveedor,"
                    + "valorNeto,"
                    + "valorIva,"
                    + "porcentajeDescuento,"
                    + "vencimientoDescuento,"
                    + "cantidad,"
                    + "idCategoria,"
                    + "idProveedor,"
                    + "idUnidadMedida"
                    + " FROM producto ORDER BY idProducto");
            try (ResultSet res = pstm.executeQuery()) {
                int increment = 0;
                while(res.next()){
                    
                    String estIdProducto = res.getString("idProducto");
                    String estCodReferencia = res.getString("codReferencia");
                    String estEan = res.getString("ean");
                    String estNombre = res.getString("nombre");
                    String estDescripcion = res.getString("descripcion");
                    String estFechaIngreso = res.getString("fechaIngreso");
                    String estValorProveedor = res.getString("valorProveedor");
                    String estValorNeto = res.getString("valorNeto");
                    String estValorIva = res.getString("valorIva");
                    String estPorcentajeDescuento = res.getString("porcentajeDescuento");
                    String estVencimientoDescuento = res.getString("vencimientoDescuento");
                    String estCantidad = res.getString("cantidad");
                    String estIdCategoria = res.getString("idCategoria");
                    String estIdProveedor = res.getString("idProveedor");
                    String estIdUnidadMedida = res.getString("idUnidadMedida");

                    data[increment][0] = estIdProducto;
                    data[increment][1] = estCodReferencia;
                    data[increment][2] = estEan;
                    data[increment][3] = estNombre;
                    data[increment][4] = estDescripcion;
                    data[increment][5] = estFechaIngreso;
                    data[increment][6] = estValorProveedor;
                    data[increment][7] = estValorNeto;
                    data[increment][8] = estValorIva;
                    data[increment][9] = estPorcentajeDescuento;
                    data[increment][10] = estVencimientoDescuento;
                    data[increment][11] = estCantidad;
                    data[increment][12] = estIdCategoria;
                    data[increment][13] = estIdProveedor;
                    data[increment][14] = estIdUnidadMedida;

                    increment++;
                }
            }
            this.closeConnection();
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, se);
            }
        return data;
    }
    
    public boolean findById(Producto producto) {
        PreparedStatement getProducto;
        ResultSet result = null;
        try {
            getProducto = getConnection().prepareStatement("SELECT * FROM producto WHERE idProducto = ?");
            getProducto.setString(1, producto.getIdProducto());
            result = getProducto.executeQuery();
            if (result.next()) {
                producto.setIdProducto(result.getString("idProducto"));
                producto.setCodReferencia(result.getString("codReferencia"));
                producto.setEan(result.getString("ean"));
                producto.setNombre(result.getString("nombre"));
                producto.setDescripcion(result.getString("descripcion"));
                producto.setFechaIngreso(result.getString("fechaIngreso"));
                producto.setValorProveedor(result.getString("valorProveedor"));
                producto.setValorNeto(result.getString("valorNeto"));
                producto.setValorIva(result.getString("valorIva"));
                producto.setPorcentajeDescuento(result.getString("porcentajeDescuento"));
                producto.setVencimientoDescuento(result.getString("vencimientoDescuento"));
                producto.setCantidad(result.getString("cantidad"));
                producto.setIdCategoria(result.getString("idCategoria"));
                producto.setIdProveedor(result.getString("idProveedor"));
                producto.setIdUnidadMedida(result.getString("idUnidadMedida"));
                
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
    
    public boolean findByEan(Producto producto) {
        PreparedStatement getProducto;
        ResultSet result = null;
        try {
            getProducto = getConnection().prepareStatement("SELECT * FROM producto WHERE ean = ?");
            getProducto.setString(1, producto.getEan());
            result = getProducto.executeQuery();
            if (result.next()) {
                producto.setIdProducto(result.getString("idProducto"));
                producto.setCodReferencia(result.getString("codReferencia"));
                producto.setEan(result.getString("ean"));
                producto.setNombre(result.getString("nombre"));
                producto.setDescripcion(result.getString("descripcion"));
                producto.setFechaIngreso(result.getString("fechaIngreso"));
                producto.setValorProveedor(result.getString("valorProveedor"));
                producto.setValorNeto(result.getString("valorNeto"));
                producto.setValorIva(result.getString("valorIva"));
                producto.setPorcentajeDescuento(result.getString("porcentajeDescuento"));
                producto.setVencimientoDescuento(result.getString("vencimientoDescuento"));
                producto.setCantidad(result.getString("cantidad"));
                producto.setIdCategoria(result.getString("idCategoria"));
                producto.setIdProveedor(result.getString("idProveedor"));
                producto.setIdUnidadMedida(result.getString("idUnidadMedida"));
                
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
    
    public boolean findByRef(Producto producto) {
        PreparedStatement getProducto;
        ResultSet result = null;
        try {
            getProducto = getConnection().prepareStatement("SELECT * FROM producto WHERE codReferencia = ?");
            getProducto.setString(1, producto.getCodReferencia());
            result = getProducto.executeQuery();
            if (result.next()) {
                producto.setIdProducto(result.getString("idProducto"));
                producto.setCodReferencia(result.getString("codReferencia"));
                producto.setEan(result.getString("ean"));
                producto.setNombre(result.getString("nombre"));
                producto.setDescripcion(result.getString("descripcion"));
                producto.setFechaIngreso(result.getString("fechaIngreso"));
                producto.setValorProveedor(result.getString("valorProveedor"));
                producto.setValorNeto(result.getString("valorNeto"));
                producto.setValorIva(result.getString("valorIva"));
                producto.setPorcentajeDescuento(result.getString("porcentajeDescuento"));
                producto.setVencimientoDescuento(result.getString("vencimientoDescuento"));
                producto.setCantidad(result.getString("cantidad"));
                producto.setIdCategoria(result.getString("idCategoria"));
                producto.setIdProveedor(result.getString("idProveedor"));
                producto.setIdUnidadMedida(result.getString("idUnidadMedida"));
                
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
    
    public boolean findByName(Producto producto) {
        PreparedStatement getProducto;
        ResultSet result = null;
        try {
            getProducto = getConnection().prepareStatement("SELECT * FROM producto WHERE nombre = ?");
            getProducto.setString(1, producto.getNombre());
            result = getProducto.executeQuery();
            if (result.next()) {
                producto.setIdProducto(result.getString("idProducto"));
                producto.setCodReferencia(result.getString("codReferencia"));
                producto.setEan(result.getString("ean"));
                producto.setNombre(result.getString("nombre"));
                producto.setDescripcion(result.getString("descripcion"));
                producto.setFechaIngreso(result.getString("fechaIngreso"));
                producto.setValorProveedor(result.getString("valorProveedor"));
                producto.setValorNeto(result.getString("valorNeto"));
                producto.setValorIva(result.getString("valorIva"));
                producto.setPorcentajeDescuento(result.getString("porcentajeDescuento"));
                producto.setVencimientoDescuento(result.getString("vencimientoDescuento"));
                producto.setCantidad(result.getString("cantidad"));
                producto.setIdCategoria(result.getString("idCategoria"));
                producto.setIdProveedor(result.getString("idProveedor"));
                producto.setIdUnidadMedida(result.getString("idUnidadMedida"));
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
    
    public boolean save(Producto producto) {
        PreparedStatement saveProducto;
        try {
            saveProducto = getConnection().prepareStatement(
                    "INSERT INTO producto ("
                    + "idProducto,"
                    + "codReferencia,"
                    + "ean,"
                    + "nombre,"
                    + "descripcion,"
                    + "fechaIngreso,"
                    + "valorProveedor,"
                    + "valorNeto,"
                    + "valorIva,"
                    + "porcentajeDescuento,"
                    + "vencimientoDescuento,"
                    + "cantidad,"
                    + "idCategoria,"
                    + "idProveedor,"
                    + "idUnidadMedida)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            saveProducto.setString(1, producto.getIdProducto());
            saveProducto.setString(2, producto.getCodReferencia());
            saveProducto.setString(3, producto.getEan());
            saveProducto.setString(4, producto.getNombre());
            saveProducto.setString(5, producto.getDescripcion());
            saveProducto.setString(6, producto.getFechaIngreso());
            saveProducto.setString(7, producto.getValorProveedor());
            saveProducto.setString(8, producto.getValorNeto());
            saveProducto.setString(9, producto.getValorIva());
            saveProducto.setString(10, producto.getPorcentajeDescuento());
            saveProducto.setString(11, producto.getVencimientoDescuento());
            saveProducto.setString(12, producto.getCantidad());
            saveProducto.setString(13, producto.getIdCategoria());
            saveProducto.setString(14, producto.getIdProveedor());
            saveProducto.setString(15, producto.getIdUnidadMedida());

            saveProducto.executeUpdate();

            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando saveProducto(): "+se.getMessage());
            producto.setError(""+errorcod);
            return false;
        }
    }
    
    public boolean update(Producto producto) {
        PreparedStatement saveProducto;
        try {
            saveProducto = getConnection().prepareStatement("UPDATE producto SET "
                    + "idProducto=?,"
                    + "codReferencia=?,"
                    + "ean=?,"
                    + "nombre=?,"
                    + "descripcion=?,"
                    + "fechaIngreso=?,"
                    + "valorProveedor=?,"
                    + "valorNeto=?,"
                    + "valorIva=?,"
                    + "porcentajeDescuento=?,"
                    + "vencimientoDescuento=?,"
                    + "cantidad=?,"
                    + "idCategoria=?,"
                    + "idProveedor=?,"
                    + "idUnidadMedida=?"
                    + " WHERE idProducto=?");
            saveProducto.setString(1, producto.getIdProducto());
            saveProducto.setString(2, producto.getCodReferencia());
            saveProducto.setString(3, producto.getEan());
            saveProducto.setString(4, producto.getNombre());
            saveProducto.setString(5, producto.getDescripcion());
            saveProducto.setString(6, producto.getFechaIngreso());
            saveProducto.setString(7, producto.getValorProveedor());
            saveProducto.setString(8, producto.getValorNeto());
            saveProducto.setString(9, producto.getValorIva());
            saveProducto.setString(10, producto.getPorcentajeDescuento());
            saveProducto.setString(11, producto.getVencimientoDescuento());
            saveProducto.setString(12, producto.getCantidad());
            saveProducto.setString(13, producto.getIdCategoria());
            saveProducto.setString(14, producto.getIdProveedor());
            saveProducto.setString(15, producto.getIdUnidadMedida());
            saveProducto.setString(16, producto.getIdProducto());
            saveProducto.executeUpdate();
            
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando updateProducto(): "+se.getMessage());
            producto.setError(""+errorcod);
            return false;
        }
    }

    public boolean delete(Producto producto) {
        PreparedStatement delProducto;
        try {

            if (producto.getIdProducto() != null) {
                delProducto = getConnection().prepareStatement(
                        "DELETE FROM producto WHERE idProducto=?");
                delProducto.setString(1, producto.getIdProducto());
                delProducto.executeUpdate();
            }
            this.closeConnection();
            return true;
        } catch (SQLException se) {
            int errorcod=se.getErrorCode();
            System.err.println("Debug: ("+errorcod+") Error ejecutando deleteProducto(): "+se.getMessage());
            producto.setError(""+errorcod);
            return false;
        }
    }

    public void closeConnection() {
        DataBaseInstance.closeConnection();
    }
    
}
