module maze {
	requires java.desktop;
	requires com.google.common;
	requires java.compiler;
    requires com.google.gson;
	opens nz.ac.vuw.ecs.swen225.gp20.maze to com.google.gson;

}