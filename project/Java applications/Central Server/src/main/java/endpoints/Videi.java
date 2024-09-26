package endpoints;

import java.sql.Date;
import java.sql.Time;
import resources.Korisnik;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import poruka.Poruka;
import resources.Kategorija;
import resources.Mesto;
import resources.Video;

/**
 *
 * @author 
 */
@Path("video")
public class Videi {
    
    @Resource(lookup = "myConnFactory")
    private  ConnectionFactory myConnFactory;
    
    @Resource(lookup = "PodSistem1PrimalacQueue")
    private  javax.jms.Queue QueuePodsistem1Primalac;
    
    @Resource(lookup = "PodSistem2PrimalacQueue")
    private  javax.jms.Queue QueuePodsistem2Primalac;
    
    @Resource(lookup = "PodSistem3PrimalacQueue")
    private  javax.jms.Queue QueuePodsistem3Primalac;
    
    @Resource(lookup = "CentralniServerPrimalacQueue")
    private  javax.jms.Queue QueueRestServer;

    private  JMSContext context;
    private  JMSProducer producer;
    private  JMSConsumer consumer;


    
    @POST
    @Path("{naziv}/{trajanje}/{korisnik}/{datum}/{vreme}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response createVideo(
            @PathParam("naziv") String naziv,
            @PathParam("trajanje") int trajanje,
            @PathParam("korisnik") int IdKor,
            @PathParam("datum") int datum,
            @PathParam("vreme") int vreme)
    {
        
        //DOPUNITI PROVERITI DA LI RADI
        
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(6);
        poruka.setPolje1(naziv);
        poruka.setBroj1(trajanje);
        poruka.setBroj2(IdKor);
        int brojac_tacke=0;
        int index=0;
        int godina;
        int dan;
        int mesec;
        int sat;
        int minut;
        int sekund;
        
        // yyyy-mm-dd
        
        dan=datum%100;
        godina=datum/10000;
        mesec=(datum%10000)/100;
        
        sat=vreme/10000;
        sekund = vreme % 100;
        minut = (vreme%10000)/100;
        
        
        // hh.mm.ss
        

        
        poruka.setBroj3(dan);
        poruka.setBroj4(mesec);
        poruka.setBroj5(godina);
        
        poruka.setBroj6(sat);
        poruka.setBroj7(minut);
        poruka.setBroj8(sekund);

 
        
        
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem2Primalac, objPoruka);
        
        Message message = consumer.receive();
        
        ObjectMessage obj = (ObjectMessage)message;
       
        try {
            String datums = Integer.toString(datum);
            String vremes = Integer.toString(vreme);
                poruka = (Poruka)(obj.getObject());
                
               if (poruka.getBroj1()<0)
               {
                   return Response.status(Response.Status.OK).entity("Korisnik ne postoji\n").build();
               }
                
                
                Video video = new Video(naziv,trajanje,IdKor,datum,vreme);
                video.setIdVid(poruka.getBroj1());
                
            StringBuffer datumBuffer = new StringBuffer(datums);
            StringBuffer vremeBuffer = new StringBuffer(vremes);
            
            datumBuffer.insert(4, "-");
            datumBuffer.insert(7, "-");
            
                if (video.getVreme()<100000)
                {
                    vremeBuffer.insert(1, ":");
                    vremeBuffer.insert(4, ":");
                    vremeBuffer.insert(0,"0");
                }
                else
                {
                    vremeBuffer.insert(2, ":");
                    vremeBuffer.insert(5, ":");
                }
            
            String res= "IdVid|Naziv|Trajanje|Korisnik|Datum|Vreme\n";
            
            res+=video.getIdVid()+"|"+video.getNaziv()+"|"+video.getTrajanje()+"|"+video.getKorisnik()+"|"+datumBuffer.toString()+"|"+vremeBuffer.toString()+"\n";
            
                
            return Response.status(Response.Status.OK).entity(res).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Video.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;  
    }
    
    
    @PUT
    @Path("{IdVid}/{naziv}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response promeniNaziv(
            @PathParam("IdVid") int IdVid,
            @PathParam("naziv") String naziv)
    {
        
        //DOPUNITI PROVERITI DA LI RADI
        
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(7);
        poruka.setPolje1(naziv);
        poruka.setBroj1(IdVid);

 
        
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem2Primalac, objPoruka);
        
        Message message = consumer.receive();
        
        ObjectMessage obj = (ObjectMessage)message;
       
        try {

                poruka = (Poruka)(obj.getObject());
                
                
                if (poruka.getBroj1()<0)
                {
                    return Response.status(Response.Status.OK).entity("Video ne postoji\n").build();
                }
                
                Video video = new Video(naziv,poruka.getBroj2(),poruka.getBroj3(),poruka.getBroj4(),poruka.getBroj5());
                video.setIdVid(poruka.getBroj1());
            
                String datums=Integer.toString(video.getDatum());
                String vremes=Integer.toString(video.getVreme());
                
                
                StringBuffer datumBuffer = new StringBuffer(datums);
                StringBuffer vremeBuffer = new StringBuffer(vremes);

                datumBuffer.insert(4, "-");
                datumBuffer.insert(7, "-");
                
                if (video.getVreme()<100000)
                {
                    vremeBuffer.insert(1, ":");
                    vremeBuffer.insert(4, ":");
                     vremeBuffer.insert(0,"0");
                }
                else
                {
                    vremeBuffer.insert(2, ":");
                    vremeBuffer.insert(5, ":");
                }
                    



                String res= "IdVid|Naziv|Trajanje|Korisnik|Datum|Vreme\n";

                res+=video.getIdVid()+"|"+video.getNaziv()+"|"+video.getTrajanje()+"|"+video.getKorisnik()+"|"+datumBuffer.toString()+"|"+vremeBuffer.toString()+"\n";
                
                
            return Response.status(Response.Status.OK).entity(res).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Video.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;  
    }
    
    @POST
    @Path("/dodajkategoriju/{IdVid}/{IdKat}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dodajKategorijuVideu(
             @PathParam("IdVid") int IdVid,
            @PathParam("IdKat") int IdKat)
    {
        

//        //DOPUNITI PROVERITI DA LI RADI
        
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(8);
        
        poruka.setBroj1(IdVid);
        poruka.setBroj2(IdKat);
 
        
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem2Primalac, objPoruka);
        
        Message message = consumer.receive();
        
        ObjectMessage obj = (ObjectMessage)message;
       
        try {

                poruka = (Poruka)(obj.getObject());
                if (poruka.getBroj_zahteva()>0)
                {
                    String res = "Uspesno dodata kategorija IdKat="+Integer.toString(IdKat) + " videu IdVid="+Integer.toString(IdVid);
            
                
                    return Response.status(Response.Status.OK).entity(res).build();
                }
                String res = "Neuspesno dodata kategorija IdKat="+Integer.toString(IdKat) + " videu IdVid="+Integer.toString(IdVid);
            
                
                    return Response.status(Response.Status.OK).entity(res).build();
               
            
        } catch (JMSException ex) {
            Logger.getLogger(Video.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;  
    }
    
    @DELETE
    @Path("{IdVid}/{IdKor}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response obrisiVideo(
             @PathParam("IdVid") int IdVid,
            @PathParam("IdKor") int IdKor)
    {
        

//        //DOPUNITI PROVERITI DA LI RADI
        
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(16);
        
        poruka.setBroj1(IdVid);
        poruka.setBroj2(IdKor);
 
        
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem2Primalac, objPoruka);
        
        Message message = consumer.receive();
        
        ObjectMessage obj = (ObjectMessage)message;
       
        try {

                poruka = (Poruka)(obj.getObject());
                if (poruka.getBroj1()>0)
                {
                    String res = "Uspesno obrisan video IdVid="+IdVid;
            
                
                    return Response.status(Response.Status.OK).entity(res).build();
                }
                if (poruka.getBroj1()==-1)
                {
                    String res = "Neuspesno obrisan video IdVid="+IdVid + " od strane korisnika koji nije vlasnik!\n";
            
                
                    return Response.status(Response.Status.OK).entity(res).build();
                }
                String res = "Neuspesno obrisan video IdVid="+IdVid + " jer video ili korisnik ne postoje\n";
            
                
                return Response.status(Response.Status.OK).entity(res).build();
               
            
        } catch (JMSException ex) {
            Logger.getLogger(Video.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;  
    }
    
    
    
    
    
    @GET
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiVidee()
    {
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(20);

        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem2Primalac, objPoruka);
        
        Message message = consumer.receive();
        if (message instanceof ObjectMessage)
        {
              ObjectMessage obj = (ObjectMessage)message;

            try {
                poruka = (Poruka)(obj.getObject());
                int broj_korisnika = poruka.getBroj1();
                
                if (broj_korisnika==0)
                {
                    return Response.status(Response.Status.OK).entity("Ne postoji nijedan video").build();
                }
                
                List<Video> lista = new ArrayList();
                
                String res = "IdVid|Naziv|Trajanje|Korisnik|Datum|Vreme\n";
                
                for (int i = 0; i < broj_korisnika; i++) {
                    message = consumer.receive();
                    ObjectMessage obj_novo = (ObjectMessage)message;
                    poruka = (Poruka)(obj_novo.getObject());
                    
                    
                    
                    Video video = new Video(poruka.getPolje1(),poruka.getBroj2(),poruka.getBroj3(),poruka.getBroj4(),poruka.getBroj5());
                    
                    
                    video.setIdVid(poruka.getBroj1());
                    
                    
                    String datums=Integer.toString(video.getDatum());
                    String vremes=Integer.toString(video.getVreme());


                    StringBuffer datumBuffer = new StringBuffer(datums);
                    StringBuffer vremeBuffer = new StringBuffer(vremes);

                    datumBuffer.insert(4, "-");
                    datumBuffer.insert(7, "-");

                    if (video.getVreme()<100000)
                    {
                        vremeBuffer.insert(1, ":");
                        vremeBuffer.insert(4, ":");
                         vremeBuffer.insert(0,"0");
                    }
                    else
                    {
                        vremeBuffer.insert(2, ":");
                        vremeBuffer.insert(5, ":");
                    }

                    res+=video.getIdVid()+"|"+video.getNaziv()+"|"+video.getTrajanje()+"|"+video.getKorisnik()+"|"+datumBuffer.toString()+"|"+vremeBuffer.toString()+"\n";
                    
                    
                    
                    
                    lista.add(video);
                }
                
                return Response.status(Response.Status.OK).entity(res).build();
                
                
                
            } catch (JMSException ex) {
                Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
            }
         
              
        }
        return null; 
    }
    
    
    @GET
    @Path("/dohvatiKategorije/{IdVid}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiKategorijeVidea(
            @PathParam("IdVid") int IdVid)
    {
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(21);
        poruka.setBroj1(IdVid);

        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem2Primalac, objPoruka);
        
        Message message = consumer.receive();
        if (message instanceof ObjectMessage)
        {
              ObjectMessage obj = (ObjectMessage)message;

            try {
                poruka = (Poruka)(obj.getObject());
                int broj_korisnika = poruka.getBroj1();
                
                if (broj_korisnika==0)
                {
                    return Response.status(Response.Status.OK).entity("Ne postoji nijedna kategorija").build();
                }
                
                List<Kategorija> lista = new ArrayList();
                String res = "IdKat|Naziv\n";
                
                for (int i = 0; i < broj_korisnika; i++) {
                    message = consumer.receive();
                    ObjectMessage obj_novo = (ObjectMessage)message;
                    poruka = (Poruka)(obj_novo.getObject());
                    
                    
                    
                    Kategorija kategorija = new Kategorija(poruka.getPolje1());
                    kategorija.setNaziv(poruka.getPolje1());
                    
                    
                    kategorija.setIdKat(poruka.getBroj1());
                    
                    res+=kategorija.getIdKat();
                    res+="|";
                    res+=kategorija.getNaziv();
                    res+="\n";
                    
                    
                    lista.add(kategorija);
                }
                
                return Response.status(Response.Status.OK).entity(res).build();
                //return Response.status(Response.Status.OK).entity(res).build();
                
                
            } catch (JMSException ex) {
                Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
            }
         
              
        }
        return null; 
    }
    
    
    
    
    }
    
    
    
    
