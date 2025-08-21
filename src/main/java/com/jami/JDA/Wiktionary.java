package com.jami.JDA;

import java.io.File;

import com.jami.App;

import de.tudarmstadt.ukp.jwktl.JWKTL;

public class Wiktionary {

  private static File wiktionaryDumpFile;
  private static File wiktionaryOutputDirectory;

  public static void parseWiktionary() {

    wiktionaryDumpFile = new File(App.getProps().getProperty("WIKTIONARY_XML"));
    wiktionaryOutputDirectory = new File(App.getProps().getProperty("WIKTIONARY_DIRECTORY"));
    new Thread(() -> JWKTL.parseWiktionaryDump(wiktionaryDumpFile, wiktionaryOutputDirectory, true))
        .start();
  }

  public static File getWiktionary() {
    return wiktionaryOutputDirectory;
  }
}
