/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author admin
 */
import java.sql.*;
public class PDProblem {

    // recupera los valores de los clientes de la tabla
    // clientes y los escribe en la tabla clientestemp
    // borrando todo dato anterior y escribiendo los nuevos
    public void inicializar(int cliente)
    {
    int datoscliente[]=new int[5];
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String select_hjk = "SELECT * FROM Balam.CLIENTES WHERE IDCliente = "+ cliente;
    String insertdatos="INSERT INTO Balam.ClientesTemp (IDCliente, pjt, Inv_inicial,"+
            " Consumo, hjk) VALUES (?,?,?,?,?)";
    String deletedatos="DELETE FROM Balam.ClientesTemp WHERE IDCliente="+cliente;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(select_hjk);
    rs.next();
    datoscliente[0]=rs.getInt("IDCliente");
    datoscliente[1]=rs.getInt("pjt");
    datoscliente[2]=rs.getInt("Inv_inicial");
    datoscliente[3]=rs.getInt("Consumo");
    datoscliente[4]=rs.getInt("hjk");
    //eliminacion del primer valor en la tabla
    stmt.executeUpdate(deletedatos);
    //ingreso del nuevo valor al final de la tabla
    PreparedStatement updateSales = con.prepareStatement(insertdatos);
    updateSales.setInt(1, datoscliente[0]);
    updateSales.setInt(2, datoscliente[1]);
    updateSales.setInt(3, datoscliente[2]);
    updateSales.setInt(4, datoscliente[3]);
    updateSales.setInt(5, datoscliente[4]);
    updateSales.executeUpdate();

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

    // Borra la tabla de tiempo y resultados
    public void inicializar2()
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String deletedatos="DELETE FROM Tiempo";
    String deletedatos1="DELETE FROM Resultados";
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    stmt.executeUpdate(deletedatos);
    stmt.executeUpdate(deletedatos1);
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

    // genera la demanda diaria y costos e inicializa los datos de entrada
    // en la tabla Tiempo y clientesTemp
   public void inicializar3(int tiempo, int clientes)
   {
       pruebaDB obj = new pruebaDB();
       int arreglo[];
       int Inventario_inicial=0;
       int Inventario_final=0;
       int pjt=0;
       int hjk=0;
       int costo_inv=0;
       int consumo=0;
       int produccion=0;
       //conexion con la base
       String url = "jdbc:derby://localhost:1527/PDPIVP";
       try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    String insertdatos="UPDATE Balam.ClientesTemp SET Inv_inicial = ?"+
            " WHERE IDCliente = ?";
    String insertdatos2="INSERT INTO Balam.Tiempo (Indice, Tabu, Entregas,"+
            " Solucion, SolucionParcial, Cambios, ProdPropuesta,CdeInvdelCliente"+
            ",CInvPlanta,SolucionFinal,ResultadoTabu) VALUES (?,0,?,1,1,0,?,?,0,1,0)";
    PreparedStatement updateSales = con.prepareStatement(insertdatos);
    for (int i=0; i<clientes;i++){

           arreglo=obj.getCliente(i);
           pjt=arreglo[1];
           Inventario_inicial=arreglo[2];
           consumo=arreglo[3];
           hjk=arreglo[4];
           Inventario_final=Inventario_inicial-consumo;
           if(Inventario_final==0){
               produccion+=pjt;
               Inventario_final=pjt;
           }
          updateSales.setInt(1, Inventario_final);
          updateSales.setInt(2, i);
          updateSales.executeUpdate();
          costo_inv+=Inventario_final*hjk;

       }
    updateSales = con.prepareStatement(insertdatos2);
    updateSales.setInt(1, tiempo);
    updateSales.setInt(2, produccion);
    updateSales.setInt(3, produccion);
    updateSales.setInt(4, costo_inv);
    updateSales.executeUpdate();
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
        System.out.println(e.getStackTrace());
    }
   }

   //crea los resultados de la funcion objetivo iniciales
public int inicializar4(int tiempo)
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Balam.Tiempo";
    int costoinv=0;
    int costoinvglobal=0;
    int costoprod=0;
    int costoprodglobal=0;
    int resinicial=0;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
    
           for (int i=0; i<tiempo;i++){
               rs.next();
               costoprod=rs.getInt("ProdPropuesta");
               costoinv=rs.getInt("CdeInvdelCliente");
               costoprodglobal+=costoprod*30+3000;
               costoinvglobal+=costoinv;
               }
    resinicial=costoinvglobal+costoprodglobal;
    
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
    return resinicial;
   }

public void inicializar5(int resinicial)
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String insertdatos2="INSERT INTO Balam.Resultados (ResultadoFinal, ResultadoTabu, Indice)"+
            " VALUES (?,?,1)";
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    PreparedStatement updateSales = con.prepareStatement(insertdatos2);
    updateSales.setInt(1, resinicial);
    updateSales.setInt(2, resinicial);
    updateSales.executeUpdate();
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

public int inicializar6()
    {
    int datoscliente=0;
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String select_hjk = "SELECT * FROM Balam.RESULTADOS";
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(select_hjk);
    rs.next();
    datoscliente=rs.getInt("ResultadoFinal");
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
    return datoscliente;
   }

}
