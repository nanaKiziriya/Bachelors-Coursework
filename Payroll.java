import java.util.Scanner;
 import java.io.File;
 import java.io.FileNotFoundException;
 import java.io.PrintWriter;

 public class Payroll {
         private static Scanner stdin;
         public static void main(String[] arg) throws FileNotFoundException {
                 stdin = new Scanner(System.in);
                 String hrFileName = getFileName("employee_file");
                // REJECT IN FAVOR OF config file:  promptAndReadString("Employee File");
                // MISTAKE: DO NOT HARD-CODE: String hrFileName = "employees";
                 String tcFileName = getFileName("timecard_file");
                 String chFileName = getFileName("check_file");
                 Employee[] employees = getEmployees(hrFileName);
                 TimeCard[] timeCards = getTimeCards(tcFileName);
                 Sort.sort(employees);
                 Money totalCost = doPayroll(employees,timeCards,chFileName);
                 System.out.println("Total cost of Payroll: "+totalCost);
         }
         private static Money doPayroll(Employee[] employees, TimeCard[] timeCards,
                                         String chFileName) throws FileNotFoundException {
                 for (int i=0; i<timeCards.length; i++) {
                         TimeCard tc = timeCards[i];
                         Employee e = Employee.findEmployee(employees, tc.getID());
                         e.logHours(tc.getHours());
                 }
                 Money total = new Money(0,0);
                 PrintWriter pw = new PrintWriter(chFileName);
                 for (int i=0; i<employees.length; i++) {
                         Employee e = employees[i];
                         Money pay = e.calcPay();
                         total.add(pay);
                         Check.genCheck(pw, e.getName(), pay);
                 }
                 pw.close();
                 return total;
         }
         private static Employee[] getEmployees(String fn) throws FileNotFoundException {
                 Employee[] employees = new Employee[countLines(fn)];
                 Scanner s = new Scanner(new File(fn));
                 for (int i=0; i<employees.length; i++) employees[i] = Employee.read(s);
                 s.close();
                 return employees;
         }
         private static int countLines(String fn) throws FileNotFoundException {
                 int cnt=0;
                 Scanner s = new Scanner(new File(fn));
                 while (s.hasNextLine()) {s.nextLine();cnt++;}
                 s.close();
                 return cnt;
         }
         private static TimeCard[] getTimeCards(String fn) throws FileNotFoundException {
                 TimeCard[] timeCards = new TimeCard[countLines(fn)];
                 Scanner s = new Scanner(new File(fn));
                 for (int i=0; i<timeCards.length; i++) timeCards[i] = TimeCard.read(s);
                 s.close();
                 return timeCards;
         }
        private static String getFileName(String filetype) throws FileNotFoundException {
                File conf2File = new File("payroll2.config");
                if (conf2File.exists()) {
                        Scanner conf2 = new Scanner(conf2File);
                        String value = getValueOfKeyword(conf2,filetype);
                        conf2.close();
                        if (value!=null) return value;
                }
                return promptAndReadString("(not available from config file) Employee File");
        }
        private static String getValueOfKeyword(Scanner sc,String filetype) {
                while (sc.hasNextLine()) {
                        String line = sc.nextLine().trim();
                        if (line.length()>0 &&  !line.startsWith("#")) {
                                String[] elements = line.split(" *# *");
                                if (elements[0].equals(filetype)) {
                                        return elements[1];
                                }
                        }
                }
                return null;
        }
        private static String promptAndReadString(String s) {
                System.err.print(s+": ");
                return stdin.next();
        }
 }
