package org.circuitdoctor.core.service;


import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.circuitdoctor.core.model.*;
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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
public class GSMControllerServiceTest {




    @Autowired
    private GSMControllerService gsmControllerService;
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setGSMControllerON() {
        User user11 = User.builder()
                .name("aaaaazz").surname("aaaaaazz").password("passwd101")
                .phoneNumber("1011102120").email("zzzz@gmail.com").build();
        user11.setId((long) 1);

        Location location1=Location.builder()
                .latitude("(123.12,349.23)").longitude("(234.34,569.23)").image("img1")
                .name("nume1").user(user11).build();
        location1.setId((long) 2);

        Room room1=Room.builder().location(location1).name("room2").build();
        room1.setId((long) 3);

        GSMController gsmCtrl1=GSMController.builder()
                .phoneNumber("1111111111")
                .room(room1)
                .build();
        gsmCtrl1.setId((long) 4);
        assertEquals(gsmCtrl1.getStatus(), GSMStatus.OFF);
        Long expectedID=4L;
        assertEquals(gsmCtrl1.getId(),expectedID);
        gsmControllerService.addGSMController(gsmCtrl1);
        GSMController gsmNew=gsmControllerService.setGSMControllerON(gsmCtrl1);
        assertEquals(GSMStatus.ON,gsmNew.getStatus());
    }

    @Test
    public void setGSMControllerOFF() {
        User user11 = User.builder()
                .name("aaaaazz").surname("aaaaaazz").password("passwd101")
                .phoneNumber("1011102120").email("zzzz@gmail.com").build();
        user11.setId((long) 1);

        Location location1=Location.builder()
                .latitude("(123.12,349.23)").longitude("(234.34,569.23)").image("img1")
                .name("nume1").user(user11).build();
        location1.setId((long) 2);

        Room room1=Room.builder().location(location1).name("room2").build();
        room1.setId((long) 3);

        GSMController gsmCtrl1=GSMController.builder()
                .phoneNumber("1111111111")
                .room(room1)
                .status(GSMStatus.ON)
                .build();
        gsmCtrl1.setId((long) 4);
        assertEquals(gsmCtrl1.getStatus(), GSMStatus.ON);
        Long expectedID=4L;
        assertEquals(gsmCtrl1.getId(),expectedID);
        gsmControllerService.addGSMController(gsmCtrl1);
        GSMController gsmNew=gsmControllerService.setGSMControllerOFF(gsmCtrl1);
        assertEquals(GSMStatus.OFF,gsmNew.getStatus());
    }

    @Test
    public void addGSMController() {
        User user11 = User.builder()
                .name("aaaaazz").surname("aaaaaazz").password("passwd101")
                .phoneNumber("1011102120").email("zzzz@gmail.com").build();
        user11.setId((long) 1);

        Location location1=Location.builder()
                .latitude("(123.12,349.23)").longitude("(234.34,569.23)").image("img1")
                .name("nume1").user(user11).build();
        location1.setId((long) 2);

        Room room1=Room.builder().location(location1).name("room2").build();
        room1.setId((long) 3);

        GSMController gsmCtrl1=GSMController.builder()
                .phoneNumber("1111111111")
                .room(room1)
                .build();
        gsmCtrl1.setId((long) 4);
        GSMController expected=gsmControllerService.addGSMController(gsmCtrl1);
        assertEquals(expected,gsmCtrl1);
        GSMController gsmCtrl2=GSMController.builder()
                .phoneNumber("1111111")
                .room(room1)
                .build();
        gsmCtrl1.setId((long) 5);
        try {
            gsmControllerService.addGSMController(gsmCtrl2);
            Assert.fail(); // raises AssertionException
        } catch (Exception ex) {
            // Catches the assertion exception, and the test passes
        }


    }
}
