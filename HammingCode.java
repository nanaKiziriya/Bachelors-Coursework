public class HammingCode{

  private static byte systemParity = 1; // Either even (0) or odd (10. Standard is odd (1), meaning xor all bit values results in all 1's
  private static int numDataBits = 4; // Math.pow(2,numParityBits)-numParityBits-1
  private static int numParityBits = 3; // parity bits are powers of 2, starting from 1
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
    
      int input = inputOptions(options);

      switch(input){
        case 0: sc.close(); System.exit(0);
        case 1: changeSystem; break;
        case 2: doTx(); break;
        case 3: doRx(); break;
      }
    }
  }

  

  private static void setSystemStatus(){
    String[] options = {
      "Switch system parity. This affects whether all values XOR to 0 (even) or 1 (odd).",
      "Change # of parity bits. This also sets # of data bits to max possible value.",
      "Change total Tx length. Tx includes both parity and data bits."
    }
    
    int input = inputOptions(options);

    switch(input){
      case 1:
        
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

  static uiBinTup();


  /* HELPER METHODS */

  // DONE
  // Underlies all user inputs
  private static String userInput(){
    System.out.print(" > ");
    return sc.nextLine();
  }

  // DONE
  // Lets user choose btwn options provided
  private static int inputOptions(String[] options){
    int first = 0, last = options.length-1;

    System.out.println("What would you like to do?");
    for(int i=0; i<options.length; i++)
      System.out.printf("%d - %s\n",i,options[i]);

    while(true){
      System.out.println(" Enter a number between %d and %d: ",first,last);
      try{
        int input = Integer.parseInt(userInput());
        if(user<first||user>last) System.out.println("Input must be an available option.");
        else return input;
      } catch(Exception e){
        System.out.println("Input must be an integer. Try again.");
      }
    }
  }
  
    
}
