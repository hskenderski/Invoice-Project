package bg.codix.spring.invoice.security;

import bg.codix.spring.invoice.dao.User.UserDao;
import bg.codix.spring.invoice.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService
{

  private final UserDao userDao;

  @Autowired
  public UserDetailServiceImpl(UserDao userDao)
  {
    this.userDao = userDao;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
  {
    User user = this.userDao.findUserByEmail(email);

    if (user == null) {
      throw new UsernameNotFoundException("No such user found in database!");
    }

    if (user.isLock()) {
      throw new LockedException("Your account is lock!");
    }

    return org.springframework.security.core.userdetails.User
        .withUsername(email)
        .password(user.getPassword())
        .roles(user.getRoleName().name())
        .build();
  }
}
