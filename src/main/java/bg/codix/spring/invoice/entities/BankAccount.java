package bg.codix.spring.invoice.entities;

import java.math.BigDecimal;

public class BankAccount
{
  private int        accId;
  private String     iban;
  private BigDecimal moneyAmount;

  public BankAccount(String iban, BigDecimal moneyAmount)
  {
    this.iban = iban;
    this.moneyAmount = moneyAmount;
  }

  public BankAccount()
  {
  }

  public BankAccount(String iban)
  {
    this.iban = iban;
  }

  public int getAccId()
  {
    return accId;
  }

  public void setAccId(int accId)
  {
    this.accId = accId;
  }

  public String getIban()
  {
    return iban;
  }

  public void setIban(String iban)
  {
    this.iban = iban;
  }

  public BigDecimal getMoneyAmount()
  {
    return moneyAmount;
  }

  public void setMoneyAmount(BigDecimal moneyAmount)
  {
    this.moneyAmount = moneyAmount;
  }
}
