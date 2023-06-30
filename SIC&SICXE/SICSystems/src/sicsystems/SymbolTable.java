//Yara Hesham Elmowafy 19100910
package sicsystems;

import java.util.ArrayList;

public class SymbolTable 
{
    private String Label;
    private int LocCTR;

    public String getLabel() {
        return Label;
    }

    public void setLabel(String Label) {
        this.Label = Label;
    }

    public int getLocCtr() {
        return LocCTR;
    }

    public void setLocCtr(int LocCtr) {
        this.LocCTR = LocCtr;
    }
    public static int getLocation(ArrayList<SymbolTable >symbolTable,String Ref){
        for(int i=0;i<symbolTable.size();i++){
            if(symbolTable.get(i).getLabel().equals(Ref)){
                return symbolTable.get(i).getLocCtr();
            }
        }
        return -1;
    }
    
}
