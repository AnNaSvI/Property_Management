import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class OwnedApartment extends Apartment {
    private static final long serialVersionUID = -4761102347229678219L;
    private final BigDecimal operating_costs;
    private final BigDecimal maintenance_reserve;

    public OwnedApartment(
        int id,
        double area,
        int rooms,
        int floor,
        LocalDate year_built,
        PropertyManagementClient.Adresse adresse,
        BigDecimal operating_costs,
        BigDecimal maintenance_reserve
    ) {
        super(id, area, rooms, floor, year_built, adresse);
        if (operating_costs.compareTo(BigDecimal.ZERO) <= 0) throw PropertyManagementClient.DATA_INVALID;
        if (maintenance_reserve.compareTo(BigDecimal.ZERO) <= 0) throw PropertyManagementClient.DATA_INVALID;
        this.operating_costs = operating_costs.setScale(2, RoundingMode.HALF_UP);
        this.maintenance_reserve = maintenance_reserve.setScale(2, RoundingMode.HALF_UP);
    }

    public OwnedApartment(
        int id,
        double area,
        int rooms,
        int floor,
        int year_built,
        PropertyManagementClient.Adresse adresse,
        BigDecimal operating_costs,
        BigDecimal maintenance_reserve
    ) {
        this(
            id,
            area,
            rooms,
            floor,
            LocalDate.of(year_built, 1, 1),
            adresse,
            operating_costs,
            maintenance_reserve
        );
    }

    public BigDecimal get_operating_costs() {
        return operating_costs;
    }

    public BigDecimal get_maintenance_reserve() {
        return maintenance_reserve;
    }

    @Override
    public BigDecimal getTotalCost() {
        BigDecimal area = BigDecimal.valueOf(getArea());
        BigDecimal floorFactor = BigDecimal.valueOf(1.0 + 0.02 * getFloor());
        BigDecimal totalCosts = operating_costs.add(maintenance_reserve);

        return totalCosts.multiply(area).multiply(floorFactor);
    }

    @Override
    public String getType() {
        return "OA";
    }

    @Override
    public String toString() {
        return super.toString()
            + print_custom("Operating Costs", PropertyManagementClient.DF.format(operating_costs))
            + print_custom("Reserve Fund", PropertyManagementClient.DF.format(maintenance_reserve));
    }
}
