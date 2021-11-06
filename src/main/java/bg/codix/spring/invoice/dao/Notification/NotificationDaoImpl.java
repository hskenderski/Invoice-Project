package bg.codix.spring.invoice.dao.Notification;

import bg.codix.spring.invoice.dto.NotificationResponse;
import bg.codix.spring.invoice.entities.Notification;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class NotificationDaoImpl implements NotificationDao
{
  private final NamedParameterJdbcOperations namedParameterJdbcTemplate;
  private final ModelMapper modelMapper = new ModelMapper();
  @Autowired
  public NotificationDaoImpl(NamedParameterJdbcOperations namedParameterJdbcTemplate){
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public Long addNotification(Notification notification)
  {
    String sql =
        "INSERT INTO NOTIFICATION            "
       +"                       (TEXT,       "
       +"                        ISSUE_DATE) "
       +"                      VALUES        "
       +"                       (:text,      "
       +"                        :issueDate) ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("text", notification.getText())
        .addValue("issueDate", notification.getDate());

    final KeyHolder holder = new GeneratedKeyHolder();
    this.namedParameterJdbcTemplate.update(sql,parameters,holder,new String[]{"NOTIFICATION_ID"});
    Number generatedId = holder.getKey();
    return Objects.requireNonNull(generatedId).longValue();

  }


  @Override
  public void addNotificationUser(Long userId, Long notificationId)
  {
    String sql =
        "INSERT INTO USER_NOTIFICATION                   "
       +"                             (USER_ID,          "
       +"                              NOTIFICATION_ID)  "
       +"                            VALUES              "
       +"                              (:userId,         "
       +"                               :notificationId) ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("userId", userId)
        .addValue("notificationId", notificationId);

    this.namedParameterJdbcTemplate.update(sql, parameters);
  }

  @Override
  public Page<NotificationResponse> showNotification(Pageable page, Long loggedUserId)
  {
    int start = page.getPageNumber() * page.getPageSize();
    int end = page.getPageSize() * (page.getPageNumber() + 1);

    String sql =
        "SELECT N.NOTIFICATION_ID,                    "
       +"       N.TEXT,                               "
       +"       N.ISSUE_DATE                          "
       +"   FROM NOTIFICATION N                       "
       +"   JOIN USER_NOTIFICATION UN                 "
       +"   ON N.NOTIFICATION_ID = UN.NOTIFICATION_ID "
       +" WHERE UN.USER_ID = :userId                  "
       +" ORDER BY N.ISSUE_DATE DESC                  "
       +"OFFSET                                       "
       +start
       +"ROWS FETCH NEXT                              "
       +end
       +"ROWS ONLY                                    ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("userId", loggedUserId);

    List<Notification> allNotificationOfLoggedUser = this.namedParameterJdbcTemplate.query(sql, parameters, (rs, rowNum) ->
        new Notification(
            rs.getLong("NOTIFICATION_ID"),
            rs.getString("TEXT"),
            rs.getObject("ISSUE_DATE", LocalDateTime.class)
        ));

    Page<Notification> p = new PageImpl<>(allNotificationOfLoggedUser, page, notificationCount());

    Type type = new TypeToken<Page<NotificationResponse>>()
    {
    }.getType();

    return this.modelMapper.map(p, type);
  }

  private Integer notificationCount()
  {
    String sql =
        "SELECT COUNT(*)                              "
       +"   FROM NOTIFICATION N                       "
       +"   JOIN USER_NOTIFICATION UN                 "
       +"   ON N.NOTIFICATION_ID = UN.NOTIFICATION_ID ";


    return namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource(),Integer.class);

  }

  @Override
  public void removeUserNotifications(Long userId)
  {
    String sql =
        "DELETE FROM USER_NOTIFICATION UN "
       +"WHERE  UN.USER_ID = :userId      ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("userId",userId);

    this.namedParameterJdbcTemplate.update(sql,parameters);
  }

  @Override
  public List<Notification> allNotificationsOfUser(Long userId)
  {
    String sql =
        "SELECT NOTIFICATION_ID     "
       +"   FROM USER_NOTIFICATION  "
       +" WHERE  USER_ID = :userId  ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("userId",userId);

    return this.namedParameterJdbcTemplate.query(sql,parameters, (rs, rowNum) ->
        new Notification(
            rs.getLong("NOTIFICATION_ID")));
  }

  @Override
  public void removeNotification(Long notificationId)
  {
    String sql =
      "DELETE FROM NOTIFICATION                  "
     +"WHERE  NOTIFICATION_ID = :notificationId  ";

    SqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("notificationId",notificationId);

    this.namedParameterJdbcTemplate.update(sql,parameters);
  }
}
