import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;

/**
 * UIHelper - Kelas utilitas untuk styling FlatLaf dan Custom Blue Palette
 * sesuai tema klinik hewan profesional "PetCare Vet Clinic & Grooming".
 */
public class UIHelper {

    // =========================================================================
    // 1. PALET WARNA RESMI (HEX COLOR CODES)
    // =========================================================================
    // Background Utama / Panel: Putih (#FFFFFF) dipadu aksen biru terang (#c0e6fd)
    public static final Color COLOR_BG_MAIN        = Color.decode("#FFFFFF"); // Latar belakang utama
    public static final Color COLOR_CARD_ACCENT    = Color.decode("#c0e6fd"); // Aksen biru paling terang (Card/Header)
    public static final Color COLOR_BG_APP         = Color.decode("#F0F6FA"); // Latar belakang window dashboard
    
    // Border / Garis Pemisah / Elemen Sekunder
    public static final Color COLOR_BORDER         = Color.decode("#80aad3"); // Border utama / garis pemisah
    public static final Color COLOR_BORDER_DARK    = Color.decode("#5b86b6"); // Elemen sekunder / border tegas
    
    // Tombol Utama (Primary Button)
    public static final Color COLOR_PRIMARY        = Color.decode("#3f6593"); // Biru dominan tombol utama
    public static final Color COLOR_PRIMARY_DARK   = Color.decode("#1b3554"); // Biru tua tombol hover / aksen
    
    // Teks Utama (Heading, Label Penting)
    public static final Color COLOR_TEXT_MAIN      = Color.decode("#000f22"); // Biru paling gelap (bukan hitam murni)
    public static final Color COLOR_TEXT_MUTED     = Color.decode("#3f6593"); // Biru sedang untuk sub-teks / label sekunder
    public static final Color COLOR_TEXT_WHITE     = Color.decode("#FFFFFF"); // Teks putih untuk tombol utama

    // Font Keluarga Sans-Serif Bersih
    public static final String FONT_FAMILY         = "Segoe UI";

    // =========================================================================
    // 2. INISIALISASI TEMA FLATLAF (GLOBAL STYLING & ROUNDED CORNERS)
    // =========================================================================
    public static void setupFlatLaf() {
        try {
            // Gunakan tema FlatMacLightLaf untuk tampilan modern minimalis
            UIManager.setLookAndFeel(new FlatMacLightLaf());

            // Terapkan Aksen Warna Utama
            UIManager.put("Component.accentColor", COLOR_PRIMARY);
            UIManager.put("Component.focusColor", COLOR_BORDER);

            // Konfigurasi Sudut Melengkung (Rounded Corners)
            UIManager.put("Button.arc", 16);
            UIManager.put("Component.arc", 14);
            UIManager.put("ProgressBar.arc", 14);
            UIManager.put("TextComponent.arc", 14);
            UIManager.put("ComboBox.arc", 14);
            UIManager.put("CheckBox.arc", 8);

            // Inner Padding untuk Komponen Input Form
            UIManager.put("TextComponent.margin", new Insets(8, 14, 8, 14));
            UIManager.put("ComboBox.padding", new Insets(6, 12, 6, 12));

            // Warna Seleksi
            UIManager.put("TextField.selectionBackground", COLOR_ACCENT_LIGHT());
            UIManager.put("TextField.selectionForeground", COLOR_TEXT_MAIN);

            // Font Default Aplikasi
            UIManager.put("defaultFont", new Font(FONT_FAMILY, Font.PLAIN, 14));

        } catch (Exception ex) {
            System.err.println("Gagal menginisialisasi FlatLaf: " + ex.getMessage());
        }
    }

    public static Color COLOR_ACCENT_LIGHT() {
        return COLOR_CARD_ACCENT;
    }

    // =========================================================================
    // 3. FACTORY & STYLING KOMPONEN
    // =========================================================================

    /**
     * Membuat Label Judul Bagian (Heading) dengan warna #000f22 dan font Sans-Serif Bold.
     */
    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 22));
        label.setForeground(COLOR_TEXT_MAIN);
        return label;
    }

    /**
     * Memberikan styling seragam pada label form input.
     */
    public static void styleFormLabel(JLabel label) {
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        label.setForeground(COLOR_TEXT_MAIN);
    }

    /**
     * Membuat Tombol Utama (Primary Button) dengan warna biru dominan #3f6593,
     * teks putih, rounded corners (roundRect), dan cursor tangan.
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, 15));
        button.setForeground(COLOR_TEXT_WHITE);
        button.setBackground(COLOR_PRIMARY);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(220, 48));

        // Properti FlatLaf untuk rounded button
        button.putClientProperty("JButton.buttonType", "roundRect");
        button.putClientProperty("FlatLaf.styleClass", "primary");

        return button;
    }

    /**
     * Membuat Label Total Pembayaran (Besar, Bold, dan tegas).
     */
    public static JLabel createTotalLabel() {
        JLabel label = new JLabel("Rp 0");
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 32));
        label.setForeground(COLOR_PRIMARY_DARK); // Biru tua #1b3554
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    /**
     * Membuat Border bergaya Card dengan sudut melengkung dan garis border #80aad3.
     */
    public static Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        );
    }

    /**
     * Membuat Panel Bergaya 'Card' dengan background putih/aksen dan border #80aad3.
     */
    public static JPanel createCardPanel(Color bgColor) {
        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(16, 18, 16, 18)
        ));
        panel.putClientProperty("FlatLaf.style", "arc: 18");
        return panel;
    }

    // =========================================================================
    // 4. IMAGE UTILITIES (IMAGE SCALING PROPORSONAL)
    // =========================================================================

    /**
     * Metode utilitas sederhana untuk me-resize gambar agar ukurannya proporsional.
     * Menggunakan Image.SCALE_SMOOTH untuk menghasilkan rendering yang tajam dan halus.
     *
     * @param srcIcon       ImageIcon asli yang akan di-resize
     * @param targetWidth   Lebar tujuan dalam pixel
     * @param targetHeight  Tinggi tujuan dalam pixel
     * @return ImageIcon baru yang telah diskalakan, atau null jika srcIcon invalid
     */
    public static ImageIcon scaleImage(ImageIcon srcIcon, int targetWidth, int targetHeight) {
        if (srcIcon == null || srcIcon.getImage() == null) {
            return null;
        }
        Image rawImage = srcIcon.getImage();
        Image scaledImage = rawImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    /**
     * Memuat file gambar dari sistem berkas lokal atau classpath, lalu me-resize
     * ke ukuran target secara proporsional.
     *
     * @param path          Path ke file gambar (relatif terhadap direktori kerja atau absolut)
     * @param targetWidth   Lebar target dalam pixel
     * @param targetHeight  Tinggi target dalam pixel
     * @return ImageIcon berukuran proporsional, atau null jika file tidak ditemukan
     */
    public static ImageIcon loadAndScaleImage(String path, int targetWidth, int targetHeight) {
        try {
            // 1. Coba muat dari classpath resource
            java.net.URL resourceUrl = UIHelper.class.getResource("/" + path);
            ImageIcon rawIcon = null;

            if (resourceUrl != null) {
                rawIcon = new ImageIcon(resourceUrl);
            } else {
                // 2. Fallback: muat dari filesystem direktori proyek
                File file = new File(path);
                if (file.exists()) {
                    rawIcon = new ImageIcon(file.getAbsolutePath());
                }
            }

            if (rawIcon != null && rawIcon.getImage() != null) {
                return scaleImage(rawIcon, targetWidth, targetHeight);
            }
        } catch (Exception ex) {
            System.err.println("Gagal memuat/skala gambar dari path: " + path + " (" + ex.getMessage() + ")");
        }
        return null;
    }

    // =========================================================================
    // 5. VALIDASI VISUAL FLATLAF
    // =========================================================================
    public static void setInvalidBorder(JComponent comp, boolean isInvalid) {
        if (isInvalid) {
            comp.putClientProperty("JComponent.outline", "error");
        } else {
            comp.putClientProperty("JComponent.outline", null);
        }
        comp.repaint();
    }
}
