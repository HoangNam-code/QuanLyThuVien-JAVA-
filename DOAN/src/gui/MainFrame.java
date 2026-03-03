package gui;

import dto.TaiKhoanDTO;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class MainFrame extends JFrame {

    private TaiKhoanDTO taiKhoan;
    private JPanel pnlMenu;
    private JPanel pnlContent;
    private CardLayout cardLayout;

    // Màu chủ đạo SGU (Xanh đậm)
    private final Color COLOR_MAIN = new Color(25, 118, 210); 
    private final Color COLOR_HOVER = new Color(0, 90, 180); 
    private final Color COLOR_TEXT = Color.WHITE;

    private ArrayList<JButton> listButtons = new ArrayList<>();

    public MainFrame(TaiKhoanDTO tk) {
        this.taiKhoan = tk;
        initComponents();
        phanQuyen(); 
    }

    private void initComponents() {
        setTitle("HỆ THỐNG QUẢN LÝ THƯ VIỆN ĐẠI HỌC SÀI GÒN");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // =====================================================================
        // 1. SIDEBAR MENU (BÊN TRÁI - ĐÃ CHỈNH SỬA)
        // =====================================================================
        pnlMenu = new JPanel();
        pnlMenu.setBackground(COLOR_MAIN);
        // Quan trọng: Set kích thước cố định cho Sidebar là 250px
        pnlMenu.setPreferredSize(new Dimension(250, 0)); 
        pnlMenu.setLayout(new BorderLayout()); // Dùng BorderLayout để dễ quản lý Trên - Giữa - Dưới

        // --- PHẦN TRÊN: LOGO & INFO USER (GÓC TRÊN TRÁI) ---
        JPanel pnlTop = new JPanel();
        pnlTop.setBackground(COLOR_MAIN);
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setBorder(new EmptyBorder(20, 10, 20, 10)); // Padding xung quanh

        // Logo SGU nhỏ & Tên trường
        JPanel pnlLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlLogo.setBackground(COLOR_MAIN);
        pnlLogo.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn trái toàn bộ

        JLabel lblLogoSmall = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/sgu_logo.png")); // File logo của bạn
            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            lblLogoSmall.setIcon(new ImageIcon(img));
        } catch (Exception e) {}

        JLabel lblSGU = new JLabel("<html><b style='font-size:16px; color:white;'>THƯ VIỆN</b><br><span style='font-size:12px; color:white;'>Đại học Sài Gòn</span></html>");
        
        pnlLogo.add(lblLogoSmall);
        pnlLogo.add(lblSGU);

        // Info User (Xin chào...)
        JPanel pnlUser = new JPanel();
        pnlUser.setLayout(new BoxLayout(pnlUser, BoxLayout.Y_AXIS));
        pnlUser.setBackground(COLOR_MAIN);
        pnlUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlUser.setBorder(new EmptyBorder(15, 5, 0, 0)); // Cách logo một chút

        JLabel lblHello = new JLabel("Xin chào, " + taiKhoan.getTenDangNhap());
        lblHello.setForeground(Color.WHITE);
        lblHello.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel lblRole = new JLabel(taiKhoan.getQuyenHan()); 
        lblRole.setForeground(new Color(220, 220, 220)); // Màu xám nhạt
        lblRole.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        pnlUser.add(lblHello);
        pnlUser.add(Box.createRigidArea(new Dimension(0, 3)));
        pnlUser.add(lblRole);

        // Add Logo và User vào Top Panel
        pnlTop.add(pnlLogo);
        pnlTop.add(pnlUser);
        pnlTop.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Đường kẻ phân cách
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 80));
        pnlTop.add(sep);

        // --- PHẦN GIỮA: DANH SÁCH MENU ---
        JPanel pnlCenter = new JPanel();
        pnlCenter.setBackground(COLOR_MAIN);
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.setBorder(new EmptyBorder(10, 0, 0, 0)); // Padding đỉnh

        taoNutMenu(pnlCenter, "Trang chủ", "HOME", "home.png");
        taoNutMenu(pnlCenter, "Quản lý Sách", "SACH", "book.png");
        taoNutMenu(pnlCenter, "Độc giả", "DOCGIA", "reader.png");
        taoNutMenu(pnlCenter, "Mượn - Trả", "MUONTRA", "borrow.png");
        taoNutMenu(pnlCenter, "Nhập hàng", "NHAPHANG", "import.png");
        taoNutMenu(pnlCenter, "Thống kê", "THONGKE", "stat.png");
        taoNutMenu(pnlCenter, "Nhân viên", "NHANVIEN", "staff.png"); 

        // --- PHẦN DƯỚI: NÚT ĐĂNG XUẤT ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBottom.setBackground(COLOR_MAIN);
        pnlBottom.setBorder(new EmptyBorder(0, 0, 20, 0));

        JButton btnLogout = createMenuButton("Đăng xuất", "logout.png");
        btnLogout.setBackground(new Color(211, 47, 47)); // Đỏ
        btnLogout.setPreferredSize(new Dimension(200, 40)); // Nút nhỏ gọn hơn
        btnLogout.setMaximumSize(new Dimension(200, 40));
        btnLogout.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa chữ
        btnLogout.addActionListener(e -> dangXuat());
        
        pnlBottom.add(btnLogout);

        // Lắp ráp Sidebar
        pnlMenu.add(pnlTop, BorderLayout.NORTH);
        pnlMenu.add(pnlCenter, BorderLayout.CENTER);
        pnlMenu.add(pnlBottom, BorderLayout.SOUTH);

        // =====================================================================
        // 2. CONTENT PANEL (BÊN PHẢI)
        // =====================================================================
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);
        pnlContent.setBackground(Color.WHITE);

        // Add các Panel giả lập
        pnlContent.add(createDummyPanel("TRANG CHỦ", Color.WHITE), "HOME");
        pnlContent.add(createDummyPanel("QUẢN LÝ SÁCH", new Color(240, 248, 255)), "SACH");
        pnlContent.add(createDummyPanel("QUẢN LÝ ĐỘC GIẢ", new Color(255, 245, 238)), "DOCGIA");
        pnlContent.add(createDummyPanel("MƯỢN - TRẢ SÁCH", new Color(240, 255, 240)), "MUONTRA");
        pnlContent.add(createDummyPanel("NHẬP HÀNG", Color.LIGHT_GRAY), "NHAPHANG");
        pnlContent.add(createDummyPanel("THỐNG KÊ", Color.ORANGE), "THONGKE");
        pnlContent.add(new QuanLyNhanVienPanel(), "NHANVIEN");

        add(pnlMenu, BorderLayout.WEST);
        add(pnlContent, BorderLayout.CENTER);
    }

    // --- HÀM TẠO NÚT MENU ---
    private void taoNutMenu(JPanel panel, String title, String cardName, String iconName) {
        JButton btn = createMenuButton(title, iconName);
        // Hack: Ép nút dãn hết chiều ngang của panel cha (250px)
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); 
        
        btn.addActionListener(e -> {
            resetButtonColor();
            btn.setBackground(COLOR_HOVER); 
            // Hiệu ứng viền trái
            btn.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 5, 0, 0, Color.WHITE), 
                new EmptyBorder(10, 15, 10, 10) 
            ));
            cardLayout.show(pnlContent, cardName);
        });
        listButtons.add(btn);
        panel.add(btn);
    }

    private JButton createMenuButton(String text, String iconName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(COLOR_TEXT);
        btn.setBackground(COLOR_MAIN);
        btn.setBorder(new EmptyBorder(10, 20, 10, 10)); // Padding mặc định
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT); // Căn trái nội dung
        
        if (iconName != null) {
            try {
                URL resource = getClass().getResource("/img/" + iconName);
                if (resource != null) {
                    ImageIcon icon = new ImageIcon(resource);
                    Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    btn.setIcon(new ImageIcon(img));
                    btn.setIconTextGap(15); 
                }
            } catch (Exception e) {}
        }
        return btn;
    }

    private void resetButtonColor() {
        for (JButton btn : listButtons) {
            btn.setBackground(COLOR_MAIN);
            btn.setBorder(new EmptyBorder(10, 20, 10, 10)); // Trả về không viền
        }
    }

    private void phanQuyen() {
        String role = taiKhoan.getQuyenHan();
        if (role != null && role.equalsIgnoreCase("Thu Thu")) {
            for (JButton btn : listButtons) {
                if (btn.getText().equals("Nhân viên") || btn.getText().equals("Thống kê")) {
                    btn.setVisible(false);
                }
            }
        }
    }
    
    private void dangXuat() {
        int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Đăng xuất", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            this.dispose(); 
            new LoginDialog().setVisible(true); 
        }
    }

    private JPanel createDummyPanel(String text, Color color) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(color);
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 30));
        p.add(lbl);
        return p;
    }
}
