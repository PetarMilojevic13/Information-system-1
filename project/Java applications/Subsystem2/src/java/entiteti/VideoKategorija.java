/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Petar
 */
@Entity
@Table(name = "video_kategorija")
@NamedQueries({
    @NamedQuery(name = "VideoKategorija.findAll", query = "SELECT v FROM VideoKategorija v"),
    @NamedQuery(name = "VideoKategorija.findByIdVid", query = "SELECT v FROM VideoKategorija v WHERE v.videoKategorijaPK.idVid = :idVid"),
    @NamedQuery(name = "VideoKategorija.findByIdKat", query = "SELECT v FROM VideoKategorija v WHERE v.videoKategorijaPK.idKat = :idKat"),
    @NamedQuery(name = "VideoKategorija.findByPolje", query = "SELECT v FROM VideoKategorija v WHERE v.polje = :polje")})
public class VideoKategorija implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Polje")
    private String polje;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VideoKategorijaPK videoKategorijaPK;
    @JoinColumn(name = "IdKat", referencedColumnName = "IdKat", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Kategorija kategorija;
    @JoinColumn(name = "IdVid", referencedColumnName = "IdVid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Video video;

    public VideoKategorija() {
    }

    public VideoKategorija(VideoKategorijaPK videoKategorijaPK) {
        this.videoKategorijaPK = videoKategorijaPK;
    }

    public VideoKategorija(VideoKategorijaPK videoKategorijaPK, String polje) {
        this.videoKategorijaPK = videoKategorijaPK;
        this.polje = polje;
    }

    public VideoKategorija(int idVid, int idKat) {
        this.videoKategorijaPK = new VideoKategorijaPK(idVid, idKat);
    }

    public VideoKategorijaPK getVideoKategorijaPK() {
        return videoKategorijaPK;
    }

    public void setVideoKategorijaPK(VideoKategorijaPK videoKategorijaPK) {
        this.videoKategorijaPK = videoKategorijaPK;
    }


    public Kategorija getKategorija() {
        return kategorija;
    }

    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
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
        hash += (videoKategorijaPK != null ? videoKategorijaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VideoKategorija)) {
            return false;
        }
        VideoKategorija other = (VideoKategorija) object;
        if ((this.videoKategorijaPK == null && other.videoKategorijaPK != null) || (this.videoKategorijaPK != null && !this.videoKategorijaPK.equals(other.videoKategorijaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.VideoKategorija[ videoKategorijaPK=" + videoKategorijaPK + " ]";
    }

    public String getPolje() {
        return polje;
    }

    public void setPolje(String polje) {
        this.polje = polje;
    }
    
}
