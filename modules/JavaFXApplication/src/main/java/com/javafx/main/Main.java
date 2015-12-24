package com.javafx.main;

// FIXME: patch because of the native JavaFX installer creates a Mac OS X bundle with the wrong main.

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(value = "NM_SAME_SIMPLE_NAME_AS_SUPERCLASS")
public class Main extends it.tidalwave.northernwind.rca.ui.impl.javafx.Main
  {
  }
