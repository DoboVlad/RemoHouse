package org.circuitdoctor.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.model.User;
import org.circuitdoctor.core.service.LocationService;
import org.circuitdoctor.web.converter.LocationConverter;
import org.circuitdoctor.web.dto.LocationDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import org.springframework.http.MediaType;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
public class LocationControllerTest {

    private MockMvc mockMvc;
    @InjectMocks
    private LocationController locationController;
    @Mock
    private LocationService locationService;
    @Mock
    private LocationConverter locationConverter;

    private Location location1;
    private Location location2;
    private User user1;
    private LocationDto locationDto1;
    private LocationDto locationDto2;

    @Before
    public void setup() throws Exception {
        initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(locationController)
                .build();
        initData();
    }

    private void initData() {
        user1 = User.builder()
                .surname("Marcel")
                .phoneNumber("0783075908")
                .password("strongpassword")
                .name("Ion")
                .email("ionmarcel@gmail.com")
                .build();
        user1.setId(1L);
        location1 = Location.builder()
                .user(user1)
                .name("apartment1")
                .longitude("(1.2,2.3)")
                .latitude("(2.3,3.4)")
                .image("image.jpeg")
                .build();
        location1.setId(1L);

        location2 = Location.builder()
                .user(user1)
                .name("apartment12")
                .longitude("(1.3,2.3)")
                .latitude("(2.4,3.4)")
                .image("image2.jpeg")
                .build();
        location2.setId(2L);

        locationDto1 = createLocationDto(location1);
        locationDto2 = createLocationDto(location2);
    }

    private LocationDto createLocationDto(Location location) {
        return LocationDto.builder()
                .userID(location.getUser().getId())
                .name(location.getName())
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .image(location.getImage())
                .id(location.getId())
                .build();
    }

    @Test
    public void addLocation() throws Exception {
        when(locationService.addLocation(location1)).thenReturn(location1);
        when(locationConverter.convertModelToDto(location1)).thenReturn(locationDto1);
        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("location/addLocation")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJsonString(locationDto1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id",is(2)));
        verify(locationService, times(1)).addLocation(location1);
        verify(locationConverter, times(1)).convertModelToDto(location1);
        verifyNoMoreInteractions(locationService, locationConverter);
    }

    private String toJsonString(LocationDto locationDto) {
        try {
            return new ObjectMapper().writeValueAsString(locationDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void getLocations() throws Exception {
        Set<Location> locationSet = new HashSet<>(Arrays.asList(location1,location2));
        Set<LocationDto> locationDtos = new HashSet<>(Arrays.asList(locationDto1,locationDto2));
        Long userID = user1.getId();
        when(locationService.getAllLocations(userID)).thenReturn(locationSet);
        when(locationConverter.convertModelsToDtos(locationSet)).thenReturn(locationDtos);

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/location/getLocations/{userID}",userID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].name",anyOf(is("apartment1"),is("apartment12"))))
                .andExpect(jsonPath("$[0].id",anyOf(is(1),is(2))));
        String result = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(result);
        verify(locationService, times(1)).getAllLocations(userID);
        verify(locationConverter,times(1)).convertModelsToDtos(locationSet);

    }
}