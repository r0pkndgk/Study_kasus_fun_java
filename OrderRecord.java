import java.io.Serializable;

/**
 * OrderRecord - Model data untuk menyimpan informasi satu transaksi pemesanan layanan
 * pada klinik hewan "BluePaw Vet & Grooming".
 *
 * <p>Kelas ini digunakan untuk integrasi tabel riwayat (JTable) di Tab 2
 * dan mendukung operasi CRUD: tambah, edit (via form Tab 1), hapus, dan selesai.</p>
 */
public class OrderRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
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

    /**
     * Konstruktor penuh OrderRecord.
     *
     * @param id             nomor pemesanan unik (misal "ORD-001")
     * @param ownerName      nama pemilik hewan
     * @param petName        nama hewan peliharaan
     * @param petType        jenis hewan (Kucing, Anjing, dll.)
     * @param weight         bobot hewan dalam kilogram
     * @param doctor         nama dokter pemeriksa
     * @param groomingPackage paket grooming yang dipilih ("Tidak Ada" jika tidak ada)
     * @param vaccine        apakah layanan Vaksinasi dipilih
     * @param food           apakah layanan Pakan Khusus dipilih
     * @param checkup        apakah layanan Checkup Umum dipilih
     * @param boarding       apakah layanan Rawat Inap dipilih
     * @param bedah          apakah layanan Bedah Minor dipilih
     * @param paymentMethod  metode pembayaran ("Cash" atau "QRIS")
     * @param cashReceived   jumlah uang yang diterima (jika Cash, 0 jika QRIS)
     * @param totalCost      total biaya keseluruhan layanan
     * @param status         status pembayaran/penyelesaian transaksi
     */
    public OrderRecord(String id, String ownerName, String petName, String petType,
                       double weight, String doctor, String groomingPackage,
                       boolean vaccine, boolean food, boolean checkup,
                       boolean boarding, boolean bedah, String paymentMethod,
                       double cashReceived, double totalCost, String status) {
        this.id             = id;
        this.ownerName      = ownerName;
        this.petName        = petName;
        this.petType        = petType;
        this.weight         = weight;
        this.doctor         = doctor;
        this.groomingPackage = groomingPackage;
        this.vaccine        = vaccine;
        this.food           = food;
        this.checkup        = checkup;
        this.boarding       = boarding;
        this.bedah          = bedah;
        this.paymentMethod  = paymentMethod;
        this.cashReceived   = cashReceived;
        this.totalCost      = totalCost;
        this.status         = status;
    }

    /**
     * Menghasilkan ringkasan teks seluruh layanan yang dipilih, dipisahkan koma.
     * Contoh: "Grooming (Full Grooming), Vaksinasi, Checkup"
     *
     * @return ringkasan layanan, atau "Tidak ada layanan" jika tidak ada yang dipilih
     */
    public String getServicesSummary() {
        final StringBuilder sb = new StringBuilder();
        if (groomingPackage != null && !"Tidak Ada".equals(groomingPackage)) {
            sb.append("Grooming (").append(groomingPackage).append(")");
        }
        appendService(sb, vaccine,  "Vaksinasi");
        appendService(sb, food,     "Pakan");
        appendService(sb, checkup,  "Checkup");
        appendService(sb, boarding, "Rawat Inap");
        appendService(sb, bedah,    "Bedah Minor");
        return sb.length() == 0 ? "Tidak ada layanan" : sb.toString();
    }

    /** Helper untuk menambahkan nama layanan ke StringBuilder dengan separator koma. */
    private static void appendService(StringBuilder sb, boolean active, String name) {
        if (active) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(name);
        }
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================

    public String getId()            { return id; }

    public String getOwnerName()     { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getPetName()       { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public String getPetType()       { return petType; }
    public void setPetType(String petType) { this.petType = petType; }

    public double getWeight()        { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getDoctor()        { return doctor; }
    public void setDoctor(String doctor) { this.doctor = doctor; }

    public String getGroomingPackage() { return groomingPackage; }
    public void setGroomingPackage(String groomingPackage) { this.groomingPackage = groomingPackage; }

    public boolean isVaccine()       { return vaccine; }
    public void setVaccine(boolean vaccine) { this.vaccine = vaccine; }

    public boolean isFood()          { return food; }
    public void setFood(boolean food) { this.food = food; }

    public boolean isCheckup()       { return checkup; }
    public void setCheckup(boolean checkup) { this.checkup = checkup; }

    public boolean isBoarding()      { return boarding; }
    public void setBoarding(boolean boarding) { this.boarding = boarding; }

    public boolean isBedah()         { return bedah; }
    public void setBedah(boolean bedah) { this.bedah = bedah; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public double getCashReceived()  { return cashReceived; }
    public void setCashReceived(double cashReceived) { this.cashReceived = cashReceived; }

    public double getTotalCost()     { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getStatus()        { return status; }
    public void setStatus(String status) { this.status = status; }
}
