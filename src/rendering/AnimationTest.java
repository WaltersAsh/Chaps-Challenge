package rendering;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;


public class AnimationTest extends Component implements KeyListener {
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
    int dx = 5, dy = 5;


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

        Map<Character, Image> stillImages = new HashMap<>();

        frontMoves.add(toolKit.getImage("resources/textures/player_skin/PlayerFront_LeftLegMove.png"));
        frontMoves.add(toolKit.getImage("resources/textures/player_skin/PlayerFront_RightLegMove.png"));
        rightMoves.add(toolKit.getImage("resources/textures/player_skin/PlayerRight_Walk1.png"));
        rightMoves.add(toolKit.getImage("resources/textures/player_skin/PlayerRight_Walk2.png"));
        rightMoves.add(toolKit.getImage("resources/textures/player_skin/PlayerRight_Walk3.png"));
        rightMoves.add(toolKit.getImage("resources/textures/player_skin/PlayerRight_Walk2.png"));
        leftMoves.add(toolKit.getImage("resources/textures/player_skin/PlayerLeft_Walk1.png"));
        leftMoves.add(toolKit.getImage("resources/textures/player_skin/PlayerLeft_Walk2.png"));
        leftMoves.add(toolKit.getImage("resources/textures/player_skin/PlayerLeft_Walk3.png"));
        leftMoves.add(toolKit.getImage("resources/textures/player_skin/PlayerLeft_Walk2.png"));
        backMoves.add(toolKit.getImage("resources/textures/player_skin/PlayerBack_LeftLegMove.png"));
        backMoves.add(toolKit.getImage("resources/textures/player_skin/PlayerBack_RightLegMove.png"));

        steveMoves.put('w', backMoves);
        steveMoves.put('a', rightMoves);
        steveMoves.put('s', frontMoves);
        steveMoves.put('d', leftMoves);

        stillImage = toolKit.getImage("resources/textures/player_skin/PlayerFront_Still.png");


        new Timer(GAME_SPEED, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPressed) {
                    animate(currentList);
                }
            }
        }).start();
    }


    public void animate(List<Image> l) {
        if (!l.isEmpty()) {
            currentImage = currentList.get(count);
            count++;
            repaint();
            if (count > currentList.size() - 1) {
                count = 0;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (currentList.size() == 0) {
            currentImage = stillImage;
        }
        int width = frame.getWidth();
        int imageWidth = currentImage.getWidth(this);

        g.drawImage(currentImage, width / 2 - imageWidth / 2, currentY, this);

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
        if (steveMoves.containsKey(c)) {
            isPressed = true;
            currentList = steveMoves.get(c);
        } else {
            isPressed = false;
        }
        if (prevChar != c) {
            count = 0;
            prevChar = c;
        }
        //move(c);


    }

    public void move(Character c) {
        switch (c) {
            case 'w':
                currentY -= dy;
                break;
            case 's':
                currentY += dy;
                break;
            case 'a':
                currentX -= dx;
                break;
            case 'd':
                currentX += dx;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        isPressed = false;
        //currentImage = stillImage;
        //repaint();
    }
}