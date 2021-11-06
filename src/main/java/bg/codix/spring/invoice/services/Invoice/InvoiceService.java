package bg.codix.spring.invoice.services.Invoice;

import bg.codix.spring.invoice.dto.InvoiceRequest;
import bg.codix.spring.invoice.entities.Invoice;
import org.springframework.web.multipart.MultipartFile;

public interface InvoiceService
{
  //Християн
  String createInvoiceInFile(InvoiceRequest invoiceRequest, MultipartFile file);

  //Ивета
  String createInvoiceInDatabase(InvoiceRequest invoiceRequest);

  //Християн
  Invoice getInvoiceFileNameById(String invoiceId);

}
