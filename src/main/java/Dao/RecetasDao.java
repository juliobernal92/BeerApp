package Dao;

import Entidades.Recetas;
import Utilidades.Conexion;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecetasDao extends Conexion {

    //METODO PARA INSERTAR
    public boolean Insertar(Recetas recetas) {
        Connection con = getConexion();
        PreparedStatement ps = null;
        String sql = "INSERT INTO recetas(nombre, litros, costo_total) VALUES (?, ?, ?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, recetas.getNombre());
            ps.setBigDecimal(2, recetas.getLitros());
            ps.setInt(3, recetas.getCostoTotal());
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

    //METODO PARA ACTUALIZAR
    public boolean Actualizar(Recetas recetas) {
        Connection con = getConexion();
        PreparedStatement ps = null;
        String sql = "UPDATE public.recetas nombre=?, litros = ?, costo_total=? WHERE id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, recetas.getNombre());
            ps.setBigDecimal(2, recetas.getLitros());
            ps.setInt(3, recetas.getCostoTotal());
            ps.setInt(4, recetas.getIdRecetas());
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

    //METODO PARA ELIMINAR
    public boolean Eliminar(int idReceta) {
        Connection con = getConexion();
        PreparedStatement ps = null;
        int resultado = 0;
        String sql = "DELETE FROM public.recetas WHERE id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, idReceta);
            resultado = ps.executeUpdate();
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

    //METODO PARA OBTENER TODOS
    public List<Recetas> ObtenerTodos() {
        Connection con = getConexion();
        List<Recetas> listaRecetas = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT id, nombre, litros, costo_total FROM public.recetas";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Recetas recetas = new Recetas();
                recetas.setIdRecetas(rs.getInt("id"));
                recetas.setNombre(rs.getString("nombre"));
                recetas.setLitros(rs.getBigDecimal("litros"));
                recetas.setCostoTotal(rs.getInt("costo_total"));
                listaRecetas.add(recetas);
            }
            return listaRecetas;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    public boolean existeRecetaPorEstilo(String estilo) {
        Connection con = getConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) FROM recetas WHERE nombre = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, estilo);
            rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }

        return false;
    }

    public List<String> obtenerEstilosReceta() {
        Connection con = getConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT nombre FROM recetas";

        List<String> estilosReceta = new ArrayList<>();

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String estilo = rs.getString("nombre");
                estilosReceta.add(estilo);
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }

        return estilosReceta;
    }

    public Recetas obtenerRecetaPorEstilo(String estilo) {
        Connection con = getConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM recetas WHERE nombre = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, estilo);
            rs = ps.executeQuery();

            if (rs.next()) {
                Recetas receta = new Recetas();
                receta.setIdRecetas(rs.getInt("id"));
                receta.setNombre(rs.getString("nombre"));
                receta.setLitros(rs.getBigDecimal("litros"));
                receta.setCostoTotal(rs.getInt("costo_total"));
                return receta;
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }

        return null;
    }

}
