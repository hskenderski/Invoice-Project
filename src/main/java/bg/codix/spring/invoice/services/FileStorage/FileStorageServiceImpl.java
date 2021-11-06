package bg.codix.spring.invoice.services.FileStorage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static bg.codix.spring.invoice.common.ExceptionMessages.*;

@Service
public class FileStorageServiceImpl implements FileStorageService
{

  private final Path root = Paths.get("uploads");

  @Override
  public void init()
  {
    if (!Files.exists(root)) {
      try {
        Files.createDirectory(root);
      }
      catch (IOException e) {
        throw new RuntimeException(FAIL_INITIALIZE_FOLDER);
      }
    }
  }

  @Override
  public void save(MultipartFile file, Long invoiceId)
  {
    try {
      Files.copy(file.getInputStream(), this.root.resolve(String.valueOf(invoiceId)));
    }
    catch (Exception e) {
      throw new RuntimeException(FAIL_FILE_STORE + e.getMessage());
    }
  }

  @Override
  public Resource load(String filename)
  {
    try {
      Path file = root.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      }
      else {
        throw new RuntimeException(READ_FILE_EX);
      }
    }
    catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

}