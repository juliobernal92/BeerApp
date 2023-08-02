package Entidades;

import java.math.BigDecimal;

public class DetallesReceta {
    private int IdDetalles;
    private int IdReceta;
    private int IdInsumos;
    private BigDecimal Cantidad;
    private int CostoUnitario;

    /**
     * @return the IdDetalles
     */
    public int getIdDetalles() {
        return IdDetalles;
    }

    /**
     * @param IdDetalles the IdDetalles to set
     */
    public void setIdDetalles(int IdDetalles) {
        this.IdDetalles = IdDetalles;
    }

    /**
     * @return the IdReceta
     */
    public int getIdReceta() {
        return IdReceta;
    }

    /**
     * @param IdReceta the IdReceta to set
     */
    public void setIdReceta(int IdReceta) {
        this.IdReceta = IdReceta;
    }

    /**
     * @return the IdInsumos
     */
    public int getIdInsumos() {
        return IdInsumos;
    }

    /**
     * @param IdInsumos the IdInsumos to set
     */
    public void setIdInsumos(int IdInsumos) {
        this.IdInsumos = IdInsumos;
    }

    /**
     * @return the Cantidad
     */
    public BigDecimal getCantidad() {
        return Cantidad;
    }

    /**
     * @param Cantidad the Cantidad to set
     */
    public void setCantidad(BigDecimal Cantidad) {
        this.Cantidad = Cantidad;
    }

    /**
     * @return the CostoUnitario
     */
    public int getCostoUnitario() {
        return CostoUnitario;
    }

    /**
     * @param CostoUnitario the CostoUnitario to set
     */
    public void setCostoUnitario(int CostoUnitario) {
        this.CostoUnitario = CostoUnitario;
    }
    
    
}
