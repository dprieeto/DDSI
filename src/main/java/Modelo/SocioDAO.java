/*
 * Cdlick nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Vista.VistaMensaje;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/** Esta clase tiene funciones para el manejo de los socios
 *
 * @author David Prieto Araujo
 */
public class SocioDAO {
    private Session sesion;

    public SocioDAO() {
        sesion = null;
    }    
    
    public SocioDAO(Session sesion) {
        this.sesion = sesion;
    }

    public void setSesion(Session sesion) {
        this.sesion = sesion;
    }
    
    /** Devuelve un ArrayList con los socios de la base de datos ordenados
     * por numero de socio.
     * @return
     * @throws SQLException 
     */
    public ArrayList<Socio> listaSocios() throws SQLException {
        Transaction transaction = sesion.beginTransaction();//comienzo de la transaccion
        //consulta HQL: El segundo parámetro indica que estamos mapeando completamente la tabla MONITOR en la clase Monitor.
        //El resultado de las consultas se recupera con el método list()
        Query consulta = sesion.createQuery ("SELECT s FROM Socio s ORDER BY NUMEROSOCIO", Socio.class);
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list();
        transaction.commit();//confirmacion de la transaccion
        return socios;
    }
    
    /** Devuelve un ArrayList con los monitores cuyo nombre empiecen por la letra pasada
     *  por parametro ordenados por numero de socio.
     * @param letra
     * @return
     * @throws SQLException 
     */
    public ArrayList<Socio> listaSociosPorLetra(String letra) throws SQLException {
        Transaction transaction = sesion.beginTransaction();
        String c = String.format("SELECT * FROM SOCIO WHERE nombre LIKE %s  ORDER BY NUMEROSOCIO", letra);
        Query consulta = sesion.createQuery (c, Socio.class);
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list();
        transaction.commit();
        return socios;
    }
    
    /** Añade un socio a la base de datos previamente creado
     * 
     * @param socio
     * @throws SQLException 
     */
    public void InsertarSocio(Socio socio) throws SQLException {
        Transaction t = sesion.beginTransaction();
        sesion.save(socio);
        t.commit();
    }
    
    /** Dado un nº de socio, elimina el socio asociado al numero de la base de datos
     * 
     * @param numSocio
     * @throws SQLException 
     */
    public void eliminarSocio(String numSocio) throws SQLException {
        Transaction transaction = sesion.beginTransaction();
        Socio socio = sesion.get(Socio.class, numSocio);
        sesion.delete(socio);
        transaction.commit();
    }
    
    /** Dado un socio, actualiza sus datos en la bases
     * 
     * @param socio
     * @throws SQLException 
     */
    public void actualizarSocio(Socio socio) throws SQLException {
        Transaction transaction = sesion.beginTransaction();
        sesion.save(socio);
        transaction.commit();
    }   
    
    /** Busca un socio de la base de datos con el numSocio pasado por parametro 
     *  y devuelve el socio asociado a ese numero.
     * @param numSocio
     * @return socio
     * @throws SQLException 
     */
    public Socio buscaSocio(String numSocio) throws SQLException {
        ArrayList<Socio> lista = listaSocios();
        int i = 0;
        boolean encontrado = false;
        Socio socio = new Socio();
        while(i<lista.size() && !encontrado) {
            if (numSocio.equals(lista.get(i).getNumerosocio())) {
                socio = lista.get(i);
                encontrado=true;
            }
            i++;
        }
        if(!encontrado) socio = null;
        return socio;
    }

    /** genera y devuelve el siguiente codigo disponible para el nuevo socio 
     *  
     * @return codigo
     * @throws SQLException
     */
    public String generarNumeroSocio() throws SQLException {
        ArrayList<Socio> lista = listaSocios();
        String codigo= "S001";//empezamos con el primer codigo para buscar el siguiente disponible
        int codNum_lista,num_codigo ;
        num_codigo=1;// los codigos del socio empiezan en S001
        if (!lista.isEmpty()) {//si la lista no esta vacia
            for(int i=0;i<lista.size();i++) {
                String codM_lista = lista.get(i).getNumerosocio().substring(1);//quitamos la m del string del num_codigo: S001 -> 001
                codNum_lista = Integer.parseInt(codM_lista);//lo pasamos a entero: 001 -> 1
                if(num_codigo == codNum_lista)
                    num_codigo++;
                if (num_codigo<10) {
                    codigo = "S00" + num_codigo;
                } else if (num_codigo < 100 && num_codigo >= 10) {
                    codigo = "S0" + num_codigo;
                } else {
                    codigo = "S" + num_codigo;
                }      
            }             
        } else
            codigo="S001";
        return codigo;
    }
    
    /** Si la cadena es numerica devuelve True. False en otro caso.
     * 
     * @param numero
     * @return correcto
     */
    public boolean numeroCorrecto(String numero) {
        boolean correcto;
        try {
            Integer.parseInt(numero);
            correcto = true;
        } catch (NumberFormatException excepcion) {
            correcto = false;
        }
        return correcto;
    }
    
    public boolean categoriaCorrecta(String categoria) {
        boolean cat;
        if (categoria.equals("A") || categoria.equals("B") || categoria.equals("C") || categoria.equals("D") 
                || categoria.equals("E"))
            cat = true;
        else cat = false;
        return cat;            
    }
    
    /** Busca un/os socio/s con una opcion elegida de busqueda la info recibida
     * 
     * @param opcion
     * @param info 
     * @throws java.sql.SQLException 
     */
    public ArrayList<Socio> buscarSocio(String opcion, String info) throws SQLException {
        ArrayList<Socio> lista = new ArrayList<>();
        ArrayList<Socio> listaSocios = listaSocios();
        switch(opcion) {
            case "Nº de socio" -> {
                String num = info.substring(1);
                if (info.startsWith("S") && this.numeroCorrecto(num)) {
                    Socio s = buscaSocio(info);
                    if (s != null){
                        lista.add(s);
                    } else VistaMensaje.StaticMensaje(null, "error", "El socio buscado no existe.");
                } else VistaMensaje.StaticMensaje(null, "error", "El nº de socio tiene la forma SXXX, siendo X un numero.");
            }
            case "D.N.I" -> {
                int i = 0;
                boolean encontrado = false;
                while (!encontrado && i<listaSocios.size()) {
                    if(listaSocios.get(i).getDni().equals(info)) {
                        lista.add(listaSocios.get(i));
                        encontrado = true;
                    }
                    i++;
                }
                if (!encontrado) VistaMensaje.StaticMensaje(null, "error", "El socio buscado no existe.");
            }
            case "Telefóno" -> {
                System.out.println("3");
                if (this.numeroCorrecto(info)) {
                    for (int i = 0; i<listaSocios.size(); i++) {
                        if (listaSocios.get(i).getTelefono().equals(info))
                            lista.add(listaSocios.get(i));
                    }
                    if (!lista.isEmpty()) {
                        //this.stabla.vaciarTablaSocios();
                        //this.stabla.rellenarTablaSocios(lista);
                    } else VistaMensaje.StaticMensaje(null, "error", "El socio buscado no existe.");
                } else VistaMensaje.StaticMensaje(null, "error", "El nº de telefóno solo puede contener caracteres numericos.");
            }
            case "Categoría" -> {
                String categoria = info.toUpperCase();
                if (this.categoriaCorrecta(categoria)) {
                    char cat = categoria.charAt(0);
                    for (int i = 0; i<listaSocios.size(); i++) {
                        if (listaSocios.get(i).getCategoria().equals(cat))
                            lista.add(listaSocios.get(i));
                    }
                    if (lista.isEmpty()) 
                        VistaMensaje.StaticMensaje(null, "info", "Actualmente no hay socios en esa categoria");
                } else VistaMensaje.StaticMensaje(null, "error", "La categoria va desde A hasta E.");
            }
        }
        return lista;
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
}