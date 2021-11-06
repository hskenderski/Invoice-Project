package bg.codix.spring.invoice.dto;

import java.util.Date;

public class NotificationResponse
{
  private String text;
  private Date   date;

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }
}
