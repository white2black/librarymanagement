/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.*;
import beans.*;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class UseDB {
    
    private static Connection connect;
    
    public UseDB() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver");
        connect = DriverManager.getConnection("jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12346922", "sql12346922" , "E3T8PQ26Yn");
        createBookTable();
        createUserTable();
        createAdminTable();
    }
    
    public static Connection getConnection()
    {
       return connect;
    }
    
    private boolean createBookTable(){
      
        try{
            Statement state = connect.createStatement();
        
            String query =  "CREATE TABLE IF NOT EXISTS Book(bookId VARCHAR(10) PRIMARY KEY NOT NULL,"
                + "bookName VARCHAR(50), category VARCHAR(20), author VARCHAR(50), "
                + "publisher VARCHAR(50), description VARCHAR(150), imagePath VARCHAR(100),"
                + "available VARCHAR(20))";
            
            state.executeUpdate(query);
        }
        catch(Exception e){
            System.out.println("create book table exception caught");
             return false;
        }        
       
        return true;

    }

    private boolean createUserTable() {
        
        try{
            Statement state = connect.createStatement();
            
            String query = "CREATE TABLE IF NOT EXISTS User(userName VARCHAR(20) PRIMARY KEY NOT NULL, password VARCHAR(15), email VARCHAR(50),"
                    + "firstName VARCHAR(50), lastName VARCHAR(50), imagePath VARCHAR(100), booksIssued VARCHAR(700))";
            
            state.executeUpdate(query);
        }
        catch(Exception e){
            System.out.println("create User Table exception caught");
            return false;
        }

        return true;
   
    }

    private boolean createAdminTable() {
        
        try{
            Statement state = connect.createStatement();
            
            String query = "CREATE TABLE IF NOT EXISTS Admin(userName VARCHAR(20) PRIMARY KEY NOT NULL, password VARCHAR(15), email VARCHAR(50),"
                    + "firstName VARCHAR(50), lastName VARCHAR(50), imagePath VARCHAR(100))";
            
            state.executeUpdate(query);
            
        }
        catch(Exception e){
            System.out.println("create admin table exception caught");
            return false;
        }
        return true;
    }
   
    public static User getUser(String username, String password){
    
        try{
          
            final String query = "SELECT * FROM User WHERE username = ? AND password = ?";
            
            PreparedStatement pstate = connect.prepareStatement(query);
            
            pstate.setString(1, username);
            pstate.setString(2, password);
            
            ResultSet rs = pstate.executeQuery();
            
            String[] bookSplit = rs.getString("booksIssued").split(",");
            
            
            User user = new User(rs.getString("username"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getString("firstName"),
            rs.getString("lastName"),
            rs.getString("imagePath"), bookSplit);
          
            return user;
        }
        catch(Exception e){
            System.out.println("getUser exception caught"); 
            return null;
        }
        
        
    }   
    
    
    public static Admin getAdmin(String username, String password){
        
        try{
          
            final String query = "SELECT * FROM Admin WHERE username = ? AND password = ?";
            
            PreparedStatement pstate = connect.prepareStatement(query);
            
            pstate.setString(1, username);
            pstate.setString(2, password);
            
            ResultSet rs = pstate.executeQuery();
                        
            Admin admin = new Admin(rs.getString("username"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getString("firstName"),
            rs.getString("lastName"),
            rs.getString("imagePath"));
          
            return admin;
        }
        catch(Exception e){
            System.out.println("getUser exception caught"); 
            return null;
        }
        
        
    }
    
    public static Book getBook(String bookId){
        
        try{
            
            final String query = "SELECT * FROM Book WHERE bookId = ?";
            
            PreparedStatement pstate = connect.prepareStatement(query);
            
            pstate.setString(1, bookId);
            
            ResultSet rs = pstate.executeQuery();
            
            Book book = new Book(rs.getString("bookId"),
            rs.getString("bookName"),
            rs.getString("category"),
            rs.getString("author"),
            rs.getString("publisher"),
            rs.getString("description"),
            rs.getString("imagePath"),
            rs.getString("available"));
            
            return book;
        }
        
        catch(Exception e){
            System.out.println("getBooks exception caught");
            return null;
        }
    }
    
    
    public static boolean addUser(User user){
        try{
            
            final String query = "INSERT INTO User VALUES(?, ?, ?,"
                    + "?, ?, ?, ?";
            
            PreparedStatement pstate = connect.prepareStatement(query);
            
            pstate.setString(1, user.getUserName());
            pstate.setString(2, user.getPassword());
            pstate.setString(3, user.getEmail());
            pstate.setString(4, user.getFirstName());
            pstate.setString(5, user.getLastName());
            pstate.setString(6, user.getImagePath());
            pstate.setString(7, ",");
            
            pstate.executeUpdate();
            return true;    
        }
        catch(Exception e){
            System.out.println("Add user caught exception");
            return false;
        }
    }
    
    public static boolean addAdmin(Admin admin){
        
        try{
            
            final String query = "INSERT INTO Admin VALUES(?, ?, ?,"
                    + "?, ?, ?";
            
            PreparedStatement pstate = connect.prepareStatement(query);
            
            pstate.setString(1, admin.getUserName());
            pstate.setString(2, admin.getPassword());
            pstate.setString(3, admin.getEmail());
            pstate.setString(4, admin.getFirstName());
            pstate.setString(5, admin.getLastName());
            pstate.setString(6, admin.getImagePath());
            
            pstate.executeUpdate();
            
            return true;    
        }
        catch(Exception e){
            System.out.println("Add user caught exception");
            return false;
        }
    }
    
    public static boolean addBook(Book book){
        
        try{
            
            final String query = "INSERT INTO Book VALUES(?,?,?,"
                    + "?, ?, ?, ?, ?";
            
            PreparedStatement pstate = connect.prepareStatement(query);
            
            pstate.setString(1, book.getBookId());
            pstate.setString(2, book.getBookName());
            pstate.setString(3, book.getCategory());
            pstate.setString(4, book.getAuthor());
            pstate.setString(5, book.getPublisher());
            pstate.setString(6, book.getDescription());
            pstate.setString(7, book.getImagePath());
            pstate.setString(8, book.getAvailable());
            
            pstate.executeUpdate();
            return true;
        }
        catch(Exception e){
            System.out.println("addBook exception caught");
            return false;
        }
    }
    
    public static boolean removeBook(String bookId){
        try{
            final String selectQuery = "SELECT * FROM Book WHERE bookId = ?";
            
            PreparedStatement pstate = connect.prepareStatement(selectQuery);
            
            pstate.setString(1, bookId);
            
            ResultSet rs = pstate.executeQuery();
            
            String available = rs.getString("Available");
            
            if(available.length() == 0){
                final String removeQuery = "DELETE FROM Book WHERE bookId = ?";
                pstate = connect.prepareStatement(removeQuery);
                pstate.setString(1, bookId);
                
                pstate.executeUpdate();
                return true;
            }
            
            return false;
            
            
        }
        catch(Exception e){
            System.out.println("removeBook exception caught");
            return false;
        }
    }
    
    public static boolean removeUser(String username){
        try{
            final String query = "SELECT * FROM User WHERE userName = ?";
            
            PreparedStatement ps = connect.prepareStatement(query);
            
            ps.setString(1, username);
            
            ResultSet rs = ps.executeQuery();
            
            String[] bookIssued = rs.getString("booksIssued").split(",");
            
            if(bookIssued.length == 0){
                final String removeQuery = "DELETE FROM User WHERE userName = ?";
                ps = connect.prepareStatement(removeQuery);
                ps.setString(1, username);
                
                ps.executeUpdate();
                return true;
            }
            
            return false;
        }
        catch(Exception e){
            System.out.println("removeUser exception caught");
            return false;
        }
    }
    
    public static boolean removeAdmin(String username){
        try{
            
            final String removeQuery = "DELETE FROM User WHERE userName = ?";

            PreparedStatement ps = connect.prepareStatement(removeQuery);
            
            ps = connect.prepareStatement(removeQuery);
            ps.setString(1, username);
                
            ps.executeUpdate();

            return true;
        }
        catch(Exception e){
            System.out.println("removeUser exception caught");
            return false;
        }
    }
    
    public static boolean setAvailable(String bookId, String username){
        
        try{
            
            final String query = "SELECT * FROM Book WHERE bookId = ?";
            
            PreparedStatement ps = connect.prepareStatement(query);
            
            ps.setString(1, bookId);
            
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                
                final String availableQuery = "UPDATE Book SET available = ? WHERE bookId = ?";
                
                ps = connect.prepareStatement(availableQuery);
                
                ps.setString(1, username);
                ps.setString(2, bookId);
                
                ps.executeUpdate();
                return true;
            }
            
            return false;
            
        }
        catch(Exception e){
            System.out.println("setAvailable book exception caught");
            return false;
        }
    }
    
    public static boolean setAvailableList(String username, String[] bookId){
        
        try{
            
            final String query = "SELECT * FROM User WHERE userName = ?";
            
            PreparedStatement ps = connect.prepareStatement(query);
            
            ps.setString(1, username);
            
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                
                final String bookIssueinUser = "UPDATE User SET booksIssued = ?";
                ps = connect.prepareStatement(bookIssueinUser);
                
                String bookIssued;
                if(bookId.length == 0)
                    bookIssued = ",";
                else{
                    bookIssued = "";
                    for(int i = 0; i< bookId.length; i++)
                    {
                        bookIssued = bookId[i] + ",";
                    } 
                }
                ps.setString(1, bookIssued);
                ps.executeUpdate();
                
                for(int i = 0; i< bookId.length; i++){
                    setAvailable(bookId[i], username);
                }
                return true;
            }
            return false;
            
        }
        catch(Exception e){
            System.out.println("setAvailableList exception caught");
            return false;
        }
        
    }
    
    public static ArrayList<Book> getBooks(){
        
        ArrayList<Book> bookArr = new ArrayList<Book>();
        
        try{
            final String query = "SELECT * FROM Book";
            
            Statement state = connect.createStatement();
            
            ResultSet rs = state.executeQuery(query);
            
            
                    
            while(rs.next()){
                bookArr.add(new Book( rs.getString(1), rs.getString(2),
                rs.getString(3), rs.getString(4), rs.getString(5), 
                rs.getString(6), rs.getString(7), rs.getString(8)));
            }
            
            return bookArr;
            
        }
        catch(Exception e){
            System.out.println("getBooks exception caught");
            return null;
        }
        
     
    }
    
    public static ArrayList<Book> getAvailableBooks(){
        
        ArrayList<Book> bookArr = new ArrayList<Book>();
        
        try{
            
            final String query = "SELECT * FROM Book WHERE available = ?";
            
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, "");
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                bookArr.add(new Book(rs.getString(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                rs.getString(8)));
            }
            return bookArr;
            
        }
        catch(Exception e){
            System.out.println("getAvailableBooks exception caught");
            return null;
        }
        
    }
    
    public static ArrayList<Book> getIssuedBooks(){
        
        ArrayList<Book> bookArr = new ArrayList<Book>();
        
        try{
            
            final String query = "SELECT * FROM Book WHERE available != ?";
            
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, "");
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                bookArr.add(new Book(rs.getString(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                rs.getString(8)));
            }
            return bookArr;
            
        }
        catch(Exception e){
            System.out.println("getIssuedBooks exception caught");
            return null;
        }
        
    }
    
    public static ArrayList<User> getUsers(){
        
        ArrayList<User> userArr = new ArrayList<User>();
        
        try{
            
            final String query = "SELECT * FROM User";
            
            Statement state = connect.createStatement();
            
            ResultSet rs = state.executeQuery(query);
            
            while(rs.next()){
                
                String[] bookIssued;
                bookIssued = rs.getString("booksIssued").split(",");
                
                userArr.add(new User(rs.getString(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getString(5), rs.getString(6), bookIssued));
                
            }
            
            return userArr;
        }
        catch(Exception e){
            System.out.println("getUsers exception caught");
            return null;
        }
    }
    
    public static ArrayList<Admin> getAdmins(){
        
        ArrayList<Admin> adminArr = new ArrayList<Admin>();
        
        try{
            
            final String query = "SELECT * FROM Admin";
            
            Statement state = connect.createStatement();
            
            ResultSet rs = state.executeQuery(query);
            
            while(rs.next()){
                
                
                adminArr.add(new Admin(rs.getString(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getString(5), rs.getString(6)));
                
            }
            
            return adminArr;
        }
        catch(Exception e){
            System.out.println("getUsers exception caught");
            return null;
        }
    }
    
    
    
    
}



