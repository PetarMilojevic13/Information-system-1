package entiteti;

import entiteti.Korisnik;
import entiteti.Video;
import java.sql.Time;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-02-10T02:57:08")
@StaticMetamodel(Gledanje.class)
public class Gledanje_ { 

    public static volatile SingularAttribute<Gledanje, Date> datum;
    public static volatile SingularAttribute<Gledanje, Integer> idGle;
    public static volatile SingularAttribute<Gledanje, Integer> sekundiOdgledano;
    public static volatile SingularAttribute<Gledanje, Time> vreme;
    public static volatile SingularAttribute<Gledanje, Integer> pocetniSekund;
    public static volatile SingularAttribute<Gledanje, Video> video;
    public static volatile SingularAttribute<Gledanje, Korisnik> korisnik;

}