module maze {
  requires java.desktop;
  requires com.google.common;
  requires java.compiler;
  requires com.fasterxml.jackson.databind;

  exports nz.ac.vuw.ecs.swen225.gp20.maze to com.fasterxml.jackson.databind;
  exports nz.ac.vuw.ecs.swen225.gp20.maze.event to nz.ac.vuw.ecs.swen225.gp20.maze, com.fasterxml.jackson.databind;
  opens nz.ac.vuw.ecs.swen225.gp20.maze to com.fasterxml.jackson.databind;
}