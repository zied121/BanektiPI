package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

    public String Url="jdbc:mysql://localhost:3306/banque2";
    public String login="root";
    public String pwd="";
    Connection cnx;
    public static MyConnection instance;
    public static MyConnection getInstance(){
        if(instance==null){
            instance =new MyConnection();
        }
        return instance;
    }
    private MyConnection(){
        try {
            cnx= DriverManager.getConnection(Url,login,pwd);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public Connection getCnx(){
        return cnx;
    }
}
