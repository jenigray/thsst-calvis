 execute(registers, memory){
   System.out.println("STC");

   EFlags flags = registers.getEFlags();

	 flags.setCarryFlag("1");
 }
