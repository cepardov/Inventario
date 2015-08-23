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
package entidad;

/**
 *
 * @author cepardov
 */
public class Producto {
//`idProducto`, `codReferencia`, `ean`, `nombre`, `descripcion`, `fechaIngreso`, `valorProveedor`, `valorNeto`, `valorIva`, `porcentajeDescuento`, `vencimientoDescuento`, `cantidad`, `idCategoria`, `idProveedor`, `idUnidadMedida`
    protected String idProducto;
    protected String codReferencia;
    protected String ean;
    protected String nombre;
    protected String descripcion;
    protected String fechaIngreso;
    protected String valorProveedor;
    protected String valorNeto;
    protected String valorIva;
    protected String porcentajeDescuento;
    protected String vencimientoDescuento;
    protected String cantidad;
    protected String unidadmedida;
    protected String idCategoria;
    protected String idProveedor;
    protected String idFabricante;
    protected String error;

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodReferencia() {
        return codReferencia;
    }

    public void setCodReferencia(String codReferencia) {
        this.codReferencia = codReferencia;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getValorProveedor() {
        return valorProveedor;
    }

    public void setValorProveedor(String valorProveedor) {
        this.valorProveedor = valorProveedor;
    }

    public String getValorNeto() {
        return valorNeto;
    }

    public void setValorNeto(String valorNeto) {
        this.valorNeto = valorNeto;
    }

    public String getValorIva() {
        return valorIva;
    }

    public void setValorIva(String valorIva) {
        this.valorIva = valorIva;
    }

    public String getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(String porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public String getVencimientoDescuento() {
        return vencimientoDescuento;
    }

    public void setVencimientoDescuento(String vencimientoDescuento) {
        this.vencimientoDescuento = vencimientoDescuento;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidadmedida() {
        return unidadmedida;
    }

    public void setUnidadmedida(String unidadmedida) {
        this.unidadmedida = unidadmedida;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getIdFabricante() {
        return idFabricante;
    }

    public void setIdFabricante(String idFabricante) {
        this.idFabricante = idFabricante;
    }

    
}