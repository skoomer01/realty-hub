import org.example.business.exception.DuplicateAddressException;
import org.example.business.impl.AddressManager;
import org.example.domain.CreateAddressRequest;
import org.example.domain.CreateAddressResponse;
import org.example.persistance.AddressRepository;
import org.example.persistance.entity.AddressEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class AddressManagerTest {
    private final CreateAddressRequest request = CreateAddressRequest.builder().number(10).postcode("ABCD12").streetName("Hemelrijken").build();
    private final Long addressId = 10L;
    private final CreateAddressResponse response = CreateAddressResponse.builder().addressId(addressId).build();
    private final AddressEntity addressEntityArgument = AddressEntity.builder().number(10).postcode("ABCD12").streetName("Hemelrijken").build();
    final AddressEntity addressEntityReturn = AddressEntity.builder().id(addressId).number(10).postcode("ABCD12").streetName("Hemelrijken").build();

    @Test
    void shouldSaveAddressWithAllFields() {
        //arrange
        AddressRepository repository = mock(AddressRepository.class);
        when(repository.save(addressEntityArgument)).thenReturn(addressEntityReturn);
        when(repository.existsByStreetName(request.getStreetName()) && repository.existsByNumber(request.getNumber())).thenReturn(false);

        AddressManager addressManager = new AddressManager(repository);
        //act
        CreateAddressResponse actualResponse = addressManager.createAddress(request);
        //assert
        assertEquals(actualResponse, response, "Wrong response is returned");
        verify(repository, description("Save of repo is not called (with correct argument).")).save(addressEntityArgument);
    }

    @Test
    void duplicatedAddressShouldThrowException() {
        // arrange
        AddressRepository repository = mock(AddressRepository.class);
        when(repository.existsByStreetName(request.getStreetName())).thenReturn(true);
        when(repository.existsByNumber(request.getNumber())).thenReturn(true);

        AddressManager addressManager = new AddressManager(repository);

        // act & assert
        Throwable thrown = Assertions.assertThrows(DuplicateAddressException.class, () -> {
            addressManager.createAddress(request);
        });

        Assertions.assertEquals("400 BAD_REQUEST \"ADDRESS_DUPLICATED\"", thrown.getMessage());
    }
}
