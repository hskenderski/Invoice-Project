package bg.codix.spring.invoice.controllers;

import bg.codix.spring.invoice.dao.Address.AddressDao;
import bg.codix.spring.invoice.dao.BankAccount.BankAccountDao;
import bg.codix.spring.invoice.dao.Invoice.InvoiceDao;
import bg.codix.spring.invoice.dao.Notification.NotificationDao;
import bg.codix.spring.invoice.dao.User.UserDao;
import bg.codix.spring.invoice.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners({WithSecurityContextTestExecutionListener.class})
@Transactional
public class UserControllerTest extends AbstractTestNGSpringContextTests
{
  @Autowired
  public MockMvc mockMvc;

  @Autowired
  private UserDao userDao;

  @Autowired
  private BankAccountDao bankAccountDao;

  @Autowired
  private AddressDao addressDao;

  @Autowired
  private NotificationDao notificationDao;

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Autowired
  private InvoiceDao invoiceDao;
  public  String     TEST_USER4_PASSWORD;
  public  Long       TEST_USER1_ID, TEST_USER2_ID, TEST_USER3_ID, TEST_USER4_ID;
  public static final String TEST_USER1_FIRST_NAME = "Ivan", TEST_USER2_FIRST_NAME = "Kiril", TEST_USER3_FIRST_NAME = "Admin1", TEST_USER4_FIRST_NAME = "test";
  public static final String TEST_USER1_MIDDLE_NAME = "Ivanov", TEST_USER2_MIDDLE_NAME = "Ivanov", TEST_USER3_MIDDLE_NAME = "Admin1", TEST_USER4_MIDDLE_NAME = "test";
  public static final String TEST_USER1_LAST_NAME = "Ivanov", TEST_USER2_LAST_NAME = "Ivanov", TEST_USER3_LAST_NAME = "Admin1", TEST_USER4_LAST_NAME = "test";
  public static final Integer TEST_USER1_AGE = 21, TEST_USER2_AGE = 23, TEST_USER3_AGE = 21, TEST_USER4_AGE = 21;
  public static final String TEST_USER1_EMAIL = "i.ivanov@abv.bg", TEST_USER2_EMAIL = "k.ivanov@abv.bg", TEST_USER3_EMAIL = "admin1@abv.bg", TEST_USER4_EMAIL = "test@abv.bg";
  public static final String TEST_USER1_USERNAME = "Ivan", TEST_USER2_USERNAME = "Kiril", TEST_USER3_USERNAME = "admin1", TEST_USER4_USERNAME = "test";
  public static final String TEST_USER1_PASSWORD = "Hrisi1ha!", TEST_USER2_PASSWORD = "Hrisi1ha!", TEST_USER3_PASSWORD = "Hrisi1ha!";
  public static final BigDecimal TEST_USER1_SALARY = BigDecimal.valueOf(2000), TEST_USER2_SALARY = null, TEST_USER3_SALARY = null, TEST_USER4_SALARY = null;
  public static final BigDecimal TEST_USER1_FACTOR_CONTRACT_DEFAULT_SUM = null, TEST_USER2_FACTOR_CONTRACT_DEFAULT_SUM = BigDecimal.valueOf(20000), TEST_USER3_FACTOR_CONTRACT_DEFAULT_SUM = null, TEST_USER4_FACTOR_CONTRACT_DEFAULT_SUM = null;
  public static final User.RoleName TEST_USER1_ROLE = User.RoleName.SUPPLIER, TEST_USER2_ROLE = User.RoleName.FACTOR, TEST_USER3_ROLE = User.RoleName.ADMIN, TEST_USER4_ROLE = User.RoleName.FACTOR;
  public Long TEST_USER1_BANK_ACC_ID, TEST_USER2_BANK_ACC_ID, TEST_USER3_BANK_ACC_ID, TEST_USER4_BANK_ACC_ID;
  public Long TEST_USER1_ADDRESS_ID, TEST_USER2_ADDRESS_ID, TEST_USER3_ADDRESS_ID, TEST_USER4_ADDRESS_ID;

  public static final String  TEST_ADDRESS_COUNTY         = "Bulgaria";
  public static final String  TEST_ADDRESS_REGION         = "Sofia";
  public static final String  TEST_ADDRESS_CITY           = "Sofia";
  public static final String  TEST_ADDRESS_NEIGHBORHOOD   = "Student City";
  public static final String  TEST_ADDRESS_STREET         = "Ivan Rilski";
  public static final String  TEST_ADDRESS_ADDRESS_NUMBER = "2A";
  public static final Integer TEST_ADDRESS_FLOOR          = 5;
  public static final String TEST_BANK_ACC1_IBAN = "BG123456", TEST_BANK_ACC2_IBAN = "BG123457", TEST_BANK_ACC3_IBAN = "BG123458", TEST_BANK_ACC4_IBAN = "BG123459";
  public static final BigDecimal TEST_BANK_ACC1_MONEY_AMOUNT = BigDecimal.ZERO, TEST_BANK_ACC2_MONEY_AMOUNT = BigDecimal.valueOf(40000), TEST_BANK_ACC3_MONEY_AMOUNT = BigDecimal.ZERO, TEST_BANK_ACC4_MONEY_AMOUNT = BigDecimal.ZERO;


  @BeforeClass
  public void setUp()
  {
    TEST_USER4_PASSWORD = passwordEncoder.encode("Hrisi1ha!");

    Address address = new Address(TEST_ADDRESS_COUNTY, TEST_ADDRESS_REGION, TEST_ADDRESS_CITY, TEST_ADDRESS_NEIGHBORHOOD, TEST_ADDRESS_STREET, TEST_ADDRESS_ADDRESS_NUMBER, TEST_ADDRESS_FLOOR);
    TEST_USER1_ADDRESS_ID = this.addressDao.addAddress(address);
    TEST_USER2_ADDRESS_ID = TEST_USER1_ADDRESS_ID;
    TEST_USER3_ADDRESS_ID = TEST_USER1_ADDRESS_ID;
    TEST_USER4_ADDRESS_ID = TEST_USER1_ADDRESS_ID;

    BankAccount bankAccount1 = new BankAccount(TEST_BANK_ACC1_IBAN, TEST_BANK_ACC1_MONEY_AMOUNT);
    BankAccount bankAccount2 = new BankAccount(TEST_BANK_ACC2_IBAN, TEST_BANK_ACC2_MONEY_AMOUNT);
    BankAccount bankAccount3 = new BankAccount(TEST_BANK_ACC3_IBAN, TEST_BANK_ACC3_MONEY_AMOUNT);
    BankAccount bankAccount4 = new BankAccount(TEST_BANK_ACC4_IBAN, TEST_BANK_ACC4_MONEY_AMOUNT);
    TEST_USER1_BANK_ACC_ID = this.bankAccountDao.addBankAccount(bankAccount1);
    TEST_USER2_BANK_ACC_ID = this.bankAccountDao.addBankAccount(bankAccount2);
    TEST_USER3_BANK_ACC_ID = this.bankAccountDao.addBankAccount(bankAccount3);
    TEST_USER4_BANK_ACC_ID = this.bankAccountDao.addBankAccount(bankAccount4);

    User user1 = new User(TEST_USER1_FIRST_NAME, TEST_USER1_MIDDLE_NAME, TEST_USER1_LAST_NAME, TEST_USER1_AGE, TEST_USER1_EMAIL, TEST_USER1_USERNAME, TEST_USER1_PASSWORD, TEST_USER1_SALARY, TEST_USER1_FACTOR_CONTRACT_DEFAULT_SUM, TEST_USER1_ROLE, TEST_USER1_BANK_ACC_ID, TEST_USER1_ADDRESS_ID);
    User user2 = new User(TEST_USER2_FIRST_NAME, TEST_USER2_MIDDLE_NAME, TEST_USER2_LAST_NAME, TEST_USER2_AGE, TEST_USER2_EMAIL, TEST_USER2_USERNAME, TEST_USER2_PASSWORD, TEST_USER2_SALARY, TEST_USER2_FACTOR_CONTRACT_DEFAULT_SUM, TEST_USER2_ROLE, TEST_USER2_BANK_ACC_ID, TEST_USER2_ADDRESS_ID);
    User user3 = new User(TEST_USER3_FIRST_NAME, TEST_USER3_MIDDLE_NAME, TEST_USER3_LAST_NAME, TEST_USER3_AGE, TEST_USER3_EMAIL, TEST_USER3_USERNAME, TEST_USER3_PASSWORD, TEST_USER3_SALARY, TEST_USER3_FACTOR_CONTRACT_DEFAULT_SUM, TEST_USER3_ROLE, TEST_USER3_BANK_ACC_ID, TEST_USER3_ADDRESS_ID);
    User user4 = new User(TEST_USER4_FIRST_NAME, TEST_USER4_MIDDLE_NAME, TEST_USER4_LAST_NAME, TEST_USER4_AGE, TEST_USER4_EMAIL, TEST_USER4_USERNAME, TEST_USER4_PASSWORD, TEST_USER4_SALARY, TEST_USER4_FACTOR_CONTRACT_DEFAULT_SUM, TEST_USER4_ROLE, TEST_USER4_BANK_ACC_ID, TEST_USER4_ADDRESS_ID);

    TEST_USER1_ID = this.userDao.addUser(user1);
    TEST_USER2_ID = this.userDao.addUser(user2);
    TEST_USER3_ID = this.userDao.addUser(user3);
    TEST_USER4_ID = this.userDao.addUser(user4);


    user1.setUserId(TEST_USER1_ID);
    user2.setUserId(TEST_USER2_ID);
    user3.setUserId(TEST_USER3_ID);
    user4.setUserId(TEST_USER4_ID);

    this.userDao.insertContractBetweenSupplierAndFactor(TEST_USER1_ID, TEST_USER2_ID, BigDecimal.valueOf(20000),
        BigDecimal.valueOf(0), LocalDateTime.now());

  }


  @AfterClass
  public void tearDown()
  {
    removeFromTableNotificationsAndUserNotifications(TEST_USER1_ID);
    removeFromTableNotificationsAndUserNotifications(TEST_USER2_ID);
    removeFromTableNotificationsAndUserNotifications(TEST_USER3_ID);
    removeFromTableNotificationsAndUserNotifications(TEST_USER4_ID);

    removeFromTableInvoicesAndUserInvoices(TEST_USER1_ID);
    removeFromTableInvoicesAndUserInvoices(TEST_USER2_ID);
    removeFromTableInvoicesAndUserInvoices(TEST_USER3_ID);
    removeFromTableInvoicesAndUserInvoices(TEST_USER4_ID);


    this.userDao.removeContract(TEST_USER1_ID);

    this.userDao.removeContract(TEST_USER1_ID);
    this.userDao.removeUser(TEST_USER1_EMAIL);
    this.userDao.removeUser(TEST_USER2_EMAIL);
    this.userDao.removeUser(TEST_USER3_EMAIL);
    this.userDao.removeUser(TEST_USER4_EMAIL);
    this.addressDao.removeAddress(TEST_USER1_ADDRESS_ID);
    this.bankAccountDao.removeBankAccount(TEST_USER1_BANK_ACC_ID);
    this.bankAccountDao.removeBankAccount(TEST_USER2_BANK_ACC_ID);
    this.bankAccountDao.removeBankAccount(TEST_USER3_BANK_ACC_ID);
    this.bankAccountDao.removeBankAccount(TEST_USER4_BANK_ACC_ID);

    if (this.userDao.findUserByEmail("hristifyan@abv.bg") != null) {
      User user = this.userDao.findUserByEmail("hristifyan@abv.bg");
      this.userDao.removeUser(user.getEmail());
      this.addressDao.removeAddress(user.getAddressId());
      this.bankAccountDao.removeBankAccount(user.getBankAccountId());
    }
  }

  private void removeFromTableNotificationsAndUserNotifications(Long testUserId)
  {
    List<Notification> notifications = this.notificationDao.allNotificationsOfUser(testUserId);
    for (Notification n : notifications) {
      this.notificationDao.removeUserNotifications(testUserId);
      this.notificationDao.removeNotification(n.getNotificationId());
    }
  }

  private void removeFromTableInvoicesAndUserInvoices(Long testUserId)
  {
    List<Invoice> invoices = this.invoiceDao.allInvoicesOfUser(testUserId);

    for (Invoice i : invoices) {
      this.invoiceDao.removeUserInvoices(i.getInvoiceId());
      this.invoiceDao.removeInvoice(i.getInvoiceId());
    }
  }

  @Test
  @WithUserDetails(TEST_USER1_EMAIL)
  public void create_contract_with_correct_user() throws Exception
  {
    mockMvc.perform(multipart("/api/v1/create/contract/{username}", TEST_USER4_USERNAME))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails(TEST_USER1_EMAIL)
  public void create_contract_with_correct_user_but_try_to_connect_another_supplier() throws Exception
  {
    mockMvc.perform(multipart("/api/v1/create/contract/{username}", TEST_USER1_USERNAME))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails(TEST_USER2_EMAIL)
  public void create_contract_with_wrong_user() throws Exception
  {
    mockMvc.perform(multipart("/api/v1/create/contract/{username}", TEST_USER2_USERNAME))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  @WithUserDetails(TEST_USER1_EMAIL)
  public void show_notification() throws Exception
  {
    mockMvc.perform(get("/api/v1/show/notifications?pageNumber=0&pageCapacity=100"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void register_user_with_correct_information() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 21,\n" +
                "     \"email\": \"hristifyan@abv.bg\",\n" +
                "     \"username\": \"Hristiyfan\",\n" +
                "     \"password\": \"Hrisi1ha!\",\n" +
                "     \"passwordConfirmation\": \"Hrisi1ha!\",\n" +
                "     \"salary\": 100,\n" +
                "     \"factorContractDefaultSum\": null,\n" +
                "     \"roleName\": \"SUPPLIER\",\n" +
                "     \"iban\": \"KO943563\",\n" +
                "     \"moneyAmount\": 200,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void register_user_with_wrong_email() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gegeabv.bg\",\n" +
                "     \"username\": \"Hristiyfn\",\n" +
                "     \"password\": \"Hrisi1ha!\",\n" +
                "     \"passwordConfirmation\": \"Hrisi1ha!\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": null,\n" +
                "     \"roleName\": \"SUPPLIER\",\n" +
                "     \"iban\": \"BG48987038\",\n" +
                "     \"moneyAmount\": null,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_wrong_username() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gegeop@abv.bg\",\n" +
                "     \"username\": \"#@Kefe\",\n" +
                "     \"password\": \"Hrisi1ha!\",\n" +
                "     \"passwordConfirmation\": \"Hrisi1ha!\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": null,\n" +
                "     \"roleName\": \"SUPPLIER\",\n" +
                "     \"iban\": \"BG428863938\",\n" +
                "     \"moneyAmount\": null,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_different_password() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gegeh@abv.bg\",\n" +
                "     \"username\": \"Julhfk\",\n" +
                "     \"password\": \"Hrisi1ha\",\n" +
                "     \"passwordConfirmation\": \"Hrisi1ha!\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": null,\n" +
                "     \"roleName\": \"SUPPLIER\",\n" +
                "     \"iban\": \"BG42563038\",\n" +
                "     \"moneyAmount\": null,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_wrong_password_MustHave_UpperCase() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gegeh@abv.bg\",\n" +
                "     \"username\": \"Julhfk\",\n" +
                "     \"password\": \"hrisi1ha!\",\n" +
                "     \"passwordConfirmation\": \"hrisi1ha!\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": null,\n" +
                "     \"roleName\": \"SUPPLIER\",\n" +
                "     \"iban\": \"BG42506338\",\n" +
                "     \"moneyAmount\": null,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }


  @Test
  public void register_user_with_wrong_password_MustHave_SpecialSymbol() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gegeh@abv.bg\",\n" +
                "     \"username\": \"Julhfk\",\n" +
                "     \"password\": \"Hrisi1ha\",\n" +
                "     \"passwordConfirmation\": \"Hrisi1ha\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": null,\n" +
                "     \"roleName\": \"SUPPLIER\",\n" +
                "     \"iban\": \"BG42503538\",\n" +
                "     \"moneyAmount\": null,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_wrong_password_MustHave_LowCase() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gegeg@abv.bg\",\n" +
                "     \"username\": \"Julkhf\",\n" +
                "     \"password\": \"HRISI1HA!\",\n" +
                "     \"passwordConfirmation\": \"HRISI1HA!\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": null,\n" +
                "     \"roleName\": \"SUPPLIER\",\n" +
                "     \"iban\": \"BG42503638\",\n" +
                "     \"moneyAmount\": null,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_wrong_password_MustHave_Digit() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gegeg@abv.bg\",\n" +
                "     \"username\": \"Julhfk\",\n" +
                "     \"password\": \"Hrisiha\",\n" +
                "     \"passwordConfirmation\": \"Hrisiha\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": null,\n" +
                "     \"roleName\": \"SUPPLIER\",\n" +
                "     \"iban\": \"BG42503538\",\n" +
                "     \"moneyAmount\": null,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_wrong_password_MustHave_Minimum_7_Symbols() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gegeg@abv.bg\",\n" +
                "     \"username\": \"Julkh\",\n" +
                "     \"password\": \"Hris1!\",\n" +
                "     \"passwordConfirmation\": \"Hris1!\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": null,\n" +
                "     \"roleName\": \"SUPPLIER\",\n" +
                "     \"iban\": \"BG42503538\",\n" +
                "     \"moneyAmount\": null,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_wrong_password_CantHave_WhiteSpace() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gegeh@abv.bg\",\n" +
                "     \"username\": \"Julhk\",\n" +
                "     \"password\": \"H ris1!\",\n" +
                "     \"passwordConfirmation\": \"H ris1!\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": null,\n" +
                "     \"roleName\": \"SUPPLIER\",\n" +
                "     \"iban\": \"BG42505338\",\n" +
                "     \"moneyAmount\": null,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_negative_age() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": -10,\n" +
                "     \"email\": \"gege@abv.bg\",\n" +
                "     \"username\": \"Julk\",\n" +
                "     \"password\": \"Hrisi1ha!\",\n" +
                "     \"passwordConfirmation\": \"Hrisi1ha!\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": null,\n" +
                "     \"roleName\": \"SUPPLIER\",\n" +
                "     \"iban\": \"BG42345038\",\n" +
                "     \"moneyAmount\": null,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_negative_salary() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gege@abv.bg\",\n" +
                "     \"username\": \"Julk\",\n" +
                "     \"password\": \"Hrisi1ha!\",\n" +
                "     \"passwordConfirmation\": \"Hrisi1ha!\",\n" +
                "     \"salary\": -100,\n" +
                "     \"factorContractDefaultSum\": null,\n" +
                "     \"roleName\": \"SUPPLIER\",\n" +
                "     \"iban\": \"BG42350638\",\n" +
                "     \"moneyAmount\": null,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_negative_factorContractDefaultSum() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gege@abv.bg\",\n" +
                "     \"username\": \"Julk\",\n" +
                "     \"password\": \"Hrisi1ha!\",\n" +
                "     \"passwordConfirmation\": \"Hrisi1ha!\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": -100,\n" +
                "     \"roleName\": \"SUPPLIER\",\n" +
                "     \"iban\": \"BG42553038\",\n" +
                "     \"moneyAmount\": null,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_wrong_role() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gege@abv.bg\",\n" +
                "     \"username\": \"Julk\",\n" +
                "     \"password\": \"Hrisi1ha!\",\n" +
                "     \"passwordConfirmation\": \"Hrisi1ha!\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": 100,\n" +
                "     \"roleName\": \"GOD\",\n" +
                "     \"iban\": \"BG425435038\",\n" +
                "     \"moneyAmount\": null,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_wrong_iban() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gege@abv.bg\",\n" +
                "     \"username\": \"Julk\",\n" +
                "     \"password\": \"Hrisi1ha!\",\n" +
                "     \"passwordConfirmation\": \"Hrisi1ha!\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": 100,\n" +
                "     \"roleName\": \"FACTOR\",\n" +
                "     \"iban\": \"wrong\",\n" +
                "     \"moneyAmount\": null,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_negative_moneyAmount() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gege@abv.bg\",\n" +
                "     \"username\": \"Julk\",\n" +
                "     \"password\": \"Hrisi1ha!\",\n" +
                "     \"passwordConfirmation\": \"Hrisi1ha!\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": -100,\n" +
                "     \"roleName\": \"FACTOR\",\n" +
                "     \"iban\": \"BG3246252\",\n" +
                "     \"moneyAmount\": 100,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 3\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_negative_floor() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gege@abv.bg\",\n" +
                "     \"username\": \"Julk\",\n" +
                "     \"password\": \"Hrisi1ha!\",\n" +
                "     \"passwordConfirmation\": \"Hrisi1ha!\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": -100,\n" +
                "     \"roleName\": \"FACTOR\",\n" +
                "     \"iban\": \"BG3246252\",\n" +
                "     \"moneyAmount\": 100,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": -100\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_existing_username() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"gegeop@abv.bg\",\n" +
                "     \"username\": \"Ivan\",\n" +
                "     \"password\": \"Hrisi1ha!\",\n" +
                "     \"passwordConfirmation\": \"Hrisi1ha!\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": 100,\n" +
                "     \"roleName\": \"FACTOR\",\n" +
                "     \"iban\": \"BG32464252\",\n" +
                "     \"moneyAmount\": 100,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 100\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void register_user_with_existing_email() throws Exception
  {
    mockMvc.perform(post("/api/v1/register/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "     \"firstName\": \"Hristiyan\",\n" +
                "     \"middleName\": \"Ivanov\",\n" +
                "     \"lastName\":  \"Skenderski\",\n" +
                "     \"age\": 10,\n" +
                "     \"email\": \"i.ivanov@abv.bg\",\n" +
                "     \"username\": \"Ivaffsn\",\n" +
                "     \"password\": \"Hrisi1ha!\",\n" +
                "     \"passwordConfirmation\": \"Hrisi1ha!\",\n" +
                "     \"salary\": null,\n" +
                "     \"factorContractDefaultSum\": 100,\n" +
                "     \"roleName\": \"FACTOR\",\n" +
                "     \"iban\": \"BG329584252\",\n" +
                "     \"moneyAmount\": 100,\n" +
                "     \"country\": \"Bulgaria\",\n" +
                "     \"region\": \"Sofia\",\n" +
                "     \"city\": \"Sofia\",\n" +
                "     \"neighborhood\": \"Student City\",\n" +
                "     \"street\": \"Ivan Rilski\",\n" +
                "     \"addressNumber\": 5,\n" +
                "     \"floor\": 100\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails(TEST_USER3_EMAIL)
  public void show_all_users_successful() throws Exception
  {
    mockMvc.perform(get("/api/v1/show/all/users"))
        .andDo(print())
        .andExpect(status().isOk());
  }


  @Test
  @WithUserDetails(TEST_USER2_EMAIL)
  public void show_all_users_with_wrong_role() throws Exception
  {
    mockMvc.perform(get("/api/v1/show/all/users"))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  @WithUserDetails(TEST_USER1_EMAIL)
  public void show_All_Users_with_wrong_role() throws Exception
  {
    mockMvc.perform(get("/api/v1/show/all/users"))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  @WithUserDetails(TEST_USER1_EMAIL)
  public void show_all_factors_correct() throws Exception
  {
    mockMvc.perform(get("/api/v1/user/role/name"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails(TEST_USER2_EMAIL)
  public void show_all_factor_with_wrong_role() throws Exception
  {
    mockMvc.perform(get("/api/v1/user/role/name"))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  @WithUserDetails(TEST_USER2_EMAIL)
  public void update_contract_max_money_successful() throws Exception
  {
    mockMvc.perform(put("/api/v1/update/money/amount/?username=" + TEST_USER1_USERNAME + "&newMoney=8000"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails(TEST_USER1_EMAIL)
  public void update_contract_max_money_incorrect_role() throws Exception
  {
    mockMvc.perform(put("/api/v1/update/money/amount/?username=" + TEST_USER1_USERNAME + "&newMoney=8000"))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  @WithUserDetails(TEST_USER2_EMAIL)
  public void update_contract_max_money_incorrect_money_amount() throws Exception
  {
    mockMvc.perform(put("/api/v1/update/money/amount/?username=" + TEST_USER1_USERNAME + "&newMoney=-100"))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails(TEST_USER2_EMAIL)
  public void update_contract_max_money_dont_have_contract() throws Exception
  {
    mockMvc.perform(put("/api/v1/update/money/amount/?username=" + TEST_USER4_USERNAME + "&newMoney=8000"))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails(TEST_USER2_EMAIL)
  public void update_contract_max_money_incorrect_username() throws Exception
  {
    mockMvc.perform(put("/api/v1/update/money/amount/?username=fake&newMoney=8000"))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails(TEST_USER3_EMAIL)
  public void update_contract_max_money_with_wrong_role() throws Exception
  {
    mockMvc.perform(put("/api/v1/update/money/amount/?username=" + TEST_USER1_USERNAME + "&newMoney=8000"))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test(priority = 1)
  @WithUserDetails(TEST_USER3_EMAIL)
  public void lock_account_successful_true() throws Exception
  {
    mockMvc.perform(put("/api/v1/lock/account?email=" + TEST_USER4_EMAIL + "&isAccountLocked=true"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails(TEST_USER3_EMAIL)
  public void lock_account_lock_admin() throws Exception
  {
    mockMvc.perform(put("/api/v1/lock/account?email=" + TEST_USER3_EMAIL + "&isAccountLocked=true"))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test(priority = 2)
  @WithUserDetails(TEST_USER3_EMAIL)
  public void lock_account_successful_false() throws Exception
  {
    mockMvc.perform(put("/api/v1/lock/account?email=" + TEST_USER4_EMAIL + "&isAccountLocked=false"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails(TEST_USER1_EMAIL)
  public void lock_account_with_wrong_role() throws Exception
  {
    mockMvc.perform(put("/api/v1/lock/account?email=" + TEST_USER4_EMAIL + "&isAccountLocked=true"))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @WithUserDetails(TEST_USER1_EMAIL)
  @Test(priority = 3)
  public void createInvoiceInFile_ReturnStatusOk() throws Exception
  {
    MockMultipartFile file = new MockMultipartFile("file", "FINANCING.txt",
        MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());


    mockMvc.perform(multipart("/api/v1/create/file")
            .file(file)
            .param("factorUsername", TEST_USER2_USERNAME)
            .param("firmName", "ala")
            .param("money", String.valueOf(10)))
        .andDo(print())
        .andExpect(status().isOk());
  }


  @Test(priority = 4)
  @WithUserDetails(TEST_USER2_EMAIL)
  public void get_files_successful() throws Exception
  {
    mockMvc.perform(get("/api/v1/files?pageNumber=0&pageCapacity=100"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails(TEST_USER1_EMAIL)
  public void get_files_forbidden() throws Exception
  {
    mockMvc.perform(get("/api/v1/files?pageNumber=0&pageCapacity=100"))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  @WithUserDetails(TEST_USER4_EMAIL)
  public void change_password_successful() throws Exception
  {
    mockMvc.perform(put("/api/v1/change/password/old/password/{oldPassword}/new/password/{newPassword}/repeat/{repeatNewPassword}", TEST_USER3_PASSWORD, "Hrisi1ha!!", "Hrisi1ha!!"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithUserDetails(TEST_USER4_EMAIL)
  public void change_password_password_dont_match() throws Exception
  {
    mockMvc.perform(put("/api/v1/change/password/old/password/{oldPassword}/new/password/{newPassword}/repeat/{repeatNewPassword}", "Fake1!ha", "Hrisi1ha!!", "Hrisi1ha!!"))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails(TEST_USER2_EMAIL)
  public void download_file_dont_exist() throws Exception
  {
    mockMvc.perform(get("/api/v1//files/{filename}", 32))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails(TEST_USER2_EMAIL)
  public void approve_invoice_dont_exist() throws Exception
  {
    mockMvc.perform(put("/api/v1/approve/invoice?approveOrDenyStatus=true&number=100"))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails(TEST_USER2_EMAIL)
  public void show_suppliers_of_logged_factor() throws Exception{
    mockMvc.perform(get("/api/v1/show/connected"))
        .andDo(print())
        .andExpect(status().isOk());
  }

}