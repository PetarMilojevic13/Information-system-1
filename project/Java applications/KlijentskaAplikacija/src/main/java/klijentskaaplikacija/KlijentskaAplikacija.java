/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package klijentskaaplikacija;

import java.io.IOException;
import java.util.Scanner;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.util.EntityUtils;


/**
 *
 * @author Petar
 */
public class KlijentskaAplikacija {
    final static CloseableHttpClient  httpClient = HttpClients.createDefault();
    
    public static void getMesta() throws IOException
    {
        HttpGet request = new HttpGet("http://localhost:8080/CentralniServer/centralniserver/mesto");
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
    }
    
    public static void createMesto(String naziv) throws IOException
    {

        HttpPost request = new HttpPost("http://localhost:8080/CentralniServer/centralniserver/mesto/"+naziv);
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
    }
    
    public static void createKorisnik(String naziv,String email,int godiste,String pol,int mesto) throws IOException
    {

        HttpPost request = new HttpPost("http://localhost:8080/CentralniServer/centralniserver/korisnik/"+naziv+"/"+email+"/"+godiste+"/"+pol+"/"+mesto);
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
    }
    
    public static void promeniEmail(int IdKor,String email) throws IOException
    {

        HttpPut request = new HttpPut("http://localhost:8080/CentralniServer/centralniserver/korisnik/"+IdKor+"/"+email);
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
    }
    
    public static void promeniMesto(int IdKor,int IdMes) throws IOException
    {

        HttpPut request = new HttpPut("http://localhost:8080/CentralniServer/centralniserver/korisnik/postaviMesto/"+IdKor+"/"+IdMes);
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
    }

    public static void createKategorija(String naziv) throws IOException
    {

        HttpPost request = new HttpPost("http://localhost:8080/CentralniServer/centralniserver/kategorija/"+naziv);
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
    }    
    
    
    
    public static void createVideo(String naziv,int trajanje,int IdKor,String datum,String vreme) throws IOException
    {
        String godinas ="";
        godinas+=datum.charAt(0);
        godinas+=datum.charAt(1);
        godinas+=datum.charAt(2);
        godinas+=datum.charAt(3);

        String mesecS ="";
        mesecS+=datum.charAt(5);
        mesecS+=datum.charAt(6);



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

        HttpPost request = new HttpPost("http://localhost:8080/CentralniServer/centralniserver/video/"+naziv+"/"+trajanje+"/"+IdKor+"/"
                +datumInt+"/"+vremeInt);
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
    }
    



    public static void promeniNazivVidea(int IdVid,String naziv) throws IOException
    {

        HttpPut request = new HttpPut("http://localhost:8080/CentralniServer/centralniserver/video/"+IdVid+"/"+naziv);
        
        
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
    }    
    
    public static void dodajKategorijuVideu(int IdVid,int IdKat) throws IOException
    {
         HttpPost request = new HttpPost("http://localhost:8080/CentralniServer/centralniserver/video/dodajkategoriju/"+IdVid+"/"+IdKat);
        
        
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }
    
    
    public static void createPaket(int cena) throws IOException
    {
         HttpPost request = new HttpPost("http://localhost:8080/CentralniServer/centralniserver/paket/"+cena);
        
        
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }
    
  
    
    public static void promeniCenuPaketa(int IdPak,int cena) throws IOException
    {
         HttpPut request = new HttpPut("http://localhost:8080/CentralniServer/centralniserver/paket/"+IdPak+"/"+cena);
        
        
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }
    
        
    public static void createPretplata(int IdKor,int IdPak,String datum,String vreme) throws IOException
    {
        String godinas ="";
        godinas+=datum.charAt(0);
        godinas+=datum.charAt(1);
        godinas+=datum.charAt(2);
        godinas+=datum.charAt(3);

        String mesecS ="";
        mesecS+=datum.charAt(5);
        mesecS+=datum.charAt(6);

       

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
        
         HttpPost request = new HttpPost("http://localhost:8080/CentralniServer/centralniserver/pretplata/"+IdKor+"/"+IdPak+"/"+datumInt+"/"+vremeInt);
        
        
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }
    
    
    
    public static void createGledanje(int IdVid,int IdKor,String datum,String vreme,int pocetniSekund,int sekundiOdgledano) throws IOException
    {
        String godinas ="";
        godinas+=datum.charAt(0);
        godinas+=datum.charAt(1);
        godinas+=datum.charAt(2);
        godinas+=datum.charAt(3);

        String mesecS ="";
        mesecS+=datum.charAt(5);
        mesecS+=datum.charAt(6);

       

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
        
         HttpPost request = new HttpPost("http://localhost:8080/CentralniServer/centralniserver/gledanje/"+IdVid+"/"+IdKor+"/"+datumInt+"/"+vremeInt
         +"/"+pocetniSekund+"/"+sekundiOdgledano);
        
        
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }
    
    
    
    public static void createOcena(int IdKor,int IdVid,int ocena,String datum,String vreme) throws IOException
    {
        String godinas ="";
        godinas+=datum.charAt(0);
        godinas+=datum.charAt(1);
        godinas+=datum.charAt(2);
        godinas+=datum.charAt(3);

        String mesecS ="";
        mesecS+=datum.charAt(5);
        mesecS+=datum.charAt(6);

       

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
        
         HttpPost request = new HttpPost("http://localhost:8080/CentralniServer/centralniserver/ocena/"+IdKor+"/"+IdVid+"/"+ocena+"/"
                 +datumInt+"/"+vremeInt);
        
        
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }
    
    
    
    public static void promeniOcenu(int IdKor,int IdVid,int ocena) throws IOException
    {
        

         HttpPut request = new HttpPut("http://localhost:8080/CentralniServer/centralniserver/ocena/"+IdKor+"/"+IdVid+"/"+ocena);
        
        
        
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }    

    
    
    
    public static void obrisiOcenu(int IdKor,int IdVid) throws IOException
    {
        

        HttpDelete request = new HttpDelete("http://localhost:8080/CentralniServer/centralniserver/ocena/"+IdKor+"/"+IdVid);
    
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }    
    
    
    public static void obrisiVideo(int IdVid,int IdKor) throws IOException
    {
        

        HttpDelete request = new HttpDelete("http://localhost:8080/CentralniServer/centralniserver/video/"+IdVid+"/"+IdKor);
    
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    } 
    
    
    public static void dohvatiSvaMesta() throws IOException
    {
        

        HttpGet request = new HttpGet("http://localhost:8080/CentralniServer/centralniserver/mesto");
    
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    } 
    
    
    
    
    public static void dohvatiSveKorisnike() throws IOException
    {
        

        HttpGet request = new HttpGet("http://localhost:8080/CentralniServer/centralniserver/korisnik");
    
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }    
    
    
    
    
    
    public static void dohvatiSveKategorije() throws IOException
    {
        

        HttpGet request = new HttpGet("http://localhost:8080/CentralniServer/centralniserver/kategorija");
    
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }    
    

            
            
            
    
    public static void dohvatiSveVideoSnimke() throws IOException
    {
        

        HttpGet request = new HttpGet("http://localhost:8080/CentralniServer/centralniserver/video");
    
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }     
    
    
    public static void dohvatiSveKategorijeVidea(int IdVid) throws IOException
    {
        

        HttpGet request = new HttpGet("http://localhost:8080/CentralniServer/centralniserver/video/dohvatiKategorije/"+IdVid);
    
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }    
    
    
    public static void dohvatiSvePakete() throws IOException
    {
        

        HttpGet request = new HttpGet("http://localhost:8080/CentralniServer/centralniserver/paket");
    
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }        
    
    
    
    
            
            
    public static void dohvatiSvePretplateKorisnika(int IdKor) throws IOException
    {
        

        HttpGet request = new HttpGet("http://localhost:8080/CentralniServer/centralniserver/pretplata/"+IdKor);
    
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }     
    
    
    public static void dohvatiSvaGledanjaVidea(int IdVid) throws IOException
    {
        

        HttpGet request = new HttpGet("http://localhost:8080/CentralniServer/centralniserver/gledanje/"+IdVid);
    
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }          
    
    
            
            
    public static void dohvatiSveOceneVidea(int IdVid) throws IOException
    {
        

        HttpGet request = new HttpGet("http://localhost:8080/CentralniServer/centralniserver/ocena/"+IdVid);
    
        CloseableHttpResponse response = httpClient.execute(request);
        
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        
        
    }               
    
    public static void main(String[] args) throws IOException {
        System.out.println("Zahtevi koje imate na raspolaganju:");
        System.out.println("1. Kreiranje grada\n" +
"2. Kreiranje korisnika\n" +
"3. Promena email adrese za korisnika\n" +
"4. Promena mesta za korisnika\n" +
"5. Kreiranje kategorije\n" +
"6. Kreiranje video snimka\n" +
"7. Promena naziva video snimka\n" +
"8. Dodavanje kategorije video snimku\n" +
"9. Kreiranje paketa\n" +
"10. Promena mesecne cene za paket\n" +
"11. Kreiranje pretplate korisnika na paket\n" +
"12. Kreiranje gledanja video snimka od strane korisnika\n" +
"13. Kreiranje ocene korisnika za video snimak\n" +
"14. Menjanje ocene korisnika za video snimak\n" +
"15. Brisanje ocene korisnika za video snimak\n" +
"16. Brisanje video snimka od strane korisnika koji ga je kreirao\n" +
"17. Dohvatanje svih mesta\n" +
"18. Dohvatanje svih korisnika\n" +
"19. Dohvatanje svih kategorija\n" +
"20. Dohvatanje svih video snimaka\n" +
"21. Dohvatanje kategorija za određeni video snimak\n" +
"22. Dohvatanje svih paketa\n" +
"23. Dohvatanje svih pretplata za korisnika\n"+
"24. Dohvatanje svih gledanja za video snimak\n" +
"25. Dohvatanje svih ocena za video snimak");
                   
        Scanner scanner = new Scanner(System.in);
        
        String naziv;
        String email;
        int godiste;
        String pol;
        int mesto;
        int IdKor;
        int IdMes;
        String datum;
        String vreme;
        int trajanje;
        int IdVid;
        int IdKat;
        int cena;
        int IdPak;
        int pocetniSekund;
        int sekundiOdgledano;
        int ocena;
        
        
        
        
        while (true)
        {
            System.out.println("Unesite zahtev koji zelite:");

            
            int zahtev = scanner.nextInt();
            
            switch(zahtev)
            {
                case 1:
                    System.out.println("Unesite naziv mesta:");
                    naziv = scanner.next();
                    createMesto(naziv);
                    break;
                    
                case 2:
                    System.out.println("Unesite ime:");
                    System.out.println("Unesite email:");
                    System.out.println("Unesite godiste:");
                    System.out.println("Unesite pol:");
                    System.out.println("Unesite Idmesta:");
                    naziv = scanner.next();
                    email= scanner.next();
                    godiste=scanner.nextInt();
                    pol=scanner.next();
                    mesto = scanner.nextInt();
                    createKorisnik(naziv, email, godiste, pol, mesto);
                    break;
                    
                case 3:
                    System.out.println("Unesite IdKor:");
                    System.out.println("Unesite email:");

                    IdKor = scanner.nextInt();
                    email= scanner.next();

                    promeniEmail(IdKor, email);
                    break;
                    
                    
                case 4:
                    System.out.println("Unesite IdKor:");
                    System.out.println("Unesite IdMesta:");

                    IdKor = scanner.nextInt();
                    IdMes= scanner.nextInt();

                    promeniMesto(IdKor, IdMes);
                    break;
                    
                case 5:
                    System.out.println("Unesite Naziv:");

                    naziv = scanner.next();

                    createKategorija(naziv);
                    break;
                    
                case 6:
                    System.out.println("Unesite Naziv:");
                    System.out.println("Unesite Trajanje:");
                    System.out.println("Unesite IdKorisnika:");
                    System.out.println("Unesite Datum(yyyy.mm.dd):");
                    System.out.println("Unesite Vreme(hh:mm:ss):");

                    naziv = scanner.next();
                    trajanje = scanner.nextInt();
                    IdKor = scanner.nextInt();
                    datum = scanner.next();
                    vreme = scanner.next();
                    
                    createVideo(naziv,trajanje,IdKor,datum,vreme);
                    break;
                    
                    
                case 7:
                    System.out.println("Unesite IdVid:");
                    System.out.println("Unesite Naziv:");


                    IdVid = scanner.nextInt();
                    naziv = scanner.next();

                    
                    promeniNazivVidea(IdVid,naziv);
                    break;        
                    
                case 8:
                    System.out.println("Unesite IdVid:");
                    System.out.println("Unesite IdKat:");


                    IdVid = scanner.nextInt();
                    IdKat = scanner.nextInt();

                    
                    dodajKategorijuVideu(IdVid,IdKat);
                    break; 
                    
                    
                case 9:
                    System.out.println("Unesite Cena:");



                    cena = scanner.nextInt();
   

                    
                    createPaket(cena);
                    break; 
                    
                case 10:
                    System.out.println("Unesite IdPak:");
                    System.out.println("Unesite Cena:");

                    IdPak = scanner.nextInt();
                    cena = scanner.nextInt();
                    
                    promeniCenuPaketa(IdPak,cena);
                    break;
                    
                    
                case 11:
                    
                    System.out.println("Unesite IdKor:");
                    System.out.println("Unesite IdPak:");
                    System.out.println("Unesite Datum(yyyy.mm.dd):");
                    System.out.println("Unesite Vreme(hh:mm:ss):");

                    IdKor = scanner.nextInt();
                    IdPak = scanner.nextInt();
                    datum = scanner.next();
                    vreme = scanner.next();
                    
                    createPretplata(IdKor,IdPak,datum,vreme);
                    break;




                case 12:
                    
                    System.out.println("Unesite IdVid:");
                    System.out.println("Unesite IdKor:");
                    System.out.println("Unesite Datum(yyyy.mm.dd):");
                    System.out.println("Unesite Vreme(hh:mm:ss):");
                    System.out.println("Unesite PocetniSekund:");
                    System.out.println("Unesite SekundiOdgledano:");

                    IdVid = scanner.nextInt();
                    IdKor = scanner.nextInt();
                    datum = scanner.next();
                    vreme = scanner.next();
                    pocetniSekund = scanner.nextInt();
                    sekundiOdgledano = scanner.nextInt();
                    
                    createGledanje(IdVid, IdKor, datum, vreme, pocetniSekund, sekundiOdgledano);
                    break;    
                    
                    
                    
                case 13:
                    

                    System.out.println("Unesite Korisnik:");
                    System.out.println("Unesite Video:");
                    System.out.println("Unesite Ocena:");
                    System.out.println("Unesite Datum(yyyy.mm.dd):");
                    System.out.println("Unesite Vreme(hh:mm:ss):");


                    
                    IdKor = scanner.nextInt();
                    IdVid = scanner.nextInt();
                    ocena = scanner.nextInt();
                    datum = scanner.next();
                    vreme = scanner.next();

                    
                    createOcena(IdKor, IdVid, ocena, datum, vreme);
                    break;   
                    
                case 14:
                    

                    System.out.println("Unesite Korisnik:");
                    System.out.println("Unesite Video:");
                    System.out.println("Unesite Ocena:");
                    
                    IdKor = scanner.nextInt();
                    IdVid = scanner.nextInt();
                    ocena = scanner.nextInt();

                    
                    promeniOcenu(IdKor, IdVid, ocena);
                    break;  


                case 15:
                    

                    System.out.println("Unesite Korisnik:");
                    System.out.println("Unesite Video:");
                    
                    IdKor = scanner.nextInt();
                    IdVid = scanner.nextInt();
                    
                    obrisiOcenu(IdKor, IdVid);
                    break; 
                    
                    
                case 16:
                    
                    System.out.println("Unesite Video:");
                    System.out.println("Unesite Korisnik:");

                    
                   
                    IdVid = scanner.nextInt();
                    IdKor = scanner.nextInt();
                    
                    obrisiVideo(IdVid,IdKor);
                    break;    
                    
                    
                case 17:

                    
                    dohvatiSvaMesta();
                    break;      
                    
                case 18:

                    
                    dohvatiSveKorisnike();
                    break; 
 
                    
                    
                case 19:

                    
                    dohvatiSveKategorije();
                    break;    
                    
                    
                                    
                case 20:

                    
                    dohvatiSveVideoSnimke();
                    break;    
                    
                case 21:
                    System.out.println("Unesite Video:");
                    IdVid = scanner.nextInt();
                    
                    dohvatiSveKategorijeVidea(IdVid);
                    break;    
                    
                    
                case 22:
                    
                    dohvatiSvePakete();
                    break;          
                    
                case 23:
                    
                    System.out.println("Unesite IdKor:");
                    IdKor = scanner.nextInt();
                    
                    dohvatiSvePretplateKorisnika(IdKor);
                    break;  
                    
                    
                case 24:
                    
                    System.out.println("Unesite IdVid:");
                    IdVid = scanner.nextInt();
                    
                    dohvatiSvaGledanjaVidea(IdVid);
                    break;


                case 25:
                    
                    System.out.println("Unesite IdVid:");
                    IdVid = scanner.nextInt();
                    
                    dohvatiSveOceneVidea(IdVid);
                    break;                    
            }
        }
    }
}
