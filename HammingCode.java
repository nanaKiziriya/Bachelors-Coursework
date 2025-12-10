public class HammingCode{

  private static byte systemParity = 1; // Either even (0) or odd (10. Standard is odd (1), meaning xor all bit values results in all 1's
  private static int numDataBits = 4; // Math.pow(2,numParityBits)-numParityBits-1
  private static int numParityBits = 3; // Must be <= Math.log(Integer.MAX)/Math.log(2)
  private static int[] bitValues = {3,5,6,7,1,2,4};
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
    
      int input = optionsPrompt(options);

      switch(input){
        case 0: sc.close(); System.exit(0);
        case 1: setSystemStatus(); break;
        case 2: doTx(); break;
        case 3: doRx(); break;
      }
    }
  }

/* Tx METHODS */

  // DONE
  private static void doTx(){
    System.out.printf("PROTOCOL:\n"
                      + "1. You enter a plaintext message in one line.\n"
                      + "1. Each ASCII character is converted to 8-bit binary.\n"
                      + "2. Each one is turned into %d %d-bit chunk(s).\n"
                      + "3. Each chunk is converted to %d bit Hamming code, and transmitted.\n\n"
                      Math.ceil(8/numDataBits),
                      numDataBits,
                      numParityBits+numDataBits);
    
    try{
      System.out.println("Enter your plaintext message:");
      byte[] message = sc.nextLine().getBytes();
    } catch(Exception e){
      System.err.println(e.getMessage());
      System.err.println("ERROR: Unsupported character entered. Returning to main menu.");
      return;
    }

    System.out.println("\nTransmitting...");
    for(byte b:message) for(int i=){
      printHammingCode(b/16);
    }
  }

  // Accepts a byte of same binary length as numDataBits
  private static void printHammingCode(byte b){
    
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
    System.out.println("Enter the new number of parity bits.");
    numParityBits = numberPrompt(2,Math.floor(Math.log(Integer.MAX_VALUE)/Math.log(2))); // positive integer < log(MAX)/log(2)
    numDataBits = Math.pow(2,numParityBits)-numParityBits-1;
    resetBitValues();
  }

  // DONE
  private static void setNumTotalBits(){
    System.out.println("Enter the new total Tx length.");
    int total = numberPrompt(3,Integer.MAX_VALUE-1);
    numParityBits = Math.floor(Math.log(total)/Math.log(2));
    numDataBits = total-numParityBits;
    resetBitValues();
  }
    
  // DONE
  private static void resetBitValues(){
    if(numDataBits+numParityBits>Math.pow(2,numParityBits)-1){
      System.err.print("DEVELOPER ERROR: The value 'numParityBits' is not large enough to support 'numDataBits'.");
      System.exit(1);
    }
    bitValues = new int[numParityBits+numDataBits];
    for(int i=1,dIndex=0,pPow=0; dIndex<numDataBits||pPow<bitValues.length;i++){
      if(i==Math.pow(2,pPow)){
        bitValues[bitValues.length-numParityBits+pPow]=i;
        pPow++;
      }else{
        bitValues[dIndex]=i;
        dIndex++;
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
  private static int optionsPrompt(String[] options){
    System.out.println("What would you like to do?");
    for(int i=0; i<options.length; i++) System.out.printf("%d - %s\n",i,options[i]);
    return numberPrompt(0,options.length-1);
  }

  // DONE
  // Prompts for an integer within given range, and returns valid input
  private static int numberPrompt(int first){ return numberPrompt(first,Integer.MAX); }
  private static int numberPrompt(int first, int last){
    while(true){
      System.out.print(" Enter a number between %d and %d: ",first,last);
      try{
        int input = Integer.parseInt(userInput());
        if(user<first||user>last) System.out.println("Input must be an valid/available option. Try again.");
        else return input;
      } catch(Exception e){
        System.out.println("Input must be an integer. Try again.");
      }
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
