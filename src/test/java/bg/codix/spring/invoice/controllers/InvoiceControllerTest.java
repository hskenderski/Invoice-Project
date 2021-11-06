package bg.codix.spring.invoice.controllers;

import bg.codix.spring.invoice.dao.Address.AddressDao;
import bg.codix.spring.invoice.dao.BankAccount.BankAccountDao;
import bg.codix.spring.invoice.dao.Invoice.InvoiceDao;
import bg.codix.spring.invoice.dao.Notification.NotificationDao;
import bg.codix.spring.invoice.dao.User.UserDao;
import bg.codix.spring.invoice.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
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
public class InvoiceControllerTest extends AbstractTestNGSpringContextTests
{
  @Autowired
  public MockMvc mockMvc;

  @Autowired
  private InvoiceDao invoiceDao;

  @Autowired
  private BankAccountDao bankAccountDao;

  @Autowired
  private AddressDao addressDao;

  @Autowired
  private NotificationDao notificationDao;

  @Autowired
  private UserDao userDao;

  public Long TEST_USER1_ID, TEST_USER2_ID;
  public static final String TEST_USER1_FIRST_NAME = "Supplier", TEST_USER2_FIRST_NAME = "Factor";
  public static final String TEST_USER1_MIDDLE_NAME = "Supplier", TEST_USER2_MIDDLE_NAME = "Factor";
  public static final String TEST_USER1_LAST_NAME = "Supplier", TEST_USER2_LAST_NAME = "Factor";
  public static final Integer TEST_USER1_AGE = 21, TEST_USER2_AGE = 23;
  public static final String TEST_USER1_EMAIL = "supplier@abv.bg", TEST_USER2_EMAIL = "factor@abv.bg";
  public static final String TEST_USER1_USERNAME = "supplier", TEST_USER2_USERNAME = "factor";
  public static final String TEST_USER1_PASSWORD = "Pass!123", TEST_USER2_PASSWORD = "Pass!123";
  public static final BigDecimal TEST_USER1_SALARY = BigDecimal.valueOf(2000), TEST_USER2_SALARY = null;
  public static final BigDecimal TEST_USER1_FACTOR_CONTRACT_DEFAULT_SUM = null,
      TEST_USER2_FACTOR_CONTRACT_DEFAULT_SUM                            = BigDecimal.valueOf(20000);
  public static final User.RoleName TEST_USER1_ROLE = User.RoleName.SUPPLIER, TEST_USER2_ROLE = User.RoleName.FACTOR;
  public Long TEST_USER1_BANK_ACC_ID, TEST_USER2_BANK_ACC_ID;
  public Long TEST_USER1_ADDRESS_ID, TEST_USER2_ADDRESS_ID;

  public static final String  TEST_ADDRESS_COUNTY         = "Bulgaria";
  public static final String  TEST_ADDRESS_REGION         = "Sofia";
  public static final String  TEST_ADDRESS_CITY           = "Sofia";
  public static final String  TEST_ADDRESS_NEIGHBORHOOD   = "Student Town";
  public static final String  TEST_ADDRESS_STREET         = "Maria G.";
  public static final String  TEST_ADDRESS_ADDRESS_NUMBER = "19A";
  public static final Integer TEST_ADDRESS_FLOOR          = 6;

  public static final String TEST_BANK_ACC1_IBAN = "BG12345", TEST_BANK_ACC2_IBAN = "BG12346";
  public static final BigDecimal TEST_BANK_ACC1_MONEY_AMOUNT = BigDecimal.ZERO, TEST_BANK_ACC2_MONEY_AMOUNT = BigDecimal.valueOf(40000);

  public static final String     TEST_INVOICE1_FIRM_NAME = "Fantastic1";
  public static final BigDecimal TEST_INVOICE1_MONEY     = BigDecimal.valueOf(300);

  @BeforeClass
  public void setUp()
  {
    Address address = new Address(TEST_ADDRESS_COUNTY, TEST_ADDRESS_REGION, TEST_ADDRESS_CITY,
        TEST_ADDRESS_NEIGHBORHOOD, TEST_ADDRESS_STREET, TEST_ADDRESS_ADDRESS_NUMBER, TEST_ADDRESS_FLOOR);

    TEST_USER1_ADDRESS_ID = this.addressDao.addAddress(address);
    TEST_USER2_ADDRESS_ID = TEST_USER1_ADDRESS_ID;

    BankAccount bankAccount1 = new BankAccount(TEST_BANK_ACC1_IBAN, TEST_BANK_ACC1_MONEY_AMOUNT);
    BankAccount bankAccount2 = new BankAccount(TEST_BANK_ACC2_IBAN, TEST_BANK_ACC2_MONEY_AMOUNT);

    TEST_USER1_BANK_ACC_ID = this.bankAccountDao.addBankAccount(bankAccount1);
    TEST_USER2_BANK_ACC_ID = this.bankAccountDao.addBankAccount(bankAccount2);

    User supplier = new User(TEST_USER1_FIRST_NAME, TEST_USER1_MIDDLE_NAME, TEST_USER1_LAST_NAME,
        TEST_USER1_AGE, TEST_USER1_EMAIL, TEST_USER1_USERNAME, TEST_USER1_PASSWORD, TEST_USER1_SALARY,
        TEST_USER1_FACTOR_CONTRACT_DEFAULT_SUM, TEST_USER1_ROLE, TEST_USER1_BANK_ACC_ID, TEST_USER1_ADDRESS_ID);

    User factor = new User(TEST_USER2_FIRST_NAME, TEST_USER2_MIDDLE_NAME, TEST_USER2_LAST_NAME,
        TEST_USER2_AGE, TEST_USER2_EMAIL, TEST_USER2_USERNAME, TEST_USER2_PASSWORD, TEST_USER2_SALARY,
        TEST_USER2_FACTOR_CONTRACT_DEFAULT_SUM, TEST_USER2_ROLE, TEST_USER2_BANK_ACC_ID, TEST_USER2_ADDRESS_ID);

    TEST_USER1_ID = this.userDao.addUser(supplier);
    TEST_USER2_ID = this.userDao.addUser(factor);

    supplier.setUserId(TEST_USER1_ID);
    factor.setUserId(TEST_USER2_ID);

    this.userDao.insertContractBetweenSupplierAndFactor(TEST_USER1_ID, TEST_USER2_ID, BigDecimal.valueOf(20000),
        BigDecimal.valueOf(0), LocalDateTime.now());


  }

  @AfterClass
  public void tearDown()
  {
    removeFromTableNotificationsAndUserNotifications(TEST_USER1_ID);
    removeFromTableNotificationsAndUserNotifications(TEST_USER2_ID);

    removeFromTableInvoicesAndUserInvoices(TEST_USER1_ID);
    removeFromTableInvoicesAndUserInvoices(TEST_USER2_ID);

    this.userDao.removeContract(TEST_USER1_ID);

    this.userDao.removeUser(TEST_USER1_EMAIL);
    this.userDao.removeUser(TEST_USER2_EMAIL);

    this.addressDao.removeAddress(TEST_USER1_ADDRESS_ID);
    this.bankAccountDao.removeBankAccount(TEST_USER1_BANK_ACC_ID);
    this.bankAccountDao.removeBankAccount(TEST_USER2_BANK_ACC_ID);


  }

  private void removeFromTableInvoicesAndUserInvoices(Long testUserId)
  {
    List<Invoice> invoices = this.invoiceDao.allInvoicesOfUser(testUserId);

    for (Invoice i : invoices) {
      this.invoiceDao.removeUserInvoices(i.getInvoiceId());
      this.invoiceDao.removeInvoice(i.getInvoiceId());
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


  @WithUserDetails(TEST_USER1_EMAIL)
  @Test
  public void create_invoice_in_file_successful() throws Exception
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

  @WithUserDetails(TEST_USER1_EMAIL)
  @Test
  public void create_invoice_in_file_with_wrong_factor_username() throws Exception
  {
    MockMultipartFile file = new MockMultipartFile("file", "FINANCING.txt",
        MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());


    mockMvc.perform(multipart("/api/v1/create/file")
            .file(file)
            .param("factorUsername", TEST_USER1_USERNAME)
            .param("firmName", TEST_INVOICE1_FIRM_NAME)
            .param("money", String.valueOf(TEST_INVOICE1_MONEY)))
        .andExpect(status().isBadRequest());
  }

  @WithUserDetails(TEST_USER2_EMAIL)
  @Test
  public void create_invoice_in_file_with_wrong_role() throws Exception
  {
    MockMultipartFile file = new MockMultipartFile("file", "FINANCING.txt",
        MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());


    mockMvc.perform(multipart("/api/v1/create/file")
            .file(file)
            .param("factorUsername", TEST_USER2_USERNAME)
            .param("firmName", TEST_INVOICE1_FIRM_NAME)
            .param("money", String.valueOf(TEST_INVOICE1_MONEY)))
        .andExpect(status().isForbidden());
  }

  @WithUserDetails(TEST_USER1_EMAIL)
  @Test
  public void create_invoice_in_database_successful() throws Exception
  {
    mockMvc.perform(post("/api/v1/create/invoice")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"factorUsername\": \"factor\",\n" +
                "    \"firmName\": \"VMWARE\",\n" +
                "    \"money\": 20\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isOk());
  }

  @WithUserDetails(TEST_USER1_EMAIL)
  @Test
  public void create_invoice_in_database_with_wrong_factor_username() throws Exception
  {
    mockMvc.perform(post("/api/v1/create/invoice")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"factorUsername\": \"TEST_USER1_USERNAME\",\n" +
                "    \"firmName\": \"TEST_INVOICE1_FIRM_NAME\",\n" +
                "    \"money\": TEST_INVOICE1_MONEY\n" +
                "}"))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @WithUserDetails(TEST_USER2_EMAIL)
  @Test
  public void create_invoice_in_database_with_wrong_role() throws Exception
  {
    mockMvc.perform(post("/api/v1/create/invoice")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "    \"factorUsername\": \"factor\",\n" +
                "    \"firmName\": \"VMWARE\",\n" +
                "    \"money\": 20\n" +
                "}")
        )
        .andDo(print())
        .andExpect(status().isForbidden());
  }
}