import java.util.Scanner;

public class CaesarCipher{

    static Scanner sc = new Scanner(System.in);
    static String exitMsg = "getmeoutofhere";
    static int dSnippetLength = 50;
    
    public static void main(String[]args){
        System.out.println("Welcome to Caesar Cipher.\n");
        System.out.println("Encrypt? Y/N");
        if(sc.next().toUpperCase().charAt(0)=='Y') runEncryption(true);
        System.out.println("Decrypt?");
        if(sc.next().toUpperCase().charAt(0)=='Y') runEncryption(false);
    }

    static void runEncryption(boolean encrypt){
        System.out.println("Enter %smessage. White spaces ignored. Enter line \"%s\" to end message.\n",
                           (encrypt)?"":"encoded ",exitMsg);
        StringBuilder stringBuilder = new StringBuilder();
        do(String line = sc.nextLine()){
            stringBuilder.add(line.split(" ","\t","\n"));
            line = sc.nextLine();
        } while(!line.equalsIgnoreCase(exitMsg));
        
        String string = stringBuilder.toString().toUpperCase();
        
        System.out.println("Received. ");
        if(encrypt){
            System.out.println("Enter number for cipher shift.");
        } else {
            System.out.println("Enter number corresponding with decrypted snippet:");
            for(char c, int i=1;i<26;i++){
                // when decrypting, shift DOWN
                System.out.printf("%-2d ",i);
                for(int j=0;j<dSnippetLength;j++){
                    c = code.charAt(j)-i;
                    c += (c<65)?26:0
                    System.out.print(c);
                }
                System.out.println()
            }
        }
            
        int cipher = Math.abs(sc.nextInt());

        if(encrypt){
            System.out.println("Cipher selected. Encrypted message:");
            for(char c, int j=0;j<string.length();j++){
                c = string.charAt(j)+cipher;
                c -= (c>90)? 26:0
                System.out.print(c);
                if(j%5==4) System.out.print(" ");
            }
        } else {
            System.out.println("Number selected is cipher. Decrypted message:");
            string = string.toLowerCase();
            for(char c, int j=0;j<string.length();j++){
                c = string.charAt(j)-cipher;
                c += (c<97) 26:0;
                System.out.print(c);
            }
        }
        
    }
}
