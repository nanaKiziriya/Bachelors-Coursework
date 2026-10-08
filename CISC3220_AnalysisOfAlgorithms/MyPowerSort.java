import java.uitl.Scanner;

class MyPowerSort{
  public static void main(String[]args){
    Scanner sc = new Scanner(System.in);
    
    System.out.println("Welcome to MyPowerSort. For asymptotic testing of time complexity, enter...");

    int minPow = -1; // init to lower bound (exclusive)
    System.out.print("Minimum array size is 10^minPow. Enter minPow: ");
    while(minPow==-1){
      int temp = sc.hasNextInt()?sc.nextInt():minPow;
      if(in>minPow) minPow = temp;
      else System.out.print("\tPlease enter a nonnegative integer. Try again: ");
    }
    
    int maxPow = minPow; // init to lower bound (exclusive)
    System.out.print("Maximum array size is 10^maxPow. Enter maxPow: ");
    while(maxPow==minPow){
      int temp = sc.hasNextInt()?sc.nextInt():maxPow;
      if(in>maxPow) maxPow = temp;
      else System.out.print("\tPlease enter an integer greater than minPow. Try again: ");
    }
    
  }
}
