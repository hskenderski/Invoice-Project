package bg.codix.spring.invoice.dao.User;


import bg.codix.spring.invoice.dto.UserDto;
import bg.codix.spring.invoice.dto.UserResponse;
import bg.codix.spring.invoice.entities.Invoice;
import bg.codix.spring.invoice.entities.SupplierFactor;
import bg.codix.spring.invoice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public interface UserDao
{
  User findUserByEmail(String email);

  User findUserByUsername(String username);

  Page<User> getAllUser(Pageable page);

  Long addUser(User user);

  Page<UserDto> showAllFactors(Pageable page);

  void insertContractBetweenSupplierAndFactor(Long supplierId, Long factorId, BigDecimal maxMoneyAmount, BigDecimal currentMoney,
                                              LocalDateTime issueDate);

  void updateCurrentMoneyOfSupplier(BigDecimal currentMoney, Long supplierId, Long factorId);

  void checkForContract(Long supplierId, Long factorId);

  SupplierFactor getContractInformation(Long supplierId, Long factorId);

  List<Invoice> findFileNamesOfFactorByEmail(Pageable page, String email);

  void refactorMaxMoneyAmount(Long supplierId, BigDecimal newMoney, Long factorId);

  void lockAccountOfUser(String email, boolean isAccountLocked);

  User findUserByInvoiceId(Long invoiceId);

  void removeUser(String email);

  void removeContract(Long supplierId);

  void changePassword(Long userId, String newPassword);

  List<UserResponse> showConnectedUsers(Pageable page, Long userId);

}
