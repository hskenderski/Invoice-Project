package bg.codix.spring.invoice.services;


import bg.codix.spring.invoice.dao.BankAccount.BankAccountDao;
import bg.codix.spring.invoice.dao.Invoice.InvoiceDao;
import bg.codix.spring.invoice.dao.User.UserDao;
import bg.codix.spring.invoice.entities.*;
import bg.codix.spring.invoice.services.Notification.NotificationService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;

public class InvoiceServiceImplTest
{
  private InvoiceDao invoicesDao;

  private UserDao userDao;

  private NotificationService notificationService;

  private BankAccountDao bankAccountDao;

  @Mock
  private Authentication authentication;

  @Mock
  private SecurityContext securityContext;

  private InitialContext initContext;

  private User user;

  @BeforeClass
  public void init() throws Exception
  {
    SimpleNamingContextBuilder.emptyActivatedContextBuilder();
    this.initContext = new InitialContext();
  }

  @BeforeMethod
  public void setUp()
  {
    userDao = mock(UserDao.class);
    bankAccountDao = mock((BankAccountDao.class));
    invoicesDao = mock(InvoiceDao.class);
    authentication = Mockito.mock(Authentication.class);
    securityContext = Mockito.mock(SecurityContext.class);
    notificationService = mock(NotificationService.class);

    user = new User(1L, "Iveta", "Vasileva", "Veselinova", 21,
        "iveta_vasileva@abv.bg", "iveta", "123", null,
        BigDecimal.valueOf(10000), User.RoleName.FACTOR, 1L, 1L);

    userDao.addUser(user);

    MockitoAnnotations.openMocks(this);

  }

  @Test
  public void testWhenFindUsername_thenReturnUsername()
  {
    Mockito.when(userDao
            .findUserByUsername(user.getUsername()))
        .thenReturn(user);

    User userResult = new User(1L, "Iveta", "Vasileva", "Veselinova", 21,
        "iveta_vasileva@abv.bg", "iveta", "123", null,
        BigDecimal.valueOf(10000), User.RoleName.FACTOR, 1L, 1L);

    assertEquals(userResult.getUsername(), user.getUsername());
  }

  @Test
  public void whenMockJndiDataSource_thenReturnJndiDataSource() throws Exception
  {
    this.initContext.bind("jdbc:oracle:thin:@//localhost:1521/orcl",
        new DriverManagerDataSource("jdbc:oracle:thin:@//localhost:1521/orcl", "system", "12345678"));
    DataSource ds = (DataSource) this.initContext.lookup("jdbc:oracle:thin:@//localhost:1521/orcl");

    assertNotNull(ds.getConnection());
  }

  @Test
  public void whenInset_thenSaveAndReturnInvoice()
  {
    Invoice invoice = new Invoice();
    invoice.setFactorUsername("Petar");
    invoice.setFileName("Petar-cv");
    invoice.setFirmName("WorldCompany");
    invoice.setMoney(BigDecimal.valueOf(400));

    Mockito.when(userDao.findUserByUsername(anyString())).thenReturn(user);

    Mockito.when(userDao.getContractInformation(anyLong(), anyLong()))
        .thenReturn(new SupplierFactor());

    when(invoicesDao.createInvoiceInDatabase(any(invoice.getClass())))
        .thenReturn(invoice.getInvoiceId());

  }

  @Test
  public void whenGetCurrentUser_thenReturnAuthenticatedUser(){

    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

    SecurityContextHolder.setContext(securityContext);

    when(authentication.getName())
        .thenReturn(user.getUsername());
  }

  @Test
  public void whenCreateInvoiceInDatabase_thenSaveInvoice(){
    Invoice invoice = new Invoice();
    invoice.setFactorUsername("Gosho");
    invoice.setFileName("Gosho-cv");
    invoice.setFirmName("WorldCompany");
    invoice.setMoney(BigDecimal.valueOf(690));

    Mockito.when(userDao.findUserByUsername(anyString())).thenReturn(user);

    Mockito.when(userDao.getContractInformation(anyLong(), anyLong()))
        .thenReturn(new SupplierFactor());

    when(invoicesDao.createInvoiceInDatabase(any(invoice.getClass())))
        .thenReturn(invoice.getInvoiceId());

  }

  @Test
  public void whenInsertNotification_thenSaveNotification(){

    Notification notification = new Notification();
    notification.setDate(LocalDateTime.now());

    Mockito.when(notificationService.addNotification(any(Notification.class)))
        .thenReturn(notification.getNotificationId());
  }

  @Test
  public void whenApproveInvoiceInDatabase_thenSuccessfullyApproveInvoice(){
    Invoice invoice = new Invoice();
    BankAccount bankAccount = new BankAccount();

    Mockito.when(invoicesDao.findInvoiceById(anyLong()))
        .thenReturn(invoice);

    Mockito.when(bankAccountDao.findById(anyLong()))
        .thenReturn(bankAccount);
  }
}