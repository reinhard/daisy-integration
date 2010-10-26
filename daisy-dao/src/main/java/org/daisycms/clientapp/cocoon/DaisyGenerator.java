package org.daisycms.clientapp.cocoon;

/**
 * @version $Id: DaisyGenerator.java 1572 2009-02-11 11:42:31Z rpoetz $
 */
public class DaisyGenerator extends AbstractDaisyProducer {

    public void execute() {
        this.getDaisyDocument().toSax(this.getSAXConsumer());
    }
}
