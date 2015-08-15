/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.FabricanteDao;
import entidad.Fabricante;

/**
 * Fabricante
 * @author cepardov
 */
public class FabricanteBeans extends Fabricante{
    FabricanteDao fabricanteDao=new FabricanteDao();
    
    public boolean save(){
        return fabricanteDao.save(this);
    }
    public boolean delete(){
        return fabricanteDao.delete(this);
    }
    public boolean update(){
        return fabricanteDao.update(this);
    }
    public boolean findByID(){
        return fabricanteDao.findById(this);
    }
    public boolean findByIdentificador(){
        return fabricanteDao.findByIdentificador(this);
    }
    public boolean findByName(){
        return fabricanteDao.findByName(this);
    }
    public Object[][] getFabricante(){
        return fabricanteDao.getFabricante();
    }
}
