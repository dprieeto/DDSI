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
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.Session;

/** Esta clase se utiliza para controlar el jpanel de socios
 *
 * @author David Prieto Araujo 
 */
public class ControladorSocios implements ActionListener{
    private VPanelSocios vsocios = null;
    private VistaMensaje vmensaje = null;
    private VDialogNuevoSocio vdNSocio = null;
    private VDialogInscripciones vdInscripciones;
    private VDialogBuscarSocio vdBuscarSocio;
    private SocioDAO sdao = null;
    private TablaSocios stabla = null;
    //private Conexion conexion = null;
    private Session sesion;
    private String numSocio; // numero de socio a crear o actualizar
    private boolean actualizar; // true-> actualiza, false -> inserta uno nuevo
    

    /** Constructor de la clase
     *  Inicializa los atributos, añade los listeners y pone el jdialog en modal
     * @param sesion
     */
    public ControladorSocios(Session sesion) {
        vsocios = new VPanelSocios();
        vmensaje = new VistaMensaje();
        vdNSocio = new VDialogNuevoSocio();
        vdBuscarSocio = new VDialogBuscarSocio();
        vdInscripciones = new VDialogInscripciones();
        sdao = new SocioDAO(sesion);
        stabla = new TablaSocios();
        this.sesion = sesion;
        numSocio = null;
        
        addListeners();
        
        vdNSocio.setLocationRelativeTo(null);//centramos el jdialog en la pantalla
        vdNSocio.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);//se pone en modo modal, 
        //toma el foco de la aplicacion y no se puede hacer nada hasta que cierre esta ventana
        vdInscripciones.jButtonContinuar.setEnabled(false);
        vdBuscarSocio.jButtonBuscarSocio.setEnabled(false);
    }

    public void setVsocios(VPanelSocios vsocios) {
        this.vsocios = vsocios;
    }   
    
    /** Obtiene los socios de la base de datos y rellena la tabla
     * 
     * @throws SQLException 
     */
    private void pideSocios() throws SQLException {
        ArrayList<Socio> lista = sdao.listaSocios();
        stabla.vaciarTablaSocios();
        stabla.rellenarTablaSocios(lista);
    }
    
    /** Carga en el jtable todos los socios de la base de datos
     *  Tambien se puede utilizar para actualizar despues de insertar, eliminar o actualizar
     * @param vpsocios
     * @throws SQLException 
     */
    public void cargarTablaSocios(VPanelSocios vpsocios) throws SQLException {
        this.vsocios = vpsocios;
        stabla.InicializarTabla(vpsocios);
        stabla.DibujarTablaSocios(vpsocios);
        pideSocios();
    }
    
    /** Vacia los datos del jtable
     * 
     * @param vpsocios 
     */
    public void vaciarTabla(VPanelSocios vpsocios) {
        vsocios = vpsocios;
        stabla.InicializarTabla(vpsocios);
        stabla.DibujarTablaSocios(vpsocios);
    }
    
    /** Abre un jdialog para crear un nuevo socio
     * 
     * @throws SQLException 
     */
    public void nuevoSocio() throws SQLException {
        actualizar = false;
        vdNSocio.setTitle("Nuevo Socio");
        vdNSocio.jButtonInsertar.setText("Insertar");
        numSocio = sdao.generarNumeroSocio();//generamos el siguiente codigo disponible
        vdNSocio.jTextFieldNSocio.setText(numSocio);//le asignamos el codigo al jtextfield del codigo
        vdNSocio.setVisible(true);//se muestra el jdialog
    }
    
    /** Obtiene los datos del jdialog y crea un socio, despues llama a una 
     *  funcion para insertarlo en la base de datos.
     * @throws SQLException 
     */
    public void InsertarSocio() throws SQLException {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");//formato de f_entrada
        String nombre = (String) (vdNSocio.jTextFieldNombre.getText());
        String dni = (String) (vdNSocio.jTextFieldDNI.getText());
        Date fchooser_nac = vdNSocio.jDateChooserFNac.getDate();
        String telefono = (String) (vdNSocio.jTextFieldTelefono.getText());
        String correo = (String) (vdNSocio.jTextFieldCorreo.getText());
        Date fchooser_entrada = vdNSocio.jDateChooserFEntrada.getDate();
        String categoria = (String) (vdNSocio.jTextFieldCategoria.getText());
        if (numSocio.isBlank() || nombre.isBlank() || dni.isBlank() || fchooser_nac == null || telefono.isBlank() || correo.isBlank() || fchooser_entrada == null || categoria.isBlank()) {
            //si cualquier string es nulo o tiene un espacio en blanco, o no se selecciono f_entrada
            System.out.println("error vacio o contiene espacios en blanco");
            vmensaje.Mensaje(vdNSocio, "error", "Datos incompletos.");
        } else if (!sdao.numeroCorrecto(telefono)) {
            //si el telefono no es una cadena numerica
            vmensaje.Mensaje(vdNSocio, "error", "El nº de telefono solo puede contener caracteres numericos.");
        } else if (fchooser_entrada.before(fchooser_nac) || fchooser_entrada.equals(fchooser_nac)) {
            //si la fecha de entrada es anterior o igual a la de nacimiento
            vmensaje.Mensaje(vdNSocio, "error", "La fecha de entrada solo puede ser posterior a la de nacimiento.");
        } else if (categoria.length()!=1 && !"A".equals(categoria) && !"B".equals(categoria) && !"C".equals(categoria) && !"D".equals(categoria) && !"E".equals(categoria)) {
            //si la longitud de la categoria es > a 1 o es distinto de A,B,C,D,E 
            vmensaje.Mensaje(vdNSocio, "error", "La categoria solo puede ser desde A hasta E.");
        } else {
            String f_nac = formatoFecha.format(fchooser_nac);
            String f_entrada = formatoFecha.format(fchooser_entrada);
            char cat = categoria.toUpperCase().charAt(0);
            Socio socio = new Socio(this.numSocio, nombre, dni, f_nac, telefono, correo, f_entrada, cat);
            sdao.setSesion(sesion);
            if (actualizar) {
                Socio aux = sdao.buscaSocio(numSocio);
                aux.setNombre(nombre);
                aux.setDni(dni);
                aux.setFechanacimiento(f_nac);
                aux.setTelefono(telefono);
                aux.setCorreo(correo);
                aux.setFechaentrada(f_entrada);
                aux.setCategoria(cat);
                sdao.actualizarSocio(aux);
                vmensaje.Mensaje(vsocios, "info", "Se ha actualizado el socio en la base de datos.");
            } else {
                sdao.InsertarSocio(socio);
                vmensaje.Mensaje(vsocios, "info", "Se ha añadido el nuevo socio a la base de datos.");
            }           
        }
    }
    
    /** Da de baja a un socio previamente seleccionado del jtable y su codigo
    * pasado por parametro. 
    * @param codigo
    * @throws SQLException 
    */
    public void bajaSocio(String codigo) throws SQLException {
        //System.out.println("codigo:" +codigo);
        Socio socio = sdao.buscaSocio(codigo);
        int opcion = vmensaje.MensajeConfirmacion(vsocios, "¿Seguro que quieres dar de baja a "+ socio.getNombre() +"?");
        if (opcion == 0) {
            //System.out.println("eliminando");    
            sdao.eliminarSocio(codigo);
            vmensaje.Mensaje(vsocios, "info", "Se ha eliminado correctamente a" + socio.getNombre());
        }
    }    
    
    /** Abre un jdialog y muestra los datos de un socio previamente seleccionado del jtable
    * para actualizarlo.
    * @param numeroSocio
    * @throws SQLException 
    */
    public void actualizarSocio(String numeroSocio) throws SQLException {
        actualizar = true;
        numSocio = numeroSocio;
        Socio socio = sdao.buscaSocio(numeroSocio);
        vdNSocio.jButtonInsertar.setText("Actualizar");//cambiamos el nombre del jbutton del jdialog
        vdNSocio.setTitle("Actualizar Socio");//le ponemos un el titulo al jdialog
        //asignamos los datos del socio para que los muestre al actualizar:
        vdNSocio.jTextFieldNSocio.setText(socio.getNumerosocio());//le asignamos el numerosocio al jtextfield del numerosocio
        vdNSocio.jTextFieldNombre.setText(socio.getNombre());
        vdNSocio.jTextFieldDNI.setText(socio.getDni());
        vdNSocio.jTextFieldTelefono.setText(socio.getTelefono());
        vdNSocio.jTextFieldCorreo.setText(socio.getCorreo());
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");//formato de fecha
        vdNSocio.jTextFieldCategoria.setText(socio.getCategoria().toString());
        try {//cambiamos la fecha tipo String a tipo Date:
            Date f_entrada = formatoFecha.parse(socio.getFechaentrada());
            Date f_nac = formatoFecha.parse(socio.getFechanacimiento());
            vdNSocio.jDateChooserFEntrada.setDate(f_entrada);
            vdNSocio.jDateChooserFNac.setDate(f_nac);
            vdNSocio.setVisible(true);//se muestra el jdialog
        } catch (ParseException ex) {
            vmensaje.Mensaje(vsocios, "error", "Se ha producido un error al cargar la/s fecha/s de entrada y/o nacimiento de " 
                    + socio.getNombre() + ":\n" + ex.getMessage());
        }
    }
    
    /** Abre un jdialog para buscar el/los socio/s.
     * 
     */
    public void buscarSocio() {
        vdBuscarSocio.setLocationRelativeTo(null);//centra el jdialog en la pantalla
        vdBuscarSocio.setTitle("Buscar Socio");
        vdBuscarSocio.setVisible(true);        
    }
    
    /** Busca el/los socio/s con la opcion elegida de filtrado y la informacion
     * correspondiente.
     * @throws SQLException 
     */
    private void buscar() throws SQLException {
        String opcion = (String) vdBuscarSocio.jComboBoxBusqueda.getSelectedItem();
        String info = (String) vdBuscarSocio.jTextFieldBuscarDato.getText();
        System.out.println("buscar " + info);
        if (!info.isBlank()) {
            try {
                ArrayList<Socio> lista = sdao.buscarSocio(opcion, info);
                vaciarTabla(vsocios);
                stabla.rellenarTablaSocios(lista);
            } catch (SQLException ex) {
                vmensaje.Mensaje(vdBuscarSocio, "error", "Se ha producido un error al buscar el socio:\n"
                + ex.getMessage());
            }
        } else vmensaje.Mensaje(vdBuscarSocio, "error", "Tienes que escribir " + opcion + " a buscar");
    }
    
    /** Activa el jbutton de busqueda si se selecciona una opcion del jcombobox
     * 
     */
    private void activarBusquedaSocio() {
        String tipo = (String) vdBuscarSocio.jComboBoxBusqueda.getSelectedItem();
        if (tipo != null)
            vdBuscarSocio.jButtonBuscarSocio.setEnabled(true);
    }
    
    /** Elimina la pantalla del jdialog cuando se ha terminado de utilizar
     * Mejorarlo para vdialog de todo tipo?
     * borrar funcion¿?
     */
    public void terminar(){
        vdNSocio.dispose();
        vdNSocio.setVisible(false);
    }

    /** que hacia esta funciuon?????? practica 5?
     * 
     * @throws SQLException 
     */
    public void gestionInscripciones() throws SQLException {
        int opcion = vmensaje.MensajeConfirmacion(vdInscripciones, "¿Quieres continuar?");
        if(opcion==0) {
            String numSocio = (String) vdInscripciones.jTextFieldDNI.getText();
            Socio socio = sdao.buscaSocio(numSocio);
        }
    } 
    
    /** Activa el jbutton de continuar si hay una opcion seleccionada en el 
     *  jcombobox.
     * 
     */
    private void compruebaOpcionInscripcion() {
        String opcion = (String) vdInscripciones.jComboBoxOpcion.getSelectedItem();
        if(opcion!=null)
            vdInscripciones.jButtonContinuar.setEnabled(true);
    }
    
    /** Añade los listeners a controlar del jdialog para nuevo socio
     * 
     */
    private void addListeners() {
        //listeners para nuevo socio:
        vdNSocio.jButtonInsertar.addActionListener(this);
        vdNSocio.jButtonCancelar.addActionListener(this);
        //listeners para la gestion de inscripciones:
        vdInscripciones.jButtonContinuar.addActionListener(this);
        vdInscripciones.jButtonSalir.addActionListener(this);
        vdInscripciones.jComboBoxOpcion.addActionListener(this);
        vdInscripciones.jComboBoxActividades.addActionListener(this);
        // listeners para buscar socio:
        vdBuscarSocio.jButtonBuscarSocio.addActionListener(this);
        vdBuscarSocio.jComboBoxBusqueda.addActionListener(this);//para que funcione activarBusqueda()  
        vdBuscarSocio.jButtonSalir.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        compruebaOpcionInscripcion();
        activarBusquedaSocio();
        switch (e.getActionCommand()) {
            case "Cancelar" -> {
                vdNSocio.dispose();
                vdNSocio.setVisible(false);
                break;
            }
            case "Insertar" -> {
                try {
                    InsertarSocio();
                } catch (SQLException ex) {
                    vmensaje.Mensaje(vdNSocio, "error", "Error al añadir el nuevo socio:\n" 
                            + ex.getMessage());                    
                }
                vdNSocio.dispose();
                vdNSocio.setVisible(false);
                break;
            }
            case "Actualizar" -> {
                try {
                    InsertarSocio();
                } catch (SQLException ex) {
                    vmensaje.Mensaje(vdNSocio, "error", "Error al actualizar el socio:\n" 
                            + ex.getMessage());                    
                }
                vdNSocio.dispose();
                vdNSocio.setVisible(false);
                break;
            }
            case "Salir" -> {
                vdInscripciones.dispose();
                vdInscripciones.setVisible(false);
                vdBuscarSocio.dispose();
                vdBuscarSocio.setVisible(false);
                break;
            }
            case "Continuar" -> {
                break;
            }
            case "Buscar socio" -> {
                try {
                    buscar();
                } catch (SQLException ex) {
                    vmensaje.Mensaje(vsocios, "error", "Se ha producido un error al buscar el socio en la base de datos:\n"
                        + ex.getMessage());
                }
                vdBuscarSocio.dispose();
                vdBuscarSocio.setVisible(false);
                break;
            }
        }
    }
}