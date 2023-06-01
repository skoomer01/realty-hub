import org.example.business.exception.InvalidAccommodationException;
import org.example.business.impl.AccommodationIdValidator;
import org.example.business.impl.AccommodationManager;
import org.example.domain.*;
import org.example.domain.classes.AccessToken;
import org.example.domain.classes.Accommodation;
import org.example.domain.classes.Address;
import org.example.persistance.AccommodationRepository;
import org.example.persistance.AddressRepository;
import org.example.persistance.UserAccommodationRepository;
import org.example.persistance.UserRepository;
import org.example.persistance.entity.AccommodationEntity;
import org.example.persistance.entity.AddressEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccommodationManagerTest {
    @Mock
    private AccommodationRepository accommodationRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    AccommodationIdValidator accommodationIdValidator;
    @Mock
    AccessToken accessToken;
    @Mock
    UserRepository userRepository;
    @Mock
    UserAccommodationRepository userAccommodationRepository;
    @InjectMocks
    AccommodationManager accommodationManager;



    @Test
    void shouldSaveAccommodationWithAllFields() {
        //arrange
        final CreateAccommodationRequest request = CreateAccommodationRequest.builder().name("Apartment").startingDate("10-08-2000").price(625).image("image").area(23).floors(1).interior("furnished").rooms(1).houseNumber(10).postcode("ABCD12").streetName("Hemelrijken").build();

        final AddressEntity addressEntity = AddressEntity.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken").build();
        final AccommodationEntity savedAccommodation = AccommodationEntity.builder().id(1L).name("Apartment").startingDate("10-08-2000").price(625.00).image("image").area(23).floors(1).interior("furnished").rooms(1).address(addressEntity).build();

        when(accommodationRepository.save(any(AccommodationEntity.class))).thenReturn(savedAccommodation);

        final AccommodationEntity accommodationEntityReturn = AccommodationEntity.builder().id(1L).name("Apartment").startingDate("10-08-2000").price(625.00).image("image").area(23).floors(1).interior("furnished").rooms(1).address(addressEntity).build();



        //act
        CreateAccommodationResponse response = accommodationManager.createAccommodation(request);
        //assert
        assertEquals(1L, (long) response.getAccommodationId());
        verify(accommodationRepository, times(1)).save(any(AccommodationEntity.class));
    }
    @Test
    void testUpdateAccommodationValid() {
        //Arrange
        final UpdateAccommodationRequest request = UpdateAccommodationRequest.builder().id(1L).name("New Apartment").startingDate("10-08-2000").price(625).image("image").area(23).floors(1).interior("unfurnished").rooms(1).houseNumber(10).postcode("ABCD12").streetName("Hemelrijken").build();

        final AddressEntity addressEntity = AddressEntity.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken").build();
        final AccommodationEntity savedAccommodation = AccommodationEntity.builder().id(1L).name("Apartment").startingDate("10-08-2000").price(625.00).image("image").area(23).floors(1).interior("furnished").rooms(1).address(addressEntity).build();



        //Act
        when(accommodationRepository.findById(1L)).thenReturn(Optional.ofNullable(savedAccommodation));

        accommodationManager.updateAccommodation(request);

        //Assert
        assertEquals(request.getName(), savedAccommodation.getName());
        assertEquals(request.getInterior(), savedAccommodation.getInterior());



        verify(accommodationRepository, times(2)).findById(1L);
        verify(accommodationRepository, times(1)).save(savedAccommodation);
    }

    @Test
    void testUpdateAccommodationIdInvalid() {
        //Arrange
        final UpdateAccommodationRequest request = UpdateAccommodationRequest.builder().id(2L).name("New Apartment").startingDate("10-08-2000").price(625).image("image").area(23).floors(1).interior("unfurnished").rooms(1).houseNumber(10).postcode("ABCD12").streetName("Hemelrijken").build();

        final AddressEntity addressEntity = AddressEntity.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken").build();
        final AccommodationEntity savedAccommodation = AccommodationEntity.builder().id(1L).name("Apartment").startingDate("10-08-2000").price(625.00).image("image").area(23).floors(1).interior("furnished").rooms(1).address(addressEntity).build();

        //Assert
        assertThrows(InvalidAccommodationException.class, () -> accommodationManager.updateAccommodation(request));
    }
    @Test
    void testDeleteAccommodation() {
        //Arrange
        AddressEntity addressEntity1 = new AddressEntity();
        addressEntity1.setId(1L);
        addressEntity1.setStreetName("test");
        addressEntity1.setNumber(1);
        addressEntity1.setPostcode("testCode");

        AccommodationEntity accommodationEntity = new AccommodationEntity();
        accommodationEntity.setId(1L);
        accommodationEntity.setName("testName");
        accommodationEntity.setAddress(addressEntity1);
        accommodationEntity.setFloors(1);
        accommodationEntity.setRooms(1);
        accommodationEntity.setImage("image");
        accommodationEntity.setPrice(500.00);
        accommodationEntity.setStartingDate("now");
        accommodationEntity.setInterior("test");
        accommodationEntity.setArea(4);
        long accommodationId = 1L;
        //Act
        accommodationManager.deleteAccommodation(accommodationId);

        //Assert
        verify(accommodationRepository, times(1)).deleteById(accommodationId);
    }
    @Test
    void testGetAllAccommodations() {
        // Arrange

        final AddressEntity addressEntity = AddressEntity.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken").build();
        final AccommodationEntity accommodationEntity = AccommodationEntity.builder().id(1L).name("Apartment").startingDate("10-08-2000").price(625.00).image("image").area(23).floors(1).interior("furnished").rooms(1).address(addressEntity).build();

        final AddressEntity addressEntity2 = AddressEntity.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken2").build();
        final AccommodationEntity accommodationEntity2 = AccommodationEntity.builder().id(1L).name("Apartment2").startingDate("10-08-2000").price(725.00).image("image2").area(24).floors(1).interior("furnished").rooms(1).address(addressEntity2).build();



        //Act
        when(accommodationRepository.findAll())
                .thenReturn(List.of(accommodationEntity, accommodationEntity2));

        GetAllAccommodationsResponse actualResult = accommodationManager.getAllAccommodations();

        final Address expectedAddress = Address.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken").build();
        final Accommodation expectedAccommodation = Accommodation.builder().id(1L).name("Apartment").startingDate("10-08-2000").price(625.00).image("image").area(23).floors(1).interior("furnished").rooms(1).address(expectedAddress).build();

        final Address expectedAddress2 = Address.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken2").build();
        final Accommodation expectedAccommodation2 = Accommodation.builder().id(1L).name("Apartment2").startingDate("10-08-2000").price(725.00).image("image2").area(24).floors(1).interior("furnished").rooms(1).address(expectedAddress2).build();

        GetAllAccommodationsResponse expectedResult = GetAllAccommodationsResponse.builder().accommodations(List.of(expectedAccommodation, expectedAccommodation2)).build();

        //Assert
        assertEquals(expectedResult, actualResult);
        verify(accommodationRepository, times(1)).findAll();
    }
    @Test
    void testGetAccommodationById() {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(1L);
        addressEntity.setStreetName("test");
        addressEntity.setNumber(1);
        addressEntity.setPostcode("testCode");

        AccommodationEntity accommodationEntity = new AccommodationEntity();
        accommodationEntity.setId(1L);
        accommodationEntity.setName("testName");
        accommodationEntity.setAddress(addressEntity);
        accommodationEntity.setFloors(1);
        accommodationEntity.setRooms(1);
        accommodationEntity.setImage("image");
        accommodationEntity.setPrice(500.00);
        accommodationEntity.setStartingDate("now");
        accommodationEntity.setInterior("test");
        accommodationEntity.setArea(4);

        when(accommodationRepository.findById(1L)).thenReturn(Optional.of(accommodationEntity));

        Optional<Accommodation> accommodation = accommodationManager.getAccommodation(1L);

        assertEquals(accommodationEntity.getId(), accommodation.get().getId());
        assertEquals(accommodationEntity.getArea(), accommodation.get().getArea());
        assertEquals(accommodationEntity.getFloors(), accommodation.get().getFloors());
        assertEquals(accommodationEntity.getPrice(), accommodation.get().getPrice());
        assertEquals(accommodationEntity.getRooms(), accommodation.get().getRooms());
        assertEquals(accommodationEntity.getImage(), accommodation.get().getImage());
        assertEquals(accommodationEntity.getName(), accommodation.get().getName());
        assertEquals(accommodationEntity.getInterior(), accommodation.get().getInterior());
        assertEquals(accommodationEntity.getStartingDate(), accommodation.get().getStartingDate());


        verify(accommodationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAccommodationByIdNotFound() {
        when(accommodationRepository.findById(1L)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> accommodationManager.getAccommodation(1L));

        verify(accommodationRepository, times(1)).findById(1L);
    }

    @Test
    void testPurchaseAccommodation() {
        //Arrange
        AddressEntity addressEntity1 = new AddressEntity();
        addressEntity1.setId(1L);
        addressEntity1.setStreetName("test");
        addressEntity1.setNumber(1);
        addressEntity1.setPostcode("testCode");

        AccommodationEntity accommodationEntity = new AccommodationEntity();
        accommodationEntity.setId(1L);
        accommodationEntity.setName("testName");
        accommodationEntity.setAddress(addressEntity1);
        accommodationEntity.setFloors(1);
        accommodationEntity.setRooms(1);
        accommodationEntity.setImage("image");
        accommodationEntity.setPrice(500.00);
        accommodationEntity.setStartingDate("now");
        accommodationEntity.setInterior("test");
        accommodationEntity.setArea(4);
        long accommodationId = 1L;
        //Act
        accommodationManager.purchaseAccommodation(accommodationId);

        //Assert
        verify(accommodationRepository, times(1)).getById(accommodationId);
        verify(userAccommodationRepository, times(1)).updateStatus(accommodationId);
    }

    @Test
    void testGetAllActiveAccommodations() {
        // Arrange

        final AddressEntity addressEntity = AddressEntity.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken").build();
        final AccommodationEntity accommodationEntity = AccommodationEntity.builder().id(1L).name("Apartment").startingDate("10-08-2000").price(625.00).image("image").area(23).floors(1).interior("furnished").rooms(1).address(addressEntity).build();

        final AddressEntity addressEntity2 = AddressEntity.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken2").build();
        final AccommodationEntity accommodationEntity2 = AccommodationEntity.builder().id(1L).name("Apartment2").startingDate("10-08-2000").price(725.00).image("image2").area(24).floors(1).interior("furnished").rooms(1).address(addressEntity2).build();



        //Act
        when(accommodationRepository.getAllAvailableAccommodations())
                .thenReturn(List.of(accommodationEntity, accommodationEntity2));

        GetAllAccommodationsResponse actualResult = accommodationManager.getAllActiveAccommodations();

        final Address expectedAddress = Address.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken").build();
        final Accommodation expectedAccommodation = Accommodation.builder().id(1L).name("Apartment").startingDate("10-08-2000").price(625.00).image("image").area(23).floors(1).interior("furnished").rooms(1).address(expectedAddress).build();

        final Address expectedAddress2 = Address.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken2").build();
        final Accommodation expectedAccommodation2 = Accommodation.builder().id(1L).name("Apartment2").startingDate("10-08-2000").price(725.00).image("image2").area(24).floors(1).interior("furnished").rooms(1).address(expectedAddress2).build();

        GetAllAccommodationsResponse expectedResult = GetAllAccommodationsResponse.builder().accommodations(List.of(expectedAccommodation, expectedAccommodation2)).build();

        //Assert
        verify(accommodationRepository, times(1)).getAllAvailableAccommodations();
    }

    @Test
    void testGetAllOwnedAccommodations() {
        // Arrange

        final AddressEntity addressEntity = AddressEntity.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken").build();
        final AccommodationEntity accommodationEntity = AccommodationEntity.builder().id(1L).name("Apartment").startingDate("10-08-2000").price(625.00).image("image").area(23).floors(1).interior("furnished").rooms(1).address(addressEntity).build();

        final AddressEntity addressEntity2 = AddressEntity.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken2").build();
        final AccommodationEntity accommodationEntity2 = AccommodationEntity.builder().id(1L).name("Apartment2").startingDate("10-08-2000").price(725.00).image("image2").area(24).floors(1).interior("furnished").rooms(1).address(addressEntity2).build();



        //Act
        when(accommodationRepository.getAllOwnedAccommodations(accessToken.getUserId()))
                .thenReturn(List.of(accommodationEntity, accommodationEntity2));

        GetAllAccommodationsResponse actualResult = accommodationManager.getAllOwnedAccommodations();

        final Address expectedAddress = Address.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken").build();
        final Accommodation expectedAccommodation = Accommodation.builder().id(1L).name("Apartment").startingDate("10-08-2000").price(625.00).image("image").area(23).floors(1).interior("furnished").rooms(1).address(expectedAddress).build();

        final Address expectedAddress2 = Address.builder().id(1L).number(10).postcode("ABCD12").streetName("Hemelrijken2").build();
        final Accommodation expectedAccommodation2 = Accommodation.builder().id(1L).name("Apartment2").startingDate("10-08-2000").price(725.00).image("image2").area(24).floors(1).interior("furnished").rooms(1).address(expectedAddress2).build();

        GetAllAccommodationsResponse expectedResult = GetAllAccommodationsResponse.builder().accommodations(List.of(expectedAccommodation, expectedAccommodation2)).build();

        //Assert
        verify(accommodationRepository, times(1)).getAllOwnedAccommodations(accessToken.getUserId());
    }
}
