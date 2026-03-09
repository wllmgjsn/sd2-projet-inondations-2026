package src;

public class Localisation {

  private int id;
  private int latitude;
  private int longitude;
  private String nom;
  private int altitude;

  public Localisation(int altitude, int id, int latitude, int longitude, String nom) {
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

  public int getId() {
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
}
