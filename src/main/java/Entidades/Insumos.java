package Entidades;

import java.math.BigDecimal;

public class Insumos {
    private int IdInsumos;
    private String Nombre;
    private String TipoInsumo;
    private BigDecimal Cantidad;
    private int Costo;

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
     * @return the Nombre
     */
    public String getNombre() {
        return Nombre;
    }

    /**
     * @param Nombre the Nombre to set
     */
    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    /**
     * @return the TipoInsumo
     */
    public String getTipoInsumo() {
        return TipoInsumo;
    }

    /**
     * @param TipoInsumo the TipoInsumo to set
     */
    public void setTipoInsumo(String TipoInsumo) {
        this.TipoInsumo = TipoInsumo;
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
     * @return the Costo
     */
    public int getCosto() {
        return Costo;
    }

    /**
     * @param Costo the Costo to set
     */
    public void setCosto(int Costo) {
        this.Costo = Costo;
    }

    
    
    
}
