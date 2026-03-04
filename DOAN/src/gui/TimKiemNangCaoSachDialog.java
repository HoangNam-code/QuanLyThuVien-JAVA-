package gui;

import bus.SachBUS;
import dao.NhaXuatBanDAO;
import dao.TacGiaDAO;
import dao.TheLoaiDAO;
import dto.NhaXuatBanDTO;
import dto.SachDTO;
import dto.TacGiaDTO;
import dto.TheLoaiDTO;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class TimKiemNangCaoSachDialog extends JDialog {

    private QuanLySachPanel parentPanel;
    private SachBUS sachBUS;

    private JTextField txtNamXB, txtGiaTu, txtGiaDen;
    private JComboBox<String> cboTacGia, cboTheLoai, cboNXB;

    private final Color COLOR_PRIMARY = new Color(25, 118, 210);
    private final Color COLOR_WARNING = new Color(255, 153, 0);

    public TimKiemNangCaoSachDialog(QuanLySachPanel parentPanel, SachBUS sachBUS) {
        this.parentPanel = parentPanel;
        this.sachBUS = sachBUS;

        setTitle("Tìm kiếm sách nâng cao");
        setSize(450, 450); // Tăng chiều cao lên một chút để đủ chỗ
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ================= NHÓM 1: THÔNG TIN SÁCH =================
        JPanel pnlGroup1 = createGroupPanel("Thông tin cơ bản");
        pnlGroup1.setLayout(new GridLayout(4, 2, 10, 10)); // Sửa thành 4 dòng

        pnlGroup1.add(new JLabel("Thể loại:"));
        cboTheLoai = new JComboBox<>();
        cboTheLoai.addItem("Tất cả");
        for (TheLoaiDTO tl : new TheLoaiDAO().selectAll()) {
            cboTheLoai.addItem(tl.getMaTL() + " - " + tl.getTenTL());
        }
        cboTheLoai.setBackground(Color.WHITE);
        pnlGroup1.add(cboTheLoai);

        pnlGroup1.add(new JLabel("Tác giả:"));
        cboTacGia = new JComboBox<>();
        cboTacGia.addItem("Tất cả");
        for (TacGiaDTO tg : new TacGiaDAO().selectAll()) {
            cboTacGia.addItem(tg.getMaTG() + " - " + tg.getHoTen());
        }
        cboTacGia.setBackground(Color.WHITE);
        pnlGroup1.add(cboTacGia);

        // THÊM MỚI: Nhà xuất bản
        pnlGroup1.add(new JLabel("Nhà xuất bản:"));
        cboNXB = new JComboBox<>();
        cboNXB.addItem("Tất cả");
        for (NhaXuatBanDTO nxb : new NhaXuatBanDAO().selectAll()) {
            cboNXB.addItem(nxb.getMaNXB() + " - " + nxb.getTenNXB());
        }
        cboNXB.setBackground(Color.WHITE);
        pnlGroup1.add(cboNXB);

        // THÊM MỚI: Năm xuất bản
        pnlGroup1.add(new JLabel("Năm xuất bản:"));
        txtNamXB = new JTextField();
        pnlGroup1.add(txtNamXB);

        // ================= NHÓM 2: MỨC GIÁ =================
        JPanel pnlGroup2 = createGroupPanel("Khoảng giá (VNĐ)");
        pnlGroup2.setLayout(new GridLayout(2, 2, 10, 10));

        pnlGroup2.add(new JLabel("Từ giá:"));
        txtGiaTu = new JTextField();
        pnlGroup2.add(txtGiaTu);

        pnlGroup2.add(new JLabel("Đến giá:"));
        txtGiaDen = new JTextField();
        pnlGroup2.add(txtGiaDen);

        pnlMain.add(pnlGroup1);
        pnlMain.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlMain.add(pnlGroup2);

        add(pnlMain, BorderLayout.CENTER);

        // ================= PANEL NÚT BẤM =================
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        pnlButtons.setBackground(Color.WHITE);

        JButton btnTimKiem = new JButton("Tìm kiếm ngay");
        btnTimKiem.setBackground(COLOR_WARNING);
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTimKiem.setFocusPainted(false);

        JButton btnLamMoi = new JButton("Làm mới bộ lọc");
        btnLamMoi.setBackground(Color.LIGHT_GRAY);
        btnLamMoi.setFocusPainted(false);

        pnlButtons.add(btnTimKiem);
        pnlButtons.add(btnLamMoi);
        add(pnlButtons, BorderLayout.SOUTH);

        // --- XỬ LÝ SỰ KIỆN NÚT BẤM ---
        btnLamMoi.addActionListener(e -> {
            txtNamXB.setText("");
            txtGiaTu.setText("");
            txtGiaDen.setText("");
            cboTheLoai.setSelectedIndex(0);
            cboTacGia.setSelectedIndex(0);
            cboNXB.setSelectedIndex(0);
        });

        btnTimKiem.addActionListener(e -> thucHienTimKiem());
    }

    private JPanel createGroupPanel(String title) {
        JPanel pnl = new JPanel();
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_PRIMARY, 1), title,
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13), COLOR_PRIMARY
        ));
        return pnl;
    }

    private void thucHienTimKiem() {
        // Lấy Mã Thể Loại
        String maTL = "Tất cả";
        if (cboTheLoai.getSelectedIndex() > 0) {
            maTL = cboTheLoai.getSelectedItem().toString().split(" - ")[0];
        }
        
        // Lấy Mã Tác Giả
        String maTG = "Tất cả";
        if (cboTacGia.getSelectedIndex() > 0) {
            maTG = cboTacGia.getSelectedItem().toString().split(" - ")[0];
        }

        // Lấy Mã NXB
        String maNXB = "Tất cả";
        if (cboNXB.getSelectedIndex() > 0) {
            maNXB = cboNXB.getSelectedItem().toString().split(" - ")[0];
        }

        // Kiểm tra Năm XB
        int namXB = 0;
        if (!txtNamXB.getText().trim().isEmpty()) {
            try {
                namXB = Integer.parseInt(txtNamXB.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Năm xuất bản phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return; // Dừng lại không tìm kiếm nếu nhập sai
            }
        }

        // Kiểm tra mức giá
        double minGia = 0, maxGia = Double.MAX_VALUE;
        try {
            if (!txtGiaTu.getText().trim().isEmpty()) {
                minGia = Double.parseDouble(txtGiaTu.getText().trim());
            }
            if (!txtGiaDen.getText().trim().isEmpty()) {
                maxGia = Double.parseDouble(txtGiaDen.getText().trim());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Mức giá phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Gọi hàm BUS tìm kiếm truyền đủ 6 tham số mới
        ArrayList<SachDTO> ketQua = sachBUS.timKiemNangCao(maTL, maTG, maNXB, namXB, minGia, maxGia);

        if (ketQua.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy cuốn sách nào khớp với điều kiện!");
        }

        // Cập nhật lại Bảng ở Form chính
        parentPanel.loadDataLenBang(ketQua);
        
        // Tắt dialog
        this.dispose();
    }
}