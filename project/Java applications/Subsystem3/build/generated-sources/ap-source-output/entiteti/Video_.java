package entiteti;

import entiteti.Gledanje;
import entiteti.Korisnik;
import entiteti.Ocena;
import java.sql.Time;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-02-10T02:57:08")
@StaticMetamodel(Video.class)
public class Video_ { 

    public static volatile SingularAttribute<Video, Date> datum;
    public static volatile SingularAttribute<Video, Integer> idVid;
    public static volatile SingularAttribute<Video, Time> vreme;
    public static volatile SingularAttribute<Video, Integer> trajanje;
    public static volatile SingularAttribute<Video, String> naziv;
    public static volatile CollectionAttribute<Video, Gledanje> gledanjeCollection;
    public static volatile SingularAttribute<Video, Korisnik> korisnik;
    public static volatile CollectionAttribute<Video, Ocena> ocenaCollection;

}