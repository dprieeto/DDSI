/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Vista.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import static java.sql.DriverManager.getConnection;
import java.sql.SQLException;

/** Esta clase controla la conexion y desconexion a la base de datos
 *
 * @author David Prieto Araujo
 */
public class Conexion {
    private Connection conexion;//bd 27
    private VistaConsola vistac;
    private VistaMensaje vMensaje = null;
    private String cadena,sgbd,ip,servicio_bd,usuario,password;
    private boolean oracle;//para actividaddao con procedures
    
    /** Constructor de la clase. Realiza la conexion a la base de datos
     * 
     * @param sgbd
     * @param ip
     * @param servicio_bd
     * @param usuario
     * @param password
     * @throws SQLException 
     */
    public Conexion (String sgbd, String ip, String servicio_bd, String usuario,String password)throws SQLException{
        vistac = new VistaConsola();
        vMensaje = new VistaMensaje();
        if (sgbd == null || servicio_bd == null || ip == null || usuario == null || password == null)
            vMensaje.Mensaje("error", "Error al conectarse, algun campo de los datos esta vacio", 0);
        this.sgbd = sgbd;
        this.servicio_bd = servicio_bd;
        this.ip = ip;
        this.usuario = usuario;
        this.password = password;
        if (sgbd.equals("oracle")) {//Si es Oracle.
            oracle = true;
            cadena = "jdbc:oracle:thin:@" + ip + ":" + servicio_bd + ":etsi";
        } else {//Si no es Oracle (si es mariaDB). Tenemos controlado todo esto fuera de este método. Ahora mismo está implementado todo de manera que si me invento el SGBD, me conectará con mariaDB. Lo suyo es tener controlado esto, pero como no lo exigen en la práctica 1 no lo haré todavía.
            oracle = false;
            cadena = "jdbc:mariadb://" + ip + ":" + servicio_bd + "/" + usuario;
        }        
        conexion = getConnection(cadena, usuario, password);
        vistac.mensajeConsola("Se ha conectado exitosamente a la BD :)");
        //vMensaje.Mensaje("info","Se ha conectado correctamente al servidor", 1);
    }
    
    /** Realiza la desconexion a la base de datos
     * 
     * @throws SQLException 
     */
    public void desconexion() throws SQLException{
        try {
            String s = informacionBD().getDatabaseProductName();
            conexion.close();
            vistac.mensajeConsola("Se ha desconectado exitosamente de la BD :)");
            vMensaje.Mensaje("info","Se ha desconectado correctamente del servidor " 
                    + s, 1);
         }
         catch (SQLException e) {
             vistac.mensajeConsola("Se produjo una excepción :(");
             vMensaje.Mensaje("error","Error al desconectarse de la BD:\n"
                                + e.getMessage(), 0);
         } 
    }
    
    /** Devuelve metadata
     * 
     * @return
     * @throws SQLException 
     */
    public DatabaseMetaData informacionBD() throws SQLException{//El control de excepciones aquí lo he puesto a la derecha de declararse la función porque no sé qué retornar en caso de que salte excepción, entonces si pongo esto me ahorro decidirlo.
            return conexion.getMetaData();
    }
    
    /** Devuelve la conexion
     * 
     * @return 
     */
    public Connection getConexion() {
        return this.conexion;
    }
    
    /** Devuelve True si se esta utilizando oracle, false si es mariadb
     * 
     * @return 
     */
    public boolean esOracle() {
        return oracle;
    }
}
