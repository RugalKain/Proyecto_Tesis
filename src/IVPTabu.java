/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author admin
 */
import java.sql.*;
public class IVPTabu {
    
    public void cleanRutaParcial(int arr)
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Rutas";
    int rutatabu=0;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1 = con1.createStatement();
    for (int i=0; i<arr;i++){
        rs.next();
        rutatabu=rs.getInt("RutaTabu");
        //System.out.println("cleanrutaparcial: rutatabu="+rutatabu+"indice="+i);
        stmt1.executeUpdate("UPDATE Rutas SET RutaParcial = "+rutatabu+
            " WHERE Indice = "+i);

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

    public void Tabu(int arr)
    {
    IVPTabu obj=new IVPTabu();
    obj.inicializar();
    System.out.println("valor del arr=;"+arr);
    for (int i=0;i<arr;i++){
        System.out.println("Tabu: for i="+i);
        for (int j=i+1/*i+1*/;j<arr/*51*/;j++){
                System.out.println("Tabu: for j="+j);
                obj.cleanRutaParcial(arr);
                System.out.println("obj.alg0(2);");
                if(i!=0&&j!=arr-1){
                obj.elSuap(i, j, arr);
                }
                System.out.println("obj.alg0(3);");
                for (int k=0;k<arr-1;k++){
                    obj.calculoDeCosto(k, k+1);
                }
                System.out.println("obj.alg0(4);");
                for (int k=0;k<arr;k++){
                    obj.diferencia(k);
                }
                System.out.println("obj.alg0(5);");
                if(i!=0&&j!=arr-1){
                obj.posiblesCambios(i, j, arr);
                }
                System.out.println("obj.alg0(6);");
            }
        }
    obj.elSuapDefinitivo(arr);
    System.out.println("obj.alg0(7);");
    for (int k=0;k<arr-1;k++){
             obj.calculoDeCosto1(k, k+1);
        }
    System.out.println("obj.alg0(8);");
    obj.funcionObjetivo(arr);
    System.out.println("obj.alg0(9);");
    obj.SolucionGlobal(arr);
    System.out.println("obj.alg0(10);");
   }

    public void elSuap(int suap1, int suap2, int arr)
    {
    IVPTabu obj = new IVPTabu();
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Rutas WHERE Indice = "+suap1;
    String selectdatos2="SELECT * FROM Rutas WHERE Indice = "+suap2;
    int parametro1=0;
    int parametro2=0;
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
    rs = stmt.executeQuery(selectdatos2);
    rs.next();
    parametro2=rs.getInt("RutaTabu");
    stmt1.executeUpdate("UPDATE Rutas SET RutaParcial = "+parametro2+
                " WHERE Indice = "+suap1);
    stmt1.executeUpdate("UPDATE Rutas SET RutaParcial = "+parametro1+
                " WHERE Indice = "+suap2);
    obj.elFixer(suap1,suap2,arr);
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

    public void calculoDeCosto(int indice1, int indice2)
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Rutas WHERE Indice = "+indice1;
    String selectdatos1="SELECT * FROM Rutas WHERE Indice = "+indice2;
    int parametro1=0;
    String parametro1h="Lugar";
    int parametro2=0;
    double distancia=0;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1 = con1.createStatement();
    rs.next();
    parametro1=rs.getInt("RutaParcial");
    rs = stmt.executeQuery(selectdatos1);
    rs.next();
    parametro2=rs.getInt("RutaParcial");
    rs = stmt.executeQuery("SELECT * FROM Distancias WHERE IDOrigen = "+
            parametro2);
    parametro1h+=parametro1;
    rs.next();
    distancia=Double.parseDouble(rs.getString(parametro1h));
    stmt1.executeUpdate("UPDATE Rutas SET CostoParcial = '"+String.valueOf(distancia)+
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

    public void diferencia(int indice1)
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Rutas WHERE Indice = "+indice1;
    double costoparcial=0;
    double costobase=0;
    double diferencia_var=0;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1 = con1.createStatement();
    rs.next();
    costoparcial=Double.parseDouble(rs.getString("CostoParcial"));
    costobase=Double.parseDouble(rs.getString("CostoBase"));
    diferencia_var=costoparcial-costobase;
    stmt1.executeUpdate("UPDATE Rutas SET Diferencia = '"+String.valueOf(diferencia_var)+
            "' WHERE Indice = "+indice1);
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

    public void elFixer(int indice1, int indice2, int arr)
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Rutas";
    int rutatabu=0;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1 = con1.createStatement();
    for (int i=0; i<arr;i++){
        rs.next();
        rutatabu=rs.getInt("RutaTabu");
        if(indice1==i||indice2==i){

        }
        else{
        stmt1.executeUpdate("UPDATE Rutas SET RutaParcial = "+rutatabu+
            " WHERE Indice = "+i);
        }

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
   //tengo problemas aqui sobre la captura
    public void posiblesCambios(int suap1, int suap2, int arr)
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Rutas";
    double parametro=0;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
    for (int i=0;i<arr/*52*/;i++){//maxima ruta 51 arr+1=52
        rs.next();
        parametro+=Double.parseDouble(rs.getString("Diferencia"));
        }
    String insertdatos="INSERT INTO MatrizSolucion (Cambio, Indice1, Indice2)"+
            " VALUES ('"+String.valueOf(parametro)+"',"+suap1+","+suap2+")";
    PreparedStatement updateSales = con1.prepareStatement(insertdatos);
    updateSales.executeUpdate();
    }
    catch(ClassNotFoundException e){
        System.out.println("no se encontro la clase \n"+
                "error ClassNotFoundException");
        System.out.println(e.toString());
    }
    catch(SQLException e){
        System.out.println("error de conexion en"+
                "posiblesCambios(int suap1, int suap2)\n"+
                "error SQLException");
        System.out.println(e.toString());
    }
   }

    public void elSuapDefinitivo(int arr)
    {
    IVPTabu obj = new IVPTabu();
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM MatrizSolucion";
    int tabu=0;
    int notabu=0;
    double cambio=0;
    int indice1=0;
    int indice2=0;
    double losmejores[]=new double[5];
    int losindices1[]=new int[5];
    int losindices2[]=new int[5];
    for (int i=0;i<5;i++){
        losmejores[i]=0;
        losindices1[i]=0;
        losindices2[i]=0;
    }
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);

    rs.next();
    
    cambio=Double.parseDouble(rs.getString("Cambio"));
    indice1=rs.getInt("Indice1");
    indice2=rs.getInt("Indice2");
    losmejores[0]=cambio;
    losindices1[0]=indice1;
    losindices2[0]=indice2;
    rs.next();
    do{
        cambio=Double.parseDouble(rs.getString("Cambio"));
        indice1=rs.getInt("Indice1");
        indice2=rs.getInt("Indice2");
        if(cambio<losmejores[0]){
            System.out.println("dentro del if de los mejores");
            losmejores[4]=losmejores[3];
            losmejores[3]=losmejores[2];
            losmejores[2]=losmejores[1];
            losmejores[1]=losmejores[0];
            losmejores[0]=cambio;
            losindices1[4]=losindices1[3];
            losindices2[4]=losindices2[3];
            losindices1[3]=losindices1[2];
            losindices2[3]=losindices2[2];
            losindices1[2]=losindices1[1];
            losindices2[2]=losindices2[1];
            losindices1[1]=losindices1[0];
            losindices2[1]=losindices2[0];
            losindices1[0]=indice1;
            losindices2[0]=indice2;
            System.out.println("elSuapDefinitivo: de los 5 el num "+
                    losmejores[0]+" "+losindices1[0]+" "+
                    losindices2[0]+"dentrodelif");
        }
      }while(rs.next()!=false);
    
    do{
     tabu=obj.restriccionTabu(losmejores[notabu], losindices1[notabu], losindices2[notabu]);
     notabu++;
    }while(tabu!=0||notabu==5);
    notabu--;
    System.out.println("de los 5 mejores fue el: "+notabu);
    System.out.println(losindices1[notabu]+" "+losindices2[notabu]);
    obj.elSuap1(losindices1[notabu], losindices2[notabu], arr);
    obj.registrarCambio(losindices1[notabu], losindices2[notabu]);
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

    public void elSuap1(int parametro1, int parametro2, int arr)
    {
    IVPTabu obj = new IVPTabu();
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Rutas WHERE Indice = "+parametro1;
    String selectdatos2="SELECT * FROM Rutas WHERE Indice = "+parametro2;
    int suap1=0;
    int suap2=0;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1 = con1.createStatement();
    rs.next();
    suap1=rs.getInt("RutaTabu");
    rs = stmt.executeQuery(selectdatos2);
    rs.next();
    suap2=rs.getInt("RutaTabu");
    stmt1.executeUpdate("UPDATE Rutas SET RutaTabu = "+suap2+
                " WHERE Indice = "+parametro1);
    stmt1.executeUpdate("UPDATE Rutas SET RutaTabu = "+suap1+
                " WHERE Indice = "+parametro2);
    obj.elFixer(parametro1,parametro2,arr);
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

    public void calculoDeCosto1(int indice1, int indice2)
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Rutas WHERE Indice = "+indice1;
    String selectdatos1="SELECT * FROM Rutas WHERE Indice = "+indice2;
    int parametro1=0;
    String parametro1h="Lugar";
    int parametro2=0;
    double distancia=0;
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
    rs = stmt.executeQuery("SELECT * FROM Distancias WHERE IDOrigen = "+
            parametro2);

    parametro1h+=parametro1;
    rs.next();
    distancia=Double.parseDouble(rs.getString(parametro1h));
    stmt1.executeUpdate("UPDATE Rutas SET CostoBase = '"+String.valueOf(distancia)+"'"+
                " WHERE Indice = "+indice2);
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

    public int restriccionTabu(double cambio, int indice1, int indice2)
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM EstructuraTabu";
    double resultadotabu=0;
    double resultadofinal=0;
    int indice1_var=0;
    int indice2_var=0;
    int tabu=0;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
        for (int i=0;i<3;i++){
            rs.next();
            indice1_var=rs.getInt("Indice1");
            indice2_var=rs.getInt("Indice2");
            if(indice1_var==indice1){
                if(indice2_var==indice2){
                    tabu=1;

                }
            }
        }
    rs = stmt.executeQuery("SELECT * FROM ResultadosIVP");
    rs.next();
    resultadofinal=Double.parseDouble(rs.getString("ResultadoFinal"));
    resultadotabu=Double.parseDouble(rs.getString("ResultadoTabu"));
    resultadotabu+=cambio;
    if(resultadotabu<resultadofinal){
        tabu=0;
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
    return tabu;
   }

    public void registrarCambio(int indice1, int indice2)
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM EstructuraTabu";
    int updated=0;
    int id=0;
    int tabu=0;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1=con1.createStatement();
        for (int i=1;i<4;i++){
            rs.next();
            tabu=rs.getInt("Tabu");
            if(tabu>0){
                tabu--;
                stmt1.executeUpdate("UPDATE EstructuraTabu SET Tabu"+
                        "= "+tabu+" WHERE ID = "+i);
            }
        }
    System.out.println("indice1 "+indice1+"indice2 "+indice2);
    rs = stmt.executeQuery(selectdatos);
    rs.next();
    do{
        id=rs.getInt("ID");
        tabu=rs.getInt("Tabu");
        if(tabu==0){
                tabu=3;
                stmt1.executeUpdate("UPDATE EstructuraTabu SET Indice1"+
                        "= "+indice1+" WHERE ID = "+id);
                stmt1.executeUpdate("UPDATE EstructuraTabu SET Indice2"+
                        "= "+indice2+" WHERE ID = "+id);
                stmt1.executeUpdate("UPDATE EstructuraTabu SET Tabu"+
                        "= "+tabu+" WHERE ID = "+id);
                updated=1;
            }
        rs.next();
    }while(updated==0);
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

    public void funcionObjetivo(int arr)
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Rutas";
    double parametro=0;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1=con1.createStatement();
    for (int i=0;i<arr;i++){
        rs.next();
        parametro+=Double.parseDouble(rs.getString("CostoBase"));
        }
    stmt1.executeUpdate("UPDATE ResultadosIVP SET ResultadoTabu"+
                        "= '"+String.valueOf(parametro)+"' WHERE indice = 1");
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

    public void inicializar()
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String deletedatos1="DELETE FROM MatrizSolucion";
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    //eliminacion de la tabla
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

    public void SolucionGlobal(int arr)
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM ResultadosIVP";
    IVPTabu obj=new IVPTabu();
    double parametro1=0;
    double parametro2=0;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1=con1.createStatement();
    rs.next();
    parametro1=Double.parseDouble(rs.getString("ResultadoFinal"));
    parametro2=Double.parseDouble(rs.getString("ResultadoTabu"));
    if(parametro2<parametro1){
        stmt1.executeUpdate("UPDATE ResultadosIVP SET ResultadoFinal"+
                        "= '"+String.valueOf(parametro2)+"' WHERE indice = 1");
        obj.rutaOficial(arr);
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

    public void rutaOficial(int arr)
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Rutas";
    int rutatabu=0;
    String costooficial="";
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1 = con1.createStatement();
    for (int i=0; i<arr;i++){
        rs.next();
        rutatabu=rs.getInt("RutaTabu");
        costooficial=rs.getString("CostoBase");
        stmt1.executeUpdate("UPDATE Rutas SET RutaOficial = "+rutatabu+
            " WHERE Indice = "+i);
        stmt1.executeUpdate("UPDATE Rutas SET CostoOficial = '"+costooficial+
            "' WHERE Indice = "+i);
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
