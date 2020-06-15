package org.circuitdoctor.core.service;


import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.model.Room;
import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.repository.RoomRepository;
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

public class RoomServiceImplTest {

    @Autowired
    private LocationService locationService;
    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;





    @Before
    public void setUp() throws Exception {
        initData();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addRoom() {
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
        Room expected=roomService.addRoom(room1);

        assertEquals(expected,room1);

        Room roomNotOk=Room.builder().location(location1).name("room@#$!#").build();
        room1.setId((long) 4);

        try {
            roomService.addRoom(roomNotOk);
            Assert.fail(); // raises AssertionException
        } catch (Exception ex) {
            // Catches the assertion exception, and the test passes
        }

    }

    @Test
    public void updateRoom() {
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
        roomService.addRoom(room1);

        Room newRoom=Room.builder().location(location1).name("kitchen").build();
        newRoom.setId((long) 3);


        assertEquals(newRoom,roomService.updateRoom(newRoom));
        assertEquals(roomService.updateRoom(newRoom).getName(),"kitchen");







    }

    private void initData(){



    }
}
