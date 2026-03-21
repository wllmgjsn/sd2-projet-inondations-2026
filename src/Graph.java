
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {
    private Map<Localisation, Set<Arc>> listeRoadLocalisation;




    //ATTRIBUT ?
    //TODO

    public Graph(String localisations, String roads)  {
        try (BufferedReader br = new BufferedReader(new FileReader(localisations))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] mots = line.split(","); // ← ton séparateur ici
                for (String mot : mots) {
                    System.out.println(mot.trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Localisation[] determinerZoneInondee(long[] idsOrigin, double epsilon) {
        ArrayDeque<Localisation> file = new ArrayDeque<>();
        Set<Localisation> dejaVisite = new HashSet<>();
        List<Localisation> resultat = new ArrayList<>();

        for(long idOrigin: idsOrigin){
            Localisation depart = null;

            for(Localisation l: listeRoadLocalisation.keySet()){
                if(l.getId() == idOrigin){
                    depart = l;
                    break;
                }
            }

            if(depart != null && !dejaVisite.contains(depart)){
                file.addFirst(depart);
                dejaVisite.add(depart);
                resultat.add(depart);
            }
        }

        while(!file.isEmpty()){
            Localisation courant = file.removeFirst();

            for(Arc a : listeRoadLocalisation.get(courant)){
                if(!dejaVisite.contains(a.arrivee) && a.arrivee.getAltitude() <= a.origine.getAltitude() + epsilon){
                    file.add(a.arrivee);
                    dejaVisite.add(a.arrivee);
                    resultat.add(a.arrivee);
                }
            }
        }
        //TODO
        return resultat.toArray(new Localisation[0]) ;
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
