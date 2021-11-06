package bg.codix.spring.invoice.dto;

import bg.codix.spring.invoice.entities.User;

import java.math.BigDecimal;

public class UserDto
{
  private String     firstName;
  private String     middleName;
  private String     lastName;
  private String     email;
  private String     username;
  private BigDecimal factorContractDefaultSum;
  private String url;

  public UserDto(long user_id, String first_name, String middle_name, String last_name, int age,
                 String email, String username, String password, BigDecimal salary,
                 BigDecimal factor_contract_default_sum, User.RoleName role, long bank_acc_id, long address_id)
  {
    this.firstName = first_name;
    this.middleName = middle_name;
    this.lastName = last_name;
    this.email = email;
    this.username = username;
    this.factorContractDefaultSum = factor_contract_default_sum;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl(String url)
  {
    this.url = url;
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

  public BigDecimal getFactorContractDefaultSum()
  {
    return factorContractDefaultSum;
  }

  public void setFactorContractDefaultSum(BigDecimal factorContractDefaultSum)
  {
    this.factorContractDefaultSum = factorContractDefaultSum;
  }
}
