import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class RentedApartment extends Apartment {
    private static final long serialVersionUID = -3980588736879024607L;
    private final BigDecimal monthly_rent;
    private final int number_of_tenants;

    public RentedApartment(
        int id,
        double area,
        int rooms,
        int floor,
        LocalDate year_built,
        PropertyManagementClient.Adresse adresse,
        BigDecimal monthly_rent,
        int number_of_tenants
    ) {
        super(id, area, rooms, floor, year_built, adresse);
        if (monthly_rent.compareTo(BigDecimal.ZERO) <= 0) throw PropertyManagementClient.DATA_INVALID;
        if (number_of_tenants <= 0) throw PropertyManagementClient.DATA_INVALID;
        this.monthly_rent = monthly_rent.setScale(2, RoundingMode.HALF_UP);
        this.number_of_tenants = number_of_tenants;
    }

    public RentedApartment(
        int id,
        double area,
        int rooms,
        int floor,
        int year_built,
        PropertyManagementClient.Adresse adresse,
        BigDecimal monthly_rent,
        int number_of_tenants
    ) {
        this(
            id,
            area,
            rooms,
            floor,
            LocalDate.of(year_built, 1, 1),
            adresse,
            monthly_rent,
            number_of_tenants
        );
    }

  
    @Override
    public BigDecimal getTotalCost() {
        if (monthly_rent.compareTo(BigDecimal.ZERO) <= 0 || getArea() <= 0) {
            throw new IllegalArgumentException("Monthly rent and area must be greater than zero.");
        }
        double tenantDiscountFactor = Math.min((number_of_tenants - 1) * 0.025, 0.1);

        BigDecimal totalCost = monthly_rent
            .multiply(BigDecimal.valueOf(getArea()))
            .multiply(BigDecimal.valueOf(1.0 + tenantDiscountFactor));

        return totalCost.setScale(2, RoundingMode.HALF_UP); 
    }


    @Override
    public String getType() {
        return "RA";
    }

    @Override
    public String toString() {
        return super.toString()
            + print_custom("Rent/m2", PropertyManagementClient.DF.format(monthly_rent))
            + print_custom("Number of Tenants", String.valueOf(number_of_tenants));
    }
}
