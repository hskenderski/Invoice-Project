package bg.codix.spring.invoice.dao.Notification;

import bg.codix.spring.invoice.dto.NotificationResponse;
import bg.codix.spring.invoice.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationDao
{
  Long addNotification(Notification notification);

  void addNotificationUser(Long userId, Long notificationId);

  Page<NotificationResponse> showNotification(Pageable page, Long loggedUserId);

  void removeUserNotifications(Long notificationId);

  List<Notification> allNotificationsOfUser(Long userId);

  void removeNotification(Long notificationId);

}
