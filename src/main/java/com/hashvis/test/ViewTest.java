package com.hashvis.test;

import com.hashvis.model.collision.*;
import com.hashvis.model.collision.CollisionResolver.HashAction;
import com.hashvis.model.hashfunc.HashFunction;
import com.hashvis.model.collision.CollisionResolver.DataType;
import com.hashvis.model.table.*;
import com.hashvis.view.table.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewTest {

  // 1. DATA STRUCTURES FOR EASY TESTING
  record ActionRequest(HashAction action, String key) {
  }

  record Scenario(
      String name,
      CollisionResolver resolver,
      int tableSize,
      List<ActionRequest> actions) {
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      // 2. DEFINE YOUR SCENARIOS HERE (This is the only part you need to modify to
      // test new things!)
      List<Scenario> testSuite = new ArrayList<>();

      testSuite.add(new Scenario("Linear Probing: Collision & Search",
          new LinearProbing(), 5,
          List.of(
              new ActionRequest(HashAction.INSERT, "10"),
              new ActionRequest(HashAction.INSERT, "15"), // Should collide
              new ActionRequest(HashAction.SEARCH, "15"),
              new ActionRequest(HashAction.DELETE, "10"))));

      testSuite.add(new Scenario("Quadratic Probing: Probing Sequence",
          new QuadraticProbing(), 10,
          List.of(
              new ActionRequest(HashAction.INSERT, "5"),
              new ActionRequest(HashAction.INSERT, "15"),
              new ActionRequest(HashAction.INSERT, "25"))));

      testSuite.add(new Scenario("Separate Chaining: Chain Growth",
          new SeparateChaining(), 3,
          List.of(
              new ActionRequest(HashAction.INSERT, "A"),
              new ActionRequest(HashAction.INSERT, "B"), // Assume collision
              new ActionRequest(HashAction.INSERT, "C"),
              new ActionRequest(HashAction.SEARCH, "B"))));

      testSuite.add(new Scenario("Double Hashing: Complex Probing",
          new DoubleHashing(), 7,
          List.of(
              new ActionRequest(HashAction.INSERT, "22"),
              new ActionRequest(HashAction.INSERT, "29"),
              new ActionRequest(HashAction.DELETE, "22"))));

      // 3. START THE TEST RUNNER
      new ScenarioRunner(testSuite).start();
    });
  }

  /**
   * THE CONTROLLER: This class manages the timing and the flow of the scenarios.
   * It mimics the "loop" in your ModelTest but uses a Timer to keep the UI alive.
   */
  static class ScenarioRunner {
    private final List<Scenario> scenarios;
    private int currentScenarioIdx = 0;
    private int currentActionIdx = 0;

    private JFrame frame;
    private TableView tableView;
    private JLabel statusLabel;
    private JLabel stepLabel;

    private Table currentTable;
    private CollisionResolver currentResolver;
    private Timer animationTimer;

    public ScenarioRunner(List<Scenario> scenarios) {
      this.scenarios = scenarios;
    }

    public void start() {
      setupUI();
      runScenario(scenarios.get(0));
    }

    private void setupUI() {
      frame = new JFrame("HashVis Scenario Runner");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(800, 600);
      frame.setLayout(new BorderLayout());

      // Top Panel: Status and Progress
      JPanel topPanel = new JPanel(new GridLayout(2, 1));
      statusLabel = new JLabel("Scenario: Loading...", SwingConstants.CENTER);
      statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
      stepLabel = new JLabel("Action: Waiting...", SwingConstants.CENTER);
      topPanel.add(statusLabel);
      topPanel.add(stepLabel);
      frame.add(topPanel, BorderLayout.NORTH);

      // Center Panel: The Table
      tableView = new TableView(new Table(1), false); // Placeholder table
      frame.add(new JScrollPane(tableView), BorderLayout.CENTER);

      // Bottom Panel: Controls
      JPanel bottomPanel = new JPanel();
      JButton nextBtn = new JButton("Skip Step");
      JButton playBtn = new JButton("Play Scenario");

      nextBtn.addActionListener(e -> manualStep());
      playBtn.addActionListener(e -> {
        if (animationTimer.isRunning())
          animationTimer.stop();
        else
          animationTimer.start();
      });

      bottomPanel.add(playBtn);
      bottomPanel.add(nextBtn);
      frame.add(bottomPanel, BorderLayout.SOUTH);

      frame.setVisible(true);

      // The Engine: This timer drives the "nextStep()" calls
      animationTimer = new Timer(800, e -> autoStep());
    }

    private void runScenario(Scenario s) {
      // frame.removeAll();
      currentScenarioIdx = scenarios.indexOf(s);
      currentActionIdx = 0;
      currentTable = new Table(s.tableSize());
      currentResolver = s.resolver();

      // Initialize hash functions for the resolver
      currentResolver.getHashFunctionFields(DataType.STRING);

      statusLabel.setText("Scenario: " + s.name());
      frame.getContentPane().remove(tableView);
      tableView = new TableView(currentTable, s.resolver() instanceof SeparateChaining);
      // Update the frame's center component
      frame.add(tableView, BorderLayout.CENTER);
      frame.revalidate();
      frame.repaint();

      executeNextAction();
    }

    private void executeNextAction() {
      Scenario s = scenarios.get(currentScenarioIdx);
      if (currentActionIdx >= s.actions().size()) {
        // Scenario finished!
        animationTimer.stop();
        JOptionPane.showMessageDialog(frame, "Scenario Complete!");
        nextScenario();
        return;
      }

      ActionRequest req = s.actions().get(currentActionIdx);
      stepLabel.setText("Action: " + req.action() + " | Key: " + req.key());

      for (HashFunction func : currentResolver.getHashFunctionFields(DataType.STRING)) {
        // func.update("len(s) % n");
      }

      // Initialize the resolver with the new action
      currentResolver.getAlgorithmAndInitalize(req.action(), req.key(), currentTable);

      // Prepare the View to show the new table
      // In a real MVC, the Controller would update the table, and the View would
      // react.
      // Here we re-init the view for simplicity in the test harness.
      updateView();
    }

    private void autoStep() {
      Scenario s = scenarios.get(currentScenarioIdx);
      ActionRequest req = s.actions().get(currentActionIdx);

      CollisionResolver.Result result = currentResolver.nextStep();

      // Update UI with the message from the Model
      stepLabel.setText(String.format("[%s] %s", req.action(), result.message()));

      if (result.currentLine() == -1) {
        // Action finished
        currentActionIdx++;
        executeNextAction();
      }
    }

    private void manualStep() {
      if (animationTimer.isRunning())
        animationTimer.stop();
      else
        animationTimer.start();
    }

    private void nextScenario() {
      currentScenarioIdx++;
      if (currentScenarioIdx < scenarios.size()) {
        runScenario(scenarios.get(currentScenarioIdx));
      } else {
        System.exit(0);
      }
    }

    private void updateView() {
      // This is a hack to force the TableView to refresh with the new table object
      // In your final project, the Controller will just update the existing table.
      // try {
      // java.lang.reflect.Field field = TableView.class.getDeclaredField("content");
      // field.setAccessible(true);
      // JPanel content = (JPanel) field.get(tableView);
      // content.removeAll();
      //
      // // Re-add all rows
      // for (int i = 0; i < currentTable.size(); i++) {
      // content.add(new RowView(currentTable.getRow(i)));
      // }
      //
      // content.revalidate();
      // content.repaint();
      // } catch (Exception e) {
      // e.printStackTrace();
      // }

    }
  }
}
