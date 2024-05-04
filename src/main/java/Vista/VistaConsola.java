/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 *
 * @author rdsbl
 */
public class VistaConsola {
    
    public void mensajeConsola(String texto){
        System.out.println(texto);
    }
    
    public void mensajeConsola(String texto, String error){
        System.out.println(texto+" "+error);
    }
    
    public void mensajeMetadatos(DatabaseMetaData dbmd){
        try{
        mensajeConsola(dbmd.getDatabaseProductName());//Nombre de la base de datos (del SGBD).
        mensajeConsola(dbmd.getDatabaseProductVersion());//Versión de la BD.
        mensajeConsola(dbmd.getURL());//URL de la conexión.
        mensajeConsola(dbmd.getDriverName());//Nombre del driver.
        mensajeConsola(dbmd.getDriverVersion());//Versión del driver.
        mensajeConsola(dbmd.getUserName());//Nombre del usuario conectado.
        mensajeConsola(dbmd.getSQLKeywords());//Palabras SQL no recogidas en el SQL estándar.
        } catch(SQLException e) {
            mensajeConsola("Se produjo una excepción :(");
        }
    }
}
