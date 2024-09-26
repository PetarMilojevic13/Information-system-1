/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package podsistem3;

import entiteti.Gledanje;
import entiteti.Korisnik;
import entiteti.Ocena;
import entiteti.OcenaPK;
import entiteti.Paket;
import entiteti.Pretplata;
import entiteti.Video;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import poruka.Poruka;

/**
 *
 * @author Petar
 */
public class PodSistem3 {
    
    @Resource(lookup = "myConnFactory")
    private static ConnectionFactory myConnFactory;
    
    @Resource(lookup = "PodSistem1PrimalacQueue")
    private static javax.jms.Queue QueuePodsistem1Primalac;
    
    @Resource(lookup = "PodSistem2PrimalacQueue")
    private static javax.jms.Queue QueuePodsistem2Primalac;
    
    @Resource(lookup = "PodSistem3PrimalacQueue")
    private static javax.jms.Queue QueuePodsistem3Primalac;
    
    @Resource(lookup = "CentralniServerPrimalacQueue")
    private static javax.jms.Queue QueueRestServer;
    
    private static EntityManagerFactory emf;
    private static EntityManager em;
 
    private static JMSContext context;
    private static JMSProducer producer;
    private static JMSConsumer consumer;
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        emf= Persistence.createEntityManagerFactory("PodSistem3PU");
        em=emf.createEntityManager();
        context = myConnFactory.createContext();
        
        producer = context.createProducer();
        consumer = context.createConsumer(QueuePodsistem3Primalac);
      
        System.out.println("podsistem3.PodSistem3.main()");
        consumer.setMessageListener(new MessageListener() {
            
            @Override
            public void onMessage(Message message) {
                if (message instanceof ObjectMessage)
                {
                    System.out.println("USLO3");
                    ObjectMessage obj = (ObjectMessage)message;
                    Poruka poruka=null;
                   
                  
                    try {
                        poruka = (Poruka)(obj.getObject());
                    } catch (JMSException ex) {
                        Logger.getLogger(PodSistem3.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                     int zahtev = poruka.getBroj_zahteva();
                     
                     switch(zahtev)
                     {
                        case 2:
                        {
                            //Dobija poruku od podsistema 1 i salje mu odgovor nazad
                            String ime = poruka.getPolje1();
                            String email = poruka.getPolje2();
                            String pol = poruka.getPolje3();
                            int godiste = poruka.getBroj1();
                            int mesto = poruka.getBroj2();
                            int IdKor = KreirajKorisnika(ime,email,godiste,pol,mesto);
                            Poruka odgovor = new Poruka(102);
                            
                            
                            odgovor.setPolje1(ime);
                            odgovor.setPolje2(email);
                            odgovor.setPolje3(pol);
                            odgovor.setBroj1(IdKor);
                            odgovor.setBroj2(godiste);
                            odgovor.setBroj3(mesto);
       
                            ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                            producer.send(QueuePodsistem1Primalac, objPoruka);
                        }
                        break;
                         
                         
                        case 3:
                        {
                            //Dobija poruku od podsistema 1 i salje mu odgovor nazad
                            String email = poruka.getPolje1();
                            int IdKor = poruka.getBroj1();
                            Korisnik k = em.find(Korisnik.class,IdKor);
                          
                            
                            em.getTransaction().begin();
                            k.setEmail(email);
                            em.getTransaction().commit();
                            
                            Poruka odgovor = new Poruka(103);
                            
                            odgovor.setBroj1(IdKor);
                            
                            
                            System.out.println("ODGOVOR POSLAT");
       
                            ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                            producer.send(QueuePodsistem1Primalac, objPoruka);
                        }
                        break;
                      
                        case 4:
                        {
                            //Dobija poruku od podsistema 1 i salje mu odgovor nazad
                           
                            int IdKor = poruka.getBroj1();
                            int mesto = poruka.getBroj2();
                            Korisnik k = em.find(Korisnik.class,IdKor);
                          
                            
                            em.getTransaction().begin();
                            k.setMesto(mesto);
                            em.getTransaction().commit();
                            
                            Poruka odgovor = new Poruka(104);
                            odgovor.setBroj1(IdKor);
                            
                            System.out.println("ODGOVOR POSLAT");
       
                            ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                            producer.send(QueuePodsistem1Primalac, objPoruka);
                        }
                        break;
                        
                        case 6:
                        {
                            String naziv = poruka.getPolje1();
                            int trajanje = poruka.getBroj1();
                            int IdKor = poruka.getBroj2();
                            Integer dan = poruka.getBroj3();
                            Integer mesec = poruka.getBroj4();
                            Integer godina = poruka.getBroj5();
                            
                            Integer sat = poruka.getBroj6();
                            Integer minut = poruka.getBroj7();
                            Integer sekund = poruka.getBroj8();
                            
                            Korisnik korisnik = em.find(Korisnik.class,IdKor);
                            
                            String datumS = godina.toString()+"-"+mesec.toString()+"-"+dan.toString();
                            
                            String vremeS = sat.toString()+":" +minut.toString() +":" +sekund.toString();
                            
                            Date datum = Date.valueOf(datumS);
                            Time vreme = Time.valueOf(vremeS);
                            
                            Video video = new Video(naziv, trajanje, korisnik, datum, vreme);
                          
                            em.getTransaction().begin();
                            em.persist(video);
                            em.getTransaction().commit();
       
                            Poruka odgovor = new Poruka(206);
                            odgovor.setBroj1(video.getIdVid());
                            ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                            producer.send(QueuePodsistem2Primalac, objPoruka);
                        }
                        break;
                       
                        
                        case 7:
                        {
                            int IdVid = poruka.getBroj1();
                            String naziv = poruka.getPolje1();
                            
                            Video video = em.find(Video.class, IdVid);
                            
                            em.getTransaction().begin();
                            video.setNaziv(naziv);
                            em.getTransaction().commit();
                            
                            Poruka odgovor = new Poruka(207);
                            
                            odgovor.setBroj1(IdVid);
                            odgovor.setBroj2(video.getTrajanje());
                            odgovor.setBroj3(video.getKorisnik().getIdKor());
                            
                            String dateFormatPattern = "yyyy-MM-dd";
                                
                            String vreme = video.getVreme().toString();
                                
                            DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
                            String datum = dateFormat.format(video.getDatum());
                            
                            String godinas ="";
                            godinas+=datum.charAt(0);
                            godinas+=datum.charAt(1);
                            godinas+=datum.charAt(2);
                            godinas+=datum.charAt(3);
                            
                            String mesecS ="";
                            mesecS+=datum.charAt(5);
                            mesecS+=datum.charAt(6);
                         
                            System.out.println(vreme);
                            
                            String danS ="";
                            danS+=datum.charAt(8);
                            danS+=datum.charAt(9);
                            
                            String satS ="";
                            satS+=vreme.charAt(0);
                            satS+=vreme.charAt(1);
                            
                            String minutS ="";
                            minutS+=vreme.charAt(3);
                            minutS+=vreme.charAt(4);
                            
                            String sekundS ="";
                            sekundS+=vreme.charAt(6);
                            sekundS+=vreme.charAt(7);
                            
                            int datumInt = Integer.parseInt(godinas)*10000 + Integer.parseInt(mesecS)*100 + Integer.parseInt(danS);
 
                            int vremeInt = Integer.parseInt(satS)*10000 + Integer.parseInt(minutS)*100 + Integer.parseInt(sekundS);
                            
                            odgovor.setBroj4(datumInt);
                            odgovor.setBroj5(vremeInt);
                            
                            ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                            producer.send(QueuePodsistem2Primalac, objPoruka);
                        }
                        break;
                        
              
                        
                        
                        
                        case 9:
                        {
                            int cena = poruka.getBroj1();
                            
                            int IdPak = KreirajPaket(cena);
                            
                            Poruka novaPoruka = new Poruka(9);
                            novaPoruka.setBroj1(IdPak);
                                
                            novaPoruka.setBroj2(cena);

                            ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                

                                
                            producer.send(QueueRestServer, objPorukaServer);
                            
                            //Obavesti server
                        }
                        break;
                        
                        case 10:
                        {
                            int IdPak = poruka.getBroj1();
                            int cena = poruka.getBroj2();
                            
                            Paket paket = em.find(Paket.class, IdPak);
                            
                            if (paket==null)
                            {
                                Poruka slanje = new Poruka(-1);
                                slanje.setBroj1(-1);
                                ObjectMessage objPorukaServer = context.createObjectMessage(slanje);
                                producer.send(QueueRestServer, objPorukaServer);
                            }
                            
                            else
                            {
                                em.getTransaction().begin();
                                paket.setCena(cena);
                                em.getTransaction().commit();

                                Poruka novaPoruka = new Poruka(10);
                                novaPoruka.setBroj1(IdPak);

                                novaPoruka.setBroj2(cena);

                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);


                                  //Obavesti server    
                                producer.send(QueueRestServer, objPorukaServer); 
                            }
                            

                        }
                        break;
                        
                        
                        case 11:
                        {
                            int IdKor = poruka.getBroj1();
                            int IdPak = poruka.getBroj2();
                            
                            Integer dan = poruka.getBroj3();
                            Integer mesec = poruka.getBroj4();
                            Integer godina = poruka.getBroj5();
                            
                            Integer sat = poruka.getBroj6();
                            Integer minut = poruka.getBroj7();
                            Integer sekund = poruka.getBroj8();
                            
                            
                            int datumBrojkaProvera = (godina*10000+mesec*100+dan);
                            int datumBrojkaProveraZavrsetak = datumBrojkaProvera + 100;
                            //Racunanje datumZavrsetka pretplate koju zelimo da ubacimo
                            if (mesec==12)
                            {
                                datumBrojkaProveraZavrsetak = datumBrojkaProvera+10000-1200+100;
                            }
                            else if (mesec==1)
                            {
                                if (dan==31 || dan==30 || dan==29)
                                {
                                    if (godina%4==0)
                                    {
                                        datumBrojkaProveraZavrsetak = datumBrojkaProvera+100 - dan + 29;
                                    }

                                    else
                                    {
                                        datumBrojkaProveraZavrsetak = datumBrojkaProvera+100 - dan + 28;
                                    }
                                    
                                  
                                }
                                   
                                }
                            else if (mesec==3 || mesec==5 || mesec==7 || mesec==8 || mesec==10)
                            {
                                if (dan==31)
                                {
                                    datumBrojkaProveraZavrsetak = datumBrojkaProvera+100 - dan + 30;
                                }
                                    
                            }
                            
                            System.out.println("EVO");
                            System.out.println(datumBrojkaProveraZavrsetak);
                            
                            int vremeBrojkaProvera = (sat*10000+minut*100+sekund);
                            
                            Korisnik korisnik = em.find(Korisnik.class,IdKor);
                            
                            Paket paket = em.find(Paket.class,IdPak);
                            
                            
                            if (paket==null || korisnik==null)
                            {
                                Poruka slanje = new Poruka(-2);
                                slanje.setBroj1(-2);
                                ObjectMessage objPorukaServer = context.createObjectMessage(slanje);
                                producer.send(QueueRestServer, objPorukaServer);
                            }
                            
                            else
                            {
                            int cena = paket.getCena();
                            
                            // Provera da li postoji pretplata koja jos uvek traje
                            
                            TypedQuery<Pretplata> query = em.createQuery("SELECT p FROM Pretplata p WHERE p.korisnik.idKor = :idKor", Pretplata.class);
                            query.setParameter("idKor", IdKor);
                            List<Pretplata> results = query.getResultList();
                            
                            int pronadjeno=0;
                            
                            for (Pretplata pret:results)
                            {
                                String dateFormatPattern = "yyyy-MM-dd";
                                
                                String vreme = pret.getVreme().toString();
                                
                                DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
                                String datum = dateFormat.format(pret.getDatum());

                                String godinas ="";
                                godinas+=datum.charAt(0);
                                godinas+=datum.charAt(1);
                                godinas+=datum.charAt(2);
                                godinas+=datum.charAt(3);

                                String mesecS ="";
                                mesecS+=datum.charAt(5);
                                mesecS+=datum.charAt(6);

                                System.out.println(vreme);

                                String danS ="";
                                danS+=datum.charAt(8);
                                danS+=datum.charAt(9);

                                String satS ="";
                                satS+=vreme.charAt(0);
                                satS+=vreme.charAt(1);

                                String minutS ="";
                                minutS+=vreme.charAt(3);
                                minutS+=vreme.charAt(4);

                                String sekundS ="";
                                sekundS+=vreme.charAt(6);
                                sekundS+=vreme.charAt(7);
                                
                                System.out.println(godinas+" "+mesecS+" "+danS);

                                int datumInt = Integer.parseInt(godinas)*10000 + Integer.parseInt(mesecS)*100 + Integer.parseInt(danS);

                                int vremeInt = Integer.parseInt(satS)*10000 + Integer.parseInt(minutS)*100 + Integer.parseInt(sekundS);
                                
                                int datumPretplata = datumInt;
                                
                                int datumZavrsetakPretplate=datumPretplata+100;
                                
                                if (mesecS.equals("12"))
                                {
                                    datumZavrsetakPretplate = datumPretplata+10000-1200+100;
                                }
                                else if (mesecS.equals("1"))
                                {
                                    if (danS.equals("31") || danS.equals("30") || danS.equals("29"))
                                    {
                                        if (Integer.parseInt(godinas)%4==0)
                                        {
                                            datumZavrsetakPretplate = datumPretplata+100 - Integer.parseInt(danS) + 29;
                                        }
                                        else
                                        {
                                            datumZavrsetakPretplate = datumPretplata+100 - Integer.parseInt(danS) + 28;
                                        }
                                        
                                    }
                                   
                                }
                                else if (mesecS.equals("3") || mesecS.equals("5") ||mesecS.equals("7") || mesecS.equals("8") || mesecS.equals("10"))
                                {
                                    if (danS.equals("31"))
                                    {
                                        datumZavrsetakPretplate = datumPretplata+100 - Integer.parseInt(danS) + 30;
                                    }
                                    
                                }
                                
                               
                               
                                
                                System.out.println("ISPIS DATUMA");
                                
                                System.out.println(datumZavrsetakPretplate);
                                System.out.println(datumBrojkaProvera);
                                
                                if (datumPretplata<datumBrojkaProvera && datumBrojkaProvera<datumZavrsetakPretplate && pret.getKorisnik().getIdKor()==IdKor)
                                {
                                    pronadjeno=1;
                                    break;
                                }
                                else if (datumBrojkaProvera<datumPretplata && datumPretplata<datumBrojkaProveraZavrsetak && pret.getKorisnik().getIdKor()==IdKor)
                                {
                                    pronadjeno=1;
                                    break;
                                }
                                
                                if ((datumPretplata==datumBrojkaProvera || datumBrojkaProvera==datumZavrsetakPretplate) && pret.getKorisnik().getIdKor()==IdKor)
                                {
                                    if (vremeBrojkaProvera>=vremeInt && datumPretplata==datumBrojkaProvera)
                                    {
                                            pronadjeno=1;
                                            break;
                                    }
                                    else if (vremeBrojkaProvera<=vremeInt && datumBrojkaProvera==datumZavrsetakPretplate)
                                    {
                                            pronadjeno=1;
                                            break;
                                    }
                                    
                                
                                }
                                
                                
                            }
                            
                            if (pronadjeno==0)
                            {
                                                  
                                String datumS = godina.toString()+"-"+mesec.toString()+"-"+dan.toString();

                                String vremeS = sat.toString()+":" +minut.toString() +":" +sekund.toString();

                                System.out.println(datumS);

                                Date datum = Date.valueOf(datumS);
                                Time vreme = Time.valueOf(vremeS);

                                int IdPre = KreirajPretplatu(korisnik, paket, datum, vreme, cena);

                                Poruka novaPoruka = new Poruka(11);
                                novaPoruka.setBroj1(IdPre);
                                novaPoruka.setBroj2(cena);
                                novaPoruka.setBroj3(11);

                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);

                                System.out.println("SERVERU POSLATA PRETPLATA");

                                producer.send(QueueRestServer, objPorukaServer);
                            }
                            else
                            {


                                Poruka novaPoruka = new Poruka(-1);

                                novaPoruka.setBroj1(-1);

                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);

                                System.out.println("SERVERU POSLATA NEUSPESNA PRETPLATA");

                                producer.send(QueueRestServer, objPorukaServer);
                            }
                            }
                            
                            //Obavesti server
                            
                        }
                        break;
                        
                        
                        
                        case 12:
                        {
                            int IdVid = poruka.getBroj1();
                            int IdKor = poruka.getBroj2();
                            
                            Integer dan = poruka.getBroj3();
                            Integer mesec = poruka.getBroj4();
                            Integer godina = poruka.getBroj5();
                            
                            Integer sat = poruka.getBroj6();
                            Integer minut = poruka.getBroj7();
                            Integer sekund = poruka.getBroj8();
                            
                            int pocetni_sekund = poruka.getBroj9();
                            int odgledano_sekundi = poruka.getBroj10();
                            
                            Video video = em.find(Video.class,IdVid);
                            Korisnik korisnik = em.find(Korisnik.class,IdKor);
                            
                            if (video==null || korisnik==null)
                            {
                                Poruka novaPoruka = new Poruka(-1);
                                novaPoruka.setBroj1(-1);
                                

                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                
                                System.out.println("SERVERU NEUSPESNA POSLATO GLEDANJE");
                                
                                producer.send(QueueRestServer, objPorukaServer);
                            }
                            else
                            {
                                String datumS = godina.toString()+"-"+mesec.toString()+"-"+dan.toString();
                            
                                String vremeS = sat.toString()+":" +minut.toString() +":" +sekund.toString();

                                Date datum = Date.valueOf(datumS);
                                Time vreme = Time.valueOf(vremeS);
                                
                                int datumGledanjeInt = godina*10000+mesec*100+dan;
                                
                                int vremeGledanjeInt = sat*10000 + minut*100+sekund;
                                
                                
                                TypedQuery<Pretplata> query = em.createQuery("SELECT p FROM Pretplata p WHERE p.korisnik.idKor = :idKor", Pretplata.class);
                                query.setParameter("idKor", IdKor);
                                List<Pretplata> results = query.getResultList();
                                
                                int pronadjeno=0;
                                
                                //Provera da li postoji pretplata
                                
                                for (Pretplata pret:results)
                                {
                                    
                                                                    String dateFormatPattern = "yyyy-MM-dd";
                                
                                    String vremePretplata = pret.getVreme().toString();

                                    DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
                                    String datumPretplata = dateFormat.format(pret.getDatum());

                                    String godinas ="";
                                    godinas+=datumPretplata.charAt(0);
                                    godinas+=datumPretplata.charAt(1);
                                    godinas+=datumPretplata.charAt(2);
                                    godinas+=datumPretplata.charAt(3);

                                    String mesecS ="";
                                    mesecS+=datumPretplata.charAt(5);
                                    mesecS+=datumPretplata.charAt(6);

                                    System.out.println(vreme);

                                    String danS ="";
                                    danS+=datumPretplata.charAt(8);
                                    danS+=datumPretplata.charAt(9);

                                    String satS ="";
                                    satS+=vremePretplata.charAt(0);
                                    satS+=vremePretplata.charAt(1);

                                    String minutS ="";
                                    minutS+=vremePretplata.charAt(3);
                                    minutS+=vremePretplata.charAt(4);

                                    String sekundS ="";
                                    sekundS+=vremePretplata.charAt(6);
                                    sekundS+=vremePretplata.charAt(7);

                                    System.out.println(godinas+" "+mesecS+" "+danS);

                                    int datumInt = Integer.parseInt(godinas)*10000 + Integer.parseInt(mesecS)*100 + Integer.parseInt(danS);

                                    int vremeInt = Integer.parseInt(satS)*10000 + Integer.parseInt(minutS)*100 + Integer.parseInt(sekundS);
                                    
                                    int datumZavrsetakPretplate=datumInt+100;
                                
                                    if (mesecS.equals("12"))
                                    {
                                        datumZavrsetakPretplate = datumInt+10000-1200+100;
                                    }
                                    else if (mesecS.equals("1"))
                                    {
                                        if (danS.equals("31") || danS.equals("30") || danS.equals("29"))
                                        {
                                            if (Integer.parseInt(godinas)%4==0)
                                            {
                                                datumZavrsetakPretplate = datumInt+100 - Integer.parseInt(danS) + 29;
                                            }
                                            else
                                            {
                                                datumZavrsetakPretplate = datumInt+100 - Integer.parseInt(danS) + 28;
                                            }

                                        }

                                    }
                                    else if (mesecS.equals("3") || mesecS.equals("5") ||mesecS.equals("7") || mesecS.equals("8") || mesecS.equals("10"))
                                    {
                                        if (danS.equals("31"))
                                        {
                                            datumZavrsetakPretplate = datumInt+100 - Integer.parseInt(danS) + 30;
                                        }

                                    }
                                    
                                    
                                    if (datumInt<datumGledanjeInt && datumGledanjeInt<datumZavrsetakPretplate && pret.getKorisnik().getIdKor()==IdKor)
                                    {
                                        pronadjeno=1;
                                        break;
                                    }


                                    else if ((datumInt==datumGledanjeInt || datumGledanjeInt==datumZavrsetakPretplate) && pret.getKorisnik().getIdKor()==IdKor)
                                    {
                                        if (vremeGledanjeInt>=vremeInt && datumInt==datumGledanjeInt)
                                        {
                                                pronadjeno=1;
                                                break;
                                        }
                                        else if (vremeGledanjeInt<=vremeInt && datumGledanjeInt==datumZavrsetakPretplate)
                                        {
                                                pronadjeno=1;
                                                break;
                                        }


                                    }
                                    
                                    
                                    
                                }
                                
                                if (pronadjeno==1)
                                {
                                    int IdGle = KreirajGledanje(video,korisnik, datum, vreme, pocetni_sekund,odgledano_sekundi);

                                    Poruka novaPoruka = new Poruka(12);
                                    novaPoruka.setBroj1(IdGle);


                                    ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);

                                    System.out.println("SERVERU POSLATA GLEDANJE");

                                    producer.send(QueueRestServer, objPorukaServer);

                                    //Obavesti server
                                }
                                else
                                {
                                    Poruka novaPoruka = new Poruka(-2);
                                    novaPoruka.setBroj1(-2);


                                    ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);

                                    System.out.println("SERVERU NEUSPESNA POSLATO GLEDANJE");

                                    producer.send(QueueRestServer, objPorukaServer);
                                }
                                
                                
                                
                               

                            }
                                          

                        }
                        break;  
                        
                        
                        case 13:
                        {

                            int IdKor = poruka.getBroj1();
                            int IdVid = poruka.getBroj2();
                            
                            int ocena = poruka.getBroj3();
                            
                            TypedQuery<Ocena> query = em.createQuery("SELECT o FROM Ocena o WHERE o.ocenaPK.korisnik = :korisnik", Ocena.class);
                            query.setParameter("korisnik", IdKor);
                            List<Ocena> results = query.getResultList();
                            
                            int pronadjeno = 0;
                            for(Ocena oc:results)
                            {
                                if (oc.getVideo1().getIdVid()==IdVid)
                                {
                                    pronadjeno=1;
                                    break;
                                }
                            }
                            
                            if (pronadjeno==1)
                            {
                                Poruka novaPoruka = new Poruka(-1);
                                novaPoruka.setBroj1(-1);
                                

                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                
                                System.out.println("SERVERU NEUSPESNA POSLATA OCENA");
                                
                                producer.send(QueueRestServer, objPorukaServer);
                            }
                            else
                            {
                                Integer dan = poruka.getBroj4();
                                Integer mesec = poruka.getBroj5();
                                Integer godina = poruka.getBroj6();

                                Integer sat = poruka.getBroj7();
                                Integer minut = poruka.getBroj8();
                                Integer sekund = poruka.getBroj9();

                                OcenaPK ocenaPk = new OcenaPK(IdKor,IdVid);

                                Video video = em.find(Video.class,IdVid);
                                Korisnik korisnik = em.find(Korisnik.class,IdKor);
                                
                                if (korisnik==null || video==null)
                                {
                                    Poruka novaPoruka = new Poruka(-2);
                                    novaPoruka.setBroj1(-2);
                                    ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                
                                    System.out.println("SERVERU NEUSPESNA POSLATA OCENA");
                                
                                    producer.send(QueueRestServer, objPorukaServer);
                                }
                                else
                                {
                                    String datumS = godina.toString()+"-"+mesec.toString()+"-"+dan.toString();

                                    String vremeS = sat.toString()+":" +minut.toString() +":" +sekund.toString();

                                    Date datum = Date.valueOf(datumS);
                                    Time vreme = Time.valueOf(vremeS);

                                    Ocena ocenaObjekat = new Ocena(ocenaPk);

                                    ocenaObjekat.setDatum(datum);
                                    ocenaObjekat.setVreme(vreme);
                                    ocenaObjekat.setOcena(ocena);
                                    ocenaObjekat.setKorisnik1(korisnik);
                                    ocenaObjekat.setVideo1(video);

                                    em.getTransaction().begin();
                                    em.persist(ocenaObjekat);
                                    em.getTransaction().commit();

                                    Poruka novaPoruka = new Poruka(13);
                                    novaPoruka.setBroj1(13);


                                    ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);

                                    System.out.println("SERVERU POSLATA OCENA");

                                    producer.send(QueueRestServer, objPorukaServer);
                                }



                                
                                
                                
                            }
                            
                       
                            
                            
                            
                            //Obavesti server
                        }
                        break;
                        
                        
                        case 14:
                        {
                            
                            int IdKor = poruka.getBroj1();
                            int IdVid = poruka.getBroj2();
                          
                            int ocena = poruka.getBroj3();
                            
                            TypedQuery<Ocena> query = em.createQuery("SELECT o FROM Ocena o WHERE o.ocenaPK.korisnik = :korisnik AND o.ocenaPK.video = :video", Ocena.class);
                            query.setParameter("korisnik", IdKor);
                            query.setParameter("video", IdVid);
                            
                            try
                            {
                                    Ocena ocenaObjekat = query.getSingleResult();
                                    em.getTransaction().begin();
                                    ocenaObjekat.setOcena(ocena);
                                    em.getTransaction().commit();
                                    
                                    
                                    //Posalji serveru
                                    
                                    
                                    
                                    Poruka novaPoruka = new Poruka(14);
                                    novaPoruka.setBroj1(14);
                                    
                                    
                                    //Dohvatanje datuma i vremena
                                    
                                    String dateFormatPattern = "yyyy-MM-dd";

                                    String vreme = ocenaObjekat.getVreme().toString();

                                    DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
                                    String datum = dateFormat.format(ocenaObjekat.getDatum());

                                    String godinas ="";
                                    godinas+=datum.charAt(0);
                                    godinas+=datum.charAt(1);
                                    godinas+=datum.charAt(2);
                                    godinas+=datum.charAt(3);

                                    String mesecS ="";
                                    mesecS+=datum.charAt(5);
                                    mesecS+=datum.charAt(6);

                                    System.out.println(vreme);

                                    String danS ="";
                                    danS+=datum.charAt(8);
                                    danS+=datum.charAt(9);

                                    String satS ="";
                                    satS+=vreme.charAt(0);
                                    satS+=vreme.charAt(1);

                                    String minutS ="";
                                    minutS+=vreme.charAt(3);
                                    minutS+=vreme.charAt(4);

                                    String sekundS ="";
                                    sekundS+=vreme.charAt(6);
                                    sekundS+=vreme.charAt(7);

                                    System.out.println(godinas+" "+mesecS+" "+danS);

                                    int datumInt = Integer.parseInt(godinas)*10000 + Integer.parseInt(mesecS)*100 + Integer.parseInt(danS);

                                    int vremeInt = Integer.parseInt(satS)*10000 + Integer.parseInt(minutS)*100 + Integer.parseInt(sekundS);
                                    
                                    novaPoruka.setBroj2(datumInt);
                                    novaPoruka.setBroj3(vremeInt);
                                    


                                    ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                
                                    System.out.println("SERVERU POSLATA OCENA");
                                
                                    producer.send(QueueRestServer, objPorukaServer);
                                    
                            }
                            catch (NoResultException ex)
                            {
                                //Posalji serveru da ne postoji ocena
                                
                                Poruka novaPoruka = new Poruka(-1);
                                novaPoruka.setBroj1(-1);


                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                
                                System.out.println("SERVERU NEUSPESNO POSLATA OCENA");
                                
                                producer.send(QueueRestServer, objPorukaServer);
                            }
                        
                            
                            
                            
                            //Obavesti server
                        }
                        break;
                        
                        
                        case 15:
                        {
                            
                            int IdKor = poruka.getBroj1();
                            int IdVid = poruka.getBroj2();
                          

                            
                            TypedQuery<Ocena> query = em.createQuery("SELECT o FROM Ocena o WHERE o.ocenaPK.korisnik = :korisnik AND o.ocenaPK.video = :video", Ocena.class);
                            query.setParameter("korisnik", IdKor);
                            query.setParameter("video", IdVid);
                            
                            try
                            {
                                    Ocena ocenaObjekat = query.getSingleResult();
                                    em.getTransaction().begin();
                                    em.remove(ocenaObjekat);
                                    em.getTransaction().commit();
                                    
                                    Poruka novaPoruka = new Poruka(15);
                                    novaPoruka.setBroj1(15);


                                    ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                
                                    System.out.println("SERVERU OBRISANA OCENA");
                                
                                    producer.send(QueueRestServer, objPorukaServer);
                                    
                                    
                                    
                                    //Posalji serveru
                                    
                            }
                            catch (NoResultException ex)
                            {
                                //Posalji serveru da ne postoji ocena
                                
                                Poruka novaPoruka = new Poruka(-1);
                                novaPoruka.setBroj1(-1);


                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                
                                System.out.println("SERVERU NEUSPESNO OBRISANA OCENA");
                                
                                producer.send(QueueRestServer, objPorukaServer);
                            }
                        
                            
                            
                            
                            //Obavesti server
                        }
                        break;
                        
                        
                                                //Odgovor podsistemu 2
                        
                        case 16:
                        {
                            int IdVid = poruka.getBroj1();
                            
//                            TypedQuery<Ocena> query = em.createQuery("SELECT oc FROM Ocena oc", Ocena.class);
//                            List<Ocena> results = query.getResultList();
//                                      
//                            for (Ocena oc:results)
//                            {
//                                if (oc.getVideo1().getIdVid()==IdVid)
//                                {
//                                    em.getTransaction().begin();
//                                    em.remove(oc);
//                                    em.getTransaction().commit();
//                                }
//                            }
//                            
//                            TypedQuery<Gledanje> queryGledanje = em.createQuery("SELECT gl FROM Gledanje gl", Gledanje.class);
//                            List<Gledanje> resultsGledanje = queryGledanje.getResultList();
//                                      
//                            for (Gledanje gled:resultsGledanje)
//                            {
//                                if (gled.getVideo().getIdVid()==IdVid)
//                                {
//                                    em.getTransaction().begin();
//                                    em.remove(gled);
//                                    em.getTransaction().commit();
//                                }
//                            }
                            
                            
                            
                            
                            
                            
                            
                            Video video = em.find(Video.class,IdVid);
                            em.getTransaction().begin();
                            em.remove(video);
                            em.getTransaction().commit();
                            
                            Poruka odgovor = new Poruka(2016);
                            ObjectMessage objPoruka = context.createObjectMessage(odgovor);
                            producer.send(QueuePodsistem2Primalac, objPoruka);
                        }
                        break; 
                        
                        case 22:
                        {
                            TypedQuery<Paket> query = em.createQuery("SELECT p FROM Paket p", Paket.class);
                            List<Paket> results = query.getResultList();
                            Poruka nova = new Poruka(22);
                            nova.setBroj1(results.size());
                            ObjectMessage objPoruka = context.createObjectMessage(nova);
                            producer.send(QueueRestServer, objPoruka);
                            //SLANJE SERVERU
                            
                            
                            for (Paket paket:results)
                            {
                           
                                Poruka novaPoruka = new Poruka(22);
                                novaPoruka.setBroj1(paket.getIdPak());
                                novaPoruka.setBroj2(paket.getCena());
                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                System.out.println("POSLO");
                                producer.send(QueueRestServer, objPorukaServer);
                                //SLANJE SERVERU
                            }
                        }
                            break;  
                            
                        case 23:
                        {
                            int IdKor = poruka.getBroj1();
                            TypedQuery<Pretplata> query = em.createQuery("SELECT p FROM Pretplata p WHERE p.korisnik.idKor = :idKor", Pretplata.class);
                            query.setParameter("idKor", IdKor);
                            List<Pretplata> results = query.getResultList();
                            Poruka nova = new Poruka(23);
                            nova.setBroj1(results.size());
                            ObjectMessage objPoruka = context.createObjectMessage(nova);
                            producer.send(QueueRestServer, objPoruka);
                            //SLANJE SERVERU
                            
                            for (Pretplata pretplata:results)
                            {
                           
                                Poruka novaPoruka = new Poruka(23);
                                
                                novaPoruka.setBroj1(pretplata.getIdPre());
                                novaPoruka.setBroj2(pretplata.getKorisnik().getIdKor());
                                novaPoruka.setBroj3(pretplata.getPaket().getIdPak());
                                novaPoruka.setBroj4(pretplata.getCena());
                                
                                String dateFormatPattern = "yyyy-MM-dd";
                                
                                String vreme = pretplata.getVreme().toString();
                                
                                DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
                                String datum = dateFormat.format(pretplata.getDatum());
                                
                                novaPoruka.setPolje1(datum);
                                novaPoruka.setPolje2(vreme);
                                
                                
                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                producer.send(QueueRestServer, objPorukaServer);
                                //SLANJE SERVERU
                            }
                        }
                            break;                            
                            
                        case 24:
                        {
                            int IdVid = poruka.getBroj1();
                            TypedQuery<Gledanje> query = em.createQuery("SELECT g FROM Gledanje g WHERE g.video.idVid = :idVid", Gledanje.class);
                            query.setParameter("idVid", IdVid);
                            List<Gledanje> results = query.getResultList();
                            Poruka nova = new Poruka(24);
                            nova.setBroj1(results.size());
                            ObjectMessage objPoruka = context.createObjectMessage(nova);
                            producer.send(QueueRestServer, objPoruka);
                            //SLANJE SERVERU
                            
                            for (Gledanje gledanje:results)
                            {
                           
                                Poruka novaPoruka = new Poruka(24);
                                Video video = gledanje.getVideo();
                                Korisnik korisnik = gledanje.getKorisnik();
                                
                                
                                String dateFormatPattern = "yyyy-MM-dd";
                                
                                String vreme = gledanje.getVreme().toString();
                                
                                DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
                                String datum = dateFormat.format(gledanje.getDatum());
                                
                                novaPoruka.setBroj1(gledanje.getIdGle());
                                novaPoruka.setBroj2(video.getIdVid());
                                novaPoruka.setBroj3(korisnik.getIdKor());
                                novaPoruka.setBroj4(gledanje.getPocetniSekund());
                                novaPoruka.setBroj5(gledanje.getSekundiOdgledano());
                                
                                
                                novaPoruka.setPolje1(datum);
                                novaPoruka.setPolje2(vreme);
                                
                                
                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                producer.send(QueueRestServer, objPorukaServer);
                                //SLANJE SERVERU
                            }
                        }
                            break;    
                            
                            
                            
                        case 25:
                        {
                            int IdVid = poruka.getBroj1();
                            TypedQuery<Ocena> query = em.createQuery("SELECT o FROM Ocena o WHERE o.ocenaPK.video = :idVid", Ocena.class);
                            query.setParameter("idVid", IdVid);
                            List<Ocena> results = query.getResultList();
                            Poruka nova = new Poruka(25);
                            nova.setBroj1(results.size());
                            ObjectMessage objPoruka = context.createObjectMessage(nova);
                            producer.send(QueueRestServer, objPoruka);
                            //SLANJE SERVERU
                            
                            for (Ocena ocena:results)
                            {
                           
                                Poruka novaPoruka = new Poruka(25);
                                
                                
                                
                                String dateFormatPattern = "yyyy-MM-dd";
                                
                                String vreme = ocena.getVreme().toString();
                                
                                DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
                                String datum = dateFormat.format(ocena.getDatum());
                                
                                novaPoruka.setBroj1(ocena.getKorisnik1().getIdKor());
                                novaPoruka.setBroj2(ocena.getVideo1().getIdVid());
                                novaPoruka.setBroj3(ocena.getOcena());

                                
                                
                                novaPoruka.setPolje1(datum);
                                novaPoruka.setPolje2(vreme);
                                
                                
                                ObjectMessage objPorukaServer = context.createObjectMessage(novaPoruka);
                                producer.send(QueueRestServer, objPorukaServer);
                                //SLANJE SERVERU
                            }
                        }
                            break;                               
                     }
                    
                }
            }
        });
        
        
        while (true)
        {
            
        }
        
        
    }
    
    
      
    public static int KreirajKorisnika(String ime,String email,int godiste,String pol,int mesto)
    {
        Korisnik k = new Korisnik(ime,email,godiste,pol,mesto);

        em.getTransaction().begin();
        em.persist(k);
        em.getTransaction().commit();
        System.out.println("OK poslato\n");
        return k.getIdKor();
    }
    
    
    public static int KreirajPaket(int cena)
    {
        Paket paket = new Paket(cena);

        em.getTransaction().begin();
        em.persist(paket);
        em.getTransaction().commit();
        System.out.println("OK poslato\n");
        return paket.getIdPak();
    }
    
    public static int KreirajPretplatu(Korisnik korisnik,Paket paket,Date datum,Time vreme,int cena)
    {
        Pretplata pretplata = new Pretplata(korisnik,paket,datum,vreme,cena);
        
        em.getTransaction().begin();
        em.persist(pretplata);
        em.getTransaction().commit();
        
        return pretplata.getIdPre();
     
    }
    
    public static int KreirajGledanje(Video video,Korisnik korisnik,Date datum,Time vreme,int pocetniSekund,int odgledanoSekundi)
    {
        Gledanje gledanje = new Gledanje(video,korisnik,datum,vreme,pocetniSekund,odgledanoSekundi);
        gledanje.setSekundiOdgledano(odgledanoSekundi);

        em.getTransaction().begin();
        em.persist(gledanje);
        em.getTransaction().commit();
  
        return gledanje.getIdGle();
    }
}
