execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);

    int desSize = 0;
    int srcSize = 0;

    if( des.isRegister() ) {
		desSize = registers.getBitSize(des);
	}
	if( src.isRegister() ) {
		srcSize = registers.getBitSize(src);
	}
	else {
		srcSize = memory.getBitSize(src);
	}

    if( des.isRegister() ) {
        if( des.isRegister() ) {
            if( (desSize == srcSize) && checkSizeOfRegister(registers, desSize) ) {
                String source = registers.get(src);
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            }
            else {
                //throw exception
            }
        }
        else if( src.isMemory() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                String source = memory.read(src, desSize);
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            }
            else {
                //throw exception
            }
        }
    }
    else {
        //throw exception
    }
}

storeResultToRegister(registers, calculator, des, source, destination, desSize) {
    String result = "";

    for(int j = 8; j < 16; j++) {
        result += source.charAt(j) + "";
    }

    for(int j = 8; j < 16; j++) {
        result += destination.charAt(j) + "";
    }

    registers.set(des, result);
}

boolean checkSizeOfRegister(registers, desSize) {
    boolean checkSize = false;

    if( 128 == desSize || 64 == desSize ) {
        checkSize = true;
    }

    return checkSize;
}
