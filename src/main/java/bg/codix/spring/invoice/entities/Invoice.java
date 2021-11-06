package bg.codix.spring.invoice.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Invoice
{
  private Long          invoiceId;
  private String        factorUsername;
  private String        fileName;
  private String        firmName;
  private LocalDateTime issueDate;
  private BigDecimal    money;
  private String        status;


  public Invoice(String factorUsername, String fileName, String firmName, BigDecimal money)
  {
    this.factorUsername = factorUsername;
    this.fileName = fileName;
    this.firmName = firmName;
    this.money = money;
    this.issueDate = LocalDateTime.now();
  }

  public Invoice(String factorUsername, String firmName, BigDecimal money)
  {
    this.factorUsername = factorUsername;
    this.firmName = firmName;
    this.issueDate = LocalDateTime.now();
    this.money = money;
  }

  public Invoice()
  {
  }

  public Invoice(String file_name, Long invoiceId)
  {
    this.fileName = file_name;
    this.invoiceId = invoiceId;
  }


  public Invoice(BigDecimal money, String status, String factorUsername)
  {
    this.money = money;
    this.status = status;
    this.factorUsername = factorUsername;
  }

  public Invoice(String fileName)
  {
    this.fileName = fileName;
  }

  public Invoice(long invoiceId)
  {
    this.invoiceId = invoiceId;
  }

  public String getFactorUsername()
  {
    return factorUsername;
  }

  public void setFactorUsername(String factorUsername)
  {
    this.factorUsername = factorUsername;
  }

  public Long getInvoiceId()
  {
    return invoiceId;
  }

  public void setInvoiceId(Long invoiceId)
  {
    this.invoiceId = invoiceId;
  }

  public String getFileName()
  {
    return fileName;
  }

  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }

  public String getFirmName()
  {
    return firmName;
  }

  public void setFirmName(String firmName)
  {
    this.firmName = firmName;
  }

  public LocalDateTime getIssueDate()
  {
    return issueDate;
  }

  public void setIssueDate(LocalDateTime issueDate)
  {
    this.issueDate = issueDate;
  }

  public BigDecimal getMoney()
  {
    return money;
  }

  public void setMoney(BigDecimal money)
  {
    this.money = money;
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
