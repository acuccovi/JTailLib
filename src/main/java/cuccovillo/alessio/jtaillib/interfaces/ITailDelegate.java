package cuccovillo.alessio.jtaillib.interfaces;

import java.io.Serializable;

/**
 *
 * @author alessio
 */
@FunctionalInterface
public interface ITailDelegate extends Serializable {

    void charRead(char c);
}
