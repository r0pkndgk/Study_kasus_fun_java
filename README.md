# BluePaw Vet & Grooming

> **Solusi Kasir & Manajemen Terpadu Klinik Hewan Peliharaan yang Modern**
> *Dibangun dengan Java Swing + FlatLaf — Ringan, Cepat, dan Siap Pakai*

---

## Deskripsi Aplikasi

**BluePaw Vet & Grooming** adalah aplikasi desktop berbasis Java Swing yang dirancang khusus untuk memenuhi kebutuhan administrasi dan kasir pada klinik dokter hewan serta pusat perawatan hewan peliharaan (*pet grooming*).

Aplikasi ini menghadirkan antarmuka modern dengan **Custom Blue Palette** menggunakan framework **FlatLaf (Mac Light theme)**, ikon vektor **SVG** beresolusi tinggi, navigasi multi-tab yang intuitif, serta kalkulasi biaya layanan secara **real-time** yang adaptif terhadap bobot hewan.

---

## Fitur Utama

### 1. Registrasi Layanan & Pasien
- Input nama pemilik dan nama hewan peliharaan
- Pilihan jenis hewan: Kucing, Anjing, Kelinci, Burung, Lainnya
- Input bobot tubuh hewan (kg) dengan validasi angka real-time dan visual error border
- Pemilihan dokter hewan bertugas (Drh. Budi, Drh. Sarah, Drh. Andi)
- Paket Grooming: Mandi Kutu, Potong Bulu, Potong Kuku, Full Grooming
- Layanan tambahan via checkbox: Vaksinasi, Pakan Khusus, Checkup Umum, Rawat Inap, Bedah Minor

### 2. Kalkulasi Harga Dinamis (Real-time)
- Total tagihan dihitung otomatis setiap kali form diubah (DocumentListener & ActionListener)
- **Surcharge bobot >= 5 kg**: hewan berbobot 5 kg ke atas dikenakan biaya tambahan Rp 20.000 per layanan medis

### 3. Sistem Pembayaran Cash & QRIS
- **Mode Cash**: Tampilkan field "Uang Diterima", hitung kembalian secara real-time, tombol Cetak Nota dinonaktifkan jika uang kurang
- **Mode QRIS**: Sembunyikan field cash, tampilkan barcode QRIS (gambar qris_dummy.png)
- Toggle antara Cash dan QRIS bersifat instan tanpa reload

### 4. Nota Tagihan Digital
- Cetak nota dengan format teks terstruktur
- Menampilkan: data pasien, rincian layanan + biaya, metode pembayaran, dan kembalian
- Setelah dicetak, data otomatis tersimpan ke Tab Riwayat

### 5. Manajemen Riwayat Pemesanan (CRUD)
- Tabel riwayat transaksi dengan kolom: ID, Nama Pemilik, Nama Hewan, Layanan, Total, Status
- **Edit**: Kembalikan data ke form Tab 1 untuk direvisi
- **Hapus/Batal**: Hapus transaksi setelah konfirmasi dialog
- **Selesai**: Tandai transaksi sebagai "Selesai"

### 6. UI/UX Modern dengan Custom Blue Palette
| Elemen              | Warna            | Kode Hex  |
|---------------------|------------------|-----------|
| Background Utama    | Putih Bersih     | #FFFFFF |
| Background Kartu    | Biru Terang      | #c0e6fd |
| Border / Pemisah    | Biru Medium      | #80aad3 |
| Tombol Primary      | Biru Solid       | #3f6593 |
| Teks Utama          | Biru Sangat Gelap| #000f22 |

- Rounded corners pada semua input field, tombol, dan panel (via FlatLaf arc)
- Logo SVG luepaw-logo.svg tampil di header dan ikon jendela (taskbar)
- Font: **Segoe UI** (modern system font)

---

## Teknologi

| Komponen          | Detail                                  |
|-------------------|-----------------------------------------|
| **Bahasa**        | Java 11+                                |
| **UI Framework**  | Java Swing                              |
| **Look & Feel**   | FlatLaf 3.2.5 (FlatMacLightLaf)        |
| **SVG Support**   | flatlaf-extras 3.2.5 + JSVG 1.3.0      |
| **Build**         | Standalone (javac + run.bat)            |

---

## Struktur Proyek

`
Petcare/
|-- PetCareGUI.java        # Kelas utama GUI (JFrame + JTabbedPane)
|-- UIHelper.java          # Utilitas warna, factory komponen, SVG loader
|-- OrderRecord.java       # Model data transaksi (Serializable)
|-- PasienHewan.java       # Model data pasien hewan
|-- LayananGrooming.java   # Model data layanan grooming
|-- bluepaw-logo.svg       # Logo vektor aplikasi
|-- qris_dummy.png         # Gambar barcode QRIS placeholder
|-- run.bat                # Script kompilasi & jalankan (Windows)
|-- README.md              # Dokumentasi proyek ini
|-- lib/
|   |-- flatlaf-3.2.5.jar
|   |-- flatlaf-extras-3.2.5.jar
|   |-- flatlaf-intellij-themes-3.2.5.jar
|   -- jsvg-1.3.0.jar
`

---

## Cara Menjalankan

### Prasyarat
- **Java JDK 11 atau lebih baru** terinstal dan ada di PATH
- Sistem operasi: **Windows** (run.bat tersedia)
- Folder lib/ berisi semua file .jar yang diperlukan

### Langkah 1 — Clone / Unduh Proyek
`ash
git clone <url-repositori>
cd Petcare
`

### Langkah 2 — Jalankan via run.bat (Windows)
Klik dua kali file un.bat, atau jalankan dari terminal:
`at
.\run.bat
`
Script ini akan otomatis mengkompilasi semua file .java dan menjalankan aplikasi.

### Langkah 3 — Kompilasi & Jalankan Manual (Opsional)
`ash
# Kompilasi
javac -cp ".;lib/*" *.java

# Jalankan
java -cp ".;lib/*" PetCareGUI
`

> **Catatan:** Pastikan file luepaw-logo.svg dan qris_dummy.png berada di direktori yang sama dengan file .class saat menjalankan.

---

## Harga Layanan

| Layanan              | Harga Dasar    | Surcharge (>= 5 kg) |
|----------------------|----------------|----------------------|
| Grooming Mandi Kutu  | Rp 50.000      | + Rp 20.000          |
| Grooming Potong Bulu | Rp 40.000      | + Rp 20.000          |
| Grooming Potong Kuku | Rp 25.000      | -                    |
| Grooming Full        | Rp 100.000     | + Rp 20.000          |
| Vaksinasi            | Rp 100.000     | + Rp 20.000          |
| Pakan Khusus         | Rp 50.000      | -                    |
| Checkup Umum         | Rp 75.000      | + Rp 20.000          |
| Rawat Inap           | Rp 150.000     | -                    |
| Bedah Minor          | Rp 250.000     | + Rp 20.000          |

---

## Lisensi

Proyek ini dibuat untuk keperluan studi kasus pengembangan aplikasi desktop Java Swing.
Bebas digunakan dan dimodifikasi untuk tujuan pembelajaran.

---

*BluePaw Vet & Grooming — Karena hewan peliharaan Anda layak mendapat perawatan terbaik.*
