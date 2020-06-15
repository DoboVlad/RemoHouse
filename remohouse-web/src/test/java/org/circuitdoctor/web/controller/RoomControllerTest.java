package org.circuitdoctor.web.controller;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.model.Room;
import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.service.LocationService;
import org.circuitdoctor.core.service.RoomService;
import org.circuitdoctor.core.service.UserService;
import org.circuitdoctor.web.converter.LocationConverter;
import org.circuitdoctor.web.converter.RoomConverter;
import org.circuitdoctor.web.dto.LocationDto;
import org.circuitdoctor.web.dto.RoomDto;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RoomControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private RoomController roomController;
    @Mock
    private RoomService roomService;
    @Mock
    private RoomConverter roomConverter;
    @Mock
    private LocationService locationService;
    @Mock
    private UserService userService;

    private Room room1;
    private Room room2;
    private Location location;
    private User user;
    private RoomDto roomDto1;
    private RoomDto roomDto2;
    private Validator validator;
    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(roomController)
                .build();
        initData();
        validator  = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory().getValidator();
    }

    private void initData() {
        user = User.builder()
                .email("email@gmail.com")
                .name("Pop")
                .password("password34566")
                .phoneNumber("0123456789")
                .surname("Ionica")
                .build();
        user.setId(1L);
        location = Location.builder()
                .image("image.pdf")
                .latitude("1.2")
                .longitude("8.6")
                .name("my house")
                .user(user)
                .build();
        location.setId(2L);
        room1 = Room.builder()
                .name("bedroom")
                .location(location)
                .build();
        room1.setId(4L);
        room2 = Room.builder()
                .name("kitchen")
                .location(location)
                .build();
        room2.setId(4L);
        roomDto1 = createRoomDto(room1);
        roomDto2 = createRoomDto(room2);
    }

    private RoomDto createRoomDto(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .name(room.getName())
                .locationID(room.getLocation().getId())
                .build();
    }

    @Test
    public void addRoom() {
        when(roomService.addRoom(room1)).thenReturn(room1);
        when(roomConverter.convertModelToDto(room1)).thenReturn(roomDto1);
        String r = roomController.addRoom(roomDto1,5L,new BeanPropertyBindingResult(roomDto1,"s"));
        assertEquals(r,"user has no access");
    }

    @Test
    public void updateRoom() {
        when(roomService.updateRoom(room2)).thenReturn(room2);
        when(roomConverter.convertModelToDto(room2)).thenReturn(roomDto2);
        String r = roomController.updateRoom(roomDto2,5L,new BeanPropertyBindingResult(roomDto2,"s"));
        assertEquals(r,"user has no access");
    }

    @Test
    public void checkValidations(){
        RoomDto room = RoomDto.builder()
                .id(0L)
                .locationID(1L)
                .name("")
                .build();
        Set<ConstraintViolation<RoomDto>> violations = validator.validate(room);
        assertFalse("no empty fields",violations.isEmpty());

        room = RoomDto.builder()
                .id(0L)
                .locationID(1L)
                .name("a")
                .build();
        violations = validator.validate(room);
        assertFalse("invalid name size",violations.isEmpty());

    }
}