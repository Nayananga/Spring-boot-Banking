package com.example.paul.unit;

import com.example.paul.constants.constants;
import com.example.paul.controllers.AccountRestController;
import com.example.paul.models.Account;
import com.example.paul.services.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountRestController.class)
class AccountRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountService;

    @Test
    void givenMissingInput_whenCheckingBalance_thenVerifyBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenInvalidInput_whenCheckingBalance_thenVerifyBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                .content("{\"sortCode\": \"53-68\",\"accountNumber\": \"78934\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenNoAccountForInput_whenCheckingBalance_thenVerifyNoContent() throws Exception {
        given(accountService.getAccount(null, null)).willReturn(null);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                .content("{\"sortCode\": \"53-68-92\",\"accountNumber\": \"78901234\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(constants.NO_ACCOUNT_FOUND))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenAccountDetails_whenCheckingBalance_thenVerifyOk() throws Exception {
        Account account = new Account("Some Bank", "John", "53-68-92", "78901234", 10.1);
        given(accountService.getAccount("53-68-92", "78901234")).willReturn(account);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                .content("{\"sortCode\": \"53-68-92\",\"accountNumber\": \"78901234\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName").value(account.getBankName()))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenMissingInput_whenCreatingAccount_thenVerifyBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenInvalidInput_whenCreatingAccount_thenVerifyBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts")
                        .content("{\"sortCode\": \"53-68\",\"accountNumber\": \"78934\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenNoAccountForInput_whenCreatingAccount_thenVerifyFailure() throws Exception {
        given(accountService.createAccount("Some Bank", "John")).willReturn(null);

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts")
                        .content("{\"bankName\": \"Some Bank\",\"ownerName\": \"John\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(constants.CREATE_ACCOUNT_FAILED))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenAccountDetails_whenCreatingAccount_thenVerifyOk() throws Exception {
        Account account = new Account("Some Bank", "John", "53-68-92", "78901234", 10.1);
        given(accountService.createAccount("Some Bank", "John")).willReturn(account);

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts")
                        .content("{\"bankName\": \"Some Bank\",\"ownerName\": \"John\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sortCode").value(account.getSortCode()))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
