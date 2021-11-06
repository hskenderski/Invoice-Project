package bg.codix.spring.invoice.dao.Invoice;

import bg.codix.spring.invoice.entities.Invoice;

import java.util.List;

public interface InvoiceDao
{
  Long createInvoiceInFile(Invoice invoice, String fileName);

  void insertIntoUserInvoices(Long userId, Long invoiceId);

  Long createInvoiceInDatabase(Invoice invoice);

  Invoice findInvoiceById(Long invoiceId);

  void changeInvoiceStatus(String status, Long invoiceId);

  Invoice getFileNameById(Long invoiceId);

  void removeUserInvoices(Long invoiceId);

  void removeInvoice(Long invoiceId);

  List<Invoice> allInvoicesOfUser(Long userId);

}
