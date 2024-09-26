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
public class Gledanje implements Serializable {

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
    @Column(name = "PocetniSekund")
    private int pocetniSekund;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SekundiOdgledano")
    private int sekundiOdgledano;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdGle")
    private Integer idGle;

    private int korisnik;

    private int video;

    public Gledanje() {
    }

    public Gledanje(Integer idGle) {
        this.idGle = idGle;
    }

    public Gledanje(int video,int korisnik,int datum, int vreme, int pocetniSekund, int sekundiOdgledano) {

        this.video=video;
        this.korisnik=korisnik;
        this.datum = datum;
        this.vreme = vreme;
        this.pocetniSekund = pocetniSekund;
        this.sekundiOdgledano = sekundiOdgledano;
    }

    public Integer getIdGle() {
        return idGle;
    }

    public void setIdGle(Integer idGle) {
        this.idGle = idGle;
    }


    public int getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(int korisnik) {
        this.korisnik = korisnik;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGle != null ? idGle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gledanje)) {
            return false;
        }
        Gledanje other = (Gledanje) object;
        if ((this.idGle == null && other.idGle != null) || (this.idGle != null && !this.idGle.equals(other.idGle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Gledanje[ idGle=" + idGle + " ]";
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

    public int getPocetniSekund() {
        return pocetniSekund;
    }

    public void setPocetniSekund(int pocetniSekund) {
        this.pocetniSekund = pocetniSekund;
    }

    public int getSekundiOdgledano() {
        return sekundiOdgledano;
    }

    public void setSekundiOdgledano(int sekundiOdgledano) {
        this.sekundiOdgledano = sekundiOdgledano;
    }
    
}
