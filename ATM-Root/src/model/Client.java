/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author USUARIO
 */
public class Client{

    /**
     * Atributos
     */
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;
    public static boolean isChat = true;

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * Metodo constructor
     * @param socket de conexion
     * @throws IOException en caso de error en la conexion
     */
    public Client(Socket socket) throws IOException {
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.socket = socket;
    }  

}
