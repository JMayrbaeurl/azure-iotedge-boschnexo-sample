package com.microsoft.samples.nexo.edgemodule.methods;

/**
 * SelectProgramMessage
 */
public class SelectProgramMessage {

    private int programnumber;

    public int getProgramnumber() {
        return programnumber;
    }

    public void setProgramnumber(int programnumber) {
        this.programnumber = programnumber;
    }

    @Override
    public String toString() {
        return "SelectProgramMessage [programnumber=" + programnumber + "]";
    }
}