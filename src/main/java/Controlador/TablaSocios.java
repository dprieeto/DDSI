/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.*;
import Modelo.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** Clase de funciones para manejar el jtable del jpanel de socios 
 *  libreria o clase de funciones, no tiene atributos
 * @author David Prieto Araujo
 */
public class TablaSocios {
    /** Inicializa la tabla para poder usarla
     * 
     * @param v 
     */
    public void InicializarTabla(VPanelSocios v) {
        //DefaultTableModel modelo = new DefaultTableModel();
        v.jTableSocio.setModel(modelo);
        //tabla.setModel(modelo);
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
    
    public void scrolltable(VPanelMonitores v) {
       v.jTableMonitor.addComponentListener(new ComponentAdapter() {
           @Override
           public void componentResized(ComponentEvent e) {
               int lastIndex = v.jTableMonitor.getRowCount()-1;
               v.jTableMonitor.changeSelection(lastIndex, 0,false,false);
           }
       });
   }
    public void DibujarTablaSocios(VPanelSocios vsocios) {
        String[] columnasTabla = {"Nº Socio", "Nombre", "DNI",
            "Fecha Nacimiento", "Telefono", "Correo", "Fecha Entrada", "Categoria"};
        modelo.setColumnIdentifiers(columnasTabla);
        //Para no permitir el redimensionamiento de las columnas con el raton:
        vsocios.jTableSocio.getTableHeader().setResizingAllowed(false);
        vsocios.jTableSocio.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        //Asi se fija el ancho en columnas:
        vsocios.jTableSocio.getColumnModel().getColumn(0).setPreferredWidth(850);//40
        vsocios.jTableSocio.getColumnModel().getColumn(1).setPreferredWidth(3000);//240
        vsocios.jTableSocio.getColumnModel().getColumn(2).setPreferredWidth(1300);//70
        vsocios.jTableSocio.getColumnModel().getColumn(3).setPreferredWidth(2000);//70
        vsocios.jTableSocio.getColumnModel().getColumn(4).setPreferredWidth(1300);//200
        vsocios.jTableSocio.getColumnModel().getColumn(5).setPreferredWidth(4500);//150
        vsocios.jTableSocio.getColumnModel().getColumn(6).setPreferredWidth(2000);//60
        vsocios.jTableSocio.getColumnModel().getColumn(7).setPreferredWidth(1000);        
    }
    
    public void rellenarTablaSocios(ArrayList<Socio> socios) {
        Object[] fila = new Object[8];
        int numRegistros = socios.size();
        for (int i = 0; i<numRegistros; i++) {
            fila[0] = socios.get(i).getNumerosocio();
            fila[1] = socios.get(i).getNombre();
            fila[2] = socios.get(i).getDni();
            fila[3] = socios.get(i).getFechanacimiento();
            fila[4] = socios.get(i).getTelefono();
            fila[5] = socios.get(i).getCorreo();
            fila[6] = socios.get(i).getFechaentrada();
            fila[7] = socios.get(i).getCategoria();
            modelo.addRow(fila);
        }
    }
    
    public void vaciarTablaSocios() {
        while(modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
    }
}
