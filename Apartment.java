import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public abstract class Apartment implements Serializable {
    private static final long serialVersionUID = -3019627042156823628L;

protected static String print_custom(String key, String value) {
    return String.format("%-19s%s%n", key + ":", value);
}



    private final int id;
    private final double area;
    private final int rooms;
    private final int floor;
    private final LocalDate year_built;
    private final PropertyManagementClient.Adresse adresse;

    public Apartment(int id, double area, int rooms, int floor, LocalDate year_built, PropertyManagementClient.Adresse adresse) {
        if (year_built.isAfter(LocalDate.now())) throw PropertyManagementClient.YEAR_BUILD_INVALID;
        if (area <= 0) throw PropertyManagementClient.DATA_INVALID;
        if (rooms <= 0) throw PropertyManagementClient.DATA_INVALID;
        this.id = id;
        this.area = area;
        this.rooms = rooms;
        this.floor = floor;
        this.year_built = year_built;
        this.adresse = adresse;
    }

    public Apartment (int id, double area, int rooms, int floor, int year_built, PropertyManagementClient.Adresse adresse) {
        this(
            id,
            area,
            rooms,
            floor,
            LocalDate.of(year_built, 1, 1),
            adresse
        );
    }

    public int getId() {
        return id;
    }

    public double getArea() {
        return area;
    }

    public int getRooms() {
        return rooms;
    }

    public int getFloor() {
        return floor;
    }

    public LocalDate get_Year_Built() {
        return year_built;
    }

    public PropertyManagementClient.Adresse getAdresse() {
        return adresse;
    }

    public Period alter() {
        return year_built.until(LocalDate.now());
    }

    public abstract BigDecimal getTotalCost();

    public abstract String getType();

@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    
    sb.append(print_custom("Type", getType()))
      .append(print_custom("Id", String.valueOf(id)))
      .append(print_custom("Area", PropertyManagementClient.DF.format(area)))
      .append(print_custom("Rooms", String.valueOf(rooms)))
      .append(print_custom("Floor", String.valueOf(floor)))
      .append(print_custom("Year Built", String.valueOf(year_built.getYear())))
      .append(print_custom("Postal Code", String.valueOf(adresse.getPlz())))
      .append(print_custom("Street", adresse.getStreet()))
      .append(print_custom("House Number", String.valueOf(adresse.get_house_number())))
      .append(print_custom("Apartment", String.valueOf(adresse.getTop())));
    
    return sb.toString();
}


}
