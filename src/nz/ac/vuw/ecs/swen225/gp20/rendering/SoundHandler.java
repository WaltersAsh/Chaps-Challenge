package nz.ac.vuw.ecs.swen225.gp20.rendering;

import java.util.*;

import nz.ac.vuw.ecs.swen225.gp20.maze.*;
import nz.ac.vuw.ecs.swen225.gp20.maze.event.*;

public class SoundHandler extends MazeEventListener{

  private HashMap<String, SoundEffect> sounds = new HashMap<>();
  private List<SoundEffect> pathSounds = new ArrayList<>();

  private SoundEffect prevSound = null;
  private SoundEffect currentSound = null;

  public SoundHandler(Maze m) {
    initialiseSounds();
    m.addListener(this);
  }

  /**
   * WA = wall PA = path KX = key with #X LX = lock with #X TR = treasure IN =
   * info square EL = exit lock EX = exit CH = chap XX = crate WT = water EN =
   * enemy
   */
  public void initialiseSounds() {
    initialiseStoneSounds();

    sounds.put("EX", new SoundEffect("resources/sound_effects/exit/trigger.wav"));
    sounds.put("TR", new SoundEffect("resources/sound_effects/pickup/pop.wav"));
    sounds.put("KB", new SoundEffect("resources/sound_effects/pickup/pop.wav"));
    sounds.put("KR", new SoundEffect("resources/sound_effects/pickup/pop.wav"));
    sounds.put("KG", new SoundEffect("resources/sound_effects/pickup/pop.wav"));
    sounds.put("KY", new SoundEffect("resources/sound_effects/pickup/pop.wav"));
    sounds.put("DB", new SoundEffect("resources/sound_effects/door/door_open.wav"));
    sounds.put("DR", new SoundEffect("resources/sound_effects/door/door_open.wav"));
    sounds.put("DG", new SoundEffect("resources/sound_effects/door/door_open.wav"));
    sounds.put("DY", new SoundEffect("resources/sound_effects/door/door_open.wav"));
    sounds.put("IN", new SoundEffect("resources/sound_effects/info/villagerHUH.wav"));
    sounds.put("EL", new SoundEffect("resources/sound_effects/exit/unlocked.wav"));
    sounds.put("WT", new SoundEffect("resources/sound_effects/water/splash.wav"));
    sounds.put("XX", new SoundEffect("resources/sound_effects/crate/grindstone_use1pitch.wav"));

  }

  public void initialiseStoneSounds() {
    String[] stoneFiles = { "resources/sound_effects/stone_step/step1.wav",
        "resources/sound_effects/stone_step/step2.wav",
        "resources/sound_effects/stone_step/step3.wav",
        "resources/sound_effects/stone_step/step4.wav",
        "resources/sound_effects/stone_step/step5.wav" };

    for (String s : stoneFiles) {
      pathSounds.add(new SoundEffect(s));
    }
  }


  public void playSound(String s) {
    SoundEffect current = sounds.get(s);
    if(current==null) return;
    current.play();
  }

  public void playRandomSound() {
    SoundEffect currentSound;
    Random rand = new Random();

    currentSound = pathSounds.get(rand.nextInt(pathSounds.size()));
    while (prevSound == currentSound) {
      prevSound = currentSound;
      currentSound = pathSounds.get(rand.nextInt(pathSounds.size()));
    }
    currentSound.play();
    prevSound = currentSound;
  }

  @Override
  public void update(MazeEventUnlocked e) {
    playSound(e.getDoor().getInitials());
  }

  @Override
  public void update(MazeEventPushed e) {
    playSound(e.getPushed().getInitials());
  }

  @Override
  public void update(MazeEventPushedWater e) {
    playSound("WT");

  }

  @Override
  public void update(MazeEventWalked e) {
    playRandomSound();
  }

  @Override
  public void update(MazeEventPickup e) {
    playSound(e.getPicked().getInitials());
  }

  @Override
  public void update(MazeEventWon e) {
    playSound("EX");
  }


  @Override
  public void update(MazeEventInfoField e) {
    playSound("IN");
  }

  @Override
  public void update(MazeEventExitUnlocked e) {
    playSound("EL");
  }

  // exitlock event is needed
  // sounds.get(exitlock.initials).play();
}
