package chaos.graphics.active64;

import chaos.graphics.AbstractGenerateGraphics;

/**
 * Algorithmically generate images
 * @author Sean A. Irvine
 */
public class GenerateGraphics extends AbstractGenerateGraphics {

  @Override
  protected int getWidthBits() {
    return 6;
  }

  /**
   * Algorithmically produce some tiles.
   * @param args ignored
   * @throws Exception if an error occurs
   */
  public static void main(final String[] args) throws Exception {
    new GenerateGraphics().generate();
  }
}

