package entiteti;

import entiteti.Korisnik;
import entiteti.VideoKategorija;
import java.sql.Time;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-02-10T03:01:37")
@StaticMetamodel(Video.class)
public class Video_ { 

    public static volatile SingularAttribute<Video, Integer> idVid;
    public static volatile SingularAttribute<Video, Date> datum;
    public static volatile CollectionAttribute<Video, VideoKategorija> videoKategorijaCollection;
    public static volatile SingularAttribute<Video, Time> vreme;
    public static volatile SingularAttribute<Video, Integer> trajanje;
    public static volatile SingularAttribute<Video, String> naziv;
    public static volatile SingularAttribute<Video, Korisnik> korisnik;

}