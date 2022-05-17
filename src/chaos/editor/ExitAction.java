package chaos.editor;

import java.awt.event.ActionEvent;
import java.util.concurrent.CountDownLatch;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

/**
 * Action for exiting the editor.
 * @author Sean A. Irvine
 */
public class ExitAction extends AbstractAction {

  private final JFrame mFrame;
  private final CountDownLatch mLock;

  ExitAction(final JFrame frame, final CountDownLatch lock) {
    super("Exit", null);
    mFrame = frame;
    mLock = lock;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    mFrame.setVisible(false);
    mFrame.dispose();
    mLock.countDown();
  }
}
