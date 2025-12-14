package FinalTerm;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    private Map<String, Image> images;

    public ImageManager() {
        images = new HashMap<>();
        
        // 뱀 (사용 안 함)
        loadImage("head", "head.png");
        loadImage("body", "body.png");
        loadImage("tail", "tail.png");

        // 먹이
        loadImage("food_normal", "apple.png");
        loadImage("food_rare", "goldenapple.png");
        
        // 랜덤 아이템
        loadImage("item_random", "randombox.png");
        
        // 포탈
        loadImage("portal_purple", "portal_purple.png");
        loadImage("portal_blue", "portal_blue.png");
        
        // 망치 & 로고
        loadImage("hammer", "hammer.png");
        loadImage("logo", "snakegame.png");
        
        // [추가] 배경 이미지
        loadImage("background", "background.png");
    }

    private void loadImage(String key, String fileName) {
        String path = "images" + File.separator + fileName;
        File file = new File(path);
        
        if (file.exists()) {
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                images.put(key, icon.getImage());
                System.out.println("[성공] 이미지 로드됨: " + path);
            } else {
                System.err.println("[에러] 이미지 로딩 중 문제 발생: " + path);
            }
        } else {
            System.err.println("[실패] 파일을 찾을 수 없습니다: " + file.getAbsolutePath());
        }
    }

    public Image getImage(String key) {
        return images.get(key);
    }
}