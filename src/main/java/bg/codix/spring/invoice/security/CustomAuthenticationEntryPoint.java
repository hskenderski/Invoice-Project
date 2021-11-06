package bg.codix.spring.invoice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static bg.codix.spring.invoice.common.ExceptionMessages.INVALID_EMAIL;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint
{
  @Override
  public void commence(HttpServletRequest httpServletRequest,
                       HttpServletResponse response,
                       AuthenticationException e) throws IOException
  {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setCharacterEncoding("utf-8");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ObjectMapper objectMapper = new ObjectMapper();
    String responseBody = objectMapper.writeValueAsString(INVALID_EMAIL);
    PrintWriter printWriter = response.getWriter();
    printWriter.print(responseBody);
    printWriter.flush();
    printWriter.close();
  }
}