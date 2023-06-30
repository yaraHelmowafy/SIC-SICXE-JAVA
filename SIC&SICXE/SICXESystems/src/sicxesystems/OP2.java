/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicxesystems;

import java.util.ArrayList;

public class OP2 
{
     private String Opcode;
   private String instructions;

    public OP2 () 
    {
    }
    
    public OP2(String instructions,String Opcode) {
        this.Opcode = Opcode;
        this.instructions = instructions;
    }

    public String getOpcode() {
        return Opcode;
    }

    public void setOpcode(String Opcode) {
        this.Opcode = Opcode;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    public static String FindOpcode(ArrayList<OP2>OpTab,String instruc)
    {
        for(int i=0;i<OpTab.size();i++)
        {
            if(OpTab.get(i).getInstructions().equals(instruc))
            {
                return OpTab.get(i).getOpcode();
            }
        }
       return "Not Found"; 
    }
    public static void Initialize (ArrayList<OP2>OpTab)
    {
        OpTab.add(new OP2("CLEAR"	,"B4"));
        OpTab.add(new OP2("TIXR"	,"B8"));
        OpTab.add(new OP2("COMPR"	,"A0"));
        OpTab.add(new OP2("DIVR"     ,"9C"));
        OpTab.add(new OP2("MULR"     ,"98"));
    }}
