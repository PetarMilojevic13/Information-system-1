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

/**
 *
 * @author Petar
 */
@Entity
@Table(name = "ocena")
@NamedQueries({
    @NamedQuery(name = "Ocena.findAll", query = "SELECT o FROM Ocena o"),
    @NamedQuery(name = "Ocena.findByKorisnik", query = "SELECT o FROM Ocena o WHERE o.ocenaPK.korisnik = :korisnik"),
    @NamedQuery(name = "Ocena.findByVideo", query = "SELECT o FROM Ocena o WHERE o.ocenaPK.video = :video"),
    @NamedQuery(name = "Ocena.findByOcena", query = "SELECT o FROM Ocena o WHERE o.ocena = :ocena"),
    @NamedQuery(name = "Ocena.findByDatum", query = "SELECT o FROM Ocena o WHERE o.datum = :datum"),
    @NamedQuery(name = "Ocena.findByVreme", query = "SELECT o FROM Ocena o WHERE o.vreme = :vreme")})
public class Ocena implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Ocena")
    private int ocena;
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

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OcenaPK ocenaPK;
    @JoinColumn(name = "Korisnik", referencedColumnName = "IdKor", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Korisnik korisnik1;
    @JoinColumn(name = "Video", referencedColumnName = "IdVid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Video video1;

    public Ocena() {
    }

    public Ocena(OcenaPK ocenaPK) {
        this.ocenaPK = ocenaPK;
    }

    public Ocena(OcenaPK ocenaPK, int ocena, Date datum, Time vreme) {
        this.ocenaPK = ocenaPK;
        this.ocena = ocena;
        this.datum = datum;
        this.vreme = vreme;
    }

    public Ocena(int korisnik, int video) {
        this.ocenaPK = new OcenaPK(korisnik, video);
    }

    public OcenaPK getOcenaPK() {
        return ocenaPK;
    }

    public void setOcenaPK(OcenaPK ocenaPK) {
        this.ocenaPK = ocenaPK;
    }


    public Korisnik getKorisnik1() {
        return korisnik1;
    }

    public void setKorisnik1(Korisnik korisnik1) {
        this.korisnik1 = korisnik1;
    }

    public Video getVideo1() {
        return video1;
    }

    public void setVideo1(Video video1) {
        this.video1 = video1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ocenaPK != null ? ocenaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ocena)) {
            return false;
        }
        Ocena other = (Ocena) object;
        if ((this.ocenaPK == null && other.ocenaPK != null) || (this.ocenaPK != null && !this.ocenaPK.equals(other.ocenaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Ocena[ ocenaPK=" + ocenaPK + " ]";
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
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
    
}
