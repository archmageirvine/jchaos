package chaos.selector;


/**
 * Tests this selector.
 *
 * @author Sean A. Irvine
 */
public class RandomAiSelectorTest extends AbstractSelectorTest {


  @Override
  public Selector getSelector() {
    return new RandomAiSelector();
  }

}

