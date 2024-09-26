package entiteti;

import entiteti.Korisnik;
import entiteti.Paket;
import java.sql.Time;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-02-10T02:57:08")
@StaticMetamodel(Pretplata.class)
public class Pretplata_ { 

    public static volatile SingularAttribute<Pretplata, Date> datum;
    public static volatile SingularAttribute<Pretplata, Time> vreme;
    public static volatile SingularAttribute<Pretplata, Integer> cena;
    public static volatile SingularAttribute<Pretplata, Integer> idPre;
    public static volatile SingularAttribute<Pretplata, Paket> paket;
    public static volatile SingularAttribute<Pretplata, Korisnik> korisnik;

}