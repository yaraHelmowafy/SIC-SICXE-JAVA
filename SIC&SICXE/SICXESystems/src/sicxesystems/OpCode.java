//Yara Hesham Elmowafy 19100910
package sicxesystems;

import java.util.ArrayList;

public class OpCode {

    private String Opcode;
    private String INST;

    public OpCode() {
    }

    public OpCode(String instructions, String Opcode) {
        this.Opcode = Opcode;
        this.INST = instructions;
    }

    public String getOpcode() {
        return Opcode;
    }

    public void setOpcode(String Opcode) {
        this.Opcode = Opcode;
    }

    public String getInstructions() {
        return INST;
    }

    public void setInstructions(String instructions) {
        this.INST = instructions;
    }

    

    public static void OPCODE(ArrayList<OpCode> Op) {
        Op.add(new OpCode("ADD", "18"));
        Op.add(new OpCode("ADDF", "58"));
        Op.add(new OpCode("AND", "40"));
        Op.add(new OpCode("COMP", "28"));
        Op.add(new OpCode("COMPF", "88"));
        Op.add(new OpCode("DIV", "24"));
        Op.add(new OpCode("DIVF", "64"));
        Op.add(new OpCode("J", "3C"));
        Op.add(new OpCode("JEQ", "30"));
        Op.add(new OpCode("JGT", "34"));
        Op.add(new OpCode("JLT", "38"));
        Op.add(new OpCode("JSUB", "48"));
        Op.add(new OpCode("LDA", "00"));
        Op.add(new OpCode("LDB", "68"));
        Op.add(new OpCode("LDCH", "50"));
        Op.add(new OpCode("LDF", "70"));
        Op.add(new OpCode("LDL", "08"));
        Op.add(new OpCode("LDS", "6C"));
        Op.add(new OpCode("LDT", "74"));
        Op.add(new OpCode("LDX", "04"));
        Op.add(new OpCode("LPS", "D0"));
        Op.add(new OpCode("MUL", "20"));
        Op.add(new OpCode("MULF", "60"));
        Op.add(new OpCode("OR", "44"));
        Op.add(new OpCode("RD", "D8"));
        Op.add(new OpCode("RSUB", "4C"));
        Op.add(new OpCode("TD", "E0"));
        Op.add(new OpCode("TIX", "2C"));
        Op.add(new OpCode("WD", "DC"));
        Op.add(new OpCode("SSK", "EC"));
        Op.add(new OpCode("STA", "0C"));
        Op.add(new OpCode("STB", "78"));
        Op.add(new OpCode("STCH", "54"));
        Op.add(new OpCode("STF", "80"));
        Op.add(new OpCode("STI", "D4"));
        Op.add(new OpCode("STL", "14"));
        Op.add(new OpCode("STSW", "E8"));
        Op.add(new OpCode("STT", "84"));
        Op.add(new OpCode("STX", "10"));
        Op.add(new OpCode("SUB", "1C"));
        Op.add(new OpCode("SUBF", "5C"));
        Op.add(new OpCode("CLEAR","B4"));
        Op.add(new OpCode("COMPR","A0"));
        Op.add(new OpCode("TIXR","B8"));
    }
    public static String GetOpCode(ArrayList<OpCode> Op, String inst) {
        for (int i = 0; i < Op.size(); i++) {
            if (Op.get(i).getInstructions().equals(inst)) {
                return Op.get(i).getOpcode();
            }
        }
        return "Not Found";
    }
}
