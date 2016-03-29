execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);

    int desSize = 0;
    int srcSize = 0;

    if( des.isRegister() ) {
		desSize = registers.getBitSize(des);
	}

	if( src.isRegister() ) {
		srcSize = registers.getBitSize(src);
	} else if( src.isMemory() ) {
        srcSize = memory.getBitSize(src);
    }

    if( des.isRegister() && checkSizeOfDestination(registers, desSize) ) {
        if( src.isRegister() ) {
            if( checkSizeOfSource(registers, srcSize) ) {
                String source = registers.get(src);
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, srcSize);
            } else {
                //throw exception
            }
        } else if( src.isMemory() ) {
            String source = memory.read(src, src);
            String destination = registers.get(des);
            storeResultToRegister(registers, calculator, des, source, destination, srcSize);
        }
    } else {
        //throw exception
    }
}

storeResultToRegister(registers, calculator, des, source, destination, srcSize) {
    float fLower = 0;
    String sLower = "";

    if( srcSize == 32 ) {
        BigInteger bLower = new BigInteger(source);

        long lLower = calculator.convertToSignedInteger(bLower, 32);

        String sLower = calculator.toHexSinglePrecisionString(lLower);
    } else if( srcSize == 64 ) {
        BigInteger bLower = new BigInteger(source.substring(8));

        long lLower = calculator.convertToSignedInteger(bLower, 32);

        String sLower = calculator.toHexSinglePrecisionString(lLower);
    }

    registers.set(des, sLower);
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

    if( 64 == srcSize || 32 == srcSize ) {
        checkSize = true;
    }

    return checkSize;
}
