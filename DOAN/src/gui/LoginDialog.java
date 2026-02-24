package gui;

import bus.TaiKhoanBUS;
import dto.TaiKhoanDTO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
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

    public LoginDialog() {
        initComponents();
        addEvents();
    }

    private void initComponents() {
        setTitle("Đăng nhập hệ thống Thư viện SGU");
        setSize(950, 550); // Mở rộng chiều ngang xíu cho thoáng
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2)); // Chia đôi 50-50

        // =========================================================================
        // PANEL TRÁI: LOGO & THƯƠNG HIỆU (GIỮ NGUYÊN)
        // =========================================================================
        JPanel pnlLeft = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
        };
        pnlLeft.setBackground(COLOR_SGU_BLUE);
        pnlLeft.setLayout(new GridBagLayout()); 

        JPanel pnlBrand = new JPanel();
        pnlBrand.setLayout(new BoxLayout(pnlBrand, BoxLayout.Y_AXIS));
        pnlBrand.setOpaque(false); 

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

        // Chữ Tiêu đề
        JLabel lblTitleSGU = new JLabel("<html><div style='text-align: center; color: white;'>"
                + "<h2 style='font-family: Segoe UI; font-weight: bold; font-size: 28px; margin-bottom: 10px;'>ĐẠI HỌC SÀI GÒN</h2>"
                + "<p style='font-family: Segoe UI; font-size: 18px; font-weight: 300; letter-spacing: 1px;'>WELCOME TO SGU LIBRARY</p>"
                + "</div></html>");
        lblTitleSGU.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlBrand.add(lblLogo);
        pnlBrand.add(Box.createRigidArea(new Dimension(0, 30))); 
        pnlBrand.add(lblTitleSGU);
        pnlLeft.add(pnlBrand);

        // =========================================================================
        // PANEL PHẢI: FORM ĐĂNG NHẬP (FULL WIDTH)
        // =========================================================================
        JPanel pnlRight = new JPanel();
    pnlRight.setBackground(COLOR_BG_RIGHT);
    pnlRight.setLayout(new GridBagLayout()); 

    JPanel pnlForm = new JPanel();
    pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
    pnlForm.setBackground(COLOR_BG_RIGHT);

    // 1. Tiêu đề Form
    JLabel lblLoginTitle = new JLabel("ĐĂNG NHẬP");
    lblLoginTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
    lblLoginTitle.setForeground(COLOR_SGU_BLUE);
    lblLoginTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa

    // 2. Ô nhập Username
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

    // Panel bao Username 
    JPanel pnlUserGroup = new JPanel();
    pnlUserGroup.setLayout(new BoxLayout(pnlUserGroup, BoxLayout.Y_AXIS));
    pnlUserGroup.setBackground(COLOR_BG_RIGHT);
    pnlUserGroup.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); 
    pnlUserGroup.setAlignmentX(Component.CENTER_ALIGNMENT); // Quan trọng: Căn giữa panel này trong form
    pnlUserGroup.add(lblUser);
    pnlUserGroup.add(Box.createRigidArea(new Dimension(0, 8)));
    pnlUserGroup.add(txtUsername);

    // 3. Ô nhập Password
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

    // Panel bao Password
    JPanel pnlPassGroup = new JPanel();
    pnlPassGroup.setLayout(new BoxLayout(pnlPassGroup, BoxLayout.Y_AXIS));
    pnlPassGroup.setBackground(COLOR_BG_RIGHT);
    pnlPassGroup.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
    pnlPassGroup.setAlignmentX(Component.CENTER_ALIGNMENT); // Quan trọng: Căn giữa
    pnlPassGroup.add(lblPass);
    pnlPassGroup.add(Box.createRigidArea(new Dimension(0, 8)));
    pnlPassGroup.add(txtPassword);

    // 4. Checkbox Ghi nhớ
    chkRemember = new JCheckBox("Ghi nhớ đăng nhập");
    chkRemember.setFont(new Font("Segoe UI", Font.BOLD, 13));
    chkRemember.setForeground(Color.GRAY);
    chkRemember.setBackground(COLOR_BG_RIGHT);
    chkRemember.setCursor(new Cursor(Cursor.HAND_CURSOR));
    chkRemember.setFocusPainted(false);

    // Panel bao Checkbox
    JPanel pnlRememberWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, -4, 0));
    pnlRememberWrap.setBackground(COLOR_BG_RIGHT);
    pnlRememberWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); 
    pnlRememberWrap.setAlignmentX(Component.CENTER_ALIGNMENT); // Quan trọng: Căn giữa
    pnlRememberWrap.add(chkRemember);

    // 5. Nút Đăng nhập
    btnLogin = new JButton("ĐĂNG NHẬP");
    btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 18));
    btnLogin.setForeground(Color.WHITE);
    btnLogin.setBackground(COLOR_SGU_BLUE);
    btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnLogin.setFocusPainted(false);
    btnLogin.setBorder(new EmptyBorder(12, 0, 12, 0));
    // Dãn rộng tối đa để 2 cạnh bên thẳng hàng với ô nhập liệu
    btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10)); 
    btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT); // Quan trọng: Căn giữa nút

    // 6. Chữ Quên mật khẩu
    lblForgotPassword = new JLabel("Quên mật khẩu?");
    lblForgotPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
    lblForgotPassword.setForeground(Color.GRAY);
    lblForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
    lblForgotPassword.setAlignmentX(Component.CENTER_ALIGNMENT); // Quan trọng: Căn giữa chữ
    lblForgotPassword.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) { lblForgotPassword.setForeground(COLOR_SGU_BLUE); }
        @Override
        public void mouseExited(MouseEvent e) { lblForgotPassword.setForeground(Color.GRAY); }
        @Override
        public void mouseClicked(MouseEvent e) { 
            JOptionPane.showMessageDialog(null, "Liên hệ Admin: 0901888xxx"); 
        }
    });

    // --- ADD VÀO FORM ---
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

    // --- ADD FORM VÀO PANEL PHẢI (CÓ PADDING 2 BÊN) ---
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 50, 0, 50); // Padding 2 bên 50px
    pnlRight.add(pnlForm, gbc);

        // Thêm 2 panel chính
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

    private void xuLyDangNhap() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TaiKhoanBUS tkBUS = new TaiKhoanBUS();
        TaiKhoanDTO tk = tkBUS.dangNhap(user, pass);

        if (tk != null) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!\nXin chào: " + tk.getTenDangNhap());
            this.dispose(); 
            MainFrame main = new MainFrame(tk); // Truyền tài khoản vào để phân quyền
            main.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new LoginDialog().setVisible(true));
    }
}