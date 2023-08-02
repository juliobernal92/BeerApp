package Dao;

import Entidades.Insumos;
import Utilidades.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    
}
