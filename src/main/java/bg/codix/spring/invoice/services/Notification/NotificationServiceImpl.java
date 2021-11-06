package bg.codix.spring.invoice.services.Notification;


import bg.codix.spring.invoice.dao.Notification.NotificationDao;
import bg.codix.spring.invoice.dto.NotificationResponse;
import bg.codix.spring.invoice.entities.Invoice;
import bg.codix.spring.invoice.entities.Notification;
import bg.codix.spring.invoice.entities.SupplierFactor;
import bg.codix.spring.invoice.entities.User;
import bg.codix.spring.invoice.services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static bg.codix.spring.invoice.common.OutputMessages.*;

@Transactional
@Service
public class NotificationServiceImpl implements NotificationService
{
  private final NotificationDao notificationDao;
  private final UserService     userService;

  //Lazy is for: The dependencies of some of the beans in the application context form a cycle:
  @Lazy
  @Autowired
  public NotificationServiceImpl(NotificationDao notificationDao, UserService userService)
  {
    this.userService = userService;
    this.notificationDao = notificationDao;
  }

  @Override
  public Long addNotification(Notification notification)
  {
    return this.notificationDao.addNotification(notification);
  }

  @Override
  public void addNotificationUser(Long userId, Long notificationId)
  {
    this.notificationDao.addNotificationUser(userId, notificationId);
  }

  @Override
  public void insertNotification(Invoice invoice, User factor, User supplier, SupplierFactor supplierFactor, String reason)
  {
    Notification notification = new Notification();
    notification.setDate(LocalDateTime.now());
    String text = "";
    if (reason.equals("moneyOverload")) {
      text = String.format(NOTIFICATION_FOR_OVERLOAD_CONTRACT_MAX_MONEY, supplier.getUsername(), supplierFactor.getMaxMoneyAmount(), supplierFactor.getCurrentMoney().add(invoice.getMoney()));
    }
    else if (reason.equals("waitingStatus")) {
      text = String.format(NOTIFICATION_FOR_WAITING_INVOICE_APPROVE, supplier.getUsername(), invoice.getInvoiceId(), invoice.getFirmName(), invoice.getMoney());
    }

    if (reason.equals("changeSupplierLimit")) {
      text = String.format(NOTIFICATION_FOR_CHANGE_CONTRACT_MAX_MONEY, factor.getUsername(), supplierFactor.getMaxMoneyAmount());
      notification.setText(text);
      Long notificationId = addNotification(notification);
      addNotificationUser(supplier.getUserId(), notificationId);
    }
    else if (reason.equals("approveInvoice")) {
      text = String.format(NOTIFICATION_FOR_INVOICE_APPROVE, factor.getUsername(), invoice.getMoney());
      notification.setText(text);
      Long notificationId = addNotification(notification);
      addNotificationUser(supplier.getUserId(), notificationId);
    }
    else {
      notification.setText(text);
      Long notificationId = addNotification(notification);
      addNotificationUser(factor.getUserId(), notificationId);
    }
  }

  @Override
  public Page<NotificationResponse> showNotification(Pageable page)
  {
    User loggedUser = this.userService.getCurrentUser();
    return this.notificationDao.showNotification(page, loggedUser.getUserId());
  }

}
