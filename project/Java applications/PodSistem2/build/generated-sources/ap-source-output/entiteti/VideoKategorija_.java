package entiteti;

import entiteti.Kategorija;
import entiteti.Video;
import entiteti.VideoKategorijaPK;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-02-10T03:01:37")
@StaticMetamodel(VideoKategorija.class)
public class VideoKategorija_ { 

    public static volatile SingularAttribute<VideoKategorija, String> polje;
    public static volatile SingularAttribute<VideoKategorija, Video> video;
    public static volatile SingularAttribute<VideoKategorija, VideoKategorijaPK> videoKategorijaPK;
    public static volatile SingularAttribute<VideoKategorija, Kategorija> kategorija;

}