/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author rdsbl
 */
@Entity
@Table(name = "ACTIVIDAD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Actividad.findAll", query = "SELECT a FROM Actividad a"),
    @NamedQuery(name = "Actividad.findByIdactividad", query = "SELECT a FROM Actividad a WHERE a.idactividad = :idactividad"),
    @NamedQuery(name = "Actividad.findByNombre", query = "SELECT a FROM Actividad a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Actividad.findByDescripcion", query = "SELECT a FROM Actividad a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "Actividad.findByPreciobasemes", query = "SELECT a FROM Actividad a WHERE a.preciobasemes = :preciobasemes")})
public class Actividad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDACTIVIDAD")
    private String idactividad;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "PRECIOBASEMES")
    private int preciobasemes;
    @JoinTable(name = "REALIZA", joinColumns = {
        @JoinColumn(name = "IDACTIVIDAD", referencedColumnName = "IDACTIVIDAD")}, inverseJoinColumns = {
        @JoinColumn(name = "NUMEROSOCIO", referencedColumnName = "NUMEROSOCIO")})
    @ManyToMany
    private Set<Socio> socios;
    @JoinColumn(name = "MONITORRESPONSABLE", referencedColumnName = "CODMONITOR")
    @ManyToOne
    private Monitor monitorresponsable;

    public Actividad() {
        this.socios = new HashSet<>();
    }

    public Actividad(String idactividad) {
        this.socios = new HashSet<>();
        this.idactividad = idactividad;
    }

    public Actividad(String idactividad, String nombre, String descripcion, int preciobasemes) {
        this.socios = new HashSet<>();
        this.idactividad = idactividad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.preciobasemes = preciobasemes;
    }

    public Actividad(String idactividad, String nombre, String descripcion, int preciobasemes, Monitor monitorresponsable) {
        this.idactividad = idactividad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.preciobasemes = preciobasemes;
        this.monitorresponsable = monitorresponsable;
    }
    

    public String getIdactividad() {
        return idactividad;
    }

    public void setIdactividad(String idactividad) {
        this.idactividad = idactividad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPreciobasemes() {
        return preciobasemes;
    }

    public void setPreciobasemes(int preciobasemes) {
        this.preciobasemes = preciobasemes;
    }

    @XmlTransient
    public Set<Socio> getSocios() {
        return socios;
    }

    public void setSocios(Set<Socio> socios) {
        this.socios = socios;
    }

    public Monitor getMonitorresponsable() {
        return monitorresponsable;
    }

    public void setMonitorresponsable(Monitor monitorresponsable) {
        this.monitorresponsable = monitorresponsable;
    }
    
    public void addSocio(Socio socio) {
        socios.add(socio);
        socio.getActividades().add(this);
    }
    
    public void removeSocio(Socio socio) {
        socios.remove(socio);
        socio.getActividades().remove(this);
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idactividad != null ? idactividad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Actividad)) {
            return false;
        }
        Actividad other = (Actividad) object;
        if ((this.idactividad == null && other.idactividad != null) || (this.idactividad != null && !this.idactividad.equals(other.idactividad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Actividad[ idactividad=" + idactividad + " ]";
    }
    
}
