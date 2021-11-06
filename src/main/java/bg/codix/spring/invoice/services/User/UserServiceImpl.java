package bg.codix.spring.invoice.services.User;

import bg.codix.spring.invoice.common.Validation;
import bg.codix.spring.invoice.controllers.UserController;
import bg.codix.spring.invoice.dao.BankAccount.BankAccountDao;
import bg.codix.spring.invoice.dao.Invoice.InvoiceDao;
import bg.codix.spring.invoice.dao.User.UserDao;
import bg.codix.spring.invoice.dto.FileInfoDto;
import bg.codix.spring.invoice.dto.UserDto;
import bg.codix.spring.invoice.dto.UserRequest;
import bg.codix.spring.invoice.dto.UserResponse;
import bg.codix.spring.invoice.entities.*;
import bg.codix.spring.invoice.exceptions.InvalidException;
import bg.codix.spring.invoice.services.Address.AddressService;
import bg.codix.spring.invoice.services.BankAccount.BankAccountService;
import bg.codix.spring.invoice.services.FileStorage.FileStorageService;
import bg.codix.spring.invoice.services.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static bg.codix.spring.invoice.common.ExceptionMessages.*;
import static bg.codix.spring.invoice.common.OutputMessages.*;

@Transactional
@Service
public class UserServiceImpl implements UserService
{
  private final Validation          validation;
  private final UserDao             userDao;
  private final InvoiceDao          invoiceDao;
  private final BankAccountDao      bankAccountDao;
  private final BankAccountService  bankAccountService;
  private final PasswordEncoder     passwordEncoder;
  private final AddressService      addressService;
  private final NotificationService notificationService;
  private final FileStorageService  fileStorageService;


  @Autowired
  public UserServiceImpl(UserDao userDao, InvoiceDao invoiceDao, BankAccountDao bankAccountDao,
                         BankAccountService bankAccountService, AddressService addressService,
                         NotificationService notificationService, Validation validation, FileStorageService fileStorageService)
  {
    this.validation = validation;
    this.invoiceDao = invoiceDao;
    this.bankAccountDao = bankAccountDao;
    this.userDao = userDao;
    this.bankAccountService = bankAccountService;
    this.addressService = addressService;
    this.fileStorageService = fileStorageService;
    this.passwordEncoder = new BCryptPasswordEncoder();
    this.notificationService = notificationService;
  }

  public void createUser(UserRequest userRequest)
  {
    if (!(userRequest.getRoleName().equals("SUPPLIER") || userRequest.getRoleName().equals("FACTOR"))) {
      throw new InvalidException(INVALID_ROLE);
    }

    this.validation.emailAndUsernameValidation(userRequest.getEmail(), userRequest.getUsername());
    this.validation.passwordValidation(userRequest.getPassword(), userRequest.getPasswordConfirmation());


    BankAccount bankAccount = new BankAccount(userRequest.getIban(), userRequest.getMoneyAmount());
    Long bankId = this.bankAccountService.createBankAccount(bankAccount);

    String encodedPass = passwordEncoder.encode(userRequest.getPassword());

    Address address = new Address(userRequest.getCountry()
        , userRequest.getRegion()
        , userRequest.getCity()
        , userRequest.getNeighborhood()
        , userRequest.getStreet()
        , userRequest.getAddressNumber()
        , userRequest.getFloor());
    Long addressId = this.addressService.createAddress(address);


    User user = new User(
        userRequest.getFirstName()
        , userRequest.getMiddleName()
        , userRequest.getLastName()
        , userRequest.getAge()
        , userRequest.getEmail()
        , userRequest.getUsername()
        , encodedPass
        , userRequest.getSalary()
        , userRequest.getFactorContractDefaultSum()
        , User.RoleName.valueOf(userRequest.getRoleName())
        , bankId
        , addressId);


    this.userDao.addUser(user);
  }

  public List<FileInfoDto> findFileNamesOfFactorByEmail(Pageable page)
  {
    List<Invoice> invoices = this.userDao.findFileNamesOfFactorByEmail(page, getCurrentUser().getUsername());
    List<FileInfoDto> fileInfoDtos = new ArrayList<>();
    for (Invoice invoice : invoices) {
      Resource resource = fileStorageService.load(String.valueOf(invoice.getInvoiceId()));
      FileInfoDto fileInfoDto = new FileInfoDto(String.valueOf(invoice.getInvoiceId()),
          MvcUriComponentsBuilder.fromMethodName(UserController.class, "getFile",
              resource.getFilename()).build().toString());
      fileInfoDtos.add(fileInfoDto);
    }
    return fileInfoDtos;
  }

  @Override
  public Page<User> getAllUsers(Pageable page)
  {
    return this.userDao.getAllUser(page);
  }

  @Override
  public Page<UserDto> showAllFactors(Pageable page)
  {
    Page<UserDto>userDto = this.userDao.showAllFactors(page);
    for (UserDto dto : userDto) {
      dto.setUrl(MvcUriComponentsBuilder.fromMethodName(UserController.class,
          "insertContract",
          dto.getUsername()).build().toString());
    }
    return userDto;

  }

  @Override
  public void createContract(String username)
  {
    User supplierLoggedUser = getCurrentUser();
    User factor = this.userDao.findUserByUsername(username);
    if (!User.RoleName.FACTOR.equals(factor.getRoleName())) {
      throw new InvalidException(USER_NOT_FACTOR);
    }
    this.userDao.insertContractBetweenSupplierAndFactor(supplierLoggedUser.getUserId(), factor.getUserId(),
        factor.getFactorContractDefaultSum(), BigDecimal.valueOf(0), LocalDateTime.now());
  }

  @Override
  public User getCurrentUser()
  {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return this.userDao.findUserByEmail(auth.getName());
  }

  @Override
  public void updateMoneyAmount(String username, BigDecimal newMoney)
  {
    User supplier = this.userDao.findUserByUsername(username);
    User factor = getCurrentUser();
    this.userDao.checkForContract(supplier.getUserId(), factor.getUserId());
    SupplierFactor supplierFactor = this.userDao.getContractInformation(supplier.getUserId(), factor.getUserId());

    this.validation.checkForWrongUpdateOfMoney(newMoney, supplierFactor.getCurrentMoney());

    supplierFactor.setMaxMoneyAmount(newMoney);

    this.notificationService.insertNotification(new Invoice(), factor, supplier, supplierFactor, "changeSupplierLimit");
    this.userDao.refactorMaxMoneyAmount(supplier.getUserId(), newMoney, factor.getUserId());
  }

  @Override
  public String lockUserAccount(String email, boolean isAccountLocked)
  {
    User user = this.userDao.findUserByEmail(email);
    if ("ex".equals(user.getEmail())) {
      throw new InvalidException(INVALID_EMAIL);
    }
    if (isAccountLocked) {
      if ("ADMIN".equals(user.getRoleName().name())) {
        throw new InvalidException(ADMIN_CANT_BE_LOCK);
      }
      this.userDao.lockAccountOfUser(email, isAccountLocked);
      return SUCCESSFUL_LOCK;
    }
    /*    Set in database column-ROLE    */
    this.userDao.lockAccountOfUser(email, isAccountLocked);
    return SUCCESSFUL_UNLOCK;
  }


  public String approveInvoiceInDatabase(boolean approveOrDenyStatus, Long number)
  {
    User factor = getCurrentUser();
    User supplier = this.userDao.findUserByInvoiceId(number);
    this.userDao.checkForContract(supplier.getUserId(), factor.getUserId());
    Invoice invoice = this.invoiceDao.findInvoiceById(number);

    BankAccount factorAcc = this.bankAccountDao.findById(factor.getBankAccountId());

    if (!factor.getUsername().equals(invoice.getFactorUsername())) {
      throw new InvalidException(INVALID_INVOICE_NUMBER);
    }

    if ("WAITING".equals(invoice.getStatus())) {
      if (approveOrDenyStatus) {
        //factorMoney-invoiceMoney<0
        if (factorAcc.getMoneyAmount().subtract(invoice.getMoney()).compareTo(BigDecimal.ZERO) < 0) {
          throw new InvalidException(NOT_ENOUGH_MONEY);
        }
        this.invoiceDao.changeInvoiceStatus("APPROVED", number);
        this.userDao.updateCurrentMoneyOfSupplier(invoice.getMoney(), supplier.getUserId(), factor.getUserId());
        this.bankAccountDao.updateBankAccountMoneyOfUser(supplier.getRoleName(), invoice.getMoney(), supplier.getBankAccountId());
        this.bankAccountDao.updateBankAccountMoneyOfUser(factor.getRoleName(), invoice.getMoney(), factor.getBankAccountId());
        this.notificationService.insertNotification(invoice, factor, supplier, new SupplierFactor(), "approveInvoice");
        return INVOICE_APPROVE;
      }
      else {
        this.invoiceDao.changeInvoiceStatus("DENIED", number);
        return INVOICE_DENY;
      }
    }
    else {
      throw new InvalidException(TRY_TO_CHANGE_INCORRECT_STATUS_INVOICE);
    }
  }

  @Override
  public void changePassword(String oldPassword, String newPassword, String repeatNewPassword)
  {
    User user = getCurrentUser();
    String pass = user.getPassword();
    boolean isMather = this.passwordEncoder.matches(oldPassword, pass);
    if (!isMather) {
      throw new InvalidException(PASSWORD_DONT_MATCH);
    }
    this.validation.passwordValidation(newPassword, repeatNewPassword);
    String encode = this.passwordEncoder.encode(newPassword);
    this.userDao.changePassword(user.getUserId(), encode);
  }

  @Override
  public List<UserResponse> showConnectedUsers(Pageable page) {
    User currentFactor = getCurrentUser();
    return this.userDao.showConnectedUsers(page, currentFactor.getUserId());
  }

}
