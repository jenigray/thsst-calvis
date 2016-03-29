execute(s,t,registers,memory) {
  int DWORD = 32;
  Calculator calculator = new Calculator(registers,memory);
  EFlags eFlags = registers.getEFlags();
  String sValue = registers.get(s);
  String tValue;
  if(t.isRegister()) {
    tValue = registers.get(t);
    tValue = calculator.cutToCertainHexSize("getLower",tValue,DWORD/4);
  } else if(t.isMemory()) {
    tValue = memory.read(t,DWORD);
  }

  // Get the sValue from 0 to 31 bit
  sValue = calculator.cutToCertainHexSize("getLower",sValue,DWORD/4);

  double doubleS = calculator.hexToDoublePrecisionFloatingPoint(sValue);
  double doubleT = calculator.hexToDoublePrecisionFloatingPoint(tValue);

  int retval = Double.compare(doubleS, doubleT);
  if(Double.isNaN(doubleS) || Double.isNaN(doubleT)) {
    System.out.println("isNaN");
    eFlags.setZeroFlag("1");
    eFlags.setParityFlag("1");
    eFlags.setCarryFlag("1");
    // S > T
  } else if(retval > 0) {
    System.out.println("isGreater");
    eFlags.setZeroFlag("0");
    eFlags.setParityFlag("0");
    eFlags.setCarryFlag("0");
    // S < T
  } else if(retval < 0) {
    System.out.println("isLess");
    eFlags.setZeroFlag("0");
    eFlags.setParityFlag("0");
    eFlags.setCarryFlag("1");
    // S == T
  } else if(retval == 0) {
    System.out.println("isEquals");
    eFlags.setZeroFlag("1");
    eFlags.setParityFlag("0");
    eFlags.setCarryFlag("0");
  }

  eFlags.setOverflowFlag("0");
  eFlags.setAuxiliaryFlag("0");
  eFlags.setSignFlag("0");
}
