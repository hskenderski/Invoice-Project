package bg.codix.spring.invoice.entities;

public class Address
{
  private Long   addressId;
  private String country;
  private String region;
  private String city;
  private String neighborhood;
  private String street;
  private String addressNumber;
  private int    floor;

  public Address(String country, String region,
                 String city, String neighborhood,
                 String street, String addressNumber, int floor)
  {
    this.country = country;
    this.region = region;
    this.city = city;
    this.neighborhood = neighborhood;
    this.street = street;
    this.addressNumber = addressNumber;
    this.floor = floor;
  }

  public Address()
  {
  }

  public Long getAddressId()
  {
    return addressId;
  }

  public void setAddressId(Long addressId)
  {
    this.addressId = addressId;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  public String getRegion()
  {
    return region;
  }

  public void setRegion(String region)
  {
    this.region = region;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public String getNeighborhood()
  {
    return neighborhood;
  }

  public void setNeighborhood(String neighborhood)
  {
    this.neighborhood = neighborhood;
  }

  public String getStreet()
  {
    return street;
  }

  public void setStreet(String street)
  {
    this.street = street;
  }

  public String getAddressNumber()
  {
    return addressNumber;
  }

  public void setAddressNumber(String address_number)
  {
    this.addressNumber = address_number;
  }

  public int getFloor()
  {
    return floor;
  }

  public void setFloor(int floor)
  {
    this.floor = floor;
  }
}
