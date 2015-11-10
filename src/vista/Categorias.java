/*
 * Copyright (C) 2015 cepardov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package vista;

import beans.CategoriasBeans;
import conexion.DataBaseInstance;
import java.awt.Cursor;
import java.sql.Connection;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author cepardov
 */
public class Categorias extends javax.swing.JInternalFrame {
    CategoriasBeans categoriasbeans=new CategoriasBeans();
    Object[][] dtPrev;
    int fila;
    /**
     * Creates new form Categorias
     */
    public Categorias() {
        initComponents();
        this.inicializa();
    }
    
    protected Connection getConnection() {
        return DataBaseInstance.getInstanceConnection();
    }
    
    protected void closeConnection() {
        DataBaseInstance.closeConnection();
    }
    
    protected int closeConnectionInt() {
        DataBaseInstance.closeConnection();
        return 0;
    }
    
    private void inicializa(){
        this.lblErrorBusqueda.setText("");
        this.jsnDescuento.setEnabled(false);
        this.cargaTablaCategorias();
        this.btnGuardar.setEnabled(true);
        this.btnModificar.setEnabled(false);
        this.btnEliminar.setEnabled(false);
    }
    
    private void busy(int estado){
        switch (estado) {
            case 1:
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                break;
            case 2:
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                break;
            default:
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); 
                break;
        }
    }
    
    public final void cargaTablaCategorias(){
        String[] columNames = {"ID","Nombre","Descripción","% Desc."};  
        dtPrev = categoriasbeans.getCategorias();
        DefaultTableModel datos = new DefaultTableModel(dtPrev,columNames);                        
        tabla.setModel(datos);
        //Autoredimensionar Columnas
        try{
            String col0=String.valueOf(tabla.getValueAt(0, 0));
            int width0=35;//col0.length()*160;
            System.out.println("Debug: Redimensión col0="+col0);
            tabla.getColumnModel().getColumn(0).setMaxWidth(width0);
            tabla.getColumnModel().getColumn(0).setMinWidth(width0);
            tabla.getColumnModel().getColumn(0).setPreferredWidth(width0);
        }
        catch (Exception se){
            System.out.println("Debug: Error redimensionar col0: "+se.getMessage());
        }
        //Esconder Comunas
//        tabla.getColumnModel().getColumn(5).setMaxWidth(0);
//        tabla.getColumnModel().getColumn(5).setMinWidth(0);
//        tabla.getColumnModel().getColumn(5).setPreferredWidth(0);
////        
//        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
//        tabla.getColumnModel().getColumn(0).setMinWidth(0);
//        tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
    }
    
    private boolean verificaDatos(){
        String idCategoria = this.lblIdCategoria.getText();
        String nombre = this.txtNombre.getText().toLowerCase();
        String decripcion = this.txtDescripcion.getText();
        String porcentajeDescuento = this.jsnDescuento.getValue().toString();
        
        if(nombre.isEmpty()){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe llenar el campo:\n- Nombre", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else {
            if(idCategoria.equals("Por Definir")){
                idCategoria = null;
            }
            categoriasbeans.setIdCategoria(idCategoria);
            categoriasbeans.setNombre(nombre);
            categoriasbeans.setDecripcion(decripcion);
            categoriasbeans.setPorcentajeDescuento(porcentajeDescuento);
            return true;
        }
        return false;
    }
    
    private boolean existencia(){
        String nombre = this.txtNombre.getText().toLowerCase();
        
        categoriasbeans.setNombre(nombre);
        
        if(categoriasbeans.findByName()!=false){
            JOptionPane.showMessageDialog(null,"La categoŕia \""+nombre+"\" ya existe", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
//            this.clean();
            this.btnGuardar.setEnabled(true);
            this.btnModificar.setEnabled(false);
            this.btnEliminar.setEnabled(false);
        } else {
            return true;
        }        
        return false;
    }
    
    private void buscar(int seleccion){
        String idCategoria = this.lblIdCategoria.getText();
        String nombre = this.txtNombre.getText().toLowerCase();
        
        switch(seleccion){
            case 0:
                categoriasbeans.setIdCategoria(idCategoria);
                if(categoriasbeans.findByID()!=false){
                    this.cargaDatos();
                } else {
                    this.limpiar();
                    this.lblErrorBusqueda.setText("La Categoría ID \""+idCategoria+"\" no existe");
                    this.lblErrorBusqueda.setText("No hemos encontrado lo que búscas!");
                }
                break;
            case 1:
                categoriasbeans.setNombre(nombre);
                if(categoriasbeans.findByName()!=false){
                   this.cargaDatos();
                    this.btnGuardar.setEnabled(false);
                    this.btnModificar.setEnabled(true);
                    this.btnEliminar.setEnabled(true);
                } else {
                    this.limpiar();
                    this.lblErrorBusqueda.setText("La Categoría nombre \""+nombre+"\" no existe");
                    this.lblErrorBusqueda.setText("No hemos encontrado lo que búscas!");
                }
                break;
            default:
//                this.limpiar();
        }
    }
    
    private void cargaDatos(){
        int descValue = Integer.parseInt(categoriasbeans.getPorcentajeDescuento());
        this.lblIdCategoria.setText(categoriasbeans.getIdCategoria());
        this.txtNombre.setText(categoriasbeans.getNombre());
        this.txtDescripcion.setText(categoriasbeans.getDecripcion());
        this.btnGuardar.setEnabled(false);
        this.btnModificar.setEnabled(true);
        this.btnEliminar.setEnabled(true);
        if(descValue>0){
            this.chkDescuentos.setSelected(true);
            this.jsnDescuento.setValue(new Integer(categoriasbeans.getPorcentajeDescuento()));
        } else {
            this.chkDescuentos.setSelected(false);
            this.jsnDescuento.setEnabled(false);
            this.jsnDescuento.setValue(new Integer(0));
        }
    }
    
    private void limpiar(){
        this.lblIdCategoria.setText("Por Definir");
        this.txtNombre.setText("");
        this.txtDescripcion.setText("");
        this.chkDescuentos.setSelected(false);
        this.jsnDescuento.setEnabled(false);
        this.jsnDescuento.setValue(new Integer(0));
        this.btnGuardar.setEnabled(true);
        this.btnModificar.setEnabled(false);
        this.btnEliminar.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnBuscar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblIdCategoria = new javax.swing.JLabel();
        lblErrorBusqueda = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        chkDescuentos = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jsnDescuento = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCrearNuevo = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Categoría"));

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel1.setText("ID Categoría:");

        lblIdCategoria.setText("Por Definir");

        lblErrorBusqueda.setForeground(new java.awt.Color(208, 22, 22));
        lblErrorBusqueda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblErrorBusqueda.setText("Error");

        jLabel3.setText("Nombre");

        jLabel4.setText("Descripción");

        chkDescuentos.setText("Activar Descuentos");
        chkDescuentos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkDescuentosItemStateChanged(evt);
            }
        });

        jLabel5.setText("Porcentaje de Descuento");

        jsnDescuento.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 5));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblIdCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblErrorBusqueda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDescripcion))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(chkDescuentos)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jsnDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscar)
                    .addComponent(jLabel1)
                    .addComponent(lblIdCategoria)
                    .addComponent(lblErrorBusqueda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkDescuentos)
                    .addComponent(jLabel5)
                    .addComponent(jsnDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista Categorías"));

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnCrearNuevo.setText("Crear Nuevo/Limpiar");
        btnCrearNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearNuevoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCrearNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCerrar)
                    .addComponent(btnGuardar)
                    .addComponent(btnModificar)
                    .addComponent(btnEliminar)
                    .addComponent(btnCrearNuevo))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void chkDescuentosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkDescuentosItemStateChanged
        // TODO add your handling code here:
        if(this.chkDescuentos.isSelected()){
            this.jsnDescuento.setEnabled(true);
//            this.jsnDescuento.setValue(0);
        } else {
            this.jsnDescuento.setEnabled(false);
            this.jsnDescuento.setValue(new Integer(0));
        }
    }//GEN-LAST:event_chkDescuentosItemStateChanged

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        this.buscar(1);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        this.busy(1);
        if(this.verificaDatos()==true&&this.existencia()){
            if(categoriasbeans.save()==false){
                JOptionPane.showMessageDialog(null,"Error: "+categoriasbeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
            } else {
                this.buscar(3);
                this.cargaTablaCategorias();
                this.btnGuardar.setEnabled(false);
                this.btnModificar.setEnabled(true);
                this.btnEliminar.setEnabled(true);
            }
        }
        this.busy(0);
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        this.busy(1);
        if(this.verificaDatos()==true){
            if(categoriasbeans.update()==false){
                JOptionPane.showMessageDialog(null,"Error: "+categoriasbeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
            } else {
                this.buscar(0);
                this.cargaTablaCategorias();
                this.btnGuardar.setEnabled(false);
                this.btnModificar.setEnabled(true);
                this.btnEliminar.setEnabled(true);
            }
        }
        this.busy(0);
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        this.busy(1);
        if(this.verificaDatos()==true){
            if(categoriasbeans.delete()==false){
                JOptionPane.showMessageDialog(null,"Error: "+categoriasbeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
            } else {
                this.cargaTablaCategorias();
                this.limpiar();
                this.btnGuardar.setEnabled(true);
                this.btnModificar.setEnabled(false);
                this.btnEliminar.setEnabled(false);
            }
        }
        this.busy(0);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        // TODO add your handling code here:
        fila = tabla.rowAtPoint(evt.getPoint());
        if (fila > -1){
            this.lblIdCategoria.setText(String.valueOf(tabla.getValueAt(fila, 0)));
            this.buscar(0);
        }
    }//GEN-LAST:event_tablaMouseClicked

    private void btnCrearNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearNuevoActionPerformed
        // TODO add your handling code here:
        this.limpiar();
    }//GEN-LAST:event_btnCrearNuevoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnCrearNuevo;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JCheckBox chkDescuentos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jsnDescuento;
    public javax.swing.JLabel lblErrorBusqueda;
    private javax.swing.JLabel lblIdCategoria;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
