/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.server.User;
import model.Client;
import view.JDAddUser;
import view.JDEditUser;
import view.JDLogIn;
import view.JFMainWindow;

/**
 *
 * @author USUARIO
 */
public class Controller implements ActionListener{
    
    /**
     * Atributos
     */
    private JFMainWindow jFMainWindow;
    private Client client;
    private User user;
    private JDLogIn jDLogIn;
    private JDAddUser jDAddUser;
    private JDEditUser jDEditUser;
    
    /**
     * Metodo constructor
     */
    public Controller(){
        this.jFMainWindow = new JFMainWindow(this);
        this.jDLogIn = new JDLogIn(this);
        this.jDAddUser = new JDAddUser(this);
        this.jDEditUser = new JDEditUser(this);
        
        this.jDLogIn.setVisible(true);
    }
    
    /**
     * Metodo para escuchar los eventos del mouse
     * @param e accion evento
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "COMMAND_LOGIN":
            try {
                manageLogIn();
            } catch (IOException | ClassNotFoundException ex) {
            }
                break;
            case "COMMAND_CREATE":
                this.jFMainWindow.setVisible(false);
                this.jDAddUser.setVisible(true);
                break;
            case "COMMAND_ADD":
            try {
                manageCreate();
            } catch (IOException ex) {
            }
                break;
            case "COMMAND_CANCEL_ADD":
                jDAddUser.close(jFMainWindow);
                break;
            case "COMMAND_READ":
                try {
                    manageRead();
                } catch (IOException | ClassNotFoundException | NullPointerException ex) {
                 }
                break;
            case "COMMAND_UPDATE":
                try {
                    manageUpdate();
                } catch (IOException ex) {
                }
                break;
            case "COMMAND_EDIT":
                try {
                    manageEdit();
                } catch (IOException ex) {
                }
                break;              
            case "COMMAND_CANCEL_EDIT":
                jDEditUser.close(jFMainWindow);
                break;
            case "COMMAND_DELETE":            
                try {
                    manageDelete();
                } catch (IOException ex) {
                }
                break;
            default:
                break;
        }
    }
        
    /**
     * Metodo que maneja el acceso al usuario
     * @throws IOException en caso de error en la conexion
     * @throws ClassNotFoundException en caso de error en el buffer de salida
     */
    private void manageLogIn() throws IOException, ClassNotFoundException {
        client = new Client(new Socket("localhost", 12345));
        client.getOut().writeByte(1);
        client.getOut().flush();
        client.getOut().writeUTF(jDLogIn.id());
        client.getOut().flush();
        client.getOut().writeUTF(jDLogIn.pass());
        client.getOut().flush();
        jDLogIn.clean();
        if (client.getIn().readBoolean() == true) {
            jDLogIn.setVisible(false);
            jFMainWindow.setVisible(true); 
            Vector v = (Vector)client.getIn().readObject();
            user = (User)v.get(0);
        } else {
            JOptionPane.showMessageDialog(null, "Datos erroneos");
            jDLogIn.setVisible(true);
            client.getSocket().close();
        }        
    }
    
    /**
     * Metodo que envia la informacion del usuario actual al servidor
     */
    public void sendUser() {
         Vector v = new Vector();
         v.add(user);
        try {
            client.getOut().writeObject(v);
            client.getSocket().close();

        } catch (IOException ex) {
        }
    }

    /**
     * Metodo que maneja la creacion de usuarios
     * @throws IOException en caso de error en la conexion
     */
    private void manageCreate() throws IOException {
        user = jDAddUser.getUser();
        client.getOut().writeByte(4);
        client.getOut().flush();
        client.getOut().writeUTF(user.getCountNumber());
        client.getOut().flush();
        if(client.getIn().readBoolean()){
            sendUser();
            jDAddUser.setVisible(false);
            jFMainWindow.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(null, "El numero de cuenta ya existe");
        }
        
    }

    /**
     * Metodo que maneja la busqueda de informacion de un usuario
     * @throws IOException en caso de error en la conexion
     */
    private void manageRead() throws IOException, ClassNotFoundException {
        client.getOut().writeByte(5);
        String id = JOptionPane.showInputDialog(null, "Ingrese el nuemero de cuenta del usuario");
        client.getOut().writeUTF(id);
        client.getOut().flush();
        if(client.getIn().readBoolean()){
            Vector v = new Vector();
            v = (Vector)client.getIn().readObject();
            user = (User)v.get(0);
            JOptionPane.showMessageDialog(null, user.toString());
        }else{
            JOptionPane.showMessageDialog(null, "Numero de cuenta incorrecto");
            manageRead();
        }
    }
    
    /**
     * Metodo que maneja la eliminacion de usuarios
     * @throws IOException en caso de error en la conexion
     */
    private void manageDelete() throws IOException {        
        client.getOut().writeByte(6);
        String id = JOptionPane.showInputDialog(null, "Ingrese el nuemero de cuenta del usuario");
        client.getOut().writeUTF(id);
        client.getOut().flush();
        if(client.getIn().readBoolean()){
            int option = JOptionPane.showConfirmDialog(null, "Desea eliminar el usuario con la cuenta " + id +"?");
            if(option == 0){
                client.getOut().writeBoolean(true);
                client.getOut().flush();
            }else{                
                client.getOut().writeBoolean(false);
                client.getOut().flush();
            }
        }
    }

    /**
     * Maneja la actualizacion de datos del usuario
     * @throws IOException en caso de error en la conexion
     */
    private void manageUpdate() throws IOException {
        client.getOut().writeByte(7);
        String id = JOptionPane.showInputDialog(null, "Ingrese el nuemero de cuenta del usuario");
        client.getOut().writeUTF(id);
        client.getOut().flush();
        if(client.getIn().readBoolean()){
            int option = JOptionPane.showConfirmDialog(null, "Desea editar el usuario con la cuenta " + id +"?");
            if(option == 0){
                jDEditUser.setVisible(true);
            }
        }
    }

    /**
     * Metodo que maneja el envio del usuario editado
     * @throws IOException en caso de error en la conexion
     */
    private void manageEdit() throws IOException {
        user = jDEditUser.getUser();
        Vector v = new Vector();
        v.add(user);
        client.getOut().writeObject(v);
        jDEditUser.setVisible(false);
    }

}
