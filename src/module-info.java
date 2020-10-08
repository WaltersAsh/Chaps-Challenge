module maze {
  requires java.desktop;
  requires com.google.common;
  requires java.compiler;
  requires com.fasterxml.jackson.databind;
  requires org.junit.jupiter.api;
  requires com.fasterxml.jackson.annotation;

  exports nz.ac.vuw.ecs.swen225.gp20.maze to com.fasterxml.jackson.databind;
  exports nz.ac.vuw.ecs.swen225.gp20.maze.event to com.fasterxml.jackson.databind;
  opens nz.ac.vuw.ecs.swen225.gp20.maze to com.fasterxml.jackson.databind;
  exports nz.ac.vuw.ecs.swen225.gp20.application to com.fasterxml.jackson.databind;

}