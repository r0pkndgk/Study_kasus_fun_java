import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

@SuppressWarnings("unused")
public class PetCareGUI extends JFrame {
    // --- FORM INPUT FIELDS ---
    private JTextField txtOwner, txtPetName, txtWeight;
    private JComboBox<String> cbPetType, cbGrooming, cbDokter;
    private JCheckBox chkVaccine, chkFood, chkCheckup, chkBoarding, chkBedah;

    // --- PAYMENT FIELDS ---
    private JComboBox<String> cbPayment;
    private JTextField txtCashReceived;
    private JLabel lblCashReceivedLabel, lblKembalian, lblKembalianValue;

    // --- QRIS BARCODE ---
    private JLabel lblQrisBarcode;
    private JLabel lblQrisInstruction;
    private JPanel pnlQrisContainer;

    // --- SUMMARY FIELDS ---
    private JTextArea txtSummary;
    private JLabel lblTotal;
    private JButton btnCetak;

    // --- STATE ---
    private double currentTotal = 0;
    private PasienHewan currentPasien;
    private LayananGrooming currentGrooming;

    // Helper to keep track of the current row while building the form
    private int formRow = 0;

    public PetCareGUI() {
        // 1. Inisialisasi Tema FlatLaf Light
        UIHelper.setupFlatLaf();

        setTitle("PetCare Vet Clinic & Grooming - Dashboard");
        setSize(1050, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Root Panel
        JPanel rootPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        rootPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        rootPanel.setBackground(new Color(245, 247, 250)); // Light modern background
        setContentPane(rootPanel);

        // --- KIRI: FORM INPUT (GridBagLayout + explicit insets) ---
        JPanel pnlForm = new JPanel(new BorderLayout());
        pnlForm.setOpaque(false);

        // Panel input menggunakan GridBagLayout dengan insets eksplisit
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
                new EmptyBorder(20, 20, 20, 20)));
        pnlInput.putClientProperty("FlatLaf.style", "arc: 20");

        GridBagConstraints gbc = new GridBagConstraints();
        Insets defaultInsets = new Insets(8, 15, 8, 15); // top, left, bottom, right
        gbc.insets = defaultInsets;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        // Header di baris pertama
        gbc.gridx = 0;
        gbc.gridy = formRow;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.insets = new Insets(5, 15, 12, 15);
        gbc.fill = GridBagConstraints.NONE;
        JLabel lblHeaderForm = UIHelper.createHeaderLabel("\uD83D\uDC36 Registrasi Layanan");
        pnlInput.add(lblHeaderForm, gbc);
        gbc.gridwidth = 1; // reset
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = defaultInsets;
        formRow++;

        // Form rows: Data Pasien
        addFormRow(pnlInput, gbc, "Nama Pemilik", txtOwner = createFixedHeightField());
        addFormRow(pnlInput, gbc, "Nama Hewan", txtPetName = createFixedHeightField());
        addFormRow(pnlInput, gbc, "Jenis Hewan",
                cbPetType = createComboBox(new String[] { "Kucing", "Anjing", "Kelinci", "Burung", "Lainnya" }));
        addFormRow(pnlInput, gbc, "Bobot Hewan (kg)", txtWeight = createFixedHeightField());

        // [FITUR BARU 1] Pemilihan Dokter Hewan
        addFormRow(pnlInput, gbc, "Dokter Pemeriksa",
                cbDokter = createComboBox(new String[] { "Drh. Budi", "Drh. Sarah", "Drh. Andi" }));

        // Paket Grooming
        addFormRow(pnlInput, gbc, "Paket Grooming", cbGrooming = createComboBox(
                new String[] { "Tidak Ada", "Mandi Kutu", "Potong Bulu", "Potong Kuku", "Full Grooming" }));

        // [FITUR BARU 2] Layanan Tambahan (extended checkboxes)
        gbc.gridy = ++formRow;
        gbc.weightx = 1.0;
        gbc.insets = defaultInsets;

        // Panel wrapper untuk semua checkbox layanan (2 baris, 3 kolom)
        JPanel pnlExtras = new JPanel(new GridLayout(2, 3, 10, 6));
        pnlExtras.setOpaque(false);
        chkVaccine = new JCheckBox("Vaksinasi");
        chkFood = new JCheckBox("Pakan");
        chkCheckup = new JCheckBox("Checkup Umum");
        chkBoarding = new JCheckBox("Rawat Inap");
        chkBedah = new JCheckBox("Bedah Minor");

        Font checkFont = new Font("Inter", Font.PLAIN, 13);
        for (JCheckBox cb : new JCheckBox[] { chkVaccine, chkFood, chkCheckup, chkBoarding, chkBedah }) {
            cb.setFont(checkFont);
            cb.setOpaque(false);
            pnlExtras.add(cb);
        }
        addFormRow(pnlInput, gbc, "Layanan Tambahan", pnlExtras);

        // [FITUR BARU 3] Metode Pembayaran
        addFormRow(pnlInput, gbc, "Metode Pembayaran", cbPayment = createComboBox(new String[] { "Cash", "QRIS" }));

        // Field Uang Diterima (hanya aktif saat Cash)
        lblCashReceivedLabel = new JLabel("Uang Diterima (Rp)");
        txtCashReceived = createFixedHeightField();
        addFormRow(pnlInput, gbc, "Uang Diterima (Rp)", txtCashReceived);

        // Filler dengan weighty = 1.0 untuk mendorong form ke atas
        gbc.gridy = ++formRow;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = defaultInsets;
        pnlInput.add(new JLabel(), gbc);

        // Bungkus pnlInput dalam JScrollPane agar bisa di-scroll jika jendela kecil
        JScrollPane scrollForm = new JScrollPane(pnlInput);
        scrollForm.setBorder(null);
        scrollForm.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollForm.getVerticalScrollBar().setUnitIncrement(16);
        pnlForm.add(scrollForm, BorderLayout.CENTER);
        rootPanel.add(pnlForm);

        // --- KANAN: LIVE SUMMARY & INVOICE ---
        JPanel pnlRight = new JPanel(new BorderLayout(0, 15));
        pnlRight.setOpaque(false);

        JLabel lblHeaderSummary = UIHelper.createHeaderLabel("\uD83E\uDDFE Ringkasan Tagihan");
        lblHeaderSummary.setBorder(new EmptyBorder(0, 0, 0, 0));
        pnlRight.add(lblHeaderSummary, BorderLayout.NORTH);

        JPanel pnlSummaryCard = new JPanel(new BorderLayout());
        pnlSummaryCard.setBackground(Color.WHITE);
        pnlSummaryCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
                new EmptyBorder(25, 25, 25, 25)));
        pnlSummaryCard.putClientProperty("FlatLaf.style", "arc: 20");

        txtSummary = new JTextArea();
        txtSummary.setEditable(false);
        txtSummary.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtSummary.setForeground(new Color(60, 70, 80));
        txtSummary.setOpaque(false);
        txtSummary.setText("Silakan lengkapi form di sebelah kiri\nuntuk melihat ringkasan tagihan.");

        JScrollPane scrollSummary = new JScrollPane(txtSummary);
        scrollSummary.setBorder(null);
        scrollSummary.setOpaque(false);
        scrollSummary.getViewport().setOpaque(false);
        pnlSummaryCard.add(scrollSummary, BorderLayout.CENTER);

        // Bagian Bawah Summary (Total, Kembalian & Tombol)
        JPanel pnlBottomSummary = new JPanel();
        pnlBottomSummary.setLayout(new BoxLayout(pnlBottomSummary, BoxLayout.Y_AXIS));
        pnlBottomSummary.setOpaque(false);

        // Panel Total
        JPanel pnlTotal = new JPanel(new BorderLayout());
        pnlTotal.setOpaque(false);
        JLabel lblTotalTitle = new JLabel("TOTAL PEMBAYARAN");
        lblTotalTitle.setFont(new Font("Inter", Font.BOLD, 14));
        lblTotalTitle.setForeground(new Color(120, 130, 140));
        lblTotal = UIHelper.createTotalLabel();
        pnlTotal.add(lblTotalTitle, BorderLayout.NORTH);
        pnlTotal.add(lblTotal, BorderLayout.CENTER);
        pnlTotal.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));

        // Panel Kembalian
        JPanel pnlKembalian = new JPanel(new BorderLayout());
        pnlKembalian.setOpaque(false);
        pnlKembalian.setBorder(new EmptyBorder(8, 0, 8, 0));
        JLabel lblKembalianTitle = new JLabel("KEMBALIAN");
        lblKembalianTitle.setFont(new Font("Inter", Font.BOLD, 13));
        lblKembalianTitle.setForeground(new Color(120, 130, 140));
        lblKembalianValue = new JLabel("Rp 0");
        lblKembalianValue.setFont(new Font("Inter", Font.BOLD, 24));
        lblKembalianValue.setForeground(new Color(46, 172, 163));
        lblKembalianValue.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlKembalian.add(lblKembalianTitle, BorderLayout.NORTH);
        pnlKembalian.add(lblKembalianValue, BorderLayout.CENTER);

        // --- [FITUR BARU] Panel QRIS Barcode ---
        pnlQrisContainer = new JPanel();
        pnlQrisContainer.setLayout(new BoxLayout(pnlQrisContainer, BoxLayout.Y_AXIS));
        pnlQrisContainer.setOpaque(false);
        pnlQrisContainer.setBorder(new EmptyBorder(10, 0, 10, 0));
        pnlQrisContainer.setVisible(false); // Hidden by default (Cash mode)

        lblQrisInstruction = new JLabel("Silakan scan QRIS berikut untuk membayar:");
        lblQrisInstruction.setFont(new Font("Inter", Font.ITALIC, 13));
        lblQrisInstruction.setForeground(new Color(80, 90, 100));
        lblQrisInstruction.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlQrisContainer.add(lblQrisInstruction);
        pnlQrisContainer.add(Box.createVerticalStrut(10));

        // Memuat gambar QRIS dari file.
        // ============================================================
        // GANTI PATH DI BAWAH INI dengan lokasi file gambar QRIS Anda.
        // Contoh: "qris_dummy.png" → file di folder yang sama
        // "assets/qris_dummy.png" → file di subfolder assets/
        // ============================================================
        lblQrisBarcode = new JLabel();
        lblQrisBarcode.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblQrisBarcode.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon qrisIcon = loadScaledIcon("qris_dummy.png", 200, 200);
        if (qrisIcon != null) {
            lblQrisBarcode.setIcon(qrisIcon);
        } else {
            // Fallback jika file gambar tidak ditemukan
            lblQrisBarcode.setText("[Gambar QRIS tidak ditemukan]");
            lblQrisBarcode.setFont(new Font("Inter", Font.PLAIN, 12));
            lblQrisBarcode.setForeground(new Color(180, 50, 50));
            lblQrisBarcode.setPreferredSize(new Dimension(200, 200));
            lblQrisBarcode.setBorder(BorderFactory.createDashedBorder(new Color(180, 50, 50), 2, 4, 4, true));
        }
        pnlQrisContainer.add(lblQrisBarcode);

        btnCetak = UIHelper.createPrimaryButton("Cetak Nota");
        btnCetak.setEnabled(false); // Disabled by default

        pnlBottomSummary.add(pnlTotal);
        pnlBottomSummary.add(pnlKembalian);
        pnlBottomSummary.add(pnlQrisContainer);
        pnlBottomSummary.add(Box.createVerticalStrut(8));
        pnlBottomSummary.add(btnCetak);

        pnlSummaryCard.add(pnlBottomSummary, BorderLayout.SOUTH);
        pnlRight.add(pnlSummaryCard, BorderLayout.CENTER);
        rootPanel.add(pnlRight);

        // --- SETUP LISTENER (INTERAKTIVITAS REAL-TIME) ---
        setupRealTimeListeners();

        // Cetak Action
        btnCetak.addActionListener(e -> {
            String payMethod = cbPayment.getSelectedItem().toString();
            String kembalianStr = "";
            if (payMethod.equals("Cash")) {
                double cash = parseCash();
                double kembalian = cash - currentTotal;
                kembalianStr = String.format("Uang Diterima : Rp %,.0f\nKembalian      : Rp %,.0f", cash, kembalian);
            } else {
                kembalianStr = "Pembayaran    : QRIS (Lunas)";
            }
            JOptionPane.showMessageDialog(this,
                    "Nota berhasil dicetak!\n\n" + txtSummary.getText()
                            + "\n" + kembalianStr
                            + "\n\nTotal: Rp " + String.format("%,.0f", currentTotal),
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    /**
     * Utility method to create a JTextField with a fixed maximum height
     * so it only expands horizontally.
     */
    private JTextField createFixedHeightField() {
        JTextField field = new JTextField();
        Dimension pref = field.getPreferredSize();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
        return field;
    }

    /**
     * Utility method to create a JComboBox with a fixed maximum height.
     */
    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        Dimension pref = combo.getPreferredSize();
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
        return combo;
    }

    /**
     * Adds a label and its corresponding input component to the form using
     * GridBagLayout.
     * The method automatically advances the internal row counter.
     */
    private void addFormRow(JPanel panel, GridBagConstraints gbc, String labelText, JComponent comp) {
        // Label
        gbc.gridx = 0;
        gbc.gridy = formRow;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel label = new JLabel(labelText);
        UIHelper.styleFormLabel(label);
        panel.add(label, gbc);

        // Input component
        gbc.gridx = 0;
        gbc.gridy = ++formRow;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 10, 10); // vertical spacing between rows
        panel.add(comp, gbc);
        // Reset insets for next label
        gbc.insets = new Insets(8, 15, 8, 15);
        formRow++;
    }

    /**
     * Parse the cash received field safely, returning 0 if invalid.
     */
    private double parseCash() {
        try {
            String text = txtCashReceived.getText().trim().replace(",", "").replace(".", "");
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Memuat gambar dari file dan me-resize ke ukuran yang ditentukan.
     * Menggunakan SCALE_SMOOTH untuk kualitas scaling terbaik.
     *
     * @param path   Path file gambar (relatif terhadap working directory atau
     *               absolut)
     * @param width  Lebar target dalam pixel
     * @param height Tinggi target dalam pixel
     * @return ImageIcon yang sudah di-scale, atau null jika file tidak ditemukan
     */
    private ImageIcon loadScaledIcon(String path, int width, int height) {
        try {
            // Coba muat dari classpath terlebih dahulu
            java.net.URL url = getClass().getResource("/" + path);
            ImageIcon rawIcon;
            if (url != null) {
                rawIcon = new ImageIcon(url);
            } else {
                // Fallback: muat dari filesystem (working directory)
                java.io.File file = new java.io.File(path);
                if (!file.exists())
                    return null;
                rawIcon = new ImageIcon(file.getAbsolutePath());
            }
            Image scaled = rawIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar QRIS: " + e.getMessage());
            return null;
        }
    }

    /**
     * Setup all real-time listeners for form inputs.
     * Listeners call calculateTotal() which updates both the summary and the
     * payment section.
     */
    private void setupRealTimeListeners() {
        // Document Listener untuk semua input teks
        DocumentListener docListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                calculateTotal();
            }

            public void removeUpdate(DocumentEvent e) {
                calculateTotal();
            }

            public void changedUpdate(DocumentEvent e) {
                calculateTotal();
            }
        };
        txtOwner.getDocument().addDocumentListener(docListener);
        txtPetName.getDocument().addDocumentListener(docListener);
        txtWeight.getDocument().addDocumentListener(docListener);

        // DocumentListener khusus untuk field "Uang Diterima" → update kembalian
        DocumentListener cashListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateKembalian();
            }

            public void removeUpdate(DocumentEvent e) {
                updateKembalian();
            }

            public void changedUpdate(DocumentEvent e) {
                updateKembalian();
            }
        };
        txtCashReceived.getDocument().addDocumentListener(cashListener);

        // Action Listener untuk semua combo box & checkbox
        cbPetType.addActionListener(e -> calculateTotal());
        cbGrooming.addActionListener(e -> calculateTotal());
        cbDokter.addActionListener(e -> calculateTotal());
        chkVaccine.addActionListener(e -> calculateTotal());
        chkFood.addActionListener(e -> calculateTotal());
        chkCheckup.addActionListener(e -> calculateTotal());
        chkBoarding.addActionListener(e -> calculateTotal());
        chkBedah.addActionListener(e -> calculateTotal());

        // Listener untuk metode pembayaran (Cash / QRIS toggle)
        cbPayment.addActionListener(e -> {
            boolean isCash = cbPayment.getSelectedItem().toString().equals("Cash");

            // Toggle visibilitas: Cash fields vs QRIS barcode
            txtCashReceived.setEnabled(isCash);
            txtCashReceived.setEditable(isCash);
            txtCashReceived.setVisible(isCash);
            pnlQrisContainer.setVisible(!isCash);

            if (!isCash) {
                // QRIS mode: kosongkan input cash, kembalian = 0
                txtCashReceived.setText("");
                lblKembalianValue.setText("Rp 0");
                lblKembalianValue.setForeground(new Color(46, 172, 163));
            }
            updateKembalian();

            // Revalidate layout agar perubahan visibilitas langsung terlihat
            pnlQrisContainer.getParent().revalidate();
            pnlQrisContainer.getParent().repaint();
        });
    }

    /**
     * Kalkulasi kembalian berdasarkan uang diterima dan total tagihan.
     * Menampilkan peringatan visual jika uang tidak cukup.
     */
    private void updateKembalian() {
        String payMethod = cbPayment.getSelectedItem().toString();

        if (payMethod.equals("QRIS")) {
            lblKembalianValue.setText("Rp 0");
            lblKembalianValue.setForeground(new Color(46, 172, 163));
            // QRIS: tombol cetak mengikuti validasi form saja
            revalidateCetakButton();
            return;
        }

        // Cash mode
        double cash = parseCash();
        if (txtCashReceived.getText().trim().isEmpty()) {
            lblKembalianValue.setText("Rp 0");
            lblKembalianValue.setForeground(new Color(46, 172, 163));
            btnCetak.setEnabled(false);
            return;
        }

        double kembalian = cash - currentTotal;
        if (kembalian < 0) {
            lblKembalianValue.setText("Uang Tidak Cukup!");
            lblKembalianValue.setForeground(new Color(220, 50, 50)); // merah
            btnCetak.setEnabled(false);
        } else {
            lblKembalianValue.setText(String.format("Rp %,.0f", kembalian));
            lblKembalianValue.setForeground(new Color(46, 172, 163));
            revalidateCetakButton();
        }
    }

    /**
     * Revalidasi apakah tombol Cetak boleh aktif.
     * Tombol aktif jika form valid DAN (QRIS dipilih ATAU cash cukup).
     */
    private void revalidateCetakButton() {
        if (!isFormValid()) {
            btnCetak.setEnabled(false);
            return;
        }
        String payMethod = cbPayment.getSelectedItem().toString();
        if (payMethod.equals("Cash")) {
            double cash = parseCash();
            btnCetak.setEnabled(cash >= currentTotal && currentTotal > 0);
        } else {
            // QRIS: langsung enable jika form valid dan ada layanan
            btnCetak.setEnabled(currentTotal > 0);
        }
    }

    /**
     * Cek validitas dasar form (nama owner, nama hewan, bobot valid).
     */
    private boolean isFormValid() {
        String owner = txtOwner.getText().trim();
        String petName = txtPetName.getText().trim();
        String weightText = txtWeight.getText().trim();

        if (owner.isEmpty() || petName.isEmpty() || weightText.isEmpty())
            return false;
        try {
            double w = Double.parseDouble(weightText);
            return w > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Kalkulasi total harga dan update ringkasan tagihan secara real-time.
     * Method ini dipanggil oleh semua listener (Document, Action, Item).
     */
    private void calculateTotal() {
        String owner = txtOwner.getText().trim();
        String petName = txtPetName.getText().trim();
        String weightText = txtWeight.getText().trim();

        boolean isValid = true;

        // Reset visual validation
        UIHelper.setInvalidBorder(txtWeight, false);
        UIHelper.setInvalidBorder(txtOwner, false);
        UIHelper.setInvalidBorder(txtPetName, false);

        if (owner.isEmpty()) {
            UIHelper.setInvalidBorder(txtOwner, true);
            isValid = false;
        }
        if (petName.isEmpty()) {
            UIHelper.setInvalidBorder(txtPetName, true);
            isValid = false;
        }

        double weight = 0;
        if (weightText.isEmpty()) {
            UIHelper.setInvalidBorder(txtWeight, true);
            isValid = false;
        } else {
            try {
                weight = Double.parseDouble(weightText);
                if (weight <= 0) {
                    UIHelper.setInvalidBorder(txtWeight, true);
                    isValid = false;
                }
            } catch (NumberFormatException ex) {
                UIHelper.setInvalidBorder(txtWeight, true);
                isValid = false;
            }
        }

        if (!isValid) {
            btnCetak.setEnabled(false);
            currentTotal = 0;
            lblTotal.setText("Rp 0");
            lblKembalianValue.setText("Rp 0");
            lblKembalianValue.setForeground(new Color(46, 172, 163));
            txtSummary.setText("Data belum lengkap atau format tidak valid.\nSilakan lengkapi form dengan benar.");
            return;
        }

        // --- KALKULASI BIAYA ---
        String petType = cbPetType.getSelectedItem().toString();
        String dokter = cbDokter.getSelectedItem().toString();
        currentPasien = new PasienHewan(petName, petType, owner, weight);

        // Grooming
        String selectedGrooming = cbGrooming.getSelectedItem().toString();
        double baseGroomingCost = 0;
        switch (selectedGrooming) {
            case "Mandi Kutu":
                baseGroomingCost = 50000;
                break;
            case "Potong Bulu":
                baseGroomingCost = 40000;
                break;
            case "Potong Kuku":
                baseGroomingCost = 25000;
                break;
            case "Full Grooming":
                baseGroomingCost = 100000;
                break;
        }
        double weightSurcharge = (weight >= 5.0) ? 20000 : 0;
        double groomingCost = 0;
        if (baseGroomingCost > 0) {
            groomingCost = baseGroomingCost + weightSurcharge;
            currentGrooming = new LayananGrooming(selectedGrooming, groomingCost);
        } else {
            currentGrooming = null;
        }

        // Layanan lama
        double vaccineCost = chkVaccine.isSelected() ? (100000 + weightSurcharge) : 0;
        double foodCost = chkFood.isSelected() ? 50000 : 0;

        // [FITUR BARU 2] Layanan tambahan baru (surcharge berlaku pada checkup & bedah)
        double checkupCost = chkCheckup.isSelected() ? (75000 + weightSurcharge) : 0;
        double boardingCost = chkBoarding.isSelected() ? 150000 : 0;
        double bedahCost = chkBedah.isSelected() ? (250000 + weightSurcharge) : 0;

        currentTotal = groomingCost + vaccineCost + foodCost + checkupCost + boardingCost + bedahCost;

        // --- UPDATE SUMMARY UI ---
        StringBuilder sb = new StringBuilder();
        sb.append("PASIEN:\n");
        sb.append(String.format("  Nama    : %s (%s)\n", currentPasien.getNamaPeliharaan(),
                currentPasien.getJenisHewan()));
        sb.append(String.format("  Owner   : %s\n", currentPasien.getNamaOwner()));
        sb.append(String.format("  Bobot   : %.1f kg\n", currentPasien.getBobotKg()));
        sb.append(String.format("  Dokter  : %s\n\n", dokter));

        sb.append("RINCIAN BIAYA:\n");
        if (currentGrooming != null) {
            sb.append(String.format("  %-22s: Rp %,10.0f\n", "Grooming (" + currentGrooming.getPaket() + ")",
                    currentGrooming.getBiayaLayanan()));
        }
        if (chkVaccine.isSelected()) {
            sb.append(String.format("  %-22s: Rp %,10.0f\n", "Vaksinasi", vaccineCost));
        }
        if (chkFood.isSelected()) {
            sb.append(String.format("  %-22s: Rp %,10.0f\n", "Pakan", foodCost));
        }
        if (chkCheckup.isSelected()) {
            sb.append(String.format("  %-22s: Rp %,10.0f\n", "Checkup Umum", checkupCost));
        }
        if (chkBoarding.isSelected()) {
            sb.append(String.format("  %-22s: Rp %,10.0f\n", "Rawat Inap", boardingCost));
        }
        if (chkBedah.isSelected()) {
            sb.append(String.format("  %-22s: Rp %,10.0f\n", "Bedah Minor", bedahCost));
        }

        // Keterangan surcharge
        boolean hasSurchargeableService = (currentGrooming != null || chkVaccine.isSelected()
                || chkCheckup.isSelected() || chkBedah.isSelected());
        if (weight >= 5.0 && hasSurchargeableService) {
            sb.append("\n*Surcharge >5kg diterapkan pada layanan medis/grooming.");
        }

        // Info pembayaran
        String payMethod = cbPayment.getSelectedItem().toString();
        sb.append("\n\nMETODE PEMBAYARAN: ").append(payMethod);

        txtSummary.setText(sb.toString());
        lblTotal.setText(String.format("Rp %,.0f", currentTotal));

        // Update kembalian setelah total berubah
        updateKembalian();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PetCareGUI().setVisible(true));
    }
}
