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

  public Noeud getArrivee() {
    return arrivee;
  }

  public void setArrivee(Noeud arrivee) {
    this.arrivee = arrivee;
  }

  public double getDistance() {
    return distance;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

  public String getNomRue() {
    return nomRue;
  }

  public void setNomRue(String nomRue) {
    this.nomRue = nomRue;
  }

  public Noeud getOrigine() {
    return origine;
  }

  public void setOrigine(Noeud origine) {
    this.origine = origine;
  }
}
