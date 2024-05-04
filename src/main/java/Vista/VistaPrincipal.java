/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

/**
 *
 * @author rdsbl
 */
public class VistaPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form VistaPrincipal
     */
    public VistaPrincipal() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuMonitores = new javax.swing.JMenu();
        jMenuGestionMonitores = new javax.swing.JMenuItem();
        jMenuItemCuota = new javax.swing.JMenuItem();
        jMenuSocios = new javax.swing.JMenu();
        jMenuGestionSocios = new javax.swing.JMenuItem();
        jMenuActividades = new javax.swing.JMenu();
        jMenuItemSociosxActividad = new javax.swing.JMenuItem();
        jMenuSalir = new javax.swing.JMenu();
        jMenuSalirAplicacion = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestión del Gimnasio \"Body Perfect\"");
        setPreferredSize(new java.awt.Dimension(1050, 300));
        setResizable(false);

        jMenuMonitores.setText("Monitores");

        jMenuGestionMonitores.setText("Gestion de monitores");
        jMenuGestionMonitores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuGestionMonitoresActionPerformed(evt);
            }
        });
        jMenuMonitores.add(jMenuGestionMonitores);

        jMenuItemCuota.setText("Cuota");
        jMenuItemCuota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCuotaActionPerformed(evt);
            }
        });
        jMenuMonitores.add(jMenuItemCuota);

        jMenuBar1.add(jMenuMonitores);

        jMenuSocios.setText("Socios");

        jMenuGestionSocios.setText("Gestion de socios");
        jMenuGestionSocios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuGestionSociosActionPerformed(evt);
            }
        });
        jMenuSocios.add(jMenuGestionSocios);

        jMenuBar1.add(jMenuSocios);

        jMenuActividades.setText("Actividades");
        jMenuActividades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuActividadesActionPerformed(evt);
            }
        });

        jMenuItemSociosxActividad.setText("Socios por Actividad");
        jMenuActividades.add(jMenuItemSociosxActividad);

        jMenuBar1.add(jMenuActividades);

        jMenuSalir.setText("Salir");

        jMenuSalirAplicacion.setText("Salir de la aplicacion");
        jMenuSalirAplicacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuSalirAplicacionActionPerformed(evt);
            }
        });
        jMenuSalir.add(jMenuSalirAplicacion);

        jMenuBar1.add(jMenuSalir);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 405, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuSalirAplicacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuSalirAplicacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuSalirAplicacionActionPerformed

    private void jMenuGestionSociosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuGestionSociosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuGestionSociosActionPerformed

    private void jMenuGestionMonitoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuGestionMonitoresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuGestionMonitoresActionPerformed

    private void jMenuActividadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuActividadesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuActividadesActionPerformed

    private void jMenuItemCuotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCuotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemCuotaActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JMenu jMenuActividades;
    private javax.swing.JMenuBar jMenuBar1;
    public javax.swing.JMenuItem jMenuGestionMonitores;
    public javax.swing.JMenuItem jMenuGestionSocios;
    public javax.swing.JMenuItem jMenuItemCuota;
    public javax.swing.JMenuItem jMenuItemSociosxActividad;
    public javax.swing.JMenu jMenuMonitores;
    public javax.swing.JMenu jMenuSalir;
    public javax.swing.JMenuItem jMenuSalirAplicacion;
    public javax.swing.JMenu jMenuSocios;
    // End of variables declaration//GEN-END:variables
}
