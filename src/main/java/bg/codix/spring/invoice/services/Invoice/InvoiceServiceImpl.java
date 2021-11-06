package bg.codix.spring.invoice.services.Invoice;

import bg.codix.spring.invoice.common.Validation;
import bg.codix.spring.invoice.dao.BankAccount.BankAccountDao;
import bg.codix.spring.invoice.dao.Invoice.InvoiceDao;
import bg.codix.spring.invoice.dao.User.UserDao;
import bg.codix.spring.invoice.dto.InvoiceRequest;
import bg.codix.spring.invoice.entities.BankAccount;
import bg.codix.spring.invoice.entities.Invoice;
import bg.codix.spring.invoice.entities.SupplierFactor;
import bg.codix.spring.invoice.entities.User;
import bg.codix.spring.invoice.exceptions.InvalidException;
import bg.codix.spring.invoice.services.FileStorage.FileStorageService;
import bg.codix.spring.invoice.services.Notification.NotificationService;
import bg.codix.spring.invoice.services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import static bg.codix.spring.invoice.common.ExceptionMessages.NOT_ENOUGH_MONEY;
import static bg.codix.spring.invoice.common.ExceptionMessages.UPLOAD_FILE_EX;
import static bg.codix.spring.invoice.common.OutputMessages.*;


@Transactional
@Service
public class InvoiceServiceImpl implements InvoiceService
{
  private final Validation          validation;
  private final InvoiceDao          invoiceDao;
  private final UserDao             userDao;
  private final NotificationService notificationService;
  private final BankAccountDao      bankAccountDao;
  private final UserService         userService;
  private final FileStorageService  fileStorageService;

  //Lazy is for: The dependencies of some of the beans in the application context form a cycle:
  @Lazy
  @Autowired
  public InvoiceServiceImpl(Validation validation, InvoiceDao invoiceDao, UserDao userDao, NotificationService notificationService, BankAccountDao bankAccountDao, UserService userService, FileStorageService fileStorageService)
  {
    this.validation = validation;
    this.invoiceDao = invoiceDao;
    this.userDao = userDao;
    this.notificationService = notificationService;
    this.bankAccountDao = bankAccountDao;
    this.userService = userService;
    this.fileStorageService = fileStorageService;
  }

  @Override
  public String createInvoiceInFile(InvoiceRequest invoiceRequest, MultipartFile file)
  {
    Invoice invoice = getInvoice(invoiceRequest);

    User supplier = this.userService.getCurrentUser();
    User factor = this.userDao.findUserByUsername(invoice.getFactorUsername());

    this.userDao.checkForContract(supplier.getUserId(), factor.getUserId());
    this.validation.checkForInvalidMoney(invoiceRequest.getMoney());

    SupplierFactor supplierFactor = this.userDao.getContractInformation(supplier.getUserId(), factor.getUserId());

    BankAccount factorAcc = this.bankAccountDao.findById(factor.getBankAccountId());

    //factorMoney-invoiceMoney<0
    if (factorAcc.getMoneyAmount().subtract(invoice.getMoney()).compareTo(BigDecimal.ZERO) < 0) {
      throw new InvalidException(NOT_ENOUGH_MONEY);
    }

    //supplierCurrentMoney+invoiceMoney > MaxMoneyAcc
    if (supplierFactor.getCurrentMoney().add(invoice.getMoney()).compareTo(supplierFactor.getMaxMoneyAmount()) > 0) {
      this.notificationService.insertNotification(invoice, factor, supplier, supplierFactor, "moneyOverload");
      return REACH_LIMIT;
    }
    else {
      Long invoiceId = this.invoiceDao.createInvoiceInFile(invoice, file.getOriginalFilename());
      this.userDao.updateCurrentMoneyOfSupplier(invoiceRequest.getMoney(), supplier.getUserId(), factor.getUserId());
      this.invoiceDao.insertIntoUserInvoices(supplier.getUserId(), invoiceId);
      this.bankAccountDao.updateBankAccountMoneyOfUser(supplier.getRoleName(), invoiceRequest.getMoney(), supplier.getBankAccountId());
      this.bankAccountDao.updateBankAccountMoneyOfUser(factor.getRoleName(), invoiceRequest.getMoney(), factor.getBankAccountId());
      try {
        this.fileStorageService.save(file, invoiceId);
        return SUCCESSFUL_INVOICE;
      }
      catch (Exception ex) {
        return String.format(UPLOAD_FILE_EX, file.getOriginalFilename());
      }
    }
  }

  @Override
  public String createInvoiceInDatabase(InvoiceRequest invoiceRequest)
  {
    Invoice invoice = getInvoice(invoiceRequest);
    User supplier = this.userService.getCurrentUser();
    User factor = this.userDao.findUserByUsername(invoice.getFactorUsername());

    this.userDao.checkForContract(supplier.getUserId(), factor.getUserId());
    this.validation.checkForInvalidMoney(invoiceRequest.getMoney());
    SupplierFactor supplierFactor = this.userDao.getContractInformation(supplier.getUserId(), factor.getUserId());

    if (supplierFactor.getCurrentMoney().add(invoice.getMoney()).compareTo(supplierFactor.getMaxMoneyAmount()) > 0) {
      this.notificationService.insertNotification(invoice, factor, supplier, supplierFactor, "moneyOverload");
      return REACH_LIMIT;
    }
    else {
      Long invoiceId = this.invoiceDao.createInvoiceInDatabase(invoice);
      invoice.setInvoiceId(invoiceId);
      this.invoiceDao.insertIntoUserInvoices(supplier.getUserId(), invoiceId);
      this.notificationService.insertNotification(invoice, factor, supplier, supplierFactor, "waitingStatus");
      return WAITING_APPROVE_INVOICE;
    }
  }

  @Override
  public Invoice getInvoiceFileNameById(String invoiceId)
  {
    try {
      Long.valueOf(invoiceId);
    }
    catch (NumberFormatException ex) {
      throw new InvalidException("Incorrect file name!");
    }
    return this.invoiceDao.getFileNameById(Long.valueOf(invoiceId));
  }


  private Invoice getInvoice(InvoiceRequest invoiceRequest)
  {
    return new Invoice(
        invoiceRequest.getFactorUsername()
        , invoiceRequest.getFirmName()
        , invoiceRequest.getMoney()
    );
  }
}