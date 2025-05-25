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
    private final String name;
    private final int streetNumber;
    private final String streetName;
    private final int postcode;
    private final String suburb;
    private final String city;
    private final String state;


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

    @Override
    public String toString() {
        return "Address{" +
                "addressID=" + getAddressID() +
                ", name='" + name + '\'' +
                ", streetNumber=" + streetNumber +
                ", streetName='" + streetName + '\'' +
                ", postcode=" + postcode +
                ", suburb='" + suburb + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        if (streetNumber != address.streetNumber) return false;
        if (postcode != address.postcode) return false;
        if (!name.equals(address.name)) return false;
        if (!streetName.equals(address.streetName)) return false;
        if (!suburb.equals(address.suburb)) return false;
        if (!city.equals(address.city)) return false;
        return state.equals(address.state);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + streetNumber;
        result = 31 * result + streetName.hashCode();
        result = 31 * result + postcode;
        result = 31 * result + suburb.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + state.hashCode();
        return result;
    }
}
