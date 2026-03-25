
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.HashSet;

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

    /**
     * ALGO 1 : SIMULATION DE LA CRUE
     *
     * @param idsOrigin Noeuds d'origine de l'inondation
     * @param epsilon Tolérance
     * @return La liste des noeuds inondés
     */
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
                file.add(depart);
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
        return resultat.toArray(new Localisation[0]) ;
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
        Set<Localisation> zoneInondee = new HashSet<>(Arrays.asList(floodedZone));

        Localisation depart = null;
        Localisation arrivee = null;

        for (Localisation localisation : listeRoadLocalisation.keySet()) {
            if (localisation.getId() == idOrigin)
                depart = localisation;
            if (localisation.getId() == idDestination)
                arrivee = localisation;
        }

        Queue<Object[]> file = new PriorityQueue<>((a, b) -> (int) a[1] - (int) b[1]);
        file.add(new Object[]{depart, 0});

        Set<Localisation> dejaVisite = new HashSet<>();

        // Key = distination, value = depart
        Map<Localisation, Localisation> parcours = new HashMap<>();

        // Dijkstra
        while (!file.isEmpty()) {
            Object[] top = file.poll();
            Localisation src = (Localisation) top[0];
            int cout = (int) top[1];

            // Ignorer noeud deja visite
            if (dejaVisite.contains(src))
                continue;
            dejaVisite.add(src);

            // Arreter la boucle : destination atteinte
            if (src.equals(arrivee))
                break;

            // Reprendre les arcs sortant du noeud courant
            Set<Arc> arcs = listeRoadLocalisation.get(src);

            // Aucun arcs
            if (arcs == null)
                continue;

            for (Arc arc : arcs) {
                Localisation destination = arc.getArrivee();

                // Ignorer les arcs inondés
                if (zoneInondee.contains(destination))
                    continue;

                if (!dejaVisite.contains(destination)) {
                    if (!parcours.containsKey(destination)) {
                        parcours.put(destination, src);
                    }
                    file.add(new Object[]{destination, cout+1});
                }
            }
        }

        if (!dejaVisite.contains(arrivee)) return  null;

        Deque<Localisation> chemin = new ArrayDeque<>();
        Localisation localisation = arrivee;
        while (localisation != null) {
            chemin.addFirst(localisation);
            localisation = parcours.get(localisation);
        }

        return chemin;
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
        PriorityQueue<Etat> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(
            Etat::getTemps));
        Map<Localisation, Double> tflood = new HashMap<>();

        for(Localisation l : idLocalisations.values()){
            tflood.put(l, Double.POSITIVE_INFINITY);
        }

        for(Long id : idsOrigin){
            Localisation l = idLocalisations.get(id);
            tflood.put(l, 0.0);
            priorityQueue.add(new Etat(l, 0.0, vWaterInit));
        }

        while(!priorityQueue.isEmpty()){
            Etat current = priorityQueue.poll();

            if(current.getTemps()  > tflood.get(current.getLocalisation())){
                continue;
            }

            Set<Arc> arcs = listeRoadLocalisation.get(current.getLocalisation());

            for(Arc a : arcs){
                Localisation voisin = a.arrivee;
                double pente = (current.getLocalisation().getAltitude() - voisin.getAltitude()) / a.distance;
                double nouvelleVitesse = current.getVitesse() + (k*pente);

                if(nouvelleVitesse <= 0){
                    continue;
                }

                double tempsArc = a.distance/nouvelleVitesse;
                double nouveauT = current.getTemps() + tempsArc;

                if(nouveauT < tflood.get(voisin)){
                    tflood.put(voisin, nouveauT);
                    priorityQueue.add(new Etat(voisin, nouveauT, nouvelleVitesse));
                }
            }
        }

        return tflood;
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
