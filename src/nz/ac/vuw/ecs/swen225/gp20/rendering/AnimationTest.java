package nz.ac.vuw.ecs.swen225.gp20.rendering;

import javax.lang.model.type.ExecutableType;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class AnimationTest extends Component implements KeyListener, ActionListener {
  static Toolkit toolKit = Toolkit.getDefaultToolkit();
  Map<Character, List<Image>> steveMoves = new HashMap<>();
  List<Image> currentList = new ArrayList<>();
  Image currentImage, stillImage;

  int count = 0;
  JFrame frame;
  char prevChar = ' ';
  boolean isPressed = false;

  static final int GAME_SPEED = 150;

  int currentX = 0, currentY = 0;
  double velx = 0, vely = 0;

  Timer t = new Timer(10, this);

  @Override
  public void actionPerformed(ActionEvent e) {
    repaint();
    currentX += velx;
    currentY += vely;
  }

  public AnimationTest() {
    frame = new JFrame();
    frame.setSize(400, 400);
    frame.setVisible(true);
    frame.add(this);
    frame.addKeyListener(this);

    List<Image> frontMoves = new ArrayList<>();
    List<Image> leftMoves = new ArrayList<>();
    List<Image> rightMoves = new ArrayList<>();
    List<Image> backMoves = new ArrayList<>();

    stillImage = toolKit.getImage(
        "resources/textures/board/moveable/character_skins/new_player_skin/SteveLeft_WalkBig.gif");
    t.start();
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    int width = frame.getWidth();
    int imageWidth = stillImage.getWidth(this);

    g.drawImage(stillImage, currentX, currentY, this);

  }

  public static void main(String[] args) {
    AnimationTest a = new AnimationTest();
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    char c = e.getKeyChar();
    move(c);
    System.out.println(velx);
  }

  public void move(Character c) {
    switch (c) {
    case 'w':
      vely = -1.5;
      velx = 0;
      break;
    case 's':
      vely = 1.5;
      velx = 0;
      break;
    case 'a':
      velx = -1.5;
      vely = 0;
      break;
    case 'd':
      velx = 1.5;
      vely = 0;
      break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    isPressed = false;
    vely = 0;
    velx = 0;
    // currentImage = stillImage;
    // repaint();
  }

}