execute(registers, memory) {
    String st0 = registers.get("ST0");
    // split significand and exponent
    // st0 = gets exponent
    // significand is pushed
    registers.set("ST0", st0);
}
