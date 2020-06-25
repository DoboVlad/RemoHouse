package org.circuitdoctor.core.service;


import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.service.ITConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/db-dataUser.xml")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAllUsers() {
        List<User> users=userService.getAllUsers();
        assertEquals("there should be 3 users",3,users.size());
    }

    @Test
    public void login() {
        User userOk=userService.getAllUsers().get(0);
        userOk.setId((long) 1);
        User userNotOk = User.builder()
                .email("email")
                .name("nume")
                .password("passwd")
                .phoneNumber("afds")
                .surname("prenume")
                .build();
        userNotOk.setId((long) 7);

        assertTrue(userService.login(userOk));

        try {
            userService.login(userNotOk);
            Assert.fail(); // raises AssertionException
        } catch (Exception ex) {
            // Catches the assertion exception, and the test passes
        }



    }

    @Test
    public void signUp() {
        User userOk = User.builder()

                .name("ajdbsbadj")
                .surname("ajdvsnhv")
                .password("passwd123")
                .phoneNumber("1111111111")

                .email("addshvh@gamil.com")
                .build();
        userOk.setId((long) 2);

        User userEmailNotOk = User.builder()
                .email("@gamil.com")
                .name("nume")
                .password("passwd123")
                .phoneNumber("2222222222")
                .surname("prenume")
                .build();
        User userPhoneNoNotOk = User.builder()
                .email("email@gamil.com")
                .name("nume")
                .password("passwd123")
                .phoneNumber("456agss")
                .surname("prenume")
                .build();
        User userPasswdNotOk = User.builder()
                .email("email@gamil.com")
                .name("nume")
                .password("passwd")
                .phoneNumber("333333333")
                .surname("prenume")
                .build();



        try {
            userService.signUp(userEmailNotOk);
            Assert.fail(); // raises AssertionException
        } catch (Exception ex) {
            // Catches the assertion exception, and the test passes
        }
        try {
            userService.signUp(userPhoneNoNotOk);
            Assert.fail(); // raises AssertionException
        } catch (Exception ex) {
            // Catches the assertion exception, and the test passes
        }
        try {
            userService.signUp(userPasswdNotOk);
            Assert.fail(); // raises AssertionException
        } catch (Exception ex) {
            // Catches the assertion exception, and the test passes
        }





        assertEquals(userOk,userService.signUp(userOk));
    }

    @Test
    public void changePassword() {
        User userOk=userService.getAllUsers().get(0);
        userOk.setPassword("newPassword");
        User newPassUser=userService.changePassword(userOk);
        assertEquals(newPassUser.getPassword(),"newPassword");
    }
}
