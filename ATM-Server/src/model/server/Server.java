/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import model.Bank;
import persistence.FileManager;

/**
 *
 * @author USUARIO
 */
public class Server extends Thread{
    
    /**
     * Atributos
     */
    private ServerSocket ss;
    private List<ConectionServer> list;
    private FileManager fileManager;
    private Bank bank;
    private User user;

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public ServerSocket getSs() {
        return ss;
    }
      
    /**
     * Metodo constructor
     * @param port puerto del servidor
     */
    public Server(int port){
        try {
            ss = new ServerSocket(port);
            list = new ArrayList();
            fileManager = new FileManager();
            bank = new Bank();
            init();
        } catch (IOException ex) {
        }
    }
        
    /**
     * Metodo que controla el hilo de las conexiones
     */
    @Override
    public void run(){
        while(true){
            try {
                Socket s = ss.accept();
                System.err.println("se conecto: "+s.getInetAddress().getHostName());
                list.add(new ConectionServer(s, this));
                
            } catch (IOException ex) {
            }
        }        
    }
    
    /**
     * Metodo main del servidor
     * @param args 
     */
    public static void main(String[] args) {
        new Server(12345).start();
    } 

    /**
     * Metodo que inicializa el servidor con los datos del banco
     * @throws IOException en caso de encontrar el archivo de datos
     */
    private void init() throws IOException{
        User user = null;
        String[] vector = null;
        for (int i = 0; i < fileManager.readFileUsers().size(); i++) {
            vector = fileManager.readFileUsers().get(i).split(",");
            user = new User(vector[0], vector[1], Double.parseDouble(vector[2]));
            bank.getListUsers().add(user);
        }
    }
        
    /**
     * Metodo que verfifica el acceso de un usuario
     * @param name identificacion ingresada por el usuario
     * @param pass numero de cuenta igresada por el usuario
     * @return verdadero si los datos son correctos o falso de lo contrario
     */
    public boolean logIn(String name, String pass){
        for (int i = 0; i < bank.getListUsers().size(); i++) {
            if(bank.getListUsers().get(i).getId().equals(name) && bank.getListUsers().get(i).getCountNumber().equals(pass)){
                user = bank.getListUsers().get(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo que actualiza los datos
     * @throws IOException en caso de no encontrar el archivo
     */
    public void writeUsers() throws IOException{
        fileManager.open1("src/persistence/users.txt");
        for (int i = 0; i < bank.getListUsers().size(); i++) {
            fileManager.write(bank.getListUsers().get(i).getCountNumber()+","+bank.getListUsers().get(i).getId()
            +","+bank.getListUsers().get(i).getMoney()+"\n");
        }
        fileManager.close();
    }
    
    /**
     * Metodo que verifica si la cuenta ya existe
     * @param acount cuenta nueva a verificar
     * @return verdadero si la cuenta no existe o falso si existe
     */
    public boolean verifyAcount1(String acount){
        for (int i = 1; i < bank.getListUsers().size(); i++) {
            if(acount.equals(bank.getListUsers().get(i).getCountNumber())){
                return false;
            }
        }
        return true;
    }
    
    /**
     * Metodo que verifica si la cuenta ya existe
     * @param acount cuenta nueva a verificar
     * @return verdadero si la cuenta existe o falso si no existe
     */
    public boolean verifyAcount2(String acount){
        for (int i = 1; i < bank.getListUsers().size(); i++) {
            if(acount.equals(bank.getListUsers().get(i).getCountNumber())){
                user = bank.getListUsers().get(i);
                return true;
            }
        }
        return false;
    }
     
    /**
     * Metodo que agrega un nuevo usuario a los datos
     * @throws IOException en caso de no encontrar el archivo
     */
    public void writeNewUser(User user) throws IOException{
        fileManager.open2("src/persistence/users.txt");
        fileManager.write(user.getCountNumber()+","+user.getId()+","+user.getMoney()+"\n");
        fileManager.close();
    }
}
