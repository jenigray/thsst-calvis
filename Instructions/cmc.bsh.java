 execute(registers, memory){
   System.out.println("CMC");

   EFlags flags = registers.getEFlags();

   if(flags.getCarryFlag().equals("1")) {
     flags.setCarryFlag("0");
   }
   else if (flags.getCarryFlag().equals("0")) {
     flags.setCarryFlag("1");
   }
 }
