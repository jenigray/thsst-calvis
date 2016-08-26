execute(cc, des, registers, memory){
    Calculator cal = new Calculator(registers, memory);
    boolean initialCondition = cal.evaluateCondition(cc.getValue());

    /**
     * If condition is true, carry out the move
     */
    if ( initialCondition ) {
        if ( des.isRegister() ) {
            registers.set(des, "1");
        }
        else if ( des.isMemory() ) {
            memory.write(des, "1", des);
        }
    }
    else {
        if ( des.isRegister() ) {
            registers.set(des, "0");
        }
        else if ( des.isMemory() ) {
            memory.write(des, "0", des);
        }
    }
}
