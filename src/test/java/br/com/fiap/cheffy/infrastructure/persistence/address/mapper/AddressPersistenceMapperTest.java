package br.com.fiap.cheffy.infrastructure.persistence.address.mapper;

import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.infrastructure.persistence.address.entity.AddressJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.user.entity.UserJpaEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressPersistenceMapperTest {

    private final AddressPersistenceMapper addressPersistenceMapper = new AddressPersistenceMapper();

    @Test
    void toAddressJpaEntity() {
        UserJpaEntity entity = new UserJpaEntity();
        Address address = new Address(
                1L,
                "Street",
                1966,
                "São Paulo",
                "12345678",
                "String neighborhood",
                "SP",
                null,
        true
        );

        AddressJpaEntity result = addressPersistenceMapper.toJpa(address, entity);

        assertNotNull(result);
        assertEquals(address.getId(), result.getId());
        assertEquals(address.getStreetName(), result.getStreetName());
    }

    @Test
    void toAddressDomain() {
        AddressJpaEntity address = new AddressJpaEntity();
                address.setId(1L);
        address.setStreetName("Street");
        address.setNumber(123);
        address.setCity("City");
        address.setPostalCode("12345678");
        address.setNeighborhood("Neighborhood");
        address.setStateProvince("SP");
        address.setMain(true);

        Address result = addressPersistenceMapper.toDomain(address);

        assertNotNull(result);
        assertEquals(address.getId(), result.getId());
        assertEquals(address.getStreetName(), result.getStreetName());
    }

}