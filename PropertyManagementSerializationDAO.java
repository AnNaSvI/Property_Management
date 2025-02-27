import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class PropertyManagementSerializationDAO implements PropertyManagementDAO {
    private final Path savefile;

    @SuppressWarnings("unchecked")
    private static <T> T readObjectUnchecked(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (T) ois.readObject();
    }

    public PropertyManagementSerializationDAO(Path savefile) {
        this.savefile = savefile;
    }


        private void saveApartment(List<Apartment> apartments) {
            if (apartments == null || apartments.isEmpty()) {
                System.err.println("Error: The list of apartments is empty or null.");
                return; 
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(
                    savefile,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE
            ))) {
                oos.writeObject(apartments);
            } catch (IOException exc) {
                System.err.println("Error during serialization: " + exc.getMessage());
                System.exit(1);
                throw new RuntimeException("Failed to save apartments", exc);
            }
        }

 
    @Override
public List<Apartment> getApartments() {
    if (!Files.exists(savefile)) {
        return new ArrayList<>(); 
    }
    try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(savefile))) {
        return readObjectUnchecked(ois); 
    } catch (IOException exc) {
        System.err.println("Error during deserialization: " + exc.getMessage());
        System.exit(1);
        throw new RuntimeException("Failed to retrieve apartments from file", exc);
    } catch (ClassNotFoundException exc) {
        System.err.println("Error: Class not found during deserialization: " + exc.getMessage());
        System.exit(1);
        throw new RuntimeException("Failed to retrieve apartments due to class not found", exc);
    }
}


    @Override
    public Apartment getApartmentById(int id) {
        List<Apartment> apartments = getApartments();
        return apartments.stream()
            .filter(apartment -> apartment.getId() == id)
            .findAny()
            .orElse(null);
    }
    


   @Override
public void saveApartment(Apartment apartment) {
    try {
        List<Apartment> apartments = getApartments();

        if (apartments.stream().anyMatch(a -> apartment.getId() == a.getId())) {
            throw PropertyManagementClient.apartmentAlreadyExists(apartment.getId());
        }

        apartments.add(apartment);
        saveApartment(apartments);

    } catch (Exception e) {
        System.err.println("Serialization error: " + e.getMessage());
        System.exit(1);
    } 
}




    @Override
    public void deleteApartment(int id) {
        
        List<Apartment> apartments = getApartments();
        if (!apartments.removeIf(apartment -> id == apartment.getId())) {
            throw PropertyManagementClient.apartmentDoesntExist(id);
        }
        saveApartment(apartments);
    }
}
