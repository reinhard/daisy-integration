package com.indoqa.daisy.pipeline;

/**
 * @version $Id: DaisyGenerator.java 1572 2009-02-11 11:42:31Z rpoetz $
 */
public class DaisyGenerator extends AbstractDaisyProducer {

    @Override
    public void execute() {
        this.getDaisyDocument().toSax(this.getSAXConsumer());
    }
}
