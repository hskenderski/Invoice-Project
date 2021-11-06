package bg.codix.spring.invoice.controllers;

import bg.codix.spring.invoice.dto.FileInfoDto;
import bg.codix.spring.invoice.dto.UserRequest;
import bg.codix.spring.invoice.services.FileStorage.FileStorageService;
import bg.codix.spring.invoice.services.Invoice.InvoiceService;
import bg.codix.spring.invoice.services.Notification.NotificationService;
import bg.codix.spring.invoice.services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

import static bg.codix.spring.invoice.common.OutputMessages.*;

@Controller
@Validated
@RequestMapping("/api/v1")
public class UserController
{
  private final UserService         userService;
  private final FileStorageService  fileStorageService;
  private final NotificationService notificationService;
  private final InvoiceService      invoiceService;

  @Autowired
  public UserController(UserService userService, FileStorageService fileStorageService, NotificationService notificationService, InvoiceService invoiceService)
  {
    this.notificationService = notificationService;
    this.userService = userService;
    this.fileStorageService = fileStorageService;
    this.invoiceService = invoiceService;
  }

  //Християн
  @PostMapping("/register/user")
  public ResponseEntity<?> register(@RequestBody @Valid UserRequest userRequest, BindingResult bindingResult)
  {
    this.userService.createUser(userRequest);
    return ResponseEntity.ok(SUCCESSFUL_REGISTRATION);
  }

  //Християн
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/show/all/users")
  public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") @Min(value = 0) Integer pageNumber,
                                       @Min(value = 1) @Max(value = 100) @RequestParam(defaultValue = "100")
                                           Integer pageCapacity)
  {
    return ResponseEntity.ok(this.userService.getAllUsers(PageRequest.of(pageNumber, pageCapacity)));
  }

  //Цветомир
  @PreAuthorize("hasRole('SUPPLIER')")
  @GetMapping("/user/role/name")
  public ResponseEntity<?> showAllFactors(@RequestParam(defaultValue = "0") @Min(value = 0) Integer pageNumber,
                                              @Min(value = 1) @Max(value = 100) @RequestParam(defaultValue = "100")
                                                  Integer pageCapacity)
  {
    return new ResponseEntity<>(userService.showAllFactors(PageRequest.of(pageNumber, pageCapacity)), HttpStatus.OK);
  }

  //Ивета
  @PreAuthorize("hasRole('SUPPLIER')")
  @PostMapping("/create/contract/{username}")
  public ResponseEntity<?> insertContract(@PathVariable String username)
  {
    userService.createContract(username);
    return ResponseEntity.ok(SUCCESSFUL_CONTRACT);
  }

  //Ивета
  @PreAuthorize("hasAnyRole('ADMIN', 'SUPPLIER', 'FACTOR')")
  @GetMapping("show/notifications")
  public ResponseEntity<?> showNotifications(@RequestParam(defaultValue = "0") @Min(value = 0) Integer pageNumber,
                                             @Min(value = 1) @Max(value = 100) @RequestParam(defaultValue = "100")
                                                 Integer pageCapacity)
  {
    return ResponseEntity.ok(this.notificationService.showNotification(PageRequest.of(pageNumber, pageCapacity)));
  }

  //Християн
  @PreAuthorize("hasRole('FACTOR')")
  @GetMapping("/files")
  public ResponseEntity<List<FileInfoDto>> getListFiles(@RequestParam(defaultValue = "0") @Min(value = 0) Integer pageNumber,
                                                        @Min(value = 1) @Max(value = 100) @RequestParam(defaultValue = "100")
                                                            Integer pageCapacity)
  {
    return ResponseEntity.status(HttpStatus.OK)
        .body(this.userService.findFileNamesOfFactorByEmail(PageRequest.of(pageNumber, pageCapacity)));
  }

  //Християн
  @PreAuthorize("hasRole('FACTOR')")
  @GetMapping("/files/{filename}")
  @ResponseBody
  public ResponseEntity<Resource> getFile(@PathVariable String filename)
  {
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
            this.invoiceService.getInvoiceFileNameById(filename).getFileName() + "\"").body(fileStorageService.load(filename));
  }

  //Ивета
  @PreAuthorize("hasRole('FACTOR')")
  @PutMapping("/update/money/amount/")
  public ResponseEntity<?> updateMoneyAmount(@RequestParam String username, @RequestParam BigDecimal newMoney)
  {
    userService.updateMoneyAmount(username, newMoney);
    return ResponseEntity.ok(SUCCESSFUL_MONEY_CHANGE);
  }

  //Ивета
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping(value = "/lock/account")
  public ResponseEntity<?> lockAccountOfUserByEmail(@RequestParam String email, @RequestParam boolean isAccountLocked)
  {
    return ResponseEntity.ok(this.userService.lockUserAccount(email, isAccountLocked));
  }

  //Ерик
  @PreAuthorize("hasRole('FACTOR')")
  @PutMapping("/approve/invoice")
  public ResponseEntity<?> approveInvoiceInDatabase(
      @RequestParam boolean approveOrDenyStatus, @RequestParam Long number)
  {
    return ResponseEntity.ok(this.userService.approveInvoiceInDatabase(approveOrDenyStatus, number));
  }

  //Ивета
  @PreAuthorize("hasAnyRole('ADMIN', 'SUPPLIER', 'FACTOR')")
  @PutMapping("/change/password/old/password/{oldPassword}/new/password/{newPassword}/repeat/{repeatNewPassword}")
  public ResponseEntity<?> changePassword(@PathVariable String oldPassword, @PathVariable String newPassword,
                                          @PathVariable String repeatNewPassword)
  {
    this.userService.changePassword(oldPassword, newPassword, repeatNewPassword);
    return ResponseEntity.ok(SUCCESSFUL_CHANGE_PASSWORD);
  }

  //Ерик
  @PreAuthorize("hasRole('FACTOR')")
  @GetMapping("/show/connected")
  public ResponseEntity<?> showConnectedUsers(@RequestParam(defaultValue = "0") @Min(value = 0) Integer pageNumber,
                                              @Min(value = 1) @Max(value = 100) @RequestParam(defaultValue = "100")
                                                  Integer pageCapacity)
  {
    return ResponseEntity.ok(this.userService.showConnectedUsers(PageRequest.of(pageNumber, pageCapacity)));
  }

}


