import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;


public final class PropertyManagement {
    private final PropertyManagementDAO PropertyManagementDAO;

    public PropertyManagement(PropertyManagementDAO PropertyManagementDAO) {
        this.PropertyManagementDAO = PropertyManagementDAO;
    }

    public  List<Apartment> getAllData() {
        return PropertyManagementDAO.getApartments();
    }

    public  Apartment getDataOf(int id) {
        return PropertyManagementDAO.getApartmentById(id);
    }

    public void addApartment(Apartment a) {
        PropertyManagementDAO.saveApartment(a);
    }

    public void deleteApartment(int id) {
        PropertyManagementDAO.deleteApartment(id);
    }

    public long count(Class<?> clazz) {
        return PropertyManagementDAO.getApartments()
            .stream()
            .filter(clazz::isInstance)
            .count();
    }

    public BigDecimal averageMonthlyCosts() {
    List<Apartment> apartments = PropertyManagementDAO.getApartments();
    BigDecimal count = BigDecimal.valueOf(apartments.size());
    if (count.compareTo(BigDecimal.ZERO) == 0) {
        return BigDecimal.ZERO;
    }

    BigDecimal totalCost = apartments.stream()
        .map(Apartment::getTotalCost)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    return totalCost.divide(count, 2, RoundingMode.HALF_UP);
    }


    public List<Integer> oldestApartmentId() {
    List<Apartment> apartments = PropertyManagementDAO.getApartments();

    if (apartments.isEmpty()) {
        return Collections.emptyList();
    }

    LocalDate oldestDate = apartments.stream()
        .map(Apartment::get_Year_Built)
        .min(LocalDate::compareTo)
        .orElseThrow(() -> new IllegalStateException("No apartments found"));

    return apartments.stream()
        .filter(apartment -> oldestDate.equals(apartment.get_Year_Built()))
        .map(Apartment::getId)
        .collect(Collectors.toList());
}

}
