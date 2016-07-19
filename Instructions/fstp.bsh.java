execute(des, registers, memory) {
   String value = registers.x87().pop();
   if ( des.isRegister() ) {
       registers.set(des, value);
   }
   else if ( des.isMemory() ) {
       int size = memory.getBitSize(des);
       Converter cal = new Converter(value);
       if ( size == 32 ) {
            // conversion to single precision
            value = cal.toSinglePrecisionHex();
       } else if ( size == 64 ) {
            // conversion to double precision
            value = cal.toDoublePrecisionHex();
       } else if ( size == 80 ) {
            value = "0000" + cal.toDoublePrecisionHex();
       } 
       memory.write(des, value, des);
   }
}
