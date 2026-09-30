import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * PetCareGUI - Kelas utama antarmuka grafis aplikasi BluePaw Vet & Grooming.
 * Arsitektur JTabbedPane dengan 2 tab: Registrasi Layanan dan Riwayat Pemesanan.
 * Menggunakan FlatMacLightLaf Custom Blue Palette, FlatSVGIcon, Clean Code, Zero Warnings.
 */
public final class PetCareGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String LOGO_SVG_PATH   = "bluepaw-logo.svg";
    private static final String QRIS_IMAGE_PATH = "qris_dummy.png";
    private static final int    QRIS_SIZE       = 200;

    // --- Tab 1: Form Input ---
    private final JTextField        txtOwner;
    private final JTextField        txtPetName;
    private final JTextField        txtWeight;
    private final JComboBox<String> cbPetType;
    private final JComboBox<String> cbGrooming;
    private final JComboBox<String> cbDokter;
    private final JCheckBox         chkVaccine;
    private final JCheckBox         chkFood;
    private final JCheckBox         chkCheckup;
    private final JCheckBox         chkBoarding;
    private final JCheckBox         chkBedah;

    // --- Tab 1: Pembayaran ---
    private final JComboBox<String> cbPayment;
    private final JPanel            pnlCashInputRow;
    private final JTextField        txtCashReceived;
    private final JPanel            pnlKembalian;
    private final JLabel            lblKembalianValue;

    // --- Tab 1: QRIS ---
    private final JPanel pnlQrisCard;
    private final JLabel lblQrisBarcode;

    // --- Tab 1: Ringkasan & Aksi ---
    private final JTextArea txtSummary;
    private final JLabel    lblTotal;
    private final JButton   btnCetak;
    private final JPanel    pnlInputForm;
    private final JPanel    pnlRightSummary;

    // --- Tab 2: Riwayat ---
    private final JTabbedPane       tabbedPane;
    private final JTable            tblRiwayat;
    private final DefaultTableModel modelRiwayat;
    private final JButton           btnEditPesanan;
    private final JButton           btnHapusPesanan;
    private final JButton           btnSelesaiPesanan;

    // --- State ---
    private final ArrayList<OrderRecord> listOrder = new ArrayList<>();
    private double currentTotal = 0;
    private int    orderCounter = 1;

    @SuppressWarnings("this-escape")
    public PetCareGUI() {
        txtOwner        = createStyledTextField();
        txtPetName      = createStyledTextField();
        txtWeight       = createStyledTextField();
        txtCashReceived = createStyledTextField();
        cbPetType  = createStyledComboBox(new String[]{"Kucing","Anjing","Kelinci","Burung","Lainnya"});
        cbGrooming = createStyledComboBox(new String[]{"Tidak Ada","Mandi Kutu","Potong Bulu","Potong Kuku","Full Grooming"});
        cbDokter   = createStyledComboBox(new String[]{"Drh. Budi","Drh. Sarah","Drh. Andi"});
        cbPayment  = createStyledComboBox(new String[]{"Cash","QRIS"});
        chkVaccine  = createStyledCheckBox("Vaksinasi");
        chkFood     = createStyledCheckBox("Pakan");
        chkCheckup  = createStyledCheckBox("Checkup");
        chkBoarding = createStyledCheckBox("Rawat Inap");
        chkBedah    = createStyledCheckBox("Bedah Minor");
        lblTotal          = UIHelper.createTotalLabel();
        lblKembalianValue = buildKembalianLabel();
        lblQrisBarcode    = new JLabel();
        lblQrisBarcode.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtSummary = buildSummaryArea();
        btnCetak   = UIHelper.createPrimaryButton("Cetak Nota");
        btnCetak.setEnabled(false);
        btnCetak.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlCashInputRow = buildCashInputRow();
        pnlKembalian    = buildKembalianPanel();
        pnlQrisCard     = buildQrisCard();
        pnlInputForm    = buildFormPanel();
        pnlRightSummary = new JPanel(new BorderLayout(0, 14));
        pnlRightSummary.setOpaque(false);
        modelRiwayat      = buildTableModel();
        tblRiwayat        = buildRiwayatTable();
        btnEditPesanan    = UIHelper.createSecondaryButton("Edit Pemesanan",  UIHelper.COLOR_PRIMARY);
        btnHapusPesanan   = UIHelper.createSecondaryButton("Batal / Hapus",   UIHelper.COLOR_PRIMARY_DARK);
        btnSelesaiPesanan = UIHelper.createSecondaryButton("Selesai",          UIHelper.COLOR_BORDER_DARK);
        tabbedPane = new JTabbedPane();
        initUI();
        setupEventListeners();
    }

    private void initUI() {
        setTitle("BluePaw Vet & Grooming - Management System");
        setSize(1120, 860);
        setMinimumSize(new Dimension(1000, 750));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        loadAppWindowIcon();
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIHelper.COLOR_BG_MAIN);
        mainPanel.add(buildAppHeader(), BorderLayout.NORTH);
        tabbedPane.setFont(new Font(UIHelper.FONT_FAMILY, Font.BOLD, 14));
        tabbedPane.setBackground(UIHelper.COLOR_BG_MAIN);
        tabbedPane.setForeground(UIHelper.COLOR_TEXT_MAIN);
        tabbedPane.addTab("Registrasi Layanan", buildTabRegistrasi());
        tabbedPane.addTab("Riwayat Pemesanan",  buildTabRiwayat());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void loadAppWindowIcon() {
        final List<Image> icons = UIHelper.loadWindowIcons(LOGO_SVG_PATH);
        if (icons != null && !icons.isEmpty()) setIconImages(icons);
    }

    private JPanel buildAppHeader() {
        final JPanel hdr = new JPanel(new BorderLayout(16, 0));
        hdr.setBackground(UIHelper.COLOR_BG_MAIN);
        hdr.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0, UIHelper.COLOR_BORDER),
                new EmptyBorder(12,24,12,24)));
        final JPanel pnlBrand = new JPanel(new FlowLayout(FlowLayout.LEFT,14,0));
        pnlBrand.setOpaque(false);
        final FlatSVGIcon logoIcon = UIHelper.loadSVGIcon(LOGO_SVG_PATH, 54, 36);
        final JLabel lblLogo = new JLabel();
        if (logoIcon != null) { lblLogo.setIcon(logoIcon); }
        else { lblLogo.setText("BP"); lblLogo.setFont(new Font(UIHelper.FONT_FAMILY, Font.BOLD, 24));
               lblLogo.setForeground(UIHelper.COLOR_PRIMARY); }
        pnlBrand.add(lblLogo);
        final JPanel pnlTxt = new JPanel();
        pnlTxt.setLayout(new BoxLayout(pnlTxt, BoxLayout.Y_AXIS));
        pnlTxt.setOpaque(false);
        final JLabel lblTitle = new JLabel("BluePaw Vet & Grooming");
        lblTitle.setFont(new Font(UIHelper.FONT_FAMILY, Font.BOLD, 22));
        lblTitle.setForeground(UIHelper.COLOR_PRIMARY_DARK);
        final JLabel lblSlog = new JLabel("Sistem Kasir & Manajemen Terpadu Klinik Hewan Peliharaan");
        lblSlog.setFont(new Font(UIHelper.FONT_FAMILY, Font.PLAIN, 12));
        lblSlog.setForeground(UIHelper.COLOR_BORDER_DARK);
        pnlTxt.add(lblTitle); pnlTxt.add(Box.createVerticalStrut(2)); pnlTxt.add(lblSlog);
        pnlBrand.add(pnlTxt);
        hdr.add(pnlBrand, BorderLayout.WEST);
        final JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,4));
        pnlRight.setOpaque(false);
        final JLabel lblBadge = new JLabel("Sistem Aktif");
        lblBadge.setFont(new Font(UIHelper.FONT_FAMILY, Font.BOLD, 12));
        lblBadge.setForeground(new Color(20,130,70));
        lblBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(170,225,190),1,true), new EmptyBorder(5,12,5,12)));
        pnlRight.add(lblBadge);
        hdr.add(pnlRight, BorderLayout.EAST);
        return hdr;
    }

    private JPanel buildTabRegistrasi() {
        final JPanel root = new JPanel(new GridLayout(1,2,24,0));
        root.setBorder(new EmptyBorder(20,20,20,20));
        root.setBackground(UIHelper.COLOR_BG_MAIN);
        final JPanel pnlFormWrapper = new JPanel(new BorderLayout());
        pnlFormWrapper.setOpaque(false);
        final JScrollPane scrollForm = new JScrollPane(pnlInputForm);
        scrollForm.setBorder(null);
        scrollForm.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollForm.getVerticalScrollBar().setUnitIncrement(16);
        pnlFormWrapper.add(scrollForm, BorderLayout.CENTER);
        root.add(pnlFormWrapper);
        pnlRightSummary.add(buildSummaryCard(), BorderLayout.CENTER);
        root.add(pnlRightSummary);
        return root;
    }

    private JPanel buildFormPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIHelper.COLOR_BG_MAIN);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.COLOR_BORDER, 1, true),
                new EmptyBorder(20,20,20,20)));
        panel.putClientProperty("FlatLaf.style", "arc: 20");
        final GridBagConstraints gbc = new GridBagConstraints();
        final Insets def = new Insets(6,10,6,10);
        gbc.insets = def; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1.0;
        int row = 0;
        gbc.gridx=0; gbc.gridy=row++; gbc.gridwidth=2; gbc.insets=new Insets(0,10,12,10);
        panel.add(createSectionHeader("Formulir Registrasi Pasien","Input data pasien, dokter, dan paket layanan"),gbc);
        gbc.gridwidth=1; gbc.insets=def;
        row = addFormRow(panel,gbc,row,"Nama Pemilik",    txtOwner);
        row = addFormRow(panel,gbc,row,"Nama Hewan",      txtPetName);
        row = addFormRow(panel,gbc,row,"Jenis Hewan",     cbPetType);
        row = addFormRow(panel,gbc,row,"Bobot Hewan (kg)",txtWeight);
        row = addFormRow(panel,gbc,row,"Dokter Pemeriksa",cbDokter);
        row = addFormRow(panel,gbc,row,"Paket Grooming",  cbGrooming);
        gbc.gridy=row++;
        final JPanel pnlExt = new JPanel(new GridLayout(2,3,8,6));
        pnlExt.setOpaque(false);
        for (JCheckBox cb : new JCheckBox[]{chkVaccine,chkFood,chkCheckup,chkBoarding,chkBedah}) pnlExt.add(cb);
        pnlExt.add(new JLabel());
        row = addFormRow(panel,gbc,row,"Layanan Tambahan",pnlExt);
        row = addFormRow(panel,gbc,row,"Metode Pembayaran",cbPayment);
        gbc.gridx=0; gbc.gridy=row++; gbc.weightx=1.0; gbc.insets=new Insets(4,10,10,10);
        panel.add(pnlCashInputRow,gbc); gbc.insets=def;
        gbc.gridy=row; gbc.weighty=1.0; gbc.fill=GridBagConstraints.BOTH;
        panel.add(Box.createGlue(),gbc);
        return panel;
    }

    private JPanel buildSummaryCard() {
        final JPanel card = new JPanel(new BorderLayout(0,12));
        card.setBackground(UIHelper.COLOR_BG_MAIN);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.COLOR_BORDER,1,true), new EmptyBorder(20,20,20,20)));
        card.putClientProperty("FlatLaf.style","arc: 20");
        card.add(createSectionHeader("Ringkasan Tagihan","Rincian kalkulasi biaya dan invoice digital"), BorderLayout.NORTH);
        final JScrollPane sc = new JScrollPane(txtSummary);
        sc.setBorder(null); sc.setOpaque(false);
        card.add(sc, BorderLayout.CENTER);
        card.add(buildBottomSummaryPanel(), BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildBottomSummaryPanel() {
        final JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setOpaque(false);
        final JPanel pnlTot = new JPanel(new BorderLayout());
        pnlTot.setOpaque(false);
        pnlTot.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1,0,1,0,UIHelper.COLOR_BORDER), new EmptyBorder(8,4,8,4)));
        final JLabel lblTotTitle = new JLabel("TOTAL TAGIHAN");
        lblTotTitle.setFont(new Font(UIHelper.FONT_FAMILY, Font.BOLD, 13));
        lblTotTitle.setForeground(UIHelper.COLOR_BORDER_DARK);
        pnlTot.add(lblTotTitle, BorderLayout.NORTH);
        pnlTot.add(lblTotal, BorderLayout.CENTER);
        pnl.add(pnlTot);
        pnl.add(pnlKembalian);
        setupQrisBarcodeImage();
        pnl.add(pnlQrisCard);
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(btnCetak);
        return pnl;
    }

    private JPanel buildTabRiwayat() {
        final JPanel wrap = new JPanel(new BorderLayout(0,16));
        wrap.setBorder(new EmptyBorder(20,20,20,20));
        wrap.setBackground(UIHelper.COLOR_BG_MAIN);
        wrap.add(createSectionHeader("Riwayat Pemesanan Layanan",
                "Pilih baris untuk Edit, Batal/Hapus, atau Tandai Selesai."), BorderLayout.NORTH);
        final JScrollPane sc = new JScrollPane(tblRiwayat);
        sc.setBorder(BorderFactory.createLineBorder(UIHelper.COLOR_BORDER,1,true));
        sc.getViewport().setBackground(UIHelper.COLOR_BG_MAIN);
        wrap.add(sc, BorderLayout.CENTER);
        final JPanel pnlAct = UIHelper.createCardPanel(UIHelper.COLOR_CARD_ACCENT);
        pnlAct.setLayout(new FlowLayout(FlowLayout.RIGHT,14,8));
        pnlAct.add(btnEditPesanan); pnlAct.add(btnHapusPesanan); pnlAct.add(btnSelesaiPesanan);
        wrap.add(pnlAct, BorderLayout.SOUTH);
        return wrap;
    }

    private JTextArea buildSummaryArea() {
        final JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setFont(new Font("Consolas", Font.PLAIN, 13));
        ta.setForeground(UIHelper.COLOR_TEXT_MAIN);
        ta.setBackground(new Color(248,251,254));
        ta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210,225,238),1,true), new EmptyBorder(12,14,12,14)));
        ta.setText("Silakan lengkapi form di sebelah kiri\nuntuk melihat rincian tagihan medis & grooming.");
        return ta;
    }

    private JLabel buildKembalianLabel() {
        final JLabel lbl = new JLabel("Rp 0");
        lbl.setFont(new Font(UIHelper.FONT_FAMILY, Font.BOLD, 22));
        lbl.setForeground(UIHelper.COLOR_PRIMARY);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        return lbl;
    }

    private JPanel buildKembalianPanel() {
        final JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(6,4,6,4));
        final JLabel lbl = new JLabel("KEMBALIAN");
        lbl.setFont(new Font(UIHelper.FONT_FAMILY, Font.BOLD, 12));
        lbl.setForeground(UIHelper.COLOR_BORDER_DARK);
        pnl.add(lbl, BorderLayout.NORTH);
        pnl.add(lblKembalianValue, BorderLayout.CENTER);
        return pnl;
    }

    private JPanel buildCashInputRow() {
        final JPanel pnl = new JPanel(new BorderLayout(0,4));
        pnl.setOpaque(false);
        final JLabel lbl = new JLabel("Uang Diterima (Rp)");
        UIHelper.styleFormLabel(lbl);
        pnl.add(lbl, BorderLayout.NORTH);
        pnl.add(txtCashReceived, BorderLayout.CENTER);
        return pnl;
    }

    private JPanel buildQrisCard() {
        final JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(UIHelper.COLOR_CARD_ACCENT);
        pnl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.COLOR_BORDER,1,true), new EmptyBorder(10,14,12,14)));
        pnl.putClientProperty("FlatLaf.style","arc: 16");
        pnl.setVisible(false);
        final JLabel lblTit = new JLabel("Silakan scan QRIS berikut");
        lblTit.setFont(new Font(UIHelper.FONT_FAMILY, Font.BOLD,14));
        lblTit.setForeground(UIHelper.COLOR_TEXT_MAIN);
        lblTit.setAlignmentX(Component.CENTER_ALIGNMENT);
        final JLabel lblSub = new JLabel("Mendukung GoPay, OVO, Dana, ShopeePay & M-Banking");
        lblSub.setFont(new Font(UIHelper.FONT_FAMILY, Font.PLAIN,11));
        lblSub.setForeground(UIHelper.COLOR_PRIMARY_DARK);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnl.add(lblTit); pnl.add(lblSub);
        pnl.add(Box.createVerticalStrut(8));
        pnl.add(lblQrisBarcode);
        return pnl;
    }

    private DefaultTableModel buildTableModel() {
        final String[] cols = {"ID/No","Nama Pemilik","Nama Hewan","Layanan","Total Biaya","Status"};
        return new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private JTable buildRiwayatTable() {
        final JTable tbl = new JTable(modelRiwayat);
        tbl.setRowHeight(36);
        tbl.setFont(new Font(UIHelper.FONT_FAMILY, Font.PLAIN,13));
        tbl.setForeground(UIHelper.COLOR_TEXT_MAIN);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(UIHelper.COLOR_CARD_ACCENT);
        tbl.setSelectionForeground(UIHelper.COLOR_TEXT_MAIN);
        tbl.setShowGrid(true); tbl.setGridColor(UIHelper.COLOR_BORDER);
        tbl.getTableHeader().setFont(new Font(UIHelper.FONT_FAMILY, Font.BOLD,13));
        tbl.getTableHeader().setBackground(UIHelper.COLOR_CARD_ACCENT);
        tbl.getTableHeader().setForeground(UIHelper.COLOR_TEXT_MAIN);
        tbl.getTableHeader().setPreferredSize(new Dimension(0,38));
        final DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
        cr.setHorizontalAlignment(SwingConstants.CENTER);
        tbl.getColumnModel().getColumn(0).setCellRenderer(cr);
        tbl.getColumnModel().getColumn(4).setCellRenderer(cr);
        tbl.getColumnModel().getColumn(5).setCellRenderer(cr);
        tbl.getColumnModel().getColumn(0).setPreferredWidth(80);
        tbl.getColumnModel().getColumn(1).setPreferredWidth(140);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(120);
        tbl.getColumnModel().getColumn(3).setPreferredWidth(260);
        tbl.getColumnModel().getColumn(4).setPreferredWidth(130);
        tbl.getColumnModel().getColumn(5).setPreferredWidth(140);
        return tbl;
    }

    private void setupQrisBarcodeImage() {
        final ImageIcon icon = UIHelper.loadAndScaleImage(QRIS_IMAGE_PATH, QRIS_SIZE, QRIS_SIZE);
        if (icon != null) {
            lblQrisBarcode.setIcon(icon); lblQrisBarcode.setText(null);
            lblQrisBarcode.setBorder(BorderFactory.createLineBorder(UIHelper.COLOR_BORDER,1,true));
        } else {
            lblQrisBarcode.setIcon(null);
            lblQrisBarcode.setText("<html><center><b>[QRIS Placeholder]</b><br><font size='2' color='#1b3554'>"
                    + QRIS_IMAGE_PATH + "</font></center></html>");
            lblQrisBarcode.setFont(new Font(UIHelper.FONT_FAMILY, Font.PLAIN,12));
            lblQrisBarcode.setPreferredSize(new Dimension(QRIS_SIZE, QRIS_SIZE));
            lblQrisBarcode.setBorder(BorderFactory.createDashedBorder(UIHelper.COLOR_BORDER_DARK,2,4,4,true));
        }
    }

    // =========================================================================
    // HELPER KOMPONEN UI
    // =========================================================================
    private JTextField createStyledTextField() {
        final JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(f.getPreferredSize().width, 36));
        f.putClientProperty("FlatLaf.style","arc: 12");
        return f;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        final JComboBox<String> cb = new JComboBox<>(items);
        cb.setPreferredSize(new Dimension(cb.getPreferredSize().width, 36));
        cb.putClientProperty("FlatLaf.style","arc: 12");
        return cb;
    }

    private JCheckBox createStyledCheckBox(String text) {
        final JCheckBox cb = new JCheckBox(text);
        cb.setFont(new Font(UIHelper.FONT_FAMILY, Font.PLAIN, 13));
        cb.setForeground(UIHelper.COLOR_TEXT_MAIN);
        cb.setOpaque(false);
        cb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return cb;
    }

    /** Menambahkan baris form (label + komponen) dan mengembalikan baris berikutnya. */
    private int addFormRow(JPanel panel, GridBagConstraints gbc, int row, String lblTxt, JComponent comp) {
        gbc.gridx=0; gbc.gridy=row; gbc.weightx=0.0;
        gbc.fill=GridBagConstraints.NONE; gbc.anchor=GridBagConstraints.WEST;
        final JLabel lbl = new JLabel(lblTxt);
        UIHelper.styleFormLabel(lbl);
        panel.add(lbl, gbc);
        gbc.gridx=0; gbc.gridy=++row; gbc.weightx=1.0;
        gbc.fill=GridBagConstraints.HORIZONTAL; gbc.insets=new Insets(2,10,8,10);
        panel.add(comp, gbc);
        gbc.insets=new Insets(6,10,6,10);
        return ++row;
    }

    private JPanel createSectionHeader(String title, String subtitle) {
        final JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(UIHelper.COLOR_CARD_ACCENT);
        pnl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIHelper.COLOR_BORDER,1,true), new EmptyBorder(10,16,10,16)));
        pnl.putClientProperty("FlatLaf.style","arc: 14");
        final JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font(UIHelper.FONT_FAMILY, Font.PLAIN,12));
        lblSub.setForeground(UIHelper.COLOR_PRIMARY_DARK);
        pnl.add(UIHelper.createHeaderLabel(title), BorderLayout.NORTH);
        pnl.add(lblSub, BorderLayout.SOUTH);
        return pnl;
    }

    // =========================================================================
    // EVENT LISTENERS
    // =========================================================================
    private void setupEventListeners() {
        final DocumentListener formListener = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { calculateTotal(); }
            @Override public void removeUpdate(DocumentEvent e)  { calculateTotal(); }
            @Override public void changedUpdate(DocumentEvent e) { calculateTotal(); }
        };
        txtOwner.getDocument().addDocumentListener(formListener);
        txtPetName.getDocument().addDocumentListener(formListener);
        txtWeight.getDocument().addDocumentListener(formListener);

        final DocumentListener cashListener = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { updateKembalian(); }
            @Override public void removeUpdate(DocumentEvent e)  { updateKembalian(); }
            @Override public void changedUpdate(DocumentEvent e) { updateKembalian(); }
        };
        txtCashReceived.getDocument().addDocumentListener(cashListener);

        cbPetType.addActionListener(e  -> calculateTotal());
        cbGrooming.addActionListener(e -> calculateTotal());
        cbDokter.addActionListener(e   -> calculateTotal());
        chkVaccine.addActionListener(e  -> calculateTotal());
        chkFood.addActionListener(e     -> calculateTotal());
        chkCheckup.addActionListener(e  -> calculateTotal());
        chkBoarding.addActionListener(e -> calculateTotal());
        chkBedah.addActionListener(e    -> calculateTotal());

        cbPayment.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) onPaymentMethodChanged();
        });

        btnCetak.addActionListener(e          -> handleCetakNota());
        btnEditPesanan.addActionListener(e    -> handleEditPemesanan());
        btnHapusPesanan.addActionListener(e   -> handleHapusPemesanan());
        btnSelesaiPesanan.addActionListener(e -> handleSelesaiPemesanan());
    }

    private void onPaymentMethodChanged() {
        final boolean isQris = isQrisSelected();
        pnlQrisCard.setVisible(isQris);
        pnlCashInputRow.setVisible(!isQris);
        pnlKembalian.setVisible(!isQris);
        if (isQris) {
            txtCashReceived.setText("");
            lblKembalianValue.setText("Rp 0");
            lblKembalianValue.setForeground(UIHelper.COLOR_PRIMARY);
        }
        updateKembalian();
        pnlInputForm.revalidate(); pnlInputForm.repaint();
        pnlRightSummary.revalidate(); pnlRightSummary.repaint();
    }

    // =========================================================================
    // HANDLER AKSI TOMBOL
    // =========================================================================
    private void handleCetakNota() {
        if (!isFormValid() || currentTotal <= 0) return;
        final String pay = getSelectedPayment();
        final double cash = parseCash();
        final double kembalian = cash - currentTotal;
        final String kemStr = "Cash".equals(pay)
                ? String.format("Metode        : Cash%nUang Diterima : Rp %,.0f%nKembalian     : Rp %,.0f", cash, kembalian)
                : "Metode        : QRIS Digital (Status: Lunas)";
        final String orderId = String.format("ORD-%03d", orderCounter++);
        final String status  = "Cash".equals(pay) ? "Lunas (Cash)" : "Lunas (QRIS)";
        final OrderRecord rec = new OrderRecord(orderId,
                txtOwner.getText().trim(), txtPetName.getText().trim(),
                getSelectedItem(cbPetType), parseWeight(), getSelectedItem(cbDokter),
                getSelectedItem(cbGrooming), chkVaccine.isSelected(), chkFood.isSelected(),
                chkCheckup.isSelected(), chkBoarding.isSelected(), chkBedah.isSelected(),
                pay, cash, currentTotal, status);
        listOrder.add(rec);
        modelRiwayat.addRow(new Object[]{rec.getId(), rec.getOwnerName(), rec.getPetName(),
                rec.getServicesSummary(), String.format("Rp %,.0f", rec.getTotalCost()), rec.getStatus()});
        JOptionPane.showMessageDialog(this,
                "=========================================\n"
                        + "    NOTA RESMI BLUEPAW VET & GROOMING\n"
                        + "=========================================\n"
                        + "No. Pemesanan : " + orderId + "\n\n"
                        + txtSummary.getText()
                        + "\n-----------------------------------------\n"
                        + kemStr + "\n-----------------------------------------\n"
                        + "TOTAL BAYAR   : Rp " + String.format("%,.0f", currentTotal)
                        + "\n\nData tersimpan di tab 'Riwayat Pemesanan'.",
                "Cetak Nota Berhasil", JOptionPane.INFORMATION_MESSAGE);
        resetForm();
    }

    private void handleEditPemesanan() {
        final int row = tblRiwayat.getSelectedRow();
        if (row == -1) { showWarning("Silakan pilih baris pemesanan yang ingin diedit!"); return; }
        final OrderRecord rec = listOrder.get(row);
        txtOwner.setText(rec.getOwnerName());
        txtPetName.setText(rec.getPetName());
        cbPetType.setSelectedItem(rec.getPetType());
        txtWeight.setText(String.valueOf(rec.getWeight()));
        cbDokter.setSelectedItem(rec.getDoctor());
        cbGrooming.setSelectedItem(rec.getGroomingPackage());
        chkVaccine.setSelected(rec.isVaccine()); chkFood.setSelected(rec.isFood());
        chkCheckup.setSelected(rec.isCheckup()); chkBoarding.setSelected(rec.isBoarding());
        chkBedah.setSelected(rec.isBedah());
        cbPayment.setSelectedItem(rec.getPaymentMethod());
        txtCashReceived.setText("Cash".equalsIgnoreCase(rec.getPaymentMethod())
                ? String.format("%.0f", rec.getCashReceived()) : "");
        modelRiwayat.removeRow(row); listOrder.remove(row);
        tabbedPane.setSelectedIndex(0); calculateTotal();
        JOptionPane.showMessageDialog(this,
                "Data [" + rec.getId() + "] dikembalikan ke form. Sesuaikan lalu klik 'Cetak Nota'.",
                "Mode Edit Aktif", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleHapusPemesanan() {
        final int row = tblRiwayat.getSelectedRow();
        if (row == -1) { showWarning("Silakan pilih baris pemesanan yang ingin dihapus!"); return; }
        final String orderId = modelRiwayat.getValueAt(row,0).toString();
        if (JOptionPane.showConfirmDialog(this, "Hapus pemesanan [" + orderId + "]?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            modelRiwayat.removeRow(row); listOrder.remove(row);
            JOptionPane.showMessageDialog(this,"Pemesanan [" + orderId + "] dihapus.","Sukses", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleSelesaiPemesanan() {
        final int row = tblRiwayat.getSelectedRow();
        if (row == -1) { showWarning("Silakan pilih baris pemesanan yang ingin diselesaikan!"); return; }
        final String orderId = modelRiwayat.getValueAt(row,0).toString();
        modelRiwayat.setValueAt("Selesai", row, 5);
        listOrder.get(row).setStatus("Selesai");
        JOptionPane.showMessageDialog(this,"Pemesanan [" + orderId + "] ditandai Selesai!","Sukses", JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================================================================
    // KALKULASI TAGIHAN
    // =========================================================================
    private void calculateTotal() {
        final String owner   = txtOwner.getText().trim();
        final String petName = txtPetName.getText().trim();
        final String wTxt    = txtWeight.getText().trim();
        boolean valid = true;
        UIHelper.setInvalidBorder(txtOwner,false); UIHelper.setInvalidBorder(txtPetName,false);
        UIHelper.setInvalidBorder(txtWeight,false);
        if (owner.isEmpty())   { UIHelper.setInvalidBorder(txtOwner,true);   valid=false; }
        if (petName.isEmpty()) { UIHelper.setInvalidBorder(txtPetName,true); valid=false; }
        double weight = 0;
        if (wTxt.isEmpty()) { UIHelper.setInvalidBorder(txtWeight,true); valid=false; }
        else {
            try {
                weight = Double.parseDouble(wTxt);
                if (weight <= 0) { UIHelper.setInvalidBorder(txtWeight,true); valid=false; }
            } catch (NumberFormatException ex) {
                UIHelper.setInvalidBorder(txtWeight,true); valid=false;
            }
        }
        if (!valid) {
            currentTotal=0; lblTotal.setText("Rp 0"); lblKembalianValue.setText("Rp 0");
            btnCetak.setEnabled(false); txtSummary.setText("Silakan lengkapi formulir dengan data yang valid.");
            return;
        }
        final double surcharge = (weight >= 5.0) ? 20_000 : 0;
        final double groomingCost = calcGroomingCost(surcharge);
        final double vaccineCost  = chkVaccine.isSelected()  ? (100_000 + surcharge) : 0;
        final double foodCost     = chkFood.isSelected()      ?  50_000 : 0;
        final double checkupCost  = chkCheckup.isSelected()  ? ( 75_000 + surcharge) : 0;
        final double boardingCost = chkBoarding.isSelected() ? 150_000 : 0;
        final double bedahCost    = chkBedah.isSelected()    ? (250_000 + surcharge) : 0;
        currentTotal = groomingCost + vaccineCost + foodCost + checkupCost + boardingCost + bedahCost;
        txtSummary.setText(buildInvoiceText(owner, petName, weight, surcharge,
                groomingCost, vaccineCost, foodCost, checkupCost, boardingCost, bedahCost));
        lblTotal.setText(String.format("Rp %,.0f", currentTotal));
        updateKembalian();
    }

    private double calcGroomingCost(double surcharge) {
        final String paket = getSelectedItem(cbGrooming);
        final double base;
        switch (paket) {
            case "Mandi Kutu":    base = 50_000;  break;
            case "Potong Bulu":   base = 40_000;  break;
            case "Potong Kuku":   base = 25_000;  break;
            case "Full Grooming": base = 100_000; break;
            default:              base = 0;        break;
        }
        return (base > 0) ? (base + surcharge) : 0;
    }

    private String buildInvoiceText(String owner, String petName, double weight, double surcharge,
                                     double grooming, double vaccine, double food,
                                     double checkup, double boarding, double bedah) {
        final StringBuilder sb = new StringBuilder();
        sb.append("DATA PASIEN:\n");
        sb.append(String.format("  Nama Pasien  : %s (%s)%n", petName, getSelectedItem(cbPetType)));
        sb.append(String.format("  Nama Pemilik : %s%n", owner));
        sb.append(String.format("  Bobot Tubuh  : %.1f kg%n", weight));
        sb.append(String.format("  Dokter Jaga  : %s%n%n", getSelectedItem(cbDokter)));
        sb.append("RINCIAN TINDAKAN & BIAYA:\n");
        if (grooming > 0) sb.append(String.format("  * %-22s: Rp %,10.0f%n","Grooming ("+getSelectedItem(cbGrooming)+")",grooming));
        if (vaccine  > 0) sb.append(String.format("  * %-22s: Rp %,10.0f%n","Vaksinasi",  vaccine));
        if (food     > 0) sb.append(String.format("  * %-22s: Rp %,10.0f%n","Pakan Khusus",food));
        if (checkup  > 0) sb.append(String.format("  * %-22s: Rp %,10.0f%n","Checkup Umum",checkup));
        if (boarding > 0) sb.append(String.format("  * %-22s: Rp %,10.0f%n","Rawat Inap", boarding));
        if (bedah    > 0) sb.append(String.format("  * %-22s: Rp %,10.0f%n","Bedah Minor", bedah));
        if (currentTotal == 0) sb.append("  (Belum ada layanan yang dipilih)\n");
        if (surcharge > 0 && currentTotal > 0) sb.append("\n*Surcharge bobot >= 5 kg (+Rp 20.000) diterapkan.");
        sb.append("\n\nMETODE PEMBAYARAN: ").append(getSelectedPayment());
        return sb.toString();
    }

    private void updateKembalian() {
        if (isQrisSelected()) {
            lblKembalianValue.setText("Rp 0"); lblKembalianValue.setForeground(UIHelper.COLOR_PRIMARY);
            revalidateCetakButton(); return;
        }
        if (txtCashReceived.getText().trim().isEmpty()) {
            lblKembalianValue.setText("Rp 0"); lblKembalianValue.setForeground(UIHelper.COLOR_PRIMARY);
            btnCetak.setEnabled(false); return;
        }
        final double kembalian = parseCash() - currentTotal;
        if (kembalian < 0) {
            lblKembalianValue.setText("Uang Kurang!"); lblKembalianValue.setForeground(new Color(220,50,50));
            btnCetak.setEnabled(false);
        } else {
            lblKembalianValue.setText(String.format("Rp %,.0f", kembalian));
            lblKembalianValue.setForeground(UIHelper.COLOR_PRIMARY); revalidateCetakButton();
        }
    }

    private void revalidateCetakButton() {
        if (!isFormValid() || currentTotal <= 0) { btnCetak.setEnabled(false); return; }
        btnCetak.setEnabled(isQrisSelected() || parseCash() >= currentTotal);
    }

    private void resetForm() {
        txtOwner.setText(""); txtPetName.setText(""); txtWeight.setText(""); txtCashReceived.setText("");
        cbPetType.setSelectedIndex(0); cbDokter.setSelectedIndex(0);
        cbGrooming.setSelectedIndex(0); cbPayment.setSelectedIndex(0);
        chkVaccine.setSelected(false); chkFood.setSelected(false); chkCheckup.setSelected(false);
        chkBoarding.setSelected(false); chkBedah.setSelected(false);
        currentTotal=0; lblTotal.setText("Rp 0"); lblKembalianValue.setText("Rp 0"); btnCetak.setEnabled(false);
        txtSummary.setText("Silakan lengkapi form di sebelah kiri\nuntuk melihat rincian tagihan medis & grooming.");
        UIHelper.setInvalidBorder(txtOwner,false); UIHelper.setInvalidBorder(txtPetName,false);
        UIHelper.setInvalidBorder(txtWeight,false);
    }

    // =========================================================================
    // UTILITY
    // =========================================================================
    private boolean isFormValid() {
        final String w = txtWeight.getText().trim();
        if (txtOwner.getText().trim().isEmpty() || txtPetName.getText().trim().isEmpty() || w.isEmpty()) return false;
        try { return Double.parseDouble(w) > 0; } catch (NumberFormatException e) { return false; }
    }

    private double parseCash() {
        try { return Double.parseDouble(txtCashReceived.getText().trim().replaceAll("[,.]","")); }
        catch (NumberFormatException e) { return 0; }
    }

    private double parseWeight() {
        try { return Double.parseDouble(txtWeight.getText().trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private String getSelectedPayment() {
        final Object s = cbPayment.getSelectedItem();
        return (s != null) ? s.toString() : "Cash";
    }

    private String getSelectedItem(JComboBox<String> cb) {
        final Object s = cb.getSelectedItem();
        return (s != null) ? s.toString() : "";
    }

    private boolean isQrisSelected() { return "QRIS".equalsIgnoreCase(getSelectedPayment()); }

    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    // =========================================================================
    // MAIN
    // =========================================================================
    public static void main(String[] args) {
        UIHelper.setupFlatLaf();
        SwingUtilities.invokeLater(() -> new PetCareGUI().setVisible(true));
    }
}
