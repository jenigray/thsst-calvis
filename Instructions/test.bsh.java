 execute(des, src, registers, memory) {
 	Calculator calculator = new Calculator(registers, memory);

 	if ( des.isRegister() ) {
 		if ( src.isRegister() ) {
      System.out.println("TEST register and register");

      //get size of des, src
      int desSize = registers.getBitSize(des);
      int srcSize = registers.getBitSize(src);

      //check if des size is 8-, 16-, 32-bit
      boolean checkSize = false;
      for(int a : registers.getAvailableSizes()) {
        if(a == desSize) {
          checkSize = true;
        }
      }

      if( (desSize == srcSize) && checkSize ) {
        //get hex value of des, src then convert to binary
        String source = calculator.hexToBinaryString(registers.get(src), src);
        String destination = calculator.hexToBinaryString(registers.get(des), des);

        BigInteger biSrc = new BigInteger(source, 2);
        BigInteger biDes = new BigInteger(destination, 2);
        BigInteger biResult = biDes.and(biSrc);

        String result = calculator.binaryToHexString(biResult.toString(2), des);
        // registers.set(des, result);

        //FLAGS
        EFlags flags = registers.getEFlags();

        flags.setCarryFlag("0");
        flags.setOverflowFlag("0");

        BigInteger bi = new BigInteger(result, 16);
        if(bi.equals(BigInteger.ZERO)) {
          flags.setZeroFlag("1");
        }
        else {
          flags.setZeroFlag("0");
        }

        String r = calculator.hexToBinaryString(result, des);
        String sign = "" + r.charAt(0);
        flags.setSignFlag(sign);

        String parity = calculator.checkParity(r);
        flags.setParityFlag(parity);

        flags.setAuxiliaryFlag("0"); //undefined
      }
    }
    else if ( src.isHex() ) {
      System.out.println("TEST register and immediate");

      //get size of des, src
      int desSize = registers.getBitSize(des);
      int srcSize = src.getValue().length();

      //check if des size is 8-, 16-, 32-bit
      boolean checkSize = false;
      for(int a : registers.getAvailableSizes()) {
        if(a == desSize) {
          checkSize = true;
        }
      }

      if( (desSize >= srcSize) && checkSize ) {
        //get hex value of des, src then convert to binary
        String source = calculator.hexToBinaryString(src.getValue(), des);
        String destination = calculator.hexToBinaryString(registers.get(des), des);

        BigInteger biSrc = new BigInteger(source, 2);
        BigInteger biDes = new BigInteger(destination, 2);
        BigInteger biResult = biDes.and(biSrc);

        String result = calculator.binaryToHexString(biResult.toString(2), des);
        // registers.set(des, result);

        //FLAGS
        EFlags flags = registers.getEFlags();

        flags.setCarryFlag("0");
        flags.setOverflowFlag("0");

        BigInteger bi = new BigInteger(result, 16);
        if(bi.equals(BigInteger.ZERO)) {
          flags.setZeroFlag("1");
        }
        else {
          flags.setZeroFlag("0");
        }

        String r = calculator.hexToBinaryString(result, des);
        String sign = "" + r.charAt(0);
        flags.setSignFlag(sign);

        String parity = calculator.checkParity(r);
        flags.setParityFlag(parity);

        flags.setAuxiliaryFlag("0"); //undefined
      }
    }
 	}
 	else if ( des.isMemory() ) {
    if ( src.isRegister() ) {
 			System.out.println("TEST memory and register");

      //get size of des, src
      int srcSize = registers.getBitSize(src);

      String source = calculator.hexToBinaryString(registers.get(src), src);
      String d = memory.read(des, srcSize);
      String destination = calculator.hexToBinaryString(d, src);

      BigInteger biSrc = new BigInteger(source, 2);
      BigInteger biDes = new BigInteger(destination, 2);
      BigInteger biResult = biDes.and(biSrc);

      String result = calculator.binaryToHexString(biResult.toString(2), src);
      // memory.write(des, result, srcSize);

      //FLAGS
      EFlags flags = registers.getEFlags();

      flags.setCarryFlag("0");
      flags.setOverflowFlag("0");

      BigInteger bi = new BigInteger(result, 16);
      if(bi.equals(BigInteger.ZERO)) {
        flags.setZeroFlag("1");
      }
      else {
        flags.setZeroFlag("0");
      }

      String r = calculator.hexToBinaryString(result, src);
      String sign = "" + r.charAt(0);
      flags.setSignFlag(sign);

      String parity = calculator.checkParity(r);
      flags.setParityFlag(parity);

      flags.setAuxiliaryFlag("0"); //undefined
    }
    else if ( src.isHex() ) {
  		System.out.println("TEST memory and immediate");

      String source = calculator.hexToBinaryString(src.getValue(), des);
      String d = memory.read(des, des);
      String destination = calculator.hexToBinaryString(d, des);

      BigInteger biSrc = new BigInteger(source, 2);
      BigInteger biDes = new BigInteger(destination, 2);
      BigInteger biResult = biDes.and(biSrc);

      String result = calculator.binaryToHexString(biResult.toString(2), des);
      // memory.write(des, result, des);

      //FLAGS
      EFlags flags = registers.getEFlags();

      flags.setCarryFlag("0");
      flags.setOverflowFlag("0");

      BigInteger bi = new BigInteger(result, 16);
      if(bi.equals(BigInteger.ZERO)) {
        flags.setZeroFlag("1");
      }
      else {
        flags.setZeroFlag("0");
      }

      String r = calculator.hexToBinaryString(result, des);
      String sign = "" + r.charAt(0);
      flags.setSignFlag(sign);

      String parity = calculator.checkParity(r);
      flags.setParityFlag(parity);

      flags.setAuxiliaryFlag("0"); //undefined
    }
  }
 }
