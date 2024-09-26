package entiteti;

import entiteti.Pretplata;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-02-10T02:57:08")
@StaticMetamodel(Paket.class)
public class Paket_ { 

    public static volatile SingularAttribute<Paket, Integer> idPak;
    public static volatile SingularAttribute<Paket, Integer> cena;
    public static volatile CollectionAttribute<Paket, Pretplata> pretplataCollection;

}