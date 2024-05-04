/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.*;
import Vista.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** Clase de funciones para manejar el jtable del jpanel de monitores 
 *  libreria o clase de funciones, no tiene atributos
 * @author David Prieto Araujo
 */
public class TablaMonitor {
    /** Inicializa la tabla para poder usarla
     * 
     * @param v 
     */
    public void InicializarTabla(VPanelMonitores v) {
        v.jTableMonitor.setModel(modelo);
        //scrolltable(v);
        //tabla.setModel(modelo);
        //table.scrollRectToVisible(table.getCellRect(table.getRowCount()-1, 0, true));
        //v.jTableMonitor.scrollRectToVisible(v.jTableMonitor.getCellRect(v.jTableMonitor.getRowCount()-1, 0, false));
        //v.jTableMonitor.scrollRectToVisible();
    }
    
    
    
    public void scrolltable(VPanelMonitores v) {
       v.jTableMonitor.addComponentListener(new ComponentAdapter() {
           @Override
           public void componentResized(ComponentEvent e) {
               int lastIndex = v.jTableMonitor.getRowCount()-1;
               v.jTableMonitor.changeSelection(lastIndex, 0,false,false);
           }
       });
   }
    
    public DefaultTableModel modelo = new DefaultTableModel() {
        /** Esta funcion permite que no se puede editar las celdas del jtable
         * 
         */
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    /** Dibuja el diseño del jtable, nombre de las columnas, ancho, etc.
     * 
     * @param vmonitor 
     */
    public void DibujarTablaMonitores(VPanelMonitores vmonitor) {
        String[] columnasTabla = {"Código", "Nombre", "DNI",
            "Telefóno", "Correo", "Fecha", "Nick"};
        modelo.setColumnIdentifiers(columnasTabla);
        //Para no permitir el redimensionamiento de las columnas con el raton:
        vmonitor.jTableMonitor.getTableHeader().setResizingAllowed(false);
        vmonitor.jTableMonitor.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        //Asi se fija el ancho en columnas:
        vmonitor.jTableMonitor.getColumnModel().getColumn(0).setPreferredWidth(450);//40
        vmonitor.jTableMonitor.getColumnModel().getColumn(1).setPreferredWidth(2500);//240
        vmonitor.jTableMonitor.getColumnModel().getColumn(2).setPreferredWidth(850);//70
        vmonitor.jTableMonitor.getColumnModel().getColumn(3).setPreferredWidth(850);//70
        vmonitor.jTableMonitor.getColumnModel().getColumn(4).setPreferredWidth(2000);//200
        vmonitor.jTableMonitor.getColumnModel().getColumn(5).setPreferredWidth(750);//150
        vmonitor.jTableMonitor.getColumnModel().getColumn(6).setPreferredWidth(750);//60
        //vmonitor.jTableMonitor.getColumnModel().getColumn(7).setPreferredWidth(450);
    }
    
    
    
    /** Rellena el jtable con la lista de monitores pasada por parametro
     * 
     * @param monitores 
     */
    public void rellenarTablaMonitores(ArrayList<Monitor> monitores) {
        Object[] fila = new Object[7];
        int numRegistros = monitores.size();
        for (int i = 0; i<numRegistros; i++) {
            fila[0] = monitores.get(i).getCodmonitor();
            fila[1] = monitores.get(i).getNombre();
            fila[2] = monitores.get(i).getDni();
            fila[3] = monitores.get(i).getTelefono();
            fila[4] = monitores.get(i).getCorreo();
            fila[5] = monitores.get(i).getFechaentrada();
            fila[6] = monitores.get(i).getNick();
            modelo.addRow(fila);
        }
    } 
    
    /** Vacia el jtable
     * 
     */
    public void vaciarTablaMonitores() {
        while(modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
    }
    
    // Examen de modificacion enero:
    
    public void InicializarTablaCuota(VDialogCuota cuota) {
        cuota.jTableActividad.setModel(modelo);
    }
    
    public void DibujarTablaCuota(VDialogCuota vcuota) {
        String[] columnas = { "Nombre", "Precio", "Socios"};
        modelo.setColumnIdentifiers(columnas);
        //Para no permitir el redimensionamiento de las columnas con el raton:
        vcuota.jTableActividad.getTableHeader().setResizingAllowed(false);
        vcuota.jTableActividad.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        //fijamos el ancho de las columnas
        vcuota.jTableActividad.getColumnModel().getColumn(0).setPreferredWidth(450);//40
        vcuota.jTableActividad.getColumnModel().getColumn(1).setPreferredWidth(450);//40
        vcuota.jTableActividad.getColumnModel().getColumn(2).setPreferredWidth(450);//40
    }
    
    public void RellenarTablaCuota(ArrayList<String[]> cuota) {
        int numRegistros = cuota.size();
        for (int i = 0; i<numRegistros; i++) {
            modelo.addRow(cuota.get(i));
        }
    }
    // FIN EXAMEN MODIFICACION ENERO:
}

