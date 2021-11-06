package bg.codix.spring.invoice.services.BankAccount;

import bg.codix.spring.invoice.common.Validation;
import bg.codix.spring.invoice.dao.BankAccount.BankAccountDao;
import bg.codix.spring.invoice.entities.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BankAccountServiceImpl implements BankAccountService
{
  private final BankAccountDao bankAccountDao;
  private final Validation     validation;

  @Autowired
  public BankAccountServiceImpl(BankAccountDao bankAccountDao, Validation validation)
  {
    this.bankAccountDao = bankAccountDao;
    this.validation = validation;
  }

  @Override
  public Long createBankAccount(BankAccount bankAccount)
  {
    this.validation.checkForValidIban(bankAccount.getIban());
    return this.bankAccountDao.addBankAccount(bankAccount);
  }
}
