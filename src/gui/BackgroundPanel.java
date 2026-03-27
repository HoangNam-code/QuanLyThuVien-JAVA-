package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
    private Image bgImage;

    public BackgroundPanel() {
        try {
            // Lấy chung 1 tấm ảnh nền bạn đã tải
            String currentDir = System.getProperty("user.dir");
            String imagePath = currentDir + File.separator + "img" + File.separator + "bg_thuvien.jpg";
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                bgImage = new ImageIcon(imagePath).getImage();
            } else {
                System.out.println("Không tìm thấy ảnh tại: " + imagePath);
            }
        } catch (Exception e) {
            System.out.println("Lỗi load ảnh nền ở BackgroundPanel!");
        }
        setOpaque(false); // Xóa nền xám mặc định của Java để lộ ảnh
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            // 1. Vẽ ảnh nền
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            
            // 2. Phủ lớp màu đen mờ (Alpha = 120) để làm nổi bật các ô màu trắng
            g.setColor(new Color(0, 0, 0, 120)); 
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
