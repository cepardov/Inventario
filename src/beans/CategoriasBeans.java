/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.CategoriasDao;
import entidad.Categorias;

/**
 * Categorias
 * @author cepardov
 */
public class CategoriasBeans extends Categorias{
    CategoriasDao categoriasDao=new CategoriasDao();
    
    public boolean save(){
        return categoriasDao.save(this);
    }
    public boolean delete(){
        return categoriasDao.delete(this);
    }
    public boolean update(){
        return categoriasDao.update(this);
    }
    public boolean findByID(){
        return categoriasDao.findById(this);
    }
    public boolean findByName(){
        return categoriasDao.findByName(this);
    }
    public Object[][] getCategorias(){
        return categoriasDao.getCategorias();
    }
}
