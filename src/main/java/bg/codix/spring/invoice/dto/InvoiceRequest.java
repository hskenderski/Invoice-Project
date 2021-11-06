package bg.codix.spring.invoice.dto;

import java.math.BigDecimal;

public class InvoiceRequest
{
  private String     factorUsername;
  private String     firmName;
  private BigDecimal money;

  public String getFirmName()
  {
    return firmName;
  }

  public void setFirmName(String firmName)
  {
    this.firmName = firmName;
  }

  public BigDecimal getMoney()
  {
    return money;
  }

  public void setMoney(BigDecimal money)
  {
    this.money = money;
  }

  public String getFactorUsername()
  {
    return factorUsername;
  }

  public void setFactorUsername(String factorUsername)
  {
    this.factorUsername = factorUsername;
  }
}
