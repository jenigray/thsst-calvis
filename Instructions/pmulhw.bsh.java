execute(des, src, registers, memory) {
	int desSize = 0;
	int srcSize = 0;
	int sizeOfHex = 4;
	Calculator c = new Calculator(registers, memory);
	if(des.isRegister()){
		desSize = registers.getBitSize(des);
	}
	else{
		desSize = memory.getBitSize(des);
	}
	if(src.isRegister()){
		srcSize = registers.getBitSize(src);
	}
	else{
		srcSize = memory.getBitSize(src);
		if( srcSize == 0){
		srcSize = registers.getBitSize(des);
		}
	}
	String sourceReg = "";
	String desStr = "";
	String srcStr = "";
	///end of defining sizes///
	if(des.isRegister()){
		if(desSize == srcSize && (srcSize == 64  || srcSize == 128 ) && src.isRegister() ){
			desStr = registers.get(des);
			srcStr = registers.get(src);
			sourceReg = executeMultiply(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex);
		}
		if((srcSize == 64  || srcSize == 128 ) && src.isMemory() ){
			desStr = registers.get(des);
			srcStr = memory.read(src, desSize);
			sourceReg = executeMultiply(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex);
		}
		registers.set(des, sourceReg);
	}
	if(des.isMemory()){
		if(desSize == srcSize && ( srcSize == 64  || srcSize == 128 ) && src.isRegister() ){
			desStr = memory.read(des, desSize);
			srcStr = registers.get(src);
			sourceReg = executeMultiply(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex);
		}
	}
}

String executeMultiply(des, src, registers, memory, c, desSize, srcSize, desStr, srcStr, sizeOfHex){
	String resultingMultiply = "";
//	for(int x = 0; x < srcSize / 4; x = x + sizeOfHex){
//		StringBuilder strToBuildDes = new StringBuilder();
//		StringBuilder strToBuildSrc = new StringBuilder();
//
//		for(int y = 0; y < sizeOfHex; y++){
//			strToBuildDes.append(desStr.charAt(x));
//			strToBuildSrc.append(srcStr.charAt(x));
//		}
//
//		BigInteger destination = new BigInteger((strToBuildDes).toString(),16);
//		BigInteger source = new BigInteger((strToBuildSrc).toString(),16);
//		destination = destination.multiply(source);
//		resultingMultiply += c.hexZeroExtend(destination.toString(16), sizeOfHex * 2).substring(sizeOfHex);
//	}

	String[] arrBi = new String[12];
	for(int x = 0; x < srcSize / 4; x = x + sizeOfHex){
		boolean isNegSrc = false;
		boolean isNegDes = false;
		String strDes = desStr.substring(x, x + sizeOfHex);
		String strSrc = srcStr.substring(x, x + sizeOfHex);

		BigInteger destination;
		BigInteger source;
		if(c.binaryZeroExtend(new BigInteger(strSrc, 16).toString(2), sizeOfHex * 4).charAt(0) == '1'){
			source = new BigInteger(c.hexZeroExtend(negate(des, src, registers, memory, c, sizeOfHex, strSrc), sizeOfHex), 16).negate();
			isNegSrc = true;
		}
		else{
			source = new BigInteger(strSrc, 16);
		}

		if(c.binaryZeroExtend(new BigInteger(strDes, 16).toString(2), sizeOfHex * 4).charAt(0) == '1'){
			destination = new BigInteger(c.hexZeroExtend(negate(des, src, registers, memory, c, sizeOfHex, strDes), sizeOfHex), 16).negate();
			isNegDes = true;
		}
		else{
			destination = new BigInteger(strDes, 16);
		}
//		System.out.println(source.toString(16) + " source");
//		System.out.println(destination.toString(16) + " destination");
		destination = destination.multiply(source);
//		System.out.println(destination.toString(16));
		if(destination.toString(16).charAt(0) == '-'){
			destination = new BigInteger(c.hexZeroExtend(negate(des, src, registers, memory, c, sizeOfHex, destination.toString(16).substring(1)), sizeOfHex), 16);
		}

		if(isNegDes && !isNegSrc || !isNegDes && isNegSrc){
			arrBi[((x + sizeOfHex)/ sizeOfHex) - 1] = (c.hexFExtend(destination.toString(16), sizeOfHex * 2));
		}
		else{
			arrBi[((x + sizeOfHex)/ sizeOfHex) - 1] = (c.hexZeroExtend(destination.toString(16), sizeOfHex * 2));
		}

	}
	if(desSize == 64)
		resultingMultiply = arrBi[0].substring(0, 4) + arrBi[1].substring(0, 4) + arrBi[2].substring(0, 4) + arrBi[3].substring(0, 4);
	else
		resultingMultiply = arrBi[0].substring(0, 4) + arrBi[1].substring(0, 4) + arrBi[2].substring(0, 4) + arrBi[3].substring(0, 4) + resultingMultiply + arrBi[4].substring(0, 4) + arrBi[5].substring(0, 4) + arrBi[6].substring(0, 4) + arrBi[7].substring(0, 4);
	return resultingMultiply;
}

String negate(des, src, registers, memory, c, sizeOfHex, desStr){
	String source = "";
	int borrow = 0;
	String destination = c.hexToBinaryString(desStr, sizeOfHex);
	destination = c.binaryZeroExtend(destination, sizeOfHex * 4);
	StringBuilder sb = new StringBuilder(destination);

	for(int x = 0; x < destination.length(); x++){
		if(sb.charAt(x) == '1'){
		sb.setCharAt(x, '0');
		}
		else{
			sb.setCharAt(x, '1');
		}
	}

	return new BigInteger(sb.toString(), 2).add(new BigInteger("1")).toString(16);
}