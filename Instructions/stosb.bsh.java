execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    Token token = new Token(Token.REG, "EDI");

    BigInteger compare1 = new BigInteger("10000", 16);
    BigInteger compare2 = new BigInteger("0000", 16);

    memory.write(registers.get("EDI"), registers.get("AL"), 8);

    if( flags.getDirectionFlag().equals("0") ) {
        BigInteger x = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.add(new BigInteger("1"));

        if( result.toString(16).equals("10000") || result.compareTo(compare1) == 1 ) {
            result = new BigInteger("00000000", 16);
            registers.set(token, calculator.binaryToHexString(result.toString(2), token));
        }
        else {
            registers.set(token, calculator.binaryToHexString(result.toString(2), token));
        }
    }
    else if( flags.getDirectionFlag().equals("1") ) {
        BigInteger x = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.subtract(new BigInteger("1"));

        if( result.toString(16).equals("-1") || result.compareTo(compare2) == -1 ) {
            result = new BigInteger("FFFF", 16);
            registers.set(token, calculator.binaryToHexString(result.toString(2), token));
        }
        else {
            registers.set(token, calculator.binaryToHexString(result.toString(2), token));
        }
    }
}
