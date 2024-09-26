/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entiteti;

import java.io.Serializable;
import java.sql.Time;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Petar
 */
@Entity
@Table(name = "video")
@NamedQueries({
    @NamedQuery(name = "Video.findAll", query = "SELECT v FROM Video v"),
    @NamedQuery(name = "Video.findByIdVid", query = "SELECT v FROM Video v WHERE v.idVid = :idVid"),
    @NamedQuery(name = "Video.findByNaziv", query = "SELECT v FROM Video v WHERE v.naziv = :naziv"),
    @NamedQuery(name = "Video.findByTrajanje", query = "SELECT v FROM Video v WHERE v.trajanje = :trajanje"),
    @NamedQuery(name = "Video.findByDatum", query = "SELECT v FROM Video v WHERE v.datum = :datum"),
    @NamedQuery(name = "Video.findByVreme", query = "SELECT v FROM Video v WHERE v.vreme = :vreme")})
public class Video implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Naziv")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Trajanje")
    private int trajanje;
    @Column(name = "Vreme")
    //@Temporal(TemporalType.TIME)
    private Time vreme;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdVid")
    private Integer idVid;
    @Column(name = "Datum")
    @Temporal(TemporalType.DATE)
    private Date datum;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "video")
    private Collection<VideoKategorija> videoKategorijaCollection;
    @JoinColumn(name = "Korisnik", referencedColumnName = "IdKor")
    @ManyToOne
    private Korisnik korisnik;

    public Video() {
    }

    public Video(Integer idVid) {
        this.idVid = idVid;
    }

    public Video(String naziv, int trajanje,Korisnik korisnik,Date datum,Time vreme) {

        this.naziv = naziv;
        this.trajanje = trajanje;
        this.korisnik=korisnik;
        this.datum=datum;
        this.vreme=vreme;
    }

    public Integer getIdVid() {
        return idVid;
    }

    public void setIdVid(Integer idVid) {
        this.idVid = idVid;
    }


    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }


    public Collection<VideoKategorija> getVideoKategorijaCollection() {
        return videoKategorijaCollection;
    }

    public void setVideoKategorijaCollection(Collection<VideoKategorija> videoKategorijaCollection) {
        this.videoKategorijaCollection = videoKategorijaCollection;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVid != null ? idVid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Video)) {
            return false;
        }
        Video other = (Video) object;
        if ((this.idVid == null && other.idVid != null) || (this.idVid != null && !this.idVid.equals(other.idVid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Video[ idVid=" + idVid + " ]";
    }


    public Time getVreme() {
        return vreme;
    }

    public void setVreme(Time vreme) {
        this.vreme = vreme;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }
    
}
