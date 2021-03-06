execute(des, src, registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    if ( des.isRegister() ) {
        if( src.isRegister() && src.getValue().equals("CL") ) {
            //get size of des
            int desSize = registers.getBitSize(des);
            String originalDes = calculator.hexToBinaryString(registers.get(des), des);
            String originalSign = originalDes.charAt(0) + "";

            BigInteger count = new BigInteger(registers.get(src), 16);
            int limit = count.intValue();
            if( checkSizeOfRegister(registers, desSize) && (limit >= 0 && limit <= 31) ) {
                String destination = calculator.hexToBinaryString(registers.get(des), des);

                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes.shiftLeft(count.intValue());

                String result = calculator.binaryToHexString(biResult.toString(2), des);
                if( result.length() > 8 ) {
                    int cut = result.length() - 8;
                    String t = result.substring(cut);
                    registers.set(des, t);
                }
                else {
                    registers.set(des, result);
                }

                //Flags
                String r = calculator.hexToBinaryString(registers.get(des), des);
                setFlags(calculator, flags, registers, des, limit, originalSign, desSize, originalDes, r);
            }
        }
        else if ( src.isHex() && src.getValue().length() <= 2) {
            //get size of des
            int desSize = registers.getBitSize(des);
            String originalDes = calculator.hexToBinaryString(registers.get(des), des);
            String originalSign = originalDes.charAt(0) + "";

            BigInteger count = new BigInteger(src.getValue(), 16);
            int limit = count.intValue();
            if( checkSizeOfRegister(registers, desSize) && (limit >= 0 && limit <= 31) ) {
                String destination = calculator.hexToBinaryString(registers.get(des), des);

                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes.shiftLeft(count.intValue());

                String result = calculator.binaryToHexString(biResult.toString(2), des);
                if( result.length() > 8 ) {
                    int cut = result.length() - 8;
                    String t = result.substring(cut);
                    registers.set(des, t);
                }
                else {
                    registers.set(des, result);
                }

                //Flags
                String r = calculator.hexToBinaryString(registers.get(des), des);
                setFlags(calculator, flags, registers, des, limit, originalSign, desSize, originalDes, r);
            }
        }
    }
    else if ( des.isMemory() ) {
        if( src.isRegister() && src.getValue().equals("CL") ) {
            //get size of des
            int desSize = memory.getBitSize(des);
            String originalDes = calculator.hexToBinaryString(memory.read(des, des), des);
            String originalSign = originalDes.charAt(0) + "";

            BigInteger count = new BigInteger(registers.get(src), 16);
            int limit = count.intValue();

            if( limit >= 0 && limit <= 31 ) {
                String destination = calculator.hexToBinaryString(memory.read(des, des), des);

                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes.shiftLeft(count.intValue());

                String result = calculator.binaryToHexString(biResult.toString(2), des);
                if( result.length() > 8 ) {
                    int cut = result.length() - 8;
                    String t = result.substring(cut);
                    memory.write(des, t, des);
                }
                else {
                    memory.write(des, result, des);
                }

                String r = calculator.hexToBinaryString(memory.read(des, des), des);
                BigInteger biR = new BigInteger(r, 2);

                //Flags
                String r = calculator.hexToBinaryString(memory.read(des, des), des);
                setFlags(calculator, flags, registers, des, limit, originalSign, desSize, originalDes, r);
            }
        }
        else if ( src.isHex() && src.getValue().length() <= 2 ) {
            //get size of des
            int desSize = memory.getBitSize(des);
            String originalDes = calculator.hexToBinaryString(memory.read(des, des), des);
            String originalSign = originalDes.charAt(0) + "";

            BigInteger count = new BigInteger(src.getValue(), 16);
            int limit = count.intValue();

            if( limit >= 0 && limit <= 31 ) {
                String destination = calculator.hexToBinaryString(memory.read(des, des), des);

                BigInteger biDes = new BigInteger(destination, 2);
                BigInteger biResult = biDes.shiftLeft(count.intValue());

                String result = calculator.binaryToHexString(biResult.toString(2), des);
                if( result.length() > 8 ) {
                    int cut = result.length() - 8;
                    String t = result.substring(cut);
                    memory.write(des, t, des);
                }
                else {
                    memory.write(des, result, des);
                }

                //Flags
                String r = calculator.hexToBinaryString(memory.read(des, des), des);
                setFlags(calculator, flags, registers, des, limit, originalSign, desSize, originalDes, r);
            }
        }
    }
}

setFlags(calculator, flags, registers, des, limit, originalSign, desSize, originalDes, r) {
    if ( limit == 0 ) {
        //flags not affected
    }
    else {
        BigInteger biR = new BigInteger(r, 2);

        if( biR.equals(BigInteger.ZERO) ) {
            flags.setZeroFlag("1");
        }
        else {
            flags.setZeroFlag("0");
        }

        String sign = "" + r.charAt(0);
        flags.setSignFlag(sign);

        String parity = calculator.checkParity(r);
        flags.setParityFlag(parity);

        if( limit == 1 && originalSign.equals(sign) ) {
            flags.setOverflowFlag("0");
        }
        else if( limit == 1 && !originalSign.equals(sign) ) {
            flags.setOverflowFlag("1");
        }
        else if( originalSign.equals(sign) ) {
            flags.setOverflowFlag("1");
        }
        else {
            flags.setOverflowFlag("0"); //undefined
        }

        if( limit <= desSize ) {
            flags.setCarryFlag(originalDes.charAt(limit - 1).toString());
        }
        else {
            flags.setCarryFlag("0");
        }

        flags.setAuxiliaryFlag("0"); //undefined
    }
}

boolean checkSizeOfRegister(registers, desSize) {
    boolean checkSize = false;
    for(int a : registers.getAvailableSizes()) {
        if( a == desSize ) {
            checkSize = true;
        }
    }

    return checkSize;
}
