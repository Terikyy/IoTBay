package model;

/**
 * The Address class is designed to be immutable to ensure that once an address is created,
 * it cannot be modified. This is particularly important because the Address class is also
 * used for orders. If a user changes their address after placing an order, it would
 * unintentionally change the delivery address for that order, which would not make sense.
 * <p>
 * To prevent this, we always create a new Address entry in the database whenever an address
 * needs to be updated or changed.
 */
public class Address extends IDObject {
    private String name;
    private int streetNumber;
    private String streetName;
    private int postcode;
    private String suburb;
    private String city;
    private String state;

    public Address() {//default constructor
        super();
    }

    public Address(String name, int streetNumber, String streetName, int postcode, String suburb, String city, String state) {
        super();
        this.name = name;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.postcode = postcode;
        this.suburb = suburb;
        this.city = city;
        this.state = state;
    }

    public Address(int addressId, String name, int streetNumber, String streetName, int postcode, String suburb, String city, String state) {
        this(name, streetNumber, streetName, postcode, suburb, city, state);
        setId(addressId);
    }

    public int getAddressID() {
        return getId();
    }

    public String getName() {
        return name;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public int getPostcode() {
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
