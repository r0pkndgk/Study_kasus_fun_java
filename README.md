# PetCare Vet Clinic & Grooming

## Overview
Aplikasi desktop Java Swing untuk manajemen klinik dokter hewan & layanan grooming hewan peliharaan (**PetCare Vet Clinic & Grooming**). Menggunakan framework Look-and-Feel modern **FlatLaf** dengan perombakan antarmuka pengguna berbasis **Custom Blue Palette** bergaya website klinik hewan profesional, kalkulasi tagihan invoice real-time, serta sistem pembayaran interaktif dinamis (**Cash** dan **QRIS Digital**).

---

## 🎨 Pembaruan Desain UI (Custom Blue Palette)

Aplikasi telah direfaktor dengan palet warna khusus bertema estetika klinik medis modern yang bersih, lega (*spacious card-style*), dan elegan.

### 1. Spesifikasi Palet Warna (Hex Code)
| Elemen UI | Hex Code | Peran & Deskripsi |
|-----------|----------|-------------------|
| **Background Panel Utama** | `#FFFFFF` | Latar belakang kartu (*Card*) form input & ringkasan tagihan. |
| **Aksen Kartu & Header** | `#c0e6fd` | Biru paling terang untuk aksen header kartu dan kontainer kartu QRIS. |
| **Background Dashboard** | `#F0F6FA` | Latar belakang dasar jendela aplikasi agar kartu terlihat kontras. |
| **Border & Pemisah** | `#80aad3` | Garis tepi (*border*) panel kartu, separator tagihan, dan input focus. |
| **Elemen Sekunder** | `#5b86b6` | Garis penegas dan label kategori sekunder. |
| **Tombol Utama (Primary)** | `#3f6593` / `#1b3554` | Biru dominan tombol "Cetak Nota" dengan teks putih murni (`#FFFFFF`). |
| **Teks Utama (Headings)** | `#000f22` | Biru paling gelap untuk judul, label penting, dan rincian biaya (bukan hitam murni). |

### 2. Hirarki Tipografi & Styling FlatLaf
- **Font Family**: Menggunakan font modern Sans-Serif (`Segoe UI` / `Inter`) yang tajam dan nyaman dibaca.
- **Heading & Label**: Judul bagian ("🐾 Registrasi Layanan", "🧾 Ringkasan Tagihan") berukuran 22pt Bold dengan warna `#000f22`. Label form berukuran 13pt Bold.
- **Rounded Corners**: Memanfaatkan properti FlatLaf (`Button.arc: 16`, `Component.arc: 14`, `FlatLaf.style: "arc: 20"`, serta `JButton.buttonType: "roundRect"`).
- **Spacious Spacing**: Menggunakan kombinasi `EmptyBorder` dan padding luas pada setiap kartu agar antarmuka tidak terasa padat atau sesak.

---

## 💳 Fitur Pembayaran Dinamis (Cash vs QRIS)

Sistem pembayaran mendukung dua mode transaksi dengan perubahan visibilitas komponen secara instan (*real-time reactive UI*):

### 1. Logika Interaktif Visibilitas
```
┌─────────────────────────────────────────────────────────────┐
│  Metode Pembayaran: [  Cash  ▼]                             │
│                                                             │
│  [✓] Input Uang Diterima (Rp) : DITAMPILKAN                 │
│  [✓] Label & Nilai Kembalian  : DITAMPILKAN                 │
│  [✗] Kartu Barcode QRIS       : DISEMBUNYIKAN               │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│  Metode Pembayaran: [  QRIS  ▼]                             │
│                                                             │
│  [✗] Input Uang Diterima (Rp) : DISEMBUNYIKAN               │
│  [✗] Label & Nilai Kembalian  : DISEMBUNYIKAN               │
│  [✓] Kartu Barcode QRIS       : DITAMPILKAN                 │
│      - Pesan: "Silakan scan QRIS berikut"                   │
│      - Gambar Barcode QRIS (200x200 px proporsional)        │
└─────────────────────────────────────────────────────────────┘
```

- **Jika memilih "QRIS"**:
  - Kartu Barcode QRIS (`pnlQrisCard`) muncul di bawah total tagihan dengan latar belakang `#c0e6fd` dan border `#80aad3`.
  - Teks instruksi *"Silakan scan QRIS berikut"* dan sub-teks e-Wallet / Mobile Banking ditampilkan.
  - Form input *"Uang Diterima"* dan panel *"Kembalian"* otomatis disembunyikan.
  - Tombol *"Cetak Nota"* langsung aktif jika data form lengkap dan tagihan > Rp 0.

- **Jika memilih "Cash"**:
  - Kartu Barcode QRIS disembunyikan.
  - Form input *"Uang Diterima"* dan panel *"Kembalian"* ditampilkan.
  - Kembalian dihitung otomatis secara real-time via `DocumentListener`. Jika uang kurang, label berubah menjadi *"Uang Kurang!"* berwarna merah dan tombol Cetak Nota dinonaktifkan.

---

## 🖼️ Panduan Pengelolaan Aset Gambar QRIS (`qris_dummy.png`)

### 1. Lokasi Menaruh File Aset Gambar
Secara default, aplikasi mencari file gambar dummy bernama **`qris_dummy.png`** langsung pada direktori utama (root) proyek:
```
d:\StudyCase\Petcare\
├── qris_dummy.png        <-- [TEMPAT FILE GAMBAR QRIS DEFAULT]
├── PetCareGUI.java
├── UIHelper.java
├── PasienHewan.java
├── LayananGrooming.java
└── run.bat
```

Anda juga dapat membuat folder khusus seperti `assets/` atau `images/` dan meletakkan gambar di sana (misal: `assets/qris_dummy.png`).

### 2. Cara Mengubah Path Gambar di Source Code
Untuk mengubah path file gambar QRIS, buka file **`PetCareGUI.java`** dan sesuaikan konstanta berikut pada bagian atas kelas:

```java
// =========================================================================
// KONFIGURASI ASET GAMBAR QRIS (PetCareGUI.java)
// =========================================================================
// Ganti nilai string di bawah ini sesuai lokasi file gambar Anda:
// Contoh jika di folder root   : "qris_dummy.png"
// Contoh jika di subfolder     : "assets/qris_dummy.png"
// Contoh path absolut Windows  : "C:/images/qris_clinic.png"
private static final String QRIS_IMAGE_PATH = "qris_dummy.png";
private static final int QRIS_BARCODE_WIDTH = 200;
private static final int QRIS_BARCODE_HEIGHT = 200;
```

### 3. Image Scaling Proporsional
Aplikasi menyediakan metode utilitas `UIHelper.loadAndScaleImage(path, width, height)` dan `UIHelper.scaleImage(...)` yang menggunakan algoritma `Image.SCALE_SMOOTH`:
```java
// Utilitas otomatis me-resize gambar ke 200x200 pixel tanpa distorsi:
ImageIcon scaledIcon = UIHelper.loadAndScaleImage(QRIS_IMAGE_PATH, 200, 200);
lblQrisBarcode.setIcon(scaledIcon);
```

> **Fallback Visual:** Jika file gambar tidak ditemukan pada path yang ditentukan, aplikasi tidak akan crash, melainkan menampilkan kotak placeholder elegan dengan garis putus-putus (*dashed border*) bertuliskan informasi lokasi file yang dicari.

---

## 📂 Struktur Proyek
```
d:\StudyCase\Petcare\
├── PetCareGUI.java        # Main Dashboard GUI (Palet Biru & Logika QRIS/Cash)
├── UIHelper.java          # Helper tema FlatLaf, warna Hex, & Image Scaling
├── PasienHewan.java       # Model data pasien hewan & pemilik
├── LayananGrooming.java   # Model data paket grooming
├── qris_dummy.png         # Aset dummy QRIS Barcode (200x200 px)
├── README.md              # Dokumentasi lengkap proyek
├── run.bat                # Script batch launcher
└── lib/
    ├── flatlaf-3.2.5.jar  # Library FlatLaf Core
    └── flatlaf-intellij-themes-3.2.5.jar
```

---

## 💉 Daftar Layanan & Tarif

| Layanan | Biaya Dasar | Surcharge Bobot (>5kg) | Keterangan |
|---------|------------|------------------------|------------|
| **Grooming: Mandi Kutu** | Rp 50.000 | +Rp 20.000 | Termasuk shampo anti-kutu |
| **Grooming: Potong Bulu** | Rp 40.000 | +Rp 20.000 | Perapian styling bulu |
| **Grooming: Potong Kuku** | Rp 25.000 | +Rp 20.000 | Perawatan kuku steril |
| **Grooming: Full Grooming**| Rp 100.000 | +Rp 20.000 | Paket lengkap menyeluruh |
| **Vaksinasi** | Rp 100.000 | +Rp 20.000 | Vaksin tahunan / rabies |
| **Pakan Khusus** | Rp 50.000 | — | Pakan nutrisi klinis |
| **Checkup Umum** | Rp 75.000 | +Rp 20.000 | Pemeriksaan fisik dokter |
| **Rawat Inap** | Rp 150.000 | — | Monitoring 24 jam di klinik |
| **Bedah Minor** | Rp 250.000 | +Rp 20.000 | Sterilisasi & jahit luka |

---

## 🚀 Cara Menjalankan Aplikasi

1. **Jalankan via Script Otomatis:**
   Cukup klik dua kali atau jalankan file batch:
   ```cmd
   run.bat
   ```

2. **Jalankan via Terminal/Command Prompt Manual:**
   ```cmd
   javac -cp ".;lib/*" *.java
   java -cp ".;lib/*" PetCareGUI
   ```

---

## 📋 Ringkasan Perubahan (Changelog v3.0)
- **UI Modernization**: Implementasi penuh **Custom Blue Palette** (`#FFFFFF`, `#c0e6fd`, `#80aad3`, `#5b86b6`, `#3f6593`, `#1b3554`, `#000f22`).
- **Dynamic QRIS Payment**: Penambahan kartu barcode QRIS dengan `JLabel` (`lblQrisBarcode`), instruksi dinamis, dan penyembunyian form uang tunai saat QRIS aktif.
- **Image Scaling Utility**: Utilitas me-resize gambar secara proporsional berukuran 200×200 pixel menggunakan `Image.SCALE_SMOOTH`.
- **Documentation**: Penambahan panduan penempatan file aset gambar QRIS dan cara kustomisasi path di `PetCareGUI.java`.
