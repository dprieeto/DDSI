/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.*;
import Modelo.*;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.Session;

/** Esta clase controla el jpanel de monitores
 *
 * @author David Prieto Araujo
 */
public class ControladorMonitores implements ActionListener{
    private VPanelMonitores vmonitor = null;
    private VDialogNuevoMonitor vdNMonitor = null;
    private VDialogCuota vdCuota = null;
    private VistaMensaje vmensaje = null;
    private TablaMonitor mtabla = null;
    private MonitorDAO mdao = null;
    private Session sesion;
    //private Conexion conexion = null;
    private String codigoMonitor = null; //codigo de monitor a crear o actualizar
    private boolean actualizar; // true-> actualiza, false -> inserta uno nuevo
    
    /** Constructor de la clase
     *  Inicializa los atributos, añade los listeners y pone el jdialog en modal
     * @param sesion
     */
    public ControladorMonitores(Session sesion/*Conexion conexion*/) {
        this.sesion = sesion;
        vmonitor = new VPanelMonitores();
        vdNMonitor = new VDialogNuevoMonitor();
        vdCuota = new VDialogCuota();
        vmensaje = new VistaMensaje();
        mdao = new MonitorDAO(sesion);
        mtabla = new TablaMonitor();
        //this.conexion = conexion;
        addListeners();    
        vdNMonitor.setLocationRelativeTo(null);//centramos el jdialog en la pantalla
        vdNMonitor.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);//se pone en modo modal, 
        //toma el foco de la aplicacion y no se puede hacer nada hasta que cierre esta ventana
        
        //incializamos la tabla de cuota aqui, // EXAMEN MODIFICACION ENERO:
        vdCuota.setLocationRelativeTo(null);//centramos el jdialog en la pantalla
        vdCuota.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);//se pone en modo modal, 
        //toma el foco de la aplicacion y no se puede hacer nada hasta que cierre esta ventana
        mtabla.InicializarTablaCuota(vdCuota);
        mtabla.DibujarTablaCuota(vdCuota);
        
    }    
    
    /** Obtiene los monitores de la base de datos y rellena la tabla
     * 
     * @throws SQLException 
     */
    private void pideMonitores() throws SQLException {
        ArrayList<Monitor> lista = mdao.listaMonitores();
        mtabla.vaciarTablaMonitores();
        mtabla.rellenarTablaMonitores(lista); 
    }
    
    /** Carga en el jtable todos los monitores de la base de datos
     *  Tambien se puede utilizar para actualizar despues de insertar, eliminar o actualizar
     * @param vpmonitor
     * @throws SQLException 
     */
    public void cargarTablaMonitor(VPanelMonitores vpmonitor) throws SQLException {
                vmonitor = vpmonitor;
                mtabla.InicializarTabla(vmonitor);
                //mtabla.scrolltable(vmonitor);
                mtabla.DibujarTablaMonitores(vmonitor);
                pideMonitores();
    }
    
    /** Abre un jdialog para crear un nuevo monitor
     * 
     * @throws SQLException 
     */
    public void nuevoMonitor() throws SQLException {       
        actualizar = false; //vamos crear un monitor no a actualizarlo
        vdNMonitor.setTitle("Nuevo Monitor");//le ponemos titulo a la ventana del jdialog
        vdNMonitor.jButtonInsertar.setText("Insertar");//le asignamos nombre que queremos que aparezca al jbutton
        //asi utilizamos el mismo jdialog pàra insertar y actualizar
        codigoMonitor = mdao.generarCodigoMonitor();//generamos el siguiente codigoMonitor disponible
        vdNMonitor.jTextFieldCodigo.setText(codigoMonitor);//le asignamos el codigoMonitor al jtextfield del codigoMonitor
        vdNMonitor.setVisible(true);//se muestra el jdialog
    }
    
    /** Obtiene los datos del jdialog y crea un monitor, despues llama a una 
     *  funcion para insertarlo en la base de datos.
     * @throws SQLException 
     */
    public void InsertarMonitor() throws SQLException {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");//formato de fecha
        String nombre = (String) (vdNMonitor.jTextFieldNombre.getText());
        String dni = (String) (vdNMonitor.jTextFieldDNI.getText());
        String telefono = (String) (vdNMonitor.jTextFieldTelefono.getText());
        String correo = (String) (vdNMonitor.jTextFieldCorreo.getText());
        Date fechachooser = vdNMonitor.jDateChooserFecha.getDate();
        String nick = (String) (vdNMonitor.jTextFieldNick.getText());
        if (codigoMonitor.isBlank() || nombre.isBlank() || dni.isBlank() || telefono.isBlank() || correo.isBlank() || fechachooser == null || nick.isBlank()) {
            //si cualquier string es nulo o tiene un espacio en blanco, o no se selecciono fecha
            vmensaje.Mensaje(vdNMonitor, "error", "Datos incompletos.");
        } else if (!mdao.telefonoCorrecto(telefono)) {
            //si el telefono no es una cadena numerica
            vmensaje.Mensaje(vdNMonitor, "error", "El nº de telefono solo puede contener caracteres numericos.");
        } else if(!mdao.validarEmail(correo)) {
            vmensaje.Mensaje(vdNMonitor, "error", "El correo electronico no tiene el formato correcto");
        } else {
            String fecha = formatoFecha.format(fechachooser);
            Monitor monitor = new Monitor(this.codigoMonitor, nombre, dni, telefono, correo, fecha, nick);
            //mdao.setConexion(conexion);
            mdao.setSesion(sesion);
            if(actualizar) {
                Monitor aux = mdao.buscaMonitor(codigoMonitor);
                aux.setNombre(nombre);
                aux.setDni(dni);
                aux.setTelefono(telefono);
                aux.setCorreo(correo);
                aux.setFechaentrada(fecha);
                aux.setNick(nick);
                mdao.actualizarMonitor(aux);
                vmensaje.Mensaje(vdNMonitor, "info", "El monitor fue actualizado correctamente.");
            } else {
                mdao.InsertarMonitor(monitor);
                vmensaje.Mensaje(vdNMonitor, "info", "El monitor fue insertado correctamente.");
            }
        }
    }
    
    /** Da de baja a un monitor previamente seleccionado del jtable y su codigo
     * pasado por parametro. 
     * @param codigo
     * @throws SQLException 
     */
    public void BajaMonitor(String codigo) throws SQLException {
        Monitor m = mdao.buscaMonitor(codigo);
        int opcion = vmensaje.MensajeConfirmacion(vmonitor, "¿Seguro que quieres dar de baja a "+ m.getNombre() +"?");
        if (opcion == 0) {
            mdao.eliminarMonitor(codigo);
            vmensaje.Mensaje(vmonitor, "info", "Se ha eliminado correctamente a " + m.getNombre());
        }
    }
    
    /** Abre un jdialog y muestra los datos de un monitor previamente seleccionado del jtable
    *  para actualizarlo.
    * @param codigo
    * @throws SQLException 
    */
    public void actualizarMonitor(String codigo) throws SQLException {
        actualizar = true;
        codigoMonitor = codigo;
        Monitor monitor = mdao.buscaMonitor(codigo);
        vdNMonitor.jButtonInsertar.setText("Actualizar");//cambiamos el nombre del jbutton del jdialog
        vdNMonitor.setTitle("Actualizar Monitor");//le ponemos un el titulo al jdialog
        //asignamos los datos del monitor para que los muestre en el jdialog:
        vdNMonitor.jTextFieldCodigo.setText(monitor.getCodmonitor());//le asignamos el codigoMonitor al jtextfield del codigoMonitor
        vdNMonitor.jTextFieldNombre.setText(monitor.getNombre());
        vdNMonitor.jTextFieldDNI.setText(monitor.getDni());
        vdNMonitor.jTextFieldTelefono.setText(monitor.getTelefono());
        vdNMonitor.jTextFieldCorreo.setText(monitor.getCorreo());
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");//formato de fecha
        vdNMonitor.jTextFieldNick.setText(monitor.getNick());
        try {//cambiamos la fecha tipo String a tipo Date:
            Date f_entrada = formatoFecha.parse(monitor.getFechaentrada());
            vdNMonitor.jDateChooserFecha.setDate(f_entrada);
            vdNMonitor.setVisible(true);//se muestra el jdialog
        } catch (ParseException ex) {
            vmensaje.Mensaje(vmonitor, "error", "Se ha producido un error al cargar la fecha de entrada de " +
                    monitor.getNombre() + ":\n" + ex.getMessage());
        }
    }    
    
   // EXAMEN MODIFICACION ENERO: 
   /** Muestra el jdialog de cuota mensual
    * 
    */
   public void verCuota() {
        vdCuota.setVisible(true);//mostramos el jdialog
    }
    
    /** Muestra la informacion del monitor indicado en un jtable en el jdialog
     * 
     * @param codMonitor
     * @throws SQLException 
     */
    private void Cuotas(String codMonitor) throws SQLException {
        mtabla.vaciarTablaMonitores();
        ArrayList<String[]> lista = mdao.verCuota(codMonitor);
        mtabla.RellenarTablaCuota(lista);
        mostrarInformacionCuota(codMonitor);        
    }
    
    /** Calcula la cuota, el nº total de socios inscritos para el monitor dado por parametro.
     *  Y muestra esa informacion en el jdialog
     * @param codMonitor
     * @throws SQLException 
     */
    private void mostrarInformacionCuota(String codMonitor) throws SQLException {
        int nsocios=0, sociosxactividad = 0, nactividades=0, precio = 0, cuota = 0;
        //int nfilas = vdCuota.jTableActividad.getRowCount();
        //float precio=0, cuota=0;
        String[] precios = new String[vdCuota.jTableActividad.getRowCount()];
        String[] socios = new String[vdCuota.jTableActividad.getRowCount()];
        if(vdCuota.jTableActividad.getRowCount()>0) {
            for(int i = 0; i<vdCuota.jTableActividad.getRowCount(); i++) {
                precios[i] = String.valueOf(vdCuota.jTableActividad.getValueAt(i, 1));//obtenemos el valor del jtable
                socios[i] = String.valueOf(vdCuota.jTableActividad.getValueAt(i, 2));
                System.out.println(precios[i]+"--"+socios[i]);
                sociosxactividad = Integer.parseInt(socios[i]);//casteamos string->int
                System.out.println(sociosxactividad);
                precio = Integer.parseInt(precios[i]);
                cuota += sociosxactividad*precio; // calculo de la cuota total mensual
                System.out.println("cuota:"+cuota);
                nsocios +=sociosxactividad; // nº total de socios
                System.out.println("nsocios:"+nsocios);
            }
        }
        // muestra el numero de activades en el jdialog:
        nactividades=vdCuota.jTableActividad.getRowCount();
        vdCuota.jLabelNActividades.setText(Integer.toString(nactividades));
        //muestra el nombre completo del monitor seleccionado:
        Monitor m = mdao.buscaMonitor(codMonitor);
        vdCuota.jLabelMonitor.setText(m.getNombre());
        //muestra el nº de socios totales por monitor:
        vdCuota.jLabelNSocios.setText(Integer.toString(nsocios));
        // muestra la cuota mensual que hace ese monitor
        vdCuota.jLabelCuota.setText(Integer.toString(cuota));   
    }
    
    /**
     * Añade los listeners a controlar
     */
    private void addListeners() {
        // Listeners jdialog nuevo monitor:
        vdNMonitor.jButtonCancelar.addActionListener(this);
        vdNMonitor.jButtonInsertar.addActionListener(this);
        //listeners del jdialog cuota, // EXAMEN MODIFICACION ENERO:
        vdCuota.jButtonSalir.addActionListener(this);
        vdCuota.jButtonVerCuota.addActionListener(this);
    }
       
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "Cancelar" -> {
                vdNMonitor.dispose();
                vdNMonitor.setVisible(false);
                break;
            }
            case "Insertar" -> {
                try {
                    InsertarMonitor();
                } catch (SQLException ex) {
                    vmensaje.Mensaje(vdNMonitor, "error", "Error al insertar el nuevo monitor:\n" 
                            + ex.getMessage());                    
                }
                vdNMonitor.dispose();
                vdNMonitor.setVisible(false);
                break;
            }
            case "Actualizar" -> {
                try {
                    InsertarMonitor();
                } catch (SQLException ex) {
                    vmensaje.Mensaje(vdNMonitor, "error", "Error al actualizar el monitor:\n" 
                            + ex.getMessage());                    
                }
                vdNMonitor.dispose();
                vdNMonitor.setVisible(false);
                break;
            }
            case "Ver cuota" -> { // EXAMEN MODIFICACION ENERO:
                try {
                    String codMonitor = (String) vdCuota.jTextFieldCodMonitor.getText();
                    Monitor monitor = mdao.buscaMonitor(codMonitor);
                    if (!codMonitor.isBlank() && monitor!=null) {
                        Cuotas(codMonitor);
                    } else vmensaje.Mensaje(vdCuota, "error", "El monitor buscado no existe.\nCodigo monitor tiene la forma MXXX.\nEj: M001");
                    break;
                } catch (SQLException ex) {
                    vmensaje.Mensaje(vdCuota, "error", "Se ha producido un error:\n" 
                            + ex.getMessage());
                }
            }
            case "Salir" -> { // EXAMEN MODIFICACION ENERO:
                vdCuota.dispose();
                vdCuota.setVisible(false);
                break;
            }
        }
    }
}