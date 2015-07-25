/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.UnidadmedidaDao;
import entidad.Unidadmedida;

/**
 * Unidadmedida
 * @author cepardov
 */
public class UnidadmedidaBeans extends Unidadmedida{
    UnidadmedidaDao unidadmedidaDao=new UnidadmedidaDao();
    
    public boolean save(){
        return unidadmedidaDao.save(this);
    }
    public boolean delete(){
        return unidadmedidaDao.delete(this);
    }
    public boolean update(){
        return unidadmedidaDao.update(this);
    }
    public boolean findByID(){
        return unidadmedidaDao.findById(this);
    }
    public boolean findByValue(){
        return unidadmedidaDao.findByValue(this);
    }
    public Object[][] getUnidadmedida(){
        return unidadmedidaDao.getUnidadmedida();
    }
}
