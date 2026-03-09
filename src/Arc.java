package src;

public class Arc {
    Noeud origine;
    Noeud arrivee;
    double distance;
    String nomRue;

    public Arc(Noeud origine, Noeud arrivee, double distance, String nomRue) {
        this.origine = origine;
        this.arrivee = arrivee;
        this.distance = distance;
        this.nomRue = nomRue;
    }
}
