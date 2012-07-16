/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author admin
 */
import java.sql.*;
public class pruebaDB {

    private int tiempo=2;
    private int productos=1;
    private int clientes=51;
    private int c_produccion=30;
    private int c_setup=3000;
    encapsulado objInventario[] = new encapsulado[clientes];

public int[] getCliente(int cliente)
    {
    int datoscliente[]=new int[5];
    int hjk=0;
    String url = "jdbc:derby://localhost:1527/PDPIVP";
    String select_hjk = "SELECT * FROM Balam.ClientesTemp WHERE IDCliente = "+ cliente;
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

//Sumatoria de clientes
public int[] sumatoria1(){

    int datoscliente[]=new int[4];
    int resultado[]=new int[2];
    int produccion=0;
    int hjk=0;
    int pjt=0;
    int yjt=0;
    int inventario=0;
    int inventario_actual=0;
    int consumo=0;
    int parametro_de_consumo=1;
    for (int i=0; i<clientes;i++){
        encapsulado obj = new encapsulado();
        objInventario[i]=obj;
    }

    for (int i=0; i<clientes;i++){
        parametro_de_consumo=objInventario[i].getInventario();
        yjt=0;
        datoscliente=getCliente(i);
        pjt=datoscliente[0];
        inventario_actual=datoscliente[1];
        consumo=datoscliente[2];
        hjk=datoscliente[3];
        //System.out.println(i+".- pjt= "+pjt+", inventario actual= "+inventario_actual+
          //      ", consumo="+consumo+", hjk= "+hjk);
        inventario=inventario_actual-(consumo*parametro_de_consumo);
        //System.out.println(i+" inventario= "+inventario+" inventario actual= "+
          //      inventario_actual+"\nconsumo= "+consumo+
            //    " parametro= "+parametro_de_consumo);
        if(inventario==0){
            yjt=1;
            inventario=pjt;
            parametro_de_consumo=1;
            objInventario[i].setInventario(parametro_de_consumo);
        }
        else{
            parametro_de_consumo++;
            objInventario[i].setInventario(parametro_de_consumo);
            System.out.println("llegue?");
        }
        if(yjt==1){
            produccion+=pjt;
            resultado[0]+=hjk*inventario;
        }
        else{
            resultado[0]+=hjk*inventario;
        }

    }
    System.out.println(produccion);
    //produccion=c_produccion*produccion;
    resultado[1]=produccion;
    System.out.println(resultado[0]);
    return resultado;
}

//Sumatoria de productos
public int[] sumatoria2(){
    int resultado1[]=new int[2];
    int resultado[]=new int[2];
    for (int i=0; i<productos;i++){
        resultado1=sumatoria1();
        resultado[0]+=resultado1[0];
        resultado[1]+=resultado1[1];
    }
    System.out.println(resultado[1]);
    System.out.println(resultado[0]);
    return resultado;
}

//Sumatoria de productos
public int[] sumatoria3(){
    int resultado1[]=new int[2];
    int resultado[]=new int[2];
    for (int i=0; i<tiempo;i++){
        resultado1=sumatoria1();
        resultado[0]+=resultado1[0];
        resultado[1]+=resultado1[1];
    }
    System.out.println(resultado[1]);
    System.out.println(resultado[0]);
    return resultado;
}

}