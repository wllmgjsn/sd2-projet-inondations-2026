public class Arc {
  Localisation origine;
  Localisation arrivee;
  double distance;
  String nomRue;

  public Arc(Localisation origine, Localisation arrivee, double distance, String nomRue) {
    this.origine = origine;
    this.arrivee = arrivee;
    this.distance = distance;
    this.nomRue = nomRue;
  }

  public Localisation getArrivee() {
    return arrivee;
  }

  public void setArrivee(Localisation arrivee) {
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

  public Localisation getOrigine() {
    return origine;
  }

  public void setOrigine(Localisation origine) {
    this.origine = origine;
  }

  @Override
  public String toString() {
    return "Rue (" + nomRue + ") vers " + arrivee.getId() + ", dist=" + distance + "m";
  }
}
