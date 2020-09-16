package nz.ac.vuw.ecs.swen225.gp20.maze;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Base class for objects which may be inside FreeTiles
 *
 * @author Ian 300474717
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Treasure.class), @JsonSubTypes.Type(value = Exit.class), @JsonSubTypes.Type(value = Door.class), @JsonSubTypes.Type(value = ExitLock.class), @JsonSubTypes.Type(value = Key.class), @JsonSubTypes.Type(value = InfoField.class),@JsonSubTypes.Type(value = Chap.class)
})
public abstract class Containable extends Drawable {
    PathTile container;

    public Containable(String filename, String initials) {
        super(filename, initials);
    }

    public Containable(String id, String initials, String filename, PathTile container) {
        super(id, initials, filename);
        this.container = container;
    }

    public Containable(PathTile container) {
        this.container = container;
    }

    protected Containable() {}

    public PathTile getContainer() {
        return container;
    }

    public void setContainer(PathTile container) {
        this.container = container;
    }

}
