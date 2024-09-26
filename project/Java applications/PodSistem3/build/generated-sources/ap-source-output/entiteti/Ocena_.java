package entiteti;

import entiteti.Korisnik;
import entiteti.OcenaPK;
import entiteti.Video;
import java.sql.Time;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-02-10T02:57:08")
@StaticMetamodel(Ocena.class)
public class Ocena_ { 

    public static volatile SingularAttribute<Ocena, Date> datum;
    public static volatile SingularAttribute<Ocena, Korisnik> korisnik1;
    public static volatile SingularAttribute<Ocena, Video> video1;
    public static volatile SingularAttribute<Ocena, Time> vreme;
    public static volatile SingularAttribute<Ocena, OcenaPK> ocenaPK;
    public static volatile SingularAttribute<Ocena, Integer> ocena;

}