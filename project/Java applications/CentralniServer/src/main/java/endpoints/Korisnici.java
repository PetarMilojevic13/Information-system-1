package endpoints;

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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import poruka.Poruka;
import resources.Mesto;

/**
 *
 * @author 
 */
@Path("korisnik")
public class Korisnici {
    
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


    
    @GET
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response getKorisnici(){
        
        

        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(18);

        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem1Primalac, objPoruka);
        
       
        Message message = consumer.receive();
        if (message instanceof ObjectMessage)
        {
              ObjectMessage obj = (ObjectMessage)message;
              Poruka poruka_primljena;
            try {
                poruka_primljena = (Poruka)(obj.getObject());
                int broj_korisnika = poruka_primljena.getBroj1();
                
                if (broj_korisnika==0)
                {
                    return Response.status(Response.Status.NO_CONTENT).build();
                }
                
                List<Korisnik> lista = new ArrayList();
                  String res = "IdKor|Ime|Email|Godiste|Pol|Mesto\n";
                
                for (int i = 0; i < broj_korisnika; i++) {
                    message = consumer.receive();
                    ObjectMessage obj_novo = (ObjectMessage)message;
                    poruka_primljena = (Poruka)(obj_novo.getObject());
                    
                    
                    
                    
                    
                    Korisnik korisnik = new Korisnik(poruka_primljena.getPolje1(),poruka_primljena.getPolje2(),
                            poruka_primljena.getBroj2(),poruka_primljena.getPolje3(),poruka_primljena.getBroj3());
                    
                    korisnik.setIdKor(poruka_primljena.getBroj1());
                    res+=korisnik.getIdKor()+"|"+korisnik.getIme()+"|"+korisnik.getEmail()+"|"+korisnik.getGodiste()+"|"+korisnik.getPol()+"|"+korisnik.getMesto()+"\n";
                    
                    lista.add(korisnik);
                }
                
                return Response.status(Response.Status.OK).entity(res).build();
                
                
                
            } catch (JMSException ex) {
                Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
            }
         
              
        }
        return null;
    }
    
    @POST
    @Path("{ime}/{email}/{godiste}/{pol}/{mesto}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response createKorisnik(
            @PathParam("ime") String naziv,
            @PathParam("email") String email,
            @PathParam("godiste") int godiste,
            @PathParam("pol") String pol,
            @PathParam("mesto") int mesto)
    {
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(2);
        poruka.setPolje1(naziv);
        poruka.setPolje2(email);
        poruka.setPolje3(pol);
        poruka.setBroj1(godiste);
        poruka.setBroj2(mesto);

        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem1Primalac, objPoruka);
        
        Message message = consumer.receive();
        
        ObjectMessage obj = (ObjectMessage)message;
        Poruka poruka_primljena;
      
        try {
            poruka_primljena = (Poruka)(obj.getObject());
            
            if (poruka_primljena.getBroj1()<0)
            {
               return Response.status(Response.Status.OK).entity("Mesto ne postoji\n").build(); 
            }
            
            else
            {
                Korisnik korisnik = new Korisnik(poruka.getPolje1(),poruka.getPolje2(),godiste,poruka.getPolje3(),mesto);
                korisnik.setIdKor(poruka_primljena.getBroj1());
            
            
                String res = "IdKor|Ime|Email|Godiste|Pol|Mesto\n";
            
                res+=korisnik.getIdKor()+"|"+korisnik.getIme()+"|"+korisnik.getEmail()+"|"+korisnik.getGodiste()+"|"+korisnik.getPol()+"|"+korisnik.getMesto()+"\n";
            
                return Response.status(Response.Status.OK).entity(res).build(); 
            }
            

            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    
    @PUT
    @Path("{IdKor}/{email}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response promenaEmaila(
            @PathParam("IdKor") int IdKor,
            @PathParam("email") String email)

    {
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(3);

        poruka.setPolje1(email);
        poruka.setBroj1(IdKor);


        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem1Primalac, objPoruka);
        
        Message message = consumer.receive();
        
        ObjectMessage obj = (ObjectMessage)message;
  
      
        try {
            poruka = (Poruka)(obj.getObject());
            

            if (poruka.getBroj1()<0)
            {
                return Response.status(Response.Status.OK).entity("Korisnik ne postoji\n").build();
            }
            else
            {
                Korisnik korisnik = new Korisnik(poruka.getPolje1(),poruka.getPolje2(),poruka.getBroj2(),poruka.getPolje3(),poruka.getBroj3());
                korisnik.setIdKor(poruka.getBroj1());

                String res = "IdKor|Ime|Email|Godiste|Pol|Mesto\n";

                res+=korisnik.getIdKor()+"|"+korisnik.getIme()+"|"+korisnik.getEmail()+"|"+korisnik.getGodiste()+"|"+korisnik.getPol()+"|"+korisnik.getMesto()+"\n";

                return Response.status(Response.Status.OK).entity(res).build();
            }
            

            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    
    
    @PUT
    @Path("postaviMesto/{IdKor}/{mesto}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response promenaMesta(
            @PathParam("IdKor") int IdKor,
            @PathParam("mesto") int IdMes)

    {
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueueRestServer);
      
        
        
        Poruka poruka = new Poruka(4);

        
        poruka.setBroj1(IdKor);
        poruka.setBroj2(IdMes);


        ObjectMessage objPoruka = context.createObjectMessage(poruka);
        producer.send(QueuePodsistem1Primalac, objPoruka);
        
        Message message = consumer.receive();
        
        ObjectMessage obj = (ObjectMessage)message;
  
      
        try {
            poruka = (Poruka)(obj.getObject());
            
              if (poruka.getBroj1()<0)
            {
                return Response.status(Response.Status.OK).entity("Korisnik ili mesto ne postoje\n").build();
            }
            
            else
            {
                   
                Korisnik korisnik = new Korisnik(poruka.getPolje1(),poruka.getPolje2(),poruka.getBroj2(),poruka.getPolje3(),poruka.getBroj3());
                korisnik.setIdKor(poruka.getBroj1());

                String res = "IdKor|Ime|Email|Godiste|Pol|Mesto\n";

                res+=korisnik.getIdKor()+"|"+korisnik.getIme()+"|"+korisnik.getEmail()+"|"+korisnik.getGodiste()+"|"+korisnik.getPol()+"|"+korisnik.getMesto()+"\n";

                return Response.status(Response.Status.OK).entity(res).build();
            }
           
            
        } catch (JMSException ex) {
            Logger.getLogger(Korisnici.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    }
    
    
    
    
