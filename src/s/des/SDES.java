/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package s.des;

/**
 *
 * @author TUSER
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
    public static int [][] XOR(int [][] val1,int val2[][]){
        int [][]P=new int[2][4];
        for(int i=0;i<2;i++){
            for(int j=0;j<4;j++){
                P[i][j]=val1[i][j]^val2[i][j];
            }
        }
        return P;
    }
    
    public static int toDecimal(String val){
        double Sum = 0;
        for(int i=val.length()-1;i>0;i--){
            Sum = Sum + Character.getNumericValue(val.charAt(i))+ Math.pow(2, i);
        }
        return (int) Sum;
    }
    
    public static int [][] performSBox(int val [][]){
        String S0_row = (Integer.toString(val[0][1])+Integer.toString(val[0][2]));
        String S0_column = (Integer.toString(val[0][0])+Integer.toString(val[0][3]));
        String S1_row = (Integer.toString(val[1][1])+Integer.toString(val[1][2]));
        String S1_column = (Integer.toString(val[1][0])+Integer.toString(val[1][3]));
        
        System.out.println(toDecimal(S1_column));
        
        return val;
    }
    
    public static void fK(int [] PT_IP){
        int getKeys[][] = keyGeneration();
        
        int[] PT_IP_left=new int[PT_IP.length/2]; //Step1
        int[] PT_IP_right=new int[PT_IP.length/2];
        for(int i=0;i<PT_IP_left.length;i++){
            PT_IP_left[i]= PT_IP[i];
            PT_IP_right[i]=PT_IP[i+PT_IP_left.length];
        }
        
        PT_IP_right=Concatinate(PT_IP_right, PT_IP_right);//Step2
        
        int [] PT_EP = table_return(PT_IP_right, EP);//Step3
        
        int [][] n = createMatrix(PT_EP);//Step4
        
        int [][] k = createMatrix(getKeys[0]); //Step5
        
        int [][] P = XOR(n, k); //Step6
        
        int[][] extracted = performSBox(P); //Step7
        
    }
    public static void Encrypt(int [] PT){
        int [] PT_IP = table_return(PT, IP); //Step1
        fK(PT_IP);
    }
    
    public static void main(String[] args) {
        int PlainText []={1,0,1,1,1,1,0,1};
        int getKeys[][] = keyGeneration();
        int [] Key1 = getKeys[0];
        int [] Key2 = getKeys[1];
        
        Encrypt(PlainText);
    }    
}
