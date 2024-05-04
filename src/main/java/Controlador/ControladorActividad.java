/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.*;
import Vista.*;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import org.hibernate.Session;

/** Esta clase controla el jdialog de actividad
 *
 * @author David Prieto Araujo
 */
public class ControladorActividad implements ActionListener{
    private VistaMensaje vmensaje;
    private VDialogActividad vdActividad;
    private TablaActividad atabla;
    private ActividadDAO adao;
    private Conexion conexion;
    private Session sesion;

    /** Constructor de la clase
     * Inicializa los atributos, añade los listeners y pone el jdialog en modal 
     * @param sesion
     */
    public ControladorActividad(Session sesion) {
        vmensaje = new VistaMensaje();
        vdActividad = new VDialogActividad();
        atabla = new TablaActividad();
        adao = new ActividadDAO(sesion);
        
        this.sesion = sesion;        
        
        addListeners();        
        // dibujamos la tabla vacia de sociosxactividad:
        atabla.InicializarTabla(vdActividad);
        atabla.DibujarTablaSociosxActividad(vdActividad);
        vdActividad.jButtonSociosInscritos.setEnabled(false);//desactiva la opcion de pulsar este jbutton
        vdActividad.setLocationRelativeTo(null);//centramos el jdialog en la pantalla
        vdActividad.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);//se pone en modo modal, 
        //toma el foco de la aplicacion y no se puede hacer nada hasta que cierre esta ventana
        //vdActividad.setVisible(true);//mostramos el jdialog
    }
    
    /** Muestra el jdialog de sociosxactividad
     * 
     */
    public void cargarJDialogSociosxActividad() {
        vdActividad.setVisible(true);
    }
    
    
    /** Añade los listeners del jdialog
     * 
     */
    private void addListeners() {
        vdActividad.jButtonSalir.addActionListener(this);
        vdActividad.jButtonVaciarTabla.addActionListener(this);
        vdActividad.jButtonSociosInscritos.addActionListener(this);
        vdActividad.jTableActividad.addMouseListener( new MouseAdapter() {//asignacion directa de eventos:
            @Override
            public void mouseClicked(MouseEvent evt) {
                SocioxActividadJTableMouseClicked(evt);
            }
            private void SocioxActividadJTableMouseClicked(MouseEvent evt) {
                int fila = vdActividad.jTableActividad.getSelectedRow();
            } 
        });
        vdActividad.jComboBoxActividades.addActionListener(this);        
    }
    
    /** Controla los eventos del jdialog de sociosxactividad
     * 
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        activarSociosInscritos();
        switch (e.getActionCommand()) {
            case "Salir" -> {
                vdActividad.dispose();
                vdActividad.setVisible(false);
                break;
            }
            case "Socios Inscritos" -> {
                String nombre_actividad = (String) vdActividad.jComboBoxActividades.getSelectedItem();
                if (nombre_actividad != null) {
                    if(atabla.tablaVacia()) {//si la tabla esta vacia mostrara los datos
                        try {
                            adao.setSesion(sesion);
                            String id = adao.buscaId(nombre_actividad);
                            if(id != null) {
                                ArrayList<String[]> lista = adao.SociosxActividad(id);
                                atabla.rellenarTablaSociosxActividad(lista);
                            } else vmensaje.Mensaje(vdActividad, "error", "Actualmente no existe niguna actividad con ese id");
                        } catch (SQLException ex) {
                            vmensaje.Mensaje(vdActividad, "error", "Se produjo un error al carga la tabla:\n"
                            + ex.getMessage());
                        }
                    }
                    else
                        vmensaje.Mensaje(vdActividad, "error", "Tienes que vaciar primero la tabla");
                }                
                break;
            }
            case "Vaciar tabla" -> {
                int opcion = vmensaje.MensajeConfirmacion(vdActividad, "¿Seguro que quieres vaciar la tabla?");
                if (opcion == 0) {
                    atabla.vaciarTablaSocioxActividad();
                    vmensaje.Mensaje(vdActividad, "info", "Se ha vaciado correctamente la tabla");
                }
                break;
            }
        }
    }
    
    /** Activa el jbutton socios inscritos si se selecciona una opcion del jcombobox
     * 
     */
    private void activarSociosInscritos() {
        String actividad = (String)vdActividad.jComboBoxActividades.getSelectedItem();
        if (actividad !=null) 
            vdActividad.jButtonSociosInscritos.setEnabled(true);   
    }    
}
