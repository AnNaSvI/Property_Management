import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public final class PropertyManagementClient {
    private enum Commands {
        LIST("list"),
        ADD("add"),
        DELETE("delete"),
        COUNT("count"),
        MEANCOSTS("meancosts"),
        OLDEST("oldest");

        private final String key;

        Commands(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

    
        public static Commands forKey(String key) {
            if (key == null) {
                throw DATA_INVALID;
            }

            for (Commands command : values()) {
                if (command.key.equals(key)) {
                    return command;
                }
            }

        throw DATA_INVALID;
        }

    }

    private enum ApartmentType {
        OWNED_APARTMENT("OA", OwnedApartment.class),
        RENTED_APARTMENT("RA", RentedApartment.class);
        private final String str;
        private final Class<?> cls;

        ApartmentType(String str, Class<?> cls) {
            this.str = str;
            this.cls = cls;
        }

        public String getStr() {
            return str;
        }

        public Class<?> getCls() {
            return cls;
        }


        public static ApartmentType forStr(String str) {
            if (str == null) {
                throw DATA_INVALID;
            }
            for (ApartmentType type : values()) {
                if (type.str.equals(str)) {
                    return type;
                }
            }
            throw DATA_INVALID;
        }


   
        public static ApartmentType forCls(Class<?> cls) {
            if (cls == null) {
                throw new IllegalArgumentException("Class cannot be null");
            }
            for (ApartmentType type : values()) {
                if (type.cls.isAssignableFrom(cls)) {
                    return type;
                }
            }
            throw DATA_INVALID;
        }

    }

    public static class Adresse implements Serializable {
        private static final long serialVersionUID = 3380825380726751815L;
        private final int plz;
        private final String street;
        private final int house_number;
        private final int top;

        public Adresse(int plz, String street, int house_number, int top) {
            this.plz = plz;
            this.street = street;
            this.house_number = house_number;
            this.top = top;
        }

        public int getPlz() {
            return plz;
        }

        public String getStreet() {
            return street;
        }

        public int get_house_number() {
            return house_number;
        }

        public int getTop() {
            return top;
        }

     

    }

    public static final IllegalArgumentException DATA_INVALID = new IllegalArgumentException("Error: Invalid parameter.");
    public static final IllegalArgumentException YEAR_BUILD_INVALID = new IllegalArgumentException("Error: Invalid year of construction.");
    public static final DecimalFormat DF = new DecimalFormat("0.00");

    public static IllegalArgumentException apartmentAlreadyExists(int id) {
        return new IllegalArgumentException("Error: Apartment already exists. (id=" + id + ')');
    }

    public static IllegalArgumentException apartmentDoesntExist(int id) {
        return new IllegalArgumentException("Error: Apartment not found. (id=" + id + ')');
    }

    public static void main(String[] args) {
        try {
            PropertyManagement property_management = new PropertyManagement(new PropertyManagementSerializationDAO(Paths.get(args[0])));
            Commands rootCommand = Commands.forKey(args[1]);

            switch (rootCommand) {
                case LIST:
                    list(property_management, args);
                    break;
                case ADD:
                    add(property_management, args);
                    break;
                case DELETE:
                    delete(property_management, args);
                    break;
                case COUNT:
                    count(property_management, args);
                    break;
                case MEANCOSTS:
                    meancosts(property_management);
                    break;
                case OLDEST:
                    oldest(property_management);
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException exc) {
            System.out.println(DATA_INVALID.getMessage());
        } catch (IllegalArgumentException exc) {
            System.out.println(exc.getMessage());
        }
    }
    

  



    public static void list(PropertyManagement property_management, String[] args) {
    if (args.length <= 2) {
        for (Apartment a : property_management.getAllData()) {
            System.out.println(a);
        }
    } else {
        try {
            int apartmentId = Integer.parseInt(args[2]);
            Apartment a = property_management.getDataOf(apartmentId);
            if (a != null) {
                System.out.println(a);
            } else {
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid apartment ID format");
        }
    }
}


    public static void add(PropertyManagement property_management, String[] args) {
        ApartmentType type = ApartmentType.forStr(args[2]);
        int id = Integer.parseInt(args[3]);
        double area = Double.parseDouble(args[4]);
        int rooms = Integer.parseInt(args[5]);
        int floor = Integer.parseInt(args[6]);
        int year_built = Integer.parseInt(args[7]);
        int plz = Integer.parseInt(args[8]);
        String street = args[9];
        int house_number = Integer.parseInt(args[10]);
        int top = Integer.parseInt(args[11]);

        switch (type) {
            case OWNED_APARTMENT:
                BigDecimal operating_costs = new BigDecimal(args[12]);
                BigDecimal maintenance_reserve = new BigDecimal(args[13]);
                property_management.addApartment(new OwnedApartment(
                    id,
                    area,
                    rooms,
                    floor,
                    year_built,
                    new Adresse(plz, street, house_number, top),
                    operating_costs,
                    maintenance_reserve
                ));
                break;
            case RENTED_APARTMENT:
                BigDecimal monthly_rent = new BigDecimal(args[12]);
                int rent = Integer.parseInt(args[13]);
                property_management.addApartment(new RentedApartment(
                    id,
                    area,
                    rooms,
                    floor,
                    year_built,
                    new Adresse(plz, street, house_number, top),
                    monthly_rent,
                    rent
                ));
                break;
        }
        System.out.println("Info: Apartment " + id + " added.");
    }

    private static void delete(PropertyManagement property_management, String[] args) {
        int id = Integer.parseInt(args[2]);
        property_management.deleteApartment(id);
        System.out.println("Info: Apartment " + id + " deleted."); 
    }

   
    private static void count(PropertyManagement property_management, String[] args) {
    if (args.length <= 2) {
        System.out.println(property_management.count(Apartment.class));
    } else {
        try {
            ApartmentType type = ApartmentType.forStr(args[2]);
            System.out.println(property_management.count(type.getCls()));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}


 
    private static void meancosts(PropertyManagement property_management) {
    try {
        BigDecimal averageCosts = property_management.averageMonthlyCosts().setScale(2, RoundingMode.HALF_UP);
        System.out.println(DF.format(averageCosts));
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
}


   

    private static void oldest(PropertyManagement property_management) {
    if (property_management.oldestApartmentId().isEmpty()) {
        System.out.println("No apartments found.");
    } else {
        for (int id : property_management.oldestApartmentId()) {
            System.out.println("Id: " + id);
        }
    }
}

}
