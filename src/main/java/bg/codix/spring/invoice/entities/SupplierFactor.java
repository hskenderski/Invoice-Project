package bg.codix.spring.invoice.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SupplierFactor
{
  private Long          supplierId;
  private Long          factorId;
  private BigDecimal    maxMoneyAmount;
  private BigDecimal    currentMoney;
  private LocalDateTime issueDate;

  public SupplierFactor()
  {
  }

  public SupplierFactor(Long supplierId, Long factorId, BigDecimal maxMoneyAmount, BigDecimal currentMoney, LocalDateTime issueDate)
  {
    this(maxMoneyAmount, currentMoney);
    this.supplierId = supplierId;
    this.factorId = factorId;
    this.issueDate = issueDate;
  }

  public SupplierFactor(BigDecimal maxMoneyAmount, BigDecimal currentMoney)
  {
    this.maxMoneyAmount = maxMoneyAmount;
    this.currentMoney = currentMoney;
  }

  public Long getSupplierId()
  {
    return supplierId;
  }

  public void setSupplierId(Long supplierId)
  {
    this.supplierId = supplierId;
  }

  public Long getFactorId()
  {
    return factorId;
  }

  public void setFactorId(Long factorId)
  {
    this.factorId = factorId;
  }

  public BigDecimal getMaxMoneyAmount()
  {
    return maxMoneyAmount;
  }

  public void setMaxMoneyAmount(BigDecimal maxMoneyAmount)
  {
    this.maxMoneyAmount = maxMoneyAmount;
  }

  public BigDecimal getCurrentMoney()
  {
    return currentMoney;
  }

  public void setCurrentMoney(BigDecimal currentMoney)
  {
    this.currentMoney = currentMoney;
  }

  public LocalDateTime getIssueDate()
  {
    return issueDate;
  }

  public void setIssueDate(LocalDateTime issueDate)
  {
    this.issueDate = issueDate;
  }
}
