package bg.codix.spring.invoice.dao.BankAccount;

import bg.codix.spring.invoice.entities.BankAccount;
import bg.codix.spring.invoice.entities.User;

import java.math.BigDecimal;

public interface BankAccountDao
{
  Long addBankAccount(BankAccount bankAccount);

  Long findByIban(String iban);

  BankAccount findById(Long bankAcc_id);

  void removeBankAccount(Long accID);

  void updateBankAccountMoneyOfUser(User.RoleName role, BigDecimal money, Long bankAccountId);

}
