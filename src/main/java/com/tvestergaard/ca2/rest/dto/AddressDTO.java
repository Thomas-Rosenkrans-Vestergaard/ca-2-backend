package com.tvestergaard.ca2.rest.dto;

import com.tvestergaard.ca2.data.entities.Address;

public class AddressDTO
{

    private String  street;
    private String  information;
    private CityDTO city;

    public AddressDTO(Address address, boolean showCity)
    {
        this.street = address.getStreet();
        this.information = address.getInformation();
        if (showCity)
            this.city = new CityDTO(address.getCity());
    }
}
