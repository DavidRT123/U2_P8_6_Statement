/*
 * Crea un programa, llámalo U2_P8_6_Statement que inserte un profesor, los valores a insertar
 * se introducirán como argumentos del main, el salario tendrá céntimos y el apellido será doble
 * (dos apellidos separados por un espacio en blanco).
 * Antes de insertar el profesor en la tabla se deberá comprobar que los valores a insertar son
 * correctos:
 *  Si alguno de los valores a introducir infringe las reglas de integridad referencial no
 * insertes el profesor y manda un mensaje explicativo.
 *  Si alguno de los valores a introducir infringe las reglas de integridad de claves no
 * insertes el profesor y manda un mensaje explicativo.
 *  El salario debe ser mayor que 0.
 *  La fecha de alta será la fecha de hoy.
 * Se deberá indicar si el profesor se ha insertado correctamente o no, en este último caso se
 * deberá indicar el motivo.
 */
package u2_p8_6_statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;

/**
 *
 * @author mdfda
 */
public class U2_P8_6_Statement {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String nombre = args[0], apellidos = args[1] + " " + args[2], email = args[3], dept_no = args[4];
        Float salario = Float.parseFloat(args[5]);
        Date fecha_antes = new Date();
        java.sql.Date fecha_alta = new java.sql.Date(fecha_antes.getTime());
        
        //Expresión regular para verificar que la dirección de email est abien formada
        Pattern patron = Pattern.compile("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@" + "[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$"); 
        Boolean emailValido = patron.matcher(email).matches(), apellidoValido;
            
        if (salario <= 0.0 || !emailValido) {
            if(salario <= 0.0){
                System.out.println("El salario debe ser mayor que 0: " + salario);
            }
            if(!emailValido){
                System.out.println("El email no tiene el formato adecuado: " + email);
            }
            System.out.println("El registro no se ha podido introducir");
        }else{
            try {
                Class.forName("org.sqlite.JDBC");

                Connection con = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\mdfda\\Desktop\\DAM\\Acceso a Datos (AD)\\Tema 2\\Ejercicios\\clase\\bases\\sqlite\\ejemplo.db");

                Statement s = con.createStatement();

                String cadena = "'" + nombre + "', '" + apellidos + "', '" + email + "',";

                //Para que la restricción de la clave foranea salte es necesario activarla antes de ejecutar la sentencia (PRAGMA foreign_keys = ON;)
                s.executeUpdate("PRAGMA foreign_keys = ON; INSERT INTO profesores VALUES((SELECT MAX(NRM) FROM profesores) + 1, " + cadena + "'" + fecha_alta +"', " + dept_no + ", " + salario + ")");

                s.close();
                con.close();
                
                System.out.println("El registro ha sido insertado correctamente");

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(U2_P8_6_Statement.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                System.out.println("Mensaje: " + ex.getMessage());
                System.out.println("Estado SQL: " + ex.getSQLState());
                System.out.println("Código de error: " + ex.getErrorCode());
            }
        }
    }
}
