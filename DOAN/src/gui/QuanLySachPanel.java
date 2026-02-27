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
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import util.Formatter;

public class QuanLySachPanel extends JPanel {

    private SachBUS sachBUS = new SachBUS();
    private JTable tblSach;
    private DefaultTableModel model;

    // Các ô nhập liệu
    private JTextField txtMa, txtTen, txtNamXB, txtSoLuong, txtDonGia, txtSoTrang, txtTimKiem;
    private JComboBox<String> cboTacGia, cboTheLoai, cboNXB;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiemNC;

    public QuanLySachPanel() {
        initComponents();
        loadDuLieuLenComboBox(); // Đổ dữ liệu vào ComboBox trước
        loadDataLenBang(sachBUS.getList()); // Đổ danh sách Sách lên bảng
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10)); // Thêm khoảng cách giữa các phần
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding viền ngoài cùng

        // --- 1. TIÊU ĐỀ ---
        JLabel lblTitle = new JLabel("QUẢN LÝ KHO SÁCH");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(25, 118, 210));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(new EmptyBorder(10, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // --- 2. BẢNG VÀ TÌM KIẾM (GIỮA) ---
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBackground(Color.WHITE);
        
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlSearch.setBackground(Color.WHITE);
        JLabel lblTimKiem = new JLabel("Tìm nhanh (Tên/Mã):");
        lblTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlSearch.add(lblTimKiem);
        
        txtTimKiem = new JTextField(25);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTimKiem.setPreferredSize(new Dimension(200, 30));
        pnlSearch.add(txtTimKiem);
        
        btnTimKiemNC = new JButton("Tìm nâng cao");
        btnTimKiemNC.setBackground(new Color(25, 118, 210));
        btnTimKiemNC.setForeground(Color.blue); // Có thể đổi thành Color.WHITE cho đẹp hơn
        btnTimKiemNC.setFocusPainted(false);
        pnlSearch.add(btnTimKiemNC);
        
        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        String[] cols = {"Mã Sách", "Tên Sách", "Tác Giả", "Thể Loại", "NXB", "Năm XB", "Trang", "Số Lượng", "Đơn Giá"};
        model = new DefaultTableModel(cols, 0);
        tblSach = new JTable(model);
        tblSach.setRowHeight(30);
        tblSach.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblSach.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblSach.getTableHeader().setBackground(new Color(240, 240, 240));
        
        // Sự kiện click vào bảng
        tblSach.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { fillForm(); }
        });
        
        // Bắt sự kiện khi dùng phím mũi tên Lên/Xuống trên bàn phím
        tblSach.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    fillForm(); 
                }
            }
        });
        pnlCenter.add(new JScrollPane(tblSach), BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // --- 3. FORM NHẬP LIỆU (DƯỚI) - ĐÃ CẢI TIẾN GIAO DIỆN ---
        JPanel pnlSouth = new JPanel(new BorderLayout(0, 15));
        pnlSouth.setBackground(Color.WHITE);
        
        // Dùng GridLayout 3x3 nhưng với các JPanel con để cố định Label và Input sát nhau
        JPanel pnlInput = new JPanel(new GridLayout(3, 3, 20, 15)); 
        pnlInput.setBackground(Color.WHITE);
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Thông tin chi tiết sách");
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        border.setTitleColor(new Color(25, 118, 210));
        pnlInput.setBorder(BorderFactory.createCompoundBorder(border, new EmptyBorder(15, 15, 15, 15)));
        
        txtMa = new JTextField(); 
        txtTen = new JTextField();
        txtNamXB = new JTextField();
        txtSoLuong = new JTextField();
        txtDonGia = new JTextField();
        txtSoTrang = new JTextField();
        cboTacGia = new JComboBox<>();
        cboTheLoai = new JComboBox<>();
        cboNXB = new JComboBox<>();

        // Gọi hàm hỗ trợ tạo từng ô ghép cặp (Label + Input)
        pnlInput.add(createFormItem("Mã Sách:", txtMa));
        pnlInput.add(createFormItem("Tên Sách:", txtTen));
        pnlInput.add(createFormItem("Đơn Giá:", txtDonGia));
        
        pnlInput.add(createFormItem("Tác Giả:", cboTacGia));
        pnlInput.add(createFormItem("Thể Loại:", cboTheLoai));
        pnlInput.add(createFormItem("Số Lượng:", txtSoLuong));

        pnlInput.add(createFormItem("Nhà XB:", cboNXB));
        pnlInput.add(createFormItem("Năm XB:", txtNamXB));
        pnlInput.add(createFormItem("Số Trang:", txtSoTrang));

        // Các nút chức năng
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pnlBtn.setBackground(Color.WHITE);
        btnThem = createButton("Thêm", "them.png", new Color(46, 204, 113));
        btnSua = createButton("Sửa", "sua.png", new Color(241, 196, 15));
        btnXoa = createButton("Xóa", "xoa.png", new Color(231, 76, 60));
        btnLamMoi = createButton("Làm mới", "lammoi.png", new Color(149, 165, 166));
        
        pnlBtn.add(btnThem); pnlBtn.add(btnSua); pnlBtn.add(btnXoa); pnlBtn.add(btnLamMoi);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);
        pnlSouth.add(pnlBtn, BorderLayout.SOUTH);
        add(pnlSouth, BorderLayout.SOUTH);

        // --- 4. SỰ KIỆN NÚT BẤM ---
        btnThem.addActionListener(e -> xuLyThem());
        btnSua.addActionListener(e -> xuLySua());
        btnXoa.addActionListener(e -> xuLyXoa());
        btnLamMoi.addActionListener(e -> lamMoi());

        // Sự kiện gõ phím tìm kiếm nhanh trên RAM
        txtTimKiem.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                ArrayList<SachDTO> list = sachBUS.timKiemTrenRAM(txtTimKiem.getText());
                loadDataLenBang(list);
            }
        });
        
        btnTimKiemNC.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chức năng Tìm kiếm Nâng cao sẽ mở ở Form khác!");
            // Bước tiếp theo tôi sẽ hướng dẫn bạn làm Form TimKiemNangCaoDialog
        });
    }

    // =========================================================================
    // CÁC HÀM HỖ TRỢ VẼ GIAO DIỆN (UI HELPERS)
    // =========================================================================
    
    // Hàm này giúp dán sát Label và TextField lại với nhau cực đẹp
    private JPanel createFormItem(String labelText, JComponent comp) {
        JPanel panel = new JPanel(new BorderLayout(10, 0)); // Khoảng cách 10px giữa chữ và ô nhập
        panel.setBackground(Color.WHITE);
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setPreferredSize(new Dimension(70, 30)); // Cố định chiều rộng để thẳng hàng nhau
        
        comp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comp.setPreferredSize(new Dimension(0, 30)); // Chiều cao đồng đều 30px
        
        panel.add(lbl, BorderLayout.WEST);
        panel.add(comp, BorderLayout.CENTER);
        return panel;
    }

    // Hàm trang trí nút bấm 
    private JButton createButton(String text, String iconName, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(115, 40)); // Tăng kích thước nút một chút để có chỗ chứa icon
        
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        // --- ĐOẠN XỬ LÝ ICON ---
        if (iconName != null && !iconName.isEmpty()) {
            try {
                java.net.URL resource = getClass().getResource("/img/" + iconName);
                if (resource != null) {
                    ImageIcon icon = new ImageIcon(resource);
                    // Thu nhỏ ảnh về kích thước 20x20 cho đẹp
                    Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    btn.setIcon(new ImageIcon(img));
                    btn.setIconTextGap(8); // Khoảng cách giữa icon và chữ
                }
            } catch (Exception e) {
                System.out.println("Không tải được icon: " + iconName);
            }
        }
        
        return btn;
    }

    // =========================================================================
    // CÁC HÀM XỬ LÝ DỮ LIỆU
    // =========================================================================

    private void loadDuLieuLenComboBox() {
        // Load Tác Giả
        ArrayList<TacGiaDTO> listTG = new TacGiaDAO().selectAll();
        for (TacGiaDTO tg : listTG) cboTacGia.addItem(tg.getMaTG() + " - " + tg.getHoTen());
        
        // Load Thể Loại
        ArrayList<TheLoaiDTO> listTL = new TheLoaiDAO().selectAll();
        for (TheLoaiDTO tl : listTL) cboTheLoai.addItem(tl.getMaTL() + " - " + tl.getTenTL());
        
        // Load NXB
        ArrayList<NhaXuatBanDTO> listNXB = new NhaXuatBanDAO().selectAll();
        for (NhaXuatBanDTO nxb : listNXB) cboNXB.addItem(nxb.getMaNXB() + " - " + nxb.getTenNXB());
    }

    private void loadDataLenBang(ArrayList<SachDTO> list) {
        model.setRowCount(0);
        for (SachDTO s : list) {
            model.addRow(new Object[]{
                s.getMaSach(), s.getTenSach(), s.getMaTG(), s.getMaTL(),
                s.getMaNXB(), s.getNamXB(), s.getSoTrang(), s.getSoLuong(), 
                Formatter.FormatVND(s.getDonGia()) 
            });
        }
    }

    private void fillForm() {
        int row = tblSach.getSelectedRow();
        if (row >= 0) {
            txtMa.setText(model.getValueAt(row, 0).toString());
            txtTen.setText(model.getValueAt(row, 1).toString());
            
            // Xử lý chọn đúng ComboBox (Tìm item có chứa Mã)
            setComboBoxItem(cboTacGia, model.getValueAt(row, 2).toString());
            setComboBoxItem(cboTheLoai, model.getValueAt(row, 3).toString());
            setComboBoxItem(cboNXB, model.getValueAt(row, 4).toString());

            txtNamXB.setText(model.getValueAt(row, 5).toString());
            txtSoTrang.setText(model.getValueAt(row, 6).toString());
            txtSoLuong.setText(model.getValueAt(row, 7).toString());
            
            // Xóa chữ VNĐ và dấu phẩy để hiển thị lại số gốc
            String gia = model.getValueAt(row, 8).toString().replace(" VNĐ", "").replace(",", "");
            txtDonGia.setText(gia);
            
            txtMa.setEditable(false);
        }
    }

    private void setComboBoxItem(JComboBox<String> cbo, String ma) {
        for (int i = 0; i < cbo.getItemCount(); i++) {
            if (cbo.getItemAt(i).startsWith(ma + " - ")) {
                cbo.setSelectedIndex(i);
                break;
            }
        }
    }

    // --- CÁC HÀM XỬ LÝ (CRUD) ---
    private SachDTO layThongTinForm() throws Exception {
        SachDTO s = new SachDTO();
        s.setMaSach(txtMa.getText());
        s.setTenSach(txtTen.getText());
        s.setNamXB(Integer.parseInt(txtNamXB.getText()));
        s.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
        s.setDonGia(Double.parseDouble(txtDonGia.getText()));
        s.setSoTrang(Integer.parseInt(txtSoTrang.getText()));
        
        // Cắt chuỗi để chỉ lấy MÃ (VD: "TG01 - Nam Cao" -> lấy "TG01")
        s.setMaTG(cboTacGia.getSelectedItem().toString().split(" - ")[0]);
        s.setMaTL(cboTheLoai.getSelectedItem().toString().split(" - ")[0]);
        s.setMaNXB(cboNXB.getSelectedItem().toString().split(" - ")[0]);
        return s;
    }

    private void xuLyThem() {
        try {
            SachDTO s = layThongTinForm();
            JOptionPane.showMessageDialog(this, sachBUS.themSach(s));
            loadDataLenBang(sachBUS.getList());
            lamMoi();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Vui lòng kiểm tra lại số liệu nhập!"); }
    }

    private void xuLySua() {
        try {
            SachDTO s = layThongTinForm();
            JOptionPane.showMessageDialog(this, sachBUS.suaSach(s));
            loadDataLenBang(sachBUS.getList());
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Vui lòng kiểm tra lại số liệu nhập!"); }
    }

    private void xuLyXoa() {
        String ma = txtMa.getText();
        if (ma.isEmpty()) return;
        if (JOptionPane.showConfirmDialog(this, "Xóa sách " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, sachBUS.xoaSach(ma));
            loadDataLenBang(sachBUS.getList());
            lamMoi();
        }
    }

    private void lamMoi() {
        txtMa.setText(""); txtMa.setEditable(true);
        txtTen.setText(""); txtNamXB.setText("");
        txtSoLuong.setText(""); txtDonGia.setText(""); txtSoTrang.setText("");
        if(cboTacGia.getItemCount() > 0) cboTacGia.setSelectedIndex(0);
        if(cboTheLoai.getItemCount() > 0) cboTheLoai.setSelectedIndex(0);
        if(cboNXB.getItemCount() > 0) cboNXB.setSelectedIndex(0);
        tblSach.clearSelection();
    }
}