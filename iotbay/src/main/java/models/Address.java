package models;

/**
 * The Address class is designed to be immutable to ensure that once an address is created,
 * it cannot be modified. This is particularly important because the Address class is also
 * used for orders. If a user changes their address after placing an order, it would 
 * unintentionally change the delivery address for that order, which would not make sense.
 * 
 * To prevent this, we always create a new Address entry in the database whenever an address
 * needs to be updated or changed.
 */
public class Address {
    private final Integer addressID; 
    private final String name;       
    private final Integer streetNumber;    
    private final String streetName;     
    private final Integer postcode;   
    private final String suburb;     
    private final String city;       
    private final String state;      

    public Address(Integer addressID, String name, Integer streetNumber, String streetName, Integer postcode, String suburb, String city, String state) {
        this.addressID = addressID;
        this.name = name;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.postcode = postcode;
        this.suburb = suburb;
        this.city = city;
        this.state = state;
    }

    public Integer getAddressID() {
        return addressID;
    }

    public String getName() {
        return name;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public Integer getPostcode() {
        return postcode;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }
}
