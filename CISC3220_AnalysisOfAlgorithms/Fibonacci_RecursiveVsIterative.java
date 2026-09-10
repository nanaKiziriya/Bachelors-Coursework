public class Fibonacci{

  public static int recursive(int f0, int f1, int n){
    if(n<0) return 0;
    else if(n==0) return f0;
    else if(n==1) return f1;
    else return recursive(f1,f0+f1,n-1);
  }

  public static int iterative(int f0, int f1, int n){
    if(n<0) return 0;
    else if(n==0) return f0;
    else if(n==1) return f1;
    //else
    int f[] = {f0,f1};
    for(int i=2; i<=n; i++) f[i%2] = f[0]+f[1];
    return f[n%2];
  }
}
