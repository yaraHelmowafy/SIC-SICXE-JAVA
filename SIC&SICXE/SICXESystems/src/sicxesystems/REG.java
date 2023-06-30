//Yara Elmowafy 19100910
package sicxesystems;
import java.util.ArrayList;
public class REG {

   
        private String REG;
        private String REF;
        
   public REG()
   {
       
   }
 public REG(String REG, String REF) {
        this.REG = REG;
        this.REF = REF;
    }
   
    public String getREG() {
        return REG;
    }

    public void setREG(String REG) {
        this.REG = REG;
    }

    
    public String getREF() {
        return REF;
    }

   
    public void setREF(String REF) {
        this.REF = REF;
    }
    
        

    public static void Regester(ArrayList<REG> RE) {
        RE.add(new REG("A", "0"));
        RE.add(new REG("X", "1"));
        RE.add(new REG("B", "3"));
        RE.add(new REG("S", "4"));
        RE.add(new REG("T", "5"));
        RE.add(new REG("F", "6"));
        
    }
    public static String GetREG(ArrayList<REG> RE, String reg) {
        for (int i = 0; i < RE.size(); i++) {
            if (RE.get(i).getREG().equals(reg)) {
                return RE.get(i).getREF();
            }
        }
        return "Not Found";
    }
}
