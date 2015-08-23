/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ProductoDao;
import entidad.Producto;

/**
 * Producto
 * @author cepardov
 */
public class ProductoBeans extends Producto{
    ProductoDao productoDao=new ProductoDao();
    
    public boolean save(){
        return productoDao.save(this);
    }
    public boolean delete(){
        return productoDao.delete(this);
    }
    public boolean update(){
        return productoDao.update(this);
    }
    public boolean findByID(){
        return productoDao.findById(this);
    }
    public boolean findByEan(){
        return productoDao.findByEan(this);
    }
    public boolean findByRef(){
        return productoDao.findByRef(this);
    }
    public boolean findByName(){
        return productoDao.findByName(this);
    }
    public Object[][] getProducto(){
        return productoDao.getProducto();
    }
    public Object[][] getProductoFilter(String column, String operator, String value, String order){
        return productoDao.getProductoFilter(column, operator, value, order);
    }
}
