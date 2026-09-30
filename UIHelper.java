import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * UIHelper - Kelas utilitas statis untuk styling FlatLaf, Custom Blue Palette,
 * dan pembuatan komponen UI standar pada aplikasi "BluePaw Vet & Grooming".
 *
 * <p>Semua konstanta warna, font, dan metode factory komponen GUI dipusatkan
 * di kelas ini agar mudah dikelola dan diubah secara konsisten.</p>
 */
public final class UIHelper {

    /** Mencegah instansiasi kelas utilitas. */
    private UIHelper() { }

    // =========================================================================
    // SECTION 1 - PALET WARNA RESMI (CUSTOM BLUE PALETTE)
    // =========================================================================

    /** Background utama aplikasi (putih bersih). */
    public static final Color COLOR_BG_MAIN      = Color.decode("#FFFFFF");

    /** Background panel kartu / form (biru terang). */
    public static final Color COLOR_CARD_ACCENT  = Color.decode("#c0e6fd");

    /** Warna border / garis pemisah elemen. */
    public static final Color COLOR_BORDER       = Color.decode("#80aad3");

    /** Elemen sekunder dan aksen agak gelap. */
    public static final Color COLOR_BORDER_DARK  = Color.decode("#5b86b6");

    /** Warna tombol aksi utama (Primary Button). */
    public static final Color COLOR_PRIMARY      = Color.decode("#3f6593");

    /** Biru tua untuk hover, judul, dan teks heading. */
    public static final Color COLOR_PRIMARY_DARK = Color.decode("#1b3554");

    /** Teks utama (biru sangat gelap, hampir hitam). */
    public static final Color COLOR_TEXT_MAIN    = Color.decode("#000f22");

    /** Teks di atas tombol / latar gelap. */
    public static final Color COLOR_TEXT_WHITE   = Color.decode("#FFFFFF");

    /** Nama font utama aplikasi. */
    public static final String FONT_FAMILY       = "Segoe UI";

    // =========================================================================
    // SECTION 2 - INISIALISASI LOOK & FEEL (dipanggil SEBELUM komponen dibuat)
    // =========================================================================

    /**
     * Mengkonfigurasi UIManager dengan palet warna kustom dan menginisialisasi
     * FlatMacLightLaf sebagai Look and Feel aktif.
     * WAJIB dipanggil sebelum membuat instance komponen Swing apapun.
     */
    public static void setupFlatLaf() {
        try {
            applyColorPalette();
            applyComponentStyling();
            FlatMacLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("[UIHelper] Gagal menginisialisasi FlatLaf: " + ex.getMessage());
        }
    }

    /** Menerapkan palet warna ke komponen-komponen Swing global. */
    private static void applyColorPalette() {
        UIManager.put("Panel.background",              COLOR_CARD_ACCENT);
        UIManager.put("RootPane.background",           COLOR_BG_MAIN);
        UIManager.put("ScrollPane.background",         COLOR_BG_MAIN);
        UIManager.put("Viewport.background",           COLOR_BG_MAIN);
        UIManager.put("TabbedPane.background",         COLOR_BG_MAIN);
        UIManager.put("TabbedPane.selectedBackground", COLOR_CARD_ACCENT);
        UIManager.put("TabbedPane.selectedForeground", COLOR_TEXT_MAIN);
        UIManager.put("TabbedPane.foreground",         COLOR_TEXT_MAIN);
        UIManager.put("TabbedPane.hoverColor",         COLOR_CARD_ACCENT);
        UIManager.put("TabbedPane.underlineColor",     COLOR_PRIMARY);
        UIManager.put("TabbedPane.focusColor",         COLOR_BORDER);
        UIManager.put("Button.background",             COLOR_PRIMARY);
        UIManager.put("Button.foreground",             COLOR_TEXT_WHITE);
        UIManager.put("Button.hoverBackground",        COLOR_PRIMARY_DARK);
        UIManager.put("Button.focusedBackground",      COLOR_PRIMARY_DARK);
        UIManager.put("Button.borderColor",            COLOR_BORDER);
        UIManager.put("Label.foreground",              COLOR_TEXT_MAIN);
        UIManager.put("CheckBox.foreground",           COLOR_TEXT_MAIN);
        UIManager.put("RadioButton.foreground",        COLOR_TEXT_MAIN);
        UIManager.put("Table.foreground",              COLOR_TEXT_MAIN);
        UIManager.put("TableHeader.foreground",        COLOR_TEXT_MAIN);
        UIManager.put("TitledBorder.titleColor",       COLOR_TEXT_MAIN);
        UIManager.put("Component.borderColor",         COLOR_BORDER);
        UIManager.put("Component.focusColor",          COLOR_BORDER_DARK);
        UIManager.put("Component.accentColor",         COLOR_PRIMARY);
        UIManager.put("Table.gridColor",               COLOR_BORDER);
        UIManager.put("TableHeader.separatorColor",    COLOR_BORDER);
        UIManager.put("Separator.background",          COLOR_BORDER);
        UIManager.put("Separator.foreground",          COLOR_BORDER_DARK);
        UIManager.put("TextField.background",          COLOR_BG_MAIN);
        UIManager.put("TextField.foreground",          COLOR_TEXT_MAIN);
        UIManager.put("ComboBox.background",           COLOR_BG_MAIN);
        UIManager.put("ComboBox.foreground",           COLOR_TEXT_MAIN);
    }

    /** Menerapkan properti gaya (arc, insets, font) ke komponen. */
    private static void applyComponentStyling() {
        UIManager.put("TabbedPane.tabArc",    12);
        UIManager.put("TabbedPane.tabInsets", new Insets(10, 24, 10, 24));
        UIManager.put("TabbedPane.font",      new Font(FONT_FAMILY, Font.BOLD, 14));
        UIManager.put("Button.arc",           16);
        UIManager.put("Component.arc",        14);
        UIManager.put("TextComponent.arc",    14);
        UIManager.put("ComboBox.arc",         14);
        UIManager.put("CheckBox.arc",          8);
        UIManager.put("TextComponent.margin", new Insets(8, 14, 8, 14));
        UIManager.put("ComboBox.padding",     new Insets(6, 12, 6, 12));
        UIManager.put("defaultFont",          new Font(FONT_FAMILY, Font.PLAIN, 14));
    }

    // =========================================================================
    // SECTION 3 - FACTORY KOMPONEN GUI
    // =========================================================================

    /** Membuat JLabel judul/header besar dengan font Bold. */
    public static JLabel createHeaderLabel(String text) {
        final JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 22));
        label.setForeground(COLOR_TEXT_MAIN);
        return label;
    }

    /** Menerapkan gaya label formulir (Bold, warna teks utama) ke JLabel. */
    public static void styleFormLabel(JLabel label) {
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        label.setForeground(COLOR_TEXT_MAIN);
    }

    /** Membuat tombol utama (Primary Button) bergaya rounded rectangle. */
    public static JButton createPrimaryButton(String text) {
        final JButton button = new JButton(text);
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        button.setForeground(COLOR_TEXT_WHITE);
        button.setBackground(COLOR_PRIMARY);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(170, 42));
        button.putClientProperty("JButton.buttonType", "roundRect");
        return button;
    }

    /** Membuat tombol sekunder (Secondary Button) dengan warna latar kustom. */
    public static JButton createSecondaryButton(String text, Color bg) {
        final JButton button = new JButton(text);
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        button.setForeground(COLOR_TEXT_WHITE);
        button.setBackground(bg);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(160, 40));
        button.putClientProperty("JButton.buttonType", "roundRect");
        return button;
    }

    /** Membuat JLabel total tagihan berukuran besar. */
    public static JLabel createTotalLabel() {
        final JLabel label = new JLabel("Rp 0");
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 32));
        label.setForeground(COLOR_PRIMARY_DARK);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    /** Membuat panel kartu (card) dengan background dan border rounded. */
    public static JPanel createCardPanel(Color bgColor) {
        final JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(16, 18, 16, 18)
        ));
        panel.putClientProperty("FlatLaf.style", "arc: 18");
        return panel;
    }

    // =========================================================================
    // SECTION 4 - VALIDASI KOMPONEN (OUTLINE ERROR)
    // =========================================================================

    /** Menampilkan atau menghapus border merah "error" pada komponen input. */
    public static void setInvalidBorder(JComponent comp, boolean isInvalid) {
        comp.putClientProperty("JComponent.outline", isInvalid ? "error" : null);
        comp.repaint();
    }

    // =========================================================================
    // SECTION 5 - IMAGE UTILITIES
    // =========================================================================

    /** Menskala ImageIcon ke dimensi target secara proporsional. */
    public static ImageIcon scaleImage(ImageIcon srcIcon, int targetWidth, int targetHeight) {
        if (srcIcon == null || srcIcon.getImage() == null) return null;
        final Image scaled = srcIcon.getImage()
                .getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /** Memuat gambar dari classpath atau file sistem lalu menskalakannya. */
    public static ImageIcon loadAndScaleImage(String path, int targetWidth, int targetHeight) {
        try {
            final URL resourceUrl = UIHelper.class.getResource("/" + path);
            if (resourceUrl != null) {
                return scaleImage(new ImageIcon(resourceUrl), targetWidth, targetHeight);
            }
            final File file = new File(path);
            if (file.exists()) {
                return scaleImage(new ImageIcon(file.getAbsolutePath()), targetWidth, targetHeight);
            }
        } catch (Exception ex) {
            System.err.println("[UIHelper] Gagal memuat gambar '" + path + "': " + ex.getMessage());
        }
        return null;
    }

    // =========================================================================
    // SECTION 6 - SVG VECTOR UTILITIES (FLATLAF EXTRAS)
    // =========================================================================

    /**
     * Memuat file SVG sebagai FlatSVGIcon dengan ukuran tertentu.
     * Urutan pencarian: classpath, src/main/resources/, direktori kerja root.
     */
    public static FlatSVGIcon loadSVGIcon(String resourceName, int width, int height) {
        try {
            URL res = UIHelper.class.getResource("/" + resourceName);
            if (res == null) {
                res = UIHelper.class.getResource(resourceName);
            }
            if (res != null) {
                return new FlatSVGIcon(res).derive(width, height);
            }
            final File srcFile = new File("src/main/resources/" + resourceName);
            if (srcFile.exists()) {
                return new FlatSVGIcon(srcFile).derive(width, height);
            }
            final File rootFile = new File(resourceName);
            if (rootFile.exists()) {
                return new FlatSVGIcon(rootFile).derive(width, height);
            }
            System.err.println("[UIHelper] File SVG '" + resourceName + "' tidak ditemukan.");
        } catch (Exception ex) {
            System.err.println("[UIHelper] Gagal memuat FlatSVGIcon '" + resourceName + "': " + ex.getMessage());
        }
        return null;
    }

    /**
     * Menghasilkan multi-resolution icon Image dari SVG untuk JFrame/Taskbar.
     */
    public static List<Image> loadWindowIcons(String resourceName) {
        try {
            URL res = UIHelper.class.getResource("/" + resourceName);
            if (res == null) {
                res = UIHelper.class.getResource(resourceName);
            }
            if (res != null) {
                return FlatSVGUtils.createWindowIconImages(res);
            }
            final File srcFile = new File("src/main/resources/" + resourceName);
            if (srcFile.exists()) {
                return FlatSVGUtils.createWindowIconImages(srcFile.toURI().toURL());
            }
            final File rootFile = new File(resourceName);
            if (rootFile.exists()) {
                return FlatSVGUtils.createWindowIconImages(rootFile.toURI().toURL());
            }
            System.err.println("[UIHelper] File SVG '" + resourceName + "' untuk window icon tidak ditemukan.");
        } catch (Exception ex) {
            System.err.println("[UIHelper] Gagal memuat window icons dari '" + resourceName + "': " + ex.getMessage());
        }
        return null;
    }
}