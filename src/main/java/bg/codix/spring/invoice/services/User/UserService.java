package bg.codix.spring.invoice.services.User;


import bg.codix.spring.invoice.dto.FileInfoDto;
import bg.codix.spring.invoice.dto.UserDto;
import bg.codix.spring.invoice.dto.UserRequest;
import bg.codix.spring.invoice.dto.UserResponse;
import bg.codix.spring.invoice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface UserService
{
  //Християн
  void createUser(UserRequest userRequest);

  //Християн
  Page<User> getAllUsers(Pageable page);

  //Цветомир
  Page<UserDto> showAllFactors(Pageable page);

  //Ивета
  void createContract(String username);

  //Християн
  User getCurrentUser();

  //Християн
  List<FileInfoDto> findFileNamesOfFactorByEmail(Pageable page);

  //Ивета
  void updateMoneyAmount(String username, BigDecimal newMoney);

  //Ивета
  String lockUserAccount(String email, boolean isAccountLocked);

  //Ерик
  String approveInvoiceInDatabase(boolean approveOrDenyStatus, Long number);

  //Ивета
  void changePassword(String oldPassword, String newPassword, String repeatNewPassword);

  //Ерик
  List<UserResponse> showConnectedUsers(Pageable page);

}

