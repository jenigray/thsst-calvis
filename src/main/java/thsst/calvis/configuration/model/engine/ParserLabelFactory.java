package thsst.calvis.configuration.model.engine;

import thsst.calvis.configuration.model.exceptions.DuplicateLabelException;

import java.util.ArrayList;

/**
 * Created by Goodwin Chua on 16/03/2016.
 */
public class ParserLabelFactory {

    private Memory memory;
    private ArrayList<Exception> exceptions;

    public ParserLabelFactory(Memory memory, ArrayList<Exception> exceptions) {
        this.memory = memory;
        this.exceptions = exceptions;
    }

    public ArrayList<Exception> createLabel(Object[] matched) {
        int labelValue = 0;
        for ( Object obj : matched ) {

            Object[] objects = (Object[]) obj;
            for ( int i = 0; i < objects.length; i++ ) {

                if ( objects[i] != null ) {
                    if ( i == 1 && objects[i] instanceof Object[] ) {

                        Object[] objects1 = (Object[]) objects[i];
                        /**
                         * objects1[0] = contains label
                         * objects1[1] = contains the colon
                         */
                        String instructionAddress = Integer.toHexString(labelValue);
                        instructionAddress = Memory.reformatAddress(instructionAddress);
                        String label = ((Token) objects1[0]).getValue();
                        if ( !memory.containsLabel(label) ) {
                            memory.putToLabelMap(((Token) objects1[0]).getValue(), instructionAddress);
                        } else {
                            try {
                                throw new DuplicateLabelException(label);
                            } catch ( DuplicateLabelException e ) {
                                exceptions.add(e);
                            }
                        }

//								for (int j = 0; j < objects1.length; j++) {

//								}
                    }
                }
            }
            labelValue++;
        }
        return exceptions;
    }
}
