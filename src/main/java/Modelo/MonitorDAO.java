/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/** Esta clase tiene funciones para el manejo de los monitores
 *
 * @author David Prieto Araujo
 */
public class MonitorDAO {
    private Conexion conexion;
    private PreparedStatement ps;
    private Session sesion;
    
    public MonitorDAO() {
        conexion = null;
        ps = null;
        sesion = null;
    }
    public MonitorDAO(Conexion conexion) {
        this.conexion = conexion;
    }

    public MonitorDAO(Session sesion) {
        this.sesion = sesion;
    }
        
    public void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }
    
    public void setSesion(Session sesion) {
        this.sesion = sesion;
    }
    
    /** Devuelve una lista con los Monitores de la base de datos
     * @return listaMonitores
     * @throws SQLException 
     */
    public ArrayList<Monitor> listaMonitores() throws SQLException {
        Transaction transaction = sesion.beginTransaction();//comienzo de la transaccion
        //consulta HQL: El segundo parámetro indica que estamos mapeando completamente la tabla MONITOR en la clase Monitor.
        //El resultado de las consultas se recupera con el método list()
        Query consulta = sesion.createQuery ("SELECT m FROM Monitor m ORDER BY CODMONITOR", Monitor.class);
        ArrayList<Monitor> monitores = (ArrayList<Monitor>) consulta.list();
        transaction.commit();//confirmacion de la transaccion
        return monitores;
    }
    
    /** Devuelve una lista con los monitores con los nombres que empiecen
     * una letra.
     * @param letra
     * @return lista
     * @throws SQLException 
     */
    public ArrayList<Monitor> listaMonitorPorLetra(String letra) throws SQLException {        
        Transaction transaction = sesion.beginTransaction();
        String c = String.format("SELECT * FROM MONITOR WHERE nombre LIKE %s  ORDER BY CODMONITOR", letra);
        Query consulta = sesion.createQuery (c, Monitor.class);
        ArrayList<Monitor> monitores = (ArrayList<Monitor>) consulta.list();
        transaction.commit();
        return monitores;
    }
    
    /** Añade un monitor a la base de datos previamente creado
     * 
     * @param monitor
     * @throws SQLException 
     */
    public void InsertarMonitor(Monitor monitor) throws SQLException {
        Transaction t = sesion.beginTransaction();
        sesion.save(monitor);
        t.commit();
    }    
    
    /** Dado un codigo, elimina el monitor asociado al codigo de la base de datos
     * 
     * @param codigo
     * @throws SQLException 
     */
    public void eliminarMonitor(String codigo) throws SQLException {
        Transaction transaction = sesion.beginTransaction();
        Monitor monitor = sesion.get(Monitor.class, codigo);
        sesion.delete(monitor);
        transaction.commit();
    }
    
    /** Dado un monitor, acutualiza sus datos en la base de datos
     * 
     * @param monitor
     * @throws SQLException 
     */
    public void actualizarMonitor(Monitor monitor) throws SQLException {
        Transaction transaction = sesion.beginTransaction();
        sesion.save(monitor);
        transaction.commit();
    }   
    
    /** Busca un monitor de la base de datos con el codigo pasado por parametro 
     *  y devuelve el monitor asociado
     * @param codigo
     * @return monitor
     * @throws SQLException 
     */
    public Monitor buscaMonitor(String codigo) throws SQLException {
        ArrayList<Monitor> lista = listaMonitores();
        int i = 0;
        boolean encontrado = false;
        Monitor monitor = null;//new Monitor();
        while(i<lista.size() && !encontrado) {
            if (codigo.equals(lista.get(i).getCodmonitor())) {
                monitor = lista.get(i);
                encontrado=true;
            }
            i++;
        }
        return monitor;
    }

    /** genera y devuelve el siguiente codigo disponible para el nuevo monitor 
     *  
     * @return codigo
     * @throws SQLException
     */
    public String generarCodigoMonitor() throws SQLException {
        ArrayList<Monitor> lista = listaMonitores();
        String codigo= "M001";//empezamos con el primer codigo para buscar el siguiente disponible
        int codNum_lista,num_codigo ;
        num_codigo=1;// los codigos del monitor empiezan en M001
        if (!lista.isEmpty()) {//si la lista no esta vacia
            for(int i=0;i<lista.size();i++) {
                String codM_lista = lista.get(i).getCodmonitor().substring(1);//quitamos la m del string del num_codigo: M001 -> 001
                codNum_lista = Integer.parseInt(codM_lista);//lo pasamos a entero: 001 -> 1
                if(num_codigo == codNum_lista)
                    num_codigo++;
                if (num_codigo<10) {
                    codigo = "M00" + num_codigo;
                } else if (num_codigo < 100 && num_codigo >= 10) {
                    codigo = "M0" + num_codigo;
                } else {
                    codigo = "M" + num_codigo;
                }      
            }             
        } else
            codigo="M001";
        return codigo;
    }   
    
    /** Si la cadena es numerica devuelve True. False en otro caso.
     * 
     * @param telefono
     * @return correcto
     */
    public boolean telefonoCorrecto(String telefono) {
        boolean correcto;
        try {
            Integer.parseInt(telefono);
            correcto = true;
        } catch (NumberFormatException excepcion) {
            correcto = false;
        }
        return correcto;
    }
    
    /** Comprueba si el string pasado por parametro tiene la estructura 
     *  de un correo electronico.
     * Devuelve true si lo valida, false en caso contrario.
     * @param email
     * @return 
     */
    public boolean validarEmail(String email) {
        boolean correcto = false;
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            correcto=true;
        }
        return correcto;
    }
    
    public ArrayList<String[]> verCuota(String codMonitor) { // EXAMEN MODIFICACION ENERO:
        ArrayList<String[]> resultado = new ArrayList<>();
        if (Controlador.ControladorLogin.esOracleStatic()) {//si la conexion es oracle
            Transaction transaction = sesion.beginTransaction();
            StoredProcedureQuery llamada = sesion.createStoredProcedureCall("VERCUOTA")
            .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, Class.class, ParameterMode.REF_CURSOR)
            .setParameter(1, codMonitor);
            llamada.execute();
            resultado = (ArrayList<String[]>) llamada.getResultList();
            transaction.commit();
        } else if(!Controlador.ControladorLogin.esOracleStatic()) {// conexion es mariadb
            Transaction transaction = sesion.beginTransaction();
            StoredProcedureQuery llamada = sesion.createStoredProcedureCall("VERCUOTA")
            .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
            .setParameter(1, codMonitor);
            llamada.execute();
            resultado = (ArrayList<String[]>) llamada.getResultList();
            transaction.commit();   
        }
        return resultado;
    }
    
    
}
