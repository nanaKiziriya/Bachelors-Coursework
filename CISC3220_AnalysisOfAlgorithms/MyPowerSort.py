###############################################
# FIND THIS CODE AND RUN IT EASILY AT
# 
###############################################
print('Welcome to MyPowerSort. For asymptotic testing of time complexity, enter...');

minPow = -1; // init to lower bound (exclusive)
print("MINIMUM array size is 10^minPow. Enter minPow: ");
while(minPow==-1){
  int temp = sc.hasNextInt()?sc.nextInt():minPow;
  if(in>minPow) minPow = temp;
  else System.out.print("Please enter a nonnegative integer. Try again: ");
}

int maxPow = minPow; // init to lower bound (exclusive)
System.out.print("MAXIMUM array size is 10^maxPow. Enter maxPow: ");
while(maxPow==minPow){
  int temp = sc.hasNextInt()?sc.nextInt():maxPow;
  if(in>maxPow) maxPow = temp;
  else System.out.print("\tPlease enter an integer greater than minPow. Try again: ");
}

for(int i=minPow; i<=maxPow; i++){
  // generate random array of size 10^i
  int n = 
  int arr[] = new int[Math.pow(10,i)];
  for(int j=0; j<
  // start time
  // sort using method
  // end time
  // confirm sorted with simple method
}

// TODO: Fix timetestshell:
int size = 1000;
Random r = new Random();
int[] arr2 = r.ints(10,0,size).toArray();
int[] arrN = arr2.clone();

long startTime = System.nanoTime();
// test 1
long endTime = System.nanoTime();
long timeElapsed2Split = endTime - startTime;
System.out.println("test 1: " + timeElapsed2Split); // in nanoseconds

startTime = System.nanoTime();
// test 2
endTime = System.nanoTime();
long timeElapsedNSplit = endTime - startTime;
System.out.println("test 2: " + timeElapsedNSplit); // in nanoseconds

