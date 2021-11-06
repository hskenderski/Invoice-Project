package bg.codix.spring.invoice.dao.Address;

import bg.codix.spring.invoice.entities.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class AddressDaoImpl implements AddressDao
{
  private final NamedParameterJdbcOperations namedParameterJdbcTemplate;

  @Autowired
  public AddressDaoImpl(NamedParameterJdbcOperations namedParameterJdbcTemplate){
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public Long addAddress(Address address)
  {
    String sql =
             "INSERT INTO ADDRESS                      "
            +"                       (COUNTRY,         "
            +"                        REGION,          "
            +"                        CITY,            "
            +"                        NEIGHBORHOOD,    "
            +"                        STREET,          "
            +"                        ADDRESS_NUMBER,  "
            +"                        FLOOR)           "
            +"                      VALUES             "
            +"                       (:country,        "
            +"                        :region,         "
            +"                        :city,           "
            +"                        :neighborhood,   "
            +"                        :street,         "
            +"                        :addressNumber,  "
            +"                        :floor)          ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("country", address.getCountry())
        .addValue("region", address.getRegion())
        .addValue("city", address.getCity())
        .addValue("neighborhood", address.getNeighborhood())
        .addValue("street", address.getStreet())
        .addValue("addressNumber", address.getAddressNumber())
        .addValue("floor", address.getFloor());

    final KeyHolder holder = new GeneratedKeyHolder();
    this.namedParameterJdbcTemplate.update(sql,parameters,holder,new String[]{"ADDRESS_ID"});
    Number generatedId = holder.getKey();
    return Objects.requireNonNull(generatedId).longValue();
  }

  @Override
  public void removeAddress(Long addressId){
    String sql =
        "DELETE FROM ADDRESS A            "
       +"WHERE  A.ADDRESS_ID = :addressId ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("addressId",addressId);

    this.namedParameterJdbcTemplate.update(sql,parameters);
  }
}
