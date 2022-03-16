package de.zwickau.vub.vending.api;

import de.zwickau.vub.vending.model.Role;
import de.zwickau.vub.vending.model.User;
import de.zwickau.vub.vending.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;



    @Test
    public void depositOk() throws Exception {
        var mockUser = newBuyer();
        when(userService.findUserByUsername("murat")).thenReturn(mockUser);
        this.mockMvc.perform(post("/users/murat/deposit/50")).andDo(print()).andExpect(status().isOk());
    }
    @Test
    void depositWrongAmount() throws Exception {
        var mockUser = newBuyer();
        when(userService.findUserByUsername("murat")).thenReturn(mockUser);
        this.mockMvc.perform(post("/users/murat/deposit/33")).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    void resetDepositOk() throws Exception {
        var mockUser = newBuyer();
        when(userService.findUserByUsername("murat")).thenReturn(mockUser);
        this.mockMvc.perform(post("/users/murat/reset")).andDo(print()).andExpect(status().isOk());
    }

    private User newBuyer() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setDeposit(0);
        mockUser.setUsername("murat");
        mockUser.setRole(Role.BUYER);
        return mockUser;
    }
}