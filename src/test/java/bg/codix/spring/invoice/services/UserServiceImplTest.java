package bg.codix.spring.invoice.services;

import bg.codix.spring.invoice.dao.User.UserDao;
import bg.codix.spring.invoice.entities.Address;
import bg.codix.spring.invoice.entities.BankAccount;
import bg.codix.spring.invoice.entities.User;
import bg.codix.spring.invoice.services.Address.AddressService;
import bg.codix.spring.invoice.services.BankAccount.BankAccountService;
import bg.codix.spring.invoice.services.Invoice.InvoiceService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserServiceImplTest
{
  private UserDao            userDao;
  private BankAccountService bankAccountService;
  private PasswordEncoder passwordEncoder;
  private AddressService addressService;
  private InvoiceService invoiceService;

  @Mock
  private Authentication authentication;

  @Mock
  private SecurityContext securityContext;

  private User user;

  @BeforeMethod
  public void setUp()
  {
    userDao = mock(UserDao.class);
    bankAccountService = mock((BankAccountService.class));
    passwordEncoder = mock(PasswordEncoder.class);
    invoiceService = mock(InvoiceService.class);
    authentication = Mockito.mock(Authentication.class);
    securityContext = Mockito.mock(SecurityContext.class);
    addressService = mock(AddressService.class);

    user = new User(1L, "Iveta", "Vasileva", "Veselinova", 21,
        "iveta_vasileva@abv.bg", "iveta", "123", null,
        BigDecimal.valueOf(10000), User.RoleName.FACTOR, 1L, 1L);

    userDao.addUser(user);

    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testWhenCreateUser_ThenSaveUserInDatabase()
  {
    BankAccount bankAccount = new BankAccount("BA123456", BigDecimal.valueOf(500));
    bankAccount.setAccId(1);
    Address address = new Address("Bulgaria", "Sofia", "Sofia", "Student town",
        "Mariika Gavrilova", "19", 6);
    address.setAddressId(1L);
    User user = new User(1L, "Iveta", "Veselinova", "Vasileva", 21, "iveta_vasileva@abv.bg", "iveta", "Pass!123",
        null, null, User.RoleName.ADMIN, Long.valueOf(bankAccount.getAccId()), address.getAddressId());

    Mockito.when(bankAccountService.createBankAccount(any(BankAccount.class)))
        .thenReturn(Long.valueOf(bankAccount.getAccId()));

    Mockito.when(userDao.addUser(any(User.class)))
        .thenReturn(user.getUserId());
  }

  @Test
  public void testWhenFindFileNamesOfFactorByEmail_ThenReturnsListWithInvoices()
  {
    Mockito.when(userDao.findFileNamesOfFactorByEmail(any(), anyString()))
        .thenReturn(new ArrayList<>());
  }

  @Test
  public void whenGetCurrentUser_thenReturnAuthenticatedUser()
  {

    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

    SecurityContextHolder.setContext(securityContext);

    when(authentication.getName())
        .thenReturn(user.getUsername());
  }

}