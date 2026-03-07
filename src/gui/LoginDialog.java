package gui;

import bus.TaiKhoanBUS;
import dto.TaiKhoanDTO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.prefs.Preferences; 
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class LoginDialog extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JCheckBox chkRemember;
    private JLabel lblForgotPassword;
    
    // Màu sắc chủ đạo
    private final Color COLOR_SGU_BLUE = new Color(25, 118, 210); 
    private final Color COLOR_BG_RIGHT = Color.WHITE;

    // Khởi tạo đối tượng Preferences để lưu trữ thông tin cục bộ
    private Preferences prefs = Preferences.userNodeForPackage(LoginDialog.class);
    private final String PREF_USER = "username";
    private final String PREF_PASS = "password";
    private final String PREF_REM = "remember";
    
    // Biến chứa ảnh nền cho form đăng nhập
    private Image bgImage;

    public LoginDialog() {
        // Load ảnh nền từ thư mục img
        try {
            bgImage = new ImageIcon(getClass().getResource("/img/bg_thuvien.jpg")).getImage();
        } catch (Exception e) {
            System.out.println("Không tìm thấy ảnh nền bg_thuvien.jpg");
        }
        
        initComponents();
        addEvents();
        loadSavedCredentials(); 
    }

    private void initComponents() {
        setTitle("Đăng nhập hệ thống Thư viện SGU");
        setSize(950, 550);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        // =========================================================================
        // PANEL TRÁI: LOGO & THƯƠNG HIỆU (CHỈ VẼ ẢNH GỐC, BỎ LÀM MỜ)
        // =========================================================================
        JPanel pnlLeft = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (bgImage != null) {
                    // Vẽ tấm ảnh nền rõ nét 100%
                    g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                    
                    // (Đã xóa đoạn code phủ màu xanh ở đây)
                } else {
                    // Nếu lỗi không load được ảnh thì tô màu xanh đặc
                    g2.setColor(COLOR_SGU_BLUE);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        pnlLeft.setLayout(new GridBagLayout()); 

        JPanel pnlBrand = new JPanel();
        pnlBrand.setLayout(new BoxLayout(pnlBrand, BoxLayout.Y_AXIS));
        pnlBrand.setOpaque(false); // Trong suốt để thấy nền phía sau

        // Logo SGU
        JLabel lblLogo = new JLabel();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/img/sgu_logo.png"));
            Image originalImage = originalIcon.getImage();
            int targetSize = 180;
            BufferedImage resizedImg = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(originalImage, 0, 0, targetSize, targetSize, null);
            g2.dispose();
            lblLogo.setIcon(new ImageIcon(resizedImg));
        } catch (Exception e) {
            lblLogo.setText("<html><h1 style='color:white; font-size:40px'>SGU</h1></html>");
        }

        // Chữ Tiêu đề (Có thêm chút bóng đổ nhẹ bằng CSS để dễ đọc nếu ảnh bị sáng)
        JLabel lblTitleSGU = new JLabel("<html><div style='text-align: center; color: white; text-shadow: 2px 2px 4px #000000;'>"
                + "<h2 style='font-family: Segoe UI; font-weight: bold; font-size: 28px; margin-bottom: 10px;'>ĐẠI HỌC SÀI GÒN</h2>"
                + "<p style='font-family: Segoe UI; font-size: 18px; font-weight: 300; letter-spacing: 1px;'>WELCOME TO SGU LIBRARY</p>"
                + "</div></html>");
        lblTitleSGU.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlBrand.add(lblLogo);
        pnlBrand.add(Box.createRigidArea(new Dimension(0, 30))); 
        pnlBrand.add(lblTitleSGU);
        pnlLeft.add(pnlBrand);

        // =========================================================================
        // PANEL PHẢI: FORM ĐĂNG NHẬP
        // =========================================================================
        JPanel pnlRight = new JPanel();
        pnlRight.setBackground(COLOR_BG_RIGHT);
        pnlRight.setLayout(new GridBagLayout()); 

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBackground(COLOR_BG_RIGHT);

        JLabel lblLoginTitle = new JLabel("ĐĂNG NHẬP");
        lblLoginTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblLoginTitle.setForeground(COLOR_SGU_BLUE);
        lblLoginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUser = new JLabel("Tên đăng nhập");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUser.setForeground(COLOR_SGU_BLUE);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT); 

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 2, 0, COLOR_SGU_BLUE),
                new EmptyBorder(5, 5, 5, 5)));
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 

        JPanel pnlUserGroup = new JPanel();
        pnlUserGroup.setLayout(new BoxLayout(pnlUserGroup, BoxLayout.Y_AXIS));
        pnlUserGroup.setBackground(COLOR_BG_RIGHT);
        pnlUserGroup.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); 
        pnlUserGroup.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlUserGroup.add(lblUser);
        pnlUserGroup.add(Box.createRigidArea(new Dimension(0, 8)));
        pnlUserGroup.add(txtUsername);

        JLabel lblPass = new JLabel("Mật khẩu");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPass.setForeground(COLOR_SGU_BLUE);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 2, 0, COLOR_SGU_BLUE),
                new EmptyBorder(5, 5, 5, 5)));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel pnlPassGroup = new JPanel();
        pnlPassGroup.setLayout(new BoxLayout(pnlPassGroup, BoxLayout.Y_AXIS));
        pnlPassGroup.setBackground(COLOR_BG_RIGHT);
        pnlPassGroup.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        pnlPassGroup.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlPassGroup.add(lblPass);
        pnlPassGroup.add(Box.createRigidArea(new Dimension(0, 8)));
        pnlPassGroup.add(txtPassword);

        chkRemember = new JCheckBox("Ghi nhớ đăng nhập");
        chkRemember.setFont(new Font("Segoe UI", Font.BOLD, 13));
        chkRemember.setForeground(Color.GRAY);
        chkRemember.setBackground(COLOR_BG_RIGHT);
        chkRemember.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chkRemember.setFocusPainted(false);

        JPanel pnlRememberWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, -4, 0));
        pnlRememberWrap.setBackground(COLOR_BG_RIGHT);
        pnlRememberWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); 
        pnlRememberWrap.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlRememberWrap.add(chkRemember);

        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(COLOR_SGU_BLUE);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(new EmptyBorder(12, 0, 12, 0));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10)); 
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblForgotPassword = new JLabel("Quên mật khẩu?");
        lblForgotPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblForgotPassword.setForeground(Color.GRAY);
        lblForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblForgotPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblForgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { lblForgotPassword.setForeground(COLOR_SGU_BLUE); }
            @Override
            public void mouseExited(MouseEvent e) { lblForgotPassword.setForeground(Color.GRAY); }
            @Override
            public void mouseClicked(MouseEvent e) { 
                JOptionPane.showMessageDialog(null, "Vui lòng liên hệ IT Admin (SDT: 0901888007) để được cấp lại mật khẩu."); 
            }
        });

        // Add vào form
        pnlForm.add(lblLoginTitle);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 40)));
        pnlForm.add(pnlUserGroup);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlForm.add(pnlPassGroup);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlForm.add(pnlRememberWrap);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 30)));
        pnlForm.add(btnLogin);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlForm.add(lblForgotPassword);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 50, 0, 50); 
        pnlRight.add(pnlForm, gbc);

        add(pnlLeft);
        add(pnlRight);
    }

    private void addEvents() {
        btnLogin.addActionListener(e -> xuLyDangNhap());
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) xuLyDangNhap();
            }
        });
    }

    private void loadSavedCredentials() {
        boolean isRemembered = prefs.getBoolean(PREF_REM, false);
        if (isRemembered) {
            txtUsername.setText(prefs.get(PREF_USER, ""));
            txtPassword.setText(prefs.get(PREF_PASS, ""));
            chkRemember.setSelected(true);
        }
    }

    private void xuLyDangNhap() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TaiKhoanBUS tkBUS = new TaiKhoanBUS();
        TaiKhoanDTO tk = tkBUS.dangNhap(user, pass);

        if (tk != null) {
            if (chkRemember.isSelected()) {
                prefs.put(PREF_USER, user);
                prefs.put(PREF_PASS, pass);
                prefs.putBoolean(PREF_REM, true);
            } else {
                prefs.remove(PREF_USER);
                prefs.remove(PREF_PASS);
                prefs.putBoolean(PREF_REM, false);
            }

            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!\nXin chào: " + tk.getTenDangNhap() + "\nQuyền: " + tk.getQuyenHan(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            
            this.dispose(); 
            MainFrame main = new MainFrame(tk); 
            main.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Sai thông tin đăng nhập, hoặc Tài khoản đã bị khoá!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new LoginDialog().setVisible(true));
    }
}
