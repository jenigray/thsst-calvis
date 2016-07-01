execute(registers, memory) {
    Calculator calculator = new Calculator(registers, memory);
    EFlags flags = registers.getEFlags();

    Token token = new Token(Token.REG, "EDI");

    memory.write(registers.get("EDI"), registers.get("AX"), 16);

    if( flags.getDirectionFlag().equals("0") ) {
        BigInteger x = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.add(new BigInteger("2"));

        registers.set(token, calculator.binaryToHexString(result.toString(2), token));
    }
    else if( flags.getDirectionFlag().equals("1") ) {
        BigInteger x = new BigInteger(registers.get("EDI"), 16);
        BigInteger result = x.subtract(new BigInteger("2"));

        if ( result.signum() == -1 ) {
            registers.set(token, "0");
        } else {
            registers.set(token, calculator.binaryToHexString(result.toString(2), token));
        }
    }
}
