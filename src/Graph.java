
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
    private final Map<Long, Localisation> idLocalisations;

    public Graph(String localisations, String roads)  {
        this.idLocalisations = new HashMap<>();
        this.listeRoadLocalisation = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(localisations))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] loca = line.split(",");
                Long id       = Long.parseLong(loca[0].trim());
                String nom    = loca[1].trim();
                double latitude  = Double.parseDouble(loca[2].trim());
                double longitude = Double.parseDouble(loca[3].trim());
                double altitude  = Double.parseDouble(loca[4].trim());

                Localisation newLoc = new Localisation(id, nom, latitude, longitude, altitude);
                listeRoadLocalisation.put(newLoc, new HashSet<>());
                idLocalisations.put(newLoc.getId(), newLoc);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(roads))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] road = line.split(",");
                Localisation origin = idLocalisations.get(Long.parseLong(road[0]));
                Localisation arrivee = idLocalisations.get(Long.parseLong(road[1]));
                double distance = Double.parseDouble(road[2]);
                String nomRue = road[3];
                Arc newRue = new Arc(origin, arrivee, distance, nomRue);
                listeRoadLocalisation.get(origin).add(newRue);
            }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
    }

    /**
     * ALGO 1 : SIMULATION DE LA CRUE
     *
     * @param idsOrigin Noeuds d'origine de l'inondation
     * @param epsilon Tolérance
     * @return La liste des noeuds inondés
     */
    public Localisation[] determinerZoneInondee(long[] idsOrigin, double epsilon) {
        ArrayDeque<Localisation> file;
        Set<Localisation> dejaVisite;

        //TODO
        return null ;
    }

    /**
     * ALGO 2 : NAVIGATION DE SECOURS
     *
     * @param idOrigin Noeuds d'origine
     * @param idDestination Noeud à atteindre
     * @param floodedZone La liste de noeuds inondés
     * @return Liste contenant le chemin le plus court, en nombre de rues (arcs), du noeud d'origine
     * au noeud de destination sans passer par un point inondé
     */
    public Deque<Localisation> trouverCheminLePlusCourtPourContournerLaZoneInondee(long idOrigin, long idDestination, Localisation[] floodedZone) {
        //TODO
        return null ;
    }

    /**
     * ALGO 3 : CHRONOLOGIE DE LA CRUE
     *
     * @param idsOrigin Noeuds d'origine de l'inondation
     * @param vWaterInit Vitesse initiale de l'eau
     * @param k Coefficient d'accélération de l'eau
     * @return Map associant chaque noeud au moment à partir duquel il est inondé
     */
    public Map<Localisation,Double> determinerChronologieDeLaCrue(long[] idsOrigin, double vWaterInit,double k) {
        //TODO
        return null ;
    }

    /**
     * ALGO 4 : EVACUATION DYNAMIQUE
     *
     * @param idOrigin Origine du chemin d'évacuation
     * @param idEvacuation Noeud d'évacutation
     * @param vVehicule Vitesse du véhicule
     * @param tFlood Map associant chaque noeud au moment à partir duquel il est inondé (algo. 3)
     * @return Liste de noeuds représentant le chemin le plus cours entre le noeud d'origine et le noeud d'évacuation
     */
    public Deque<Localisation> trouverCheminDEvacuationLePlusCourt(long idOrigin, long idEvacuation, double vVehicule, Map<Localisation,Double> tFlood) {
        //TODO
        return null ;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph: ")
            .append(listeRoadLocalisation.size())
            .append(" noeuds, ")
            .append(listeRoadLocalisation.values().stream().mapToInt(Set::size).sum())
            .append(" arcs\n");
        sb.append("─".repeat(50)).append("\n");

        for (Map.Entry<Localisation, Set<Arc>> entry : listeRoadLocalisation.entrySet()) {
            sb.append(entry.getKey()).append("\n");
            if (entry.getValue().isEmpty()) {
                sb.append("   (aucun arc sortant)\n");
            } else {
                for (Arc arc : entry.getValue()) {
                    sb.append("   -> ").append(arc).append("\n");
                }
            }
        }
        return sb.toString();
    }

}
