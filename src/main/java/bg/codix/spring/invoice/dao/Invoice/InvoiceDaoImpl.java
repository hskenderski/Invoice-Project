package bg.codix.spring.invoice.dao.Invoice;

import bg.codix.spring.invoice.entities.Invoice;
import bg.codix.spring.invoice.exceptions.InvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Repository
public class InvoiceDaoImpl implements InvoiceDao
{
  private final NamedParameterJdbcOperations namedParameterJdbcTemplate;

  @Autowired
  public InvoiceDaoImpl(NamedParameterJdbcOperations namedParameterJdbcTemplate){
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }


  @Override
  public Invoice getFileNameById(Long invoiceId){
    String sql =
        "SELECT I.FILE_NAME              "
       +"FROM INVOICES I                 "
       +"WHERE I.INVOICE_ID = :invoiceId ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("invoiceId", invoiceId);

    try{
      return this.namedParameterJdbcTemplate.queryForObject(sql,parameters,(rs, rowNum) ->
          new Invoice(
              rs.getString("FILE_NAME")));
    }catch (EmptyResultDataAccessException ex){
      throw new InvalidException("File doesn't exist!");
    }

  }

  @Override
  public Long createInvoiceInFile(Invoice invoice, String fileName)
  {
    String sql =
             "INSERT INTO INVOICES                     "
            +"                       (FILE_NAME,       "
            +"                        STATUS,          "
            +"                        FIRM_NAME,       "
            +"                        ISSUE_DATE,      "
            +"                        MONEY,           "
            +"                        FACTOR_USERNAME) "
            +"                      VALUES             "
            +"                       (:fileName,       "
            +"                       :status,          "
            +"                       :firmName,        "
            +"                       :issueDate,       "
            +"                       :money,           "
            +"                       :factorUsername)  ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("fileName", fileName)
        .addValue("status","APPROVED")
        .addValue("firmName", invoice.getFirmName())
        .addValue("issueDate", invoice.getIssueDate())
        .addValue("money", invoice.getMoney())
        .addValue("factorUsername", invoice.getFactorUsername());


    final KeyHolder holder = new GeneratedKeyHolder();
    this.namedParameterJdbcTemplate.update(sql,parameters,holder,new String[]{"INVOICE_ID"});
    Number generatedId = holder.getKey();
    return Objects.requireNonNull(generatedId).longValue();
  }

  @Transactional
  @Override
  public void insertIntoUserInvoices(Long userId, Long invoiceId)
  {
    String sql =
             "INSERT INTO USER_INVOICES                   "
            +"                             (USER_ID,      "
            +"                              INVOICE_ID)   "
            +"                            VALUES          "
            +"                              (:userId,     "
            +"                               :invoiceId)  ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("userId", userId)
        .addValue("invoiceId", invoiceId);

    this.namedParameterJdbcTemplate.update(sql, parameters);

  }

  @Override
  public Long createInvoiceInDatabase(Invoice invoice)
  {
    String sql =
             "INSERT INTO INVOICES                      "
            +"                       (FIRM_NAME,        "
            +"                        ISSUE_DATE,       "
            +"                        MONEY,            "
            +"                        STATUS,           "
            +"                        FACTOR_USERNAME)  "
            +"                      VALUES              "
            +"                       (:firmName,        "
            +"                        :issueDate,       "
            +"                        :money,           "
            +"                        :status,          "
            +"                        :factorUsername)  ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("firmName", invoice.getFirmName())
        .addValue("issueDate", invoice.getIssueDate())
        .addValue("money", invoice.getMoney())
        .addValue("status","WAITING")
        .addValue("factorUsername", invoice.getFactorUsername());

    final KeyHolder holder = new GeneratedKeyHolder();
    this.namedParameterJdbcTemplate.update(sql,parameters,holder,new String[]{"INVOICE_ID"});
    Number generatedId = holder.getKey();
    return Objects.requireNonNull(generatedId).longValue();
  }

  public Invoice findInvoiceById(Long invoiceId){
    String sql =
        "SELECT I.MONEY,                 "
       +"       I.STATUS,                "
       +"       I.FACTOR_USERNAME        "
       +" FROM INVOICES I                "
       +"WHERE I.INVOICE_ID = :invoiceId ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("invoiceId", invoiceId);

    return this.namedParameterJdbcTemplate.queryForObject(sql,parameters,(rs, rowNum) ->
        new Invoice(
            rs.getBigDecimal("MONEY"),
            rs.getString("STATUS"),
            rs.getString("FACTOR_USERNAME")
            ));
  }

  public void changeInvoiceStatus(String status, Long invoiceId){
    String sql =
             "UPDATE INVOICES I               "
            +"  SET STATUS = :status          "
            +"WHERE I.INVOICE_ID = :invoiceId ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("invoiceId", invoiceId)
        .addValue("status",status);

    this.namedParameterJdbcTemplate.update(sql,parameters);

  }

  @Override
  public void removeUserInvoices(Long invoiceId)
  {
    String sql =
        "DELETE FROM USER_INVOICES       "
       +"WHERE  INVOICE_ID = :invoiceId  ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("invoiceId",invoiceId);

    this.namedParameterJdbcTemplate.update(sql,parameters);
  }

  @Override
  public void removeInvoice(Long invoiceId)
  {
    String sql =
        "DELETE FROM INVOICES I            "
       +"WHERE  I.INVOICE_ID = :invoiceId  ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("invoiceId",invoiceId);

    this.namedParameterJdbcTemplate.update(sql,parameters);
  }

  @Override
  public List<Invoice> allInvoicesOfUser(Long userId)
  {
    String sql =
        "SELECT INVOICE_ID          "
       +"   FROM USER_INVOICES      "
       +" WHERE  USER_ID = :userId  ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("userId",userId);

    return this.namedParameterJdbcTemplate.query(sql,parameters, (rs, rowNum) ->
        new Invoice(
            rs.getLong("INVOICE_ID")
           ));  }


}
