/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entiteti;

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

/**
 *
 * @author Petar
 */
@Entity
@Table(name = "korisnik")
@NamedQueries({
    @NamedQuery(name = "Korisnik.findAll", query = "SELECT k FROM Korisnik k"),
    @NamedQuery(name = "Korisnik.findByIdKor", query = "SELECT k FROM Korisnik k WHERE k.idKor = :idKor"),
    @NamedQuery(name = "Korisnik.findByIme", query = "SELECT k FROM Korisnik k WHERE k.ime = :ime"),
    @NamedQuery(name = "Korisnik.findByEmail", query = "SELECT k FROM Korisnik k WHERE k.email = :email"),
    @NamedQuery(name = "Korisnik.findByGodiste", query = "SELECT k FROM Korisnik k WHERE k.godiste = :godiste"),
    @NamedQuery(name = "Korisnik.findByPol", query = "SELECT k FROM Korisnik k WHERE k.pol = :pol"),
    @NamedQuery(name = "Korisnik.findByMesto", query = "SELECT k FROM Korisnik k WHERE k.mesto = :mesto")})
public class Korisnik implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Ime")
    private String ime;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Godiste")
    private int godiste;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Pol")
    private String pol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Mesto")
    private int mesto;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdKor")
    private Integer idKor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "korisnik")
    private Collection<Pretplata> pretplataCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "korisnik")
    private Collection<Video> videoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "korisnik")
    private Collection<Gledanje> gledanjeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "korisnik1")
    private Collection<Ocena> ocenaCollection;

    public Korisnik() {
    }

    public Korisnik(Integer idKor) {
        this.idKor = idKor;
    }

    public Korisnik( String ime, String email, int godiste, String pol, int mesto) {
        this.ime = ime;
        this.email = email;
        this.godiste = godiste;
        this.pol = pol;
        this.mesto = mesto;
    }

    public Integer getIdKor() {
        return idKor;
    }

    public void setIdKor(Integer idKor) {
        this.idKor = idKor;
    }


    public Collection<Pretplata> getPretplataCollection() {
        return pretplataCollection;
    }

    public void setPretplataCollection(Collection<Pretplata> pretplataCollection) {
        this.pretplataCollection = pretplataCollection;
    }

    public Collection<Video> getVideoCollection() {
        return videoCollection;
    }

    public void setVideoCollection(Collection<Video> videoCollection) {
        this.videoCollection = videoCollection;
    }

    public Collection<Gledanje> getGledanjeCollection() {
        return gledanjeCollection;
    }

    public void setGledanjeCollection(Collection<Gledanje> gledanjeCollection) {
        this.gledanjeCollection = gledanjeCollection;
    }

    public Collection<Ocena> getOcenaCollection() {
        return ocenaCollection;
    }

    public void setOcenaCollection(Collection<Ocena> ocenaCollection) {
        this.ocenaCollection = ocenaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idKor != null ? idKor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Korisnik)) {
            return false;
        }
        Korisnik other = (Korisnik) object;
        if ((this.idKor == null && other.idKor != null) || (this.idKor != null && !this.idKor.equals(other.idKor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Korisnik[ idKor=" + idKor + " ]";
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGodiste() {
        return godiste;
    }

    public void setGodiste(int godiste) {
        this.godiste = godiste;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public int getMesto() {
        return mesto;
    }

    public void setMesto(int mesto) {
        this.mesto = mesto;
    }
    
}
