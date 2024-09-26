/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entiteti;

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

/**
 *
 * @author Petar
 */
@Entity
@Table(name = "gledanje")
@NamedQueries({
    @NamedQuery(name = "Gledanje.findAll", query = "SELECT g FROM Gledanje g"),
    @NamedQuery(name = "Gledanje.findByIdGle", query = "SELECT g FROM Gledanje g WHERE g.idGle = :idGle"),
    @NamedQuery(name = "Gledanje.findByDatum", query = "SELECT g FROM Gledanje g WHERE g.datum = :datum"),
    @NamedQuery(name = "Gledanje.findByVreme", query = "SELECT g FROM Gledanje g WHERE g.vreme = :vreme"),
    @NamedQuery(name = "Gledanje.findByPocetniSekund", query = "SELECT g FROM Gledanje g WHERE g.pocetniSekund = :pocetniSekund"),
    @NamedQuery(name = "Gledanje.findBySekundiOdgledano", query = "SELECT g FROM Gledanje g WHERE g.sekundiOdgledano = :sekundiOdgledano")})
public class Gledanje implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Datum")
    @Temporal(TemporalType.DATE)
    private Date datum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Vreme")
    //@Temporal(TemporalType.TIME)
    private Time vreme;
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
    @JoinColumn(name = "Korisnik", referencedColumnName = "IdKor")
    @ManyToOne(optional = false)
    private Korisnik korisnik;
    @JoinColumn(name = "Video", referencedColumnName = "IdVid")
    @ManyToOne(optional = false)
    private Video video;

    public Gledanje() {
    }

    public Gledanje(Integer idGle) {
        this.idGle = idGle;
    }

    public Gledanje(Video video,Korisnik korisnik,Date datum, Time vreme, int pocetniSekund, int sekundiOdgledano) {

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


    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
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

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Time getVreme() {
        return vreme;
    }

    public void setVreme(Time vreme) {
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
