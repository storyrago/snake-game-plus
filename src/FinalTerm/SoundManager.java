package FinalTerm;

//SoundManager.java - 사운드 관리

import javax.sound.sampled.*;

public class SoundManager {
 private boolean enabled;
 
 public SoundManager() {
     this.enabled = true;
 }
 
 public void playSound(int frequency, int duration) {
     if (!enabled) return;
     
     new Thread(() -> {
         try {
             AudioFormat af = new AudioFormat(8000, 8, 1, true, false);
             SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
             sdl.open(af);
             sdl.start();
             
             byte[] buf = new byte[duration];
             for (int i = 0; i < buf.length; i++) {
                 double angle = i / (8000.0 / frequency) * 2.0 * Math.PI;
                 buf[i] = (byte)(Math.sin(angle) * 127.0);
             }
             
             sdl.write(buf, 0, buf.length);
             sdl.drain();
             sdl.close();
         } catch (Exception e) {
             // 사운드 재생 실패 무시
         }
     }).start();
 }
 
 public void toggle() {
     enabled = !enabled;
 }
 
 public boolean isEnabled() {
     return enabled;
 }
}

