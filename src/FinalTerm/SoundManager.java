// SoundManager.java
package FinalTerm;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private boolean enabled;
    private Map<String, Clip> audioClips;
    
    //현재 재생되어야 할 배경음악 키를 저장
    private String currentBgmKey;

    public SoundManager() {
        this.enabled = true;
        this.audioClips = new HashMap<>();
        
        loadAudio("click", "click.wav");
        loadAudio("gameover", "gameover.wav");
        loadAudio("roulette", "spinningwheel.wav");
        loadAudio("break", "breakrock.wav");
        loadAudio("bgm", "mainmenubackground.wav");
        loadAudio("game_bgm", "gamebackground.wav");
    }

    private void loadAudio(String key, String filename) {
        try {
            File file = new File("audio" + File.separator + filename);
            if (file.exists()) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                
                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    if (key.equals("bgm") || key.equals("game_bgm")) {
                        gainControl.setValue(-20.0f); // 배경음악 은은하게
                    } else {
                        gainControl.setValue(0.0f);
                    }
                }
                audioClips.put(key, clip);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSound(int frequency, int duration) {
        if (!enabled) return;
        
        new Thread(() -> {
            try {
                AudioFormat af = new AudioFormat(8000, 8, 1, true, false);
                SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
                sdl.open(af);
                sdl.start();
                
                byte[] buf = new byte[duration * 8];
                for (int i = 0; i < buf.length; i++) {
                    double angle = i / (8000.0 / frequency) * 2.0 * Math.PI;
                    buf[i] = (byte)(Math.sin(angle) * 127.0);
                }
                
                sdl.write(buf, 0, buf.length);
                sdl.drain();
                sdl.close();
            } catch (Exception e) {}
        }).start();
    }

    public void playClip(String key) {
        if (!enabled || !audioClips.containsKey(key)) return;
        try {
            Clip clip = audioClips.get(key);
            if (clip.isRunning()) clip.stop();
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {}
    }
    
    public void playClipIfNotPlaying(String key) {
        if (!enabled || !audioClips.containsKey(key)) return;
        try {
            Clip clip = audioClips.get(key);
            if (!clip.isRunning()) {
                clip.setFramePosition(0);
                clip.start();
            }
        } catch (Exception e) {}
    }
    
    public void loopClip(String key) {
        //어떤 음악이 재생되어야 하는지 항상 기억
        currentBgmKey = key;
        
        if (!enabled || !audioClips.containsKey(key)) return;
        try {
            Clip clip = audioClips.get(key);
            if (clip.isRunning()) return; 
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {}
    }
    
    public void stopClip(String key) {
        if (audioClips.containsKey(key)) {
            Clip clip = audioClips.get(key);
            if (clip != null && clip.isRunning()) clip.stop();
        }
    }

    public void toggle() {
        enabled = !enabled;
        
        if (!enabled) {
            // 소리 끄기: 모든 클립 중지
            for (Clip clip : audioClips.values()) {
                if (clip.isRunning()) clip.stop();
            }
        } else {
            //소리 켜기: 배경음악이 설정되어 있었다면 다시 재생
            if (currentBgmKey != null) {
                // loopClip을 호출하여 enabled 상태 체크 후 재생 시작
                loopClip(currentBgmKey);
            }
        }
    }

    public boolean isEnabled() { return enabled; }
}