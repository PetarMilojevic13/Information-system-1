/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package endpoints;

import resources.Korisnik;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import poruka.Poruka;
import resources.Mesto;
import resources.Ocena;
import resources.Video;

/**
 *
 * @author Petar
 */


@Path("ocena")
public class Ocene implements Serializable {

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
    @Path("{IdKor}/{IdVid}/{ocena}/{datum}/{vreme}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response createOcena(
            @PathParam("IdKor") int IdKor,
            @PathParam("IdVid") int IdVid,
            @PathParam("ocena") int ocena,
            @PathParam("datum") int datum,
            @PathParam("vreme") int vreme)
    {
        if (ocena<=0 || ocena>5)
        {
            return Response.status(Response.Status.OK).entity("Nije ispravno uneta ocena").build();
        }
        

        
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(13);

        poruka.setBroj1(IdKor);
        poruka.setBroj2(IdVid);
        poruka.setBroj3(ocena);
        
        
        
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
        

        
        poruka.setBroj4(dan);
        poruka.setBroj5(mesec);
        poruka.setBroj6(godina);
        
        poruka.setBroj7(sat);
        poruka.setBroj8(minut);
        poruka.setBroj9(sekund);
        
        
 
        
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem3Primalac, objPoruka);
        
        Message message = consumer.receive();
        
        ObjectMessage obj = (ObjectMessage)message;
       
        try {

                poruka = (Poruka)(obj.getObject());
                
                if (poruka.getBroj1()<0)
                {
                    if (poruka.getBroj1()==-1)
                        return Response.status(Response.Status.OK).entity("Ocena za IdKor=" + IdKor+" i video IdVid" +IdVid+" vec postoji").build();
                    if (poruka.getBroj1()==-2)
                        return Response.status(Response.Status.OK).entity("Korisnik IdKor=" + IdKor+" ili video IdVid=" +IdVid+" ne postoji").build();
                }
                else
                {
                    Ocena ocenaObjekat = new Ocena(IdKor, IdVid, ocena, datum, vreme);
                    
                    String res = "Korisnik|Video|Ocena|Datum|Vreme\n";
                    
                    String datums=Integer.toString(ocenaObjekat.getDatum());
                    String vremes=Integer.toString(ocenaObjekat.getVreme());
                
                
                    StringBuffer datumBuffer = new StringBuffer(datums);
                    StringBuffer vremeBuffer = new StringBuffer(vremes);

                    datumBuffer.insert(4, "-");
                    datumBuffer.insert(7, "-");

                    if (ocenaObjekat.getVreme()<100000)
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
                    
                    res+=ocenaObjekat.getKorisnik1()+"|"+ocenaObjekat.getVideo1()+"|"+ocenaObjekat.getOcena()+"|"+datumBuffer.toString()+"|"
                            +vremeBuffer.toString()+"\n";
                    
                    
                    
                    
                    return Response.status(Response.Status.OK).entity(res).build();
                }
                

            
                
       
            
        } catch (JMSException ex) {
            Logger.getLogger(Video.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;  
        
    }
    
    
    
    
   
    @PUT
    @Path("{IdKor}/{IdVid}/{ocena}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response promeniOcenu(
            @PathParam("IdKor") int IdKor,
            @PathParam("IdVid") int IdVid,
            @PathParam("ocena") int ocena)
    {
        if (ocena<=0 || ocena>5)
        {
            return Response.status(Response.Status.OK).entity("Nije ispravno uneta ocena").build();
        }
        

        
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(14);

        poruka.setBroj1(IdKor);
        poruka.setBroj2(IdVid);
        poruka.setBroj3(ocena);
        
           
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem3Primalac, objPoruka);
        
        Message message = consumer.receive();
        
        ObjectMessage obj = (ObjectMessage)message;
       
        try {

                poruka = (Poruka)(obj.getObject());
                
                if (poruka.getBroj1()<0)
                {
                    return Response.status(Response.Status.OK).entity("Ocena za IdKor=" + IdKor+" i video IdVid" +IdVid+" ne postoji").build();
                }
                else
                {
                    Ocena ocenaObjekat = new Ocena(IdKor, IdVid, ocena, poruka.getBroj2(), poruka.getBroj3());             
                    
                     String res = "Korisnik|Video|Ocena|Datum|Vreme\n";
                    
                    String datums=Integer.toString(ocenaObjekat.getDatum());
                    String vremes=Integer.toString(ocenaObjekat.getVreme());
                
                
                    StringBuffer datumBuffer = new StringBuffer(datums);
                    StringBuffer vremeBuffer = new StringBuffer(vremes);

                    datumBuffer.insert(4, "-");
                    datumBuffer.insert(7, "-");

                    if (ocenaObjekat.getVreme()<100000)
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
                    
                    res+=ocenaObjekat.getKorisnik1()+"|"+ocenaObjekat.getVideo1()+"|"+ocenaObjekat.getOcena()+"|"+datumBuffer.toString()+"|"
                            +vremeBuffer.toString()+"\n";
                    
                    
                    
                    
                    return Response.status(Response.Status.OK).entity(res).build();
                }
                

            
                
       
            
        } catch (JMSException ex) {
            Logger.getLogger(Video.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;  
        
    }
    
    
    
       
    @DELETE
    @Path("{IdKor}/{IdVid}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response obrisiOcenu(
            @PathParam("IdKor") int IdKor,
            @PathParam("IdVid") int IdVid)
    {

        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(15);

        poruka.setBroj1(IdKor);
        poruka.setBroj2(IdVid);

        
           
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem3Primalac, objPoruka);
        
        Message message = consumer.receive();
        
        ObjectMessage obj = (ObjectMessage)message;
       
        try {

                poruka = (Poruka)(obj.getObject());
                
                if (poruka.getBroj1()<0)
                {
                    return Response.status(Response.Status.OK).entity("Ocena za IdKor=" + IdKor+" i video IdVid=" +IdVid+" ne postoji").build();
                }
                else
                {
                    
                    return Response.status(Response.Status.OK).entity("Ocena za IdKor=" + IdKor+" i video IdVid" +IdVid+" uspesno obrisana").build();
                }
                

            
                
       
            
        } catch (JMSException ex) {
            Logger.getLogger(Video.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;  
        
    }
    
    
    
    
    
    
    
    
   
    @GET
    @Path("{IdVid}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvGledanja(
        @PathParam("IdVid") int IdVid)
    {
        //DOVRSI
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(25);
        poruka.setBroj1(IdVid);

        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem3Primalac, objPoruka);
        
       
        Message message = consumer.receive();
        if (message instanceof ObjectMessage)
        {
              ObjectMessage obj = (ObjectMessage)message;

            try {
                poruka = (Poruka)(obj.getObject());
                int broj_korisnika = poruka.getBroj1();
                
                if (broj_korisnika==0)
                {
                    return Response.status(Response.Status.OK).entity("Ne postoji nijedna ocena").build();
                }
                
                String res="Korisnik|Video|Ocena|Datum|Vreme\n";
                
                for (int i = 0; i < broj_korisnika; i++) {
                    message = consumer.receive();
                    ObjectMessage obj_novo = (ObjectMessage)message;
                    poruka = (Poruka)(obj_novo.getObject());

                    
                    
                    res+=poruka.getBroj1()+"|";
                    res+=poruka.getBroj2()+"|";
                    res+=poruka.getBroj3()+"|";
                    res+=poruka.getPolje1()+"|";
                    res+=poruka.getPolje2()+"\n";

                }
                
                return Response.status(Response.Status.OK).entity(res).build();
                
                
                
            } catch (JMSException ex) {
                Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
            }
         
              
        }
        return null; 
    }
    
    
    
    
    
    
}
