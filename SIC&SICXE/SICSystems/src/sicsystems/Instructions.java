//Yara Hesham Elmowafy 19100910
package sicsystems;

import java.text.DecimalFormat;


public class Instructions
{
    private int LOC;
    private String label;
    private String INST;
    private String objectCode;

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public int getLocation() {
        return LOC;
    }

    public void setLocation(int location) 
    {
        this.LOC = location;
       
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInst() {
        return INST;
    }

    public void setInst(String inst) {
        this.INST = inst;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
    private String ref;
    
}
