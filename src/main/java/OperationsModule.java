import java.sql.*;
import java.io.*;


public class OperationsModule {

    static final private String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final private String DB_URL = "jdbc:mysql://localhost/Users?useSSL=false";
    static final private String USER   = "root";
    //static final private String PASS   = USER;
    static private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static float add (float a, float b)
    { return (a+b); }
    private static float multiply(float a , float b)
    { return (a*b); }
    private static double divide(float a , float b) {
        try{
            return (a/b);
        }catch(ArithmeticException ae)
        { return 0;}
    }
    private static float sub (float a , float b)
    { return (a-b);}
    private static int mod (int a, int b)
    {
        try{
            return (a%b);
        }catch(ArithmeticException ae)
        {  return 0;}
    }

    private static void getMenu()
    {
        System.out.println("<---       Calculator          --->");
        System.out.println("<--- Specify your choice --->");
        System.out.println("1.    ADD");
        System.out.println("2.    SUBTRACT");
        System.out.println("3.    MULTIPLY");
        System.out.println("4.    DIVIDE");
        System.out.println("5.    ABSOLUTE");
        System.out.println("6.    MOD");
        System.out.println("7.    MAX");
        System.out.println("8.    MIN");
        System.out.println("Press the number of choice or press X|x to exit: ");

    }

    private static float getOperandFloat(String position)
    {
        try {
            System.out.println("Enter"+position+" operand : ");
            return  (Float.parseFloat(br.readLine()));
        }catch(Exception ae )
        { return 1.0F;}

    }
    private static int getOperandInt(String position)
    {
        try {
            System.out.println("Enter"+position+" operand : ");
            return  (Integer.parseInt(br.readLine()));
        }catch(Exception ie)
        { return 1;}

    }

    private static boolean addToDatabase(Connection con, float leftOperand, float rightOperand, String Operator, double result) {

        PreparedStatement ps=null;
        try {

            String InsertQuery = "insert into calculator(left_operand,right_operand,operator,result)" +
                    "values (?,?,?,?);";
            ps = con.prepareStatement(InsertQuery);
            ps.setFloat(1, leftOperand);
            ps.setFloat(2, rightOperand);
            ps.setString(3, Operator);
            ps.setDouble(4, result);
            return (ps.execute());
        } catch (Exception e) {
            System.out.println("Error:  during Query Execution");
            return false;
        }finally {
            try{
                if(ps!=null)
                {ps.close();}
            }catch(SQLException se)
            {se.getSQLState();}

        }
    }
    private static void ShowTable(Connection con ,String operator) {
        PreparedStatement ps = null;
        try {
            String SelectQuery = "SELECT * FROM calculator WHERE operator = ?";
            ps = con.prepareStatement(SelectQuery);
            ps.setString(1, operator);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getTimestamp("time") + "   |   " + rs.getFloat("left_operand") + "   |   " + rs.getFloat("right_operand")
                        + "   |   " + rs.getString("operator") + "   |   " + rs.getFloat("result"));
            }
        } catch (Exception e) {
            System.out.println("Error:  during Query Execution "+e.getMessage());

        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException se) {
                se.getSQLState();
            }
        }
    }


    public static void main(String[] args) throws IOException {

        String choice;
        String operate;
        float a;
        float b;
        double result;
        Connection con = null;
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_URL, USER, USER);
            do {

                getMenu();
                choice = br.readLine();
                switch (choice) {
                    case "1": {
                        a = getOperandFloat("Left");
                        b = getOperandFloat("Right");
                        result = add(a, b);
                        operate = "+";
                        if (addToDatabase(con, a, b, operate, result)) {
                            choice = "x";
                        }
                        break;
                    }
                    case "2": {
                        a = getOperandFloat("Left");
                        b = getOperandFloat("Right");
                        result = sub(a, b);
                        operate = "-";
                        if (addToDatabase(con, a, b, operate, result)) {
                            choice = "x";
                        }
                        break;
                    }
                    case "3": {
                        a = getOperandFloat("Left");
                        b = getOperandFloat("Right");
                        result = multiply(a, b);
                        operate = "*";
                        if (addToDatabase(con, a, b, operate, result)) {
                            choice = "x";
                        }
                        break;
                    }
                    case "4": {
                        a = getOperandFloat("Left");
                        b = getOperandFloat("Right");
                        result = divide(a, b);
                        operate = "/";
                        if (addToDatabase(con, a, b, operate, result)) {
                            choice = "x";
                        }
                        break;
                    }
                    case "5": {
                        a = getOperandFloat("");
                        b = 0.0f;
                        result = Math.abs(a);
                        operate = "ABS";
                        if (addToDatabase(con, a, b, operate, result)) {
                            choice = "x";
                        }
                        break;
                    }
                    case "6": {
                        a = getOperandInt("Left");
                        b = getOperandInt("Right");
                        result = mod((int) a, (int) b);
                        operate = "MOD";
                        if (addToDatabase(con, a, b, operate, result)) {
                            choice = "x";
                        }
                        break;
                    }
                    case "7": {
                        a = getOperandFloat("Left");
                        b = getOperandFloat("Right");
                        result = Math.max(a, b);
                        operate = "MAX";
                        if (addToDatabase(con, a, b, operate, result)) {
                            choice = "x";
                        }
                        break;
                    }
                    case "8": {
                        a = getOperandFloat("Left");
                        b = getOperandFloat("Right");
                        result = Math.min(a, b);
                        operate = "MIN";
                        if (addToDatabase(con, a, b, operate, result)) {
                            choice = "x";
                        }
                        break;
                    }
                    case "X": {
                    }
                    case "x": {
                        choice = "x";
                        break;
                    }
                    default: {
                        System.out.println("Choose a valid option <1-7>");
                        break;
                    }
                }
               // System.out.println(choice);

            } while (!"x".equalsIgnoreCase(choice));

            System.out.println("Which operator's tuple would you like to see : ");
            getMenu();
            choice = br.readLine();
            switch (choice) {
                case "1":
                    choice = "+";
                    break;
                case "2":
                    choice = "-";
                    break;
                case "3":
                    choice = "*";
                    break;
                case "4":
                    choice = "/";
                    break;
                case "5":
                    choice = "ABS";
                    break;
                case "6":
                    choice = "MOD";
                    break;
                case "7":
                    choice = "MIN";
                    break;
                case "8":
                    choice = "MAX";
                    break;
                default:
                    choice = "NO";
                    break;
            }
            if (!choice.equalsIgnoreCase("NO")) {
                ShowTable(con,choice);
            }
        }catch(SQLException se)
        { se.printStackTrace();}
        catch(NullPointerException npe)
        {npe.getMessage();}
        catch(ClassNotFoundException ce)
        {ce.getCause();}
        finally {
            try {
                if (con != null) {
                    con.close();
                }

            }catch(NullPointerException npe)
            { npe.printStackTrace();}
            catch(SQLException sqe)
            {sqe.getMessage();}

        }
    }
}
