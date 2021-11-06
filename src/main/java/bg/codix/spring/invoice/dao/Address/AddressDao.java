package bg.codix.spring.invoice.dao.Address;

import bg.codix.spring.invoice.entities.Address;

public interface AddressDao
{
  Long addAddress(Address address);

  void removeAddress(Long addressId);
}
