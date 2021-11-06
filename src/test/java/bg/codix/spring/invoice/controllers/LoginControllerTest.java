package bg.codix.spring.invoice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners({WithSecurityContextTestExecutionListener.class})
public class LoginControllerTest extends AbstractTestNGSpringContextTests
{
  @Autowired
  public MockMvc mockMvc;


  @WithMockUser(roles = {"ADMIN", "SUPPLIER", "FACTOR"})
  @Test
  public void login_successful() throws Exception
  {
    mockMvc.perform(post("/api/v1/login")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(String.valueOf(status().isOk())));
  }

  @WithMockUser(roles = {"ADMIN", "SUPPLIER", "FACTOR"})
  @Test
  public void login_status_method_not_allowed() throws Exception
  {
    mockMvc.perform(post("/api/v1/login"))
        .andDo(print())
        .andExpect(status().isMethodNotAllowed());
  }


  @WithMockUser(roles = {"ADMIN", "SUPPLIER", "FACTOR"})
  @Test
  public void login_status_bad_request() throws Exception
  {
    mockMvc.perform(multipart("/api/v1/login")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(String.valueOf(status().isBadRequest())));
  }

  @WithUserDetails("h.skenderski@abv.bg")
  @Test
  public void login_successfully() throws Exception
  {
    mockMvc.perform(get("/api/v1/login"))
        .andExpect(status().isOk());
  }


}