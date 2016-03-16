execute(des, src, registers, memory) {
    int QWORD = 64;
    int DQWORD = 128;

    Calculator calculator = new Calculator(registers, memory);

    int desSize = 0;
    int srcSize = 0;

    if( des.isRegister() ) {
		desSize = registers.getBitSize(des);
	}
	if( src.isRegister() ) {
		srcSize = registers.getBitSize(src);
	}

    if( des.isRegister() && checkSizeOfDestination(registers, desSize) ) {
        if( src.isRegister() ) {
            if( checkSizeOfSource(registers, srcSize) ) {
                String srcValue = registers.get(src);
                String desValue = registers.get(des);
                storeResultToRegister(registers, calculator, des, srcValue, desValue, desSize);
            }
            else {
                //throw exception
            }
        }
        else if( src.isMemory() ) {
            String srcValue = memory.read(src, QWORD);
            String desValue = registers.get(des);
            storeResultToRegister(registers, calculator, des, srcValue, desValue, desSize);
        }
    }
    else {
        //throw exception
    }
}

storeResultToRegister(registers, calculator, des, srcValue, desValue, desSize) {
    String sUpper = calculator.convertSPToDP(srcValue.substring(0,8));
    String sLower = calculator.convertSPToDP(srcValue.substring(8));

    sUpper = calculator.hexZeroExtend(sUpper, QWORD/4);
    sLower = calculator.hexZeroExtend(sLower, QWORD/4);

    registers.set(des, sUpper + sLower);
}

boolean checkSizeOfDestination(registers, desSize) {
    boolean checkSize = false;

    if( 128 == desSize ) {
        checkSize = true;
    }

    return checkSize;
}

boolean checkSizeOfSource(registers, srcSize) {
    boolean checkSize = false;

    if( 64 == srcSize || 128 == srcSize ) {
        checkSize = true;
    }

    return checkSize;
}