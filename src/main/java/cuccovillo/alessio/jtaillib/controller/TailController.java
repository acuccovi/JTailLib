package cuccovillo.alessio.jtaillib.controller;

import cuccovillo.alessio.jtaillib.Settings;
import cuccovillo.alessio.jtaillib.interfaces.ITailDelegate;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alessio
 */
public class TailController implements Serializable {

    static final long serialVersionUID = 1L;
    private final Settings settings;
    private final ITailDelegate delegate;
    private boolean pause;

    public TailController(Settings settings, ITailDelegate delegate) {
        this.settings = settings;
        this.delegate = delegate;
    }

    public void startTail() {
        new Thread(() -> {
            pause = false;
            try (FileReader fr = new FileReader(settings.getFile());
                    BufferedReader br = new BufferedReader(fr)) {
                if (settings.isVerbose()) {
                    String header = String.format("==> %s <==%n", settings.getFile().toString());
                    for (char c : header.toCharArray()) {
                        delegate.charRead(c);
                    }
                }
                List<String> linesRead = new ArrayList<>();
                String line;
                while (settings.getLines() > 0 && (line = br.readLine()) != null) {
                    linesRead.add(line);
                }
                if (linesRead.size() > settings.getLines()) {
                    int start = linesRead.size() - settings.getLines();
                    linesRead = linesRead.subList(start, linesRead.size());
                }
                linesRead.forEach((l) -> {
                    for (char c : l.toCharArray()) {
                        delegate.charRead(c);
                    }
                    delegate.charRead('\n');
                });
                linesRead.clear();
                int c;
                while (settings.isFollow()) {
                    synchronized (this) {
                        if (pause) {
                            try {
                                wait();
                            } catch (InterruptedException ignore) {
                            }
                        }
                        if ((c = br.read()) != -1) {
                            delegate.charRead((char) c);
                            continue;
                        }
                        sleepLocal(settings.getSleep());
                    }
                }
            } catch (FileNotFoundException fnfe) {
                System.err.println("jtail: " + fnfe.getMessage());
                System.exit(-2);
            } catch (IOException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                System.err.println("jtail:");
                System.err.println(sw.toString());
            }
            System.out.println();
        }).start();
    }

    public void pauseTail() {
        pause = true;
    }

    public void resumeTail() {
        synchronized (this) {
            notifyAll();
            pause = false;
        }
    }

    public boolean isPaused() {
        return pause;
    }

    //Just because NetBeans complain if Thread.sleep() is called within a loop
    private void sleepLocal(Long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ignore) {
        }
    }

}
