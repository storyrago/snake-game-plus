package FinalTerm;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private boolean enabled;
    private Map<String, Clip> audioClips;

    public SoundManager() {
        this.enabled = true;
        this.audioClips = new HashMap<>();
        
        // WAV 파일 로드 (배경음악, 효과음)
        loadAudio("click", "click.wav");
        loadAudio("gameover", "gameover.wav");
        loadAudio("roulette", "spinningwheel.wav");
        loadAudio("break", "breakrock.wav");
        loadAudio("bgm", "mainmenubackground.wav");
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
                    if (key.equals("bgm")) {
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

    // [추가] 비프음 재생 (먹이, 포탈, 해머용)
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
            for (Clip clip : audioClips.values()) {
                if (clip.isRunning()) clip.stop();
            }
        }
    }

    public boolean isEnabled() { return enabled; }
}