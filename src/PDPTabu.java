/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author admin
 */
import java.sql.*;
public class PDPTabu {

    //Toma los valores de la columna Solucion y los reestablece
    //en la columna SolucionParcial para realizar nuevas pruebas
    public void cleanSolParcial()
    {
        PDPTabu obj=new PDPTabu();
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Balam.Tiempo";
    int solucion_var=0;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1 = con1.createStatement();
    for (int i=1; i<31;i++){
        rs.next();
        solucion_var=rs.getInt("Solucion");
        stmt1.executeUpdate("UPDATE Balam.Tiempo SET SolucionParcial = "+solucion_var+
            " WHERE Indice = "+i);
        
        }
    obj.cerrarDB(con);
    obj.cerrarDB(con1);
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

    //Manda llamar todos los procesos tabu para una sola iteracion
    //correr este metodo varias veces genera la busqueda
    public void Tabu()
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    PDPTabu obj=new PDPTabu();

    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt = con.createStatement();
    for (int i=2;i<31;i++){
        obj.cleanSolParcial();
        //System.out.println(i);
        stmt.executeUpdate("UPDATE Balam.Tiempo SET SolucionParcial = 0"+
                " WHERE Indice = "+i);
        obj.solucionParcialTabu();
        obj.costodeInvenlaPlanta();
        obj.ResultadoTabu(i);
        }
        obj.cleanSolParcial();
        System.out.println("obj.cleanSolParcial();");
        obj.eleccionTabu();
        System.out.println("obj.eleccionTabu();");
        obj.cerrarDB(con);
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


    //Este metodo genera la produccion propuesta a partir de la
    //solucion parcial obtenida, cada solucion parcial genera
    //una produccion propuesta diferente, la solucion parcial
    //cambia en el metodo de Tabu antes de mandar llamar a este
    //metodo, generando una solucion parcial para el resto de
    //los metodos
    public void solucionParcialTabu()
    {
        PDPTabu obj=new PDPTabu();
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Balam.Tiempo";
    int solucion_var=0;
    int prod_propuesta=0;
    int indice_var=1;
    int entregas_var=0;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt = con.createStatement();
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1 = con1.createStatement();
    
    for (int i=1; i<31;i++){
        
        rs.next();
        entregas_var=rs.getInt("Entregas");
        solucion_var=rs.getInt("SolucionParcial");
        
        if(solucion_var==1){
            prod_propuesta=0;
            indice_var=i;
            
            prod_propuesta=entregas_var;
            
            stmt1.executeUpdate("UPDATE Balam.Tiempo SET ProdPropuesta = "+prod_propuesta
                +" WHERE Indice = "+indice_var);
            
        }
        else{
            prod_propuesta+=entregas_var;
            
            stmt1.executeUpdate("UPDATE Balam.Tiempo SET ProdPropuesta = "+prod_propuesta
                +" WHERE Indice = "+indice_var);
            
            stmt1.executeUpdate("UPDATE Balam.Tiempo SET ProdPropuesta = 0"+
                    " WHERE Indice = "+i);
            
        }
        }
    obj.cerrarDB(con);
    obj.cerrarDB(con1);
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

    //dada una produccion propuesta se generara un costo para la planta
    //la solucion inicial indica que se produce la cantidad exacta
    //diariamente para repartir a los clientes, generando un costo
    //nulo de inventario para la planta, pero al producir una mayor
    //cantidad en ciertos dias y dejando de producir en otros se
    //reduce el costo de produccion pero el costo de inventario
    //se incrementa, este metodo calcula esos costos para cada dia
        public void costodeInvenlaPlanta()
    {
            PDPTabu obj=new PDPTabu();
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Balam.Tiempo";
    int prod_propuesta=0;
    int entregas_var=0;
    int costoinv_planta=0;
    int solucion_var=0;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt = con.createStatement();
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1 = con1.createStatement();
    for (int i=1; i<31;i++){
        rs.next();
        entregas_var=rs.getInt("Entregas");
        solucion_var=rs.getInt("SolucionParcial");
        if(solucion_var==1){
        prod_propuesta=rs.getInt("ProdPropuesta");
        prod_propuesta=prod_propuesta-entregas_var;
        costoinv_planta=prod_propuesta*3;
        }
        else{
        prod_propuesta=prod_propuesta-entregas_var;
        costoinv_planta=prod_propuesta*3;
        }
        
        stmt1.executeUpdate("UPDATE Balam.Tiempo SET CInvPlanta = "+costoinv_planta
                +" WHERE Indice = "+i);
        }
    obj.cerrarDB(con);
    obj.cerrarDB(con1);
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

        // Una vez generado los costos de cada dia, se suma iterativamente
        //todos los costos generados de inventarios de clientes y planta
        //mas la produccion, este resultado sera uno de tantos como
        //soluciones parciales existan
       public void ResultadoTabu(int tiempo)
    {
           PDPTabu obj=new PDPTabu();
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Balam.Tiempo";
    int prod_propuesta=0;
    int costoinv_cliente=0;
    int costoinv_planta=0;
    int resultado=0;
    int resultadotabu=0;
    int cambio_var=0;
    int solucion_var=0;
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt = con.createStatement();
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1 = con1.createStatement();
    for (int i=1; i<31;i++){
        rs.next();
        solucion_var=rs.getInt("SolucionParcial");
        prod_propuesta=rs.getInt("ProdPropuesta")*30;
        if(solucion_var==1){
            prod_propuesta+=3000;
        }
        costoinv_cliente=rs.getInt("CdeInvdelCliente");
        costoinv_planta=rs.getInt("CInvPlanta");
        resultado+=prod_propuesta+costoinv_cliente+costoinv_planta;
        }
    rs = stmt.executeQuery("SELECT * FROM Balam.Resultados");
    rs.next();
    resultadotabu=rs.getInt("ResultadoTabu");
    
    stmt1.executeUpdate("UPDATE Balam.Tiempo SET ResultadoTabu = "+resultado
                       +" WHERE Indice = "+tiempo);
    cambio_var=resultado-resultadotabu;
    stmt1.executeUpdate("UPDATE Balam.Tiempo SET Cambios = "+cambio_var
                       +" WHERE Indice = "+tiempo);
    obj.cerrarDB(con);
    obj.cerrarDB(con1);
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

       //una vez generadas todas las posibles soluciones se elegira
       //la mejor de todas, se cambiaran los valores de la solucion
       //para generar nuevas soluciones parciales a parti de la elegida
       //hasta que se encuentre la solucion marcando como tabu la
       //solucion elegida, la marca tabu es de 4, pero inmediatamente
       //se manda llamar al metodo de restricciones para que redusca en
       //1 todas las restricciones tabu, por lo que la duracion de
       //cada restriccion es de 3 iteraciones
       public void eleccionTabu()
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Balam.Tiempo";
    int cambios_var=0;
    int indice_var=0;
    int soluciontabu=0;
    int valor_min=0;
    int resultado=0;
    int tabu_var=0;
    PDPTabu obj=new PDPTabu();
    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt = con.createStatement();
    Connection con2 = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt2 = con2.createStatement();
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    ResultSet rs = stmt.executeQuery(selectdatos);
    ResultSet rs1 = stmt2.executeQuery("SELECT * FROM Balam.Resultados");
    Statement stmt1 = con1.createStatement();
    rs.next();
    rs1.next();
    resultado=rs1.getInt("ResultadoFinal");
    indice_var=rs.getInt("Indice");
    rs.next();
        indice_var=rs.getInt("Indice");
        tabu_var=rs.getInt("Tabu");
        cambios_var=rs.getInt("Cambios");
        soluciontabu=rs.getInt("ResultadoTabu");
        valor_min=cambios_var;

    for (int i=3; i<31;i++){
        rs.next();
        tabu_var=rs.getInt("Tabu");
        cambios_var=rs.getInt("Cambios");
        soluciontabu=rs.getInt("ResultadoTabu");
        if(tabu_var<1){
            if(cambios_var<valor_min){
            valor_min=cambios_var;
            indice_var=i;
            }
        }
        else if(soluciontabu<resultado){
            if(cambios_var<valor_min){
            valor_min=cambios_var;
            indice_var=i;
                }
            }
        }
        stmt1.executeUpdate("UPDATE Balam.Tiempo SET Solucion = 0"+
                " WHERE Indice = "+indice_var);
        stmt1.executeUpdate("UPDATE Balam.Resultados SET ResultadoTabu = "+
                soluciontabu+" WHERE Indice = 1");
        stmt1.executeUpdate("UPDATE Balam.Tiempo SET Tabu = 4"+
                " WHERE Indice = "+indice_var);
        System.out.println("iteracion");
        obj.restriccionTabu();
        ///////////////////////////////////////////
        if(soluciontabu<resultado){
            //System.out.println("entre al if");
            stmt1.executeUpdate("UPDATE Balam.Resultados SET ResultadoFinal = "+
                soluciontabu+" WHERE Indice = 1");
            obj.Solfinal();
            
        }
        obj.cerrarDB(con);
        obj.cerrarDB(con1);
        obj.cerrarDB(con2);
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

       //Si la solucion es la mejor encontrada hasta el momento
       // es copiada de la solucion actual y almacenada en otro
       //bloque de memoria, de esta forma la solucion podra seguir
       //cambiando, pero si empeora la solucion final permanecera
       //indicando que el proceso de busqueda continuo, aunque no
       //encontro nada mejor
       public void Solfinal()
    {
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Balam.Tiempo";
    int solucion_var=0;

    try{
        PDPTabu obj=new PDPTabu();
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1 = con1.createStatement();
    for (int i=1; i<31;i++){
        rs.next();
        solucion_var=rs.getInt("Solucion");
        stmt1.executeUpdate("UPDATE Balam.Tiempo SET SolucionFinal = "+solucion_var+
            " WHERE Indice = "+i);

        }
    obj.cerrarDB(con);
    obj.cerrarDB(con1);
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

       //reduce las restricciones tabu en 1, en cada iteracion las
       //restricciones viejas deben perder su valor para que en un
       //futuro puedan ser elegidas de nuevo, por supuesto que si la
       //solucion tabu es la mejor
       public void restriccionTabu()
    {
           PDPTabu obj=new PDPTabu();
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String selectdatos="SELECT * FROM Balam.Tiempo";
    int solucion_var=0;

    try{
    Class.forName("org.apache.derby.jdbc.ClientDriver");
    //catch SQLException
    Connection con = DriverManager.getConnection(url, "Balam", "Balam");
    Connection con1 = DriverManager.getConnection(url, "Balam", "Balam");
    Statement stmt=con.createStatement();
    ResultSet rs = stmt.executeQuery(selectdatos);
    Statement stmt1 = con1.createStatement();
    for (int i=1; i<31;i++){
        rs.next();
        solucion_var=rs.getInt("Tabu");
        if(solucion_var>0){
        solucion_var--;
        stmt1.executeUpdate("UPDATE Balam.Tiempo SET Tabu = "+solucion_var+
            " WHERE Indice = "+i);
        System.out.println("valor tabu "+solucion_var);
        }
        }
    obj.cerrarDB(con);
    obj.cerrarDB(con1);
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

       public void cerrarDB(Connection conexion){
     try{
        if (conexion != null) conexion.close();
        }
     catch(SQLException e){
            System.out.println(e.getCause().toString());
        }
    }
}
