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
import beans.FabricanteBeans;
import beans.ProductoBeans;
import beans.ProveedorBeans;
import conexion.DataBaseInstance;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import utilidades.DateTime;

/**
 *
 * @author cepardov
 */
public class Producto extends javax.swing.JInternalFrame {
    ProductoBeans productobeans=new ProductoBeans();
    ProveedorBeans proveedorbeans=new ProveedorBeans();
    CategoriasBeans categoriasbeans=new CategoriasBeans();
    FabricanteBeans fabricantebeans=new FabricanteBeans();
    DateTime datetime=new DateTime();
    Object[][] dtPrev;
    int fila;
    /**
     * Creates new form Producto
     */
    public Producto() {
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
//        this.txtValorProveedor.setEnabled(false);
        this.txtValorNeto.setValue(0);
        this.txtValorProveedor.setValue(0);
        this.txtNuevoStock.setEnabled(false);
        //Grupo botones stock
        this.rbtnGroupStock.add(this.rbtnAgregar);
        this.rbtnGroupStock.add(this.rbtnMermar);
        //grupo botones buscar
        this.rbtnGroupBuscar.add(this.rbtnEan);
        this.rbtnGroupBuscar.add(this.rbtnReferencia);
        this.rbtnGroupBuscar.add(this.rbtnNombre);
        //grupo botones filtrar
        this.rbtnGroupFiltrar.add(this.rbtnFiltrarTodos);
        this.rbtnGroupFiltrar.add(this.rbtnFiltrarDescuento);
        this.rbtnGroupFiltrar.add(this.rbtnFiltrarStockBajo);
        
        this.lblFechaIngreso.setText(datetime.getFecha());
        this.txtValorNeto.setEditable(false);
        this.chkDescuento.setSelected(false);
        this.jsnDescuento.setEnabled(false);
        this.txtPrecioVenta.setEditable(false);
        
        this.jsnDescuento.setEnabled(false);
        this.cargaTablaProducto();
        this.btnGuardar.setEnabled(true);
        this.btnModificar.setEnabled(false);
        this.btnEliminar.setEnabled(false);
        
        this.getComboCategorias();
        this.getComboProveedor();
        this.getComboFabricante();
        
        this.txtAyuda.setText("Sin elementos de ayuda...");
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
    private static final int STATUS_COL = 12;
    public final void cargaTablaProducto(){
        String[] columNames = {"ID","Referencia","Cod. EAN","Nombre","Descripción","Fecha Ing.","$ Proveedor","$ Neto","$ Venta","% Desc.","V. Desc","Stock","U. Medida","Categoria","Proveedor","Fabricante"};
        dtPrev = productobeans.getProducto();
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
            
            tabla.getColumnModel().getColumn(2).setMaxWidth(120);
            tabla.getColumnModel().getColumn(2).setMinWidth(120);
            tabla.getColumnModel().getColumn(2).setPreferredWidth(120);
            
            tabla.getColumnModel().getColumn(3).setMaxWidth(200);
            tabla.getColumnModel().getColumn(3).setMinWidth(200);
            tabla.getColumnModel().getColumn(3).setPreferredWidth(200);
            
            tabla.getColumnModel().getColumn(12).setMaxWidth(100);
            tabla.getColumnModel().getColumn(12).setMinWidth(100);
            tabla.getColumnModel().getColumn(12).setPreferredWidth(100);
        }
        catch (Exception se){
            System.out.println("Debug: Error redimensionar col0: "+se.getMessage());
        }
        //Esconder Comunas
        tabla.getColumnModel().getColumn(1).setMaxWidth(0);
        tabla.getColumnModel().getColumn(1).setMinWidth(0);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(0);
        
        tabla.getColumnModel().getColumn(4).setMaxWidth(0);
        tabla.getColumnModel().getColumn(4).setMinWidth(0);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(0);
        
        tabla.getColumnModel().getColumn(5).setMaxWidth(0);
        tabla.getColumnModel().getColumn(5).setMinWidth(0);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(0);
        
        tabla.getColumnModel().getColumn(7).setMaxWidth(0);
        tabla.getColumnModel().getColumn(7).setMinWidth(0);
        tabla.getColumnModel().getColumn(7).setPreferredWidth(0);
        
        tabla.getColumnModel().getColumn(10).setMaxWidth(0);
        tabla.getColumnModel().getColumn(10).setMinWidth(0);
        tabla.getColumnModel().getColumn(10).setPreferredWidth(0);
////        
//        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
//        tabla.getColumnModel().getColumn(0).setMinWidth(0);
//        tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
    }
    /**
     * Crea un filtro para cargar la tabla segun valor seleccionado
     * @param column Columna a seleccionar
     * @param operator Operador de validación
     * @param value  Valor buscado en la columna
     * @param order Ordenar segun valor seleccionado
     */
    public final void cargaTablaProductoFilter(String column, String operator, String value, String order){
        String[] columNames = {"ID","Referencia","Cod. EAN","Nombre","Descripción","Fecha Ing.","$ Proveedor","$ Neto","$ Venta","% Desc.","V. Desc","Stock","U. Medida","Categoria","Proveedor","Fabricante"};
        dtPrev = productobeans.getProductoFilter(column, operator, value, order);
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
            
            tabla.getColumnModel().getColumn(2).setMaxWidth(120);
            tabla.getColumnModel().getColumn(2).setMinWidth(120);
            tabla.getColumnModel().getColumn(2).setPreferredWidth(120);
            
            tabla.getColumnModel().getColumn(3).setMaxWidth(200);
            tabla.getColumnModel().getColumn(3).setMinWidth(200);
            tabla.getColumnModel().getColumn(3).setPreferredWidth(200);
            
            tabla.getColumnModel().getColumn(12).setMaxWidth(100);
            tabla.getColumnModel().getColumn(12).setMinWidth(100);
            tabla.getColumnModel().getColumn(12).setPreferredWidth(100);
        }
        catch (Exception se){
            System.out.println("Debug: Error redimensionar col0: "+se.getMessage());
        }
        //Esconder Comunas
        tabla.getColumnModel().getColumn(1).setMaxWidth(0);
        tabla.getColumnModel().getColumn(1).setMinWidth(0);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(0);
        
        tabla.getColumnModel().getColumn(4).setMaxWidth(0);
        tabla.getColumnModel().getColumn(4).setMinWidth(0);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(0);
        
        tabla.getColumnModel().getColumn(5).setMaxWidth(0);
        tabla.getColumnModel().getColumn(5).setMinWidth(0);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(0);
        
        tabla.getColumnModel().getColumn(7).setMaxWidth(0);
        tabla.getColumnModel().getColumn(7).setMinWidth(0);
        tabla.getColumnModel().getColumn(7).setPreferredWidth(0);
        
        tabla.getColumnModel().getColumn(10).setMaxWidth(0);
        tabla.getColumnModel().getColumn(10).setMinWidth(0);
        tabla.getColumnModel().getColumn(10).setPreferredWidth(0);
////        
//        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
//        tabla.getColumnModel().getColumn(0).setMinWidth(0);
//        tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
        JTable newRenderedTable = Producto.getNewRenderedTable(tabla);
    }
    
    private static JTable getNewRenderedTable(final JTable table) {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                String status = (String)table.getModel().getValueAt(row, STATUS_COL);
                System.out.println("dsada"+status);
                if ("4".equals(status)) {
                    setBackground(Color.WHITE);
                    setForeground(Color.RED);
                } else {
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());
                }       
                return this;
            }   
        });
        return table;
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
    
    private String getNombreCategorias(String idCategorias){
        categoriasbeans.setIdCategoria(idCategorias);
        
        if(!categoriasbeans.findByID()){
            JOptionPane.showMessageDialog(null,"Error al obtener nombre categoria\n"+categoriasbeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
        } else {
            return categoriasbeans.getNombre();
        }
        return null;
    }
    
    private String getIdCategorias(String nombreCategorias){
        categoriasbeans.setNombre(nombreCategorias);

        if(!categoriasbeans.findByName()){
            JOptionPane.showMessageDialog(null,"Error al obtener ID categoria\n"+categoriasbeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
        }else{
            return categoriasbeans.getIdCategoria();
        }
        return null;
    }
    
    private void getComboCategorias(){
        try{
            DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
            PreparedStatement pstm = getConnection().prepareStatement("SELECT * FROM categorias ORDER BY nombre");
            try (ResultSet res = pstm.executeQuery()) {
                modeloCombo.addElement("Seleccione");
                while (res.next()) {
                    modeloCombo.addElement(res.getObject("nombre"));
                }
            }
            this.closeConnection();
            this.cbCategorias.setModel(modeloCombo);
        } catch (SQLException se) {
            System.out.println("Error al cargar combo="+se);
        }
    }
    
    private String getNombreFabricante(String idFabricante){
        fabricantebeans.setIdFabricante(idFabricante);
        
        if(!fabricantebeans.findByID()){
            JOptionPane.showMessageDialog(null,"Error al obtener nombre fabricante\n"+fabricantebeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
        } else {
            return fabricantebeans.getNombre();
        }
        return null;
    }
    
    private String getIdFabricante(String nombreFabricante){
        fabricantebeans.setNombre(nombreFabricante);

        if(!fabricantebeans.findByName()){
            JOptionPane.showMessageDialog(null,"Error al obtener ID fabricante\n"+fabricantebeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
        }else{
            return fabricantebeans.getIdFabricante();
        }
        return null;
    }
    
    private void getComboFabricante(){
        try{
            DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
            PreparedStatement pstm = getConnection().prepareStatement("SELECT * FROM fabricante ORDER BY nombre");
            try (ResultSet res = pstm.executeQuery()) {
                modeloCombo.addElement("Seleccione");
                while (res.next()) {
                    modeloCombo.addElement(res.getObject("nombre"));
                }
            }
            this.closeConnection();
            this.cbFabricante.setModel(modeloCombo);
        } catch (SQLException se) {
            System.out.println("Error al cargar combo="+se);
        }
    }
    
    public boolean verificaDatos(){
        String idProducto = this.lblIdProducto.getText();
        String codReferencia = this.txtReferencia.getText();
        String ean = this.txtEan.getText();
        String nombre = this.txtNombre.getText();
        String descripcion = this.txtDescripcion.getText();
        String fechaIngreso = this.lblFechaIngreso.getText();
        String valorProveedor = String.valueOf(this.getValorProveedor());
        String valorNeto = String.valueOf(this.getValorNeto());
        String valorIva = String.valueOf(this.getValorTotal());
        String porcentajeDescuento = this.jsnDescuento.getValue().toString();
        String vencimientoDescuento = null;
        String cantidad = String.valueOf(this.getStock());
        String unidadmedida = this.cbUnidadMedida.getSelectedItem().toString();
        String idCategoria = this.cbCategorias.getSelectedItem().toString();
        String idProveedor = this.cbProveedor.getSelectedItem().toString();
        String idFabricante = this.cbFabricante.getSelectedItem().toString();
        
        String valorTotal = String.valueOf(this.getValorTotal());
        String porcentajeGanancia = String.valueOf(this.getPorcentajeGanancia());
        
        boolean adicionProducto = this.rbtnAgregar.isSelected();
        boolean mermaProducto = this.rbtnMermar.isSelected();
        String nuevoStock = String.valueOf(this.getNuevoStock());
        
        if(ean.isEmpty()){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe llenar el campo:\n- EAN", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
            this.txtEan.requestFocus();
        } else if (nombre.isEmpty()){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe llenar el campo:\n- Nombre", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
            this.txtNombre.requestFocus();
        } else if(porcentajeGanancia.equals("0")){
            JOptionPane.showMessageDialog(null,"El producto no tiene un porcentaje de ganancia.\nAsigne un valor diferente al 0%.", "Verificación de Formularios", JOptionPane.WARNING_MESSAGE);
        } else if(valorProveedor.equals("0")){
            JOptionPane.showMessageDialog(null,"Debe ingresar el valor sin IVA del producto.\nAsigne un valor distinto de $0", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else if(valorTotal.equals("0")){
            JOptionPane.showMessageDialog(null,"Debe ingresar el valor con iva del producto.\nAsigne un valor distinto de $0", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        
//        } else if(!adicionProducto&&!mermaProducto){
//            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe seleccionar una funcion de stock.\nSeleccione si va añadir o quitar productos de su stock.", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
//        } else if(nuevoStock.equals("0")){
//            JOptionPane.showMessageDialog(null,"Debe ingresar al valor del nuevo stock según la funcion seleccionada.", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else if(unidadmedida.equals("Seleccione")){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe seleccionar:\n- Unidad de Medida", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else if(idCategoria.equals("Seleccione")){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe seleccionar:\n- Categoria", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else if(idProveedor.equals("Seleccione")){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe seleccionar:\n- Proveedor", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else if(idFabricante.equals("Seleccione")){
            JOptionPane.showMessageDialog(null,"Formulario Inclompleto. Debe seleccionar:\n- fabricante", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
        } else {
            System.out.println("Envio datos a calse entidad product0");
            if(idProducto.equals("Por Definir")){
                idProducto = null;
            }
            if(!adicionProducto&&!mermaProducto){
                cantidad = this.lblStock.getText();
            }
            productobeans.setIdProducto(idProducto);
            productobeans.setCodReferencia(codReferencia);
            productobeans.setEan(ean);
            productobeans.setNombre(nombre);
            productobeans.setDescripcion(descripcion);
            productobeans.setFechaIngreso(fechaIngreso);
            productobeans.setValorProveedor(valorProveedor);
            productobeans.setValorNeto(valorNeto);
            productobeans.setValorIva(valorIva);
            productobeans.setPorcentajeDescuento(porcentajeDescuento);
            productobeans.setVencimientoDescuento(vencimientoDescuento);
            productobeans.setCantidad(cantidad);
            productobeans.setUnidadmedida(unidadmedida);
            productobeans.setIdCategoria(this.getIdCategorias(idCategoria));
            productobeans.setIdProveedor(this.getIdProveedor(idProveedor));
            productobeans.setIdFabricante(this.getIdFabricante(idFabricante));
            return true;
        }
        return false;
    }
    
    private boolean existencia(){
        String ean = this.txtEan.getText();
        
        productobeans.setEan(ean);
        
        if(productobeans.findByEan()!=false){
            JOptionPane.showMessageDialog(null,"El producto Cod. EAN \""+ean+"\" ya existe", "Verificación de Formularios", JOptionPane.ERROR_MESSAGE);
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
        String idProducto = this.lblIdProducto.getText();
        String ean = this.txtEan.getText();
        String nombre = this.txtNombre.getText();
        String codReferencia = this.txtReferencia.getText();
        
        switch(seleccion){
            case 0:
                productobeans.setIdProducto(idProducto);
                if(productobeans.findByID()!=false){
                    this.cargaDatos();
                } else {
//                    this.limpiar();
//                    this.lblErrorBusqueda.setText("La Categoría ID \""+idCategoria+"\" no existe");
//                    this.lblErrorBusqueda.setText("No hemos encontrado lo que búscas!");
                }
                break;
            case 1:
                productobeans.setEan(ean);
                if(productobeans.findByEan()!=false){
                   this.cargaDatos();
                } else {
//                    this.limpiar();
//                    this.lblErrorBusqueda.setText("La Categoría nombre \""+nombre+"\" no existe");
//                    this.lblErrorBusqueda.setText("No hemos encontrado lo que búscas!");
                }
                break;
            case 3:
                productobeans.setCodReferencia(codReferencia);
                if(productobeans.findByRef()!=false){
                   this.cargaDatos();
                } else {
//                    this.limpiar();
//                    this.lblErrorBusqueda.setText("La Categoría nombre \""+nombre+"\" no existe");
//                    this.lblErrorBusqueda.setText("No hemos encontrado lo que búscas!");
                }
                break;
            case 4:
                productobeans.setNombre(nombre);
                if(productobeans.findByName()!=false){
                   this.cargaDatos();
                } else {
//                    this.limpiar();
//                    this.lblErrorBusqueda.setText("La Categoría nombre \""+nombre+"\" no existe");
//                    this.lblErrorBusqueda.setText("No hemos encontrado lo que búscas!");
                }
                break;
            default:
                System.out.println("Switch to default");
//                this.limpiar();
        }
    }
    
    private void cargaDatos(){
        this.limpiar();
        this.lblIdProducto.setText(productobeans.getIdProducto());
        this.txtReferencia.setText(productobeans.getCodReferencia());
        this.txtEan.setText(productobeans.getEan());
        this.txtNombre.setText(productobeans.getNombre());
        this.txtDescripcion.setText(productobeans.getDescripcion());
        this.lblFechaIngreso.setText(productobeans.getFechaIngreso());
        
        this.txtValorProveedor.setValue(this.setValorProveedor(productobeans.getValorProveedor()));
        this.txtValorNeto.setText(productobeans.getValorNeto());
        this.txtTotal.setText(productobeans.getValorIva());
        this.txtPrecioVenta.setValue(Integer.parseInt(productobeans.getValorIva()));
        int descuento = Integer.parseInt(productobeans.getPorcentajeDescuento());
        System.out.println("Descuento="+descuento);
        if(descuento>0){
            System.out.println("Set descuento enables true");
            this.chkDescuento.setSelected(true);
            this.jsnDescuento.setValue(new Integer(descuento));
            this.calcularDescuento();
        }
        System.out.println("despues del if 0 desc");
        this.lblStock.setText(productobeans.getCantidad());
        this.cbUnidadMedida.setSelectedItem(productobeans.getUnidadmedida());
        this.cbCategorias.setSelectedItem(this.getNombreCategorias(productobeans.getIdCategoria()));
        this.cbProveedor.setSelectedItem(this.getNombreProveedor(productobeans.getIdProveedor()));
        this.cbFabricante.setSelectedItem(this.getNombreFabricante(productobeans.getIdFabricante()));
        this.quitarIva();
        this.btnGuardar.setEnabled(false);
        this.btnModificar.setEnabled(true);
        this.btnEliminar.setEnabled(true);
    }
    
    private void limpiar(){
        this.inicializa();
        this.lblIdProducto.setText("Por Definir");
        this.txtEan.setText("");
        this.txtNombre.setText("");
        this.txtReferencia.setText("");
        this.txtDescripcion.setText("");
        this.jsnPorcentajeGanancia.setValue(new Integer(0));
        this.setValorProveedor("0");
        this.txtValorNeto.setValue(0);
        this.txtTotal.setValue(0);
        this.chkDescuento.setSelected(false);
        this.txtPrecioVenta.setValue(0);
        this.lblStock.setText("0");
        this.rbtnAgregar.setSelected(false);
        this.rbtnMermar.setSelected(false);
        this.txtNuevoStock.setValue(0);
        this.lblStockFinal.setText("(0 Prod.)");
        this.cbUnidadMedida.setSelectedItem("Seleccione");
        this.cbCategorias.setSelectedItem("Seleccione");
        this.cbProveedor.setSelectedItem("Seleccione");
        this.cbFabricante.setSelectedItem("Seleccione");
        this.cargaTablaProducto();
        this.btnGuardar.setEnabled(true);
        this.btnModificar.setEnabled(false);
        this.btnEliminar.setEnabled(false);
    }
    
    //Adiciones y mermas de productos
    private int getStock(){
        this.txtNuevoStock.setEnabled(true);
        int stockActual = Integer.parseInt(this.lblStock.getText());
        int nuevoStock = this.getNuevoStock();
        int stock = 0;
        if(this.rbtnAgregar.isSelected()){
            stock = stockActual+nuevoStock;
            if(stock<0){
                stock = 0;
            }
            this.lblStockFinal.setText("("+stock+" Prod.)");
        } else if (this.rbtnMermar.isSelected()){
            stock = stockActual-nuevoStock;
            if(stock<0){
                stock = 0;
            }
            this.lblStockFinal.setText("("+stock+" Prod.)");
        }
        System.out.println("Stock="+stock);
        return stock;
    }
   
    private int getNuevoStock(){
        String nuevoStock = this.txtNuevoStock.getText();
        try{
            return Integer.parseInt(nuevoStock);
        } catch (Exception se){
            System.out.println("Error getNuevoStock");
        }
        return 0;
    }
    
    private double getPrecioVenta(){
        String precioVenta = this.txtPrecioVenta.getText();
        try{
            return Double.parseDouble(precioVenta);
        } catch(Exception se){
            System.out.println("Error getPrecioVenta");
        }
        return 0;
    }
    
    private double getValorProveedor(){
        String valorProveedor = this.txtValorProveedor.getText();
        try{
            return Double.parseDouble(valorProveedor);
        } catch (Exception se){
            System.out.println("Error getValorProveedor="+se);
        }
        return 0;
        
    }
    
    private double setValorProveedor(String valorProveedor){
//        String valorProveedor = this.txtValorProveedor.getText();
        try{
            return Double.parseDouble(valorProveedor);
        } catch (Exception se){
            System.out.println("Error setValorProveedor="+se);
        }
        return 0;
        
    }
    
    private double getPorcentajeGanancia(){
        String porcentajeGanancia = this.jsnPorcentajeGanancia.getValue().toString();
        try{
            return Double.parseDouble(porcentajeGanancia);
        } catch (Exception se){
            System.out.println("Error getPorcentajeGanancia="+se);
        }
        return 0;
    }
    
    private double getPorcentajeDescuento(){
        String porcentajeDescuento = this.jsnDescuento.getValue().toString();
        try{
            return Double.parseDouble(porcentajeDescuento);
        } catch (Exception se){
            System.out.println("Erroe getPorcentajeDesceunto");
        }
        return 0;
    }
    
    private double getValorNeto(){
        String valorNeto = this.txtValorNeto.getValue().toString();
        try{
            return Double.parseDouble(valorNeto);
        } catch(Exception se){
            System.out.println("Error getValorNeto="+se);
        }
        return 0;
    }  
    
    private double getValorTotal(){
        String valorTotal = this.txtTotal.getText();
        try{
            return Double.parseDouble(valorTotal);
        }catch(Exception se){
            System.out.println("Error getValorTotal="+se);
        }
        return 0;
    }
    
    private void valorGananciaTotal(){
        double valorIva = 19.0;
        double valorGanancia;
        double valorNeto;
        double valorTotal;
        valorGanancia = Math.round((this.getValorProveedor()*this.getPorcentajeGanancia())/100);
        valorNeto = Math.round(this.getValorProveedor()+valorGanancia);
        valorTotal = Math.round(((valorNeto*valorIva)/100)+valorNeto);
        this.txtValorNeto.setValue(valorNeto);
        this.txtTotal.setValue(valorTotal);
    }
    
    private void obtenerPorcentajeGanancia(){
        if(this.getValorNeto()!=0.0){
        double diffGanancia;
        double porcentajeGanancia;
        diffGanancia = (this.getValorNeto()-this.getValorProveedor());
        porcentajeGanancia = Math.round((diffGanancia/this.getValorProveedor())*100);
        System.out.println("Porcentaje ganancia="+porcentajeGanancia);
        this.jsnPorcentajeGanancia.setValue((int)porcentajeGanancia);
        porcentajeGanancia = 0;
        } else {
            this.jsnPorcentajeGanancia.setValue(new Integer(0));
        }
    }
    
    private void quitarIva(){
        double valorNeto = Math.round(this.getValorTotal()/1.19);
        this.txtValorNeto.setValue(valorNeto);
        
        this.obtenerPorcentajeGanancia();
    }
    
    private void seccionDescuento(){
        if(this.chkDescuento.isSelected()){
            this.jsnDescuento.setEnabled(true);
            if("0".equals(this.jsnDescuento.getValue().toString())){
                this.getValorTotal();
            } else {
                this.calcularDescuento();
            }
        } else {
            this.jsnDescuento.setEnabled(false);
            this.jsnDescuento.setValue(new Integer(0));
            if(this.getValorTotal()>0){
                this.txtPrecioVenta.setValue(this.getValorTotal());
            }
        }
    }
    
    private void calcularDescuento(){
        double valorDescuento = (this.getValorTotal()*this.getPorcentajeDescuento())/100;
        double totalDescuento = Math.round(this.getValorTotal()-valorDescuento);
        
        this.txtPrecioVenta.setValue(totalDescuento);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rbtnGroupStock = new javax.swing.ButtonGroup();
        rbtnGroupBuscar = new javax.swing.ButtonGroup();
        rbtnGroupFiltrar = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblIdProducto = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtReferencia = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtEan = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        lblFechaIngreso = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jsnPorcentajeGanancia = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JFormattedTextField();
        txtValorNeto = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        txtValorProveedor = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        chkDescuento = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        jsnDescuento = new javax.swing.JSpinner();
        jLabel14 = new javax.swing.JLabel();
        txtPrecioVenta = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        rbtnAgregar = new javax.swing.JRadioButton();
        rbtnMermar = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        lblStock = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        lblDlogAgregar = new javax.swing.JLabel();
        txtNuevoStock = new javax.swing.JFormattedTextField();
        lblStockFinal = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        cbUnidadMedida = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        cbCategorias = new javax.swing.JComboBox();
        cbProveedor = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cbFabricante = new javax.swing.JComboBox();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAyuda = new javax.swing.JTextArea();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        rbtnEan = new javax.swing.JRadioButton();
        rbtnReferencia = new javax.swing.JRadioButton();
        rbtnNombre = new javax.swing.JRadioButton();
        btnBuscar = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        rbtnFiltrarTodos = new javax.swing.JRadioButton();
        rbtnFiltrarStockBajo = new javax.swing.JRadioButton();
        rbtnFiltrarDescuento = new javax.swing.JRadioButton();
        btnFiltrar = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel1.setText("ID:");

        lblIdProducto.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        lblIdProducto.setText("Por Definir");

        jLabel3.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel3.setText("Ref.");

        jLabel4.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel4.setText("EAN");

        txtEan.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N

        txtNombre.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel5.setText("Nombre");

        jLabel6.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel6.setText("Descripción");

        txtDescripcion.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel7.setText("Fecha Ingreso:");

        lblFechaIngreso.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        lblFechaIngreso.setText("dd/mm/aaaa hh:mm:ss");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Valores del Producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel11.setText("% Ganancia");

        jsnPorcentajeGanancia.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jsnPorcentajeGanancia.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 1));
        jsnPorcentajeGanancia.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jsnPorcentajeGananciaStateChanged(evt);
            }
        });
        jsnPorcentajeGanancia.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jsnPorcentajeGananciaCaretPositionChanged(evt);
            }
        });
        jsnPorcentajeGanancia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jsnPorcentajeGananciaKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jsnPorcentajeGananciaKeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel10.setText("Valor Neto");

        jLabel12.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel12.setText("Precio + 19% IVA");

        txtTotal.setForeground(new java.awt.Color(255, 0, 0));
        txtTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtTotal.setText("0");
        txtTotal.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        txtTotal.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtTotalCaretUpdate(evt);
            }
        });
        txtTotal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTotalFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotalFocusLost(evt);
            }
        });
        txtTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTotalKeyReleased(evt);
            }
        });

        txtValorNeto.setEditable(false);
        txtValorNeto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtValorNeto.setDisabledTextColor(new java.awt.Color(1, 1, 1));
        txtValorNeto.setEnabled(false);
        txtValorNeto.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel9.setText("Valor Proveedor");

        txtValorProveedor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtValorProveedor.setText("0");
        txtValorProveedor.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        txtValorProveedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValorProveedorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtValorProveedorFocusLost(evt);
            }
        });
        txtValorProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtValorProveedorKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jsnPorcentajeGanancia, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(txtValorProveedor))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtValorNeto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jsnPorcentajeGanancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValorNeto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValorProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Descuentos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14))); // NOI18N

        chkDescuento.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        chkDescuento.setText("Descuento al Producto (Se aplica al valor neto)");
        chkDescuento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkDescuentoItemStateChanged(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel13.setText("% Descuento");

        jsnDescuento.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jsnDescuento.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 1));
        jsnDescuento.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jsnDescuentoStateChanged(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel14.setText("$ Venta");

        txtPrecioVenta.setForeground(new java.awt.Color(47, 115, 44));
        txtPrecioVenta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtPrecioVenta.setText("0");
        txtPrecioVenta.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkDescuento)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jsnDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(chkDescuento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jsnDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Stok de Producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14))); // NOI18N

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "¿Que desaea hacer?", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14))); // NOI18N

        rbtnAgregar.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        rbtnAgregar.setText("Agregar Unidades al stock actual");
        rbtnAgregar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbtnAgregarItemStateChanged(evt);
            }
        });

        rbtnMermar.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        rbtnMermar.setText("Merma de unidades al stock actual");
        rbtnMermar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbtnMermarItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbtnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbtnMermar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(rbtnAgregar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rbtnMermar)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Stock Actual", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14))); // NOI18N

        lblStock.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        lblStock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStock.setText("0");

        jLabel16.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Unidades");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(lblStock)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ingreso/Merma de Productos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14))); // NOI18N

        lblDlogAgregar.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        lblDlogAgregar.setText("Ingrese la cantidad de productos");

        txtNuevoStock.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtNuevoStock.setText("0");
        txtNuevoStock.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNuevoStockFocusGained(evt);
            }
        });
        txtNuevoStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNuevoStockKeyReleased(evt);
            }
        });

        lblStockFinal.setText("(0 Prod.)");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDlogAgregar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNuevoStock, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStockFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDlogAgregar)
                    .addComponent(txtNuevoStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStockFinal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Unidad de Medida", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14))); // NOI18N

        cbUnidadMedida.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        cbUnidadMedida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione", "Unidades", "Kilogramos", "Litros" }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbUnidadMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbUnidadMedida, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Asociaciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14))); // NOI18N

        jLabel18.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel18.setText("Categoría");

        jLabel19.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel19.setText("Proveedor");

        cbCategorias.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        cbCategorias.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione" }));

        cbProveedor.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        cbProveedor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione" }));

        jLabel2.setText("Fabricante");

        cbFabricante.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione" }));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel18)
                .addGap(6, 6, 6)
                .addComponent(cbCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbFabricante, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cbCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel18)
                .addComponent(jLabel19)
                .addComponent(cbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel2)
                .addComponent(cbFabricante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Panel de ayuda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14))); // NOI18N

        txtAyuda.setColumns(20);
        txtAyuda.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        txtAyuda.setLineWrap(true);
        txtAyuda.setRows(2);
        txtAyuda.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtAyuda);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista de Productos"));

        tabla.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
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
        jScrollPane2.setViewportView(tabla);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNombre)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(23, 23, 23)
                                .addComponent(txtDescripcion))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblFechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtEan, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(58, 58, 58)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIdProducto)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7)
                    .addComponent(lblFechaIngreso)
                    .addComponent(jLabel4)
                    .addComponent(txtEan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Funciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14))); // NOI18N

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda por:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14))); // NOI18N

        rbtnEan.setText("EAN");

        rbtnReferencia.setText("Referencia");

        rbtnNombre.setText("Nombre");

        btnBuscar.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbtnEan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbtnReferencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbtnNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(rbtnEan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbtnReferencia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbtnNombre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscar))
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Edición"));

        btnGuardar.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnModificar.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnNuevo.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnLimpiar.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpiar))
        );

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtrar Lista por:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14))); // NOI18N

        rbtnFiltrarTodos.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        rbtnFiltrarTodos.setText("Todos");

        rbtnFiltrarStockBajo.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        rbtnFiltrarStockBajo.setText("Stock Bajo");

        rbtnFiltrarDescuento.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        rbtnFiltrarDescuento.setText("Con Descuento");

        btnFiltrar.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        btnFiltrar.setText("Filtrar");
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbtnFiltrarTodos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbtnFiltrarStockBajo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(rbtnFiltrarDescuento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnFiltrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 6, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(rbtnFiltrarTodos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbtnFiltrarStockBajo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbtnFiltrarDescuento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFiltrar))
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(btnCerrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jsnPorcentajeGananciaCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jsnPorcentajeGananciaCaretPositionChanged
        // TODO add your handling code here:
        //eliminar
    }//GEN-LAST:event_jsnPorcentajeGananciaCaretPositionChanged

    private void jsnPorcentajeGananciaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jsnPorcentajeGananciaStateChanged
        // TODO add your handling code here:
        if(this.txtTotal.isFocusOwner()){
            this.obtenerPorcentajeGanancia();
            this.seccionDescuento();
        } else {
            this.valorGananciaTotal();
            this.seccionDescuento();
        }
//        int porcentaje = (int)this.jsnPorcentajeGanancia.getValue();
//        if(porcentaje>0){
//            this.txtValorProveedor.setEnabled(true);
//            if(this.getValorProveedor()>0){
//                this.valorGananciaTotal();
//            }
//        } else {
//            this.txtValorProveedor.setEnabled(false);
//        }
//        try{
//            this.valorGananciaTotal();
//        } catch (Exception se){
//            System.out.println("Error="+se);
//        }
        
    }//GEN-LAST:event_jsnPorcentajeGananciaStateChanged

    private void txtTotalCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtTotalCaretUpdate
        // TODO add your handling code here:
        //eliminar
    }//GEN-LAST:event_txtTotalCaretUpdate

    private void txtTotalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalKeyReleased
        // TODO add your handling code here:
        this.quitarIva();
        this.seccionDescuento();
    }//GEN-LAST:event_txtTotalKeyReleased

    private void jsnPorcentajeGananciaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jsnPorcentajeGananciaKeyReleased
        // TODO add your handling code here:
        //eliminar
    }//GEN-LAST:event_jsnPorcentajeGananciaKeyReleased

    private void jsnPorcentajeGananciaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jsnPorcentajeGananciaKeyTyped
        // TODO add your handling code here:
        //eliminar
    }//GEN-LAST:event_jsnPorcentajeGananciaKeyTyped

    private void txtValorProveedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorProveedorKeyReleased
        // TODO add your handling code here:
        this.valorGananciaTotal();
        this.seccionDescuento();
    }//GEN-LAST:event_txtValorProveedorKeyReleased

    private void txtValorProveedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorProveedorFocusGained
        // TODO add your handling code here:
        this.txtValorProveedor.setText("");
    }//GEN-LAST:event_txtValorProveedorFocusGained

    private void txtValorProveedorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorProveedorFocusLost
        // TODO add your handling code here:
        //eliminar
    }//GEN-LAST:event_txtValorProveedorFocusLost

    private void txtTotalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotalFocusGained
        // TODO add your handling code here:
        this.txtTotal.setText("");
    }//GEN-LAST:event_txtTotalFocusGained

    private void txtTotalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotalFocusLost
        // TODO add your handling code here:
        //eliminar
    }//GEN-LAST:event_txtTotalFocusLost

    private void chkDescuentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkDescuentoItemStateChanged
        // TODO add your handling code here:
        this.seccionDescuento();
    }//GEN-LAST:event_chkDescuentoItemStateChanged

    private void jsnDescuentoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jsnDescuentoStateChanged
        // TODO add your handling code here:
        this.seccionDescuento();
    }//GEN-LAST:event_jsnDescuentoStateChanged

    private void rbtnAgregarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbtnAgregarItemStateChanged
        // TODO add your handling code here:
        this.getStock();
    }//GEN-LAST:event_rbtnAgregarItemStateChanged

    private void rbtnMermarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbtnMermarItemStateChanged
        // TODO add your handling code here:
        this.getStock();
    }//GEN-LAST:event_rbtnMermarItemStateChanged

    private void txtNuevoStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNuevoStockKeyReleased
        // TODO add your handling code here:
        this.getStock();
    }//GEN-LAST:event_txtNuevoStockKeyReleased

    private void txtNuevoStockFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNuevoStockFocusGained
        // TODO add your handling code here:
        String nuevoStock = this.txtNuevoStock.getText();
        if(nuevoStock.equals("0")){
            this.txtNuevoStock.setText("");
        }
    }//GEN-LAST:event_txtNuevoStockFocusGained

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        this.busy(1);
        if(this.verificaDatos()==true&&this.existencia()){
            if(productobeans.save()==false){
                JOptionPane.showMessageDialog(null,"Error: "+productobeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
            } else {
                this.buscar(1);
                this.cargaTablaProducto();
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
            if(productobeans.update()==false){
                JOptionPane.showMessageDialog(null,"Error: "+productobeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
            } else {
                this.buscar(0);
                this.cargaTablaProducto();
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
            if(productobeans.delete()==false){
                JOptionPane.showMessageDialog(null,"Error: "+productobeans.getError(), "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
            } else {
                this.cargaTablaProducto();
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
            this.lblIdProducto.setText(String.valueOf(tabla.getValueAt(fila, 0)));
            this.buscar(0);
        }
    }//GEN-LAST:event_tablaMouseClicked

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        this.limpiar();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        this.limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        if(this.rbtnEan.isSelected()){
            this.buscar(1);
        } else if (this.rbtnReferencia.isSelected()){
            this.buscar(3);
        } else if (this.rbtnNombre.isSelected()){
            this.buscar(4);
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        // TODO add your handling code here:
        if(this.rbtnFiltrarTodos.isSelected()){
            this.cargaTablaProducto();
        } else if(this.rbtnFiltrarDescuento.isSelected()){
            this.cargaTablaProductoFilter("porcentajeDescuento", ">", "0", "porcentajeDescuento");
        } else if(this.rbtnFiltrarStockBajo.isSelected()){
            this.cargaTablaProductoFilter("cantidad", "<=", "5", "cantidad");
        }
    }//GEN-LAST:event_btnFiltrarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox cbCategorias;
    private javax.swing.JComboBox cbFabricante;
    private javax.swing.JComboBox cbProveedor;
    private javax.swing.JComboBox cbUnidadMedida;
    private javax.swing.JCheckBox chkDescuento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jsnDescuento;
    private javax.swing.JSpinner jsnPorcentajeGanancia;
    private javax.swing.JLabel lblDlogAgregar;
    private javax.swing.JLabel lblFechaIngreso;
    private javax.swing.JLabel lblIdProducto;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lblStockFinal;
    private javax.swing.JRadioButton rbtnAgregar;
    private javax.swing.JRadioButton rbtnEan;
    private javax.swing.JRadioButton rbtnFiltrarDescuento;
    private javax.swing.JRadioButton rbtnFiltrarStockBajo;
    private javax.swing.JRadioButton rbtnFiltrarTodos;
    private javax.swing.ButtonGroup rbtnGroupBuscar;
    private javax.swing.ButtonGroup rbtnGroupFiltrar;
    private javax.swing.ButtonGroup rbtnGroupStock;
    private javax.swing.JRadioButton rbtnMermar;
    private javax.swing.JRadioButton rbtnNombre;
    private javax.swing.JRadioButton rbtnReferencia;
    private javax.swing.JTable tabla;
    private javax.swing.JTextArea txtAyuda;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtEan;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JFormattedTextField txtNuevoStock;
    private javax.swing.JFormattedTextField txtPrecioVenta;
    private javax.swing.JTextField txtReferencia;
    private javax.swing.JFormattedTextField txtTotal;
    private javax.swing.JFormattedTextField txtValorNeto;
    private javax.swing.JFormattedTextField txtValorProveedor;
    // End of variables declaration//GEN-END:variables
}
