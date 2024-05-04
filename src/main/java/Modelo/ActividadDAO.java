/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/** Clase que maneja las actividades
 *
 * @author David Prieto Araujo
 */
public class ActividadDAO {
    private Session sesion;

    public ActividadDAO() {
        sesion = null;
    }

    public ActividadDAO(Session sesion) {
        this.sesion = sesion;
    }

    public void setSesion(Session sesion) {
        this.sesion = sesion;
    }
    
    /** Devuelve la lista de actividades creadas en la base de datos
     * 
     * @return
     * @throws SQLException 
     */
    public ArrayList<Actividad> actividades() throws SQLException {
        Transaction transaction = sesion.beginTransaction();//comienzo de la transaccion
        //consulta HQL: El segundo parámetro indica que estamos mapeando completamente la tabla Actividad en la clase Actividad.
        //El resultado de las consultas se recupera con el método list()
        Query consulta = sesion.createQuery ("SELECT a FROM Actividad a ORDER BY IDACTIVIDAD", Actividad.class);
        ArrayList<Actividad> actividades = (ArrayList<Actividad>) consulta.list();
        transaction.commit();//confirmacion de la transaccion
        return actividades;
    }
    
    /** Busca y devuelve un id de actividad en la base de datos pasandole un nombre por parametro
     * 
     * @param nombre
     * @return
     * @throws SQLException 
     */
    public String buscaId(String nombre) throws SQLException {
        String idActividad = null;
        int i =0;
        boolean encontrado = false;
        ArrayList<Actividad> lista = actividades();
        while (!encontrado && i < lista.size()) {
            if (lista.get(i).getNombre().equals(nombre)) {
                idActividad = lista.get(i).getIdactividad();
                encontrado = true;
            }
            i++;
        }
        return idActividad;
    }
    
    /** Muestra el nombre y correo de los socios de una determinada actividad
     * pasada por parametro
     * @param idActividad
     * @return 
     * @throws SQLException 
     */
    public ArrayList<String[]> SociosxActividad(String idActividad) throws SQLException {
        ArrayList<String[]> resultado = new ArrayList<>();
        if (Controlador.ControladorLogin.esOracleStatic()) {
            Transaction transaction = sesion.beginTransaction();
            StoredProcedureQuery llamada = sesion.createStoredProcedureCall("SOCIOSXACTIVIDAD")
            .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, Class.class, ParameterMode.REF_CURSOR)
            .setParameter(1, idActividad);
            llamada.execute();
            resultado = (ArrayList<String[]>) llamada.getResultList();
            transaction.commit();
        } else if(!Controlador.ControladorLogin.esOracleStatic()) {
            Transaction transaction = sesion.beginTransaction();
            StoredProcedureQuery llamada = sesion.createStoredProcedureCall("SOCIOSXACTIVIDAD")
            .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
            .setParameter(1, idActividad);
            llamada.execute();
            resultado = (ArrayList<String[]>) llamada.getResultList();
            transaction.commit();   
        }
        return resultado;
    }
    
    /** Da
     * 
     * @param numSocio 
     */
    /*public void darAlta(String numSocio) {
        Actividad act = actividades.get(numSocio);
        Transaction t = sesion.beginTransaction();
        System.out.println(socio.getNombre());
        act.addSocio(socio);
        sesion.save(act);
        t.commit();
    }*/
    
    public void darBaja(String numSocio) {
        Transaction transaction = sesion.beginTransaction();
        Socio socio = sesion.get(Socio.class, numSocio);
        sesion.delete(socio);
        transaction.commit();
    }
}
