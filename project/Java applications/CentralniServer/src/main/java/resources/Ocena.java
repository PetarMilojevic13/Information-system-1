/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resources;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petar
 */
@XmlRootElement
public class Ocena implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Ocena")
    private int ocena;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Datum")
   
    private int datum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Vreme")
    
    private int vreme;

    private static final long serialVersionUID = 1L;

    private int korisnik1;

    private int video1;

    public Ocena() {
    }


    public Ocena( int korisnik,int video,int ocena, int datum, int vreme) {
     
        this.ocena = ocena;
        this.datum = datum;
        this.vreme = vreme;
        this.korisnik1=korisnik;
        this.video1=video;
    }






    public int getKorisnik1() {
        return korisnik1;
    }

    public void setKorisnik1(int korisnik1) {
        this.korisnik1 = korisnik1;
    }

    public int getVideo1() {
        return video1;
    }

    public void setVideo1(int video1) {
        this.video1 = video1;
    }

    @Override
    public int hashCode() {
       
        int hash = 0;
        
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ocena)) {
            return false;
        }
        Ocena other = (Ocena) object;

        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Ocena[ ocenaPK=" + 5 + " ]";
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public int getDatum() {
        return datum;
    }

    public void setDatum(int datum) {
        this.datum = datum;
    }

    public int getVreme() {
        return vreme;
    }

    public void setVreme(int vreme) {
        this.vreme = vreme;
    }
    
}
