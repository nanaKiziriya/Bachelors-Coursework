/* DRIVER */
public class ComplexNumConverter{
    // convert between general (cartesian), polar, and exponential (Euler's formula) form for complex numbers
    public static void main(String[]args){
        //driver
    }
}

/* UTILS */
public class ComplexMath{
    // tolerance
    final static int numToleranceDigits = 1; // pick how many least-significant-digit places can be diffrent to make up for floating point error
    final static double tolerance = Math.pow(10,numToleranceDigits);
    final static double pi = Math.PI;

    // equals depends on tolerance
    public static boolean equalLinear(double n, double m){ return (n-m)/tolerance==0; }
    public static boolean equalRadian(double n, double m){ return arc(n-m)/tolerance==0; }

    // calculations
    public static double real(double radius, double radians){ return radius*Math.cos(radians); }
    public static double imaginary(double radius, double radians){ return radius*Math.sin(radians); }
    public static double radius(double real, double imaginary){ return Math.sqrt(Math.pow(real,2)+Math.pow(imaginary,2)); }
    public static double radians(double real, double imaginary){ return Math.atan2(imaginary,real); }
    public static double piRadians(double real, double imaginary){ return radians(real,imaginary)/pi; }
    
    public static double arc(double d){ return d%(2*pi); }
    public static double modSumDifference(ComplexNumber c, ComplexNumber d){
        return c.getModulus() + d.getModulus() - (c.clone().add(d)).getModulus();
    }
}


/* ComplexNumber Object Classes */
abstract class ComplexNumber implements Cloneable{

    public static void printCurrentStackTrace(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        System.out.println("Current stack trace:");
        for(StackTraceElement e : stackTraceElements){ System.out.println(e.toString()); }
    }
    
    public ComplexNumber clone() throws CloneNotSupportedException{ return (ComplexNumber)super.clone(); }
    public boolean equals(ComplexNumber that){
        return (ComplexMath.equalLinear(getReal(),that.getReal()) && ComplexMath.equalLinear(getImaginary(),that.getImaginary()))
            || (ComplexMath.equalLinear(getRadius(),that.getRadius()) && ComplexMath.equalRadian(getRadians(),that.getRadians()));
    }

    abstract public void add(ComplexNumber that);
    abstract public void subtract(ComplexNumber that);
    abstract public void multiply(ComplexNumber that);
    abstract public void divide(ComplexNumber that);

    public void multiplyScalar(double d){ setRadius(d*getRadius()); }

    abstract public double getReal();
    abstract public double getImaginary();    
    abstract public double getRadius();
    abstract public double getRadians();
    abstract public double getPiRadians();
    public double getModulus(){ return getRadius(); }

    abstract public void setReal(double d);
    abstract public void setImaginary(double d);
    abstract public void setRadius(double d);
    abstract public void setRadians(double d);
    abstract public void setPiRadians(double d);
    public void setModulus(double d){ setRadius(d); }
    public void normalize(){ setRadius(1); }

    public ComplexNumber getNormalized(){ return new clone().normalize(); }
    abstract public ComplexNumber getConjugate();
    public double getConjugateProduct(){ return Math.pow(getRadius(),2); }
    
    public GenComplex toGenComplex(){ return new GenComplex(getReal(),getImaginary()); }
    public ExpComplex toExpComplex(){ return new ExpComplex(getRadius(),getRadians()); }
    public PlrComplex toPlrComplex(){ return new PlrComplex(getRadius(),getRadians()); }
    
    abstract public String toString();
}

class GenComplex extends ComplexNumber{
    // datafields
    private double real=0, imaginary=0;
    
    // constructors
    GenComplex(){}
    GenComplex(double real, double imaginary){
        this.real = real;
        this.imaginary = imaginary;
    }
    GenComplex(ComplexNumber that){
        this(that.getReal(),that.getImaginary());
    }
    
    // methods
    public void add(ComplexNumber that){
        real += that.getReal();
        imaginary += that.getImaginary();
    }
    public void subtract(ComplexNumber that){
        real -= that.getReal();
        imaginary -= that.getImaginary();
    }
    public void multiply(ComplexNumber that){
        real = real*that.getReal() - imaginary*that.getImaginary();
        imaginary = real *that.getImaginary() + imaginary*that.getReal();
    }
    public void divide(ComplexNumber that){
        if(ComplexMath.equalLinear(that.getRadius(),0)){
            System.err.printf("/ by zero error: attempted %s / %s",this.toString(),that.toString());
            printCurrentStackTrace();
            System.exit(1);
        } else{
            multiply(that.getConjugate()); // multiplies numerator
            multiplyScalar(1/that.getConjugateProduct()); // divide by (double)denominator, NOT recursive
        }
    }

    
    public double getReal(){ return real; }
    public double getImaginary(){ return imaginary; }
    public double getRadius(){ return ComplexMath.radius(real,imaginary); }
    public double getRadians(){ return ComplexMath.radians(real,imaginary); }
    public double getPiRadians(){ return getRadians()/Math.PI; }

    
    public void setReal(double d){ real = d; }
    public void setImaginary(double d){ imaginary = d; }
    public void setRadius(double d){
        double scalingFactor = d/getRadius();
        real *= scalingFactor;
        imaginary *= scalingFactor;
    }
    public void setRadians(double d){
        real = ComplexMath.real(getRadius(),d);
        imaginary = ComplexMath.imaginary(getRadius(),d);
    }
    public void setPiRadians(double d){ setRadians(d*Math.PI); }
    
    @Override
    public GenComplex getConjugate(){ return new GenComplex(real,-1*imaginary); }
    @Override
    public String toString(){ return String.format("%f+%fi",real,imaginary); }
}

class PlrComplex extends ComplexNumber{
    // datafields
    private double radius=0, radians=0;
    
    // constructors
    PlrComplex(){}
    PlrComplex(double radius, double radians){
        this.radius = radius;
        this.radians = ComplexMath.arc(radians);
    }
    PlrComplex(ComplexNumber that){
        this(that.getRadius(),that.getRadians());
    }
    
    // methods
    public void add(ComplexNumber that){
        double real = getReal()+that.getReal(), imaginary = getImaginary()+that.getImaginary();
        radius = ComplexMath.radius(real,imaginary);
        radians = ComplexMath.radians(real,imaginary);
    }
    public void subtract(ComplexNumber that){
        double real = getReal()-that.getReal(), imaginary = getImaginary()-that.getImaginary();
        radius = ComplexMath.radius(real,imaginary);
        radians = ComplexMath.radians(real,imaginary);
    }
    public void multiply(ComplexNumber that){
        radius *= that.getRadius();
        radians = ComplexMath.arc(radians+that.getRadians());
    }
    public void divide(ComplexNumber that){
        if(ComplexMath.equalLinear(that.getRadius(),0)){
            System.err.printf("/ by zero error: attempted %s / %s",this.toString(),that.toString());
            printCurrentStackTrace();
            System.exit(1);
        } else{
            radius /= that.getRadius();
            radians = ComplexMath.arc(radians-that.getRadians());
        }
    }
    
    public double getRadius(){ return radius; }
    public double getRadians(){ return radians; }
    public double getPiRadians(){ return radians/Math.PI; }
    public double getReal(){ return ComplexMath.real(radius,radians); }
    public double getImaginary(){ return ComplexMath.imaginary(radius,radians); }

    public void setRadius(double d){ radius = d; }
    public void setRadians(double d){ radians = ComplexMath.arc(d); }
    public void setPiRadians(double d){ radians = ComplexMath.arc(d*Math.PI); }
    public void setReal(double d){
        radius = ComplexMath.radius(d,getImaginary());
        radians = ComplexMath.radians(d,getImaginary());
    }
    public void setImaginary(double d){
        radius = ComplexMath.radius(getReal(),d);
        radians = ComplexMath.radians(getReal(),d);
    }
    
    @Override
    public PlrComplex getConjugate(){ return new PlrComplex(radius,-1*radians); }
    @Override
    public String toString(){ return String.format("%f,%fpi",radius,getPiRadians()); }
}

class ExpComplex extends PlrComplex{
    // datafields inherited from superclass
    
    // constructors
    ExpComplex(){
        super();
    }
    ExpComplex(double radius, double radians){
        super(radius, radians);
    }
    ExpComplex(ComplexNumber that){
        super(that);
    }
    
    // methods
    @Override
    public ExpComplex getConjugate(){ return new ExpComplex(getRadius(),-1*getRadians()); }
    @Override
    public String toString(){
        return String.format("%fexp(i*%f)",getRadius(),getPiRadians());
    }
}


/* Matrix Object Classes */
public class Matrix{
    
    public static Matrix zero(int m,int n){
        double[][] id = new double[m][n];
        java.util.Arrays.fill(id,0);
    }
    public static Matrix identity(int m,int n){
        double[][] id = zero(m,n);
        for(int i=0;i<Math.min(m,n);i++){ id[i][i]=1; }
        return id;
    }

    private double[][] matrix;


