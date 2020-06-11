package org.circuitdoctor.web.controller;

import org.circuitdoctor.core.model.Location;
import org.circuitdoctor.core.service.LocationService;
import org.circuitdoctor.web.converter.LocationConverter;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;

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
    @Test
    public void addLocation() {
    }

    @Test
    public void getLocations() {
    }
}