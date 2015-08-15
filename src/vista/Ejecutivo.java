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

import beans.EjecutivoBeans;
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
public class Ejecutivo extends javax.swing.JInternalFrame {
    EjecutivoBeans ejecutivobeans=new EjecutivoBeans();
    ProveedorBeans proveedorbeans=new ProveedorBeans();
    Object[][] dtPrev;
    int fila;
    /**
     * Creates new form Ejecutivo
     */
    public Ejecutivo() {
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
        this.cargaTablaEjecutivo();
        this.getComboProveedor();
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
    
    public final void cargaTablaEjecutivo(){
        String[] columNames = {"ID","ID","Nombre","Apellido","Descripción","Correo - e","Telefono","Movil","IDProveedor"};  
        dtPrev = ejecutivobeans.getEjecutivo();
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
            
            tabla.getColumnModel().getColumn(1).setMaxWidth(width0);
            tabla.getColumnModel().getColumn(1).setMinWidth(width0);
            tabla.getColumnModel().getColumn(1).setPreferredWidth(width0);
            
            tabla.getColumnModel().getColumn(8).setMaxWidth(width0);
            tabla.getColumnModel().getColumn(8).setMinWidth(width0);
            tabla.getColumnModel().getColumn(8).setPreferredWidth(width0);
            
            tabla.getColumnModel().getColumn(5).setMaxWidth(160);
            tabla.getColumnModel().getColumn(5).setMinWidth(160);
            tabla.getColumnModel().getColumn(5).setPreferredWidth(160);
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
        String idEjecutivo = this.lblIdEjecutivo.getText();
        String identificador = this.txtIdentificador.getText();
        String nombre = this.txtNombre.getText();
        String apellido = this.txtApellido.getText();
        String descripcion = this.txtDescripcion.getText();
        String correoNombre = this.txtCorreoNombre.getText();
        String correoDominio = this.txtCorreoDominio.getText();
        String telefono = this.txtTelefono.getText();
        String movil = this.txtMovil.getText();
        String idProveedor = this.cbProveedor.getSelectedItem().toString();
        
        if(identificador.isEmpty()){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe llenar el campo:\n- Identificador", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else if (nombre.isEmpty()){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe llenar el campo:\n- Nombre", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else if (apellido.isEmpty()){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe llenar el campo:\n- Apellido", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else if (correoNombre.isEmpty()){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe llenar el campo:\n- Usuario Correo", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else if (correoDominio.isEmpty()){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe llenar el campo:\n- Dominio Correo", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else if (idProveedor.equals("Seleccione")){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe seleccionar campo:\n- Proveedor", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else if (telefono.isEmpty()){
            JOptionPane.showMessageDialog(null,"Considere obtener un numero de contacto, de no ser así rellene con \"0\"", "Verificación de Formularios", JOptionPane.WARNING_MESSAGE);
        } else if (movil.isEmpty()){
            JOptionPane.showMessageDialog(null,"Considere obtener un numero de contacto, de no ser así rellene con \"0\"", "Verificación de Formularios", JOptionPane.WARNING_MESSAGE);
        } else {
            String correo = correoNombre+"@"+correoDominio;
            if(idEjecutivo.equals("Por Definir")){
                idEjecutivo = null;
            }
            ejecutivobeans.setIdEjecutivo(idEjecutivo);
            ejecutivobeans.setIdentificador(identificador);
            ejecutivobeans.setNombre(nombre);
            ejecutivobeans.setApellido(apellido);
            ejecutivobeans.setDescripcion(descripcion);
            ejecutivobeans.setCorreo(correo);
            ejecutivobeans.setTelefono(telefono);
            ejecutivobeans.setMovil(movil);
            ejecutivobeans.setIdProveedor(this.getIdProveedor(idProveedor));
            return true;
        }
        return false;
        
    }
    
    private boolean existencia(){
        String identificador = this.txtIdentificador.getText().replace(" ", "").toLowerCase();
        
        ejecutivobeans.setIdentificador(identificador);
        
        if(ejecutivobeans.findByIdentificador()!=false){
            JOptionPane.showMessageDialog(null,"El Ejecutivo ID \""+identificador+"\" ya existe", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
//            this.clean();
            this.btnGuardar.setEnabled(true);
            this.btnModificar.setEnabled(false);
            this.btnEliminar.setEnabled(false);
        } else {
            return true;
        }        
        return false;
    }
    
    private String desglosaNombreCorreo(String correo){
        String nombreCorreo = correo.substring(0,correo.indexOf("@"));
        return nombreCorreo;
    }
    
    private String desglosaDominioCorreo(String correo){
        String dominio = correo.substring(correo.indexOf("@")+1,correo.length());
        return dominio;
    }
    
    private boolean buscar(int seleccion){
        String idEjecutivo = this.lblIdEjecutivo.getText();
        String identificador = this.txtIdentificador.getText();
        String nombre = this.txtNombre.getText();
        
        switch(seleccion){
            case 0:
                ejecutivobeans.setNombre(nombre);
                if(ejecutivobeans.findByName()!=false){
                   this.cargaDatos();
                } else {
                    this.limpiar();
                    this.lblErrorBusqueda.setText("El ejecutivo \""+nombre+"\" no existe");
                    this.lblErrorBusqueda.setText("No hemos encontrado lo que búscas!");
                    return true;
                }
                break;
            case 1:
                ejecutivobeans.setIdEjecutivo(idEjecutivo);
                if(ejecutivobeans.findByID()!=false){
                    this.cargaDatos();
                } else {
                    this.limpiar();
                    this.lblErrorBusqueda.setText("El ejecutivo \""+nombre+"\" no existe");
                    this.lblErrorBusqueda.setText("No hemos encontrado lo que búscas!");
                    return true;
                }
                break;
            case 3:
                ejecutivobeans.setIdentificador(identificador);
                if(ejecutivobeans.findByIdentificador()!=false){
                    this.cargaDatos();
                } else {
                    this.limpiar();
                    this.lblErrorBusqueda.setText("El ejecutivo \""+nombre+"\" no existe");
                    this.lblErrorBusqueda.setText("No hemos encontrado lo que búscas!");
                    return true;
                }
                break;
            default:
                //ByName
        }
        return false;
    }
    
    private void cargaDatos(){
        this.lblIdEjecutivo.setText(ejecutivobeans.getIdEjecutivo());
        this.txtIdentificador.setText(ejecutivobeans.getIdentificador());
        this.txtNombre.setText(ejecutivobeans.getNombre());
        this.txtApellido.setText(ejecutivobeans.getApellido());
        this.txtCorreoNombre.setText(this.desglosaNombreCorreo(ejecutivobeans.getCorreo()));
        this.txtCorreoDominio.setText(this.desglosaDominioCorreo(ejecutivobeans.getCorreo()));
        this.txtTelefono.setText(ejecutivobeans.getTelefono());
        this.txtMovil.setText(ejecutivobeans.getMovil());
        this.cbProveedor.setSelectedItem(this.getNombreProveedor(ejecutivobeans.getIdProveedor()));
        this.txtDescripcion.setText(ejecutivobeans.getDescripcion());
        this.btnGuardar.setEnabled(false);
        this.btnModificar.setEnabled(true);
        this.btnEliminar.setEnabled(true);
    }
    
    public void limpiar(){
        this.inicializa();
        this.lblErrorBusqueda.setText("");
        this.lblIdEjecutivo.setText("Por Definir");
        this.txtIdentificador.setText("");
        this.txtNombre.setText("");
        this.txtApellido.setText("");
        this.txtCorreoNombre.setText("");
        this.txtCorreoDominio.setText("");
        this.txtDescripcion.setText("");
        this.txtMovil.setText("");
        this.txtTelefono.setText("");
        this.cbProveedor.setSelectedItem("Seleccione");
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
        lblIdEjecutivo = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        txtIdentificador = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lblErrorBusqueda = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtCorreoNombre = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCorreoDominio = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtMovil = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cbProveedor = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCrearNuevo = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Ejecutivo"));

        jLabel1.setText("ID Ejecutivo:");

        lblIdEjecutivo.setText("Por Definir");

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel3.setText("Identificador");

        lblErrorBusqueda.setForeground(new java.awt.Color(208, 22, 22));
        lblErrorBusqueda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblErrorBusqueda.setText("Error");

        jLabel4.setText("Nombre");

        jLabel5.setText("Apellido");

        jLabel6.setText("Descripción");

        jLabel7.setText("Correo");

        jLabel8.setText("@");

        jLabel9.setText("Teléfono");

        jLabel10.setText("Movil");

        jLabel11.setText("Proveedor");

        cbProveedor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDescripcion)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel6)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtMovil, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(cbProveedor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel9))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel10))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel11)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(txtCorreoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel8)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtCorreoDominio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel7))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblIdEjecutivo, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblErrorBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIdentificador, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscar)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(lblIdEjecutivo)
                            .addComponent(btnBuscar)
                            .addComponent(txtIdentificador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblErrorBusqueda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCorreoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtCorreoDominio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMovil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista Ejecutivo"));

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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCerrar)
                    .addComponent(btnGuardar)
                    .addComponent(btnModificar)
                    .addComponent(btnEliminar)
                    .addComponent(btnCrearNuevo)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap())
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
        if(this.verificaDatos()==true&&this.existencia()){
            if(ejecutivobeans.save()==false){
                JOptionPane.showMessageDialog(null,"Error: "+ejecutivobeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
            } else {
                this.buscar(3);
                this.cargaTablaEjecutivo();
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
            if(ejecutivobeans.update()==false){
                JOptionPane.showMessageDialog(null,"Error: "+ejecutivobeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
            } else {
                this.cargaTablaEjecutivo();
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
            if(ejecutivobeans.delete()==false){
                JOptionPane.showMessageDialog(null,"Error: "+ejecutivobeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
            } else {
                this.cargaTablaEjecutivo();
                this.limpiar();
                this.btnGuardar.setEnabled(true);
                this.btnModificar.setEnabled(false);
                this.btnEliminar.setEnabled(false);
            }
        }
        this.busy(0);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnCrearNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearNuevoActionPerformed
        // TODO add your handling code here:
        this.limpiar();
    }//GEN-LAST:event_btnCrearNuevoActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        this.buscar(3);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        // TODO add your handling code here:
        fila = tabla.rowAtPoint(evt.getPoint());
        if (fila > -1){
            this.lblIdEjecutivo.setText(String.valueOf(tabla.getValueAt(fila, 0)));
            this.buscar(1);
        }
    }//GEN-LAST:event_tablaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnCrearNuevo;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox cbProveedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lblErrorBusqueda;
    private javax.swing.JLabel lblIdEjecutivo;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCorreoDominio;
    private javax.swing.JTextField txtCorreoNombre;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtIdentificador;
    private javax.swing.JTextField txtMovil;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
