/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.ProveedorDao;
import entidad.Proveedor;

/**
 * Proveedor
 * @author cepardov
 */
public class ProveedorBeans extends Proveedor{
    ProveedorDao proveedorDao=new ProveedorDao();
    
    public boolean save(){
        return proveedorDao.save(this);
    }
    public boolean delete(){
        return proveedorDao.delete(this);
    }
    public boolean update(){
        return proveedorDao.update(this);
    }
    public boolean findByID(){
        return proveedorDao.findById(this);
    }
    public boolean findByName(){
        return proveedorDao.findByName(this);
    }
    public boolean findByRut(){
        return proveedorDao.findByRut(this);
    }
    public Object[][] getProveedor(){
        return proveedorDao.getProveedor();
    }
}
