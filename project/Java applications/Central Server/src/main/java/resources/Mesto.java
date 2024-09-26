/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resources;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petar
 */


@XmlRootElement

public class Mesto implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Naziv")
    private String naziv;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdMes")
    private Integer idMes;
   

    public Mesto() {
    }


    public Mesto( String naziv) {
        
        this.naziv = naziv;
    }

    public Integer getIdMes() {
        return idMes;
    }

    public void setIdMes(Integer idMes) {
        this.idMes = idMes;
    }




    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMes != null ? idMes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mesto)) {
            return false;
        }
        Mesto other = (Mesto) object;
        if ((this.idMes == null && other.idMes != null) || (this.idMes != null && !this.idMes.equals(other.idMes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Mesto[ idMes=" + idMes + " ]";
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    
}
