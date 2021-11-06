package bg.codix.spring.invoice.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class UserRequest
{
  private String     firstName;
  private String     middleName;
  private String     lastName;
  @Min(value = 1, message = "Your age can't be negative number!")
  @Max(value = 115, message = "You enter invalid age!")
  private int        age;
  private String     email;
  private String     username;
  private String     password;
  private String     passwordConfirmation;
  @Min(value = 1, message = "You enter invalid salary!")
  private BigDecimal salary;
  @Min(value = 1, message = "You enter invalid sum!")
  private BigDecimal factorContractDefaultSum;
  private boolean    isLock;
  private String     roleName;
  private String     iban;
  @Min(value = 1, message = "You enter invalid sum in your bank account!")
  private BigDecimal moneyAmount;
  private String     country;
  private String     region;
  private String     city;
  private String     neighborhood;
  private String     street;
  private String     addressNumber;
  @Min(value = 1, message = "You enter invalid floor!")
  private int        floor;
  private String     status = "о";

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public String getMiddleName()
  {
    return middleName;
  }

  public void setMiddleName(String middleName)
  {
    this.middleName = middleName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public int getAge()
  {
    return age;
  }

  public void setAge(int age)
  {
    this.age = age;
  }

  public String getEmail()
  {
    return email;
  }

  public boolean isLock()
  {
    return isLock;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getPasswordConfirmation()
  {
    return passwordConfirmation;
  }

  public void setPasswordConfirmation(String passwordConfirmation)
  {
    this.passwordConfirmation = passwordConfirmation;
  }

  public BigDecimal getSalary()
  {
    return salary;
  }

  public void setSalary(BigDecimal salary)
  {
    this.salary = salary;
  }

  public String getRoleName()
  {
    return roleName;
  }

  public void setRoleName(String roleName)
  {
    this.roleName = roleName;
  }

  public void setLock(boolean lock)
  {
    isLock = lock;
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

  public void setAddressNumber(String addressNumber)
  {
    this.addressNumber = addressNumber;
  }

  public int getFloor()
  {
    return floor;
  }

  public void setFloor(int floor)
  {
    this.floor = floor;
  }

  public BigDecimal getMoneyAmount()
  {
    return moneyAmount;
  }

  public void setIban(String iban)
  {
    this.iban = iban;
  }

  public String getIban()
  {
    return iban;
  }

  public void setMoneyAmount(BigDecimal moneyAmount)
  {
    this.moneyAmount = moneyAmount;
  }

  public BigDecimal getFactorContractDefaultSum()
  {
    return factorContractDefaultSum;
  }

  public void setFactorContractDefaultSum(BigDecimal factorContractDefaultSum)
  {
    this.factorContractDefaultSum = factorContractDefaultSum;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }
}
