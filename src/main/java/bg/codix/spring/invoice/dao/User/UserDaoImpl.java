package bg.codix.spring.invoice.dao.User;


import bg.codix.spring.invoice.dto.UserDto;
import bg.codix.spring.invoice.dto.UserResponse;
import bg.codix.spring.invoice.entities.Invoice;
import bg.codix.spring.invoice.entities.SupplierFactor;
import bg.codix.spring.invoice.entities.User;
import bg.codix.spring.invoice.exceptions.InvalidException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static bg.codix.spring.invoice.common.ExceptionMessages.INVALID_INVOICE_NUMBER;
import static bg.codix.spring.invoice.common.ExceptionMessages.NO_CONTRACT;

@Repository
public class UserDaoImpl implements UserDao
{
  private final NamedParameterJdbcOperations namedParameterJdbcTemplate;

  @Autowired
  public UserDaoImpl(NamedParameterJdbcOperations namedParameterJdbcTemplate)
  {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Transactional
  @Override
  public User findUserByEmail(String email)
  {
    String sql =
              "SELECT U.USER_ID,                      "
             +"       U.FIRST_NAME,                   "
             +"       U.MIDDLE_NAME,                  "
             +"       U.LAST_NAME,                    "
             +"       U.AGE,                          "
             +"       U.EMAIL,                        "
             +"       U.USERNAME,                     "
             +"       U.PASSWORD,                     "
             +"       U.SALARY,                       "
             +"       U.FACTOR_CONTRACT_DEFAULT_SUM,  "
             +"       U.ROLE,                         "
             +"       U.IS_LOCK,                      "
             +"       U.BANK_ACC_ID,                  "
             +"       U.ADDRESS_ID                    "
             +"  FROM USERS U                         "
             +" WHERE U.EMAIL = :email                ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("email", email);
    try
    {
      return this.namedParameterJdbcTemplate.queryForObject(sql, parameters, (rs, rowNum) ->
            new User(
              rs.getLong("USER_ID"),
              rs.getString("FIRST_NAME"),
              rs.getString("MIDDLE_NAME"),
              rs.getString("LAST_NAME"),
              rs.getInt("AGE"),
              rs.getString("EMAIL"),
              rs.getString("USERNAME"),
              rs.getString("PASSWORD"),
              rs.getBigDecimal("SALARY"),
              rs.getBigDecimal("FACTOR_CONTRACT_DEFAULT_SUM"),
              rs.getObject("ROLE", User.RoleName.class),
              rs.getBoolean("IS_LOCK"),
              rs.getLong("BANK_ACC_ID"),
              rs.getLong("ADDRESS_ID"))
             );
    } catch (EmptyResultDataAccessException ex)
    {
      User user = new User();
      user.setEmail("ex");
      return user;
    }
  }

  @Transactional
  @Override
  public User findUserByUsername(String username)
  {
    String sql =
        "SELECT U.USER_ID,                      "
       +"       U.FIRST_NAME,                   "
       +"       U.MIDDLE_NAME,                  "
       +"       U.LAST_NAME,                    "
       +"       U.AGE,                          "
       +"       U.EMAIL,                        "
       +"       U.USERNAME,                     "
       +"       U.PASSWORD,                     "
       +"       U.SALARY,                       "
       +"       U.FACTOR_CONTRACT_DEFAULT_SUM,  "
       +"       U.ROLE,                         "
       +"       U.BANK_ACC_ID,                  "
       +"       U.ADDRESS_ID                    "
       +"  FROM USERS U                         "
       +" WHERE U.USERNAME = :username          ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("username", username);
    try
    {
      return namedParameterJdbcTemplate.queryForObject(sql, parameters,(rs, rowNum) ->
          new User(
              rs.getLong("USER_ID"),
              rs.getString("FIRST_NAME"),
              rs.getString("MIDDLE_NAME"),
              rs.getString("LAST_NAME"),
              rs.getInt("AGE"),
              rs.getString("EMAIL"),
              rs.getString("USERNAME"),
              rs.getString("PASSWORD"),
              rs.getBigDecimal("SALARY"),
              rs.getBigDecimal("FACTOR_CONTRACT_DEFAULT_SUM"),
              rs.getObject("ROLE", User.RoleName.class),
              rs.getLong("BANK_ACC_ID"),
              rs.getLong("ADDRESS_ID")));
    }
    catch (EmptyResultDataAccessException ex)
    {
      User user = new User();
      user.setEmail("ex");
      return user;
    }
  }

  @Transactional
  @Override
  public SupplierFactor getContractInformation(Long supplierId, Long factorId)
  {
    String sql =
        "SELECT SF.MAX_MONEY_AMOUNT,          "
       +"       SF.CURRENT_MONEY              "
       +"  FROM SUPPLIER_FACTOR SF            "
       +" WHERE SF.SUPPLIER_ID = :supplierId  "
       +"   AND SF.FACTOR_ID = :factorId      ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("supplierId", supplierId)
        .addValue("factorId", factorId);

    return namedParameterJdbcTemplate.queryForObject(sql, parameters, (rs, row) ->
        new SupplierFactor(
          rs.getBigDecimal("MAX_MONEY_AMOUNT"),
          rs.getBigDecimal("CURRENT_MONEY")
      )
    );
  }

  private Integer usersCount()
  {
    String sql =
        "SELECT COUNT(*)  "
       +" FROM USERS      ";

      return namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Integer.class);

  }


  @Override
  public Page<User> getAllUser(Pageable page)
  {
    int start = page.getPageNumber() * page.getPageSize();      //0-2 0-2 | 0-3 0-3
    int end = page.getPageSize() * (page.getPageNumber() + 1);  //2-4 1-2 | 3-6 1-3
                                                                //4-6 2-2 | 6-9 2-3
    String sql =
        "SELECT U.USER_ID,                      "
       +"       U.FIRST_NAME,                   "
       +"       U.MIDDLE_NAME,                  "
       +"       U.LAST_NAME,                    "
       +"       U.AGE,                          "
       +"       U.EMAIL,                        "
       +"       U.USERNAME,                     "
       +"       U.PASSWORD,                     "
       +"       U.SALARY,                       "
       +"       U.FACTOR_CONTRACT_DEFAULT_SUM,  "
       +"       U.ROLE,                         "
       +"       U.BANK_ACC_ID,                  "
       +"       U.ADDRESS_ID                    "
       +" FROM USERS U                          "
       +" ORDER BY U.USER_ID                    "
       +" OFFSET                                "
       +start
       +" ROWS FETCH NEXT                       "
       +end
       +" ROWS ONLY                             ";

    List<User> users = this.namedParameterJdbcTemplate.query(sql,(rs, rowNum) ->
        new User(
          rs.getLong("USER_ID"),
          rs.getString("FIRST_NAME"),
          rs.getString("MIDDLE_NAME"),
          rs.getString("LAST_NAME"),
          rs.getInt("AGE"),
          rs.getString("EMAIL"),
          rs.getString("USERNAME"),
          rs.getString("PASSWORD"),
          rs.getBigDecimal("SALARY"),
          rs.getBigDecimal("FACTOR_CONTRACT_DEFAULT_SUM"),
          User.RoleName.valueOf(rs.getString("ROLE")),
          rs.getLong("BANK_ACC_ID"),
          rs.getLong("ADDRESS_ID")));

    return new PageImpl<>(users, page, usersCount());
  }

  @Transactional
  @Override
  public Long addUser(User user)
  {
    String sql =
        "INSERT INTO USERS                              "
       +"                 (FIRST_NAME,                  "
       +"                  MIDDLE_NAME,                 "
       +"                  LAST_NAME,                   "
       +"                  AGE,                         "
       +"                  EMAIL,                       "
       +"                  USERNAME,                    "
       +"                  PASSWORD,                    "
       +"                  SALARY,                      "
       +"                  FACTOR_CONTRACT_DEFAULT_SUM, "
       +"                  IS_LOCK,                     "
       +"                  ROLE,                        "
       +"                  BANK_ACC_ID,                 "
       +"                  ADDRESS_ID)                  "
       +"               VALUES                          "
       +"                 (:firstName,                  "
       +"                  :middleName,                 "
       +"                  :lastName,                   "
       +"                  :age,                        "
       +"                  :email,                      "
       +"                  :username,                   "
       +"                  :password,                   "
       +"                  :salary,                     "
       +"                  :factorContractDefaultSum,   "
       +"                  :isLock,                     "
       +"                  :roleName,                   "
       +"                  :bankAccId,                  "
       +"                  :addressId)                  ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("firstName", user.getFirstName())
        .addValue("middleName", user.getMiddleName())
        .addValue("lastName", user.getLastName())
        .addValue("age", user.getAge())
        .addValue("email", user.getEmail())
        .addValue("username", user.getUsername())
        .addValue("password", user.getPassword())
        .addValue("salary", user.getSalary())
        .addValue("factorContractDefaultSum", user.getFactorContractDefaultSum())
        .addValue("isLock", user.isLock())
        .addValue("roleName", user.getRoleName().name())
        .addValue("bankAccId", user.getBankAccountId())
        .addValue("addressId", user.getAddressId());

    final KeyHolder holder = new GeneratedKeyHolder();
    this.namedParameterJdbcTemplate.update(sql, parameters,holder,new String[]{"USER_ID"});
    Number generatedId = holder.getKey();
    return Objects.requireNonNull(generatedId).longValue();
  }

  @Override
  public Page<UserDto> showAllFactors(Pageable page)
  {
    int start = page.getPageNumber() * page.getPageSize();
    int end = page.getPageSize() * (page.getPageNumber() + 1);

    String sql =
        "SELECT U.USER_ID,                        "
       +"       U.FIRST_NAME,                     "
       +"       U.MIDDLE_NAME,                    "
       +"       U.LAST_NAME,                      "
       +"       U.AGE,                            "
       +"       U.EMAIL,                          "
       +"       U.USERNAME,                       "
       +"       U.PASSWORD,                       "
       +"       U.SALARY,                         "
       +"       U.FACTOR_CONTRACT_DEFAULT_SUM,    "
       +"       U.ROLE,                           "
       +"       U.IS_LOCK,                        "
       +"       U.BANK_ACC_ID,                    "
       +"       U.ADDRESS_ID                      "
       +"FROM USERS U                             "
       +"WHERE ROLE LIKE 'FACTOR'                 "
       +" ORDER BY U.FACTOR_CONTRACT_DEFAULT_SUM  "
       +"OFFSET                                   "
       +start
       +"ROWS FETCH NEXT                          "
       +end
       +"ROWS ONLY                                ";

    List<UserDto> users = this.namedParameterJdbcTemplate.query(sql,(rs, rowNum) ->
        new UserDto(
            rs.getLong("USER_ID"),
            rs.getString("FIRST_NAME"),
            rs.getString("MIDDLE_NAME"),
            rs.getString("LAST_NAME"),
            rs.getInt("AGE"),
            rs.getString("EMAIL"),
            rs.getString("USERNAME"),
            rs.getString("PASSWORD"),
            rs.getBigDecimal("SALARY"),
            rs.getBigDecimal("FACTOR_CONTRACT_DEFAULT_SUM"),
            User.RoleName.valueOf(rs.getString("ROLE")),
            rs.getLong("BANK_ACC_ID"),
            rs.getLong("ADDRESS_ID")));

    return new PageImpl<>(users, page, usersCount());
  }

  @Override
  public void insertContractBetweenSupplierAndFactor(Long supplierId, Long factorId, BigDecimal maxMoneyAmount,
                                                     BigDecimal currentMoney, LocalDateTime issueDate)
  {
    String sql =
        "INSERT INTO SUPPLIER_FACTOR                  "
       +"                         VALUES              "
       +"                           (:supplierId,     "
       +"                           :factorId,        "
       +"                           :maxMoneyAmount,  "
       +"                           :currentMoney,    "
       +"                           :issueDate)       ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("supplierId",supplierId)
        .addValue("factorId",factorId)
        .addValue("maxMoneyAmount",maxMoneyAmount)
        .addValue("currentMoney",currentMoney)
        .addValue("issueDate",issueDate);
    try{
      this.namedParameterJdbcTemplate.update(sql, parameters);
    }catch (DataAccessException ex){
      throw new InvalidException("You already have contract with that factor!");
    }


  }

  @Override
  public void updateCurrentMoneyOfSupplier(BigDecimal currentMoney, Long supplierId, Long factorId)
  {
    String sql =
        "UPDATE SUPPLIER_FACTOR                               "
       +"   SET CURRENT_MONEY = CURRENT_MONEY + :currentMoney "
       +" WHERE SUPPLIER_ID = :supplierId                     "
       +"   AND FACTOR_ID = :factorId                         ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("currentMoney",currentMoney)
        .addValue("supplierId",supplierId)
        .addValue("factorId",factorId);
    this.namedParameterJdbcTemplate.update(sql,parameters);
  }

  @Override
  public void checkForContract(Long supplierId, Long factorId)
  {
    String sql =
        "SELECT SUPPLIER_ID                 "
       +"  FROM SUPPLIER_FACTOR             "
       +" WHERE SUPPLIER_ID = :supplierId   "
       +"   AND FACTOR_ID = :factorId       ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("supplierId", supplierId)
        .addValue("factorId", factorId);
    try
    {
      this.namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);
    }
    catch (EmptyResultDataAccessException ex)
    {
      throw new InvalidException(NO_CONTRACT);
    }
  }

  public List<Invoice> findFileNamesOfFactorByEmail(Pageable page, String username)
  {
    int start = page.getPageNumber() * page.getPageSize();
    int end = page.getPageSize() * (page.getPageNumber() + 1);
    String sql =
         "SELECT I.FILE_NAME,                        "
        +"I.INVOICE_ID                               "
        +" FROM INVOICES I                           "
        +"WHERE I.FACTOR_USERNAME = :factorUsername  "
        +"AND I.FILE_NAME is not null                "
        +" ORDER BY I.ISSUE_DATE                     "
        +" OFFSET                                    "
        +start
        +" ROWS FETCH NEXT                           "
        +end
        +" ROWS ONLY                                 ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("factorUsername",username);

    return this.namedParameterJdbcTemplate.query(sql,parameters,(rs, rowNum) ->
        new Invoice(
            rs.getString("FILE_NAME"),
            rs.getLong("INVOICE_ID")));
  }

  @Override
  public void refactorMaxMoneyAmount(Long supplierId, BigDecimal newMoney, Long factorId)
  {
    String sql =
         "UPDATE SUPPLIER_FACTOR                    "
        +"   SET MAX_MONEY_AMOUNT = :maxMoneyAmount "
        +" WHERE SUPPLIER_ID = :supplierId          "
        +"   AND FACTOR_ID = :factorId              ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("maxMoneyAmount", newMoney)
        .addValue("supplierId", supplierId)
        .addValue("factorId", factorId);

    this.namedParameterJdbcTemplate.update(sql, parameters);
  }

  @Override
  public void lockAccountOfUser(String email, boolean isAccountLocked)
  {
    String sql =
         "UPDATE USERS                 "
        +"   SET IS_LOCK = :isLock     "
        +" WHERE EMAIL = :email        ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("email", email)
        .addValue("isLock", isAccountLocked);

    this.namedParameterJdbcTemplate.update(sql, parameters);
  }

  public User findUserByInvoiceId(Long invoiceId){
    String sql =
        "SELECT U.USER_ID,                        "
       +"       U.FIRST_NAME,                     "
       +"       U.MIDDLE_NAME,                    "
       +"       U.LAST_NAME,                      "
       +"       U.AGE,                            "
       +"       U.EMAIL,                          "
       +"       U.USERNAME,                       "
       +"       U.PASSWORD,                       "
       +"       U.SALARY,                         "
       +"       U.FACTOR_CONTRACT_DEFAULT_SUM,    "
       +"       U.ROLE,                           "
       +"       U.IS_LOCK,                        "
       +"       U.BANK_ACC_ID,                    "
       +"       U.ADDRESS_ID                      "
       +" FROM INVOICES                           "
       +"JOIN USER_INVOICES UI                    "
       +" ON INVOICES.INVOICE_ID = UI.INVOICE_ID  "
       +"JOIN USERS U                             "
       +" ON U.USER_ID = UI.USER_ID               "
       +"WHERE UI.INVOICE_ID = :invoiceId         ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("invoiceId", invoiceId);

    try {
      return this.namedParameterJdbcTemplate.queryForObject(sql, parameters, (rs, rowNum) ->
          new User(
              rs.getLong("USER_ID"),
              rs.getString("FIRST_NAME"),
              rs.getString("MIDDLE_NAME"),
              rs.getString("LAST_NAME"),
              rs.getInt("AGE"),
              rs.getString("EMAIL"),
              rs.getString("USERNAME"),
              rs.getString("PASSWORD"),
              rs.getBigDecimal("SALARY"),
              rs.getBigDecimal("FACTOR_CONTRACT_DEFAULT_SUM"),
              User.RoleName.valueOf(rs.getString("ROLE")),
              rs.getLong("BANK_ACC_ID"),
              rs.getLong("ADDRESS_ID")));
    }catch (EmptyResultDataAccessException ex) {
      throw new InvalidException(INVALID_INVOICE_NUMBER);
    }
  }

  @Override
  public void removeUser(String email){
    String sql =
        "DELETE FROM USERS     "
       +"WHERE  EMAIL = :email ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("email",email);

    this.namedParameterJdbcTemplate.update(sql,parameters);
  }

  @Override
  public void removeContract(Long supplierId){
    String sql =
        "DELETE FROM SUPPLIER_FACTOR SF       "
       +"WHERE  SF.SUPPLIER_ID = :supplierId  ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("supplierId",supplierId);

    this.namedParameterJdbcTemplate.update(sql,parameters);
  }

  @Override
  public void changePassword(Long userId, String newPassword)
  {
    String sql =
        "UPDATE USERS U                   "
       +" SET U.PASSWORD = :newPassword   "
       +"WHERE U.USER_ID = :userId        ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("userId",userId)
        .addValue("newPassword",newPassword);

    this.namedParameterJdbcTemplate.update(sql,parameters);
  }
  @Override
  public List<UserResponse> showConnectedUsers(Pageable page, Long userId) {

    int start = page.getPageNumber() * page.getPageSize();
    int end = page.getPageSize() * (page.getPageNumber() + 1);

    String sql =
             "SELECT U.FIRST_NAME,                      "
            +"       U.MIDDLE_NAME,                     "
            +"       U.LAST_NAME,                       "
            +"       U.AGE,                             "
            +"       U.EMAIL,                           "
            +"       U.USERNAME                         "
            +" FROM SUPPLIER_FACTOR SF                  "
            +"JOIN USERS U ON U.USER_ID = SF.SUPPLIER_ID"
            +" WHERE SF.FACTOR_ID = :factorId           "
            +" ORDER BY SF.ISSUE_DATE DESC              "
            +"OFFSET                                    "
            +start
            +"ROWS FETCH NEXT                           "
            +end
            +"ROWS ONLY                                 ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("factorId", userId);

    List<User> connectedUsers = this.namedParameterJdbcTemplate
        .query(sql, parameters, (rs, rowNum) ->
            new User(
                rs.getString("FIRST_NAME"),
                rs.getString("MIDDLE_NAME"),
                rs.getString("LAST_NAME"),
                rs.getInt("AGE"),
                rs.getString("EMAIL"),
                rs.getString("USERNAME")));

    Type type = new TypeToken<List<UserResponse>>() {
    }.getType();

    ModelMapper modelMapper = new ModelMapper();

    return modelMapper.map(connectedUsers,type);

  }

}