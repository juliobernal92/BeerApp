package Entidades;

import java.math.BigDecimal;

public class Recetas {
    private int IdRecetas;
    private String Nombre;
    private BigDecimal Litros;
    private int CostoTotal;

    /**
     * @return the IdRecetas
     */
    public int getIdRecetas() {
        return IdRecetas;
    }

    /**
     * @param IdRecetas the IdRecetas to set
     */
    public void setIdRecetas(int IdRecetas) {
        this.IdRecetas = IdRecetas;
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

    public BigDecimal getLitros() {
        return Litros;
    }

    public void setLitros(BigDecimal Litros) {
        this.Litros = Litros;
    }

    
    
    
    /**
     * @return the CostoTotal
     */
    public int getCostoTotal() {
        return CostoTotal;
    }

    /**
     * @param CostoTotal the CostoTotal to set
     */
    public void setCostoTotal(int CostoTotal) {
        this.CostoTotal = CostoTotal;
    }
    
    
}
