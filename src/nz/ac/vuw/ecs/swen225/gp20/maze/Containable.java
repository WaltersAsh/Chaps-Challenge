package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Base class for objects which may be inside FreeTiles
 *
 * @author Ian 300474717
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = Exit.class),
        @JsonSubTypes.Type(value = Teleporter.class), @JsonSubTypes.Type(value = Key.class),
        @JsonSubTypes.Type(value = Treasure.class)
})

public abstract class Containable extends Drawable {

    /**
     * Instantiates a new empty Containable. For Jackson.
     */
    public Containable() {
    }

    public Containable(String filename, String initials) {
        super(filename, initials);

    }

    PathTile container;

    public PathTile getContainer() {
        return container;
    }

    public void setContainer(PathTile container) {
        this.container = container;
    }

}
