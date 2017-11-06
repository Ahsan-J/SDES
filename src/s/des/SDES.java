/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package s.des;

/**
 *
 * @author Ahsan Jawed
 */
public class SDES {

    /**
     * @param args the command line arguments
     */
    static int P10 [] = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    static int IP [] = {2, 6, 3, 1, 4, 8, 5, 7};
    static int EP [] = {4, 1, 2, 3, 2, 3, 4, 1};
    static int P4 [] = {2, 4, 3, 1};
    static int IP_inverse [] = {4, 1, 3, 5, 7, 2, 8, 6};
    static int P8 [] = {6, 3, 7, 4, 8, 5, 10, 9};
    static int S0 [][] = 
    {
        {1, 0, 3, 2},
        {3, 2, 1, 0},
        {0, 2, 1, 3},
        {3, 1, 3, 2}
    };
    static int S1 [][] = 
    {
        {0, 1, 2, 3},
        {2, 0, 1, 3},
        {3, 0, 1, 0},
        {2, 1, 0, 3}
    };
    static int Key[]={1,0,1,0,0,0,0,0,1,0}; 
    
    public static int [] table_return(int[] value,int[] tables){
        int [] newValue = new int [tables.length];
        for(int i=0;i<tables.length;i++){
            newValue[i]=value[tables[i]-1];
        }
        return newValue;
    }
    public static void display(int [] value){
        for(int i=0;i<value.length;i++){
            System.out.print(value[i]);
        }
    }
    public static int[] CircularLeftShift(int[] value){
        int temp = value[0];
        int[]  newValue = new int [value.length];
        
        for(int i=0;i<value.length-1;i++){
            newValue[i]=value[i+1];
        }
        newValue[value.length-1]=temp;
        return newValue;
    }
    public static int[] Concatinate(int [] val1,int [] val2){
        int [] newValue=new int[val1.length+val2.length];
        int i=0;
        for(i=0;i<val1.length;i++){
            newValue[i]=val1[i];
        }
        for(int j=0;j<val2.length;j++){
            newValue[i+j]=val2[j];
        }
        return newValue;
    }
    public static int [][] keyGeneration(){
        int subKeys[][] = new int [2][8];
        int [] Key_P10 = table_return(Key,P10); //Step1
        
        int[] Key_left=new int[P10.length/2]; //Step2
        int[] Key_right=new int[P10.length/2];
        for(int i=0;i<Key_left.length;i++){
            Key_left[i]=Key_P10[i];
            Key_right[i]=Key_P10[i+Key_left.length];
        }
        
        Key_left = CircularLeftShift(Key_left); //Step3
        Key_right = CircularLeftShift(Key_right);
        int [] Key_Con = Concatinate(Key_left, Key_right);
        
        subKeys[0] = table_return(Key_Con,P8);//Step4
        
        Key_left = CircularLeftShift(Key_left);//Step5
        Key_right = CircularLeftShift(Key_right);
        Key_left = CircularLeftShift(Key_left); 
        Key_right = CircularLeftShift(Key_right);
        Key_Con = Concatinate(Key_left, Key_right);
        
        subKeys[1] = table_return(Key_Con,P8);//Step6
        
        return subKeys;
    }
    public static int [][] createMatrix(int [] value){
        int[] left=new int[value.length/2];
        int[] right=new int[value.length/2];
        for(int i=0;i<left.length;i++){
            left[i]= value[i];
            right[i]=value[i+left.length];
        }
        int [][] Matrix=new int [2][4];
        Matrix[0]=left;
        Matrix[1]=right;
        return Matrix;
    }
    public static int [][] XOR_2D(int [][] val1,int val2[][]){
        int [][]P=new int[2][4];
        for(int i=0;i<2;i++){
            for(int j=0;j<4;j++){
                P[i][j]=val1[i][j]^val2[i][j];
            }
        }
        return P;
    }
    
    public static int [] XOR_1D(int [] val1,int val2[]){
        int []P=new int[val1.length];
        if(val1.length==val2.length){
            for(int i=0;i<val1.length;i++){
                P[i]=val1[i]^val2[i];
            }
        }
        return P;
    }
    
    public static int toDecimal(String val){
        double Sum = 0;
        for(int i=0;i<val.length();i++){
            Sum = Sum + Character.getNumericValue(val.charAt(i))* Math.pow(2, (val.length()-1)-i);
        }
        return (int) Sum;
    }
    
    public static int [] performSBox(int val [][]){
        String S0_x = (Integer.toString(val[0][1])+Integer.toString(val[0][2]));
        String S0_y = (Integer.toString(val[0][0])+Integer.toString(val[0][3]));
        String S1_x = (Integer.toString(val[1][1])+Integer.toString(val[1][2]));
        String S1_y = (Integer.toString(val[1][0])+Integer.toString(val[1][3]));
        
        String merged = Integer.toBinaryString(S0[toDecimal(S0_y)][toDecimal(S0_x)])+Integer.toBinaryString(S1[toDecimal(S1_y)][toDecimal(S1_x)]);
        int [] retVal = new int [merged.length()];
        
        for(int i=0;i<merged.length();i++){
            retVal[i] = Character.getNumericValue(merged.charAt(i));
        }
        
        return retVal;
    }
    
    public static int [] fK(int [] PT_IP,int[] Key){
       int[] PT_IP_left=new int[PT_IP.length/2]; //Step1
        int[] PT_IP_right=new int[PT_IP.length/2];
        for(int i=0;i<PT_IP_left.length;i++){
            PT_IP_left[i]= PT_IP[i];
            PT_IP_right[i]=PT_IP[i+PT_IP_left.length];
        }
        
        int [] New_PT_IP_right=Concatinate(PT_IP_right, PT_IP_right);//Step2
        
        int [] PT_EP = table_return(New_PT_IP_right, EP);//Step3
        
        int [][] n = createMatrix(PT_EP);//Step4
        
        int [][] k = createMatrix(Key); //Step5
        
        int [][] P = XOR_2D(n, k); //Step6
        
        int[] S = performSBox(P); //Step7 & Step8
        
        int [] S_P4 = table_return(S, P4); //Step9
        
        int [] BitXORed = (XOR_1D(PT_IP_left,S_P4)); //Step10
        
        int [] Concatinated = Concatinate(BitXORed,PT_IP_right);
        
        int [] SwitchedValue = Concatinate(PT_IP_right, BitXORed);
        
        return SwitchedValue;
    }
    
//    public static int [] switchFunction(int [] val){
//        int [] valA = new int [val.length/2];
//        int [] valB = new int [val.length/2];
//        for(){
//            
//        }
//    } 
    
    public static void Encrypt(int [] PT){
        int getKeys[][] = keyGeneration();
        int [] PT_IP = table_return(PT, IP); //Step1
        int [] Switched = fK(PT_IP,getKeys[0]);
//        display(Switched);
        display(fK(Switched,getKeys[1]));
    }
    
    public static void main(String[] args) {
        int PlainText []={1,0,1,1,1,1,0,1};
        int getKeys[][] = keyGeneration();
        int [] Key1 = getKeys[0];
        int [] Key2 = getKeys[1];
        
        Encrypt(PlainText);
    }    
}
