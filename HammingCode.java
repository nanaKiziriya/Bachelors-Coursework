import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class HammingCode{

    private static int systemParity = 0; // Either even (0) or odd (1). All bits must XOR to systemParity
    private static int numDataBits = 4; // ==Math.pow(2,numParityBits)-numParityBits-1
    private static int numParityBits = 3; // Must be s.t. >=2 AND <= 4, because ASCII conv. to 8-bit tuples
    private static int numTotalBits = 7;
    private static int numChunks = 2; // how many Hamming chunks one 8- bit ASCII char is broken into
    private static int[] bitValues = {7,6,5,3,1,2,4};
    private final static Scanner sc = new Scanner(System.in);

    // DONE
    public static void main(String[] args){
        System.out.print("\nPowering up my *ASCII HAMMING CODE* program");
        for(int i=0; i<3; i++){
            System.out.print(" .");
            pause(.5);
        }
        System.out.println("\n");

        System.out.println("[CONCEPT] Transmissions are sent as a combination of data bits and parity bits for error correction purposes.");
        pause(.5);
        System.out.println("[IMPLEMENTATION] Hamming code length is limited by ASCII char bit-length of 8. Each char is broken into chunks for Hamming encoding.\n");
        pause(1);

        printSystemStatus();

        while(true){

            pause(1);
            System.out.println("\n. . . <MAIN MENU> . . .\n");
            pause(.5);
            String[] options = {
                    "Terminate program",
                    "Change Hamming Code system",
                    "Tx: transmit a message",
                    "Rx: receive and correct a message"
            };

            int input = optionsPrompt(options);

            switch(input){
                case 0: sc.close(); System.out.println("Thank you for using my *ASCII HAMMING CODE* program!"); System.exit(0);
                case 1: setSystemStatus(); break;
                case 2: doTx(); break;
                case 3: doRx(); break;
            }
        }
    }

    /* Rx METHODS */

    // DONE
    private static void doRx(){

        String[] options = {
                "(Basic) Do error detection and correction on a single line of Hamming code.",
                "(Advanced) Correct and convert lines of Hamming code into a plaintext message."
        };
        int input = optionsPrompt(options);

        switch(input){
            case 0: doRx(false); break;
            case 1: doRx(true); break;
        }
    }

    // DONE
    private static void doRx(boolean multipleLines){

        System.out.printf("""
                        [PROTOCOL]
                            1. Enter %s%d-bit Hamming code%s.
                            2. %s Hamming code is calculated for error.
                            3. Error is indicated, and code corrected.
                            4. Corrected code is converted back to plaintext, given enough data.
                        
                        """,
                multipleLines?"each ":"the ",
                numTotalBits,
                multipleLines?" in a new line":"",
                multipleLines?String.format("Each %d-bit chunk of",numTotalBits):"The"
        );
        pause(.5);
        String[] bitStrings = validBitStringPrompt(numTotalBits, multipleLines);

        int[] charInts = new int[bitStrings.length/numChunks];
        int leftoverChunks=0; // numChunks may not divide # bitStrings

        for(int i=0; i<bitStrings.length; i++){

            String bitString = bitStrings[i];
            System.out.print(bitString+" -> "); // Now either print "VALID" or the corrected code

            int errorTerm = hammingXOR(bitString);

            if(errorTerm==0){
                System.out.println("VALID");
            } else {
                int index = Arrays.stream(bitValues).boxed().toList().indexOf(errorTerm);
                char[] tempChars = bitString.toCharArray();
                tempChars[index] = tempChars[index]=='0'?'1':'0';
                bitString = new String(tempChars);
                System.out.println(bitString);
            }

            try{

                charInts[i/numChunks] = (int) (charInts[i/numChunks]*Math.pow(2,numDataBits)+bitStringToInt(bitString.substring(0,numDataBits)));
            } catch(Exception E) { // No longer enough chunks to make a full ASCII char
                leftoverChunks++;
            }

        }

        System.out.println();
        if(multipleLines && leftoverChunks>0) System.out.printf("[NOTE] %d leftover chunks couldn't make full char, which requires %d chunks.\n\n",leftoverChunks,numChunks);

        if(multipleLines || leftoverChunks==0) {
            System.out.println("<Decoded Message>");
            for (int b : charInts) {
                try {
                    System.out.print((char) b);
                } catch (Exception e) {
                    System.err.printf("[ERROR] Cannot convert %d to ASCII char.\n", b);
                }
            }

            System.out.println("\n");
        }

        pause(1);

    }

    // DONE
    private static int hammingXOR(String bitString){
        int sum = 0;
        for(int i=0; i<bitString.length(); i++) if(bitString.charAt(i)=='1') sum^=bitValues[i];
        return sum;
    }

    // DONE
    private static int bitStringToInt(String bitString){
        int sum = 0;
        for(int i=0; i<bitString.length(); i++){
            sum*=2;
            if(bitString.charAt(i)=='1') sum++;
        }
        return sum;
    }

    /* Tx METHODS */

    // DONE
    private static void doTx(){
        String[] options = {
                String.format("(Basic) Convert a single %d-bit line of data into a %d-bit line of Hamming code.",numDataBits,numTotalBits),
                "(Advanced) Convert a plaintext message into lines of Hamming code."
        };

        int input = optionsPrompt(options);

        switch(input){
            case 0: doBasicTx(); break;
            case 1: doAdvancedTx(); break;
        }
    }

    // DONE
    private static void doBasicTx(){
        int data = dataBitsPrompt(numDataBits);
        System.out.println("<Encoded Message>");
        int parity = printHammingData(data,numDataBits);
        printHammingParity(parity,numParityBits);
        System.out.println("\n");
    }

    // DONE
    private static void doAdvancedTx(){

        System.out.printf("""
                        [PROTOCOL]
                            1. You enter a plaintext message in one line.
                            2. Each ASCII character is converted to 8-bit binary.
                            3. Each one is turned into %d %d-bit chunk(s).
                            4. Each chunk is converted to %d bit Hamming code, and transmitted.
                        
                        """,
                numChunks,
                numDataBits,
                numTotalBits);
        pause(.5);
        byte[] message;
        try{
            System.out.println("Enter your plaintext message:");
            message = userInput().getBytes();
        } catch(Exception e){
            System.err.println(e.getMessage());
            System.err.println("[ERROR] Unsupported character entered. Returning to main menu.");
            return;
        }

        System.out.println("<Encoded Message>");
        for(int b:message) for(int i=numChunks-1;i>=0;i--){
            int data = (int) (b/Math.pow(2,numDataBits*i));
            int parity = printHammingData(data,numDataBits);
            printHammingParity(parity,numParityBits);
            System.out.println();
            b %= (int) Math.pow(2,numDataBits*i);
        }
        System.out.println();
    }

    // DONE
    // Accepts an int of same binary length as numDataBits
    // Recursive, returns parity -> printHammingParity() after
    private static int printHammingData(int b,int numBitsLeft){
        if(numBitsLeft<=0) return systemParity;
        int data = b%2, parity = printHammingData(b/2,numBitsLeft-1);
        System.out.print(data);
        return (bitValues[numDataBits-numBitsLeft]*data)^parity;
    }

    // DONE
    // Accepts an int of same binary length as numParityBits
    // Recursive
    private static void printHammingParity(int b,int numBitsLeft){
        if(numBitsLeft<=0) return;
        System.out.print(b%2);
        printHammingParity(b/2,numBitsLeft-1);
    }


    /* SET SYSTEM STATUS METHODS */

    // DONE
    // Branches to Hamming system-changing methods
    private static void setSystemStatus(){
        while(true){
            String[] options = {
                    "Nothing. Return to main menu.",
                    String.format("Switch system parity to %s. All values would XOR to %d.",systemParity==0?"ODD":"EVEN",(systemParity+1)%2),
                    "Change # of parity bits. This also sets # of data bits to max possible value.",
                    "Change total Tx length. This also affects both # of parity and data bits."
            };

            int input = optionsPrompt(options);

            switch(input){
                case 0: printSystemStatus(); return;
                case 1: systemParity^=1; break;
                case 2: setNumParityBits(); break; // affects numParityBits, numDataBits, bitValues
                case 3: setNumTotalBits(); break; // affects numParityBits, numDataBits, bitValues
            }

            System.out.println("Change successful.\n");
            printSystemStatus();
        }
    }

    // DONE
    private static void setNumParityBits(){
        System.out.println("What's the new number of parity bits?");
        numParityBits = validNumberPrompt(2,4); // accommodates ASCII chars as ints
        numDataBits = (int) (Math.pow(2,numParityBits)-numParityBits-1);
        numResets();
    }

    // DONE
    private static void setNumTotalBits(){
        System.out.println("What's the new total Tx length?");
        int total = validNumberPrompt(3,15); // accommodates ASCII chars as ints: parity bits between [2,7]
        numParityBits = (int) (Math.log(total)/Math.log(2)+1);
        numDataBits = total-numParityBits;
        numResets();

    }

    // DONE
    private static void numResets(){
        numTotalBits = numParityBits + numDataBits;
        numChunks = (int) Math.ceil(8.0/numDataBits);
        resetBitValues();
    }

    // DONE
    private static void resetBitValues(){
        if(numTotalBits>=Math.pow(2,numParityBits+1)){
            System.err.print("[DEVELOPER ERROR] The value 'numParityBits' is not large enough to support 'numDataBits'.");
            System.exit(1);
        }
        bitValues = new int[numTotalBits];

        // 'i' is whole number being inserted into array, and 'dIndex' (data index) starts at highest val and goes down to 0, whilst "pIndex" starts from lowest and goes up
        for(int i=1,dIndex=numDataBits-1,pPow=0; i<=bitValues.length;i++){
            if(i==Math.pow(2,pPow)){
                bitValues[bitValues.length-numParityBits+pPow]=i;
                pPow++;
            } else {
                bitValues[dIndex] = i;
                dIndex--;
            }
        }
    }


    /* PROMPT METHODS */

    // DONE
    // Underlies all user inputs
    private static String userInput(){ return userInput(false); }
    private static String userInput(boolean multipleLines){
        System.out.print(" > ");
        String input = sc.nextLine();

        if(!multipleLines){
            pause(.5);
            System.out.println();
        }
        return input;
    }

    // DONE
    // Lets user choose btwn options provided, and returns valid (non-negative integer) input
    private static int optionsPrompt(String[] options){
        System.out.println("[PROMPT] What would you like to do?");
        pause(.5);
        for(int i=0; i<options.length; i++) System.out.printf("\t%d - %s\n",i,options[i]);
        pause(.5);
        System.out.println();
        return validNumberPrompt(0,options.length-1);
    }

    // DONE
    // Prompts for an integer within given range, and returns valid input
    private static int validNumberPrompt(int first, int last){
        while(true){
            System.out.printf("Enter a number between %d and %d:\n",first,last);
            try{
                int input = Integer.parseInt(userInput());
                if(input<first||input>last) System.out.println("Input must be an valid/available option. Try again.");
                else return input;
            } catch(Exception e){
                System.out.println("Input must be a whole number. Try again.");
            }
        }
    }

    // DONE
    // Turns input of 1's and 0's into int value
    private static int dataBitsPrompt(int bitLength){
        String input = validBitStringPrompt(bitLength,false)[0];
        int intValue=0;
        for(int i=0; i<input.length(); i++) intValue = 2*intValue + Integer.parseInt(""+input.charAt(i));
        return intValue;
    }

    // DONE
    // Returns String of bits of given length, multiple lines if specified
    private static String[] validBitStringPrompt(int bitLength, boolean multipleLines){
        System.out.printf("Enter the %d-bit token%s",bitLength,multipleLines?"s. ":":");
        if(multipleLines) System.out.printf("Enter a blank line when done:",bitLength);
        System.out.println();

        ArrayList<String> inputs = new ArrayList<>();

        while(true){
            boolean isValid = true;
            String input = userInput(multipleLines);

            if(multipleLines && !inputs.isEmpty() && input.trim().isEmpty()) return inputs.toArray(new String[0]);

            if(input.length()!=bitLength){
                System.out.printf("Input must be length %d. Try again.\n",bitLength);
                continue;
            }
            for(int i=0; i<input.length(); i++){
                int b = Integer.parseInt(""+input.charAt(i));
                if(b!=b%2){
                    System.out.println("Input must be 1's and 0's. Try again.");
                    isValid = false;
                    break;
                }
            }
            if(isValid){
                inputs.add(input);
                if(!multipleLines) return inputs.toArray(new String[0]);
            }
            else if(!multipleLines) System.out.printf("Enter an %d-bit token: \n",bitLength);
        }

    }


    /* HELPER METHODS */

    // DONE
    private static void printSystemStatus(){
        System.out.printf("""
                        [CURRENT STATUS]
                            System parity..........%s
                            # of parity bits.......%d
                            # of data bits.........%d
                            # of total bits........%d
                            # of chunks per char...%d
                            Value of each bit......%s
                        
                        """,

                systemParity==1?"ODD":"EVEN",
                numParityBits,
                numDataBits,
                numTotalBits,
                numChunks,
                Arrays.toString(bitValues));

    }

    private static void pause(double seconds){
        try{
            Thread.sleep((long) (seconds*1000));
        } catch(Exception e){
            System.err.print("[DEVELOPER ERROR] This application is not thread-safe: cannot use Thread.sleep().");
            System.exit(1);
        }
    }


}
