package entiteti;

import entiteti.Gledanje;
import entiteti.Ocena;
import entiteti.Pretplata;
import entiteti.Video;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-02-10T02:57:08")
@StaticMetamodel(Korisnik.class)
public class Korisnik_ { 

    public static volatile SingularAttribute<Korisnik, String> ime;
    public static volatile SingularAttribute<Korisnik, Integer> idKor;
    public static volatile SingularAttribute<Korisnik, Integer> godiste;
    public static volatile SingularAttribute<Korisnik, Integer> mesto;
    public static volatile CollectionAttribute<Korisnik, Video> videoCollection;
    public static volatile SingularAttribute<Korisnik, String> pol;
    public static volatile SingularAttribute<Korisnik, String> email;
    public static volatile CollectionAttribute<Korisnik, Pretplata> pretplataCollection;
    public static volatile CollectionAttribute<Korisnik, Gledanje> gledanjeCollection;
    public static volatile CollectionAttribute<Korisnik, Ocena> ocenaCollection;

}