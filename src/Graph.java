package src;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Set;

public class Graph {

    private Map<Localisation, Set<Arc>> listeRoadLocalisation;



	//ATTRIBUT ?
	//TODO

    public Graph(String localisations, String roads)  {

        //TODO
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
