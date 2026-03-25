import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;

public class Graph {

  private final Map<Localisation, Set<Arc>> listeRoadLocalisation;
  private final Map<Long, Localisation> idLocalisations;

  public Graph(String localisations, String roads) {
    this.idLocalisations = new HashMap<>();
    this.listeRoadLocalisation = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader(localisations))) {
      String line;
      br.readLine();
      while ((line = br.readLine()) != null) {
        String[] loca = line.split(",");
        Long id = Long.parseLong(loca[0].trim());
        String nom = loca[1].trim();
        double latitude = Double.parseDouble(loca[2].trim());
        double longitude = Double.parseDouble(loca[3].trim());
        double altitude = Double.parseDouble(loca[4].trim());

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
        Deque<Localisation> resultat = new ArrayDeque<>();
        Set<Localisation> dejaVisite = new HashSet<>();
        Queue<Localisation> file = new LinkedList<>();
        Map<Localisation, Localisation> predecesseurs = new HashMap<>();


        Localisation depart = idLocalisations.get(idOrigin);
        Localisation arrivee = idLocalisations.get(idDestination);


        dejaVisite.add(depart);
        file.add(depart);
        predecesseurs.put(depart, null);

        // --- BFS ---
        while (!file.isEmpty()) {
            Localisation courant = file.poll();
            Set<Arc> adjacents = listeRoadLocalisation.get(courant);

            if (courant.equals(arrivee)) {
                Localisation l = arrivee;
                while (l != null) {
                    resultat.addFirst(l);
                    l = predecesseurs.get(l);
                }
                break;
            }

            // Explore chaque voisin du noead courant
            for (Arc adjacent : adjacents) {
                if (!dejaVisite.contains(adjacent.arrivee) && !zoneInondee.contains(adjacent.arrivee)) {
                    file.add(adjacent.arrivee);
                    dejaVisite.add(adjacent.arrivee);
                    predecesseurs.put(adjacent.arrivee, courant);
                }
            }
        }

        return resultat;
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

        for (Localisation l : idLocalisations.values()) {
            tflood.put(l, Double.POSITIVE_INFINITY);
        }

        for (long id : idsOrigin) {
            Localisation l = idLocalisations.get(id);
            if (l != null) {
                tflood.put(l, 0.0);
                priorityQueue.add(new Etat(l, 0.0, vWaterInit));
            }
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

                if (nouveauT < tflood.getOrDefault(voisin, Double.POSITIVE_INFINITY)) {
                    tflood.put(voisin, nouveauT);
                    priorityQueue.add(new Etat(voisin, nouveauT, nouvelleVitesse));
                }
            }

            tflood.entrySet().removeIf(e -> Double.isInfinite(e.getValue()));
        }

        return tflood;
    }

  /**
   * ALGO 4 : EVACUATION DYNAMIQUE
   *
   * @param idOrigin     Origine du chemin d'évacuation
   * @param idEvacuation Noeud d'évacutation
   * @param vVehicule    Vitesse du véhicule
   * @param tFlood       Map associant chaque noeud au moment à partir duquel il est inondé (algo.
   *                     3)
   * @return Liste de noeuds representant le chemin le plus cours entre le noeud d'origine et le
   * noeud d'évacuation
   */
  public Deque<Localisation> trouverCheminDEvacuationLePlusCourt(long idOrigin, long idEvacuation,
      double vVehicule, Map<Localisation, Double> tFlood) {


    Localisation origin = idLocalisations.get(idOrigin);
    Localisation evac = idLocalisations.get(idEvacuation);

    if (tFlood.getOrDefault(origin, Double.POSITIVE_INFINITY) <= 0.0) {
      return new ArrayDeque<>();
    }

    Set<Localisation> noeudsFixes = new HashSet<>();
    PriorityQueue<Map.Entry<Localisation, Double>> tempsProvisoiresVehicule = new PriorityQueue<>(
        Comparator.comparingDouble(Map.Entry::getValue));
    Map<Localisation, Double> meilleursTemps = new HashMap<>();
    Map<Localisation, Localisation> predecesseurs = new HashMap<>();

    tempsProvisoiresVehicule.add(new SimpleEntry<>(origin, 0.0));

    meilleursTemps.put(origin, 0.0);

    while (!tempsProvisoiresVehicule.isEmpty()) {
      Map.Entry<Localisation, Double> top = tempsProvisoiresVehicule.poll();
      Localisation pos = top.getKey();

      double tCumuleVehicule = top.getValue();

      if (tCumuleVehicule > meilleursTemps.getOrDefault(pos, Double.POSITIVE_INFINITY)) {
        continue;
      }

      if (noeudsFixes.contains(pos)) {
        continue;
      }
      noeudsFixes.add(pos);

      if (pos.equals(evac)) {
        break;
      }

      for (Arc rue : listeRoadLocalisation.get(pos)) {
        Localisation voisin = rue.getArrivee();
        double tArrivee = tCumuleVehicule + rue.getDistance() / vVehicule;
        Double tInondation = tFlood.get(rue.getArrivee());

        if (tInondation != null && tArrivee >= tInondation) {
          continue;
        }

        if (!noeudsFixes.contains(voisin)
            && tArrivee < meilleursTemps.getOrDefault(voisin, Double.POSITIVE_INFINITY)) {
          meilleursTemps.put(voisin, tArrivee);
          predecesseurs.put(voisin, pos);
          tempsProvisoiresVehicule.add(new SimpleEntry<>(voisin, tArrivee));
        }
      }
    }
    ArrayDeque<Localisation> chemin = new ArrayDeque<>();
    Localisation courant = evac;
    while (courant != null) {
      chemin.addFirst(courant);
      courant = predecesseurs.get(courant);
    }
    return chemin.getFirst().equals(origin) ? chemin : new ArrayDeque<>();
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
