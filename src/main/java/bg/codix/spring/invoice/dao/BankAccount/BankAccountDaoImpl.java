package bg.codix.spring.invoice.dao.BankAccount;

import bg.codix.spring.invoice.entities.BankAccount;
import bg.codix.spring.invoice.entities.User;
import bg.codix.spring.invoice.exceptions.InvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Objects;

import static bg.codix.spring.invoice.common.ExceptionMessages.INVALID_BANK_ACCOUNT;

@Repository
public class BankAccountDaoImpl implements BankAccountDao
{
  private final NamedParameterJdbcOperations namedParameterJdbcTemplate;

  @Autowired
  public BankAccountDaoImpl(NamedParameterJdbcOperations namedParameterJdbcTemplate){
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public Long addBankAccount(BankAccount bankAccount)
  {
    String sql =
        "INSERT INTO BANKACCOUNT                "
       +"                       (IBAN,          "
       +"                        MONEY_AMOUNT)  "
       +"                      VALUES           "
       +"                       (:iban,         "
       +"                        :moneyAmount)  ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("iban", bankAccount.getIban())
        .addValue("moneyAmount", bankAccount.getMoneyAmount());

    final KeyHolder holder = new GeneratedKeyHolder();
    this.namedParameterJdbcTemplate.update(sql,parameters,holder,new String[]{"ACC_ID"});
    Number generatedId = holder.getKey();
    return Objects.requireNonNull(generatedId).longValue();
  }

  @Override
  public Long findByIban(String iban)
  {
    String sql =
        "SELECT COUNT(BA.IBAN)  "
       +"  FROM BANKACCOUNT BA  "
       +" WHERE BA.IBAN = :iban ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("iban", iban);
    return namedParameterJdbcTemplate.queryForObject(sql,parameters,Long.class);
  }

  public BankAccount findById(Long bankAccId){
    String sql =
             "SELECT BA.IBAN,                "
            +"       BA.MONEY_AMOUNT         "
            +"  FROM BANKACCOUNT BA          "
            +" WHERE BA.ACC_ID = :bankAccId ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("bankAccId", bankAccId);

    try {
      return this.namedParameterJdbcTemplate.queryForObject(sql, parameters,
          (rs, rowNum) ->
              new BankAccount(
                  rs.getString("IBAN"),
                  rs.getBigDecimal("MONEY_AMOUNT")
              ));
    } catch (EmptyResultDataAccessException ex) {
      throw new InvalidException(INVALID_BANK_ACCOUNT);
    }
  }

  @Override
  public void removeBankAccount(Long accID){
    String sql =
        "DELETE FROM BANKACCOUNT B            "
       +"WHERE  B.ACC_ID = :accID             ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("accID",accID);

    this.namedParameterJdbcTemplate.update(sql,parameters);
  }


  @Override
  public void updateBankAccountMoneyOfUser(User.RoleName role, BigDecimal money, Long bankAccount)
  {
    String sql =
        "UPDATE BANKACCOUNT                             "
       +"SET MONEY_AMOUNT = MONEY_AMOUNT + :moneyAmount "
       +" WHERE BANKACCOUNT.ACC_ID = :bankAccount       ";

    String sqlUpdateOfFactor =
        "UPDATE BANKACCOUNT                             "
       +"SET MONEY_AMOUNT = MONEY_AMOUNT - :moneyAmount "
       +" WHERE BANKACCOUNT.ACC_ID = :bankAccount       ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("moneyAmount", money)
        .addValue("bankAccount", bankAccount);

    if(role.equals(User.RoleName.SUPPLIER)){
      this.namedParameterJdbcTemplate.update(sql, parameters);
      return;
    }
    this.namedParameterJdbcTemplate.update(sqlUpdateOfFactor, parameters);
  }

}
