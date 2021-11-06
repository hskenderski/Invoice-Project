package bg.codix.spring.invoice.services.Address;

import bg.codix.spring.invoice.dao.Address.AddressDao;
import bg.codix.spring.invoice.entities.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AddressServiceImpl implements AddressService
{
  private final AddressDao addressDao;

  @Autowired
  public AddressServiceImpl(AddressDao addressDao)
  {
    this.addressDao = addressDao;
  }

  @Override
  public Long createAddress(Address address)
  {
    return this.addressDao.addAddress(address);
  }
}
