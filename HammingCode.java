public class HammingCode{

  private static byte systemParity = 0; // Either even (0) or odd (1). All bits must XOR to systemParity
  private static byte numDataBits = 4; // ==Math.pow(2,numParityBits)-numParityBits-1
  private static byte numParityBits = 3; // Must be s.t. >=2 AND <= 4, because ASCII conv. to 8-bit tuples, and Byte.MAX_VALUE==127
  priavte static byte numChunks = 2; // how many Hamming chunks one 8- bit ASCII char is broken into
  private static byte[] bitValues = {7,6,5,3,1,2,4};
  private final static Scanner sc = new Scanner(System.in);

  // DONE
  public static void main(String[] args){
    System.out.println("WELCOME TO HAMMING CODE PROGRAM.");
    System.out.println("Note: Transmissions are sent as a combination of data bits and parity bits for error correction purposes.\n");

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

// NOT Done
  private static void doRx(){
    
    System.out.printf("PROTOCOL:\n"
                      + "1. You enter the entire binary string in one line.\n"
                      + "1. Each %d-bit chunk of Hamming code is calculated for error.\n"
                      + "2. Error is indicated, and code corrected.\n"
                      + "3. Corrected code is converted back to plaintext.\n\n"
                      numParityBits+numDataBits
                      );
    
    try{
      System.out.println("Enter each %d-bit Hamming code in a new line. Enter a blank line when done.");
      String[][] charRxChunks = userInput().getBytes();
    } catch(Exception e){
      System.err.println(e.getMessage());
      System.err.println("ERROR: Unsupported character entered. Returning to main menu.");
      return;
    }

    System.out.println("\nTransmitting...");
    for(byte b:message) for(byte i=numChunks-1;i>=0;i--){
      byte data = b/Math.pow(2,numDataBits*i);
      byte parity = printHammingData(data,numDataBits);
      printHammingParity(parity,numParityBits);
      System.out.println();
      b %= Math.pow(2,numDataBits*i);
    }
  }
  
/* Tx METHODS */

  // DONE 
  private static void doTx(){
    byte input = optionsPrompt({
      String.format("[Basic] Convert a %d-tuple into a %d-tuple Hamming code.",numDataBits,numDataBits+NumParityBits),
      "[Advanced] Convert a plaintext message into Hamming code."
      });

    switch(input){
      case 0: doAdvancedTx(); break;
      case 1: doBasicTx(); break;
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
                      + "4. Each chunk is converted to %d bit Hamming code, and transmitted.\n\n"
                      numChunks,
                      numDataBits,
                      numParityBits+numDataBits);
    
    try{
      System.out.println("Enter your plaintext message:");
      byte[] message = userInput().getBytes();
    } catch(Exception e){
      System.err.println(e.getMessage());
      System.err.println("ERROR: Unsupported character entered. Returning to main menu.");
      return;
    }

    System.out.println("\nTransmitting...");
    for(byte b:message) for(byte i=numChunks-1;i>=0;i--){
      byte data = b/Math.pow(2,numDataBits*i);
      byte parity = printHammingData(data,numDataBits);
      printHammingParity(parity,numParityBits);
      System.out.println();
      b %= Math.pow(2,numDataBits*i);
    }
  }

  // DONE
  // Accepts a byte of same binary length as numDataBits
  // Recursive, returns parity -> printHammingParity() after
  private static byte printHammingData(byte b,byte numBitsLeft){
    if(numBitsLeft<=0) return systemParity;
    byte data = b%2, parity = printHammingData(b/2,numBitsLeft-1);
    System.out.print(data);
    return (bitValues[numDataBits-numBitsLeft]*data)^parity;
  }

  // DONE
  // Accepts a byte of same binary length as numParityBits
  // Recursive
  private static byte printHammingParity(byte b,byte numBitsLeft){
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
      }
      
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
    numParityBits = numberPrompt(2,4); // accomodates ASCII chars as bytes
    numDataBits = Math.pow(2,numParityBits)-numParityBits-1;
    numChunks = Math.ceil(8/numDataBits);
    resetBitValues();
  }

  // DONE
  private static void setNumTotalBits(){
    System.out.println("What's the new total Tx length?");
    byte total = numberPrompt(3,15); // accomodates ASCII chars as bytes: parity bits between [2,7]
    numParityBits = Math.floor(Math.log(total)/Math.log(2));
    numDataBits = total-numParityBits;
    numChunks = Math.ceil(8/numDataBits);
    resetBitValues();
  }
    
  // DONE
  private static void resetBitValues(){
    if(numDataBits+numParityBits>Math.pow(2,numParityBits)-1){
      System.err.print("DEVELOPER ERROR: The value 'numParityBits' is not large enough to support 'numDataBits'.");
      System.exit(1);
    }
    bitValues = new int[numParityBits+numDataBits];
    for(byte i=1,dIndex=numDataBits-1,pPow=0; dIndex>=0||pPow<bitValues.length;i++){
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
    return validNumberPrompt(0,options.length-1);
  }

  // DONE
  // Prompts for an integer within given range, and returns valid input
  private static byte validNumberPrompt(byte first, byte last){
    while(true){
      System.out.print("Enter a number between %d and %d: ",first,last);
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
  private static byte dataBitsPrompt(byte bitLength){
    String input = validBitStringPrompt(bitLength);
    byte byteValue=0;
    for(int i=0; i<input.length(); i++) byteValue = 2*byteValue + Byte.parseByte(input.charAt(i));
    return byteValue;
  }

  // DONE
  // Returns String of bits of given length
  private static String validBitStringPrompt(byte bitLength){
    while(true){
      boolean isValid = true;
      System.out.println("Enter an %d-bit token: ",bitLength);
      String input = userInput();
      if(input.length()!=bitLength){
        System.out.printf("Input must be length %d. Try again.\n",bitLength);
        continue;
      }
      for(int i=0; i<input.length(); i++){
        byte b = Byte.parseByte(input.charAt(i));
        if(b!=b%2){
          System.out.println("Input must be 1's and 0's. Try again.");
          isValid = false;
          break;
        }
      }
      if(isValid) return input;
    }
  }

  // DONE
  private static void printSystemStatus(){
    System.out.printf("CURRENT STATUS\n"
                      + "System parity: %s\n"
                      + "# of parity bits: %d\n"
                      + "# of data bits: %d\n"
                      + "Total Tx length: %d\n"
                      + "Value of each bit: %s\n",
                      
                      systemParity==1?"ODD":"EVEN",
                      numParityBits,
                      numDataBits,
                      numParityBits+numDataBits,
                      bitValues);
  }

    
}
