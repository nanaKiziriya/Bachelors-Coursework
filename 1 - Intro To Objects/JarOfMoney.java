class JarOfMoney{

  /* OVERVIEW
      Ideas to think about:
        static vs instance(no keyword) varibles/methods
            - does it depend on a specific object/instance, or it is more general?
        get (accessor) vs set (mutator) methods
          - these are associated with specific instance variables
          - the var is typically private, can't be changed willy nilly/cause logical error
          - control exactly what a user can do with a private variable
        syntax errors (compiler no likey) vs logical errors (human brain no likey)
  
      Classes can have:
        variables/datafields (static or instance)
        Constructors (instance)
        methods/functions (static or instance)
  */

  
  
  /* VARIABLES/DATAFIELDS */
  
  // instance variables/datafields
  private int amnt=0;

  // static variables/datafields
  private static int numJars=0;


  
  /* CONSTRUCTORS
      the code that runs when you use the 'new' keyword
      makes a new instance of this class
      note the unique signature: nameOfClass(parameters)
      when you use the 'new' keyword, the compiler looks in the class for a constructor of the same type you wrote directly after
        - e.g. j1 = new JarOfMoney(50,"red");
        - the compiler looks for a constructor in class JarOfMoney with the parameters (int,String)
        - if it doesn't find it, it'll end the execution and tell you where hit an error
  */

  // no-argument constructor
  JarOfMoney(){
    numJars++; // increment static jar counter variable
  }

  // constructor with specific argument type
  JarOfMoney(int amnt){
    if(amnt>=0) this.amnt=amnt; // logical error: can't have negative money!
    numJars++; // increment static jar counter variable
  }


  
  /* METHODS */
  
  // instance methods
  public int getAmnt(){ // get method for (private int amnt)
    return amnt;
  }

  public void deposit(int d){
    amnt+=d;
  }

  public boolean withdraw(int w){
    if(amnt<w){
      // can also print a msg like "Oops! I don't have $(w) in this jar"
      return false; // indicate failure
    } else{ // syntax error: 'else' instead of 'if(amnt>=w)' to let compiler know a return statement will always be reached
      amnt-=w;
      return true; // indicate success
    }
  }   

  // static methods
  public static int getNumJars(){ // get method for (private static int numJars)
    return numJars;
  }
  
}
