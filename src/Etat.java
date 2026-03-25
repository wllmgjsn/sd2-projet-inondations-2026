public class Etat {
  private Localisation localisation;
  private double temps;
  private double vitesse;

  public Etat(Localisation localisation, double temps, double vitesse) {
    this.localisation = localisation;
    this.temps = temps;
    this.vitesse = vitesse;
  }

  public Localisation getLocalisation() {
    return localisation;
  }

  public void setLocalisation(Localisation localisation) {
    this.localisation = localisation;
  }

  public double getTemps() {
    return temps;
  }

  public void setTemps(double temps) {
    this.temps = temps;
  }

  public double getVitesse() {
    return vitesse;
  }

  public void setVitesse(double vitesse) {
    this.vitesse = vitesse;
  }
}
