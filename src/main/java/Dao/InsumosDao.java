package Dao;

import Entidades.Insumos;
import Utilidades.Conexion;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InsumosDao extends Conexion{
    //METODO PARA INSERTAR
    public boolean Insertar(Insumos insumos){
        Connection con = getConexion();
        PreparedStatement ps = null;
        String sql = "INSERT INTO insumos(nombre, tipoinsumo, cantidad, costo) VALUES (?, ?, ?, ?)";
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, insumos.getNombre());
            ps.setString(2, insumos.getTipoInsumo());
            ps.setBigDecimal(3, insumos.getCantidad());
            ps.setInt(4,insumos.getCosto());
            ps.execute();
            return true;
            
        }catch (SQLException e) {
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
    public boolean Actualizar (Insumos insumos){
        Connection con = getConexion();
        PreparedStatement ps = null;
        String sql ="UPDATE insumos SET nombre=?, tipoinsumo=?, cantidad=?, costo=? WHERE id=?";
        try{
            ps=con.prepareStatement(sql);
            ps.setString(1,insumos.getNombre());
            ps.setString(2,insumos.getTipoInsumo());
            ps.setBigDecimal(3,insumos.getCantidad());
            ps.setInt(4,insumos.getCosto());
            ps.setInt(5,insumos.getIdInsumos());
            ps.execute();
            return true;
            
        }catch(SQLException e){
            System.err.println(e);
            return false;
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.err.println(e);
            }
        }
    }
    
    //METODO PARA ELIMINAR
    public boolean Eliminar (int idInsumos){
        Connection con = getConexion();
        PreparedStatement ps=null;
        int resultado=0;
        String sql = "DELETE FROM insumos WHERE id=?";
        try{
            ps=con.prepareStatement(sql);
            ps.setInt(1, idInsumos);
            resultado = ps.executeUpdate();
            return resultado >0;
            
        }catch(SQLException e){
            System.err.println(e);
            return false;
        }finally{
            try{
                con.close();
            }catch(SQLException e){
                System.err.println(e);
            }
        }
    }
    
    //METODO PARA OBTENER TODOS
    public List<Insumos> ObtenerTodos(){
        Connection con = getConexion();
        List<Insumos> listaInsumos = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT id, nombre, tipoinsumo, cantidad, costo FROM insumos";
        try{
            ps=con.prepareStatement(sql);
            rs= ps.executeQuery();
            while(rs.next()){
                Insumos insumos = new Insumos();
                insumos.setIdInsumos(rs.getInt("id"));
                insumos.setNombre(rs.getString("nombre"));
                insumos.setTipoInsumo(rs.getString("tipoinsumo"));
                insumos.setCantidad(rs.getBigDecimal("cantidad"));
                insumos.setCosto(rs.getInt("costo"));
                listaInsumos.add(insumos);
            }
            return listaInsumos;
        }catch(SQLException e){
            System.err.println(e);
            return null;
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
    
    public Insumos obtenerStockInsumoPorId(int idInsumo) {
        Insumos stockInsumo = null;
        Connection con = getConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM insumos WHERE id = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, idInsumo);
            rs = ps.executeQuery();

            if (rs.next()) {
                stockInsumo = new Insumos();
                stockInsumo.setIdInsumos(rs.getInt("id"));
                stockInsumo.setNombre(rs.getString("nombre"));
                stockInsumo.setTipoInsumo(rs.getString("tipoinsumo"));
                stockInsumo.setCantidad(rs.getBigDecimal("cantidad"));
                stockInsumo.setCosto(rs.getInt("costo"));
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
        return stockInsumo;
    }
    
    public int obtenerIdPorNombre(String nombre) {
        Connection con = getConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idInsumo = 0;

        try {
            String sql = "SELECT id FROM insumos WHERE nombre = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();

            if (rs.next()) {
                idInsumo = rs.getInt("id");
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

        return idInsumo;
    }
    
    public boolean restarCantidad(int idInsumo, int cantidadRestar) {
        Connection con = getConexion();
        PreparedStatement ps = null;
        String sql = "UPDATE insumos SET cantidad = cantidad - ? WHERE id = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, cantidadRestar);
            ps.setInt(2, idInsumo);

            int filasActualizadas = ps.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.err.println(e);
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
    
    public List<String> obtenerTiposInsumo() {
        Connection con = getConexion();
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT DISTINCT tipoinsumo FROM insumos";
        List<String> tiposInsumo = new ArrayList<>();

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String tipoInsumo = rs.getString("tipoinsumo");
                tiposInsumo.add(tipoInsumo);
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

        return tiposInsumo;
    }

    public List<String> obtenerNombresInsumoPorTipo(String tipoInsumo) {
        Connection con = getConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT nombre FROM insumos WHERE tipoinsumo = ?";
        List<String> nombresInsumo = new ArrayList<>();

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, tipoInsumo);
            rs = ps.executeQuery();

            while (rs.next()) {
                String nombreInsumo = rs.getString("nombre");
                nombresInsumo.add(nombreInsumo);
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

        return nombresInsumo;
    }
    
    public int obtenerCostoPorId(int id) {
        Connection con = getConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT costo FROM insumos WHERE id = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("costo");
            } else {
                return -1; // Valor de retorno indicando que el insumo no fue encontrado
            }
        } catch (SQLException e) {
            System.err.println(e);
            return -1; // Valor de retorno indicando un error
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
    
    
    public int obtenerCostoPorNombre(String nombre) {
    Connection con = getConexion();
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "SELECT costo FROM insumos WHERE nombre = ?";
    
    try {
        ps = con.prepareStatement(sql);
        ps.setString(1, nombre);
        rs = ps.executeQuery();
        
        if (rs.next()) {
            return rs.getInt("costo");
        } else {
            return -1; // Valor de retorno indicando que el insumo no fue encontrado
        }
    } catch (SQLException e) {
        System.err.println(e);
        return -1; // Valor de retorno indicando un error
    } finally {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}


}
