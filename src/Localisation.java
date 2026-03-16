import java.util.Objects;

public class Localisation {

  private long id;
  private int latitude;
  private int longitude;
  private String nom;
  private int altitude;

  public Localisation(long
      id, String nom, int latitude, int longitude, int altitude) {
    this.altitude = altitude;
    this.id = id;
    this.latitude = latitude;
    this.longitude = longitude;
    this.nom = nom;
  }

  public int getAltitude() {
    return altitude;
  }

  public void setAltitude(int altitude) {
    this.altitude = altitude;
  }

  public long getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getLatitude() {
    return latitude;
  }

  public void setLatitude(int latitude) {
    this.latitude = latitude;
  }

  public int getLongitude() {
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
    return id == that.id && latitude == that.latitude && longitude == that.longitude
        && altitude == that.altitude && Objects.equals(nom, that.nom);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, latitude, longitude, nom, altitude);
  }
}
