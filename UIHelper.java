import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

/**
 * UIHelper - Kelas utilitas untuk styling FlatLaf dan Custom Blue Palette
 * sesuai tema klinik hewan profesional "PetCare Vet Clinic & Grooming".
 */
public final class UIHelper {

    // Private constructor untuk utility class
    private UIHelper() {
    }

    // =========================================================================
    // 1. PALET WARNA RESMI (HEX COLOR CODES)
    // =========================================================================
    public static final Color COLOR_BG_MAIN        = Color.decode("#FFFFFF"); // Background Utama
    public static final Color COLOR_CARD_ACCENT    = Color.decode("#c0e6fd"); // Background Panel/Kartu (Biru Terang)
    public static final Color COLOR_BORDER         = Color.decode("#80aad3"); // Border / Garis Pemisah
    public static final Color COLOR_BORDER_DARK    = Color.decode("#5b86b6"); // Elemen Sekunder
    public static final Color COLOR_PRIMARY        = Color.decode("#3f6593"); // Background Tombol Utama (Primary)
    public static final Color COLOR_PRIMARY_DARK   = Color.decode("#1b3554"); // Biru Tua (Hover / Secondary Accent)
    public static final Color COLOR_TEXT_MAIN      = Color.decode("#000f22"); // Teks Utama (Biru Sangat Gelap)
    public static final Color COLOR_TEXT_WHITE     = Color.decode("#FFFFFF"); // Teks Putih

    public static final String FONT_FAMILY         = "Segoe UI";

    // =========================================================================
    // 2. INISIALISASI UIMANAGER SEBELUM FLATLAF.SETUP()
    // =========================================================================
    public static void setupFlatLaf() {
        try {
            // A. Terapkan konfigurasi UIManager.put SEBELUM Look-and-Feel dimuat
            // Background Utama & Panel / Kartu
            UIManager.put("Panel.background", COLOR_CARD_ACCENT);
            UIManager.put("RootPane.background", COLOR_BG_MAIN);
            UIManager.put("ScrollPane.background", COLOR_BG_MAIN);
            UIManager.put("Viewport.background", COLOR_BG_MAIN);

            // TabbedPane Styling
            UIManager.put("TabbedPane.background", COLOR_BG_MAIN);
            UIManager.put("TabbedPane.selectedBackground", COLOR_CARD_ACCENT);
            UIManager.put("TabbedPane.selectedForeground", COLOR_TEXT_MAIN);
            UIManager.put("TabbedPane.foreground", COLOR_TEXT_MAIN);
            UIManager.put("TabbedPane.hoverColor", COLOR_CARD_ACCENT);
            UIManager.put("TabbedPane.underlineColor", COLOR_PRIMARY);
            UIManager.put("TabbedPane.focusColor", COLOR_BORDER);
            UIManager.put("TabbedPane.tabArc", 12);
            UIManager.put("TabbedPane.tabInsets", new Insets(10, 24, 10, 24));
            UIManager.put("TabbedPane.font", new Font(FONT_FAMILY, Font.BOLD, 14));

            // Tombol Utama (Primary Button)
            UIManager.put("Button.background", COLOR_PRIMARY);
            UIManager.put("Button.foreground", COLOR_TEXT_WHITE);
            UIManager.put("Button.hoverBackground", COLOR_PRIMARY_DARK);
            UIManager.put("Button.focusedBackground", COLOR_PRIMARY_DARK);
            UIManager.put("Button.borderColor", COLOR_BORDER);
            UIManager.put("Button.arc", 16);

            // Teks Utama (Foreground)
            UIManager.put("Label.foreground", COLOR_TEXT_MAIN);
            UIManager.put("CheckBox.foreground", COLOR_TEXT_MAIN);
            UIManager.put("RadioButton.foreground", COLOR_TEXT_MAIN);
            UIManager.put("Table.foreground", COLOR_TEXT_MAIN);
            UIManager.put("TableHeader.foreground", COLOR_TEXT_MAIN);
            UIManager.put("TitledBorder.titleColor", COLOR_TEXT_MAIN);

            // Border & Elemen Sekunder
            UIManager.put("Component.borderColor", COLOR_BORDER);
            UIManager.put("Component.focusColor", COLOR_BORDER_DARK);
            UIManager.put("Component.accentColor", COLOR_PRIMARY);
            UIManager.put("Table.gridColor", COLOR_BORDER);
            UIManager.put("TableHeader.separatorColor", COLOR_BORDER);
            UIManager.put("Separator.background", COLOR_BORDER);
            UIManager.put("Separator.foreground", COLOR_BORDER_DARK);

            // Input Fields & Arcs
            UIManager.put("TextField.background", COLOR_BG_MAIN);
            UIManager.put("TextField.foreground", COLOR_TEXT_MAIN);
            UIManager.put("ComboBox.background", COLOR_BG_MAIN);
            UIManager.put("ComboBox.foreground", COLOR_TEXT_MAIN);
            UIManager.put("Component.arc", 14);
            UIManager.put("TextComponent.arc", 14);
            UIManager.put("ComboBox.arc", 14);
            UIManager.put("CheckBox.arc", 8);

            // Insets & Font Default
            UIManager.put("TextComponent.margin", new Insets(8, 14, 8, 14));
            UIManager.put("ComboBox.padding", new Insets(6, 12, 6, 12));
            UIManager.put("defaultFont", new Font(FONT_FAMILY, Font.PLAIN, 14));

            // B. Inisialisasi tema FlatMacLightLaf setelah semua UIManager.put selesai
            FlatMacLightLaf.setup();

        } catch (Exception ex) {
            System.err.println("Gagal menginisialisasi FlatLaf: " + ex.getMessage());
        }
    }

    // =========================================================================
    // 3. FACTORY & STYLING KOMPONEN
    // =========================================================================
    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 22));
        label.setForeground(COLOR_TEXT_MAIN);
        return label;
    }

    public static void styleFormLabel(JLabel label) {
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        label.setForeground(COLOR_TEXT_MAIN);
    }

    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        button.setForeground(COLOR_TEXT_WHITE);
        button.setBackground(COLOR_PRIMARY);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(170, 42));
        button.putClientProperty("JButton.buttonType", "roundRect");
        return button;
    }

    public static JButton createSecondaryButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        button.setForeground(COLOR_TEXT_WHITE);
        button.setBackground(bg);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(160, 40));
        button.putClientProperty("JButton.buttonType", "roundRect");
        return button;
    }

    public static JLabel createTotalLabel() {
        JLabel label = new JLabel("Rp 0");
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 32));
        label.setForeground(COLOR_PRIMARY_DARK);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

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
    // 4. IMAGE UTILITIES (IMAGE SCALING PROPORSIONAL)
    // =========================================================================
    public static ImageIcon scaleImage(ImageIcon srcIcon, int targetWidth, int targetHeight) {
        if (srcIcon == null || srcIcon.getImage() == null) return null;
        Image rawImage = srcIcon.getImage();
        Image scaledImage = rawImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public static ImageIcon loadAndScaleImage(String path, int targetWidth, int targetHeight) {
        try {
            java.net.URL resourceUrl = UIHelper.class.getResource("/" + path);
            ImageIcon rawIcon = null;
            if (resourceUrl != null) {
                rawIcon = new ImageIcon(resourceUrl);
            } else {
                File file = new File(path);
                if (file.exists()) {
                    rawIcon = new ImageIcon(file.getAbsolutePath());
                }
            }
            if (rawIcon != null && rawIcon.getImage() != null) {
                return scaleImage(rawIcon, targetWidth, targetHeight);
            }
        } catch (Exception ex) {
            System.err.println("Gagal memuat gambar: " + ex.getMessage());
        }
        return null;
    }

    public static void setInvalidBorder(JComponent comp, boolean isInvalid) {
        if (isInvalid) {
            comp.putClientProperty("JComponent.outline", "error");
        } else {
            comp.putClientProperty("JComponent.outline", null);
        }
        comp.repaint();
    }
}
