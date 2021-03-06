execute(des,src,op3,registers,memory) {
    int DWORD = 32;
    int DQWORD = 128;
    String desValue = registers.get(des);
    String desValue0 = desValue.substring(24,32);
    String desValue1 = desValue.substring(16,24);
    String desValue2 = desValue.substring(8,16);
    String desValue3 = desValue.substring(0,8);
    String srcValue;
    String srcValue0;
    String srcValue1;
    String srcValue2;
    String srcValue3;
    Calculator calculator = new Calculator(registers,memory);

    if(src.isRegister()) {
        srcValue = registers.get(src);
    } else if(src.isMemory()) {
        srcValue = memory.read(src,DQWORD);
    }

    srcValue0 = srcValue.substring(24,32);
    srcValue1 = srcValue.substring(16,24);
    srcValue2 = srcValue.substring(8,16);
    srcValue3 = srcValue.substring(0,8);













    String result0 = compare(calculator,desValue0,srcValue0,op3);
    String result1 = compare(calculator,desValue1,srcValue1,op3);
    String result2 = compare(calculator,desValue2,srcValue2,op3);
    String result3 = compare(calculator,desValue3,srcValue3,op3);

    registers.set(des,result3.concat(result2.concat(result1.concat(result0))));
}

String compare(calculator,desValue,srcValue,op3) {



    Float floatDes = calculator.convertHexToSinglePrecision(desValue);
    Float floatSrc = calculator.convertHexToSinglePrecision(srcValue);

    String operand = op3.getValue();
    int intOperand = Integer.parseInt(operand);


    int retval = Float.compare(floatDes, floatSrc);
    switch(intOperand) {
    case 0:
        // des == src
        if(retval == 0)
            return "FFFFFFFF";
        else
            return "00000000";
        break;
    case 1:
        // des < src
        if(retval < 0)
            return "FFFFFFFF";
        else
            return "00000000";
        break;
    case 2:
        // des <= src
        if(retval < 0 || retval == 0)
            return "FFFFFFFF";
        else
            return "00000000";
        break;
    case 3:
        if(floatDes.isNaN() || floatSrc.isNaN())
            return "FFFFFFFF";
        else
            return "00000000";
        break;
    case 4:
        // des != src
        if(retval != 0)
            return "FFFFFFFF";
        else
            return "00000000";
        break;
    case 5:
        // des >= src
        if(retval > 0 || retval == 0)
            return "FFFFFFFF";
        else
            return "00000000";
        break;
    case 6:
        // des > src
        if(retval > 0)
            return "FFFFFFFF";
        else
            return "00000000";
        break;
    case 7:
        if(!floatDes.isNaN() && !floatSrc.isNaN())
            return "FFFFFFFF";
        else
            return "00000000";
        break;
    default:
    }
}
