package bg.codix.spring.invoice.controllers;

import bg.codix.spring.invoice.dto.InvoiceRequest;
import bg.codix.spring.invoice.services.Invoice.InvoiceService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Validated
@RequestMapping("/api/v1")
public class InvoiceController
{
  private final InvoiceService invoiceService;

  public InvoiceController(InvoiceService invoiceService)
  {
    this.invoiceService = invoiceService;
  }

  //Християн
  @PreAuthorize("hasRole('SUPPLIER')")
  @PostMapping(value = "/create/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createInvoiceInFile(InvoiceRequest invoiceRequest, @RequestParam("file") MultipartFile file)
  {
    return ResponseEntity
        .ok(this.invoiceService.createInvoiceInFile(invoiceRequest, file));
  }

  //Ивета
  @PreAuthorize("hasRole('SUPPLIER')")
  @PostMapping("/create/invoice")
  public ResponseEntity<?> createInvoiceInDatabase(@RequestBody InvoiceRequest invoiceRequest)
  {
    return ResponseEntity
        .ok(this.invoiceService.createInvoiceInDatabase(invoiceRequest));
  }
}

