/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author admin
 */
import java.sql.*;
public class IVProblem {

    public void inicializar()
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String deletedatos="DELETE FROM Balam.Rutas";
    String deletedatos1="DELETE FROM Balam.MatrizSolucion";
    String deletedatos2="DELETE FROM Balam.ResultadosIVP";
    String deletedatos3="DELETE FROM Balam.EstructuraTabu";
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    //eliminacion de la tabla
    stmt.executeUpdate(deletedatos);
    stmt.executeUpdate(deletedatos1);
    stmt.executeUpdate(deletedatos2);
    stmt.executeUpdate(deletedatos3);
    }
    catch(ClassNotFoundException e){
        System.out.println("no se encontro la clase \n"+
                "error ClassNotFoundException");
        System.out.println(e.toString());
    }
    catch(SQLException e){
        System.out.println("error de conexion \n"+
                "error SQLException");
        System.out.println(e.toString());
    }
   }

    public void inicializar2(int arr[])
    {
        String url = "jdbc:derby://localhost:1527/PDPIVP";
        String insertdatos="INSERT INTO Balam.Rutas (Indice, RutaOficial, RutaTabu,"+
            " RutaParcial, CostoOficial, CostoTabu, CostoParcial, "+
            "CostoBase, Diferencia, Tabu) VALUES (?,?,?,?,'0','0','0','0','0',0)";
        
        try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    PreparedStatement updateSales = con.prepareStatement(insertdatos);
        for (int i=0;i<arr.length;i++){
            updateSales.setInt(1, i);
            updateSales.setInt(2, arr[i]);
            updateSales.setInt(3, arr[i]);
            updateSales.setInt(4, arr[i]);
            updateSales.executeUpdate();
        }
    
    }
    catch(ClassNotFoundException e){
        System.out.println("no se encontro la clase \n"+
                "error ClassNotFoundException");
        System.out.println(e.toString());
    }
    catch(SQLException e){
        System.out.println("error de conexion \n"+
                "error SQLException");
        System.out.println(e.toString());
    }
   }

    public void inicializar3(int indice1, int indice2)
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Balam.Rutas WHERE Indice = "+indice1;
    String selectdatos1="SELECT * FROM Balam.Rutas WHERE Indice = "+indice2;
    int parametro1=0;
    String parametro1h="Lugar";
    int parametro2=0;
    String distancia;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1 = con1.createStatement();
    rs.next();
    parametro1=rs.getInt("RutaTabu");
    rs = stmt.executeQuery(selectdatos1);
    rs.next();
    parametro2=rs.getInt("RutaTabu");
    rs = stmt.executeQuery("SELECT * FROM Balam.Distancias WHERE IDOrigen = "+
            parametro2);
    parametro1h+=parametro1;
    rs.next();
    distancia=rs.getString(parametro1h);
    //System.out.println("distancia linea = "+parametro2+" columna "+parametro1h+
      //      " distancia = "+distancia+" indice = "+indice2);
    stmt1.executeUpdate("UPDATE Balam.Rutas SET CostoOficial = '"+distancia+
                "' WHERE Indice = "+indice2);
    stmt1.executeUpdate("UPDATE Balam.Rutas SET CostoTabu = '"+distancia+
                "' WHERE Indice = "+indice2);
    stmt1.executeUpdate("UPDATE Balam.Rutas SET CostoParcial = '"+distancia+
                "' WHERE Indice = "+indice2);
    stmt1.executeUpdate("UPDATE Balam.Rutas SET CostoBase = '"+distancia+
                "' WHERE Indice = "+indice2);
    }
    catch(ClassNotFoundException e){
        System.out.println("no se encontro la clase \n"+
                "error ClassNotFoundException");
        System.out.println(e.toString());
    }
    catch(SQLException e){
        System.out.println("error de conexion \n"+
                "error SQLException");
        System.out.println(e.toString());
    }
   }

    public void inicializar4(int arr[])
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Balam.Rutas";
    String insertdatos="INSERT INTO Balam.ResultadosIVP (ResultadoFinal, ResultadoTabu,"+
            " indice) VALUES ('";
    double resultado=0;
    String res="";
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
        for (int i=0;i<arr.length;i++){
            rs.next();
            resultado+=Double.parseDouble(rs.getString("CostoBase"));
        }
    res=String.valueOf(resultado);
    System.out.println("antes del update res="+res);
    insertdatos+=res+"','"+res+"',1)";
    stmt.executeUpdate(insertdatos);
    }
    catch(ClassNotFoundException e){
        System.out.println("no se encontro la clase \n"+
                "error ClassNotFoundException");
        System.out.println(e.toString());
    }
    catch(SQLException e){
        System.out.println("error de conexion \n"+
                "error SQLException");
        System.out.println(e.toString());
    }
   }

    public void inicializar5()
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String insertdatos="INSERT INTO Balam.EstructuraTabu (ID, Indice1,"+
            " Indice2, Tabu) VALUES (?,0,0,0)";
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    PreparedStatement updateSales = con.prepareStatement(insertdatos);
        for (int i=0;i<3;i++){
            updateSales.setInt(1, i+1);
            updateSales.executeUpdate();
        }
    }
    catch(ClassNotFoundException e){
        System.out.println("no se encontro la clase \n"+
                "error ClassNotFoundException");
        System.out.println(e.toString());
    }
    catch(SQLException e){
        System.out.println("error de conexion \n"+
                "error SQLException");
        System.out.println(e.toString());
    }
   }
}
