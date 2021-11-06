package bg.codix.spring.invoice;

import bg.codix.spring.invoice.services.FileStorage.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class InvoiceApplication implements CommandLineRunner
{

  //This is annotation like @Autowired but in Java
  @Resource
  FileStorageService storageService;

  public static void main(String[] args)
  {
    SpringApplication.run(InvoiceApplication.class, args);
  }

  @Override
  public void run(String... args)
  {
    storageService.init();
  }
}