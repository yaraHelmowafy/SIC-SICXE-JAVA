//Yara Hesham Elmowafy 19100910 
package sicsystems;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Scanner;
public class SICSystems {
 public static void insertfile (ArrayList<Instructions> List,String []T1)
    {
        try{
            File file = new File("C:\\Users\\Dell\\OneDrive\\Desktop\\SIC\\inSIC.txt");
            Scanner Read = new Scanner(file);
            while (Read.hasNextLine()) {
                String item = Read.nextLine();
                 item = item.trim();
                           
                
                String[] T2 = item.split("\t");
                
                if(T2[1].equalsIgnoreCase("start"))
                {
                    //program name
                    T1[0]=T2[0];
                    //first location 
                    T1[1]=T2[2];
                   
                    continue;
                }
                
                if(T2.length==3)
                {
                    
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
            else{
                
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
    
    public static String getASCII (char c)
    {
        return String.format("%x", c);
    }
   
    public static void getObjectCode (ArrayList<Instructions> List, ArrayList<OpCode> OpTab, ArrayList<SymbolTable> symbolTable)
    {
   
        for(int i=0; i<List.size();i++)   
        {    
            String currentRef = List.get(i).getRef();
            String objCode =""  ;
            if((List.get(i).getInst().equalsIgnoreCase("RESW"))||(List.get(i).getInst().equalsIgnoreCase("RESB"))||(List.get(i).getInst().equalsIgnoreCase("END")))
            {
                objCode="______";
            }
            else if(List.get(i).getInst().equalsIgnoreCase("WORD"))
            {
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
            else
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
                    
                }
            }
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
            getObjectCode(List, OpTab, symbolTable);
            
            ArrayList<String> HTEList = new ArrayList<String>();
            HTE_record(List, progName, firstLocation,HTEList);
            
            System.out.println("Location Counter :\n================================");
            for (int i =0; i<List.size(); i++)
            {
                 System.out.println(Integer.toHexString(List.get(i).getLocation())+"\t"+List.get(i).getLabel()+"\t"+List.get(i).getInst()+"\t"+List.get(i).getRef()+"\t"+List.get(i).getObjectCode());
            }
            
            System.out.println("------------------------------------------------------------------------------------------------------");
            
            System.out.println("Symbol Table :\n================================");
            for (int i =0;i<symbolTable.size();i++)
            {
                System.out.println(symbolTable.get(i).getLabel()+"\t"+Integer.toHexString(symbolTable.get(i).getLocCtr())+"\t");
            }
            System.out.println("------------------------------------------------------------------------------------------------------");
            
            System.out.println("HTE Record :\n================================");
            for (int i =0;i<HTEList.size();i++)
            {
                System.out.println(HTEList.get(i));
            }
    }
    }
    

