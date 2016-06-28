execute(registers, memory) {
	Calculator c = new Calculator(registers, memory);
    String st0 = registers.get("ST0");
    String st1 = registers.get("ST1");
    // scale st0 by st1
    // st0 = st0 * 2 ^ roundtowardszero(st1)
    double exponent = c.convertHexToDoublePrecision(st1);
	double mantissa = c.convertHexToDoublePrecision(st0);
	double resultPow = Math.pow(2, exponent.intValue());
	double resultingAnswer = resultPow * mantissa;
    registers.set("ST0", st0);
	registers.x87().status().set("C3",'0');
	registers.x87().status().set("C0",'0');
}
