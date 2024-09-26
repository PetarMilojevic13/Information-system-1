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
import resources.Gledanje;
import resources.Mesto;
import resources.Paket;
import resources.Pretplata;

/**
 *
 * @author Petar
 */


@Path("gledanje")
public class Gledanja implements Serializable {

    
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
    @Path("{IdVid}/{IdKor}/{datum}/{vreme}/{pocetnisekund}/{trajanjesekunde}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response createGledanje(
          @PathParam("IdVid") int IdVid,
          @PathParam("IdKor") int IdKor,
          @PathParam("datum") int datum,
          @PathParam("vreme") int vreme,
          @PathParam("pocetnisekund") int pocetnisekund,
          @PathParam("trajanjesekunde") int trajanjesekunde)
    {
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(12);
        poruka.setBroj1(IdVid);
        poruka.setBroj2(IdKor);
       
        
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
        
        poruka.setBroj9(pocetnisekund);
        poruka.setBroj10(trajanjesekunde);
        
        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem3Primalac, objPoruka);
        
        Message message = consumer.receive();
        
        ObjectMessage obj = (ObjectMessage)message;
        Poruka poruka_primljena;
        try {
            poruka_primljena = (Poruka)(obj.getObject());
            
            if (poruka_primljena.getBroj1()<0)
            {
                
                if (poruka_primljena.getBroj1()==-1)
                {
                    String res = "Neuspesno pravljenje gledanja,jer video ili korisnik ne postoje\n";
                    return Response.status(Response.Status.OK).entity(res).build();
                }
                
                if (poruka_primljena.getBroj1()==-2)
                {
                    String res = "Ne postoji aktivna pretplata za korisnika za dati datum i vreme\n";
                    return Response.status(Response.Status.OK).entity(res).build();
                }

            }
            
            
            else
            {
                Gledanje gledanje = new Gledanje(IdVid,IdKor,datum,vreme,pocetnisekund,trajanjesekunde);
                gledanje.setIdGle(poruka_primljena.getBroj1());

                String res = "IdGle|Video|Korisnik|Datum|Vreme|PocetniSekund|SekundiOdgledano\n";

                String datums=Integer.toString(gledanje.getDatum());
                String vremes=Integer.toString(gledanje.getVreme());


                StringBuffer datumBuffer = new StringBuffer(datums);
                StringBuffer vremeBuffer = new StringBuffer(vremes);

                datumBuffer.insert(4, "-");
                datumBuffer.insert(7, "-");

                if (gledanje.getVreme()<100000)
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




                res+=gledanje.getIdGle()+"|"+gledanje.getVideo()+"|"+gledanje.getKorisnik()+"|"+datumBuffer.toString()+"|"+vremeBuffer.toString()+
                        "|"+gledanje.getPocetniSekund()+"|"+gledanje.getSekundiOdgledano()+"\n";

                return Response.status(Response.Status.OK).entity(res).build();
            }
            

            
        } catch (JMSException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
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
      
        
        
        Poruka poruka = new Poruka(24);
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
                    return Response.status(Response.Status.OK).entity("Ne postoji nijedno gledanje").build();
                }
                
                String res="IdGle|Video|Korisnik|Datum|Vreme|PocetniSekund|SekundiOdgledano\n";
                
                for (int i = 0; i < broj_korisnika; i++) {
                    message = consumer.receive();
                    ObjectMessage obj_novo = (ObjectMessage)message;
                    poruka = (Poruka)(obj_novo.getObject());

                    
                    
                    res+=poruka.getBroj1()+"|";
                    res+=poruka.getBroj2()+"|";
                    res+=poruka.getBroj3()+"|";
                    res+=poruka.getPolje1()+"|";
                    res+=poruka.getPolje2()+"|";
                    res+=poruka.getBroj4()+"|";
                    res+=poruka.getBroj5()+"\n";
                }
                
                context.close();
                consumer.close();
                
                
                
                return Response.status(Response.Status.OK).entity(res).build();
                
                
                
            } catch (JMSException ex) {
                Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
            }
         
              
        }
        return null; 
    }

}
