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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Pretplata implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Datum")

    private int datum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Vreme")
   
    private int vreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Cena")
    private int cena;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdPre")
    private Integer idPre;

    private int korisnik;

    private int paket;

    public Pretplata() {
    }


    public Pretplata(int korisnik,int paket, int datum, int vreme, int cena) {
        this.korisnik=korisnik;
        this.paket=paket;
        this.datum = datum;
        this.vreme = vreme;
        this.cena = cena;
    }

    public Integer getIdPre() {
        return idPre;
    }

    public void setIdPre(Integer idPre) {
        this.idPre = idPre;
    }


    public int getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(int korisnik) {
        this.korisnik = korisnik;
    }

    public int getPaket() {
        return paket;
    }

    public void setPaket(int paket) {
        this.paket = paket;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPre != null ? idPre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pretplata)) {
            return false;
        }
        Pretplata other = (Pretplata) object;
        if ((this.idPre == null && other.idPre != null) || (this.idPre != null && !this.idPre.equals(other.idPre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Pretplata[ idPre=" + idPre + " ]";
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

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }
    
}
