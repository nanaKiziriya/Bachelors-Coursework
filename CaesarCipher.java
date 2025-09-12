import java.util.Scanner;

public class CaesarCipher{

    static Scanner sc = new Scanner(System.in);
    static String exitMsg = "endmessage";
    static int snippetLength = 50;
    
    public static void main(String[]args){
        System.out.println("Welcome to Caesar Cipher.\n");
        System.out.println("Encrypt? Y/N");
        if(sc.next().toUpperCase().charAt(0)=='Y') runEncryption(true);
        System.out.println("Decrypt?");
        if(sc.next().toUpperCase().charAt(0)=='Y') runEncryption(false);
        System.out.println("Program terminate.");
    }

    static void runEncryption(boolean encrypt){
        System.out.printf("Begin entering %smessage. Enter line \"%s\" to end message.\n",
                           (encrypt)?"":"encoded ",exitMsg);
        
        StringBuilder unfilteredSB = new StringBuilder();
        
        String token = sc.next();
        do{
            unfilteredSB.append(token);
            token = sc.next();
        } while(!token.equalsIgnoreCase(exitMsg));
        
        char[] unfilteredChars;
        StringBuilder filteredSB = new StringBuilder();
        
        if(encrypt) {
            unfilteredChars = unfilteredSB.toString().toUpperCase().toCharArray();
            for(char c:unfilteredChars) if(64<c&&c<91) filteredSB.append(c);
        } else {
            unfilteredChars = unfilteredSB.toString().toLowerCase().toCharArray();
            for(char c:unfilteredChars) if(96<c&&c<123) filteredSB.append(c);
        }
        
        String filteredStr = filteredSB.toString(); // alphabetical, of proper case
        
        System.out.println("Received. ");
        
        if(encrypt){
            System.out.println("Enter number for cipher shift.");
        } else {
            System.out.println("Enter number corresponding with decrypted snippet:");
            for(int i=1;i<26;i++){
                // when decrypting, shift DOWN
                System.out.printf("%-2d ",i);
                decryptShift(filteredStr, i, snippetLength);
                System.out.println();
            }
        }
            
        int cipher = Math.abs(sc.nextInt()); // assert non-negative

        if(encrypt){
            System.out.println("Cipher selected. Encrypted message:");
            encryptShift(filteredStr, cipher);
        } else {
            System.out.println("Number selected is cipher. Decrypted message:");
            decryptShift(filteredStr, cipher, filteredStr.length());
        }
        System.out.println();
        System.out.println();
        
    }

    private static void encryptShift(String filteredStr, int cipher){
        for(int j=0; j<filteredStr.length(); j++){
            char c = (char)(string.charAt(j)+cipher);
            c -= (c>90)? 26:0;
            System.out.print(c);
            if(j%5==4) System.out.print(" ");
        }
    }

    private static void decryptShift(String filteredStr, int cipher, int length){
        for(int j=0; j<length; j++){
                char c = (char)(string.charAt(j)-cipher);
                c += (c<97)? 26:0;
                System.out.print(c);
            }
    }
}
