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

import beans.FabricanteBeans;
import beans.ProveedorBeans;
import conexion.DataBaseInstance;
import java.awt.Cursor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author cepardov
 */
public class Fabricante extends javax.swing.JInternalFrame {
    FabricanteBeans fabricantebeans=new FabricanteBeans();
    ProveedorBeans proveedorbeans=new ProveedorBeans();
    Object[][] dtPrev;
    int fila;
    /**
     * Creates new form Fabricante
     */
    public Fabricante() {
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
    
    public final void inicializa(){
//        this.cargaTablaUsuario();
        this.lblErrorBusqueda.setText("");
        this.cargaTablaFabricante();
        this.getComboProveedor();
        this.btnGuardar.setEnabled(true);
        this.btnModificar.setEnabled(false);
        this.btnEliminar.setEnabled(false);
    }
    
    private String getNombreProveedor(String idProveedor){
        proveedorbeans.setIdProveedor(idProveedor);
        
        if(!proveedorbeans.findByID()){
            JOptionPane.showMessageDialog(null,"Error al obtener nombre Proveedor\n"+proveedorbeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
        } else {
            return proveedorbeans.getNombre();
        }
        return null;
    }
    
    private String getIdProveedor(String nombreProveedor){
        proveedorbeans.setNombre(nombreProveedor);
        System.out.println("Nombre Proveedor="+nombreProveedor);
        if(!proveedorbeans.findByName()){
            System.out.println("Result falso");
            JOptionPane.showMessageDialog(null,"Error al obtener ID Proveedor\n"+proveedorbeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
        }else{
            System.out.println("Result verdadero");
            return proveedorbeans.getIdProveedor();
        }
        
        return null;
        
    }
    
    public final void cargaTablaFabricante(){
        String[] columNames = {"ID","Identificador","Nombre","Descripción","Proveedor"};  
        dtPrev = fabricantebeans.getFabricante();
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
            
//            String col1=String.valueOf(tabla.getValueAt(0, 0));
//            int width=col0.length()*160;
//            System.out.println("Debug: Redimensión col0="+col0);
//            tabla.getColumnModel().getColumn(0).setMaxWidth(width);
//            tabla.getColumnModel().getColumn(0).setMinWidth(width);
//            tabla.getColumnModel().getColumn(0).setPreferredWidth(width);
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
    
    private void getComboProveedor(){
        try{
            DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
            PreparedStatement pstm = getConnection().prepareStatement("SELECT * FROM proveedor ORDER BY nombre");
            try (ResultSet res = pstm.executeQuery()) {
                modeloCombo.addElement("Seleccione");
                while (res.next()) {
                    modeloCombo.addElement(res.getObject("nombre"));
                }
            }
            this.closeConnection();
            this.cbProveedor.setModel(modeloCombo);
        } catch (SQLException se) {
            System.out.println("Error al cargar combo="+se);
        }
    }
    
    private boolean verificaDatos(){
        String idFabricante = this.lblIdFabricante.getText();
        String identificador = this.txtIdentificador.getText();
        String nombre = this.txtNombre.getText().toLowerCase();
        String descripcion = this.txtDescripcion.getText();
        String nombreProveedor = this.cbProveedor.getSelectedItem().toString();
        
        if(identificador.isEmpty()){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe llenar el campo:\n- Identificador", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else if (nombre.isEmpty()){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe llenar el campo:\n- nombre", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else if (nombreProveedor.equals("Seleccione")){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe seleccionar:\n- Proveedor", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else {
            if(idFabricante.equals("No definido")){
                idFabricante = null;
            }
            fabricantebeans.setIdFabricante(idFabricante);
            fabricantebeans.setIdentificador(identificador);
            fabricantebeans.setNombre(nombre);
            fabricantebeans.setDescripcion(descripcion);
            fabricantebeans.setIdProveedor(this.getIdProveedor(nombreProveedor));
            return true;
        }
        
        return false;
        
    }
    
    private boolean existenciaIdentificador(){
        String identificador = this.txtIdentificador.getText();
        
        fabricantebeans.setIdentificador(identificador);
        
        if(fabricantebeans.findByIdentificador()!=false){
            JOptionPane.showMessageDialog(null,"El fabricante con identificador \""+identificador+"\" ya existe", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
//            this.clean();
            this.btnGuardar.setEnabled(true);
            this.btnModificar.setEnabled(false);
            this.btnEliminar.setEnabled(false);
        } else {
            return true;
        }        
        return false;
    }
    
    //Existencia
    private boolean existenciaNombre(){
        String nombre = this.txtNombre.getText().toLowerCase();
        
        fabricantebeans.setNombre(nombre);
        
        if(fabricantebeans.findByName()!=false){
            JOptionPane.showMessageDialog(null,"El fabricante \""+nombre+"\" ya existe", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
//            this.clean();
            this.btnGuardar.setEnabled(true);
            this.btnModificar.setEnabled(false);
            this.btnEliminar.setEnabled(false);
        } else {
            return true;
        }        
        return false;
    }
    
    //buscar
    
    private boolean buscar(int seleccion){
        String idFabricante = this.lblIdFabricante.getText();
        String identificador = this.txtIdentificador.getText();
        String nombre = this.txtNombre.getText();
        
        switch(seleccion){
            case 0:
                fabricantebeans.setNombre(nombre);
                if(fabricantebeans.findByName()!=false){
                    this.lblIdFabricante.setText(fabricantebeans.getIdFabricante());
                    this.txtIdentificador.setText(fabricantebeans.getIdentificador());
                    this.txtNombre.setText(fabricantebeans.getNombre());
                    this.txtDescripcion.setText(fabricantebeans.getDescripcion());
                    this.cbProveedor.setSelectedItem(this.getNombreProveedor(fabricantebeans.getIdProveedor()));
                    this.btnGuardar.setEnabled(false);
                    this.btnModificar.setEnabled(true);
                    this.btnEliminar.setEnabled(true);
                } else {
                    this.limpiar();
                    this.lblErrorBusqueda.setText("El fabricante \""+nombre+"\" no existe");
                    this.lblErrorBusqueda.setText("No hemos encontrado lo que búscas!");
                    return true;
                }
                break;
            case 1:
                fabricantebeans.setIdFabricante(idFabricante);
                if(fabricantebeans.findByID()!=false){
                    this.lblIdFabricante.setText(fabricantebeans.getIdFabricante());
                    this.txtIdentificador.setText(fabricantebeans.getIdentificador());
                    this.txtNombre.setText(fabricantebeans.getNombre());
                    this.txtDescripcion.setText(fabricantebeans.getDescripcion());
                    this.cbProveedor.setSelectedItem(this.getNombreProveedor(fabricantebeans.getIdProveedor()));
                    this.btnGuardar.setEnabled(false);
                    this.btnModificar.setEnabled(true);
                    this.btnEliminar.setEnabled(true);
                } else {
                    this.limpiar();
                    this.lblErrorBusqueda.setText("El fabricante \""+nombre+"\" no existe");
                    this.lblErrorBusqueda.setText("No hemos encontrado lo que búscas!");
                    return true;
                }
                break;
            case 3:
                fabricantebeans.setIdentificador(identificador);
                if(fabricantebeans.findByIdentificador()!=false){
                    this.lblIdFabricante.setText(fabricantebeans.getIdFabricante());
                    this.txtIdentificador.setText(fabricantebeans.getIdentificador());
                    this.txtNombre.setText(fabricantebeans.getNombre());
                    this.txtDescripcion.setText(fabricantebeans.getDescripcion());
                    this.cbProveedor.setSelectedItem(this.getNombreProveedor(fabricantebeans.getIdProveedor()));
                    this.btnGuardar.setEnabled(false);
                    this.btnModificar.setEnabled(true);
                    this.btnEliminar.setEnabled(true);
                } else {
                    this.limpiar();
                    this.lblErrorBusqueda.setText("El fabricante \""+nombre+"\" no existe");
                    this.lblErrorBusqueda.setText("No hemos encontrado lo que búscas!");
                    return true;
                }
                break;
            default:
                //ByName
        }
        return false;
    }
    
    public void limpiar(){
        this.lblErrorBusqueda.setText("");
        this.lblIdFabricante.setText("No definido");
        this.txtIdentificador.setText("");
        this.txtNombre.setText("");
        this.txtDescripcion.setText("");
        this.cbProveedor.setSelectedItem("Seleccione");
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
        jLabel1 = new javax.swing.JLabel();
        lblIdFabricante = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtIdentificador = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbProveedor = new javax.swing.JComboBox();
        lblErrorBusqueda = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Fabricante"));

        jLabel1.setText("ID Fabricante:");

        lblIdFabricante.setText("No definido");

        jLabel3.setText("Identificador:");

        jLabel4.setText("Nombre Fabricante");

        jLabel5.setText("Descripción");

        jLabel6.setText("Asociación con un proveedor");

        cbProveedor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione" }));

        lblErrorBusqueda.setForeground(new java.awt.Color(208, 22, 22));
        lblErrorBusqueda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblErrorBusqueda.setText("Error");

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtNombre)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblIdFabricante)))
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDescripcion)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblErrorBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIdentificador, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblIdFabricante)
                    .addComponent(jLabel3)
                    .addComponent(txtIdentificador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrorBusqueda)
                    .addComponent(btnBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista de Fabricantes"));

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
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

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

        btnNuevo.setText("Nuevo/Limpiar");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnGuardar)
                .addComponent(btnModificar)
                .addComponent(btnEliminar)
                .addComponent(btnNuevo)
                .addComponent(btnCerrar))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        // TODO add your handling code here:
        
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        this.busy(1);
        if(this.verificaDatos()==true&&this.existenciaNombre()&&this.existenciaIdentificador()){
            if(fabricantebeans.save()==false){
                JOptionPane.showMessageDialog(null,"Error: "+fabricantebeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
            } else {
                this.buscar(3);
                this.cargaTablaFabricante();
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
            if(fabricantebeans.update()==false){
                JOptionPane.showMessageDialog(null,"Error: "+fabricantebeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
            } else {
                this.cargaTablaFabricante();
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
            if(fabricantebeans.delete()==false){
                JOptionPane.showMessageDialog(null,"Error: "+fabricantebeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
            } else {
                this.cargaTablaFabricante();
                this.limpiar();
                this.btnGuardar.setEnabled(true);
                this.btnModificar.setEnabled(false);
                this.btnEliminar.setEnabled(false);
            }
        }
        this.busy(0);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        this.limpiar();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        // TODO add your handling code here:
        fila = tabla.rowAtPoint(evt.getPoint());
        if (fila > -1){
            this.lblIdFabricante.setText(String.valueOf(tabla.getValueAt(fila, 0)));
            this.buscar(1);
        }
    }//GEN-LAST:event_tablaMouseClicked

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        this.buscar(3);
    }//GEN-LAST:event_btnBuscarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox cbProveedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lblErrorBusqueda;
    private javax.swing.JLabel lblIdFabricante;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtIdentificador;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
