public class LayananGrooming {
    private String paket;
    private double biayaLayanan;

    public LayananGrooming(String paket, double biayaLayanan) {
        this.paket = paket;
        this.biayaLayanan = biayaLayanan;
    }

    public String getPaket() {
        return paket;
    }

    public void setPaket(String paket) {
        this.paket = paket;
    }

    public double getBiayaLayanan() {
        return biayaLayanan;
    }

    public void setBiayaLayanan(double biayaLayanan) {
        this.biayaLayanan = biayaLayanan;
    }
}
