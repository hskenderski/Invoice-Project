package bg.codix.spring.invoice.entities;

import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;

import java.math.BigDecimal;

public class User implements GrantedAuthority
{
  private Long       userId;
  private String     firstName;
  private String     middleName;
  private String     lastName;
  private Integer    age;
  private String     email;
  private String     username;
  private String     password;
  private BigDecimal salary;
  private BigDecimal factorContractDefaultSum;
  private boolean    isLock;
  private RoleName   roleName;
  private Long       bankAccountId;
  private Long       addressId;

  public User(Long userId, String firstName, String middleName, String lastName,
              int age, String email, String username,
              String password, BigDecimal salary, BigDecimal factorContractDefaultSum, RoleName roleName, Long bankAccountId, Long addressId)
  {
    this(firstName, middleName, lastName, age, email, username, password, salary, factorContractDefaultSum, roleName, bankAccountId, addressId);
    setUserId(userId);
  }

  public User(String firstName, String middleName, String lastName,
              int age, String email, String username,
              String password, BigDecimal salary, BigDecimal factorContractDefaultSum, RoleName roleName, Long bankAccountId, Long addressId)
  {
    setFirstName(firstName);
    setLastName(lastName);
    setMiddleName(middleName);
    setAge(age);
    setEmail(email);
    setUsername(username);
    setPassword(password);
    setSalary(salary);
    setRoleName(roleName);
    setLock(false);
    setBankAccountId(bankAccountId);
    setAddressId(addressId);
    setFactorContractDefaultSum(factorContractDefaultSum);
  }

  public User()
  {
    setLock(false);
  }

  public User(long userId, String firstName, String middleName, String lastName, int age, String email,
              String username, String password, BigDecimal salary, BigDecimal factorContractDefaultSum,
              RoleName role, boolean isLock, long bankAccId, long addressId)
  {
    setUserId(userId);
    setFirstName(firstName);
    setLastName(lastName);
    setMiddleName(middleName);
    setAge(age);
    setEmail(email);
    setUsername(username);
    setPassword(password);
    setSalary(salary);
    setRoleName(role);
    setBankAccountId(bankAccId);
    setAddressId(addressId);
    setFactorContractDefaultSum(factorContractDefaultSum);
    setLock(isLock);
  }

  public User(String firstName, String middleName, String lastName, int age, String email, String username)
  {
    this.firstName=firstName;
    this.middleName=middleName;
    this.lastName=lastName;
    this.age=age;
    this.email=email;
    this.username=username;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId(Long userId)
  {
    this.userId = userId;
  }

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

  public BigDecimal getSalary()
  {
    return salary;
  }

  public void setSalary(BigDecimal salary)
  {
    this.salary = salary;
  }

  public RoleName getRoleName()
  {
    return roleName;
  }

  public boolean isLock()
  {
    return isLock;
  }

  public void setRoleName(RoleName roleName)
  {
    this.roleName = roleName;
  }

  public void setAge(Integer age)
  {
    this.age = age;
  }

  public void setLock(boolean lock)
  {
    isLock = lock;
  }

  public Long getBankAccountId()
  {
    return bankAccountId;
  }

  public void setBankAccountId(Long bankAccountId)
  {
    this.bankAccountId = bankAccountId;
  }

  public Long getAddressId()
  {
    return addressId;
  }

  public void setAddressId(Long addressId)
  {
    this.addressId = addressId;
  }

  public BigDecimal getFactorContractDefaultSum()
  {
    return factorContractDefaultSum;
  }

  public void setFactorContractDefaultSum(BigDecimal factorContractDefaultSum)
  {
    this.factorContractDefaultSum = factorContractDefaultSum;
  }

  public enum RoleName
  {
    SUPPLIER,
    FACTOR,
    ADMIN
  }

  @Transient
  @Override
  public String getAuthority()
  {
    return "ROLE_" + roleName.toString();
  }

}
