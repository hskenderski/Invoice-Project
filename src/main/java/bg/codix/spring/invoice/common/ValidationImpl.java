package bg.codix.spring.invoice.common;

import bg.codix.spring.invoice.dao.BankAccount.BankAccountDao;
import bg.codix.spring.invoice.dao.User.UserDao;
import bg.codix.spring.invoice.entities.User;
import bg.codix.spring.invoice.exceptions.InvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static bg.codix.spring.invoice.common.ExceptionMessages.*;

@Component
public class ValidationImpl implements Validation
{
  private final UserDao        userDao;
  private final BankAccountDao bankAccountDao;

  @Autowired
  public ValidationImpl(UserDao userDao, BankAccountDao bankAccountDao)
  {
    this.userDao = userDao;
    this.bankAccountDao = bankAccountDao;
  }


  @Override
  public void emailAndUsernameValidation(String email, String username)
  {
    User user = this.userDao.findUserByEmail(email);
    if (!(email.matches("([^-_][a-zA-Z0-9\\._]+[^-_])@([a-zA-Z0-9]*\\.[a-zA-Z0-9]+)+"))) {
      throw new InvalidException(INVALID_EMAIL);
    }
    else if (!user.getEmail().equals("ex")) {
      throw new InvalidException(EMAIL_ALREADY_EXIST);
    }
    else if (!(username.matches("^[A-Za-z0-9.\\-]+$"))) {
      throw new InvalidException(INVALID_USERNAME);
    }
    else if (!(this.userDao.findUserByUsername(username).getEmail().equals("ex"))) {
      throw new InvalidException(USERNAME_ALREADY_EXIST);
    }
  }

  @Override
  public void passwordValidation(String password, String repeatPassword)
  {
    if (!(password.equals(repeatPassword))) {
      throw new InvalidException(PASSWORD_DONT_MATCH);
    }
    boolean containsUpperCaseLetter = false;
    boolean containsLowerCaseLetter = false;
    boolean containsSpecialSymbol = false;
    boolean containsDigit = false;
    boolean containsWhiteSpace = false;
    for (char c : password.toCharArray()) {
      if (Character.isUpperCase(c)) {
        containsUpperCaseLetter = true;
      }
      if (Character.isLowerCase(c)) {
        containsLowerCaseLetter = true;
      }
      if (!Character.isAlphabetic(c) && (!Character.isDigit(c)) && (!Character.isWhitespace(c))) {
        containsSpecialSymbol = true;
      }
      if (Character.isDigit(c)) {
        containsDigit = true;
      }
      if (Character.isWhitespace(c)) {
        containsWhiteSpace = true;
      }
    }
    if (!containsSpecialSymbol) {
      throw new InvalidException(PASSWORD_SHOULD_HAVE_SPECIAL_SYMBOL);
    }
    else if (!containsDigit) {
      throw new InvalidException(PASSWORD_SHOULD_HAVE_DIGIT);
    }
    else if (!containsUpperCaseLetter) {
      throw new InvalidException(PASSWORD_SHOULD_HAVE_UPPERCASE_LETTER);
    }
    else if (!containsLowerCaseLetter) {
      throw new InvalidException(PASSWORD_SHOULD_HAVE_LOWERCASE_LETTER);
    }
    else if (containsWhiteSpace) {
      throw new InvalidException(PASSWORD_CANT_HAVE_WHITESPACE);
    }
    else if (password.length() < 7) {
      throw new InvalidException(PASSWORD_SHOULD_BE_MAX_7_SYMBOLS);
    }
  }

  @Override
  public void checkForWrongUpdateOfMoney(BigDecimal newMoney, BigDecimal currentMoney)
  {
    if (newMoney.compareTo(BigDecimal.ZERO) < 0) {
      throw new InvalidException(NEGATIVE_MONEY);
    }
    //currentMoney > maxMoneyAmount
    if (currentMoney.compareTo(newMoney) > 0) {
      throw new InvalidException(INVALID_MONEY_UPDATE_INPUT);
    }

  }

  @Override
  public void checkForInvalidMoney(BigDecimal money)
  {
    if (money.compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidException(NEGATIVE_MONEY);
    }
  }

  public void checkForValidIban(String iban)
  {
    if (!iban.matches("[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}")) {
      throw new InvalidException(INVALID_IBAN);
    }
    if (this.bankAccountDao.findByIban(iban) > 0) {
      throw new InvalidException(IBAN_ALREADY_EXIST);
    }
  }
}
