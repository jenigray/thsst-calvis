execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);

    int desSize = 0;
    int srcSize = 0;

    if( des.isRegister() ) {
        desSize = registers.getBitSize(des);
    }

    if( src.isRegister() ) {
        srcSize = registers.getBitSize(src);
    } else {
        srcSize = memory.getBitSize(src);
    }

    if( des.isRegister() ) {
        if( src.isRegister() ) {
            if( (desSize == srcSize) && checkSizeOfRegister(registers, desSize) ) {
                String source = registers.get(src);
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            }
        } else if( src.isMemory() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                String source = memory.read(src, desSize);
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            }
        }
    }
}

storeResultToRegister(registers, calculator, des, source, destination, desSize) {
    String result = "";
    int limit = 0;

    if( desSize == 64 ) {
        limit = 4;
    } else if( desSize == 128 ) {
        limit = 12;
    }

    for(int i = 0; i <= limit; i += 4) {
        for(int j = 0; j < 4; j++) {
            result += source.charAt(i + j) + "";
        }

        for(int j = 0; j < 4; j++) {
            result += destination.charAt(i + j) + "";
        }
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
