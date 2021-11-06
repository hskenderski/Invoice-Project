package bg.codix.spring.invoice.services.FileStorage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService
{
  //Християн
  void init();

  //Християн
  void save(MultipartFile file, Long invoiceId);

  //Християн
  Resource load(String filename);

}
