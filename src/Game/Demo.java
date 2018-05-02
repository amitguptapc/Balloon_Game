package Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Random;

public class Demo implements MouseListener, KeyListener {
  private static int xLoc[] = new int[12];
  private static int yLoc[] = new int[12];
  private static Random ran = new Random();
  private static java.applet.AudioClip burst;
  private static java.applet.AudioClip crash;
  private static boolean crashed = false;
  private static boolean paused = false;
  private static int crashCount = 0;
  private static int score=0;
  private static int unBurstBalloon=0;
  private static JPanel panel;
  private static Demo d1;
  public static void main(String[] args) {
    JFrame frame = new JFrame();
    Demo.panel = new JPanel();
    panel.setPreferredSize(new Dimension(700, 700));
    frame.add(Demo.panel);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setResizable(false);
    Demo.d1 = new Demo();
    panel.setFocusable(true);
    panel.requestFocus();
    Graphics g = panel.getGraphics();
    panel.addMouseListener(Demo.d1);
    panel.addKeyListener(Demo.d1);
    Image bgImage = null;
    Image balloon1Image = null;
    Image balloon2Image = null;
    Image balloon3Image = null;
    Image balloon4Image = null;
    Image missileImage = null;
    Image pauseImage = null;
    Image crashImage = null;
    burst = Applet.newAudioClip(Demo.class.getResource("../audios/balloon.wav"));
    crash = Applet.newAudioClip(Demo.class.getResource("../audios/bomb.wav"));
    java.applet.AudioClip theme = Applet.newAudioClip(Demo.class.getResource("../audios/theme.wav"));
    try {
      bgImage = ImageIO.read(Demo.class.getResource("../images/back.jpg"));
      balloon1Image = ImageIO.read(Demo.class.getResource("../images/balloon1.png"));
      balloon2Image = ImageIO.read(Demo.class.getResource("../images/balloon2.png"));
      balloon3Image = ImageIO.read(Demo.class.getResource("../images/balloon3.png"));
      balloon4Image = ImageIO.read(Demo.class.getResource("../images/balloon4.png"));
      missileImage = ImageIO.read(Demo.class.getResource("../images/rocket.png"));
      pauseImage = ImageIO.read(Demo.class.getResource("../images/pause.png"));
      crashImage = ImageIO.read(Demo.class.getResource("../images/crash.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    theme.loop();
    Image imageArray[] = {balloon1Image, balloon2Image, balloon3Image, balloon4Image,
            balloon1Image, balloon2Image, balloon3Image, balloon4Image,
            missileImage, missileImage, missileImage, missileImage};

    for (int i = 0; i <12; i++) {
      Demo.xLoc[i] = Demo.ran.nextInt(600);
      Demo.yLoc[i] = 700 + Demo.ran.nextInt(1000);
    }

    while (true) {
      try {
        Thread.sleep(70);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (Demo.crashCount == 1 && Demo.crashed) {
        panel.removeMouseListener(Demo.d1);
        g.drawImage(crashImage, 200, 200, null);
        continue;
      }
      if (Demo.crashCount == 2 && !Demo.crashed) {
        for (int i = 0; i < 12; i++) {
          Demo.xLoc[i] = Demo.ran.nextInt(600);
          Demo.yLoc[i] = 700 + Demo.ran.nextInt(1000);
        }
        panel.addMouseListener(Demo.d1);
        Demo.score=0;
        Demo.crashCount = 1;
      }

      if (Demo.paused) {
        panel.removeMouseListener(Demo.d1);
        g.drawImage(pauseImage, 220, 250, null);
        continue;
      }

      g.drawImage(bgImage, 0, -1, null);
      for (int i = 0; i < 12; i++) {
        if (Demo.yLoc[i] < -150) {
          Demo.xLoc[i] = Demo.ran.nextInt(600);
          Demo.yLoc[i] = 700;
          if(i<=7)
          Demo.unBurstBalloon++;
        }
        if(Demo.unBurstBalloon>=10){
          Demo.crash.play();
          Demo.crashed=true;
          Demo.crashCount=1;
        }
        g.drawImage(imageArray[i], Demo.xLoc[i], Demo.yLoc[i], null);
        g.drawImage(imageArray[i], Demo.xLoc[i], Demo.yLoc[i], null);
        Demo.yLoc[i] -= (i*2+5);
      }
      g.setColor(Color.BLACK);
      g.setFont(new Font("Arial",Font.BOLD,16));
      g.drawString("Your Score : "+Demo.score,300,33);
    }

  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    for (int i = 0; i < 12; i++) {
      if (x >= Demo.xLoc[i] && x <= Demo.xLoc[i] + 100 && y >= Demo.yLoc[i] && y <= Demo.yLoc[i] + 150) {
        if (i <= 7) {
          Demo.burst.play();
          Demo.xLoc[i] = Demo.ran.nextInt(600);
          Demo.yLoc[i] = 700;
          Demo.score+=10;
          break;
        } else {
          Demo.crash.play();
          Demo.crashed=true;
          Demo.crashCount=1;
        }
      }
    }
  }
  @Override
  public void mouseReleased(MouseEvent e) {

  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SPACE && !Demo.crashed) {
      Demo.paused = !Demo.paused;
      if(!Demo.paused){
        Demo.panel.addMouseListener(Demo.d1);
      }
    }
    if (e.getKeyCode() == KeyEvent.VK_ENTER && Demo.crashed) {
      Demo.crashed = false;
      Demo.crashCount = 2;
    }

  }

  @Override
  public void keyReleased(KeyEvent e) {

  }
}