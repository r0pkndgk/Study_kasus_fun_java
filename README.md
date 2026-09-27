# PetCare Vet Clinic & Grooming

## Overview
A Java Swing desktop application for managing pet clinic & grooming services. Features real-time invoice calculation, veterinarian selection, expanded service options, and a Cash/QRIS payment system. The UI uses **FlatLaf** for a modern, clean look with `GridBagLayout` for precise component placement.

---

## 📂 Project Structure
```
PetCareGUI.java        # Main Swing UI with all 3 new features
UIHelper.java          # Helper utilities for FlatLaf styling
PasienHewan.java       # Model: pet owner & pet data
LayananGrooming.java   # Model: grooming package & cost
README.md              # This documentation
run.bat                # Simple script to compile & launch
lib/
  flatlaf-3.2.5.jar    # FlatLaf Look-and-Feel library
  flatlaf-intellij-themes-3.2.5.jar
```

---

## ✨ Fitur Baru (v2.0)

### 1. Pemilihan Dokter Hewan
- **Komponen**: `JComboBox` berlabel "Dokter Pemeriksa"
- **Pilihan**: Drh. Budi, Drh. Sarah, Drh. Andi
- **Integrasi**: Nama dokter yang dipilih ditampilkan di Ringkasan Tagihan dan ikut tercetak di Nota

### 2. Variasi Layanan Tambahan
Selain Grooming, Vaksinasi, dan Pakan yang sudah ada, kini tersedia 3 layanan baru:

| Layanan | Biaya Dasar | Surcharge >5kg |
|---------|------------|----------------|
| Grooming (Mandi Kutu) | Rp 50.000 | +Rp 20.000 |
| Grooming (Potong Bulu) | Rp 40.000 | +Rp 20.000 |
| Grooming (Potong Kuku) | Rp 25.000 | +Rp 20.000 |
| Grooming (Full Grooming) | Rp 100.000 | +Rp 20.000 |
| Vaksinasi | Rp 100.000 | +Rp 20.000 |
| Pakan | Rp 50.000 | — |
| **Checkup Umum** _(baru)_ | Rp 75.000 | +Rp 20.000 |
| **Rawat Inap** _(baru)_ | Rp 150.000 | — |
| **Bedah Minor** _(baru)_ | Rp 250.000 | +Rp 20.000 |

- Semua checkbox layanan tersusun dalam grid 2×3 yang rapi
- Surcharge bobot >5kg berlaku otomatis pada layanan medis/grooming (Vaksinasi, Checkup, Bedah Minor, dan semua paket Grooming)
- Rawat Inap dan Pakan **tidak** terkena surcharge bobot

### 3. Sistem Pembayaran (Cash / QRIS)
**Komponen**: `JComboBox` "Metode Pembayaran" dengan pilihan Cash dan QRIS

#### Logika Interaktif:
```
┌─────────────────────────────────────────────────┐
│  Metode Pembayaran: [  Cash  ▼]                 │
│  Uang Diterima:     [  150000  ]    ← aktif     │
│                                                 │
│  KEMBALIAN:  Rp 25.000  (hijau)                 │
│  [Cetak Nota]  ← aktif                          │
└─────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────┐
│  Metode Pembayaran: [  QRIS  ▼]                 │
│  Uang Diterima:     [         ]    ← disabled   │
│                                                 │
│  KEMBALIAN:  Rp 0                               │
│  [Cetak Nota]  ← aktif                          │
└─────────────────────────────────────────────────┘
```

- **QRIS dipilih**: Field "Uang Diterima" di-disable dan dikosongkan. Kembalian otomatis Rp 0. Tombol "Cetak Nota" aktif selama form valid.
- **Cash dipilih**: Field "Uang Diterima" aktif. Kembalian dihitung real-time via `DocumentListener`:
  - `Kembalian = Uang Diterima - Total Tagihan`
  - Jika uang kurang → teks **"Uang Tidak Cukup!"** berwarna **merah**, tombol "Cetak Nota" **disabled**
  - Jika uang cukup → kembalian ditampilkan normal berwarna hijau teal

---

## 🔧 Arsitektur Kode

### Separation of Concerns
Logika kalkulasi dipisahkan ke method-method berikut:

| Method | Tanggung Jawab |
|--------|---------------|
| `calculateTotal()` | Kalkulasi total biaya dari semua layanan yang dipilih, update ringkasan tagihan |
| `updateKembalian()` | Kalkulasi kembalian (Cash) atau reset (QRIS), update visual kembalian |
| `revalidateCetakButton()` | Validasi apakah tombol Cetak boleh aktif berdasarkan form + pembayaran |
| `isFormValid()` | Cek kelengkapan & validitas data dasar (nama, bobot) |
| `parseCash()` | Parse input uang diterima dengan aman (return 0 jika invalid) |

### Listener Chain
```
Input berubah
    ↓
DocumentListener / ActionListener
    ↓
calculateTotal()          → hitung total, update ringkasan
    ↓
updateKembalian()         → hitung kembalian, validasi cash
    ↓
revalidateCetakButton()   → enable/disable tombol cetak
```

### Layout
- Form kiri menggunakan `GridBagLayout` dengan `Insets(8, 15, 8, 15)` default
- Checkbox layanan menggunakan `GridLayout(2, 3, 10, 6)` untuk susunan 2 baris × 3 kolom
- Form dibungkus `JScrollPane` agar tetap bisa di-scroll pada jendela kecil
- Panel kanan menggunakan `BorderLayout` dengan `BoxLayout` vertikal untuk bagian bawah (Total → Kembalian → Tombol)

---

## 🐞 Original Issue (v1.0)
Panel kiri "Registrasi Layanan" mengalami **overlapping komponen**:
- `JLabel`, `JTextField`, dan `JComboBox` bertumpuk tanpa spacing
- Sudah diperbaiki dengan `GridBagLayout` + explicit `Insets` + helper `addFormRow()`

---

## 📖 How to Run
```bat
run.bat
```
Script ini akan mengkompilasi dan menjalankan aplikasi:
```bat
javac -cp ".;lib/*" *.java
java -cp ".;lib/*" PetCareGUI
```
Pastikan folder `lib/` berisi **FlatLaf JAR** (`flatlaf-3.2.5.jar`).

---

## ✅ Changelog

### v2.0 (Current)
| File | Perubahan |
|------|-----------|
| `PetCareGUI.java` | Tambah JComboBox "Dokter Pemeriksa" (Drh. Budi/Sarah/Andi). Tambah 3 layanan baru: Checkup Umum, Rawat Inap, Bedah Minor. Implementasi sistem pembayaran Cash/QRIS dengan kalkulasi kembalian real-time. Refactor logika ke `calculateTotal()`, `updateKembalian()`, `revalidateCetakButton()`. Form dibungkus JScrollPane. |
| `README.md` | Dokumentasi lengkap fitur baru, tabel harga, diagram logika, dan arsitektur kode. |

### v1.0
| File | Perubahan |
|------|-----------|
| `PetCareGUI.java` | Refactor panel kiri ke `GridBagLayout` dengan insets, helper `addFormRow()`, fixed-height fields. |
| `README.md` | Dokumentasi awal: masalah overlapping & solusi layout. |

---

## 🎨 Visual Preview
```
┌──────────────────────────────┬──────────────────────────────┐
│ 🐶 Registrasi Layanan       │ 🧾 Ringkasan Tagihan        │
│                              │                              │
│ Nama Pemilik  [___________]  │ PASIEN:                      │
│ Nama Hewan    [___________]  │   Nama  : Mochi (Kucing)     │
│ Jenis Hewan   [Kucing    ▼]  │   Owner : Andi               │
│ Bobot (kg)    [___________]  │   Bobot : 3.5 kg             │
│ Dokter        [Drh. Budi ▼]  │   Dokter: Drh. Budi          │
│ Grooming      [Full Groom▼]  │                              │
│                              │ RINCIAN BIAYA:               │
│ Layanan Tambahan:            │   Grooming (Full)  Rp 100.000│
│ [✓] Vaksinasi  [ ] Pakan     │   Vaksinasi        Rp 100.000│
│ [✓] Checkup    [ ] Boarding  │   Checkup Umum     Rp  75.000│
│ [ ] Bedah Minor              │                              │
│                              │ METODE: Cash                 │
│ Pembayaran    [Cash      ▼]  │                              │
│ Uang Diterima [  300000   ]  │ TOTAL PEMBAYARAN             │
│                              │              Rp 275.000      │
│                              │ KEMBALIAN                    │
│                              │              Rp  25.000      │
│                              │ [    Cetak Nota    ]         │
└──────────────────────────────┴──────────────────────────────┘
```

---

*Dokumentasi ini otomatis diperbarui setiap kali fitur baru ditambahkan.*
