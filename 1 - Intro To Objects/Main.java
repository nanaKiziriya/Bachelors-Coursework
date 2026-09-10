public class Main{
    public static void main(String[] args) {
        JarOfMoney j1 = new JarOfMoney(5);
        j1.deposit(3);
        System.out.println(j1.getAmnt());
    }
}
