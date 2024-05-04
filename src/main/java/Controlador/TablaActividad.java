/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.VDialogActividad;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** Esta clase o libreria de funciones, se utilizara para el manejo del jtable 
 * de actividades
 * @author David Prieto Araujo
 */
public class TablaActividad {
    /** Inicializa la tabla para poder usarla
     * 
     * @param v 
     */
    public void InicializarTabla(VDialogActividad v) {
        v.jTableActividad.setModel(modelo);
        //scrolltable(v);
        //tabla.setModel(modelo);
        //table.scrollRectToVisible(table.getCellRect(table.getRowCount()-1, 0, true));
        //v.jTableMonitor.scrollRectToVisible(v.jTableMonitor.getCellRect(v.jTableMonitor.getRowCount()-1, 0, false));
        //v.jTableMonitor.scrollRectToVisible();
    }
    
    public void scrolltable(VDialogActividad v) {
       v.jTableActividad.addComponentListener(new ComponentAdapter() {
           @Override
           public void componentResized(ComponentEvent e) {
               int lastIndex = v.jTableActividad.getRowCount()-1;
               v.jTableActividad.changeSelection(lastIndex, 0,false,false);
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
    
    /** Dibuja la tabla, las columnas, el ancho, etc.
     * 
     * @param vactividad 
     */
    public void DibujarTablaSociosxActividad(VDialogActividad vactividad) {
        String[] columnasTabla = {"Nombre", "Correo"};
        modelo.setColumnIdentifiers(columnasTabla);
        //Para no permitir el redimensionamiento de las columnas con el raton:
        vactividad.jTableActividad.getTableHeader().setResizingAllowed(false);
        vactividad.jTableActividad.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        //Asi se fija el ancho en columnas:
        vactividad.jTableActividad.getColumnModel().getColumn(0).setPreferredWidth(150);//40
        vactividad.jTableActividad.getColumnModel().getColumn(1).setPreferredWidth(150);//240
        
    }
    
    /** Rellena la tabla de jdialog de sociosxactividad
     * 
     * @param sociosxActividad 
     */
    public void rellenarTablaSociosxActividad(ArrayList<String[]> sociosxActividad) {
        int numRegistros = sociosxActividad.size();
        for (int i = 0; i<numRegistros; i++) {
            modelo.addRow(sociosxActividad.get(i));
        }
    }
    
    /** Vacia la tabla de jtable
     * 
     */
    public void vaciarTablaSocioxActividad() {
        while(modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
    }
    
    /** Comprueba que la tabla este vacia. 
     * True si esta vacia, False en otro caso.
     * @return 
     */
    public boolean tablaVacia() {
        boolean vacio;
        if (modelo.getRowCount()>0) vacio = false;
        else vacio = true;
        return vacio;
    }
}
