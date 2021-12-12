package com.itechart.finnhubapi.controller;

import com.itechart.finnhubapi.dto.CompanyDto;
import com.itechart.finnhubapi.dto.CompanyDtoRequest;
import com.itechart.finnhubapi.dto.UserDtoResponse;
import com.itechart.finnhubapi.mapper.CompanyMapper;
import com.itechart.finnhubapi.mapper.UserMapper;
import com.itechart.finnhubapi.model.entity.RoleEntity;
import com.itechart.finnhubapi.model.entity.SubscriptionEntity;
import com.itechart.finnhubapi.model.entity.UserEntity;
import com.itechart.finnhubapi.service.UserService;
import com.itechart.finnhubapi.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.itechart.finnhubapi.controller.MainControllerTest.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(
        MockitoExtension.class
)
class UserRestControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private UserUtil userUtil;
    @InjectMocks
    private UserRestController userRestController;

    protected MockMvc mvc;
    private final UserEntity user = new UserEntity();
    private final SubscriptionEntity subscription = new SubscriptionEntity();
    private final RoleEntity role = new RoleEntity();
    private final CompanyDto company = new CompanyDto();

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.standaloneSetup(userRestController).build();
        user.setId(3L);
        user.setEmail("test@gmail.com");
        user.setUsername("testUser");
        user.setPassword("test");
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        user.setStatus("ACTIVE");
        user.setFirstName("TestFirst");
        user.setLastName("TestLast");
        subscription.setStartTime(LocalDateTime.now());
        subscription.setFinishTime(LocalDateTime.now().plusYears(3));
        user.setSubscription(subscription);
        role.setName("ROLE_USER");
        List<RoleEntity> listRole = new ArrayList<>();
        listRole.add(role);
        user.setRoles(listRole);
        company.setId(4L);
        company.setSymbol("WDGJF");
        company.setMic("OOTC");
        company.setType("Common Stock");
        company.setFigi("BBG000BJL537");
        company.setCurrency("USD");
        company.setDescription("JOHN WOOD GROUP PLC");
        company.setDisplaySymbol("WDGJF");
    }

    @Test
    void getAllUsers() throws Exception {
        List<UserEntity> userList = new ArrayList<>();
        userList.add(user);
        doReturn(userList).when(userService).findAll();
        MvcResult result = mvc.perform(get("/api/v1/user/getAllUsers"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(userService, times(1)).findAll();
    }

    @Test
    void getOneUser() throws Exception {
        UserDtoResponse userdto = UserMapper.INSTANCE.userToUserDtoResponse(user);
        doReturn(userdto).when(userService).findById(anyLong());
        MvcResult result = mvc.perform(get("/api/v1/user/getOneUser/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(userService, times(1)).findById(anyLong());
    }

    @Test
    void testGetOneUser() throws Exception {
        doReturn(user.getId()).when(userUtil).userID(any());
        doReturn(UserMapper.INSTANCE.userToUserDtoResponse(user)).when(userService).findById(anyLong());
        MvcResult result = mvc.perform(get("/api/v1/user/getOneUser"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(userUtil, times(1)).userID(any());
        verify(userService, times(1)).findById(anyLong());
    }

    @Test
    void deleteOneCompanyFromUser() throws Exception {
        List<CompanyDto> companyDtos = new ArrayList<>();
        companyDtos.add(company);
        CompanyDtoRequest companyDtoRequest = CompanyMapper.INSTANCE.companyToCompanyDtoRequest(CompanyMapper.INSTANCE.companyDtoToCompanyEntity(company));
        doReturn(user.getId()).when(userUtil).userID(any());
        doReturn(companyDtos).when(userService).deleteOneCompanyFromUser(anyLong(), anyLong());
        MvcResult result = mvc.perform(post("/api/v1/user/deleteOneCompanyFromUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(companyDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(userService, times(1)).deleteOneCompanyFromUser(anyLong(), anyLong());
        verify(userUtil, times(1)).userID(any());
    }
}