import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PetCareGUI extends JFrame {
    private JTextField txtOwner, txtPetName, txtWeight;
    private JComboBox<String> cbPetType, cbGrooming;
    private JCheckBox chkVaccine, chkFood;
    private JTextArea txtSummary;
    private JLabel lblTotal;
    private JButton btnCetak;

    private double currentTotal = 0;
    private PasienHewan currentPasien;
    private LayananGrooming currentGrooming;

    public PetCareGUI() {
        // 1. Inisialisasi Tema FlatLaf Light
        UIHelper.setupFlatLaf();

        setTitle("PetCare Clinic & Grooming - Dashboard");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Root Panel
        JPanel rootPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        rootPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        rootPanel.setBackground(new Color(245, 247, 250)); // Light modern background
        setContentPane(rootPanel);

        // --- KIRI: FORM INPUT (refactored with GridBagLayout + explicit insets) ---
        JPanel pnlForm = new JPanel(new BorderLayout());
        pnlForm.setOpaque(false);
        
        // Panel input menggunakan GridBagLayout dengan insets eksplisit
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        pnlInput.putClientProperty("FlatLaf.style", "arc: 20");
        
        GridBagConstraints gbc = new GridBagConstraints();
        // Insets default: top=10, left=15, bottom=15, right=15 (sesuai kriteria)
        Insets defaultInsets = new Insets(10, 15, 15, 15);
        gbc.insets = defaultInsets;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Header di baris pertama
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        JLabel lblHeaderForm = UIHelper.createHeaderLabel("🐶 Registrasi Layanan");
        pnlInput.add(lblHeaderForm, gbc);
        
        // Row 1: Nama Pemilik
        gbc.gridy = ++row; gbc.weightx = 1.0;
        gbc.insets = defaultInsets;
        addFormRow(pnlInput, gbc, "Nama Pemilik", txtOwner = new JTextField());
        
        // Row 2: Nama Hewan
        gbc.gridy = ++row; gbc.weightx = 1.0;
        gbc.insets = defaultInsets;
        addFormRow(pnlInput, gbc, "Nama Hewan", txtPetName = new JTextField());
        
        // Row 3: Jenis Hewan
        gbc.gridy = ++row; gbc.weightx = 1.0;
        gbc.insets = defaultInsets;
        addFormRow(pnlInput, gbc, "Jenis Hewan", cbPetType = new JComboBox<>(new String[]{"Kucing", "Anjing", "Kelinci", "Burung", "Lainnya"}));
        
        // Row 4: Bobot Hewan
        gbc.gridy = ++row; gbc.weightx = 1.0;
        gbc.insets = defaultInsets;
        addFormRow(pnlInput, gbc, "Bobot Hewan (kg)", txtWeight = new JTextField());
        
        // Row 5: Paket Grooming
        gbc.gridy = ++row; gbc.weightx = 1.0;
        gbc.insets = defaultInsets;
        addFormRow(pnlInput, gbc, "Paket Grooming", cbGrooming = new JComboBox<>(new String[]{"Tidak Ada", "Mandi Kutu", "Potong Bulu", "Potong Kuku", "Full Grooming"}));
        
        // Row 6: Layanan Tambahan
        gbc.gridy = ++row; gbc.weightx = 1.0;
        gbc.insets = defaultInsets;
        JPanel pnlExtras = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlExtras.setOpaque(false);
        chkVaccine = new JCheckBox("Vaksinasi");
        chkFood = new JCheckBox("Pakan");
        chkVaccine.setFont(new Font("Inter", Font.PLAIN, 14));
        chkFood.setFont(new Font("Inter", Font.PLAIN, 14));
        pnlExtras.add(chkVaccine);
        pnlExtras.add(Box.createRigidArea(new Dimension(20, 0)));
        pnlExtras.add(chkFood);
        addFormRow(pnlInput, gbc, "Layanan Tambahan", pnlExtras);

        // Filler dengan weighty = 1.0 untuk mendorong form ke atas
        gbc.gridy = ++row; gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.insets = defaultInsets;
        pnlInput.add(new JLabel(), gbc);

        pnlForm.add(pnlInput, BorderLayout.CENTER);
        rootPanel.add(pnlForm);


        // --- KANAN: LIVE SUMMARY & INVOICE ---
        JPanel pnlRight = new JPanel(new BorderLayout(0, 20));
        pnlRight.setOpaque(false);
        
        JLabel lblHeaderSummary = UIHelper.createHeaderLabel("🧾 Ringkasan Tagihan");
        lblHeaderSummary.setBorder(new EmptyBorder(0, 0, 0, 0));
        pnlRight.add(lblHeaderSummary, BorderLayout.NORTH);

        JPanel pnlSummaryCard = new JPanel(new BorderLayout());
        pnlSummaryCard.setBackground(Color.WHITE);
        pnlSummaryCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));
        pnlSummaryCard.putClientProperty("FlatLaf.style", "arc: 20");

        txtSummary = new JTextArea();
        txtSummary.setEditable(false);
        txtSummary.setFont(new Font("Consolas", Font.PLAIN, 15));
        txtSummary.setForeground(new Color(60, 70, 80));
        txtSummary.setOpaque(false);
        txtSummary.setText("Silakan lengkapi form di sebelah kiri\nuntuk melihat ringkasan tagihan.");
        
        pnlSummaryCard.add(txtSummary, BorderLayout.CENTER);

        // Bagian Bawah Summary (Total & Tombol)
        JPanel pnlBottomSummary = new JPanel(new BorderLayout(0, 15));
        pnlBottomSummary.setOpaque(false);
        
        JPanel pnlTotal = new JPanel(new BorderLayout());
        pnlTotal.setOpaque(false);
        JLabel lblTotalTitle = new JLabel("TOTAL PEMBAYARAN");
        lblTotalTitle.setFont(new Font("Inter", Font.BOLD, 14));
        lblTotalTitle.setForeground(new Color(120, 130, 140));
        
        lblTotal = UIHelper.createTotalLabel();
        
        pnlTotal.add(lblTotalTitle, BorderLayout.NORTH);
        pnlTotal.add(lblTotal, BorderLayout.CENTER);
        pnlTotal.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));
        pnlTotal.add(Box.createVerticalStrut(15), BorderLayout.NORTH);

        btnCetak = UIHelper.createPrimaryButton("Cetak Nota");
        btnCetak.setEnabled(false); // Disabled by default

        pnlBottomSummary.add(pnlTotal, BorderLayout.CENTER);
        pnlBottomSummary.add(btnCetak, BorderLayout.SOUTH);

        pnlSummaryCard.add(pnlBottomSummary, BorderLayout.SOUTH);
        pnlRight.add(pnlSummaryCard, BorderLayout.CENTER);
        
        rootPanel.add(pnlRight);

        // --- SETUP LISTENER (INTERAKTIVITAS REAL-TIME) ---
        setupRealTimeListeners();
        
        // Cetak Action
        btnCetak.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Nota berhasil dicetak!\n\n" + txtSummary.getText() + "\nTotal: Rp " + String.format("%,.0f", currentTotal), 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        JLabel label = new JLabel(labelText);
        UIHelper.styleFormLabel(label);
        panel.add(label, gbc);

        gbc.gridy = row + 1; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;
        // Beri margin bawah antar baris
        gbc.insets = new Insets(5, 10, 15, 10);
        panel.add(comp, gbc);
        gbc.insets = new Insets(10, 10, 0, 10); // Reset for next label
    }

    private void setupRealTimeListeners() {
        // Document Listener untuk semua input teks
        DocumentListener docListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calculateLive(); }
            public void removeUpdate(DocumentEvent e) { calculateLive(); }
            public void changedUpdate(DocumentEvent e) { calculateLive(); }
        };
        
        txtOwner.getDocument().addDocumentListener(docListener);
        txtPetName.getDocument().addDocumentListener(docListener);
        txtWeight.getDocument().addDocumentListener(docListener);

        // Action Listener untuk combo box & checkbox
        cbPetType.addActionListener(e -> calculateLive());
        cbGrooming.addActionListener(e -> calculateLive());
        chkVaccine.addActionListener(e -> calculateLive());
        chkFood.addActionListener(e -> calculateLive());
    }

    private void calculateLive() {
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
            lblTotal.setText("Rp 0");
            txtSummary.setText("Data belum lengkap atau format tidak valid.\nSilakan lengkapi form dengan benar.");
            return;
        }

        // --- KALKULASI ---
        String petType = cbPetType.getSelectedItem().toString();
        currentPasien = new PasienHewan(petName, petType, owner, weight);
        
        String selectedGrooming = cbGrooming.getSelectedItem().toString();
        double baseGroomingCost = 0;

        switch (selectedGrooming) {
            case "Mandi Kutu": baseGroomingCost = 50000; break;
            case "Potong Bulu": baseGroomingCost = 40000; break;
            case "Potong Kuku": baseGroomingCost = 25000; break;
            case "Full Grooming": baseGroomingCost = 100000; break;
        }

        double weightSurcharge = (weight >= 5.0) ? 20000 : 0;
        double groomingCost = 0;
        
        if (baseGroomingCost > 0) {
            groomingCost = baseGroomingCost + weightSurcharge;
            currentGrooming = new LayananGrooming(selectedGrooming, groomingCost);
        } else {
            currentGrooming = null;
        }

        double vaccineCost = chkVaccine.isSelected() ? (100000 + weightSurcharge) : 0;
        double foodCost = chkFood.isSelected() ? 50000 : 0;
        
        currentTotal = groomingCost + vaccineCost + foodCost;

        // --- UPDATE SUMMARY UI ---
        StringBuilder sb = new StringBuilder();
        sb.append("PASIEN:\n");
        sb.append(String.format("• Nama    : %s (%s)\n", currentPasien.getNamaPeliharaan(), currentPasien.getJenisHewan()));
        sb.append(String.format("• Owner   : %s\n", currentPasien.getNamaOwner()));
        sb.append(String.format("• Bobot   : %.1f kg\n\n", currentPasien.getBobotKg()));
        
        sb.append("RINCIAN BIAYA:\n");
        if (currentGrooming != null) {
            sb.append(String.format("• %-20s : Rp %,.0f\n", "Grooming (" + currentGrooming.getPaket() + ")", currentGrooming.getBiayaLayanan()));
        }
        if (chkVaccine.isSelected()) {
            sb.append(String.format("• %-20s : Rp %,.0f\n", "Vaksinasi", vaccineCost));
        }
        if (chkFood.isSelected()) {
            sb.append(String.format("• %-20s : Rp %,.0f\n", "Pakan", foodCost));
        }
        
        if (weight >= 5.0 && (currentGrooming != null || chkVaccine.isSelected())) {
            sb.append("\n*Surcharge >5kg diterapkan pada \n layanan medis/grooming.");
        }

        txtSummary.setText(sb.toString());
        lblTotal.setText(String.format("Rp %,.0f", currentTotal));
        
        btnCetak.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PetCareGUI().setVisible(true);
        });
    }
}

