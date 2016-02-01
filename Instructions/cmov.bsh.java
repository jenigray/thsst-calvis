 execute(des, src, registers, memory) {
 	if ( des.isRegister() ){
 		// 16 bit (4 hex) or 32 (8 hex) bit registers only
 		if ( registers.getSize(des) == 4 
 				|| registers.getSize(des) == 8) {
	 		if ( src.isRegister() ) { 
	 			if ( registers.getSize(src) == 4 
 					|| registers.getSize(src) == 8) {
	 			System.out.println("CMoving register to register");
	 			String x = registers.get(src);
	 			registers.set(des, x);
	 			}
	 		}
	 		else if ( src.isMemory() ){
	 			System.out.println("CMoving memory to register");
	 			EnvironmentConfiguration.model.Calculator cal = new EnvironmentConfiguration.model.Calculator(registers, memory);
	 			String address = cal.computeMemoryAddress(src);
	 			int des_reg_size = registers.getSize(des);
	 			String x = memory.read(address, des_reg_size);
	 			registers.set(des, x);
	 		}
 		}
 	}
 }

 /*
 	CONCERN: Where do we put the logic for CC
 */