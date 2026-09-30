import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

/**
 * PetCare Vet Clinic & Grooming - Management Dashboard
 * Versi Multi-Tab (JTabbedPane) dengan Custom Blue Palette FlatLaf
 * dan Manajemen Riwayat Pemesanan (CRUD: Tambah, Edit, Hapus, Selesai).
 * Mengikuti prinsip Clean Code, Zero Warnings, dan Java Best Practices.
 */
public final class PetCareGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    // =========================================================================
    // KONFIGURASI ASET GAMBAR QRIS
    // =========================================================================
    private static final String QRIS_IMAGE_PATH = "qris_dummy.png";
    private static final int QRIS_BARCODE_WIDTH = 200;
    private static final int QRIS_BARCODE_HEIGHT = 200;

    // --- MAIN CONTAINER ---
    private JTabbedPane tabbedPane;

    // --- TAB 1: FORM INPUT FIELDS ---
    private JTextField txtOwner;
    private JTextField txtPetName;
    private JTextField txtWeight;
    private JComboBox<String> cbPetType;
    private JComboBox<String> cbGrooming;
    private JComboBox<String> cbDokter;
    private JCheckBox chkVaccine;
    private JCheckBox chkFood;
    private JCheckBox chkCheckup;
    private JCheckBox chkBoarding;
    private JCheckBox chkBedah;

    // --- TAB 1: PAYMENT FIELDS ---
    private JComboBox<String> cbPayment;
    private JPanel pnlCashInputRow;
    private JLabel lblCashReceivedLabel;
    private JTextField txtCashReceived;
    private JPanel pnlKembalian;
    private JLabel lblKembalianValue;

    // --- TAB 1: QRIS BARCODE COMPONENTS ---
    private JPanel pnlQrisCard;
    private JLabel lblQrisBarcode;

    // --- TAB 1: SUMMARY & ACTIONS ---
    private JTextArea txtSummary;
    private JLabel lblTotal;
    private JButton btnCetak;
    private JPanel pnlInputForm;
    private JPanel pnlRightSummary;

    // --- TAB 2: RIWAYAT PEMESANAN COMPONENTS ---
    private JTable tblRiwayat;
    private DefaultTableModel modelRiwayat;
    private JButton btnEditPesanan;
    private JButton btnHapusPesanan;
    private JButton btnSelesaiPesanan;
    private final ArrayList<OrderRecord> listOrder = new ArrayList<>();
    private int orderCounter = 1;

    // --- STATE DATA ---
    private double currentTotal = 0;
    private int formRow = 0;

    @SuppressWarnings("this-escape")
    public PetCareGUI() {
        initUI();
    }

    private void initUI() {
        setTitle("PetCare Vet Clinic & Grooming - Management System");
        setSize(1120, 840);
        setMinimumSize(new Dimension(1000, 750));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inisialisasi JTabbedPane sebagai kontainer utama
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font(UIHelper.FONT_FAMILY, Font.BOLD, 14));
        tabbedPane.setBackground(UIHelper.COLOR_BG_MAIN);
        tabbedPane.setForeground(UIHelper.COLOR_TEXT_MAIN);

        // Tab 1: Registrasi Layanan & Invoice
        final JPanel tabRegistrasi = buildTabRegistrasi();
        tabbedPane.addTab("🐾 Registrasi Layanan", tabRegistrasi);

        // Tab 2: Riwayat Pemesanan (CRUD)
        final JPanel tabRiwayat = buildTabRiwayat();
        tabbedPane.addTab("📋 Riwayat Pemesanan", tabRiwayat);

        setContentPane(tabbedPane);

        // Setup seluruh Event Listener
        setupEventListeners();
    }

    // =========================================================================
    // BUILDER: TAB 1 (REGISTRASI LAYANAN & NOTA PEMBAYARAN)
    // =========================================================================
    private JPanel buildTabRegistrasi() {
        final JPanel rootPanel = new JPanel(new GridLayout(1, 2, 24, 0));
        rootPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        rootPanel.setBackground(UIHelper.COLOR_BG_MAIN);

        // --- PANEL KIRI: FORM INPUT ---
        final JPanel pnlFormWrapper = new JPanel(new BorderLayout());
        pnlFormWrapper.setOpaque(false);

        pnlInputForm = new JPanel(new GridBagLayout());
        pnlInputForm.setBackground(UIHelper.COLOR_BG_MAIN);
        pnlInputForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.COLOR_BORDER, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        pnlInputForm.putClientProperty("FlatLaf.style", "arc: 20");

        final GridBagConstraints gbc = new GridBagConstraints();
        final Insets defaultInsets = new Insets(6, 10, 6, 10);
        gbc.insets = defaultInsets;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        // Header Panel Kiri
        gbc.gridx = 0;
        gbc.gridy = formRow;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 12, 10);
        final JPanel pnlHeaderLeft = createSectionHeader("🐾 Formulir Registrasi Pasien", "Input data pasien, dokter, dan paket layanan");
        pnlInputForm.add(pnlHeaderLeft, gbc);
        gbc.gridwidth = 1;
        gbc.insets = defaultInsets;
        formRow++;

        // Form Fields
        addFormRow(pnlInputForm, gbc, "Nama Pemilik", txtOwner = createStyledTextField());
        addFormRow(pnlInputForm, gbc, "Nama Hewan", txtPetName = createStyledTextField());
        addFormRow(pnlInputForm, gbc, "Jenis Hewan", cbPetType = createStyledComboBox(new String[]{"Kucing", "Anjing", "Kelinci", "Burung", "Lainnya"}));
        addFormRow(pnlInputForm, gbc, "Bobot Hewan (kg)", txtWeight = createStyledTextField());
        addFormRow(pnlInputForm, gbc, "Dokter Pemeriksa", cbDokter = createStyledComboBox(new String[]{"Drh. Budi", "Drh. Sarah", "Drh. Andi"}));
        addFormRow(pnlInputForm, gbc, "Paket Grooming", cbGrooming = createStyledComboBox(new String[]{"Tidak Ada", "Mandi Kutu", "Potong Bulu", "Potong Kuku", "Full Grooming"}));

        // Layanan Tambahan (Checkbox Grid 2x3)
        gbc.gridy = ++formRow;
        final JPanel pnlExtras = new JPanel(new GridLayout(2, 3, 8, 6));
        pnlExtras.setOpaque(false);
        chkVaccine  = new JCheckBox("Vaksinasi");
        chkFood     = new JCheckBox("Pakan");
        chkCheckup  = new JCheckBox("Checkup");
        chkBoarding = new JCheckBox("Rawat Inap");
        chkBedah    = new JCheckBox("Bedah Minor");

        final Font checkFont = new Font(UIHelper.FONT_FAMILY, Font.PLAIN, 13);
        final JCheckBox[] checkBoxes = {chkVaccine, chkFood, chkCheckup, chkBoarding, chkBedah};
        for (final JCheckBox cb : checkBoxes) {
            cb.setFont(checkFont);
            cb.setForeground(UIHelper.COLOR_TEXT_MAIN);
            cb.setOpaque(false);
            cb.setCursor(new Cursor(Cursor.HAND_CURSOR));
            pnlExtras.add(cb);
        }
        addFormRow(pnlInputForm, gbc, "Layanan Tambahan", pnlExtras);

        // Metode Pembayaran
        addFormRow(pnlInputForm, gbc, "Metode Pembayaran", cbPayment = createStyledComboBox(new String[]{"Cash", "QRIS"}));

        // Field Uang Diterima (Hanya saat Cash)
        pnlCashInputRow = new JPanel(new BorderLayout(0, 4));
        pnlCashInputRow.setOpaque(false);
        lblCashReceivedLabel = new JLabel("Uang Diterima (Rp)");
        UIHelper.styleFormLabel(lblCashReceivedLabel);
        txtCashReceived = createStyledTextField();
        pnlCashInputRow.add(lblCashReceivedLabel, BorderLayout.NORTH);
        pnlCashInputRow.add(txtCashReceived, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = ++formRow;
        gbc.insets = new Insets(4, 10, 10, 10);
        pnlInputForm.add(pnlCashInputRow, gbc);

        // Spacer Vertikal
        gbc.gridy = ++formRow;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        pnlInputForm.add(Box.createGlue(), gbc);

        final JScrollPane scrollForm = new JScrollPane(pnlInputForm);
        scrollForm.setBorder(null);
        scrollForm.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollForm.getVerticalScrollBar().setUnitIncrement(16);
        pnlFormWrapper.add(scrollForm, BorderLayout.CENTER);
        rootPanel.add(pnlFormWrapper);

        // --- PANEL KANAN: RINGKASAN TAGIHAN & PEMBAYARAN ---
        pnlRightSummary = new JPanel(new BorderLayout(0, 14));
        pnlRightSummary.setOpaque(false);

        final JPanel pnlSummaryCard = new JPanel(new BorderLayout(0, 12));
        pnlSummaryCard.setBackground(UIHelper.COLOR_BG_MAIN);
        pnlSummaryCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.COLOR_BORDER, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        pnlSummaryCard.putClientProperty("FlatLaf.style", "arc: 20");

        final JPanel pnlHeaderRight = createSectionHeader("🧾 Ringkasan Tagihan", "Rincian kalkulasi biaya tindakan dan invoice digital");
        pnlSummaryCard.add(pnlHeaderRight, BorderLayout.NORTH);

        txtSummary = new JTextArea();
        txtSummary.setEditable(false);
        txtSummary.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtSummary.setForeground(UIHelper.COLOR_TEXT_MAIN);
        txtSummary.setBackground(new Color(248, 251, 254));
        txtSummary.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 225, 238), 1, true),
                new EmptyBorder(12, 14, 12, 14)
        ));
        txtSummary.setText("Silakan lengkapi form di sebelah kiri\nuntuk melihat rincian tagihan medis & grooming.");

        final JScrollPane scrollSummary = new JScrollPane(txtSummary);
        scrollSummary.setBorder(null);
        scrollSummary.setOpaque(false);
        pnlSummaryCard.add(scrollSummary, BorderLayout.CENTER);

        // Bottom Summary: Total, Kembalian, QRIS Barcode & Tombol Cetak
        final JPanel pnlBottomSummary = new JPanel();
        pnlBottomSummary.setLayout(new BoxLayout(pnlBottomSummary, BoxLayout.Y_AXIS));
        pnlBottomSummary.setOpaque(false);

        // Total
        final JPanel pnlTotalContainer = new JPanel(new BorderLayout());
        pnlTotalContainer.setOpaque(false);
        pnlTotalContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, UIHelper.COLOR_BORDER),
                new EmptyBorder(8, 4, 8, 4)
        ));
        final JLabel lblTotalTitle = new JLabel("TOTAL TAGIHAN");
        lblTotalTitle.setFont(new Font(UIHelper.FONT_FAMILY, Font.BOLD, 13));
        lblTotalTitle.setForeground(UIHelper.COLOR_BORDER_DARK);
        lblTotal = UIHelper.createTotalLabel();
        pnlTotalContainer.add(lblTotalTitle, BorderLayout.NORTH);
        pnlTotalContainer.add(lblTotal, BorderLayout.CENTER);
        pnlBottomSummary.add(pnlTotalContainer);

        // Kembalian (Mode Cash)
        pnlKembalian = new JPanel(new BorderLayout());
        pnlKembalian.setOpaque(false);
        pnlKembalian.setBorder(new EmptyBorder(6, 4, 6, 4));
        final JLabel lblKembalianTitle = new JLabel("KEMBALIAN");
        lblKembalianTitle.setFont(new Font(UIHelper.FONT_FAMILY, Font.BOLD, 12));
        lblKembalianTitle.setForeground(UIHelper.COLOR_BORDER_DARK);
        lblKembalianValue = new JLabel("Rp 0");
        lblKembalianValue.setFont(new Font(UIHelper.FONT_FAMILY, Font.BOLD, 22));
        lblKembalianValue.setForeground(UIHelper.COLOR_PRIMARY);
        lblKembalianValue.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlKembalian.add(lblKembalianTitle, BorderLayout.NORTH);
        pnlKembalian.add(lblKembalianValue, BorderLayout.CENTER);
        pnlBottomSummary.add(pnlKembalian);

        // Card Barcode QRIS
        pnlQrisCard = new JPanel();
        pnlQrisCard.setLayout(new BoxLayout(pnlQrisCard, BoxLayout.Y_AXIS));
        pnlQrisCard.setBackground(UIHelper.COLOR_CARD_ACCENT);
        pnlQrisCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.COLOR_BORDER, 1, true),
                new EmptyBorder(10, 14, 12, 14)
        ));
        pnlQrisCard.putClientProperty("FlatLaf.style", "arc: 16");
        pnlQrisCard.setVisible(false);

        final JLabel lblQrisInstruction = new JLabel("Silakan scan QRIS berikut");
        lblQrisInstruction.setFont(new Font(UIHelper.FONT_FAMILY, Font.BOLD, 14));
        lblQrisInstruction.setForeground(UIHelper.COLOR_TEXT_MAIN);
        lblQrisInstruction.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlQrisCard.add(lblQrisInstruction);

        final JLabel lblQrisSub = new JLabel("Mendukung GoPay, OVO, Dana, ShopeePay & M-Banking");
        lblQrisSub.setFont(new Font(UIHelper.FONT_FAMILY, Font.PLAIN, 11));
        lblQrisSub.setForeground(UIHelper.COLOR_PRIMARY_DARK);
        lblQrisSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlQrisCard.add(lblQrisSub);
        pnlQrisCard.add(Box.createVerticalStrut(8));

        lblQrisBarcode = new JLabel();
        lblQrisBarcode.setAlignmentX(Component.CENTER_ALIGNMENT);
        setupQrisBarcodeImage();
        pnlQrisCard.add(lblQrisBarcode);

        pnlBottomSummary.add(pnlQrisCard);
        pnlBottomSummary.add(Box.createVerticalStrut(10));

        // Tombol Cetak Nota (Primary Button)
        btnCetak = UIHelper.createPrimaryButton("Cetak Nota");
        btnCetak.setEnabled(false);
        btnCetak.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlBottomSummary.add(btnCetak);

        pnlSummaryCard.add(pnlBottomSummary, BorderLayout.SOUTH);
        pnlRightSummary.add(pnlSummaryCard, BorderLayout.CENTER);
        rootPanel.add(pnlRightSummary);

        return rootPanel;
    }

    // =========================================================================
    // BUILDER: TAB 2 (RIWAYAT PEMESANAN & CRUD JTABLE)
    // =========================================================================
    private JPanel buildTabRiwayat() {
        final JPanel pnlRiwayatWrapper = new JPanel(new BorderLayout(0, 16));
        pnlRiwayatWrapper.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlRiwayatWrapper.setBackground(UIHelper.COLOR_BG_MAIN);

        // Header Tab Riwayat
        final JPanel pnlHeader = createSectionHeader("📋 Riwayat Pemesanan Layanan",
                "Daftar transaksi aktif. Pilih baris pemesanan untuk melakukan Edit, Batal/Hapus, atau Tandai Selesai.");
        pnlRiwayatWrapper.add(pnlHeader, BorderLayout.NORTH);

        // Struktur Tabel JTable dengan DefaultTableModel
        final String[] columnNames = {"ID/No", "Nama Pemilik", "Nama Hewan", "Layanan", "Total Biaya", "Status Pembayaran"};
        modelRiwayat = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel read-only secara langsung, edit via tombol aksi
            }
        };

        tblRiwayat = new JTable(modelRiwayat);
        tblRiwayat.setRowHeight(36);
        tblRiwayat.setFont(new Font(UIHelper.FONT_FAMILY, Font.PLAIN, 13));
        tblRiwayat.setForeground(UIHelper.COLOR_TEXT_MAIN);
        tblRiwayat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblRiwayat.setSelectionBackground(UIHelper.COLOR_CARD_ACCENT);
        tblRiwayat.setSelectionForeground(UIHelper.COLOR_TEXT_MAIN);
        tblRiwayat.setShowGrid(true);
        tblRiwayat.setGridColor(UIHelper.COLOR_BORDER);

        // Header Tabel Styling
        tblRiwayat.getTableHeader().setFont(new Font(UIHelper.FONT_FAMILY, Font.BOLD, 13));
        tblRiwayat.getTableHeader().setBackground(UIHelper.COLOR_CARD_ACCENT);
        tblRiwayat.getTableHeader().setForeground(UIHelper.COLOR_TEXT_MAIN);
        tblRiwayat.getTableHeader().setPreferredSize(new Dimension(0, 38));

        // Format Alignment Kolom
        final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblRiwayat.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        tblRiwayat.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Total Biaya
        tblRiwayat.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Status

        // Lebar Kolom
        tblRiwayat.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblRiwayat.getColumnModel().getColumn(1).setPreferredWidth(140);
        tblRiwayat.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblRiwayat.getColumnModel().getColumn(3).setPreferredWidth(260);
        tblRiwayat.getColumnModel().getColumn(4).setPreferredWidth(130);
        tblRiwayat.getColumnModel().getColumn(5).setPreferredWidth(140);

        final JScrollPane scrollTable = new JScrollPane(tblRiwayat);
        scrollTable.setBorder(BorderFactory.createLineBorder(UIHelper.COLOR_BORDER, 1, true));
        scrollTable.getViewport().setBackground(UIHelper.COLOR_BG_MAIN);
        pnlRiwayatWrapper.add(scrollTable, BorderLayout.CENTER);

        // Panel Tombol Aksi di Bawah Tabel
        final JPanel pnlActionCard = UIHelper.createCardPanel(UIHelper.COLOR_CARD_ACCENT);
        pnlActionCard.setLayout(new FlowLayout(FlowLayout.RIGHT, 14, 8));

        btnEditPesanan = UIHelper.createSecondaryButton("✏️ Edit Pemesanan", UIHelper.COLOR_PRIMARY);
        btnHapusPesanan = UIHelper.createSecondaryButton("❌ Batal / Hapus", UIHelper.COLOR_PRIMARY_DARK);
        btnSelesaiPesanan = UIHelper.createSecondaryButton("✅ Selesai", UIHelper.COLOR_BORDER_DARK);

        pnlActionCard.add(btnEditPesanan);
        pnlActionCard.add(btnHapusPesanan);
        pnlActionCard.add(btnSelesaiPesanan);

        pnlRiwayatWrapper.add(pnlActionCard, BorderLayout.SOUTH);
        return pnlRiwayatWrapper;
    }

    // =========================================================================
    // EVENT LISTENERS & LOGIKA INTERAKTIF
    // =========================================================================
    private void setupEventListeners() {
        // Document Listener untuk Form Input
        final DocumentListener docListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { calculateTotal(); }
            @Override
            public void removeUpdate(DocumentEvent e) { calculateTotal(); }
            @Override
            public void changedUpdate(DocumentEvent e) { calculateTotal(); }
        };
        txtOwner.getDocument().addDocumentListener(docListener);
        txtPetName.getDocument().addDocumentListener(docListener);
        txtWeight.getDocument().addDocumentListener(docListener);

        // Listener khusus Input Uang Diterima
        final DocumentListener cashListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateKembalian(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateKembalian(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateKembalian(); }
        };
        txtCashReceived.getDocument().addDocumentListener(cashListener);

        // Action Listener ComboBox & Checkbox
        cbPetType.addActionListener(e -> calculateTotal());
        cbGrooming.addActionListener(e -> calculateTotal());
        cbDokter.addActionListener(e -> calculateTotal());
        chkVaccine.addActionListener(e -> calculateTotal());
        chkFood.addActionListener(e -> calculateTotal());
        chkCheckup.addActionListener(e -> calculateTotal());
        chkBoarding.addActionListener(e -> calculateTotal());
        chkBedah.addActionListener(e -> calculateTotal());

        // Toggle Cash vs QRIS
        cbPayment.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                final boolean isQris = "QRIS".equalsIgnoreCase(cbPayment.getSelectedItem().toString());
                pnlQrisCard.setVisible(isQris);
                pnlCashInputRow.setVisible(!isQris);
                pnlKembalian.setVisible(!isQris);

                if (isQris) {
                    txtCashReceived.setText("");
                    lblKembalianValue.setText("Rp 0");
                    lblKembalianValue.setForeground(UIHelper.COLOR_PRIMARY);
                }
                updateKembalian();
                pnlInputForm.revalidate();
                pnlInputForm.repaint();
                pnlRightSummary.revalidate();
                pnlRightSummary.repaint();
            }
        });

        // 1. Cetak Nota Action -> Simpan ke Tabel Riwayat & Reset Form
        btnCetak.addActionListener(e -> handleCetakNota());

        // 2. Tombol Edit Pemesanan
        btnEditPesanan.addActionListener(e -> handleEditPemesanan());

        // 3. Tombol Batal / Hapus Pemesanan
        btnHapusPesanan.addActionListener(e -> handleHapusPemesanan());

        // 4. Tombol Tandai Selesai
        btnSelesaiPesanan.addActionListener(e -> handleSelesaiPemesanan());
    }

    /**
     * Handler saat tombol "Cetak Nota" diklik:
     * - Menampilkan dialog nota
     * - Menyimpan data pemesanan ke model JTable di Tab 2
     * - Melakukan reset form di Tab 1
     */
    private void handleCetakNota() {
        if (!isFormValid() || currentTotal <= 0) return;

        final String payMethod = cbPayment.getSelectedItem().toString();
        final double cash = parseCash();
        final double kembalian = cash - currentTotal;
        final String kembalianStr = payMethod.equals("Cash")
                ? String.format("Metode        : Cash\nUang Diterima : Rp %,.0f\nKembalian     : Rp %,.0f", cash, kembalian)
                : "Metode        : QRIS Digital (Status: Lunas)";

        // Buat ID Pemesanan Unik
        final String orderId = String.format("ORD-%03d", orderCounter++);
        final String initialStatus = payMethod.equals("Cash") ? "Lunas (Cash)" : "Lunas (QRIS)";

        // Simpan ke Object Model OrderRecord
        final OrderRecord record = new OrderRecord(
                orderId,
                txtOwner.getText().trim(),
                txtPetName.getText().trim(),
                cbPetType.getSelectedItem().toString(),
                Double.parseDouble(txtWeight.getText().trim()),
                cbDokter.getSelectedItem().toString(),
                cbGrooming.getSelectedItem().toString(),
                chkVaccine.isSelected(),
                chkFood.isSelected(),
                chkCheckup.isSelected(),
                chkBoarding.isSelected(),
                chkBedah.isSelected(),
                payMethod,
                cash,
                currentTotal,
                initialStatus
        );

        listOrder.add(record);

        // Tambahkan baris baru ke JTable di Tab 2
        modelRiwayat.addRow(new Object[]{
                record.getId(),
                record.getOwnerName(),
                record.getPetName(),
                record.getServicesSummary(),
                String.format("Rp %,.0f", record.getTotalCost()),
                record.getStatus()
        });

        // Tampilkan dialog nota
        JOptionPane.showMessageDialog(this,
                "=========================================\n"
                        + "      NOTA RESMI PETCARE CLINIC\n"
                        + "=========================================\n"
                        + "No. Pemesanan : " + orderId + "\n\n"
                        + txtSummary.getText()
                        + "\n-----------------------------------------\n"
                        + kembalianStr
                        + "\n-----------------------------------------\n"
                        + "TOTAL BAYAR   : Rp " + String.format("%,.0f", currentTotal)
                        + "\n\nData pemesanan telah tersimpan di Tab 'Riwayat Pemesanan'!",
                "Cetak Nota Berhasil", JOptionPane.INFORMATION_MESSAGE);

        // Reset Formulir Registrasi
        resetForm();
    }

    /**
     * Handler saat tombol "Edit Pemesanan" diklik:
     * - Mengambil data dari baris tabel yang dipilih
     * - Mengembalikan data ke form di Tab 1
     * - Menghapus baris dari tabel sementara
     * - Memindahkan fokus tab ke Tab 1
     */
    private void handleEditPemesanan() {
        final int selectedRow = tblRiwayat.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih baris pemesanan yang ingin diedit terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        final OrderRecord record = listOrder.get(selectedRow);

        // Pindahkan data kembali ke formulir Tab 1
        txtOwner.setText(record.getOwnerName());
        txtPetName.setText(record.getPetName());
        cbPetType.setSelectedItem(record.getPetType());
        txtWeight.setText(String.valueOf(record.getWeight()));
        cbDokter.setSelectedItem(record.getDoctor());
        cbGrooming.setSelectedItem(record.getGroomingPackage());

        chkVaccine.setSelected(record.isVaccine());
        chkFood.setSelected(record.isFood());
        chkCheckup.setSelected(record.isCheckup());
        chkBoarding.setSelected(record.isBoarding());
        chkBedah.setSelected(record.isBedah());

        cbPayment.setSelectedItem(record.getPaymentMethod());
        if ("Cash".equalsIgnoreCase(record.getPaymentMethod())) {
            txtCashReceived.setText(String.format("%.0f", record.getCashReceived()));
        } else {
            txtCashReceived.setText("");
        }

        // Hapus pemesanan dari tabel dan list sementara
        modelRiwayat.removeRow(selectedRow);
        listOrder.remove(selectedRow);

        // Pindah fokus kembali ke Tab 1 (Registrasi Layanan)
        tabbedPane.setSelectedIndex(0);

        // Hitung ulang tagihan
        calculateTotal();

        JOptionPane.showMessageDialog(this,
                "Data pemesanan [" + record.getId() + "] telah dikembalikan ke formulir Tab Registrasi.\n"
                        + "Silakan sesuaikan data yang diperlukan lalu klik 'Cetak Nota' kembali.",
                "Mode Edit Aktif", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Handler saat tombol "Batal / Hapus" diklik.
     */
    private void handleHapusPemesanan() {
        final int selectedRow = tblRiwayat.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih baris pemesanan yang ingin dibatalkan/dihapus!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        final String orderId = modelRiwayat.getValueAt(selectedRow, 0).toString();
        final int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin membatalkan dan menghapus pemesanan [" + orderId + "]?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            modelRiwayat.removeRow(selectedRow);
            listOrder.remove(selectedRow);
            JOptionPane.showMessageDialog(this,
                    "Pemesanan [" + orderId + "] berhasil dibatalkan dan dihapus.",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Handler saat tombol "Selesai" diklik:
     * Mengubah status pemesanan di tabel menjadi "Selesai".
     */
    private void handleSelesaiPemesanan() {
        final int selectedRow = tblRiwayat.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih baris pemesanan yang ingin diselesaikan!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        final String orderId = modelRiwayat.getValueAt(selectedRow, 0).toString();
        modelRiwayat.setValueAt("Selesai", selectedRow, 5);
        listOrder.get(selectedRow).setStatus("Selesai");

        JOptionPane.showMessageDialog(this,
                "Pemesanan [" + orderId + "] telah ditandai sebagai Selesai!",
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mengosongkan formulir input registrasi setelah transaksi selesai.
     */
    private void resetForm() {
        txtOwner.setText("");
        txtPetName.setText("");
        cbPetType.setSelectedIndex(0);
        txtWeight.setText("");
        cbDokter.setSelectedIndex(0);
        cbGrooming.setSelectedIndex(0);

        chkVaccine.setSelected(false);
        chkFood.setSelected(false);
        chkCheckup.setSelected(false);
        chkBoarding.setSelected(false);
        chkBedah.setSelected(false);

        cbPayment.setSelectedIndex(0);
        txtCashReceived.setText("");
        currentTotal = 0;

        lblTotal.setText("Rp 0");
        lblKembalianValue.setText("Rp 0");
        btnCetak.setEnabled(false);
        txtSummary.setText("Silakan lengkapi form di sebelah kiri\nuntuk melihat rincian tagihan medis & grooming.");

        UIHelper.setInvalidBorder(txtOwner, false);
        UIHelper.setInvalidBorder(txtPetName, false);
        UIHelper.setInvalidBorder(txtWeight, false);
    }

    // =========================================================================
    // KALKULASI TAGIHAN & VALIDASI
    // =========================================================================
    private void calculateTotal() {
        final String owner = txtOwner.getText().trim();
        final String petName = txtPetName.getText().trim();
        final String weightText = txtWeight.getText().trim();

        boolean isValid = true;
        UIHelper.setInvalidBorder(txtWeight, false);
        UIHelper.setInvalidBorder(txtOwner, false);
        UIHelper.setInvalidBorder(txtPetName, false);

        if (owner.isEmpty()) { UIHelper.setInvalidBorder(txtOwner, true); isValid = false; }
        if (petName.isEmpty()) { UIHelper.setInvalidBorder(txtPetName, true); isValid = false; }

        double weight = 0;
        if (weightText.isEmpty()) {
            UIHelper.setInvalidBorder(txtWeight, true);
            isValid = false;
        } else {
            try {
                weight = Double.parseDouble(weightText);
                if (weight <= 0) { UIHelper.setInvalidBorder(txtWeight, true); isValid = false; }
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
            txtSummary.setText("Silakan lengkapi formulir di sebelah kiri dengan data yang valid.");
            return;
        }

        final String petType = cbPetType.getSelectedItem().toString();
        final String dokter = cbDokter.getSelectedItem().toString();
        final PasienHewan pasien = new PasienHewan(petName, petType, owner, weight);

        // Grooming
        final String selectedGrooming = cbGrooming.getSelectedItem().toString();
        double baseGrooming = 0;
        switch (selectedGrooming) {
            case "Mandi Kutu":    baseGrooming = 50000;  break;
            case "Potong Bulu":   baseGrooming = 40000;  break;
            case "Potong Kuku":   baseGrooming = 25000;  break;
            case "Full Grooming": baseGrooming = 100000; break;
            default:              baseGrooming = 0;      break;
        }
        final double surcharge = (weight >= 5.0) ? 20000 : 0;
        double groomingCost = 0;
        LayananGrooming grooming = null;
        if (baseGrooming > 0) {
            groomingCost = baseGrooming + surcharge;
            grooming = new LayananGrooming(selectedGrooming, groomingCost);
        }

        final double vaccineCost  = chkVaccine.isSelected()  ? (100000 + surcharge) : 0;
        final double foodCost     = chkFood.isSelected()     ? 50000 : 0;
        final double checkupCost  = chkCheckup.isSelected()  ? (75000 + surcharge)  : 0;
        final double boardingCost = chkBoarding.isSelected() ? 150000 : 0;
        final double bedahCost    = chkBedah.isSelected()    ? (250000 + surcharge) : 0;

        currentTotal = groomingCost + vaccineCost + foodCost + checkupCost + boardingCost + bedahCost;

        // Render Invoice Summary
        final StringBuilder sb = new StringBuilder();
        sb.append("DATA PASIEN:\n");
        sb.append(String.format("  Nama Pasien  : %s (%s)\n", pasien.getNamaPeliharaan(), pasien.getJenisHewan()));
        sb.append(String.format("  Nama Pemilik : %s\n", pasien.getNamaOwner()));
        sb.append(String.format("  Bobot Tubuh  : %.1f kg\n", pasien.getBobotKg()));
        sb.append(String.format("  Dokter Jaga  : %s\n\n", dokter));

        sb.append("RINCIAN TINDAKAN & BIAYA:\n");
        if (grooming != null) {
            sb.append(String.format("  • %-22s: Rp %,10.0f\n", "Grooming (" + grooming.getPaket() + ")", grooming.getBiayaLayanan()));
        }
        if (chkVaccine.isSelected()) {
            sb.append(String.format("  • %-22s: Rp %,10.0f\n", "Vaksinasi", vaccineCost));
        }
        if (chkFood.isSelected()) {
            sb.append(String.format("  • %-22s: Rp %,10.0f\n", "Pakan Khusus", foodCost));
        }
        if (chkCheckup.isSelected()) {
            sb.append(String.format("  • %-22s: Rp %,10.0f\n", "Checkup Umum", checkupCost));
        }
        if (chkBoarding.isSelected()) {
            sb.append(String.format("  • %-22s: Rp %,10.0f\n", "Rawat Inap", boardingCost));
        }
        if (chkBedah.isSelected()) {
            sb.append(String.format("  • %-22s: Rp %,10.0f\n", "Bedah Minor", bedahCost));
        }

        if (currentTotal == 0) {
            sb.append("  (Belum ada layanan yang dipilih)\n");
        }

        final boolean hasSurcharge = (grooming != null || chkVaccine.isSelected() || chkCheckup.isSelected() || chkBedah.isSelected());
        if (weight >= 5.0 && hasSurcharge) {
            sb.append("\n*Catatan: Surcharge bobot >5kg (+Rp 20.000) diterapkan.");
        }

        final String payMethod = cbPayment.getSelectedItem().toString();
        sb.append("\n\nMETODE PEMBAYARAN: ").append(payMethod);

        txtSummary.setText(sb.toString());
        lblTotal.setText(String.format("Rp %,.0f", currentTotal));

        updateKembalian();
    }

    private void updateKembalian() {
        final String payMethod = cbPayment.getSelectedItem().toString();
        if ("QRIS".equalsIgnoreCase(payMethod)) {
            lblKembalianValue.setText("Rp 0");
            lblKembalianValue.setForeground(UIHelper.COLOR_PRIMARY);
            revalidateCetakButton();
            return;
        }

        final double cash = parseCash();
        if (txtCashReceived.getText().trim().isEmpty()) {
            lblKembalianValue.setText("Rp 0");
            lblKembalianValue.setForeground(UIHelper.COLOR_PRIMARY);
            btnCetak.setEnabled(false);
            return;
        }

        final double kembalian = cash - currentTotal;
        if (kembalian < 0) {
            lblKembalianValue.setText("Uang Kurang!");
            lblKembalianValue.setForeground(new Color(220, 50, 50));
            btnCetak.setEnabled(false);
        } else {
            lblKembalianValue.setText(String.format("Rp %,.0f", kembalian));
            lblKembalianValue.setForeground(UIHelper.COLOR_PRIMARY);
            revalidateCetakButton();
        }
    }

    private void revalidateCetakButton() {
        if (!isFormValid() || currentTotal <= 0) {
            btnCetak.setEnabled(false);
            return;
        }
        final String payMethod = cbPayment.getSelectedItem().toString();
        if ("Cash".equalsIgnoreCase(payMethod)) {
            final double cash = parseCash();
            btnCetak.setEnabled(cash >= currentTotal);
        } else {
            btnCetak.setEnabled(true);
        }
    }

    private boolean isFormValid() {
        final String owner = txtOwner.getText().trim();
        final String petName = txtPetName.getText().trim();
        final String weightText = txtWeight.getText().trim();
        if (owner.isEmpty() || petName.isEmpty() || weightText.isEmpty()) return false;
        try {
            return Double.parseDouble(weightText) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private double parseCash() {
        try {
            final String text = txtCashReceived.getText().trim().replace(",", "").replace(".", "");
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void setupQrisBarcodeImage() {
        final ImageIcon scaledIcon = UIHelper.loadAndScaleImage(QRIS_IMAGE_PATH, QRIS_BARCODE_WIDTH, QRIS_BARCODE_HEIGHT);
        if (scaledIcon != null) {
            lblQrisBarcode.setIcon(scaledIcon);
            lblQrisBarcode.setText(null);
            lblQrisBarcode.setBorder(BorderFactory.createLineBorder(UIHelper.COLOR_BORDER, 1, true));
        } else {
            lblQrisBarcode.setIcon(null);
            lblQrisBarcode.setText("<html><center><b>[QRIS Code Placeholder]</b><br>"
                    + "<font size='2' color='#1b3554'>File: " + QRIS_IMAGE_PATH + "</font></center></html>");
            lblQrisBarcode.setFont(new Font(UIHelper.FONT_FAMILY, Font.PLAIN, 12));
            lblQrisBarcode.setPreferredSize(new Dimension(QRIS_BARCODE_WIDTH, QRIS_BARCODE_HEIGHT));
            lblQrisBarcode.setBorder(BorderFactory.createDashedBorder(UIHelper.COLOR_BORDER_DARK, 2, 4, 4, true));
        }
    }

    // =========================================================================
    // UI COMPONENT HELPERS
    // =========================================================================
    private JPanel createSectionHeader(String title, String subtitle) {
        final JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(UIHelper.COLOR_CARD_ACCENT);
        pnl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.COLOR_BORDER, 1, true),
                new EmptyBorder(10, 16, 10, 16)
        ));
        pnl.putClientProperty("FlatLaf.style", "arc: 14");

        final JLabel lblTitle = UIHelper.createHeaderLabel(title);
        final JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font(UIHelper.FONT_FAMILY, Font.PLAIN, 12));
        lblSub.setForeground(UIHelper.COLOR_PRIMARY_DARK);

        pnl.add(lblTitle, BorderLayout.NORTH);
        pnl.add(lblSub, BorderLayout.SOUTH);
        return pnl;
    }

    private JTextField createStyledTextField() {
        final JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 36));
        field.putClientProperty("FlatLaf.style", "arc: 12");
        return field;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        final JComboBox<String> combo = new JComboBox<>(items);
        combo.setPreferredSize(new Dimension(combo.getPreferredSize().width, 36));
        combo.putClientProperty("FlatLaf.style", "arc: 12");
        return combo;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, String labelText, JComponent comp) {
        gbc.gridx = 0;
        gbc.gridy = formRow;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        final JLabel label = new JLabel(labelText);
        UIHelper.styleFormLabel(label);
        panel.add(label, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++formRow;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 10, 8, 10);
        panel.add(comp, gbc);

        gbc.insets = new Insets(6, 10, 6, 10);
        formRow++;
    }

    // =========================================================================
    // MAIN METHOD: KONFIGURASI UIMANAGER.PUT() SEBELUM INISIALISASI FRAME
    // =========================================================================
    public static void main(String[] args) {
        // 1. Eksekusi konfigurasi UIManager.put() dan inisialisasi tema FlatLaf
        // SEBELUM membuat instance JFrame/komponen Swing apapun.
        UIHelper.setupFlatLaf();

        // 2. Jalankan aplikasi di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new PetCareGUI().setVisible(true));
    }
}
