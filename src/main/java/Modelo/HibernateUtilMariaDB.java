/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import org.hibernate.service.ServiceRegistry;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/** Conexion a la base de datos de mariadb mediante hibernate
 *
 * @author David Prieto Araujo
 */
public class HibernateUtilMariaDB {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static SessionFactory buildSessionFactory() {
        try {
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
            .configure("hibernateMariaDB.cfg.xml").build();
            Metadata metadata = new MetadataSources( serviceRegistry).getMetadataBuilder().build();
            return metadata.getSessionFactoryBuilder().build();
        } catch (Throwable ex) {
            Vista.VistaMensaje.StaticMensaje(null, "error", "Se ha producido un error al abrir la sesion, revise la conexion:\n"
                    + ex.getMessage());
            System.err.println("Build SeesionFactory failed :" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() { return sessionFactory; }
    
    public static void close() {
        if ((sessionFactory!=null) && (sessionFactory.isClosed()==false)) {
            sessionFactory.close();
            sessionFactory.close();
        }
    }
}
