package gui;

import dto.TaiKhoanDTO;
import java.awt.*;
import java.awt.event.*;
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
    private JLabel lblHamburger;

    // Màu chủ đạo SGU (Xanh đậm)
    private final Color COLOR_MAIN = new Color(25, 118, 210);
    private final Color COLOR_HOVER = new Color(0, 90, 180);
    private final Color COLOR_TEXT = Color.WHITE;

    private ArrayList<JButton> listButtons = new ArrayList<>();

    // --- BIẾN ĐIỀU KHIỂN ANIMATION MENU ---
    private int currentMenuWidth = 0; // Bắt đầu ở trạng thái tàng hình (0px)
    private int targetMenuWidth = 0;
    private Timer menuTimer;

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
        // 1. TOP HEADER (CHỨA NÚT 3 GẠCH & TIÊU ĐỀ)
        // =====================================================================
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(20, 100, 190)); // Xanh đậm hơn chút để nổi bật
        pnlHeader.setPreferredSize(new Dimension(0, 55)); // Chiều cao thanh Top bar

        // Nút 3 gạch ngang (Hamburger)
        lblHamburger = new JLabel(new HamburgerIcon());
        lblHamburger.setBorder(new EmptyBorder(0, 20, 0, 20));
        lblHamburger.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblHamburger.setToolTipText("Mở Menu");

        // Tiêu đề phần mềm trên Header
        JLabel lblAppTitle = new JLabel("HỆ THỐNG QUẢN LÝ THƯ VIỆN ĐẠI HỌC SÀI GÒN");
        lblAppTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblAppTitle.setForeground(Color.WHITE);

        JPanel pnlHeaderLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlHeaderLeft.setOpaque(false);
        lblHamburger.setPreferredSize(new Dimension(65, 55));
        lblHamburger.setHorizontalAlignment(SwingConstants.CENTER);
        lblAppTitle.setPreferredSize(new Dimension(500, 55));
        lblAppTitle.setVerticalAlignment(SwingConstants.CENTER);

        pnlHeaderLeft.add(lblHamburger);
        pnlHeaderLeft.add(lblAppTitle);
        pnlHeader.add(pnlHeaderLeft, BorderLayout.WEST);

        add(pnlHeader, BorderLayout.NORTH);

        // =====================================================================
        // 2. SIDEBAR MENU (BÊN TRÁI - ẨN/HIỆN MƯỢT MÀ)
        // =====================================================================
        // Container chính thay đổi kích thước (0 -> 250px)
        pnlMenu = new JPanel(new BorderLayout());
        pnlMenu.setBackground(COLOR_MAIN);
        pnlMenu.setPreferredSize(new Dimension(currentMenuWidth, 0));

        // Khối chứa Nội dung Menu (Cố định 250px để không bị bóp méo chữ khi Container
        // thu nhỏ)
        JPanel pnlMenuContent = new JPanel(new BorderLayout());
        pnlMenuContent.setBackground(COLOR_MAIN);
        pnlMenuContent.setPreferredSize(new Dimension(250, 0));

        // --- PHẦN TRÊN: LOGO & INFO USER ---
        JPanel pnlTopMenu = new JPanel();
        pnlTopMenu.setBackground(COLOR_MAIN);
        pnlTopMenu.setLayout(new BoxLayout(pnlTopMenu, BoxLayout.Y_AXIS));
        pnlTopMenu.setBorder(new EmptyBorder(20, 10, 20, 10));

        JPanel pnlLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlLogo.setBackground(COLOR_MAIN);
        pnlLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblLogoSmall = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/sgu_logo.png"));
            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            lblLogoSmall.setIcon(new ImageIcon(img));
        } catch (Exception e) {
        }

        JLabel lblSGU = new JLabel(
                "<html><b style='font-size:16px; color:white;'>THƯ VIỆN</b><br><span style='font-size:12px; color:white;'>Đại học Sài Gòn</span></html>");
        pnlLogo.add(lblLogoSmall);
        pnlLogo.add(lblSGU);

        JPanel pnlUser = new JPanel();
        pnlUser.setLayout(new BoxLayout(pnlUser, BoxLayout.Y_AXIS));
        pnlUser.setBackground(COLOR_MAIN);
        pnlUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlUser.setBorder(new EmptyBorder(15, 5, 0, 0));

        JLabel lblHello = new JLabel("Xin chào, " + taiKhoan.getTenDangNhap());
        lblHello.setForeground(Color.WHITE);
        lblHello.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lblRole = new JLabel(taiKhoan.getQuyenHan());
        lblRole.setForeground(new Color(220, 220, 220));
        lblRole.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        pnlUser.add(lblHello);
        pnlUser.add(Box.createRigidArea(new Dimension(0, 3)));
        pnlUser.add(lblRole);

        pnlTopMenu.add(pnlLogo);
        pnlTopMenu.add(pnlUser);
        pnlTopMenu.add(Box.createRigidArea(new Dimension(0, 10)));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 100));
        pnlTopMenu.add(sep);

        // --- PHẦN GIỮA: DANH SÁCH MENU ---
        JPanel pnlCenterMenu = new JPanel();
        pnlCenterMenu.setBackground(COLOR_MAIN);
        pnlCenterMenu.setLayout(new BoxLayout(pnlCenterMenu, BoxLayout.Y_AXIS));
        pnlCenterMenu.setBorder(new EmptyBorder(10, 0, 0, 0));

        taoNutMenu(pnlCenterMenu, "Trang chủ", "HOME", "home.png");
        taoNutMenu(pnlCenterMenu, "Quản lý Sách", "SACH", "book.png");
        taoNutMenu(pnlCenterMenu, "Độc giả", "DOCGIA", "reader.png");
        taoNutMenu(pnlCenterMenu, "Nhân viên", "NHANVIEN", "staff.png");
        taoNutMenu(pnlCenterMenu, "Mượn - Trả", "MUONTRA", "borrow.png");
        taoNutMenu(pnlCenterMenu, "Nhập hàng", "NHAPHANG", "import.png");
        taoNutMenu(pnlCenterMenu, "Thống kê", "THONGKE", "stat.png");

        // --- PHẦN DƯỚI: NÚT ĐĂNG XUẤT ---
        JPanel pnlBottomMenu = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBottomMenu.setBackground(COLOR_MAIN);
        pnlBottomMenu.setBorder(new EmptyBorder(0, 0, 20, 0));

        JButton btnLogout = createMenuButton("Đăng xuất", "logout.png");
        btnLogout.setBackground(new Color(211, 47, 47));
        btnLogout.setPreferredSize(new Dimension(220, 45));
        btnLogout.setMaximumSize(new Dimension(220, 45));
        btnLogout.setHorizontalAlignment(SwingConstants.CENTER);
        btnLogout.addActionListener(e -> dangXuat());

        pnlBottomMenu.add(btnLogout);

        // Lắp ráp Content vào Menu Container (Cắt xén thông minh bằng
        // BorderLayout.WEST)
        pnlMenuContent.add(pnlTopMenu, BorderLayout.NORTH);
        pnlMenuContent.add(pnlCenterMenu, BorderLayout.CENTER);
        pnlMenuContent.add(pnlBottomMenu, BorderLayout.SOUTH);
        pnlMenu.add(pnlMenuContent, BorderLayout.WEST);

        // =====================================================================
        // 3. CONTENT PANEL (BÊN PHẢI)
        // =====================================================================
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);
        pnlContent.setBackground(Color.WHITE);

        pnlContent.add(new TrangChuPanel(taiKhoan), "HOME");
        pnlContent.add(new QuanLySachPanel(), "SACH");
        pnlContent.add(new QuanLyDocGiaPanel(), "DOCGIA");
        pnlContent.add(new QuanLyNhanVienPanel(), "NHANVIEN");
        pnlContent.add(new MuonTraSachPanel(), "MUONTRA");
        pnlContent.add(new QuanLyNhapHangPanel(), "NHAPHANG");
        pnlContent.add(new ThongKePanel(),"THONGKE");

        add(pnlMenu, BorderLayout.WEST);
        add(pnlContent, BorderLayout.CENTER);

        // Khởi động Animation
        setupMenuAnimation();
    }

    // --- HÀM ANIMATION XỬ LÝ TRƯỢT MENU (LẮNG NGHE CHUỘT) ---
    private void setupMenuAnimation() {
        menuTimer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Point mousePos = MouseInfo.getPointerInfo().getLocation();

                    // 1. Kiểm tra chuột có đang ở trên nút 3 Gạch không
                    boolean isHoveringHamburger = false;
                    if (lblHamburger.isShowing()) {
                        Point loc = lblHamburger.getLocationOnScreen();
                        Rectangle bounds = new Rectangle(loc.x, loc.y, lblHamburger.getWidth(),
                                lblHamburger.getHeight() + 20); // Dư ra xíu để không bị ngắt quãng
                        isHoveringHamburger = bounds.contains(mousePos);
                    }

                    // 2. Kiểm tra chuột có đang ở trong khu vực Panel Menu đang trượt không
                    boolean isHoveringMenu = false;
                    if (pnlMenu.isShowing() && pnlMenu.getWidth() > 0) {
                        Point loc = pnlMenu.getLocationOnScreen();
                        Rectangle bounds = new Rectangle(loc.x, loc.y, pnlMenu.getWidth() + 10, pnlMenu.getHeight());
                        isHoveringMenu = bounds.contains(mousePos);
                    }

                    // Chốt mục tiêu: Nếu đang lia chuột -> Bật ra 250px | Nếu đưa chuột đi chỗ khác
                    // -> Co lại 0px
                    if (isHoveringHamburger || isHoveringMenu) {
                        targetMenuWidth = 250;
                    } else {
                        targetMenuWidth = 0;
                    }

                    // Thực thi trượt mượt mà
                    if (currentMenuWidth != targetMenuWidth) {
                        int speed = 25; // Tốc độ trượt (Nhanh và mượt)
                        if (currentMenuWidth < targetMenuWidth) {
                            currentMenuWidth += speed;
                            if (currentMenuWidth > targetMenuWidth)
                                currentMenuWidth = targetMenuWidth;
                        } else {
                            currentMenuWidth -= speed;
                            if (currentMenuWidth < targetMenuWidth)
                                currentMenuWidth = targetMenuWidth;
                        }

                        pnlMenu.setPreferredSize(new Dimension(currentMenuWidth, 0));
                        pnlMenu.revalidate(); // Cập nhật khung hình ngay lập tức
                    }
                } catch (Exception ex) {
                }
            }
        });
        menuTimer.start();
    }

    // --- HÀM TẠO NÚT MENU ---
    private void taoNutMenu(JPanel panel, String title, String cardName, String iconName) {
        JButton btn = createMenuButton(title, iconName);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        btn.addActionListener(e -> {
            resetButtonColor();
            btn.setBackground(COLOR_HOVER);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 5, 0, 0, Color.WHITE),
                    new EmptyBorder(10, 15, 10, 10)));
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

        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 10));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!(btn.getBorder() instanceof MatteBorder)) {
                    btn.setBackground(new Color(40, 130, 220));
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!(btn.getBorder() instanceof MatteBorder)) {
                    btn.setBackground(COLOR_MAIN);
                }
            }
        });

        if (iconName != null) {
            try {
                URL resource = getClass().getResource("/img/" + iconName);
                if (resource != null) {
                    ImageIcon icon = new ImageIcon(resource);
                    Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    btn.setIcon(new ImageIcon(img));
                    btn.setIconTextGap(15);
                }
            } catch (Exception e) {
            }
        }
        return btn;
    }

    private void resetButtonColor() {
        for (JButton btn : listButtons) {
            btn.setBackground(COLOR_MAIN);
            btn.setBorder(new EmptyBorder(10, 20, 10, 10));
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
        int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Đăng xuất",
                JOptionPane.YES_NO_OPTION);
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

    // =========================================================================
    // LỚP VẼ ICON 3 GẠCH NGANG (HAMBURGER) SẮC NÉT BẰNG VECTOR
    // =========================================================================
    class HamburgerIcon implements Icon {
        private int width = 26;
        private int height = 20;

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            // Bật bộ lọc khử răng cưa để nét vẽ cực kỳ mượt mà
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int spacing = 8;
            int startY = y + 2;
            g2.drawLine(x, startY, x + width, startY); // Dòng 1
            g2.drawLine(x, startY + spacing, x + width, startY + spacing); // Dòng 2
            g2.drawLine(x, startY + spacing * 2, x + width, startY + spacing * 2); // Dòng 3
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }
    }
}