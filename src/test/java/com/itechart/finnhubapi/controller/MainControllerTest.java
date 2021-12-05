package com.itechart.finnhubapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.finnhubapi.dto.MonthDto;
import com.itechart.finnhubapi.dto.SubscriptionIdDto;
import com.itechart.finnhubapi.dto.UserDto;
import com.itechart.finnhubapi.dto.UserDtoResponse;
import com.itechart.finnhubapi.dto.UserUpdateDto;
import com.itechart.finnhubapi.mapper.UserMapper;
import com.itechart.finnhubapi.model.entity.RoleEntity;
import com.itechart.finnhubapi.model.entity.SubscriptionEntity;
import com.itechart.finnhubapi.model.entity.UserEntity;
import com.itechart.finnhubapi.service.UserService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(
        MockitoExtension.class
)
class MainControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private MainController mainController;

    protected MockMvc mvc;
    private final UserEntity user = new UserEntity();
    private final SubscriptionEntity subscription = new SubscriptionEntity();
    private final RoleEntity role = new RoleEntity();

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.standaloneSetup(mainController).build();
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
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registration() throws Exception {
        doReturn(UserMapper.INSTANCE.userToUserDtoResponse(user)).when(userService).saveUser(any(UserDto.class));
        UserDto userDto = UserMapper.INSTANCE.userToUserDto(user);
        MvcResult result = mvc.perform(post("/api/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(userService, times(1)).saveUser(any(UserDto.class));
    }

    @Test
    void updateUser() throws Exception {
        doReturn(UserMapper.INSTANCE.userToUserDtoResponse(user)).when(userService).updateUser(any(UserUpdateDto.class));
        UserDto userDto = UserMapper.INSTANCE.userToUserDto(user);
        MvcResult result = mvc.perform(post("/api/v1/updateUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(userService, times(1)).updateUser(any(UserUpdateDto.class));
    }

    @Test
    void locking() throws Exception {
        UserDtoResponse userdto = UserMapper.INSTANCE.userToUserDtoResponse(user);
        doReturn(userdto).when(userService).lockOrUnlock(anyLong(), anyString());
        MvcResult result = mvc.perform(post("/api/v1/lockingUser/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(userService, times(1)).lockOrUnlock(anyLong(), anyString());
    }

    @Test
    void unlocking() throws Exception {
        UserDtoResponse userdto = UserMapper.INSTANCE.userToUserDtoResponse(user);
        doReturn(userdto).when(userService).lockOrUnlock(anyLong(), anyString());
        MvcResult result = mvc.perform(post("/api/v1/unlockingUser/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(userService, times(1)).lockOrUnlock(anyLong(), anyString());
    }

    @Test
    void delete() throws Exception {
        doNothing().when(userService).deleteById(anyLong());
        MvcResult result = mvc.perform(post("/api/v1/deleteUser/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(userService, times(1)).deleteById(anyLong());
    }

    @Test
    void changeSubscription() throws Exception {
        SubscriptionIdDto subscriptionNameDto = new SubscriptionIdDto();
        subscriptionNameDto.setId(2L);
        UserDtoResponse userdto = UserMapper.INSTANCE.userToUserDtoResponse(user);
        doReturn(userdto).when(userService).changeSubscription(anyLong());
        MvcResult result = mvc.perform(post("/api/v1/changeSubscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(subscriptionNameDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(userService, times(1)).changeSubscription(anyLong());
    }

    @Test
    void renewSubscription() throws Exception {
        MonthDto month = new MonthDto();
        month.setMonth(3L);
        UserDtoResponse userdto = UserMapper.INSTANCE.userToUserDtoResponse(user);
        doReturn(userdto).when(userService).renewSubscription(anyLong());
        MvcResult result = mvc.perform(post("/api/v1/renewSubscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(month)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        verify(userService, times(1)).renewSubscription(anyLong());
    }
}