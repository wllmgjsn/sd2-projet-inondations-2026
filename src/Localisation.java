import java.util.Objects;

public class Localisation {

  private Long id;
  private double latitude;
  private double longitude;
  private String nom;
  private double altitude;

  public Localisation(Long id, String nom, double latitude, double longitude, double altitude) {
    this.altitude = altitude;
    this.id = id;
    this.latitude = latitude;
    this.longitude = longitude;
    this.nom = nom;
  }

  public double getAltitude() {
    return altitude;
  }

  public void setAltitude(int altitude) {
    this.altitude = altitude;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(int longitude) {
    this.longitude = longitude;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Localisation that = (Localisation) o;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "[NOEUD= " + id + ", ALTITUDE= " + altitude + "m]";
  }

}
