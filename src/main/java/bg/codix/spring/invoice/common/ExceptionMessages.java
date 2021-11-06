package bg.codix.spring.invoice.common;

public class ExceptionMessages
{

  public static final String INVALID_ROLE = "Invalid role!";

  public static final String USER_NOT_FACTOR = "This user is not Factor!";

  public static final String ADMIN_CANT_BE_LOCK = "Admin can't be locked!";

  public static final String INVALID_INVOICE_NUMBER = "Invalid invoice number!";

  public static final String NOT_ENOUGH_MONEY = "Don't have enough money for that invoice!";

  public static final String TRY_TO_CHANGE_INCORRECT_STATUS_INVOICE = "You can change only WAITING status Invoices!";

  public static final String FAIL_INITIALIZE_FOLDER = "Could not initialize folder for upload!";

  public static final String FAIL_FILE_STORE = "Could not store the file. Error: ";

  public static final String READ_FILE_EX = "Could not read the file!";

  public static final String UPLOAD_FILE_EX = "Could not upload the file: %s!";

  public static final String NO_CONTRACT = "Between supplier and factor has no contract!";

  public static final String INVALID_BANK_ACCOUNT = "Invalid bankAccount!";

  public static final String INVALID_EMAIL = "You must enter valid email or your account is lock!";

  public static final String EMAIL_ALREADY_EXIST = "Email already exist!";

  public static final String INVALID_USERNAME = "You must enter valid username!";

  public static final String USERNAME_ALREADY_EXIST = "Username already exist!";

  public static final String PASSWORD_DONT_MATCH = "Password don't match!";

  public static final String PASSWORD_SHOULD_HAVE_SPECIAL_SYMBOL = "Password should contain special symbol!";

  public static final String PASSWORD_SHOULD_HAVE_DIGIT = "Password should contain digit!";

  public static final String PASSWORD_SHOULD_HAVE_UPPERCASE_LETTER = "Password should contain uppercase letter!";

  public static final String PASSWORD_SHOULD_HAVE_LOWERCASE_LETTER = "Password should contain lowercase letter!";

  public static final String PASSWORD_CANT_HAVE_WHITESPACE = "Password should not contain whitespaces!";

  public static final String PASSWORD_SHOULD_BE_MAX_7_SYMBOLS = "Password should be minimum 7 symbols!";

  public static final String NEGATIVE_MONEY = "Money cannot be negative number!";

  public static final String INVALID_MONEY_UPDATE_INPUT = "Cannot update money amount, because current money is bigger than it!";

  public static final String INVALID_IBAN = "You must enter valid IBAN!";

  public static final String IBAN_ALREADY_EXIST = "IBAN already exist!";

}
