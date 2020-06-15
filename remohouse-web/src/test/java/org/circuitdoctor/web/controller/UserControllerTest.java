package org.circuitdoctor.web.controller;
import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.service.UserService;
import org.circuitdoctor.web.converter.UserConverter;
import org.circuitdoctor.web.dto.UserDto;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
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
    private Validator validator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
        initData();
        validator  =Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory().getValidator();
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
        assertFalse("should be false", r);
    }
    @Test
    public void validationErrors(){
        UserDto userNotOk = UserDto.builder()
                .id(23L)
                .email("email")
                .name("nume")
                .password("passwffsfsefefed")
                .phoneNumber("0123456789")
                .surname("prenume")
                .build();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userNotOk);
        assertFalse("email is worng",violations.isEmpty());

        userNotOk = UserDto.builder()
                .id(23L)
                .email("email@email.com")
                .name("nume123")
                .password("pasehreshseswd")
                .phoneNumber("0123456789")
                .surname("prenume")
                .build();
        violations = validator.validate(userNotOk);
        assertFalse("name is worng",violations.isEmpty());

        userNotOk = UserDto.builder()
                .id(23L)
                .email("email@email.com")
                .name("nume")
                .password("pd")
                .phoneNumber("0123456789")
                .surname("prenume")
                .build();
        violations = validator.validate(userNotOk);
        assertFalse("password is worng",violations.isEmpty());

        userNotOk = UserDto.builder()
                .id(23L)
                .email("email@email.com")
                .name("nume")
                .password("passrhsr5hs5hsswd")
                .phoneNumber("0123456789")
                .surname("prenume123")
                .build();
        violations = validator.validate(userNotOk);
        assertFalse("surname is worng",violations.isEmpty());

        userNotOk = UserDto.builder()
                .id(23L)
                .email("email@email.com")
                .name("nume123")
                .password("passsrththswd")
                .phoneNumber("afds")
                .surname("prenume")
                .build();
        violations = validator.validate(userNotOk);
        assertFalse("phonenumber is worng",violations.isEmpty());
    }
    @Test
    public void signUp() throws Exception {
        when(userService.signUp(user2)).thenReturn(user2);
        when(userConverter.convertModelToDto(user2)).thenReturn(userDto2);
        BindingResult e = new BeanPropertyBindingResult(userDto1,"s");
        String r = userController.signUp(userDto2,  e);
        assertEquals(r,String.valueOf(user2.getId()));

    }

    @Test
    public void changePassword() throws Exception {
        when(userService.changePassword(user3)).thenReturn(user3);
        when(userConverter.convertModelToDto(user3)).thenReturn(userDto3);
        String r = userController.changePassword(userDto3,userDto3.getId(),new BeanPropertyBindingResult(userDto3,"s"));
        assertEquals(r,"null");
    }
}