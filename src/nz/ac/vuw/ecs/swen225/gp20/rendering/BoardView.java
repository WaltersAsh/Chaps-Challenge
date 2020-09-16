package nz.ac.vuw.ecs.swen225.gp20.rendering;


import nz.ac.vuw.ecs.swen225.gp20.application.Gui;
import nz.ac.vuw.ecs.swen225.gp20.maze.*;

import javax.swing.*;
import java.awt.*;

public class BoardView extends JComponent {

    private Maze m;
    private Tile[][] tiles;
    private int blockSize =40;
    private int width, height, panelWidth, panelHeight;


    public BoardView(Maze m){
        this.m = m;
        m = BoardRig.lesson1();
        tiles = m.getTiles();
        width = m.getWidth();
        height = m.getHeight();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);



        //drawWholeBoard(g);
        drawWindowedBoard(g);
    }

    public void drawWholeBoard(Graphics g){
        for(int row=0; row<height; row++){
            for(int col = 0; col<width; col++){
                Tile t = m.getTileAt(row,col);
                g.drawImage(getToolkit().getImage(t.getFilename()), col*blockSize, row*blockSize, blockSize, blockSize, this);
                if(t instanceof PathTile){
                    PathTile pt = (PathTile)t;
                    if(!pt.getContainedEntities().isEmpty()){
                        for(Containable c: pt.getContainedEntities()){
                            g.drawImage(getToolkit().getImage(c.getFilename()), col * blockSize, row * blockSize, blockSize, blockSize, this);
                        }
                    }
                }
            }
        }
    }

    /**
     * Draws the windowed board on the screen.
     * @param g the graphics used
     */
    public void drawWindowedBoard(Graphics g){
        //TODO Get resizing working
        int minPanel = Math.min(panelHeight, panelWidth);

        int viewTiles = 4;
        int windowSize = (2*viewTiles)+1;



        int blockSize = 71;
//        System.out.printf("%d/%d=%d\n", panelHeight,panelWidth,blockSize);
//        System.out.printf("%d/%d=%d\n", minPanel,windowSize,blockSize);

        int chapRow = m.getChap().getContainer().getRow();
        int chapCol = m.getChap().getContainer().getCol();

        int startRow = chapRow - viewTiles;
        int startCol = chapCol - viewTiles;

        int endRow = chapRow + viewTiles;
        int endCol = chapCol + viewTiles;

        if(startRow<0){startRow = 0;}
        if(startCol<0){startCol = 0;}

        if(startCol+windowSize>width-1){startCol = width-1-windowSize;}
        if(startRow+windowSize>height-1){startRow = height-1-windowSize;}

        int currentRow = 0;
        for(int row=startRow; row<startRow+windowSize; row++){
            int currentCol = 0;
            for(int col = startCol; col<startCol+windowSize; col++){
                Tile t = m.getTileAt(row,col);
                g.drawImage(getToolkit().getImage(t.getFilename()), currentCol*blockSize, currentRow*blockSize, blockSize, blockSize, this);
                if(t instanceof PathTile){
                    PathTile pt = (PathTile)t;
                    if(!pt.getContainedEntities().isEmpty()){
                        for(Containable c: pt.getContainedEntities()){
                            g.drawImage(getToolkit().getImage(c.getFilename()), currentCol * blockSize, currentRow * blockSize, blockSize, blockSize, this);
                        }
                    }
                }
                currentCol++;
            }
            currentRow++;

        }



    }
}
