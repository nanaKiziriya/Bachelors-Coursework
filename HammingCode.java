public class HammingCode{

  private final static byte systemParity = 1; // Either even (0) or odd (10. Standard is odd (1), meaning xor all bit values results in all 1's
  private final static int numDataBits = Math.pow(2,numParityBits)-numParityBits-1;
  private final static int numParityBits = 3;
  private final static int[] bitValues;
  private final static Scanner sc = new Scanner(System.in);

  // DONE
  public static void main(String[] args){
    System.out.printf("WELCOME TO HAMMING CODE PROGRAM. Current status: Hamming(%d,%d)\n\n",numDataBits+numParityBits,numParityBits);
    System.out.println("Note: Transmissions are sent as a combination of data bits and parity bits, for error correction purposes.\n");
    
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
  static String userInput(){
    System.out.print(" > ");
    return sc.nextLine();
  }

  // DONE
  // Choosing btwn options 0,1,2,3
  static int inputOptions(){
    System.out.println("What would you like to do?\n"
                      + "0 - terminate program\n"
                      + "1 - change Hamming system\n"
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

  static uiBinTup
    
}
