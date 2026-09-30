import java.io.Serializable;

/**
 * OrderRecord - Model data untuk menyimpan informasi pemesanan layanan
 * pada klinik hewan PetCare. Digunakan untuk integrasi tabel riwayat dan fungsi edit.
 */
public class OrderRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String ownerName;
    private String petName;
    private String petType;
    private double weight;
    private String doctor;
    private String groomingPackage;
    private boolean vaccine;
    private boolean food;
    private boolean checkup;
    private boolean boarding;
    private boolean bedah;
    private String paymentMethod;
    private double cashReceived;
    private double totalCost;
    private String status;

    public OrderRecord(String id, String ownerName, String petName, String petType, double weight,
                       String doctor, String groomingPackage, boolean vaccine, boolean food,
                       boolean checkup, boolean boarding, boolean bedah, String paymentMethod,
                       double cashReceived, double totalCost, String status) {
        this.id = id;
        this.ownerName = ownerName;
        this.petName = petName;
        this.petType = petType;
        this.weight = weight;
        this.doctor = doctor;
        this.groomingPackage = groomingPackage;
        this.vaccine = vaccine;
        this.food = food;
        this.checkup = checkup;
        this.boarding = boarding;
        this.bedah = bedah;
        this.paymentMethod = paymentMethod;
        this.cashReceived = cashReceived;
        this.totalCost = totalCost;
        this.status = status;
    }

    /**
     * Menghasilkan teks ringkasan seluruh layanan yang dipilih.
     */
    public String getServicesSummary() {
        StringBuilder sb = new StringBuilder();
        if (groomingPackage != null && !groomingPackage.equals("Tidak Ada")) {
            sb.append("Grooming (").append(groomingPackage).append(")");
        }
        if (vaccine) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("Vaksinasi");
        }
        if (food) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("Pakan");
        }
        if (checkup) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("Checkup");
        }
        if (boarding) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("Rawat Inap");
        }
        if (bedah) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("Bedah Minor");
        }
        return sb.length() == 0 ? "Tidak ada layanan" : sb.toString();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public String getPetType() { return petType; }
    public void setPetType(String petType) { this.petType = petType; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getDoctor() { return doctor; }
    public void setDoctor(String doctor) { this.doctor = doctor; }

    public String getGroomingPackage() { return groomingPackage; }
    public void setGroomingPackage(String groomingPackage) { this.groomingPackage = groomingPackage; }

    public boolean isVaccine() { return vaccine; }
    public void setVaccine(boolean vaccine) { this.vaccine = vaccine; }

    public boolean isFood() { return food; }
    public void setFood(boolean food) { this.food = food; }

    public boolean isCheckup() { return checkup; }
    public void setCheckup(boolean checkup) { this.checkup = checkup; }

    public boolean isBoarding() { return boarding; }
    public void setBoarding(boolean boarding) { this.boarding = boarding; }

    public boolean isBedah() { return bedah; }
    public void setBedah(boolean bedah) { this.bedah = bedah; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public double getCashReceived() { return cashReceived; }
    public void setCashReceived(double cashReceived) { this.cashReceived = cashReceived; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
