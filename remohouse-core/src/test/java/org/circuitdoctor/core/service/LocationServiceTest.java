package org.circuitdoctor.core.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.model.User;
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

import javax.validation.constraints.AssertTrue;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/db-dataLocation.xml")
public class LocationServiceTest {

    @Autowired
    private LocationService locationServie;
    @Autowired
    private UserService userService;




    @Before
    public void setUp() throws Exception {
        //initData();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAllLocations() {
        User user20 = User.builder()
                .name("bbbbbbbb").surname("bbbbbb").password("passwd200")
                .phoneNumber("2020202020").email("bbbbb@gmail.com").build();
        user20.setId((long) 20);
        User user10 = User.builder()
                .name("aaaaa").surname("aaaaaa").password("passwd100")
                .phoneNumber("1010102120").email("aaaaa@gmail.com").build();
        user10.setId((long) 10);
        Location location1=Location.builder()
                .latitude("(123,345)").longitude("(234,567)").image("img1")
                .name("nume1").user(user10).build();
        location1.setId((long) 1);
        Location location2=Location.builder()
                .latitude("(123.23,345.43)").longitude("(123.12,4566.12)").image("img2")
                .name("nume2").user(user10).build();
        location2.setId((long) 2);
        Location location3=Location.builder()
                .latitude("(123.34,3453.12)").longitude("(114323.32,789.12)").image("img3")
                .name("nume3").user(user20).build();
        location3.setId((long) 3);
        locationServie.addLocation(location1);
        locationServie.addLocation(location2);
        locationServie.addLocation(location3);
        Set<Location> locations1=locationServie.getAllLocations(user10.getId());
        Set<Location> locations2=locationServie.getAllLocations(user20.getId());

        assertEquals(locations2.size(),1);


    }

    @Test
    public void addLocation() {

        //User user10=userService.getAllUsers().get(0);
        //user10.setId((long) 15);


        User user20 = User.builder()
                .name("bbbbbbbb").surname("bbbbbb").password("passwd200")
                .phoneNumber("2020202020").email("bbbbb@gmail.com").build();
        user20.setId((long) 20);
        Location location1=Location.builder()
                .latitude("(123.21,345.43)").longitude("(234.21,567.45)").image("img1")
                .name("nume1").user(user20).build();
        location1.setId((long) 1);
        Location location2=Location.builder()
                .latitude("(123.21,345.56)").latitude("").image("img1")
                .name("nume1").user(user20).build();
        location2.setId((long) 2);
        Location location3=Location.builder()
                .latitude("(123.76,345.76)").latitude("(123.34,789.23)").image("img1")
                .name("n").user(user20).build();
        location3.setId((long) 3);

        Location expected=locationServie.addLocation(location1);
        assertEquals(location1,expected);
        assertEquals(location1.getId(),expected.getId());
        try {
            locationServie.addLocation(location2);
            Assert.fail(); // raises AssertionException
        } catch (Exception ex) {
            // Catches the assertion exception, and the test passes
        }
        try {
            locationServie.addLocation(location3);
            Assert.fail(); // raises AssertionException
        } catch (Exception ex) {
            // Catches the assertion exception, and the test passes
        }

        Location location4=Location.builder()
                .latitude("(124.21)").longitude("(234.24,564.35)").image("img1")
                .name("nume4").user(user20).build();
        location1.setId((long) 4);
        try {
            locationServie.addLocation(location4);
            Assert.fail(); // raises AssertionException
        } catch (Exception ex) {
            // Catches the assertion exception, and the test passes
        }





    }

    @Test
    public void checkAccessLocation(){
        User user20 = User.builder()
                .name("bbbbbbbb").surname("bbbbbb").password("passwd200")
                .phoneNumber("2020202020").email("bbbbb@gmail.com").build();
        user20.setId((long) 20);
        User user10 = User.builder()
                .name("aaaaa").surname("aaaaaa").password("passwd100")
                .phoneNumber("1010102120").email("aaaaa@gmail.com").build();
        user10.setId((long) 10);
        Location location1=Location.builder()
                .latitude("(123,345)").longitude("(234,567)").image("img1")
                .name("nume1").user(user10).build();
        location1.setId((long) 1);
        Location location2=Location.builder()
                .latitude("(123.23,345.43)").longitude("(123.12,4566.12)").image("img2")
                .name("nume2").user(user10).build();
        location2.setId((long) 2);
        locationServie.addLocation(location1);
        locationServie.addLocation(location2);


        assertTrue(locationServie.checkAccessLocation(user10.getId(),location1.getId()));
        assertFalse(locationServie.checkAccessLocation(user20.getId(),location1.getId()));

    }

    private void initData(){







    }
}
