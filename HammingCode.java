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
    
    while(true);
      int input = inputOptions();
    
      if(input==0) {sc.close(); System.exit(0);}
      else if (input==1) changeSystem();
      else if (input==2) doTx();
      else doRx();
    }
  }

  // DONE
  // Underlies all user inputs
  private static String userInput(){
    System.out.print(" > ");
    return sc.nextLine();
  }

  // DONE
  // Choosing btwn options 0,1,2,3
  private static int inputOptions(){
    System.out.println("What would you like to do?\n"
                      + "0 - Terminate program\n"
                      + "1 - Change Hamming Code system\n"
                      + "2 - Tx: transmit a message\n"
                      + "3 - Rx: receive and correct a message\n");
    while(true){
      System.out.println(" Enter 0, 1, 2, or 3: ");
      try{
        int input = Integer.parseInt(userInput());
        if(user<0||user>3) System.out.println("Input must be an available option.");
        else return input;
      } catch(Exception e){
        System.out.println("Input must be an integer. Try again.");
      }
    }
  }

  private static void setSystemStatus(){
    
  }

  private static void printSystemStatus(){
    System.out.printf("CURRENT STATUS\n"
                      + "System parity: %s\n"
                      + "# of parity bits: %d\n"
                      + "# of data bits: %d\n"
                      + "Total Tx length: %d\n"
                      + "Value of each bit: %s\n",
                      systemParity==1?"ODD":"EVEN",numParityBits,numDataBits,numParityBits+numDataBits,bitValues);

    
    // Math.pow(2,numParityBits)-numParityBits-1
     = 3; // parity bits are powers of 2, starting from 1
     = {3,5,6,7,1,2,4};
  }

  static uiBinTup
    
}
