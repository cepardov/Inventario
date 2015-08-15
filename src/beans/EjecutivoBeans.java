/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.EjecutivoDao;
import entidad.Ejecutivo;

/**
 * Ejecutivo
 * @author cepardov
 */
public class EjecutivoBeans extends Ejecutivo{
    EjecutivoDao ejecutivoDao=new EjecutivoDao();
    
    public boolean save(){
        return ejecutivoDao.save(this);
    }
    public boolean delete(){
        return ejecutivoDao.delete(this);
    }
    public boolean update(){
        return ejecutivoDao.update(this);
    }
    public boolean findByID(){
        return ejecutivoDao.findById(this);
    }
    public boolean findByIdentificador(){
        return ejecutivoDao.findByIdentificador(this);
    }
    public boolean findByName(){
        return ejecutivoDao.findByName(this);
    }
    public Object[][] getEjecutivo(){
        return ejecutivoDao.getEjecutivo();
    }
}
