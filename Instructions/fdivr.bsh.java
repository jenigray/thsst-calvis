execute(src, registers, memory) {
    Calculator c = new Calculator(registers, memory);
    if ( src.isMemory() ) {
        int size = memory.getBitSize(src);
        String value = memory.read(src, size);
        double spValue = 0.0;
        spValue = c.switchPrecisionValue(size, value);
        String st0 = registers.get("ST0");
        double stValue = Double.parseDouble(st0);
        double resultingValue =  spValue / stValue;
        boolean isException = c.generateFPUExceptions(registers, resultingValue, "ST0");
        if(stValue == 0) {
            c.setDivideByZeroOperation(registers, spValue, stValue, "ST0");
            isException = true;
        }
        if(!isException) {
            registers.set("ST0", "" + resultingValue);
        }

        registers.x87().status().set("C3",'0');
        registers.x87().status().set("C2",'0');
        registers.x87().status().set("C0",'0');
    }
}

execute(des, src, registers, memory) {
    Calculator c = new Calculator(registers, memory);
    if ( des.isRegister() && src.isRegister() ) {
        if ( registers.getBitSize(des) == registers.getBitSize(src) ) {
            String desValue = registers.get(des);
            String srcValue = registers.get(src);

            if(des.getValue().equals("ST0")) {

            }
            else if(src.getValue().equals("ST0")) {

            }
            else{
                throw new IncorrectParameterException("FDIVR");
            }

            double dbDes = Double.parseDouble(desValue);
            double dbSrc = Double.parseDouble(srcValue);

            double resultingValue = dbSrc / dbDes;
            boolean isException = c.generateFPUExceptions(registers, resultingValue, des.getValue());
            if(dbDes == 0) {
                c.setDivideByZeroOperation(registers, dbDes, dbSrc, des.getValue());
                isException = true;
            }
            if(!isException) {
                registers.set(des.getValue(), "" + (resultingValue));
            }

            registers.x87().status().set("C3",'0');
            registers.x87().status().set("C2",'0');
            registers.x87().status().set("C0",'0');

        }
    }
}
