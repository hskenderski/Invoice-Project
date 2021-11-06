package bg.codix.spring.invoice.common;

import java.math.BigDecimal;

public interface Validation
{
  void emailAndUsernameValidation(String email, String username);

  void checkForWrongUpdateOfMoney(BigDecimal newMoney, BigDecimal currentMoney);

  void passwordValidation(String password, String repeatPassword);

  void checkForInvalidMoney(BigDecimal money);

  void checkForValidIban(String iban);
}
