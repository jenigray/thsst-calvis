 execute(des, src, registers, memory) {
 	Calculator calculator = new Calculator();

 	if ( des.isRegister() ) {
 		if ( src.isRegister() ) {
 			System.out.println("AND register and register");
 			String x = registers.get(src);
 			String y = registers.get(des);
 			System.out.println("des: " + y + "src: " + x);
 			System.out.println("hello");
            String result = "";
        /*
      for (int i = 0; i < 32; i++) {
        if (y.charAt(i) == '1' && x.charAt(i) == '1') {
          result.concat("1");
        }
        else {
          result.concat("0");
        }
      }

 	  registers.set(des, result);

        */
      //  EFlags flags = registers.getEflags();
      //  flags.setCarryFlag("0");
      /*
        registers.setCF("0");
        registers.setOF(0);
        registers.setAF(-1); undefined

        if (des == 0)
          registers.setZF(1)
        else
          registers.setZF(0)

        registers.setSF(des.charAt(0));
		
		int i = registers.getSize(des);
		int count = 0;
		int counter = 0;
		while ( counter <= 7 ) {
			if ( des.charAt(i) == '1' )
				count++;
			counter ++
		}
		if ( count % 2 == 0 )
			registers.setPF(1);
		else
			registers.setPF(0);
      */
 		}
 		else if ( src.isMemory() ) {
 			System.out.println("AND register and memory");
 			int des_reg_size = registers.getSize(des);
 			String x = memory.read(src, des_reg_size);
      String result = "";

      for (int i = 0; i < registers.getSize(des); i++) {
        if (des.charAt(i) == '1' && x.charAt(i) == '1') {
          result.concat("1");
        }
        else {
          result.concat("0");
        }
      }
 			registers.set(des, result);

      /*
        registers.setCF(0);
        registers.setOF(0);
        registers.setAF(-1); undefined

        if (des == 0)
          registers.setZF(1)
        else
          registers.setZF(0)

        registers.setSF(des.charAt(0));
		
		int i = registers.getSize(des);
		int count = 0;
		int counter = 0;
		while ( counter <= 7 ) {
			if ( des.charAt(i) == '1' )
				count++;
			counter ++
		}
		if ( count % 2 == 0 )
			registers.setPF(1);
		else
			registers.setPF(0);
      */
 		}
    else if ( scr.isHex() ) {
      System.out.println("AND register and immediate");
      String x = src;
      String result = "";

      for (int i = 0; i < registers.getSize(des); i++) {
        if (des.charAt(i) == '1' && x.charAt(i) == '1') {
          result.concat("1");
        }
        else {
          result.concat("0");
        }
      }
 			registers.set(des, result);

      /*
        registers.setCF(0);
        registers.setOF(0);
        registers.setAF(-1); undefined

        if (des == 0)
          registers.setZF(1)
        else
          registers.setZF(0)

        registers.setSF(des.charAt(0));
		
		int i = registers.getSize(des);
		int count = 0;
		int counter = 0;
		while ( counter <= 7 ) {
			if ( des.charAt(i) == '1' )
				count++;
			counter ++
		}
		if ( count % 2 == 0 )
			registers.setPF(1);
		else
			registers.setPF(0);
      */
    }
 	}
 	else if ( des.isMemory() ){
    if ( src.isRegister() ) {
 			System.out.println("AND memory and register");
 			String x = registers.get(src);
      String result = "";

      for (int i = 0; i < registers.getSize(des); i++) {
        if (des.charAt(i) == '1' && x.charAt(i) == '1') {
          result.concat("1");
        }
        else {
          result.concat("0");
        }
      }
 			registers.set(des, result);

      /*
        registers.setCF(0);
        registers.setOF(0);
        registers.setAF(-1); undefined

        if (des == 0)
          registers.setZF(1)
        else
          registers.setZF(0)

        registers.setSF(des.charAt(0));
		
		int i = registers.getSize(des);
		int count = 0;
		int counter = 0;
		while ( counter <= 7 ) {
			if ( des.charAt(i) == '1' )
				count++;
			counter ++
		}
		if ( count % 2 == 0 )
			registers.setPF(1);
		else
			registers.setPF(0);
      */
 		}
 		else if ( src.isMemory() ) {
 			System.out.println("AND memory and memory");
 			int des_reg_size = registers.getSize(des);
 			String x = memory.read(src, des_reg_size);
      String result = "";

      for (int i = 0; i < registers.getSize(des); i++) {
        if (des.charAt(i) == '1' && x.charAt(i) == '1') {
          result.concat("1");
        }
        else {
          result.concat("0");
        }
      }
 			registers.set(des, result);

      /*
        registers.setCF(0);
        registers.setOF(0);
        registers.setAF(-1); undefined

        if (des == 0)
          registers.setZF(1)
        else
          registers.setZF(0)

        registers.setSF(des.charAt(0));
		
		int i = registers.getSize(des);
		int count = 0;
		int counter = 0;
		while ( counter <= 7 ) {
			if ( des.charAt(i) == '1' )
				count++;
			counter ++
		}
		if ( count % 2 == 0 )
			registers.setPF(1);
		else
			registers.setPF(0);
      */
 		}
    else if ( scr.isHex() ) {
      System.out.println("AND memory and immediate");
      String x = src;
      String result = "";

      for (int i = 0; i < registers.getSize(des); i++) {
        if (des.charAt(i) == '1' && x.charAt(i) == '1') {
          result.concat("1");
        }
        else {
          result.concat("0");
        }
      }
 			registers.set(des, result);

      /*
        registers.setCF(0);
        registers.setOF(0);
        registers.setAF(-1); undefined

        if (des == 0)
          registers.setZF(1)
        else
          registers.setZF(0)

        registers.setSF(des.charAt(0));
		
		int i = registers.getSize(des);
		int count = 0;
		int counter = 0;
		while ( counter <= 7 ) {
			if ( des.charAt(i) == '1' )
				count++;
			counter ++
		}
		if ( count % 2 == 0 )
			registers.setPF(1);
		else
			registers.setPF(0);
      */
    }
 	}
 }
