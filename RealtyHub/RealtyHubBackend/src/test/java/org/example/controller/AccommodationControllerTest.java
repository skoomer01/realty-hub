package org.example.controller;

import org.example.business.impl.AccommodationManager;
import org.example.domain.classes.Accommodation;
import org.example.domain.classes.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
public class AccommodationControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AccommodationManager accommodationManager;
//
//    @Test
//    void getAccommodation_shouldReturn200WithAccommodation_whenAccommodationFound() throws Exception {
////        Address expectedAddress = Address.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken").build();
////        Accommodation accommodation = Accommodation.builder().id(1L).name("Apartment").startingDate("10-08-2000").price(625.00).image("image").area(23).floors(1).interior("furnished").rooms(1).address(expectedAddress).build();
////        when(accommodationManager.getAccommodation(1L)).thenReturn(Optional.of(accommodation));
////
////        verify(accommodationManager).getAccommodation(1L);
//    }

}
