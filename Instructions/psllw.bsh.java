execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);

    int desSize = 0;

    if( des.isRegister() ) {
		desSize = registers.getBitSize(des);
	}

    if( des.isRegister() ) {
        if( src.isRegister() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                BigInteger count = new BigInteger(registers.get(src), 16);
                int source = count.intValue();
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            }
            else {
                //throw exception
            }
        }
        else if( src.isMemory() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                BigInteger count = new BigInteger(memory.read(src, desSize), 16);
                int source = count.intValue();
                String destination = registers.get(des);
                storeResultToRegister(registers, calculator, des, source, destination, desSize);
            }
            else {
                //throw exception
            }
        }
        else if( src.isHex() ) {
            if( checkSizeOfRegister(registers, desSize) ) {
                BigInteger count = new BigInteger(src.getValue(), 16);
                int source = count.intValue();
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

    if( source >= 0 && source <= 15 ) {
        Token token = new Token(Token.REG, "AX");
        String temp = "";
        String binaryDes = "";
        BigInteger biDes;
        BigInteger biResult;

        if( desSize == 64 ) {
            for(int i = 0; i <= 12; i+=4) {
                if( i == 12 ) {
                    binaryDes = calculator.hexToBinaryString(destination.substring(i), token);
                    biDes = new BigInteger(binaryDes, 2);
                    biResult = biDes.shiftLeft(source);
                    temp = calculator.binaryToHexString(biResult.toString(2), token);
                    result += temp;
                }
                else {
                    binaryDes = calculator.hexToBinaryString(destination.substring(i, i + 4), token);
                    biDes = new BigInteger(binaryDes, 2);
                    biResult = biDes.shiftLeft(source);
                    temp = calculator.binaryToHexString(biResult.toString(2), token);
                    result += temp;
                }
            }
        }
        else if( desSize == 128 ) {
            String binaryDes = "";
            BigInteger biDes;
            BigInteger biResult;

            for(int i = 0; i <= 28; i+=4) {
                if( i == 28 ) {
                    binaryDes = calculator.hexToBinaryString(destination.substring(i), token);
                    biDes = new BigInteger(binaryDes, 2);
                    biResult = biDes.shiftLeft(source);
                    temp = calculator.binaryToHexString(biResult.toString(2), token);
                    result += temp;
                }
                else {
                    binaryDes = calculator.hexToBinaryString(destination.substring(i, i + 4), token);
                    biDes = new BigInteger(binaryDes, 2);
                    biResult = biDes.shiftLeft(source);
                    temp = calculator.binaryToHexString(biResult.toString(2), token);
                    result += temp;
                }
            }
        }
    }
    else {
        result = calculator.hexZeroExtend("0", des);
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
