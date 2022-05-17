/*
 * Copyright (c) 2018. Real Time Genomics Limited.
 *
 * Use of this source code is bound by the Real Time Genomics Limited Software Licence Agreement
 * for Academic Non-commercial Research Purposes only.
 *
 * If you did not receive a license accompanying this file, a copy must first be obtained by email
 * from support@realtimegenomics.com.  On downloading, using and/or continuing to use this source
 * code you accept the terms of that license agreement and any amendments to those terms that may
 * be made from time to time by Real Time Genomics Limited.
 */
package chaos.setup;

import javax.swing.JCheckBox;

import chaos.util.ChaosProperties;

/**
 * @author Sean A. Irvine
 */
enum CheckboxOption {
  FSEM("Full screen exclusive mode", ChaosProperties.FSEM_PROPERTY, "If possible open the game full-screen with exclusive access."),
  TEXAS("Texas trade'em", ChaosProperties.TEXAS_PROPERTY, "If selected, then a spell must be discarded for each spell selected."),
  COMBAT_CAP("Combat cap", ChaosProperties.COMBAT_CAP_PROPERTY, "If selected, combat is capped at 15 rather than 100."),
  LIFE_LEECH("Life leech", ChaosProperties.LIFE_LEECH_PROPERTY, "If selected, all wizards start with a life leech."),
  RANDOM_PLAY_ORDER("Randomize play order", ChaosProperties.RANDOMIZE_PLAY_ORDER_PROPERTY, "If selected, the order of wizards is shuffled at the start of the game.");

  private final String mLabel;
  private final String mProperty;
  private final String mTooltip;

  CheckboxOption(final String label, final String property, final String tooltip) {
    mLabel = label;
    mProperty = property;
    mTooltip = tooltip;
  }

  String getLabel() {
    return mLabel;
  }

  String getProperty() {
    return mProperty;
  }

  String getTooltip() {
    return mTooltip;
  }

  JCheckBox getCheckBox() {
    final JCheckBox input = new JCheckBox(getLabel(), ChaosProperties.properties().getBooleanProperty(getProperty(), false));
    input.setToolTipText(getTooltip());
    return input;
  }

}
