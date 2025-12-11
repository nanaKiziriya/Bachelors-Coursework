import java.util.Scanner;
import java.util.Arrays;

public class HammingCode{

  private static byte systemParity = 0; // Either even (0) or odd (1). All bits must XOR to systemParity
  private static byte numDataBits = 4; // ==Math.pow(2,numParityBits)-numParityBits-1
  private static byte numParityBits = 3; // Must be s.t. >=2 AND <= 4, because ASCII conv. to 8-bit tuples, and Byte.MAX_VALUE==127
  private static byte numTotalBits = 7;
  private static byte numChunks = 2; // how many Hamming chunks one 8- bit ASCII char is broken into
  private static byte[] bitValues = {7,6,5,3,1,2,4};
  private final static Scanner sc = new Scanner(System.in);

  // DONE
  public static void main(String[] args){
    System.out.println("WELCOME TO MY ASCII HAMMING CODE PROGRAM.");
    System.out.println("Concept: Transmissions are sent as a combination of data bits and parity bits for error correction purposes.");
    System.out.println("Implementation: Code length is limited by ASCII char bit-length. Each char is broken into chunks for Hamming encoding.");
    System.out.println();
    
    printSystemStatus();
    
    while(true){
      String[] options = {
        "Terminate program",
        "Change Hamming Code system",
        "Tx: transmit a message",
        "Rx: receive and correct a message"
      };
    
      byte input = optionsPrompt(options);

      switch(input){
        case 0: sc.close(); System.exit(0);
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
      "[Basic] Do error detection and correction on a single line of Hamming code.",
      "[Advanced] Correct and convert lines of Hamming code into a plaintext message."
    };
    byte input = optionsPrompt(options);

    switch(input){
      case 0: doRx(false); break;
      case 1: doRx(true); break;
    }
  }
  
  // DONE
  private static void doRx(boolean multipleLines){
    
    System.out.printf("PROTOCOL:\n"
                      + "1. Enter %s%d-bit Hamming code%s.\n"
                      + "2. %s Hamming code is calculated for error.\n"
                      + "3. Error is indicated, and code corrected.\n"
                      + "4. Corrected code is converted back to plaintext.\n\n",
                      multipleLines?"each ":"the ",
                      numTotalBits,
                      multipleLines?" in a new line":"",
                      multipleLines?String.format("Each %d-bit chunk of",numTotalBits):"The "
                      );
    
    String[] bitStrings = validBitStringPrompt(numTotalBits, multipleLines);

    byte[] charBytes = new byte[bitStrings.length/numChunks];

    for(int i=0; i<bitStrings.length; i++){
      
      String bitString = bitStrings[i];
      System.out.print(bitString+" -> "); // Now either print "VALID" or the corrected code
      
      byte errorTerm = hammingXOR(bitString);
      
      if(errorTerm==0){
        System.out.println("VALID");
      } else {
        byte index = (byte)(Arrays.asList(bitValues).indexOf(errorTerm));
        char[] tempChars = bitString.toCharArray();
        tempChars[index] = tempChars[index]=='0'?'1':'0';
        bitString = new String(tempChars);
        System.out.println(bitString);
      }

      try{
        charBytes[i/3] = charBytes[i/3]*(byte)Math.pow(2,numDataBits)+bitStringToByte(bitString.substring(0,numDataBits));
      } catch(Exception E){} // No longer enough chunks to make a full ASCII char
    }

    for(byte b:charBytes){
      try{ System.out.print((char)b); }
      catch(Exception e){ System.err.printf("ERROR: Cannot convert %d to ASCII char.\n",b); }
    }
  }

  // DONE
  private static byte hammingXOR(String bitString){
    int sum = 0;
    for(int i=0; i<bitString.length(); i++) if(bitString.charAt(i)=='1') sum^=bitValues[i];
    return (byte)sum;
  }

  // DONE
  private static byte bitStringToByte(String bitString){
    int sum = 0;
    for(int i=0; i<bitString.length(); i++){
      sum*=2;
      if(bitString.charAt(i)=='1') sum++;
    }
    return (byte)sum;
  }
  
/* Tx METHODS */

  // DONE 
  private static void doTx(){
    String[] options = {
      String.format("[Basic] Convert a single %d-bit line of data into a %d-bit line of Hamming code.",numDataBits,numTotalBits),
      "[Advanced] Convert a plaintext message into lines of Hamming code."
      };
      
    byte input = optionsPrompt(options);

    switch(input){
      case 0: doBasicTx(); break;
      case 1: doAdvancedTx(); break;
    }
  }

  // DONE
  private static void doBasicTx(){
    byte data = dataBitsPrompt(numDataBits);
    byte parity = printHammingData(data,numDataBits);
    printHammingParity(parity,numParityBits);
  }

  // DONE
  private static void doAdvancedTx(){
    
    System.out.printf("PROTOCOL:\n"
                      + "1. You enter a plaintext message in one line.\n"
                      + "2. Each ASCII character is converted to 8-bit binary.\n"
                      + "3. Each one is turned into %d %d-bit chunk(s).\n"
                      + "4. Each chunk is converted to %d bit Hamming code, and transmitted.\n\n",
                      numChunks,
                      numDataBits,
                      numTotalBits);
    
    byte[] message;
    try{
      System.out.println("Enter your plaintext message:");
      message = userInput().getBytes();
    } catch(Exception e){
      System.err.println(e.getMessage());
      System.err.println("ERROR: Unsupported character entered. Returning to main menu.");
      return;
    }

    System.out.println("\nTransmitting...");
    for(byte b:message) for(int i=numChunks-1;i>=0;i--){
      byte data = b/(byte)Math.pow(2,numDataBits*i);
      byte parity = printHammingData(data,numDataBits);
      printHammingParity(parity,numParityBits);
      System.out.println();
      b %= Math.pow(2,numDataBits*i);
    }
  }

  // DONE
  // Accepts a byte of same binary length as numDataBits
  // Recursive, returns parity -> printHammingParity() after
  private static byte printHammingData(int b,int numBitsLeft){
    if(numBitsLeft<=0) return systemParity;
    int data = b%2, parity = printHammingData(b/2,numBitsLeft-1);
    System.out.print(data);
    return (byte)((bitValues[numDataBits-numBitsLeft]*data)^parity);
  }

  // DONE
  // Accepts a byte of same binary length as numParityBits
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
        String.format("Switch system parity to %s. All values would XOR to %d.",systemParity==1?"ODD":"EVEN",systemParity),
        "Change # of parity bits. This also sets # of data bits to max possible value.",
        "Change total Tx length. This also affects both # of parity and data bits."
      };
      
      byte input = optionsPrompt(options);
  
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
    numParityBits = validNumberPrompt(2,4); // accomodates ASCII chars as bytes
    numDataBits = Math.pow(2,numParityBits)-numParityBits-1;
    numTotalBits = numParityBits + numDataBits;
    numChunks = Math.ceil(8/numDataBits);
    resetBitValues();
  }

  // DONE
  private static void setNumTotalBits(){
    System.out.println("What's the new total Tx length?");
    byte total = validNumberPrompt(3,15); // accomodates ASCII chars as bytes: parity bits between [2,7]
    numParityBits = Math.floor(Math.log(total)/Math.log(2));
    numDataBits = total-numParityBits;
    numTotalBits = numParityBits + numDataBits;
    numChunks = Math.ceil(8/numDataBits);
    resetBitValues();
  }
    
  // DONE
  private static void resetBitValues(){
    if(numTotalBits>Math.pow(2,numParityBits)-1){
      System.err.print("DEVELOPER ERROR: The value 'numParityBits' is not large enough to support 'numDataBits'.");
      System.exit(1);
    }
    bitValues = new int[numTotalBits];
    for(int i=1,dIndex=numDataBits-1,pPow=0; dIndex>=0||pPow<bitValues.length;i++){
      if(i==Math.pow(2,pPow)){
        bitValues[bitValues.length-numParityBits+pPow]=i;
        pPow++;
      } else {
        bitValues[dIndex]=i;
        dIndex--;
      }
    }
  }


/* HELPER METHODS */

  // DONE
  // Underlies all user inputs
  private static String userInput(){
    System.out.print(" > ");
    return sc.nextLine();
  }

  // DONE
  // Lets user choose btwn options provided, and returns valid (nonnegative integer) input
  private static byte optionsPrompt(String[] options){
    System.out.println("What would you like to do?");
    for(int i=0; i<options.length; i++) System.out.printf("%d - %s\n",i,options[i]);
    return validNumberPrompt(0,options.length-1;
  }

  // DONE
  // Prompts for an integer within given range, and returns valid input
  private static byte validNumberPrompt(int first, int last){
    while(true){
      System.out.printf("Enter a number between %d and %d:\n",first,last);
      try{
        byte input = Byte.parseByte(userInput());
        if(input<first||input>last) System.out.println("Input must be an valid/available option. Try again.");
        else return input;
      } catch(Exception e){
        System.out.println("Input must be a reasonably small whole number. Try again.");
      }
    }
  }

  // DONE
  // Turns input of 1's and 0's into byte value
  private static byte dataBitsPrompt(int bitLength){
    String input = validBitStringPrompt(bitLength,false)[0];
    int byteValue=0;
    for(int i=0; i<input.length(); i++) byteValue = 2*byteValue + Byte.parseByte(""+input.charAt(i));
    return (byte)byteValue;
  }

  // DONE
  // Returns String of bits of given length
  private static String[] validBitStringPrompt(int bitLength, boolean multipleLines){
    if(!multipleLines) System.out.printf("Enter an %d-bit token: \n",bitLength);
    else System.out.printf("Enter the %d-bit tokens. Enter a blank line when done: ",bitLength);

    ArrayList<String> inputs = new ArrayList<>();
    
    while(true){
      boolean isValid = true;
      String input = userInput();
      
      if(multipleLines && inputs.size()>0 && input.trim().isEmpty()) return inputs;
      
      if(input.length()!=bitLength){
        System.out.printf("Input must be length %d. Try again.\n",bitLength);
        continue;
      }
      for(int i=0; i<input.length(); i++){
        byte b = Byte.parseByte(""+input.charAt(i));
        if(b!=b%2){
          System.out.println("Input must be 1's and 0's. Try again.");
          isValid = false;
          break;
        }
      }
      if(isValid){
        inputs.add(input);
        if(!multipleLines) return inputs;
      }
      else if(!multipleLines) System.out.println("Enter an %d-bit token: ",bitLength);
    }
    
  }

  // DONE
  private static void printSystemStatus(){
    System.out.printf("CURRENT STATUS\n"
                      + "System parity: %s\n"
                      + "# of parity bits: %d\n"
                      + "# of data bits: %d\n"
                      + "# of total bits: %d\n"
                      + "# of Hamming code chunks per ASCII char: %d\n"
                      + "Value of each bit: %s\n",
                      
                      systemParity==1?"ODD":"EVEN",
                      numParityBits,
                      numDataBits,
                      numTotalBits,
                      numChunks,
                      bitValues);
  }

    
}
