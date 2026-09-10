/*
OVERVIEW:
For Middle-Square method of generating pseudo-random numbers
This program priorities time efficiency over space efficiency: Avoids iterating ANY previously iterated elements
For each seed, it iterates through the orbit and calculates important info. After each completed orbit, it stores info and restarts.

Terminology:
seed - the initial value of a sequence
tail - the longest acyclic sequence of the orbit
tip - num. iterations until periodic point, or equivalently, longest sequence excluding periodic points (strictly shorter than tail)

This program:
Calculates the tail length of each seed
Finds the longest tail length and its seeds

Built-in functionality:
Keeps track of seed & tailLength in both directions -> seedTail, tailSeed

USER INPUTS starting at line 33:
firstSeed - first seed value to calculate sequ of; program will continue to calculate until largest seed of given seedLength
seedLength - controls length of the elements; Note: for middle-square, odd-lengths favor the left side of the middle
printSequ, firstRoot - booleans to det. what info you want to calcluate/display

*/

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.ArrayList;

class MidSqTester {
    
    // USER INPUTS
        // Seeds to iterate over
        static int firstSeed=0, seedLength=3;
        static int lastSeed =
            (int)Math.pow(10,seedLength)-1; // Automatically calculates largest seed of given length
        // Precursory Info:
        static boolean printTerminology = true;
        // During/after each orbit:
        static boolean printOrbitInfoOfEachSeed = false;
        // At very end:
        static int printL = 20; // for PrintAllTail/Tip
        static boolean printAllUniqueCycles = true;
        static boolean printMaxTailLength = false;
        static boolean printAllTailLengths = false;
        static boolean printMaxTipLength = true;
        static boolean printAllTipLengths = true;
    // END OF USER INPUTS
    
    public static void main(String[] args) {

        /* DATAFIELDS */
        
        // Holds long-term info
        HashMap<Integer,ArrayList<Integer>> seedInfo = new HashMap<>(); // <seed,{period(-1 if not periodic), tailLength, tipLength}>, keySet() holds all previous elements, helps avoid unnecessary repeats
        HashMap<Integer,TreeSet<Integer>> tailSeed = new HashMap<>(); // <tailLength,seedSet>
        HashMap<Integer,TreeSet<Integer>> tipSeed = new HashMap<>(); // <tipLength,seedSet>
        ArrayList<ArrayList<Integer>> uniqueCycles =  new ArrayList<>(); // holds cycles, no repeats
        
        int maxTailLength=0, maxTipLength=0; // holds largest tail,tip length
        
        // Reset for each orbit
        ArrayList<Integer> orbitHolder = new ArrayList<>(); // for each seed, holds orbit in order, helps calc (seed,tailL) pair quicker, reset empty per seed
        HashSet<Integer> cycleChecker = new HashSet<Integer>(); // for each seed, for quickly checking cycle, reset empty per seed
        ArrayList<Integer> cycleHolder = new ArrayList<>(); // when collecting cycles in uniqueCycles, holds the current cycle
        
        int tailLength=0, tipLength=0; // for each seed, holds its tail,tip length
        
        // For time efficiency: avoid iterating over ALL previously iterated elements
        int tempTail, tempTip, tempIndex, tailMin, flagIndex; // works with tempSeed; tempIndex, flagIndex instead of ArrayList.indexOf() for time efficiency
        boolean cycleCheckerFlag, seedFlag; // flags where the element was seen before, current orbit vs. previous orbit, affects tailLength calculations of same-cycle elements
        
        
        /* ALGORITHM */

        if(printTerminology){
            System.out.println("Terminology:");
            System.out.println("seed - the initial value of a sequence");
            System.out.println("tail - the longest acyclic sequence of the orbit");
            System.out.println("tip - num. iterations until periodic point, or equivalently, longest sequence excluding periodic points (strictly shorter than tail)");
            System.out.println();
        }

        // execute for each seed from firstSeed (user input) to lastSeed (999..9)
        System.out.printf("(Calculating orbits of all seeds from %d to %d...)\n",firstSeed,lastSeed);

        /*printOrbitInfoOfEachSeed, START*/
        if(printOrbitInfoOfEachSeed) System.out.println("Printing orbit info of each seed (format: orbit [period,tail,tip])");
        
        for(int i=firstSeed,j; i<=lastSeed; i++){
            
            // reset vals/prep for new seed orbit
            j=i; tailLength=tipLength=tempIndex=flagIndex=0;
            cycleChecker.clear();
            orbitHolder.clear();
            cycleHolder.clear();
            cycleCheckerFlag=seedFlag=false;
            
            while(true){ // for each root, continues orbit calc until hits repeat in either current orbit or some previous orbit

                // Only ONE of the following will be flagged in each new orbit
                cycleCheckerFlag=cycleChecker.contains(j); // if current orbit has a brand new periodic point/cycle
                seedFlag=seedInfo.keySet().contains(j); // if we've calculated and stored this as a seed before, may or may NOT be a periodic point
                
                if(!(cycleCheckerFlag||seedFlag)){ // if nothing is flagged
                    
                    /*printOrbitInfoOfEachSeed*/
                    if(printOrbitInfoOfEachSeed) System.out.print(j+" ");
                    
                    cycleChecker.add(j);
                    orbitHolder.add(j);
                } else {break;} // something got flagged; break this while loop, i.e. stop iterating on this orbit
                
                j=fnct(j);
            }
            
            // while loop break guaranteed since a flag is guaranteed to be hit
            // if cycleChecker is flagged (instead of seedTailFlag) that means its a NEW periodic point, thus every subsequent element has the same period/tailLength...
            // ... and the tempTail of the element that flagged cycleChecker is the sharp lower bound for tailLengths of all elements in the current 
            
            /*printOrbitInfoOfEachSeed*/
            if(printOrbitInfoOfEachSeed) System.out.print(j+"... ");
            
            /* Update seedInfo, tailSeed, tipSeed; time-not space-efficient*/

            // if cycleCheckerFlag then ==start of complete cycle in orbit, else if seedFlag then ==lastElementPlace in incomplete orbit
            flagIndex = orbitHolder.indexOf(j);

            // updating cycleHolder if new cycle detected
            if(cycleCheckerFlag){ // only if completely new orbit
                // .subList() is synced with original list; avoids erasure when orbitHolder is cleared
                cycleHolder = new ArrayList<>(orbitHolder.subList(flagIndex,orbitHolder.size()));

                /*printAllUniqueCycles, START*/
                if(printAllUniqueCycles) uniqueCycles.add(new ArrayList<>(cycleHolder)); // STORE cycle if you want to print later
            }
            
            // if orbit was prematurely stopped before all unique elements were calculated (seedFlag==true) account for lost length for tailLength
            tailLength = cycleChecker.size() + ((seedFlag)? seedInfo.get(j).get(1):0);

            // update tipLength
            if(cycleCheckerFlag){
                tipLength = tailLength-cycleHolder.size();
            } else{
                tipLength = cycleChecker.size() + seedInfo.get(j).get(2);
            }
            
            // Note, tailMin: If a non-fixed periodic point is hit (flagged cycleChecker in loop) every subsequent element has the exact same period/tailLength
            tailMin = (cycleCheckerFlag)? tailLength-orbitHolder.indexOf(j):0;
            
            // Note: int tempIndex = 0, here
            for(int tempSeed:orbitHolder){
                if(!seedInfo.keySet().contains(tempSeed)){ // if this seed hasn't been calculated and stored b4

                    seedInfo.put(tempSeed, new ArrayList<Integer>(3));
                    // Period: -1 if either seedFlag (last element accounted for already, and previous elements cannot be periodic), or...
                    // ... if (since cycleCheckerFlag already implied) element came strictly before j, the first periodic element in the orbit
                    seedInfo.get(tempSeed).add(((seedFlag||tempIndex<flagIndex)?-1:cycleHolder.size()));
                    
                    tempTail = Math.max(tailLength-tempIndex,tailMin);
                    seedInfo.get(tempSeed).add(tempTail);
                    
                    // if not already accounted for, any elements at or after j are periodic, i.e. tipLength=0 (no iterations needed to get to a cycle)
                    tempTip = Math.max(tipLength-tempIndex,0);
                    seedInfo.get(tempSeed).add(tempTip);
                    
                    if(!tailSeed.keySet().contains(tempTail)) tailSeed.put(tempTail,new TreeSet<>());
                    tailSeed.get(tempTail).add(tempSeed);

                    if(!tipSeed.keySet().contains(tempTip)) tipSeed.put(tempTip,new TreeSet<>());
                    tipSeed.get(tempTip).add(tempSeed);
                }
                tempIndex++;
            }

            /*printOrbitInfoOfEachSeed, DONE*/
            if(printOrbitInfoOfEachSeed) System.out.println(seedInfo.get(i).toString());
            
            // update maxTailLength and maxTipLength
            if(maxTailLength<tailLength) maxTailLength=tailLength;
            if(maxTipLength<tipLength) maxTipLength=tipLength;
            
            // RESTART for-loop again
        }
        
        // END OF ALL LOOPING
        
        // Print final information: Cycle,Tip,Tail Info
        
        /*printAllUniqueCycles, DONE*/
        if(printAllUniqueCycles){
            System.out.println("\nAll unique cycles:");
            for(ArrayList c:uniqueCycles){
                System.out.printf("%s (pd=%d)\n",c.toString(),seedInfo.get(c.get(0)).get(0));
            }
            System.out.println();
        }
        
        /*printMaxTailLength, START/DONE*/
        if(printMaxTailLength){
            System.out.printf("Largest tail length is %d with seed(s): ",maxTailLength);
            for(int n:tailSeed.get(maxTailLength)){
                System.out.print(n+" ");
            }
            System.out.println();
        }
        
        /*printAllTailLengths, START/DONE*/
        if(printAllTailLengths){
            System.out.print("Tail lengths in order:");
            for(int i=firstSeed;i<=lastSeed;i++){
                if(i%printL==firstSeed%printL) System.out.printf("\n(%d-%d) ",i,i+printL-1);
                System.out.printf("%d ",seedInfo.get(i).get(1));
            }
            System.out.println();
        }

        /*printMaxTipLength, START/DONE*/
        if(printMaxTipLength){
            System.out.printf("Largest tip length is %d with seed(s): ",maxTipLength);
            for(int n:tipSeed.get(maxTipLength)){
                System.out.print(n+" ");
            }
            System.out.println();
        }
        
        /*printAllTipLengths, START/DONE*/
        if(printAllTipLengths){
            System.out.print("Tip lengths in order:");
            for(int i=firstSeed;i<=lastSeed;i++){
                if(i%printL==firstSeed%printL) System.out.printf("\n(%d-%d) ",i,i+printL-1);
                System.out.printf("%d ",seedInfo.get(i).get(2));
            }
            System.out.println();
        }

    }
    
    public static int fnct(int n){
        int squareLength = 2*seedLength;
        StringBuilder s = new StringBuilder(""+n*n);
        while(s.length()<squareLength){
            s.insert(0,"0");
        }
        
        int startIndex = (seedLength+1)/2;
        n=Integer.parseInt(s.substring(startIndex,startIndex+seedLength));
        return n;
    }
}
