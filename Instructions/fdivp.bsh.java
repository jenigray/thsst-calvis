/*
execute(src, registers, memory) {
	Calculator c = new Calculator(registers, memory);
    if ( src.isMemory() ) {
		
        int size = memory.getBitSize(src);
        String value = memory.read(src, size);
		double spValue = 0.0;
        if ( size == 32 ) {
            // conversion to extended precision
			spValue = c.convertHexToSinglePrecision(value);
			
        } else if ( size == 64 ) {
            // conversion
			spValue = c.convertHexToDoublePrecision(value);
        }
		
        String st0 = registers.get("ST0");
        double stValue = c.convertHexToDoublePrecision(st0);
		double resultingValue = stValue - spValue;
		if(resultingValue > Math.pow(2,64)){
			registers.mxscr.setOverflowFlag("1");
		}
		else if(resultingValue < Math.pow(2, 64) * -1){
			registers.mxscr.setUnderflowFlag("1");
		}
		else{
			//System.out.println(resultingValue + " value");
			registers.set("ST0", c.hexZeroExtend(c.convertDoublePrecisionToHexString(resultingValue), 20));
		}
        
		registers.x87().status().set("C3",'0');
		registers.x87().status().set("C2",'0');
		registers.x87().status().set("C0",'0');
    }
}
*/
execute(des, src, registers, memory) {
	Calculator c = new Calculator(registers, memory);
    if ( des.isRegister() && src.isRegister() ) {
        if ( registers.getBitSize(des) == registers.getBitSize(src) ) {
            String desValue = registers.get(des);
            String srcValue = registers.get(src);
			//String result = desValue;
//            if(des.getValue().equals("ST1")){
//
//			}
            if(src.getValue().equals("ST0")){
				
			}
			else{
				throw new IncorrectParameterException("FDIVP");
			}
			
			double dbDes = Double.parseDouble(desValue);
			double dbSrc = Double.parseDouble(srcValue);
			
			double resultingValue =  dbDes / dbSrc;
			boolean isException = c.generateFPUExceptions(registers, resultingValue, des.getValue());
			if(dbSrc == 0){
				c.setDivideByZeroOperation(registers, dbDes, dbSrc, des.getValue());
				isException = true;
			}
			if(!isException){
				//System.out.println(resultingValue + " value");
				registers.set(des.getValue(), "" + resultingValue);
			}
		
		registers.x87().status().set("C3",'0');
		registers.x87().status().set("C2",'0');
		registers.x87().status().set("C0",'0');
			
        }
    }
}

execute(registers, memory) {
	Calculator c = new Calculator(registers, memory);
			double dbDes = Double.parseDouble(registers.get("ST1"));
			double dbSrc = Double.parseDouble(registers.get("ST0"));
			System.out.println(dbSrc + " fucker");
			double resultingValue = dbDes / dbSrc;
			boolean isException = c.generateFPUExceptions(registers, resultingValue, "ST1");
			if(dbSrc == 0){
				c.setDivideByZeroOperation(registers, dbDes, dbSrc, "ST1");
				isException = true;
			}
			if(!isException){
				//System.out.println(resultingValue + " value");
				registers.set("ST1", "" + (resultingValue));
				registers.x87().pop();
			}
		
		registers.x87().status().set("C3",'0');
		registers.x87().status().set("C2",'0');
		registers.x87().status().set("C0",'0');

}