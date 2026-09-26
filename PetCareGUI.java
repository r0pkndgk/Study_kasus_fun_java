import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PetCareGUI extends JFrame {
    private JTextField txtOwner, txtPetName, txtWeight;
    private JComboBox<String> cbPetType, cbGrooming;
    private JCheckBox chkVaccine, chkFood;
    private JTextArea txtReceipt;

    public PetCareGUI() {
        setTitle("PetCare Vet Clinic & Grooming");
        setSize(550, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel Input
        JPanel pnlInput = new JPanel(new GridLayout(7, 2, 10, 10));
        pnlInput.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        pnlInput.add(new JLabel("Nama Pemilik:"));
        txtOwner = new JTextField();
        pnlInput.add(txtOwner);

        pnlInput.add(new JLabel("Nama Hewan:"));
        txtPetName = new JTextField();
        pnlInput.add(txtPetName);

        pnlInput.add(new JLabel("Jenis Hewan:"));
        String[] types = {"Kucing", "Anjing", "Kelinci"};
        cbPetType = new JComboBox<>(types);
        pnlInput.add(cbPetType);

        pnlInput.add(new JLabel("Bobot Hewan (kg):"));
        txtWeight = new JTextField();
        pnlInput.add(txtWeight);

        pnlInput.add(new JLabel("Paket Grooming:"));
        String[] groomingPackages = {"Tidak Ada", "Mandi Kutu", "Potong Bulu", "Potong Kuku"};
        cbGrooming = new JComboBox<>(groomingPackages);
        pnlInput.add(cbGrooming);

        pnlInput.add(new JLabel("Layanan Tambahan:"));
        JPanel pnlExtras = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        chkVaccine = new JCheckBox("Vaksinasi");
        chkFood = new JCheckBox("Pakan Hewan");
        pnlExtras.add(chkVaccine);
        pnlExtras.add(chkFood);
        pnlInput.add(pnlExtras);

        JButton btnCalculate = new JButton("Hitung Tarif & Cetak Nota");
        btnCalculate.setBackground(new Color(70, 130, 180));
        btnCalculate.setForeground(Color.WHITE);
        btnCalculate.setFont(new Font("Arial", Font.BOLD, 12));
        
        pnlInput.add(new JLabel()); // Space
        pnlInput.add(btnCalculate);

        add(pnlInput, BorderLayout.NORTH);

        // Panel Receipt
        txtReceipt = new JTextArea();
        txtReceipt.setEditable(false);
        txtReceipt.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtReceipt.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(txtReceipt);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Nota Pemeriksaan / Layanan"));
        add(scrollPane, BorderLayout.CENTER);

        // Action Listener
        btnCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateAndPrint();
            }
        });
    }

    private void calculateAndPrint() {
        try {
            String owner = txtOwner.getText().trim();
            String petName = txtPetName.getText().trim();
            
            if (owner.isEmpty() || petName.isEmpty() || txtWeight.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mohon lengkapi semua data input!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String petType = cbPetType.getSelectedItem().toString();
            double weight = Double.parseDouble(txtWeight.getText().trim());

            if (weight <= 0) {
                JOptionPane.showMessageDialog(this, "Bobot hewan harus lebih besar dari 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Instansiasi Object PasienHewan
            PasienHewan pasien = new PasienHewan(petName, petType, owner, weight);

            String selectedGrooming = cbGrooming.getSelectedItem().toString();
            double baseGroomingCost = 0;

            switch (selectedGrooming) {
                case "Mandi Kutu": baseGroomingCost = 50000; break;
                case "Potong Bulu": baseGroomingCost = 40000; break;
                case "Potong Kuku": baseGroomingCost = 25000; break;
            }

            // Kategori Bobot: Jika >= 5kg maka ada biaya tambahan layanan
            double weightSurcharge = (weight >= 5.0) ? 20000 : 0;
            
            double groomingCost = 0;
            LayananGrooming grooming = null;
            if (baseGroomingCost > 0) {
                groomingCost = baseGroomingCost + weightSurcharge;
                // Instansiasi Object LayananGrooming
                grooming = new LayananGrooming(selectedGrooming, groomingCost);
            }

            // Tarif vaksinasi (misal jika >5kg biaya vaksin juga nambah)
            double vaccineCost = chkVaccine.isSelected() ? (100000 + weightSurcharge) : 0;
            
            // Tarif Pakan (harga flat)
            double foodCost = chkFood.isSelected() ? 50000 : 0;

            double totalCost = groomingCost + vaccineCost + foodCost;

            // Cetak Nota
            StringBuilder nota = new StringBuilder();
            nota.append("==================================================\n");
            nota.append("           PETCARE VET CLINIC & GROOMING          \n");
            nota.append("==================================================\n");
            nota.append(String.format("Nama Pemilik   : %s\n", pasien.getNamaOwner()));
            nota.append(String.format("Nama Hewan     : %s\n", pasien.getNamaPeliharaan()));
            nota.append(String.format("Jenis Hewan    : %s\n", pasien.getJenisHewan()));
            nota.append(String.format("Bobot Hewan    : %.2f kg\n", pasien.getBobotKg()));
            nota.append(String.format("Kategori Bobot : %s\n", weight >= 5.0 ? ">= 5 kg (Biaya Tambahan Rp 20.000/layanan)" : "< 5 kg (Normal)"));
            nota.append("--------------------------------------------------\n");
            
            if (grooming != null) {
                nota.append(String.format("Layanan Grooming (%s)  : Rp %,.2f\n", grooming.getPaket(), grooming.getBiayaLayanan()));
            } else {
                nota.append("Layanan Grooming           : Tidak Ada\n");
            }

            if (chkVaccine.isSelected()) {
                nota.append(String.format("Vaksinasi                  : Rp %,.2f\n", vaccineCost));
            }

            if (chkFood.isSelected()) {
                nota.append(String.format("Pembelian Pakan Hewan      : Rp %,.2f\n", foodCost));
            }
            
            nota.append("--------------------------------------------------\n");
            nota.append(String.format("TOTAL TAGIHAN              : Rp %,.2f\n", totalCost));
            nota.append("==================================================\n");
            nota.append("       Terima Kasih Atas Kepercayaan Anda!        \n");

            txtReceipt.setText(nota.toString());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Format bobot hewan tidak valid! Masukkan angka yang benar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new PetCareGUI().setVisible(true);
        });
    }
}
