 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Conexion;
import Modelo.HibernateUtilMariaDB;
import Modelo.HibernateUtilOracle;
import Vista.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import org.hibernate.Session;

/** Esta clase controla el acceso a la base de datos
 *
 * @author David Prieto Araujo
 */
public class ControladorLogin implements ActionListener{//para los actionslisteners
    private Conexion conexion = null;
    private VistaConsola vconsola = null;
    private VistaLogin vLogin = null;
    private VistaMensaje vMensaje = null;
    private String servidor; //Nombre del SGBD al que se quiere conectar (Oracle o MariaDB)
    private String ip; //Dirección IP del servidor
    private String servicio; //Nombre del servicio (Oracle) o de la base de datos (MariaDB)
    private String usuario; //Nombre del usuario con el que se realiza la conexion
    private String password; //clave del usuario con el que se realiza la conexión
    private Session sesion;
    private static boolean es_oracle;
    
    /** Constructor de la clase, inicialzia los atributos y añade los listeners
     * 
     */
    public ControladorLogin(){
        vconsola=new VistaConsola();
        vLogin=new VistaLogin();        
        vMensaje=new VistaMensaje();//Usaremos un objeto de tipo vMensaje para mostrar mensajes
        addListeners();        
        vLogin.setLocationRelativeTo(null);//situa la ventana en el centro de la pantalla
        vLogin.setVisible(true);//muestra la ventana
    }
    
    /** Recupera los metadatos
     * 
     */
    public void recuperarMetaDatos(){
        try {
            vconsola.mensajeMetadatos(this.conexion.informacionBD());
        } catch (SQLException ex) {
            vconsola.mensajeConsola("Se produjo una excepción :(");
        }
    }
    
    /** Obtiene el servidor que quiere utilizar del jcombobox (e introduce el resto de datos si es en forma automatica)
     *  dependiendo si se va a utilizar oracle o mariadb. Si se quisiese conectar a otra BD usando los mismos servidores
     * habria que cambiarlo aqui, o cambiar la forma de obtener los datos a manual.
     * De forma automatica no va a salir error de datos incorrectos una vez se seleccione el servidor, solo saldra si no se selecciona este
     */
    private void obtenerDatos() {
        // Forma automatica:
        servidor = (String) (vLogin.JComboBoxServidor.getSelectedItem());
        if ( servidor != null) {
            if (servidor.equals("oracle")) {
                ip = "172.17.20.39";
                servicio = "1521";                    
                vLogin.jTextFieldDir_ip.setText(ip);
                vLogin.jTextFieldServicio.setText(servicio);                   
            } else if (servidor.equals("mariadb")) {
                ip = "172.18.1.241";
                servicio = "3306";                    
                vLogin.jTextFieldDir_ip.setText(ip);
                vLogin.jTextFieldServicio.setText(servicio);
            }
            // esta parte es opcional ponerla pero se hace mas sencillo al iniciar sesion si es el mismo usuario:
            usuario = "DDSI_027";
            password = "DDSI_027";
            vLogin.jTextFieldUsuario.setText(usuario);
            vLogin.jPasswordFieldPassword.setText(password);
        }
        /* 
        // Forma manual:
        servidor = (String) (vLogin.JComboBoxServidor.getSelectedItem());
        ip = (String) (vLogin.jTextFieldDir_ip.getText());
        servicio = (String) (vLogin.jTextFieldServicio.getText());
        usuario = (String) (vLogin.jTextFieldUsuario.getText());
        password = new String(vLogin.jPasswordFieldPassword.getPassword());
        */
    }
    
    /** Añade los listeners a controlar
     * 
     */
    private void addListeners(){//se asignan los listeners a los componenetes que lanzan eventos
        vLogin.jButtonConectar.addActionListener(this);
        vLogin.jButtonSalir.addActionListener(this);
        // se añade tambien el jcombobox para que al seleccionar un servidor se escriba el resto de los datos automaticamente
        vLogin.JComboBoxServidor.addActionListener(this);
        /*
        vLogin.jTextFieldDir_ip.addActionListener(this);
        vLogin.jTextFieldServicio.addActionListener(this);
        vLogin.jTextFieldUsuario.addActionListener(this);
        vLogin.jPasswordFieldPassword.addActionListener(this);
        */
    }
    
    /** Comprueba que no haya datos vacios o en blanco, o tenga valor nulo
     * 
     * @return 
     */
    private boolean datosCorrectos() {
        boolean formato = false;
        if (ip != null && servidor != null && servicio != null && password != null && usuario != null)
            if (!ip.isBlank() && !servidor.isBlank() && !servicio.isBlank() && !password.isBlank() && !usuario.isBlank())
                    formato = true;
        return formato;
    }
    
    /** Se conecta a la base de datos indicada
     * 
     * @return conexion
     */
    public Conexion conectar(){
         Conexion c = null;
         try {
             c = new Conexion(servidor, ip, servicio, usuario, password);
         }
         catch (SQLException e) {
             vconsola.mensajeConsola("Se produjo una excepción :(");
             vMensaje.Mensaje("error","Error en la conexion. Revise los datos introducidos\n"
                                + e.getMessage(), 0);
         } 
         return c;
    }
    
    /** Desconecta al usuario de la base de datos
      * 
      * @throws SQLException 
      */
     public void desconectar() throws SQLException {
         conexion.desconexion();
     }
    
    /** Se conecta a la base de datos mediante hibernate
     * 
     * @return 
     */
    public Session conectarHibernate() {
        //String server = (String) (vLogin.JComboBoxServidor.getSelectedItem());
        //if (server == "MariaDB") server = "mariadb";
        //else if (server == "Oracle") server = "oracle";
        if (esOracle()/*"oracle".equals(server)*/) {
            sesion = HibernateUtilOracle.getSessionFactory().openSession();
        } else if (!esOracle()/*"mariadb".equals(server)*/) {
            sesion = HibernateUtilMariaDB.getSessionFactory().openSession();
        }
        return (sesion);
    }
    
    /** Devuelve true si la conexion realizada en la base de datos pertence a
     * oracle, false si pertenece a mariadb. 
     * @return 
     */
    public boolean esOracle() {
        String server = (String) (vLogin.JComboBoxServidor.getSelectedItem());
        boolean oracle=false;
        if(server.equals("oracle")) {
            oracle = true;
            es_oracle = true;
        }
            
        return oracle;
    }
    
    /** Devuelve true si la conexion realizada en la base de datos pertence a
     * oracle, false si pertenece a mariadb. 
     * Metodo estatico ppara que se pueda llamar sin necesidad de crear un 
     * objeto de la clase.
     * @return 
     */
    public static boolean esOracleStatic() {
        boolean oracle=false;
        if(es_oracle) {
            oracle = true;
        } else {
        }
        return oracle;
    }
     
    /** Desconecta la sesion abierta del hibernate
     * 
     */
    public void desconectarHibernate() {
        sesion.close();
    } 
    
     /** Controla los jbuttons del jframe de vistalogin
      * 
      * @param e 
      */
    @Override
    public void actionPerformed(ActionEvent e) {
        obtenerDatos();//forma automatica
        switch(e.getActionCommand()){ // Tipo de componente, ej: JButton
            case "Conectar" -> {
                //obtenerDatos();//forma manual
                if (datosCorrectos()) {
                    sesion=conectarHibernate();                                  
                    //conexion = conectar();
                    if (sesion!=null/*conexion != null*/) {
                        try {
                            vconsola.mensajeConsola("info", "Conectado correctamente al servidor " /*+ conexion.informacionBD().getDatabaseProductName()*/);
                            vMensaje.Mensaje("info","Conectado correctamente al servidor "
                                    /*+ sesion.informacionBD().getDatabaseProductName()*/, 1);// 1 para mostrar icono de informacion
                            vLogin.dispose();                        
                            ControladorPrincipal controlador = new ControladorPrincipal(sesion);
                            
                            //ControladorPrincipal controlador = new ControladorPrincipal(conexion);//instanciamos unn objeto de tipo conexion
                        } catch (Exception sqle) {
                            vconsola.mensajeConsola("error", "Error en la conexion. Revise los datos introducidos"+ sqle.getMessage());
                            vMensaje.Mensaje("error","Error en la conexion. Revise la conexion o los datos introducidos\n"
                                    + sqle.getMessage(), 0);//0 para mostrar icono de error
                        }
                    }
                } else {
                    vMensaje.Mensaje("error", "Datos incompletos.", 0);
                }
                //break;
            }
            case "Salir" -> { //Propiedad actionCommand del botón "Salir"
                vLogin.dispose(); //limpia todos los objetos
                System.exit(0);
                //break;
            }
        }
        /* Otra manera de hacerlo mediante if-else if:
        if (e.getSource() == vLogin.jButtonSalir) {
            vl.dispose();
            System.exit(0);
        }
        else if (e.getSource() == vLogin.jButtonConectar) {
            //codigo
        }
        */
    }
}