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
import resources.Mesto;
import resources.Paket;

/**
 *
 * @author Petar
 */


@Path("paket")
public class Paketi implements Serializable {

    
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
    @Path("{cena}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response createPaket(
          @PathParam("cena") int cena)
    {
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(9);
        poruka.setBroj1(cena);

        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem3Primalac, objPoruka);
        
        Message message = consumer.receive();
        
        ObjectMessage obj = (ObjectMessage)message;
        Poruka poruka_primljena;
        try {
            poruka_primljena = (Poruka)(obj.getObject());
            int IdPak = poruka_primljena.getBroj1();
            
            Paket paket = new Paket(cena);
            paket.setCena(cena);
            paket.setIdPak(IdPak);
            
            String res = "IdPak|Cena\n";
            res+=paket.getIdPak()+"|"+paket.getCena()+"\n";
            
            return Response.status(Response.Status.OK).entity(res).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;  
    }
    
    
    
    @PUT
    @Path("{IdPak}/{cena}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response promeniCenu(
          @PathParam("IdPak") int IdPak,
          @PathParam("cena") int cena)
    {
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(10);
        poruka.setBroj1(IdPak);
        poruka.setBroj2(cena);

        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem3Primalac, objPoruka);
        
        Message message = consumer.receive();
        
        ObjectMessage obj = (ObjectMessage)message;
        Poruka poruka_primljena;
        try {
            poruka_primljena = (Poruka)(obj.getObject());
            
            if (poruka_primljena.getBroj1()<0)
            {
               return Response.status(Response.Status.OK).entity("Paket ne postoji\n").build(); 
            }

            
            Paket paket = new Paket(cena);
            paket.setCena(cena);
            paket.setIdPak(IdPak);
            
            String res = "IdPak|Cena\n";
            res+=paket.getIdPak()+"|"+paket.getCena()+"\n";
            
            return Response.status(Response.Status.OK).entity(res).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;  
    }
    
    
    @GET
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvPakete()
    {
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(22);


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
                    return Response.status(Response.Status.OK).entity("Ne postoji nijedan paket").build();
                }
                
                List<Paket> lista = new ArrayList();
                
                
                String res = "IdPak|Cena\n";
                
                for (int i = 0; i < broj_korisnika; i++) {
                    message = consumer.receive();
                    ObjectMessage obj_novo = (ObjectMessage)message;
                    poruka = (Poruka)(obj_novo.getObject());
                    
                    
                    
                    Paket paket = new Paket(poruka.getBroj2());
                    
                    paket.setIdPak(poruka.getBroj1());
                    
                    res+=paket.getIdPak()+"|"+paket.getCena()+"\n";
                    
                    lista.add(paket);
                }
                
                return Response.status(Response.Status.OK).entity(res).build();
                
                
                
            } catch (JMSException ex) {
                Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
            }
         
              
        }
        return null;   
    }
}
