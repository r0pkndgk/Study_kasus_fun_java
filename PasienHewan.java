import java.io.Serializable;

public class PasienHewan implements Serializable {
    private static final long serialVersionUID = 1L;

    private String namaPeliharaan;
    private String jenisHewan;
    private String namaOwner;
    private double bobotKg;

    public PasienHewan(String namaPeliharaan, String jenisHewan, String namaOwner, double bobotKg) {
        this.namaPeliharaan = namaPeliharaan;
        this.jenisHewan = jenisHewan;
        this.namaOwner = namaOwner;
        this.bobotKg = bobotKg;
    }

    public String getNamaPeliharaan() {
        return namaPeliharaan;
    }

    public void setNamaPeliharaan(String namaPeliharaan) {
        this.namaPeliharaan = namaPeliharaan;
    }

    public String getJenisHewan() {
        return jenisHewan;
    }

    public void setJenisHewan(String jenisHewan) {
        this.jenisHewan = jenisHewan;
    }

    public String getNamaOwner() {
        return namaOwner;
    }

    public void setNamaOwner(String namaOwner) {
        this.namaOwner = namaOwner;
    }

    public double getBobotKg() {
        return bobotKg;
    }

    public void setBobotKg(double bobotKg) {
        this.bobotKg = bobotKg;
    }
}
