/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Petar
 */
@Embeddable
public class VideoKategorijaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "IdVid")
    private int idVid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKat")
    private int idKat;

    public VideoKategorijaPK() {
    }

    public VideoKategorijaPK(int idVid, int idKat) {
        this.idVid = idVid;
        this.idKat = idKat;
    }

    public int getIdVid() {
        return idVid;
    }

    public void setIdVid(int idVid) {
        this.idVid = idVid;
    }

    public int getIdKat() {
        return idKat;
    }

    public void setIdKat(int idKat) {
        this.idKat = idKat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idVid;
        hash += (int) idKat;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VideoKategorijaPK)) {
            return false;
        }
        VideoKategorijaPK other = (VideoKategorijaPK) object;
        if (this.idVid != other.idVid) {
            return false;
        }
        if (this.idKat != other.idKat) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.VideoKategorijaPK[ idVid=" + idVid + ", idKat=" + idKat + " ]";
    }
    
}
