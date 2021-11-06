package bg.codix.spring.invoice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Validated
@RequestMapping("/api/v1")
public class LoginController
{

  //Ивета
  @PreAuthorize("hasAnyRole('ADMIN', 'SUPPLIER', 'FACTOR')")
  @GetMapping("/login")
  public ResponseEntity<?> login()
  {
    return ResponseEntity.ok("Successfully login!");
  }

}
