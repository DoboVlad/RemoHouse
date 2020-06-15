package org.circuitdoctor.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.service.UserService;
import org.circuitdoctor.web.converter.UserConverter;
import org.circuitdoctor.web.dto.LocationDto;
import org.circuitdoctor.web.dto.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.validation.BindException;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;
public class UserControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private UserConverter userConverter;

    private User user1;
    private User user2;
    private User user3;
    private UserDto userDto1;
    private UserDto userDto2;
    private UserDto userDto3;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
        initData();
    }

    private void initData() {
        user1 = User.builder()
                .email("email@email.com")
                .name("Name")
                .password("password")
                .phoneNumber("0792792792")
                .surname("Surname")
                .build();
        user1.setId(1L);
        user2 = User.builder()
                .surname("NewSurname")
                .phoneNumber("0789456123")
                .password("password")
                .name("NewName")
                .email("newemail@gmail.com")
                .build();
        user2.setId(2L);
        user3 = User.builder()
                .surname("NewSurname")
                .phoneNumber("0789456123")
                .password("changedpassword")
                .name("NewName")
                .email("newemail@gmail.com")
                .build();
        user3.setId(2L);
        userDto1 = createUserDto(user1);
        userDto2 = createUserDto(user2);
        userDto3 = createUserDto(user3);
    }

    private UserDto createUserDto(User user) {
        return UserDto.builder()
                .surname(user.getSurname())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }

    @Test
    public void login() throws Exception {
        when(userService.login(user1)).thenReturn(true);
        when(userConverter.convertModelToDto(user1)).thenReturn(userDto1);
        boolean r = userController.login(userDto1);
        assertEquals("should be false",r,false);
    }
    private String toJsonString(UserDto studentDto) {
        try {
            return new ObjectMapper().writeValueAsString(studentDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void signUp() throws Exception {
        when(userService.signUp(user2)).thenReturn(user2);
        when(userConverter.convertModelToDto(user2)).thenReturn(userDto2);
        Errors e = new BeanPropertyBindingResult(userDto1,"s");
        UserDto r = userController.signUp(userDto1,  e);
        assertEquals(r,userDto1);
    }

    @Test
    public void changePassword() throws Exception {
        when(userService.changePassword(user3)).thenReturn(user3);
        when(userConverter.convertModelToDto(user3)).thenReturn(userDto3);
        UserDto r = userController.changePassword(userDto3,new BeanPropertyBindingResult(userDto3,"s"));
        assertEquals(r,null);
    }

    private String toJsonString(LocationDto locationDto) {
        try {
            return new ObjectMapper().writeValueAsString(locationDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}