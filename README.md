# PetCare Vet Clinic & Grooming

## Overview
Aplikasi desktop Java Swing untuk manajemen klinik dokter hewan & layanan grooming hewan peliharaan (**PetCare Vet Clinic & Grooming**). Menggunakan framework Look-and-Feel modern **FlatLaf** dengan implementasi **Custom Blue Palette** yang diatur via `UIManager.put(...)` sebelum inisialisasi tema, struktur navigasi multi-tab (**JTabbedPane**), sistem pembayaran interaktif (**Cash & QRIS Digital**), serta pencatatan riwayat pemesanan berbasis tabel (**JTable CRUD**).

---

## 🎨 Pembaruan Desain UI (Custom Blue Palette & UIManager Fix)

Untuk memastikan palet warna kustom tidak tertimpa oleh tema bawaan FlatLaf, seluruh pengaturan warna didaftarkan menggunakan `UIManager.put(...)` **sebelum** pemanggilan `FlatMacLightLaf.setup()` / inisialisasi frame utama.

### 1. Spesifikasi Palet Warna (Hex Code)
| Elemen UI | Hex Code | Properti FlatLaf / Peran |
|-----------|----------|--------------------------|
| **Background Utama** | `#FFFFFF` | `"RootPane.background"`, `"ScrollPane.background"`, `"TabbedPane.background"` |
| **Background Panel / Kartu** | `#c0e6fd` | `"Panel.background"`, `"TabbedPane.selectedBackground"` (Biru Terang) |
| **Border / Garis Pemisah** | `#80aad3` | `"Component.borderColor"`, `"Table.gridColor"`, `"Button.borderColor"` |
| **Elemen Sekunder** | `#5b86b6` | `"Component.focusColor"`, `"Separator.foreground"` |
| **Tombol Utama (Primary)** | `#3f6593` / `#1b3554` | `"Button.background"` & `"Button.hoverBackground"` (Teks: `#FFFFFF`) |
| **Teks Utama (Foreground)** | `#000f22` | `"Label.foreground"`, `"Table.foreground"`, `"TabbedPane.foreground"` |

---

## 📑 Struktur Layout Multi-Tab (`JTabbedPane`)

Aplikasi menggunakan antarmuka tab ganda untuk memisahkan alur transaksi aktif dengan arsip transaksi:

### Tab 1: "🐾 Registrasi Layanan"
- **Formulir Pasien & Tindakan**: Input nama pemilik, nama hewan, jenis hewan, bobot tubuh (kg), dokter jaga, paket grooming, dan checkbox layanan tambahan.
- **Sistem Pembayaran Dinamis**:
  - **Cash**: Menampilkan input "Uang Diterima" dan kalkulasi "Kembalian" secara real-time. Jika uang kurang, label berwarna merah dan tombol Cetak dinonaktifkan.
  - **QRIS**: Menampilkan kartu barcode QRIS (200x200 px proporsional) dan menyembunyikan input uang tunai.
- **Cetak Nota & Auto-Save**: Saat tombol "Cetak Nota" diklik, nota resmi ditampilkan, pemesanan otomatis dicatat ke Tab 2 (Riwayat Pemesanan), dan form di-reset bersih untuk pasien berikutnya.

### Tab 2: "📋 Riwayat Pemesanan" (Fitur Baru)
- **Komponen**: `JTable` di dalam `JScrollPane` menggunakan `DefaultTableModel`.
- **Kolom Tabel**:
  1. `ID/No` (Auto-generated: `ORD-001`, `ORD-002`, dst.)
  2. `Nama Pemilik`
  3. `Nama Hewan`
  4. `Layanan` (Rincian seluruh tindakan yang dipilih)
  5. `Total Biaya` (Format mata uang Rupiah)
  6. `Status Pembayaran` (Status: `Lunas (Cash)`, `Lunas (QRIS)`, atau `Selesai`)
- **Aksi Interaktif (Tombol di Bawah Tabel)**:
  - **✏️ Edit Pemesanan**: Mengambil data baris yang dipilih, mengembalikan seluruh input ke form Tab 1 agar dapat disesuaikan ulang, menghapus baris sementara dari tabel, dan memindahkan fokus user ke Tab 1.
  - **❌ Batal / Hapus**: Menghapus pemesanan yang dipilih dari tabel setelah konfirmasi dialog.
  - **✅ Selesai**: Mengubah nilai kolom status pemesanan menjadi `"Selesai"`.

---

## 🖼️ Panduan Pengelolaan Aset Gambar QRIS (`qris_dummy.png`)

### 1. Lokasi File Gambar
Secara default, file gambar barcode bernama **`qris_dummy.png`** diletakkan di root direktori proyek:
```
d:\StudyCase\Petcare\
├── qris_dummy.png        <-- [FILE ASET GAMBAR QRIS]
├── PetCareGUI.java
├── UIHelper.java
├── OrderRecord.java
├── PasienHewan.java
├── LayananGrooming.java
└── run.bat
```

### 2. Kustomisasi Path di Kode Sumber
Buka [PetCareGUI.java](file:///d:/StudyCase/Petcare/PetCareGUI.java) dan ubah konstanta berikut jika gambar dipindahkan:
```java
private static final String QRIS_IMAGE_PATH = "qris_dummy.png";
private static final int QRIS_BARCODE_WIDTH = 200;
private static final int QRIS_BARCODE_HEIGHT = 200;
```

---

## 📂 Struktur Proyek
```
d:\StudyCase\Petcare\
├── PetCareGUI.java        # Main JFrame: JTabbedPane, Form Registrasi, dan Riwayat JTable
├── UIHelper.java          # Utility FlatLaf, UIManager Color Overrides, & Image Scaling
├── OrderRecord.java       # Model Data Pemesanan untuk integrasi JTable & fungsi Edit
├── PasienHewan.java       # Model Data Pasien
├── LayananGrooming.java   # Model Data Layanan Grooming
├── qris_dummy.png         # Aset dummy QRIS Barcode (200x200 px)
├── README.md              # Dokumentasi lengkap
├── run.bat                # Batch launcher otomatis
└── lib/
    ├── flatlaf-3.2.5.jar
    └── flatlaf-intellij-themes-3.2.5.jar
```

---

## 🚀 Cara Menjalankan Aplikasi

Jalankan via launcher batch:
```cmd
run.bat
```
Atau manual melalui command prompt:
```cmd
javac -cp ".;lib/*" *.java
java -cp ".;lib/*" PetCareGUI
```
