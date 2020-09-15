package nz.ac.vuw.ecs.swen225.gp20.rendering;


import nz.ac.vuw.ecs.swen225.gp20.maze.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class BoardView extends JComponent {

    private Maze m;
    private Tile[][] tiles;
    private int blockSize = 40;

    public BoardView(){
        m = BoardRig.lesson1();
        tiles = m.getTiles();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int row=0; row<tiles.length; row++){
            for(int col = 0; col<tiles[row].length; col++){
                Tile t = m.getTileAt(row,col);
                g.drawImage(t.getImage(), col*blockSize, row*blockSize, blockSize, blockSize, this);
                if(t instanceof PathTile){
                    //TODO Something in here I think
                    if(!((PathTile) t).getContainable().empty()){
                        g.drawImage(t.getImage(), col*blockSize, row*blockSize, blockSize, blockSize, this);
                    }
                }
            }
        }

        System.out.println(m);

    }
}
