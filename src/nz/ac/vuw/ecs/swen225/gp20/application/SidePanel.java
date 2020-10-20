package nz.ac.vuw.ecs.swen225.gp20.application;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.stream.IntStream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import nz.ac.vuw.ecs.swen225.gp20.maze.Maze;

/**
 * SidePanel class for instantiating a side panel for the gui.
 *
 * @author Justin 300470389
 */
public class SidePanel extends JPanel {

  // inner panels inside of side panel
  private JPanel levelPanel;
  private JPanel timePanel;
  private JPanel treasuresPanel;
  private JPanel inventoryPanel;

  // inner panels inside of inner panels of side panel
  private JPanel levelContentPanel;
  private JPanel timeContentPanel;
  private JPanel treasuresContentPanel;
  private JPanel inventoryGridPanel;
  private JPanel inventoryContentPanel;

  // value labels
  private JLabel levelValueLabel;
  private JLabel timeValueLabel;
  private JLabel treasuresValueLabel;
  private JLabel[] inventoryValueLabels;

  private final Maze maze;

  /**
   * Side panel is constructed with its inner side panels.
   *
   * @param m the maze passed through from the gui
   */
  public SidePanel(Maze m) {
    maze = m;

    initialise();
    createInnerSidePanels();
    initialiseInnerSidePanels();

    // add content panels to inner side panels
    levelPanel.add(levelContentPanel);
    timePanel.add(timeContentPanel);
    treasuresPanel.add(treasuresContentPanel);
    inventoryPanel.add(inventoryContentPanel);

    // add inner side panels to this
    add(levelPanel);
    add(timePanel);
    add(treasuresPanel);
    add(inventoryPanel);
  }

  /**
   * Create the side panel.
   */
  private void initialise() {
    setBackground(ComponentLibrary.paleLavender);
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(new EmptyBorder(5, 5, 5, 5));
    setPreferredSize(new Dimension(175, 500));
    setMaximumSize(new Dimension(250, 800));
  }

  /**
   * Create the inner side panels.
   */
  private void createInnerSidePanels() {
    final JPanel[] panels = new JPanel[]{
      levelPanel = new JPanel(),
      timePanel = new JPanel(),
      treasuresPanel = new JPanel(),
      inventoryPanel = new JPanel()
    };

    inventoryGridPanel = new JPanel();

    inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));
    inventoryGridPanel.setLayout(new GridLayout(2, 4));

    levelPanel.setBackground(ComponentLibrary.fullLavender);
    timePanel.setBackground(ComponentLibrary.lavender);
    treasuresPanel.setBackground(ComponentLibrary.deepLavender);
    inventoryPanel.setBackground(ComponentLibrary.darkLavender);
    inventoryGridPanel.setBackground(ComponentLibrary.darkLavender);

    Arrays.stream(panels).forEach(panel -> {
      panel.setPreferredSize(new Dimension(175, 125));
      panel.setBorder(new LineBorder(ComponentLibrary.paleLavender, 2, false));
    });

    // inventory grid panel initialisation
    inventoryValueLabels = new JLabel[8];
    IntStream.range(0, 8).forEach(i -> {
      JLabel label = new JLabel();
      label.setText(" ");
      label.setOpaque(true);
      label.setBackground(ComponentLibrary.darkLavender);
      label.setBorder(BorderFactory.createLineBorder(ComponentLibrary.paleLavender));
      inventoryValueLabels[i] = label;
      inventoryGridPanel.add(label);
    });
  }

  /**
   * Create and initialise the panels in the side panel.
   */
  private void initialiseInnerSidePanels() {
    // initialise inner panels for inner panels in side panel
    JPanel[] panels = new JPanel[]{
      levelContentPanel = new JPanel(),
      timeContentPanel = new JPanel(),
      treasuresContentPanel = new JPanel(),
      inventoryContentPanel = new JPanel()
    };

    Arrays.stream(panels).forEach(panel -> panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)));

    levelContentPanel.setBackground(ComponentLibrary.fullLavender);
    timeContentPanel.setBackground(ComponentLibrary.lavender);
    treasuresContentPanel.setBackground(ComponentLibrary.deepLavender);
    inventoryContentPanel.setBackground(ComponentLibrary.darkLavender);

    // initialise title labels for panels in inner side panel
    JLabel levelTitleLabel;
    JLabel timeTitleLabel;
    JLabel treasuresTitleLabel;
    JLabel inventoryTitleLabel;
    JLabel[] titleLabels = new JLabel[]{
      levelTitleLabel = new JLabel("LEVEL"),
      timeTitleLabel = new JLabel("TIME LEFT"),
      treasuresTitleLabel = new JLabel("TREASURES REMAINING"),
      inventoryTitleLabel = new JLabel("INVENTORY"),
    };

    //configure and stylise title labels
    Arrays.stream(titleLabels).forEach(label -> {
      label.setFont(ComponentLibrary.sideFont);
      label.setForeground(Color.WHITE);
      label.setAlignmentX(Component.CENTER_ALIGNMENT);
    });

    // initialise value labels
    JLabel[] valueLabels = new JLabel[]{
      levelValueLabel = new JLabel("1"),
      timeValueLabel = new JLabel("60"),
      treasuresValueLabel = new JLabel(String.valueOf(maze.numTreasures())),
    };

    //configure and stylise value labels
    Arrays.stream(valueLabels).forEach(valueLabel -> {
      valueLabel.setFont(ComponentLibrary.bigFont);
      valueLabel.setForeground(Color.BLACK);
      valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    });

    // add labels and JComponents to inner panels in side panels
    levelContentPanel.add(levelTitleLabel);
    levelContentPanel.add(Box.createRigidArea(new Dimension(0, 35)));
    levelContentPanel.add(levelValueLabel);
    timeContentPanel.add(timeTitleLabel);
    timeContentPanel.add(Box.createRigidArea(new Dimension(0, 35)));
    timeContentPanel.add(timeValueLabel);
    treasuresContentPanel.add(treasuresTitleLabel);
    treasuresContentPanel.add(Box.createRigidArea(new Dimension(0, 35)));
    treasuresContentPanel.add(treasuresValueLabel);
    inventoryContentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    inventoryContentPanel.add(inventoryTitleLabel);
    inventoryContentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
    inventoryContentPanel.add(inventoryGridPanel);
    inventoryContentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
  }

  /**
   * Get the time value label.
   *
   * @return the time value JLabel
   */
  public JLabel getTimeValueLabel() {
    return timeValueLabel;
  }

  /**
   * Get the treasures value label.
   *
   * @return the treasures value JLabel
   */
  public JLabel getTreasuresValueLabel() {
    return treasuresValueLabel;
  }

  /**
   * Get the inventory value label.
   *
   * @return the inventory value JLabel
   */
  public JLabel[] getInventoryValueLabels() {
    JLabel[] copy =  inventoryValueLabels;
    return copy;
  }

  /**
   * Get the level value label.
   *
   * @return the level value JLabel
   */
  public JLabel getLevelValueLabel() {
    return levelValueLabel;
  }
}
