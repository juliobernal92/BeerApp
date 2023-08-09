package Dao;

import Entidades.DetallesReceta;
import Utilidades.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetallesRecetaDao extends Conexion {

    public List<DetallesReceta> obtenerRecetaInsumoPorEstiloIdReceta(String estilo, int idReceta) {
        Connection con = getConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT dr.id, dr.receta_id, dr.insumo_id, dr.cantidad, dr.costo_unitario "
                 + "FROM detalles_receta dr "
                 + "JOIN recetas r ON dr.receta_id = r.id "
                 + "WHERE r.nombre = ? AND r.id = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, estilo);
            ps.setInt(2, idReceta);
            rs = ps.executeQuery();

            List<DetallesReceta> recetaInsumos = new ArrayList<>();

            while (rs.next()) {
                DetallesReceta detallesReceta = new DetallesReceta();
                detallesReceta.setIdDetalles(rs.getInt("id"));
                detallesReceta.setIdInsumos(rs.getInt("insumo_id"));
                detallesReceta.setCantidad(rs.getBigDecimal("cantidad"));
                detallesReceta.setCostoUnitario(rs.getInt("costo_unitario"));
                recetaInsumos.add(detallesReceta);
            }

            return recetaInsumos;
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }

        return Collections.emptyList();
    }
    
    
    public boolean Insertar(DetallesReceta detalles) {
        Connection con = getConexion();
        PreparedStatement ps = null;
        String sql = "INSERT INTO public.detalles_receta(receta_id, insumo_id, cantidad, costo_unitario) VALUES (?, ?, ?, ?);";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, detalles.getIdReceta());
            ps.setInt(2, detalles.getIdInsumos());
            ps.setBigDecimal(3, detalles.getCantidad());
            ps.setInt(4,detalles.getCostoUnitario());

            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
    
    public boolean eliminar(int idDetalles) {
        Connection con = getConexion();
        PreparedStatement ps = null;
        int resultado = 0;
        String sql = "delete from detalles_receta where id=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, idDetalles);
            resultado = ps.executeUpdate();
            System.out.println("Filas eliminadas: " + resultado);

            return resultado > 0;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
    
    public boolean Actualizar(DetallesReceta detalles) {
        Connection con = getConexion();
        PreparedStatement ps = null;
        String sql = "UPDATE public.detalles_receta SET receta_id=?, insumo_id=?, cantidad=?, costo_unitario=? WHERE id= ?";
        
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, detalles.getIdReceta());
            ps.setInt(2, detalles.getIdInsumos());
            ps.setBigDecimal(3, detalles.getCantidad());
            ps.setInt(4,detalles.getCostoUnitario());
            ps.setInt(5, detalles.getIdDetalles());

            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    
    public boolean actualizarCostoUnitario(int idDetalles, int nuevoCosto) {
    Connection con = getConexion();
    PreparedStatement ps = null;
    String sql = "UPDATE public.detalles_receta SET costo_unitario=? WHERE id= ?";
    
    try {
        ps = con.prepareStatement(sql);
        ps.setInt(1, nuevoCosto);
        ps.setInt(2, idDetalles);

        ps.executeUpdate(); // Utiliza executeUpdate en lugar de execute para actualizaciones

        return true;
    } catch (SQLException e) {
        System.err.println(e);
        return false;
    } finally {
        try {
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}


}
