package gui;

import bus.LoaiThanhVienBUS;
import bus.LyDoPhatBUS;
import dto.LoaiThanhVienDTO;
import dto.LyDoPhatDTO;
import dto.TaiKhoanDTO;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import util.Formatter;

public class TrangChuPanel extends JPanel {

    private final Color COLOR_PRIMARY = new Color(25, 118, 210);
    private final Font FONT_TEXT = new Font("Segoe UI", Font.PLAIN, 15);
    
    private TaiKhoanDTO taiKhoan;
    private JTextArea txtPhi, txtPhat;
    private Image bgImage;

    public TrangChuPanel(TaiKhoanDTO tk) {
        this.taiKhoan = tk;
        
        // Load ảnh nền
        try {
            bgImage = new ImageIcon(getClass().getResource("/img/bg_thuvien.jpg")).getImage();
        } catch (Exception e) {
            System.out.println("Không tìm thấy ảnh nền bg_thuvien.jpg");
        }

        initComponents();
        loadDuLieuDong(); 
    }

    // =========================================================
    // HÀM VẼ ẢNH NỀN VÀ LÀM MỜ (OVERLAY)
    // =========================================================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            // 1. Vẽ ảnh nền gốc
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            
            // 2. Vẽ một lớp sương mù mờ lên toàn bộ màn hình để "dìm" ảnh xuống
            // Màu đen trong suốt (Alpha = 120), giúp ảnh chìm xuống, làm nổi bật các ô màu trắng lên
            g.setColor(new Color(0, 0, 0, 120)); 
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20)); 
        setOpaque(false); // Xóa nền gốc để thấy ảnh
        setBorder(new EmptyBorder(25, 30, 30, 30)); 

        // ================= PHẦN 1: TIÊU ĐỀ & GIỚI THIỆU (NỀN TRẮNG BÓC) =================
        JPanel pnlIntro = new JPanel(new BorderLayout(0, 10));
        pnlIntro.setBackground(Color.WHITE); // Trắng bóc 100%
        pnlIntro.setOpaque(true);
        // Thêm viền lùi vào trong cho khung Intro
        pnlIntro.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ THƯ VIỆN ĐẠI HỌC SÀI GÒN (SGU)", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_PRIMARY);
        pnlIntro.add(lblTitle, BorderLayout.NORTH);

        JTextArea txtIntro = new JTextArea(
            "Thư viện Đại học Sài Gòn là trung tâm thông tin tư liệu phục vụ đắc lực cho công tác giảng dạy, học tập và nghiên cứu khoa học.\n" +
            "Phần mềm được thiết kế nhằm tối ưu hóa quy trình quản lý, giúp cán bộ thư viện theo dõi thông tin chính xác, nhanh chóng và chuyên nghiệp nhất."
        );
        txtIntro.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        txtIntro.setForeground(Color.BLACK); // Chữ đen rõ nét
        txtIntro.setBackground(Color.WHITE); // Nền trắng bóc
        txtIntro.setEditable(false);
        txtIntro.setBorder(new EmptyBorder(5, 50, 5, 50)); 
        pnlIntro.add(txtIntro, BorderLayout.CENTER);

        // Nút làm mới dữ liệu
        JPanel pnlUpdateBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlUpdateBtn.setOpaque(false); 
        
        JButton btnLamMoi = new JButton("Tải lại quy định mới nhất");
        btnLamMoi.setBackground(new Color(40, 167, 69));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLamMoi.addActionListener(e -> loadDuLieuDong());
        pnlUpdateBtn.add(btnLamMoi);
        
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false); // Panel bọc ngoài phải trong suốt
        pnlTop.add(pnlIntro, BorderLayout.CENTER);
        pnlTop.add(pnlUpdateBtn, BorderLayout.SOUTH);
        add(pnlTop, BorderLayout.NORTH);

        // ================= PHẦN 2: QUY ĐỊNH THƯ VIỆN (NỀN TRẮNG BÓC) =================
        // Layout chứa 2 khung, khe hở 30px (khe hở này sẽ thấy được ảnh nền ở dưới)
        JPanel pnlRules = new JPanel(new GridLayout(1, 2, 30, 0)); 
        pnlRules.setOpaque(false); 

        // 1. Khung Phí Thành Viên
        txtPhi = new JTextArea();
        setupTextArea(txtPhi);
        JScrollPane scrollPhi = new JScrollPane(txtPhi);
        scrollPhi.setBorder(createCustomBorder(" Quy định Phí Thành Viên "));
        scrollPhi.setBackground(Color.WHITE); // Trắng bóc
        scrollPhi.setOpaque(true);
        scrollPhi.getViewport().setBackground(Color.WHITE);
        pnlRules.add(scrollPhi);

        // 2. Khung Quy Định Phạt
        txtPhat = new JTextArea();
        setupTextArea(txtPhat);
        JScrollPane scrollPhat = new JScrollPane(txtPhat);
        scrollPhat.setBorder(createCustomBorder(" Quy định Xử Phạt Vi Phạm "));
        scrollPhat.setBackground(Color.WHITE); // Trắng bóc
        scrollPhat.setOpaque(true);
        scrollPhat.getViewport().setBackground(Color.WHITE);
        pnlRules.add(scrollPhat);

        add(pnlRules, BorderLayout.CENTER);
    }

    // --- HÀM LOAD DỮ LIỆU ĐỘNG ---
    private void loadDuLieuDong() {
        LoaiThanhVienBUS ltvBUS = new LoaiThanhVienBUS();
        ArrayList<LoaiThanhVienDTO> listLTV = ltvBUS.getList();
        StringBuilder sbPhi = new StringBuilder();
        if (listLTV != null && !listLTV.isEmpty()) {
            for (LoaiThanhVienDTO ltv : listLTV) {
                String mucPhi = (ltv.getPhi() == 0) ? "Miễn phí" : Formatter.FormatVND(ltv.getPhi());
                sbPhi.append("• ").append(ltv.getTenLoaiTV().toUpperCase()).append(":\n");
                sbPhi.append("   - Mức phí: ").append(mucPhi).append("\n\n");
            }
        } else {
            sbPhi.append("Chưa có dữ liệu Loại thành viên từ Hệ thống.");
        }
        txtPhi.setText(sbPhi.toString());
        txtPhi.setCaretPosition(0); 

        LyDoPhatBUS ldpBUS = new LyDoPhatBUS();
        ArrayList<LyDoPhatDTO> listLDP = ldpBUS.getList();
        StringBuilder sbPhat = new StringBuilder();
        if (listLDP != null && !listLDP.isEmpty()) {
            for (LyDoPhatDTO ldp : listLDP) {
                sbPhat.append("• ").append(ldp.getTenLyDoPhat().toUpperCase()).append(":\n");
                sbPhat.append("   - Phạt: ").append(ldp.getCachTinhPhat()).append("\n");
                if (ldp.getGhiChu() != null && !ldp.getGhiChu().isEmpty()) {
                    sbPhat.append("   - Ghi chú: ").append(ldp.getGhiChu()).append("\n");
                }
                sbPhat.append("\n");
            }
        } else {
            sbPhat.append("Chưa có dữ liệu Lý do phạt từ Hệ thống.");
        }
        txtPhat.setText(sbPhat.toString());
        txtPhat.setCaretPosition(0); 
    }

    private void setupTextArea(JTextArea txt) {
        txt.setFont(FONT_TEXT);
        txt.setForeground(Color.BLACK); // Chữ đen tuyền sắc nét
        txt.setBackground(Color.WHITE); // Nền trắng bóc 100%
        txt.setEditable(false);
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setOpaque(true);
        txt.setBorder(new EmptyBorder(15, 20, 15, 20)); 
    }

    private TitledBorder createCustomBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_PRIMARY, 2, true),
            title, TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 16), COLOR_PRIMARY
        );
    }
}