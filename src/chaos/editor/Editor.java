package chaos.editor;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Editor for Chaos.
 * @author Sean A. Irvine
 */
public final class Editor {

  private final JFrame mFrame;
  private final CountDownLatch mLock;
  private final ExitAction mExitAction;

  private Editor() {
    mFrame = new JFrame();
    mFrame.setTitle("Domination Editor");
    mFrame.setLayout(new BorderLayout());
    mFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    mLock = new CountDownLatch(1);
    mExitAction = new ExitAction(mFrame, mLock);
    mFrame.setJMenuBar(initMenus());
  }

  private JMenu initFileMenu() {
    final JMenu menu = new JMenu("File");
    menu.setMnemonic(KeyEvent.VK_F);
    final JMenuItem exit = new JMenuItem("Exit");
    exit.setMnemonic(KeyEvent.VK_E);
    exit.addActionListener(mExitAction);
    menu.add(exit);
    return menu;
  }

  private JMenuBar initMenus() {
    final JMenuBar menus = new JMenuBar();
    menus.add(initFileMenu());
    return menus;
  }

  private void run() throws InvocationTargetException, InterruptedException {
    SwingUtilities.invokeAndWait(() -> {
      mFrame.pack();
      mFrame.setSize(1024, 768);
      mFrame.setLocation(50, 50);
      mFrame.setVisible(true);
    });
    mLock.await();
  }

  /**
   * Start the editor.
   * @param args currently ignored
   * @throws InvocationTargetException if the GUI cannot be displayed
   * @throws InterruptedException if execution is interrupted
   */
  public static void main(final String[] args) throws InvocationTargetException, InterruptedException {
    final Editor editor = new Editor();
    editor.run();
  }
}
