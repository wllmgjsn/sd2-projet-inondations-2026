
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
    private final Map<Localisation, Set<Arc>> listeRoadLocalisation;

    //ATTRIBUT ?
    //TODO

    public Graph(String localisations, String roads)  {
        this.listeRoadLocalisation = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(localisations))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] loca = line.split(",");
                Long id       = Long.parseLong(loca[0].trim());
                String nom    = loca[1].trim();
                double latitude  = Long.parseLong(loca[2].trim());
                double longitude = Long.parseLong(loca[3].trim());
                double altitude  = Long.parseLong(loca[4].trim());

                Localisation newLoc = new Localisation(id, nom, latitude, longitude, altitude);
                listeRoadLocalisation.put(newLoc, new HashSet<>());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(roads))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] road = line.split(",");


            }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
    }

    public Localisation[] determinerZoneInondee(long[] idsOrigin, double epsilon) {
        ArrayDeque<Localisation> file;
        Set<Localisation> dejaVisite;

        //TODO
        return null ;
    }

    public Deque<Localisation> trouverCheminLePlusCourtPourContournerLaZoneInondee(long idOrigin, long idDestination, Localisation[] floodedZone) {
        //TODO
        return null ;
    }

    public Map<Localisation,Double> determinerChronologieDeLaCrue(long[] idsOrigin, double vWaterInit,double k) {
        //TODO
        return null ;
    }

    public Deque<Localisation> trouverCheminDEvacuationLePlusCourt(long idOrigin, long idEvacuation, double vVehicule, Map<Localisation,Double> tFlood) {
        //TODO
        return null ;
    }


}
