/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.*;
import Vista.*;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.JPanel;
import org.hibernate.Session;

/** Esta clase controla el jframe principal y los jpanels
 *
 * @author David Prieto Araujo
 */
public class ControladorPrincipal implements ActionListener {
    private Conexion conexion = null;
    private VistaPrincipal vprincipal = null;
    private VistaMensaje vmensaje = null;
    private VPanelMonitores vpmonitor = null;
    private VPanelSocios vpsocios = null;
    private VPanelPrincipal vpPrincipal = null;
    private Session sesion = null;
    
    /** Constructor de la clase, inicializa los atributos, añade los listeners,
     * los jpanels al jframe y modifica el jframe
     * @param conexion 
     */
    public ControladorPrincipal(Conexion conexion){
        vprincipal = new VistaPrincipal();
        vmensaje = new VistaMensaje();
        vpmonitor = new VPanelMonitores();
        vpsocios = new VPanelSocios();
        vpPrincipal = new VPanelPrincipal();
        this.conexion=conexion;
        addListeners();//añade los listeners        
        //vprincipal.setLayout(null);// null para poder trabajar con coordenadas dentro del frame
        vprincipal.setLocationRelativeTo(null);//situa la ventana en el centro de la pantalla
        vprincipal.setVisible(true);//muestra la ventana
        //vprincipal.setPreferredSize(new Dimension(970,400));
        vprincipal.getContentPane().setLayout(new CardLayout());//Layout CardLayout para poder tener 
                                                                //mas de un panel en la misma posicion
        vprincipal.add(vpmonitor);// añade el jpanel a la vista del frame
        vprincipal.add(vpsocios);
        vprincipal.add(vpPrincipal);
        //vprincipal.pack();
        this.muestraPanel(vpPrincipal);//para que muestre un jpanel vacio al abrirlo
    }
    
    /** Constructor de la clase para hibernate
     * 
     * @param sesion 
     */
    ControladorPrincipal(Session sesion) {
        vprincipal = new VistaPrincipal();
        vmensaje = new VistaMensaje();
        vpmonitor = new VPanelMonitores();
        vpsocios = new VPanelSocios();
        vpPrincipal = new VPanelPrincipal();
        this.sesion=sesion;
        addListeners();//añade los listeners
        //vprincipal.setLayout(null);// null para poder trabajar con coordenadas dentro del frame
        vprincipal.setLocationRelativeTo(null);//situa la ventana en el centro de la pantalla
        vprincipal.setVisible(true);//muestra la ventana
        vprincipal.getContentPane().setLayout(new CardLayout());//Layout CardLayout para poder tener 
                                                                //mas de un panel en la misma posicion
        vprincipal.add(vpmonitor);// añade el jpanel a la vista del frame
        vprincipal.add(vpsocios);
        vprincipal.add(vpPrincipal);
        this.muestraPanel(vpPrincipal);//para que muestre un jpanel vacio al abrirlo
    }
    
    /** Añade los listeners a controlar
     * 
     */
    private void addListeners(){//se asignan los listeners a los componenetes que lanzan eventos
        //listener de vistaprincipal:
        vprincipal.jMenuSalirAplicacion.addActionListener(this);
        vprincipal.jMenuGestionMonitores.addActionListener(this);
        vprincipal.jMenuGestionSocios.addActionListener(this);
        vprincipal.jMenuItemSociosxActividad.addActionListener(this);
        vprincipal.jMenuItemCuota.addActionListener(this);
        //listener de vpanelmonitores:
        vpmonitor.jButtonNuevoMonitor.addActionListener(this);
        vpmonitor.jButtonBajaMonitor.addActionListener(this);
        vpmonitor.jButtonActualizacionMonitor.addActionListener(this);
        vpmonitor.jTableMonitor.addMouseListener( new MouseAdapter() {//asignacion directa de eventos:
            @Override
            public void mouseClicked(MouseEvent evt) {
                MonitorJTableMouseClicked(evt);
            }
            private void MonitorJTableMouseClicked(MouseEvent evt) {
                int fila = vpmonitor.jTableMonitor.getSelectedRow();
            }            
        });
        //listener del vpanelsocios:
        vpsocios.jButtonNuevoSocio.addActionListener(this);
        vpsocios.jButtonBajaSocio.addActionListener(this);
        vpsocios.jButtonActualizacionSocio.addActionListener(this);
        vpsocios.jButtonCargarTodos.addActionListener(this);
        vpsocios.jButtonVaciarTabla.addActionListener(this);
        vpsocios.jButtonBuscar.addActionListener(this);
        vpsocios.jTableSocio.addMouseListener( new MouseAdapter() {//asignacion directa de eventos:
            @Override
            public void mouseClicked(MouseEvent evt) {
                SocioJTableMouseClicked(evt);
            }
            private void SocioJTableMouseClicked(MouseEvent evt) {
                int fila = vpsocios.jTableSocio.getSelectedRow();
            } 
        });
        //listeners de actividad:
        
    }
    
    /** Se pasa por parametro un jpanel y se muestra visible
     * 
     * @param panel 
     */
    private void muestraPanel(JPanel panel) {
        vpPrincipal.setVisible(false);
        vpmonitor.setVisible(false);
        vpsocios.setVisible(false);
        panel.setVisible(true);//muestra el panel
        panel.updateUI();//actualiza el look and feel del jpanel
    }
    
    /** Controla los distintos eventos producidos al clickar en jbuttons,etc. 
     * 
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {//PASAR LISTENER AL PRINCIPAL
        switch(e.getActionCommand()) {
            case "Salir de la aplicacion" -> { //jmenu vistaprincipal
                vprincipal.dispose();//limpia los objetos; el SO destruye y limpia el jframe
                try {
                    //conexion.desconexion();
                    sesion.close();
                    VistaMensaje.StaticMensaje(null, "info", "Se ha desconectado de la base de datos");
                } catch (Exception ex) {
                    vmensaje.Mensaje("error","Se ha producido un error al desconectarse de la base de datos:\n"
                                    + ex.getMessage(), 0);
                }
                System.exit(0);
                break;
            }
            case "Gestion de monitores" -> {//jmenu item vistaprincpal
                this.muestraPanel(vpmonitor);   
                ControladorMonitores cm = new ControladorMonitores(sesion);
                try {
                    cm.cargarTablaMonitor(vpmonitor);                    
                } catch (SQLException ex) {
                    vmensaje.Mensaje(vpmonitor, "error", "Se ha producido un error al cargar la base de datos de monitores:\n"
                    + ex.getMessage());
                }
                break;
            }
            case "Gestion de socios" -> { //jmenu item vistaprincipal
                this.muestraPanel(vpsocios);
                ControladorSocios cs = new ControladorSocios(this.sesion);
                try {
                    cs.cargarTablaSocios(vpsocios);                    
                } catch (SQLException ex) {
                    vmensaje.Mensaje(vpmonitor, "error", "Se ha producido un error al cargar la base de datos de monitores:\n"
                    + ex.getMessage());
                }
                break;
            }
            case "Cuota" -> { // EXAMEN MODIFICACION ENERO:
                ControladorMonitores cm = new ControladorMonitores(sesion);
                cm.verCuota();
                break;
            }
            case "Socios por Actividad" -> { //jmenu item vistaprincipal
                ControladorActividad ca = new ControladorActividad(sesion);
                ca.cargarJDialogSociosxActividad();//no funciona
                break;
            }
            case "Nuevo monitor" -> {//jbutton vpanelmonitores
                ControladorMonitores cm = new ControladorMonitores(sesion);
                try {
                    cm.nuevoMonitor();
                    cm.cargarTablaMonitor(vpmonitor);
                } catch (SQLException ex) {
                    vmensaje.Mensaje(vpmonitor, "error", "Se ha producido un error al insertar el nuevo monitor:\n"
                    + ex.getMessage());
                }
                muestraPanel(vpmonitor);
                break;
            }
            case "Baja de monitor" -> {//jbutton vpanelmonitores
                int fila = vpmonitor.jTableMonitor.getSelectedRow();//si no hay ninguna seleccionada es -1
                if (fila != -1) {
                    try {
                        String codigo = (String) this.vpmonitor.jTableMonitor.getValueAt(fila, 0);//obtiene el numSocio de la fila seleccionada
                        ControladorMonitores cm = new ControladorMonitores(sesion);
                        cm.BajaMonitor(codigo);
                        cm.cargarTablaMonitor(vpmonitor);
                    } catch (SQLException ex) {
                        vmensaje.Mensaje(vpmonitor, "error", "Error al dar de baja:\n"
                        + ex.getMessage());
                    }
                }
                else vmensaje.Mensaje(vpmonitor, "error", "Tienes que seleccionar una fila para dar de baja a un monitor.");
                muestraPanel(vpmonitor);
                break;
            }
            case "Actualizacion de monitor" -> {//jbutton vpanelmonitores
                    int fila = vpmonitor.jTableMonitor.getSelectedRow();//si no hay ninguna seleccionada es -1
                    ControladorMonitores cm = new ControladorMonitores(sesion);
                    if (fila != -1) {
                        try {
                            String codigo = (String) this.vpmonitor.jTableMonitor.getValueAt(fila, 0);//obtiene el numSocio de la fila seleccionada        
                            cm.actualizarMonitor(codigo);
                            cm.cargarTablaMonitor(vpmonitor);
                        } catch (SQLException ex) {
                            vmensaje.Mensaje(vpmonitor, "error", "Error al actualizar el monitor :\n"
                            + ex.getMessage());
                        }
                    }
                    else vmensaje.Mensaje(vpmonitor, "error", "Tienes que seleccionar una fila para actualizar a un monitor.");
                    muestraPanel(vpmonitor);
                    break;
            }
            case "Nuevo socio" -> { //jbutton vpanelsocios
                ControladorSocios cs = new ControladorSocios(sesion);
                try {
                    cs.nuevoSocio();
                    cs.cargarTablaSocios(vpsocios);
                } catch (SQLException ex) {
                    vmensaje.Mensaje(vpsocios, "error", "Se ha producido un error al insertar el nuevo socio:\n"
                    + ex.getMessage());
                }
                cs.terminar();
                muestraPanel(vpsocios);
                break;
            }
            case "Baja de socio" -> { //jbutton vpanelsocios
                int fila = vpsocios.jTableSocio.getSelectedRow();//si no hay niguna seleccionada es -1
                if (fila != -1) {
                    try {
                        String numSocio = (String) this.vpsocios.jTableSocio.getValueAt(fila, 0);//obtiene el numSocio de la fila seleccionada
                        ControladorSocios cs = new ControladorSocios(sesion);
                        cs.bajaSocio(numSocio);
                        cs.cargarTablaSocios(vpsocios);
                    } catch (SQLException ex) {
                        vmensaje.Mensaje(vpsocios, "error", "Error al dar de baja:\n"
                        + ex.getMessage());
                    }
                }
                else vmensaje.Mensaje(vpsocios, "error", "Tienes que seleccionar una fila para dar de baja a un socio.");
                muestraPanel(vpsocios);
                break;
            }
            case "Actualizacion de socio" -> { //jbutton vpanelsocios
                    int fila = vpsocios.jTableSocio.getSelectedRow();//si no hay niguna seleccionada es -1
                    ControladorSocios cs = new ControladorSocios(sesion);
                    if (fila != -1) {
                        try {
                            String numSocio = (String) this.vpsocios.jTableSocio.getValueAt(fila, 0);//obtiene el numSocio de la fila seleccionada
                            cs.actualizarSocio(numSocio);
                            cs.cargarTablaSocios(vpsocios);
                        } catch (SQLException ex) {
                            vmensaje.Mensaje(vpsocios, "error", "Error al actualizar el socio:\n"
                            + ex.getMessage());
                        }
                    }
                    else vmensaje.Mensaje(vpsocios, "error", "Tienes que seleccionar una fila para actualizar el socio.");
                    cs.terminar();
                    muestraPanel(vpsocios);
                    break;
            }
            case "Cargar todos los socios" -> {
                ControladorSocios cs = new ControladorSocios(this.sesion);
                try {
                    cs.cargarTablaSocios(vpsocios);                    
                } catch (SQLException ex) {
                    vmensaje.Mensaje(vpmonitor, "error", "Se ha producido un error al cargar la base de datos de monitores:\n"
                    + ex.getMessage());
                }
                break;
            }
            case "Vaciar tabla" -> {
                ControladorSocios cs = new ControladorSocios(sesion);
                cs.vaciarTabla(vpsocios);
                break;
            }
            case "Buscar socio" -> {
                ControladorSocios cs = new ControladorSocios(sesion); 
                cs.setVsocios(vpsocios);
                cs.buscarSocio();
                break;
            }
        }
    }
}