package chaos.setup;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import chaos.util.BooleanLock;
import chaos.util.ChaosProperties;

/**
 * Configure some Chaos game options.
 * @author Sean A. Irvine
 */
public final class Configure extends JFrame implements ActionListener {

  private final BooleanLock mInitLock = new BooleanLock();
  private final Map<CheckboxOption, JCheckBox> mCheckBoxes = new EnumMap<>(CheckboxOption.class);

  @Override
  public void actionPerformed(final ActionEvent event) {
    for (final Map.Entry<CheckboxOption, JCheckBox> e : mCheckBoxes.entrySet()) {
      final String prop = e.getKey().getProperty();
      final ChaosProperties properties = ChaosProperties.properties();
      properties.setProperty(prop, e.getValue().isSelected() ? "true" : "false");
      setVisible(false);
      dispose();
      properties.save();
      mInitLock.setValue(true);
    }
  }

  private JButton startButton() {
    final JButton startButton = new JButton("Start");
    startButton.addActionListener(this);
    return startButton;
  }

  /**
   * Chaos set up configuration options.
   */
  public Configure() {
    super("Java Chaos Options");
    setFocusable(true);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    final Container pane = getContentPane();
    pane.setLayout(new GridBagLayout());
    final GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridwidth = 1;
    int yPos = 0;
    for (final CheckboxOption opt : CheckboxOption.values()) {
      c.weightx = 1;
      c.ipadx = 20;
      c.gridx = 0;
      c.gridy = yPos++;
      final JCheckBox input = opt.getCheckBox();
      mCheckBoxes.put(opt, input);
      pane.add(input, c);
    }
    final JButton start = startButton();
    c.gridx = 0;
    c.gridy = yPos;
    pane.add(start, c);
    pack();
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          actionPerformed(null);
        }
      }
    });
  }

  /**
   * Produce a simple dialog allowing the user to select some game options.  The options
   * are reflected into the chaos properties structure.
   * @return true if configuration completed normally
   */
  public static boolean configure() {
    final Configure conf = new Configure();
    javax.swing.SwingUtilities.invokeLater(() -> conf.setVisible(true));
    try {
      conf.mInitLock.waitUntilTrue(100000);
    } catch (final InterruptedException e) {
      return false;
    }
    return true;
  }

  /**
   * Noddy, can be used to set configuration without actually starting a game.
   * @param args ignored
   */
  public static void main(final String[] args) {
    configure();
  }
}
