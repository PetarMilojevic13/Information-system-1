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
@Table(name = "pretplata")
@NamedQueries({
    @NamedQuery(name = "Pretplata.findAll", query = "SELECT p FROM Pretplata p"),
    @NamedQuery(name = "Pretplata.findByIdPre", query = "SELECT p FROM Pretplata p WHERE p.idPre = :idPre"),
    @NamedQuery(name = "Pretplata.findByDatum", query = "SELECT p FROM Pretplata p WHERE p.datum = :datum"),
    @NamedQuery(name = "Pretplata.findByVreme", query = "SELECT p FROM Pretplata p WHERE p.vreme = :vreme"),
    @NamedQuery(name = "Pretplata.findByCena", query = "SELECT p FROM Pretplata p WHERE p.cena = :cena")})
public class Pretplata implements Serializable {

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
    @Column(name = "Cena")
    private int cena;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdPre")
    private Integer idPre;
    @JoinColumn(name = "Korisnik", referencedColumnName = "IdKor")
    @ManyToOne(optional = false)
    private Korisnik korisnik;
    @JoinColumn(name = "Paket", referencedColumnName = "IdPak")
    @ManyToOne(optional = false)
    private Paket paket;

    public Pretplata() {
    }


    public Pretplata(Korisnik korisnik,Paket paket, Date datum, Time vreme, int cena) {
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


    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public Paket getPaket() {
        return paket;
    }

    public void setPaket(Paket paket) {
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

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }
    
}
