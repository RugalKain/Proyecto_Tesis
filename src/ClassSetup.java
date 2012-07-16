/*
 * Este programa genera un array con valores 0 y 1 que
 * representan dias, 0 si no se incurre en producción
 * i de otra forma
 */

/**
 *
 * @author admin
 */

import java.util.*;
public class ClassSetup {

    //crea un arreglo con la configuracion de dias en los cuales
    //se generara un costo setup, se ayuda de los metodos
    //vectordeDias para saber que dias especificos
    //se genera el costo, y de vectorArray que convierte
    //el vectordeDias en un arreglo de enteros
    public int[] arraydeClientes(String clientes){
    int arrayclientes[];
    Vector vectorsetup;
    vectorsetup=vectordeClientes(clientes);
    arrayclientes=vectorArray(vectorsetup);
    return arrayclientes;
}

    //crea un vector con los dias especificos en los que
    //abra costo setup
    public Vector vectordeClientes(String clientes){
    Vector vectorsetup=new Vector(7,3);
    String subdiasetup="";
    int cliente=0;
    int posicion=0;
    int posicion1=clientes.indexOf(",");
    int posicion2=0;
    do{
        subdiasetup=clientes.substring(posicion2, posicion1);
        posicion=posicion1;
        posicion2=posicion1+1;
        posicion1=clientes.indexOf(",",posicion+1);
        cliente=Integer.parseInt(subdiasetup);
        vectorsetup.addElement(cliente);
    }while(posicion!=clientes.lastIndexOf(","));
    subdiasetup=clientes.substring(posicion2);
    cliente=Integer.parseInt(subdiasetup);
    vectorsetup.addElement(cliente);
    return vectorsetup;
}

    //convierte los elementos de un vector en enteros
    // y los almacena en un arreglo de tipo int
    public int[] vectorArray(Vector vectorsetup){
        int tamaño=vectorsetup.size();
        int clientes[]=new int[tamaño];
        for (int i=0;i<clientes.length;i++){
            clientes[i]=Integer.parseInt(vectorsetup.elementAt(i).toString());
        }
        return clientes;
        }
}
