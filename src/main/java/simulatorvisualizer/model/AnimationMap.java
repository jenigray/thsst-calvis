package simulatorvisualizer.model;

import simulatorvisualizer.model.instructionanimation.*;

import java.util.HashMap;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class AnimationMap extends HashMap<String, CalvisAnimation> {

    public AnimationMap() {
        super();
        generateGP();
    }

    private void generateGP() {
        this.generateGPDataTransfer();
        this.generateGPBinaryArithmetic();
        this.generateGPDecimalArithmetic();
        this.generateGPRotate();
        this.generateGPStack();
        this.generateGPJump();
    }

    private void generateGPDataTransfer() {
        this.put("MOV", new Mov());
        this.put("LEA", new Lea());
        this.put("XLAT", new Xlat());
        this.put("CMOV", new Cmov());
        this.put("STC", new Stc());
        this.put("CLC", new Clc());
        this.put("CMC", new Cmc());
        this.put("CLD", new Cld());
        this.put("STD", new Std());
        this.put("STI", new Sti());
        this.put("CLI", new Cli());
        this.put("SAHF", new Sahf());
        this.put("LAHF", new Lahf());
        this.put("PUSHF", new Pushf());
        this.put("PUSHFD", new Pushfd());
        this.put("POPF", new Popf());
        this.put("POPFD", new Popfd());
        this.put("AND", new And());
        this.put("OR", new Or());
        this.put("XOR", new Xor());
        this.put("NOT", new Not());
        this.put("TEST", new Test());
        this.put("BT", new Bt());
        this.put("BTS", new Bts());
        this.put("BTR", new Btr());
        this.put("BTC", new Btc());
        this.put("BSF", new Bsf());
        this.put("BSR", new Bsr());
        this.put("CBW", new Cbw());
        this.put("CWDE", new Cwde());
        this.put("CWD", new Cwd());
        this.put("CDQ", new Cdq());
        this.put("MOVSX", new Movsx());
        this.put("MOVZX", new Movzx());
        this.put("SHL", new Shl());
        this.put("SAL", new Sal());
        this.put("SHR", new Shr());
        this.put("SAR", new Sar());
        this.put("SHLD", new Shld());
        this.put("SHRD", new Shrd());
        this.put("SET", new Set());
        this.put("XCHG", new Xchg());
        this.put("BSWAP", new Bswap());
        this.put("XADD", new Xadd());
        this.put("CMPXCHG", new Cmpxchg());
        this.put("CMPXCHGS8", new Cmpxchgs8());
    }

    private void generateGPBinaryArithmetic() {
        this.put("ADD", new Add());
        this.put("ADC", new Ad("CF", true));
        this.put("ADCX", new Ad("CF", false));
        this.put("ADOX", new Ad("OF", false));
        this.put("SUB", new Sub());
        this.put("SBB", new Sbb());
        this.put("INC", new Inc());
        this.put("DEC", new Dec());
        this.put("CMP", new Cmp());
        this.put("NEG", new Neg());
        this.put("MUL", new Mul());
        this.put("IMUL", new Imul());
        this.put("DIV", new Div());
        this.put("IDIV", new Div());
    }

    private void generateGPDecimalArithmetic() {
        this.put("DAA", new Daa());
        this.put("DAS", new Das());
        this.put("AAA", new Aaa());
        this.put("AAS", new Aas());
        this.put("AAM", new Aam());
        this.put("AAD", new Aad());
    }

    private void generateGPRotate(){
        this.put("ROR", new Ror());
        this.put("ROL", new Rol());
        this.put("RCR", new Rcr());
        this.put("RCL", new Rcl());
    }

    private void generateGPStack(){
        this.put("PUSH", new Push());
        this.put("PUSHA", new Pusha());
        this.put("PUSHAD", new Pushad());
        this.put("POP", new Pop());
        this.put("POPA", new Popa());
        this.put("POPAD", new Popad());
    }

    private void generateGPJump(){
        this.put("JMP", new Jmp());
        this.put("J", new J());
        this.put("JECXZ", new Jecxz());
        this.put("JCXZ", new Jcxz());
        this.put("LOOPE", new Loope());
        this.put("LOOPNE", new Loopne());
        this.put("LOOPZ", new Loopz());
        this.put("LOOPNZ", new Loopnz());
        this.put("LOOP", new Loop());
    }
}
