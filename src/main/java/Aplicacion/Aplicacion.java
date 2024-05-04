/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Aplicacion;

import Controlador.ControladorLogin;
import Vista.VistaMensaje;
import com.formdev.flatlaf.FlatDarculaLaf;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.util.Calendar;

/** Clase main principal, es la que se ejecuta para iniciar el acceso a la aplicacion
 *
 * @author David Prieto Araujo
 */
public class Aplicacion {
    public static void main(String[] args) {
        LightMode();
        init();
    }
    
    /** Inicia la aplicacion al acceso de inicio de usuario.
     * 
     */
    @SuppressWarnings("unchecked")
    private static void init() {
        new ControladorLogin();
    }
    
    /** Modo nocturno/diurno
     * Cambia el diseño de la interfaz de la aplicacion dependiendo de la hora 
     * actual.
     * Modo diurno: 08:00-20:00
     * Modo nocturno: 20:01-07:59 
     */
    private static void LightMode() {
        VistaMensaje vmensaje = new VistaMensaje();
        Calendar calendario = Calendar.getInstance();
        int hora = calendario.get(Calendar.HOUR_OF_DAY);//obtiene la hora en formtao 24h
        //System.out.println(hora);
        try {//modifica el aspecto de la interfaz; bordes, fondo, colores...
            if(hora>8 || hora<20)
                UIManager.setLookAndFeel( new NimbusLookAndFeel());
            if(hora<8 || hora>20)
                UIManager.setLookAndFeel( new FlatDarculaLaf());
        } catch(UnsupportedLookAndFeelException ex) {
            System.err.println( "Mensaje de error main UIMAnager:\n" + ex.getMessage());
            vmensaje.Mensaje("error", "Se ha producido un error con UIManager.setLookAndFell:\n"
                    + ex.getMessage(), 0);
        }        
    }    
}
