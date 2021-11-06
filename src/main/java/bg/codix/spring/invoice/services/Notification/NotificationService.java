package bg.codix.spring.invoice.services.Notification;

import bg.codix.spring.invoice.dto.NotificationResponse;
import bg.codix.spring.invoice.entities.Invoice;
import bg.codix.spring.invoice.entities.Notification;
import bg.codix.spring.invoice.entities.SupplierFactor;
import bg.codix.spring.invoice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService
{
  //Ивета
  Long addNotification(Notification notification);

  //Ивета
  void addNotificationUser(Long userId, Long notificationId);

  //Християн
  void insertNotification(Invoice invoice, User factor, User supplier, SupplierFactor supplierFactor, String reason);

  //Ивета
  Page<NotificationResponse> showNotification(Pageable page);
}
