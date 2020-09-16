package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Base class for all Tiles representable on the board
 *
 * @author Ian 300474717
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PathTile.class), @JsonSubTypes.Type(value = WallTile.class)
})
public abstract class Tile extends Drawable {
    int row, col;
    boolean walkable = false;

    public Tile(String filename, String initials) {
        super(filename, initials);
    }

    public Tile(String id, String initials, String filename, int row, int col, boolean walkable) {
        super(id, initials, filename);
        this.row = row;
        this.col = col;
        this.walkable = walkable;
    }

    public Tile() {

    }

    /**
     * can the Chap walk on this Tile?
     *
     * @return whether Chap can walk on this Tile
     */
    public boolean isWalkable() {
        return walkable;
    }

    /**
     * Set the coordinates of this Drawable
     *
     * @param r the row
     * @param c the column
     */
    public void setCoords(int r, int c) {
        row = r;
        col = c;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return this.getClass() + " at " + row + ", " + col;
    }
}
