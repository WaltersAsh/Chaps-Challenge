package nz.ac.vuw.ecs.swen225.gp20.rendering;


import nz.ac.vuw.ecs.swen225.gp20.maze.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class BoardView extends JComponent {

    private Maze m;
    private Tile[][] tiles;
    private int blockSize =80;

    public BoardView(){
        m = BoardRig.lesson1();
        tiles = m.getTiles();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int row=4; row<tiles.length; row++){
            for(int col = 5; col<tiles[row].length; col++){
                Tile t = m.getTileAt(row,col);
                g.drawImage(t.getImage(), (col-5)*blockSize, (row-4)*blockSize, blockSize, blockSize, this);
                if(t instanceof PathTile){
                    PathTile pt = (PathTile)t;
                    if(!pt.getContainedEntities().isEmpty()){
                        for(Containable c: pt.getContainedEntities()){
                            g.drawImage(c.getImage(), (col-5)*blockSize, (row-4)*blockSize, blockSize, blockSize, this);
                        }
                    }
                }
            }
        }

    }
}
