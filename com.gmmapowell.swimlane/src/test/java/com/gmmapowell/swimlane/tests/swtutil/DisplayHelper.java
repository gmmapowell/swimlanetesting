package com.gmmapowell.swimlane.tests.swtutil;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class DisplayHelper implements TestRule {

  private final Collection<Shell> capturedShells;

  private boolean displayOwner;
  private Display display;

  public DisplayHelper() {
    capturedShells = new ArrayList<>();
    capturedShells.addAll( asList( captureShells() ) );
  }

  public Display getDisplay() {
    if( display == null ) {
      displayOwner = Display.getCurrent() == null;
      display = Display.getDefault();
    }
    return display;
  }

  public Shell[] getNewShells() {
    Collection<Shell> newShells = new ArrayList<>();
    Shell[] shells = captureShells();
    for( Shell shell : shells ) {
      if( !capturedShells.contains( shell ) ) {
        newShells.add( shell );
      }
    }
    Shell[] ret = new Shell[newShells.size()];
    return newShells.toArray(ret);
  }

  public Shell createShell() {
    return createShell( SWT.NONE );
  }

  public Shell createShell( int style ) {
    return new Shell( getDisplay(), style );
  }

  public void ensureDisplay() {
    getDisplay();
  }

  public void flushPendingEvents() {
    while(    Display.getCurrent() != null
           && !Display.getCurrent().isDisposed()
           && Display.getCurrent().readAndDispatch() ) {}
  }

  public void dispose() {
    flushPendingEvents();
    disposeNewShells();
    disposeDisplay();
  }

  public Statement apply( final Statement base, Description description ) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        try {
          base.evaluate();
        } finally {
          dispose();
        }
      }
    };
  }

  private void disposeNewShells() {
    Shell[] newShells = getNewShells();
    for( Shell shell : newShells ) {
      shell.dispose();
    }
  }

  private static Shell[] captureShells() {
    Shell[] result = new Shell[ 0 ];
    Display currentDisplay = Display.getCurrent();
    if( currentDisplay != null ) {
      result = currentDisplay.getShells();
    }
    return result;
  }

  private void disposeDisplay() {
    if( display != null && displayOwner ) {
      if( display.isDisposed() ) {
        display.dispose();
      }
      display = null;
    }
  }
}