//Yara Hesham Elmowafy 19100910 
package sicxesystems;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
public class SICXESystems {
 public static void insertfile (ArrayList<Instructions> List,String []T1)
    {
        try{
            File file = new File("C:\\Users\\Dell\\OneDrive\\Desktop\\inSICXE.txt");
            Scanner Read = new Scanner(file);
            while (Read.hasNextLine()) {
                String item = Read.nextLine();
                item = item.trim();
                String[] T2 = item.split("\t");
            
                
                if  (T2.length==1){
                     Instructions obj = new Instructions();
                    
                    obj.setLabel("####");
                    obj.setInst(T2[0]);
                    obj.setRef("####");
                    
                    List.add(obj);
                }
                else if(T2.length==3)
                {
                    if(T2[1].equalsIgnoreCase("START"))
                {
                    //program name
                    T1[0]=T2[0];
                    //first location 
                    T1[1]=T2[2];
                   
                    continue;
                }
                    Instructions obj = new Instructions();
                    
                    obj.setLabel(T2[0]);
                    obj.setInst(T2[1]);
                    obj.setRef(T2[2]);
                    
                    List.add(obj);
                }
                else if(T2.length==2)
                {
                    if(T2[1].equalsIgnoreCase("END")){
                        Instructions obj = new Instructions();
            
                        obj.setLabel(T2[0]);
                        obj.setInst(T2[1]);
                        obj.setRef("####");
                    
                        List.add(obj);
                    }
                    else{
                        Instructions obj = new Instructions();

                        obj.setLabel("####");
                        obj.setInst(T2[0]);
                        obj.setRef(T2[1]);

                        List.add(obj);
                    }
                }
            }
            
            Read.close();
        } 
        catch (FileNotFoundException e) {
            System.out.println("File not found !");
            e.printStackTrace();
        }
    }

    public static void getLocation (ArrayList<Instructions> List,String firstLocation)
    {
        List.get(0).setLocation(Integer.parseInt(firstLocation, 16));
       
        for(int i=0; i < List.size();i++)
        {    
             String CurrentInst = List.get(i).getInst();
            if(List.get(i).getInst().equalsIgnoreCase("RESW"))   
            {
                List.get(i+1).setLocation(List.get(i).getLocation()+Integer.parseInt(List.get(i).getRef())*3);
            }
            else if(List.get(i).getInst().equalsIgnoreCase("RESB")){
                 List.get(i+1).setLocation(List.get(i).getLocation()+Integer.parseInt(List.get(i).getRef()));
                     
            }
            else if((List.get(i).getInst().equalsIgnoreCase("BYTE"))){
            
                if(List.get(i).getRef().charAt(0)=='C'){
                      List.get(i+1).setLocation(List.get(i).getLocation()+List.get(i).getRef().length()-3);
                    
                }
               
                else{
                    
                      List.get(i+1).setLocation(List.get(i).getLocation()+(List.get(i).getRef().length()-3)/2);
                }
            }
            else if (List.get(i).getInst().equalsIgnoreCase("END")){
                return;
                
            }
            else if(List.get(i).getInst().equalsIgnoreCase("WORD"))
            {
                String []n= List.get(i).getRef().split(",");
                List.get(i+1).setLocation(List.get(i).getLocation()+(n.length*3));
            }
            //Format 2
            else if (CurrentInst.charAt(CurrentInst.length()-1)=='R')
            {  
                List.get(i+1).setLocation(List.get(i).getLocation()+2);
            }
            //Format 4
              else if (CurrentInst.charAt(0)=='+')
            {  
                List.get(i+1).setLocation(List.get(i).getLocation()+4);
            }
               else if (List.get(i).getInst().equalsIgnoreCase("BASE"))
                        {
                            List.get(i+1).setLocation(List.get(i).getLocation());
                        }
            else
            {
                List.get(i+1).setLocation(List.get(i).getLocation()+3);
            }
        }
    }
    
    public static void getTable (ArrayList<Instructions> List,ArrayList<SymbolTable> symbolTable)
    {
        for(int i =0; i<List.size()-1;i++)
        {
            if(!List.get(i).getLabel().equalsIgnoreCase("####"))
            {
               
                SymbolTable sym = new SymbolTable();
                sym.setLabel(List.get(i).getLabel());
                sym.setLocCtr(List.get(i).getLocation());
                symbolTable.add(sym);
            }
        }
        
    }
    
    public static String Neglect (String op){
        op = Integer.toBinaryString(Integer.parseInt(op,16));
        String copy = "";
        for(int i=0; i<op.length()-2;i++)
        {
            copy += op.charAt(i);
        }
        
        return copy;
    }
    
    public static String NeglectFirst (String op){
        String copy="";
        for(int i=1; i<op.length();i++)
        {
            copy+=op.charAt(i);
        }
        return copy;
    }
    
      public static String getASCII (char c)
   {   int ascii = (int)c;
       String charc = Integer.toHexString(ascii);
       return charc;
    
   }
   
     public static String getObjectCode2(String op, String[]f2,ArrayList<REG>RE)
   {
       if (f2.length==2)
       {
           return op + REG.GetREG(RE,f2[0]) + REG.GetREG(RE,f2[1]);
       }
       return op + REG.GetREG(RE,f2[0]) + "0";
   }
     
  public static String getObjectCode4 (String currentRef,String Instructions , ArrayList<OpCode> OpTab, ArrayList<SymbolTable> symbolTable)
    {
        String n,i,x,b,p,e;
        b = p ="0";
        e="1";
        
        String objCode = "";
        
        String opCode = OpCode.GetOpCode(OpTab, Instructions);
            
            objCode = Neglect(opCode);
            
             if(currentRef.charAt(0)== '@')
             {
                 n="1";
                 i="0";
                 currentRef = NeglectFirst(currentRef);
             }
             else if(currentRef.charAt(0)== '#')
             {
                 n="0";
                 i="1";
                 currentRef = NeglectFirst(currentRef);
             }
             else 
             {
                 n=i="1";
             }
             if(currentRef.charAt(currentRef.length()-1)=='X'&& currentRef.charAt(currentRef.length()-2)==',')
             {
                 x="1";
             }
             else
             {
                 x="0";
             }
            objCode += n+i+x+b+p+e;
            
            objCode = String.format("%x", Integer.parseInt(objCode,2));
            
            if(SymbolTable.getLocation(symbolTable, currentRef) != -1)
            {
                int address = SymbolTable.getLocation(symbolTable, currentRef);
                String hexAddress = String.format("%05x", address);
                objCode += hexAddress;
            }
            //Incase #45454(any number)
            else
            {
                String hexAddress = String.format("%05x", Integer.parseInt(currentRef));
                objCode += hexAddress;
            }
            
            return objCode;
            
        }
  
        
    
  public static String getObjectCode3 (String currentRef,String Instructions ,int nextloc, ArrayList<OpCode> OpTab, ArrayList<SymbolTable> symbolTable)
    
            {
        String n,i,x ,b ,p ,e ;
        
        e="0";
        

          String objCode = "";
        
          String op3 = OpCode.GetOpCode(OpTab, Instructions);
            
            objCode = Neglect(op3);
            
            
              if(currentRef.charAt(0)== '@')
             {
                 n="1";
                 i="0";
                 currentRef = NeglectFirst(currentRef);
             }
             else if(currentRef.charAt(0)== '#')
             {
                 n="0";
                 i="1";
                 currentRef = NeglectFirst(currentRef);
                 
             }
             else 
             {
                 n=i="1";
             }
              if(currentRef.charAt(currentRef.length()-1)=='X'&& currentRef.charAt(currentRef.length()-2)==',')
              {
                  x="1";
                  String []r=currentRef.split(",");
                  currentRef=r[0];
              }
              else
              {
                  x="0";
              }
              
            objCode += n+i+x;
           // objCode = String.format("%x", Integer.parseInt(objCode,2));
            
            if(SymbolTable.getLocation(symbolTable, currentRef) != -1)
            {
                int TA = SymbolTable.getLocation(symbolTable, currentRef);
                
                int Dis = TA - nextloc;
               // String hexDIS = String.format("%03x", Dis);
               // objCode += hexDIS;
                if (0xfffff800 < Dis && Dis < 0x7ff)
                {
                    p="1";
                    b="0";
                }
                else
                {
                   p="0";
                   b="1";
                }
               objCode+=b+p+e;
              objCode = String.format("%x", Integer.parseInt(objCode,2));
               String hexDIS = String.format("%03x", Dis);
               objCode += hexDIS;
               
            }
            else if  (Instructions.equals("RSUB")){
                     objCode = OpCode.GetOpCode(OpTab, Instructions)+"0000";
                     // objCode="______";
                     
            }
            //Incase #45454(any number)
            else
            {  // System.out.print(currentRef);
                String hexDIS = String.format("%03x", Integer.parseInt(currentRef));
                objCode += hexDIS;
            } 
            return objCode;
        }
    
    
   public static void getObjectCode (ArrayList<Instructions> List, ArrayList<OpCode> OpTab, ArrayList<SymbolTable> symbolTable,ArrayList<REG> RE, ArrayList <OP2> OpTab2 )
    { 
        
        
        for(int i=0; i<List.size();i++)   
        {  
          
            String CurrentInst = List.get(i).getInst();
         
            String currentRef = List.get(i).getRef();
            String objCode =""  ;
             String objW ="";
            if((List.get(i).getInst().equalsIgnoreCase("RESW"))||(List.get(i).getInst().equalsIgnoreCase("RESB"))||(List.get(i).getInst().equalsIgnoreCase("END"))||(List.get(i).getInst().equalsIgnoreCase("BASE")))
            {
                objCode="______";
            }
            else if(List.get(i).getInst().equalsIgnoreCase("WORD"))
            {
                String [] n = List.get(i).getRef().split(",");
                int m=n.length;
                for (String n1 : n) {
                    objW += String.format("%06x", Integer.parseInt(n1));
                }
                objCode += objW;
                objCode=String.format("%06x",Integer.parseInt(currentRef));
            }
            else if(List.get(i).getInst().equalsIgnoreCase("BYTE"))
            {
                if(currentRef.charAt(0)=='X')
                {   
                    String objx = "";
                    for(int j=2; j<currentRef.length()-1;j++)
                    {
                        objx += currentRef.charAt(j);
                    }
                    objCode = objx;
                }
                else
                {
                    String objc = "";
                    for(int j=2; j<currentRef.length()-1;j++)
                    {
                        objc += getASCII(currentRef.charAt(j));
                    }
                    objCode = objc; 
                }
            }
            //Format 4
           else if(List.get(i).getInst().charAt(0)=='+')
           {    
               String inst = NeglectFirst(CurrentInst);
               
               objCode = getObjectCode4 (currentRef,inst,OpTab, symbolTable); 
               //objCode="________";
           }
           
           else if (!OP2.FindOpcode(OpTab2,List.get(i).getInst()).equals("Not Found"))
            {
                
                String op = OP2.FindOpcode(OpTab2,List.get(i).getInst());
                String[]f2= List.get(i).getRef().split(",");
                objCode = getObjectCode2(op,f2,RE);
            }
            //format3
            else 
            {
                int nextloc = List.get(i+1).getLocation();
           objCode= getObjectCode3(currentRef, CurrentInst, nextloc, OpTab, symbolTable);
             //objCode="______";
            }
            /*else
            {
                
                String instruction = List.get(i).getInst();
   
                objCode = OpCode.GetOpCode(OpTab, instruction);
                
                if(currentRef.charAt(currentRef.length()-2)==',')
                {
                    String [] refX =currentRef.split(",");
                    int number=SymbolTable.getLocation(symbolTable,refX[0]) + 32768;
                    objCode += String.format("%04x", number);
                }
                else{
                     String [] refX =currentRef.split(",");
                    int number=SymbolTable.getLocation(symbolTable,refX[0]);
                    objCode += String.format("%04x", number);
                    
                }*/
            
            List.get(i).setObjectCode(objCode);
        }
    }
  
    
    public static void HTE_record (ArrayList<Instructions> List,String progName , String firstLocation,ArrayList<String> HTEList )
    {
         //HRecord
        String H = "H^";
        for(int i=progName.length();i<6;i++)
        {
            H+=" ";
        }
       
        int Lastlocation = List.get(List.size()-1).getLocation();
        String progLength = String.format("%06x",Lastlocation - Integer.parseInt(firstLocation,16) );
        
        H+=progName+"^"+firstLocation+"^"+progLength;
        
        HTEList.add(H);
        
        //TRecord
         for(int i=0; i<List.size()-1;i++)
       {    
           int size =0;
           ArrayList <String> TList =new ArrayList <String> ();
           int Startloc= List.get(i).getLocation();
           
           boolean flag = false;
           
        
           while(!List.get(i).getObjectCode().equalsIgnoreCase("______") && size<60)
           {
               flag = true;
               
               TList.add(List.get(i).getObjectCode());
               size+=List.get(i).getObjectCode().length();
               i++;
           }
           
           if(flag == true)
           {
               String TLength = String.format("%02x", List.get(i).getLocation()-Startloc);
               String T = "T^" + String.format("%06x",Startloc) + "^" + TLength + "^";
               for(int j=0; j<TList.size();j++)
               {
                   T += TList.get(j);
                   if(j<TList.size()-1)
                   {
                       T += " ";
                   }
               }
               HTEList.add(T);
               
               if( size >= 60)
               {
                   i--;
               }
           }
       }
         //ERecord
        String E = "E^" + String.format("%06x", Integer.parseInt(firstLocation,16));
        
        HTEList.add(E);
              
    }
    
    public static void main(String[] args) {
            String progName=null;
            
            String firstLocation;
            
            ArrayList<Instructions> List = new ArrayList<Instructions>( );
            String[]temp = new String[2];
            insertfile(List,temp);
            progName = temp[0];
            firstLocation = temp[1];
            getLocation(List, firstLocation);
            
            ArrayList<SymbolTable> symbolTable = new ArrayList<SymbolTable>();
            getTable(List ,symbolTable);
            
           ArrayList<OpCode> OpTab = new ArrayList<OpCode>();
            OpCode.OPCODE(OpTab);
            ArrayList<REG> RE = new ArrayList<REG>(); 
            REG.Regester(RE);
           ArrayList <OP2> OpTab2 = new ArrayList <OP2>() ;
            OP2.Initialize(OpTab2);
           getObjectCode(List, OpTab, symbolTable,RE,OpTab2);
            
            ArrayList<String> HTEList = new ArrayList<String>();
            HTE_record(List, progName, firstLocation,HTEList);
            
            System.out.println("Location Counter :\n================================");
            for (int i =0; i<List.size(); i++)
            {
                 System.out.println(String.format("%04x", List.get(i).getLocation())+"\t"+List.get(i).getLabel()+"\t"+List.get(i).getInst()+"\t"+List.get(i).getRef()+"\t"+List.get(i).getObjectCode());
            }
            
            System.out.println("------------------------------------------------------------------------------------------------------");
            
            System.out.println("Symbol Table :\n================================");
            for (int i =0;i<symbolTable.size();i++)
            {
                
                System.out.println(symbolTable.get(i).getLabel()+"\t"+String.format("%04x",symbolTable.get(i).getLocCtr())+"\t");
            }
            System.out.println("------------------------------------------------------------------------------------------------------");
            
            System.out.println("HTE Record :\n================================");
            for (int i =0;i<HTEList.size();i++)
            {
                System.out.println(HTEList.get(i));
            }
    }
    }