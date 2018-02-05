/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USUARIO
 */
public class ConectionServer extends Thread{
    
    /**
     * Atributos
     */
    private ObjectInputStream in;
    private ObjectOutputStream out;   
    private Socket socket;
    private Server server;
    private Vector v;
    private User aux;

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }
                   
    /**
     * Metodo constructor
     * @param socket socket que se ha conectado
     * @param server servidor
     * @throws IOException en caso de error en la conexion
     */
    public ConectionServer(Socket socket, Server server) throws IOException {
        v  = new Vector();
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.server = server;
        this.start();
    }
    
    /**
     * Hilo que controla lo que se envia y se recibe al servidor
     */
    @Override
    public void run(){
        while (!socket.isClosed()) {
            try {
                byte x = 0;
                try {
                    x = in.readByte();
                    switch(x){
                        case 1:
                            manageLogIn();
                            break;                    
                        case 2:
                            manageRetire();
                            break;
                        case 3:
                            manageDeposit();
                            break;
                        case 4:
                            manageCreate();
                            break;
                        case 5:
                            manageRead();
                            break;
                        case 6:
                            manageDelete();
                            break;
                        case 7:
                            manageUpdate();
                            break;
                    }
                    
                v = (Vector)in.readObject();
                server.setUser((User)v.get(0));
                server.writeUsers();
                }catch (IOException ex) {
                }
            }catch (ClassNotFoundException ex) {
                Logger.getLogger(ConectionServer.class.getName()).log(Level.SEVERE, null, ex);
        }   
        }
    }

    /**
     * Metodo que maneja el ingreso del usuario
     * @throws IOException en caso de error en la conexion
     */
    private void manageLogIn() throws IOException{
        String name = in.readUTF();
        String pass = in.readUTF();
        if(server.logIn(name,pass)){
            out.writeBoolean(true);
            out.flush();
            v.add(server.getUser());
            out.writeObject(v);
            out.flush();
        }else{
            out.writeBoolean(false); 
            out.flush();   
        }
    }

    /**
     * Metodo que gestiona el retiro de dinero por parte del usuario
     * @throws IOException en caso de error en la conexion
     */
    private void manageRetire() throws IOException {       
        double money = in.readDouble();
        if (money <= server.getUser().getMoney() && money <= server.getBank().getMoneyAtm()) {
            out.writeBoolean(true);
            out.flush();
            server.getBank().setMoneyAtm(server.getBank().getMoneyAtm()-money);
            server.getUser().setMoney(server.getUser().getMoney()-money);
        } else {
            out.writeBoolean(false);
            out.flush();
        }
    }

    /**
     * Metodo que gestiona el deposito de dinero por parte del usuario
     * @throws IOException en caso de error en la conexion
     */
    private void manageDeposit() throws IOException {
        double money = in.readDouble();
        out.writeBoolean(true);
        out.flush();
        server.getBank().setMoneyAtm(server.getBank().getMoneyAtm() + money);
        server.getUser().setMoney(server.getUser().getMoney() + money);
    }

    /**
     * Metodo que maneja la creacion de nuevos usuarios
     * @throws IOException si hay error en la conexion
     */
    private void manageCreate() throws IOException, ClassNotFoundException {
        if(server.verifyAcount1(in.readUTF())){
            out.writeBoolean(true);
            out.flush();
            v = (Vector)in.readObject();
            aux = (User)v.get(0);
            server.writeNewUser(aux);
        }else{
            out.writeBoolean(false);
            out.flush();
        }
    }
    
    /**
     * Metodo que maneja la creacion de nuevos usuarios
     * @throws IOException si hay error en la conexion
     */
    private void manageRead() throws IOException {
        String acount = in.readUTF();
        if(server.verifyAcount2(acount)){
            out.writeBoolean(true);
            out.flush();
            Vector vaux = new Vector();
            vaux.add(server.getUser());
            out.writeObject(vaux);
            out.flush();
        }else{
            out.writeBoolean(false);
            out.flush();
        }
    }

    /**
     * Metodo que maneja la eliminacion de un usuario
     * @throws IOException en caso de error en la conexion
     */
    private void manageDelete() throws IOException {
        String acount = in.readUTF();
        if(server.verifyAcount2(acount)){
            out.writeBoolean(true);
            out.flush();
            if(in.readBoolean()){
                server.getBank().getListUsers().remove(server.getUser());
                server.writeUsers();
            }
        }else{
            out.writeBoolean(false);
            out.flush();
        }
    }

    /**
     * Metodo que maneja la actualizacion de los datos del usuario
     * @throws IOException en caso de error en la conexion
     * @throws ClassNotFoundException en caso de no encontrar la clase del objeto que llega
     */
    private void manageUpdate() throws IOException, ClassNotFoundException {
        String acount = in.readUTF();
        if(server.verifyAcount2(acount)){
            out.writeBoolean(true);
            out.flush();
            Vector vaux = new Vector();
            vaux = (Vector) in.readObject();
            server.getBank().getListUsers().remove(server.getUser());
            server.setUser((User)vaux.get(0));
            server.getBank().getListUsers().add(server.getUser());
            server.writeUsers();
        }else{
            out.writeBoolean(false);
            out.flush();
        }
    }
    
}
