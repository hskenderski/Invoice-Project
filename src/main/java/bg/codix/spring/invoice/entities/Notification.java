package bg.codix.spring.invoice.entities;

import java.time.LocalDateTime;

public class Notification
{
  private Long          notificationId;
  private String        text;
  private LocalDateTime date;

  public Notification()
  {
  }

  public Notification(long notificationId, String text, LocalDateTime issueDate)
  {
    this.notificationId = notificationId;
    this.text = text;
    this.date = issueDate;
  }

  public Notification(long notificationId)
  {
    this.notificationId = notificationId;
  }

  public String getText()
  {
    return text;
  }

  public void setNotificationId(Long notificationId)
  {
    this.notificationId = notificationId;
  }

  public void setDate(LocalDateTime date)
  {
    this.date = date;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public LocalDateTime getDate()
  {
    return date;
  }

  public Long getNotificationId()
  {
    return notificationId;
  }


}
