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
    
    /**
     * Metodo constructor
     */
    public Controller(){
        this.jFMainWindow = new JFMainWindow(this);
        this.jDLogIn = new JDLogIn(this);
        
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
            case "COMMAND_EXIT":
            try {
                manageExit();
            } catch (IOException ex) {
            }
                break;
            case "COMMAND_RETIRE":
            try {
                manageRetire();
            } catch (IOException ex) {
            }
                break;
            case "COMMAND_DEPOSIT":
            try {
                manageDeposit();
            } catch (IOException ex) {
            }
                break;
            case "COMMAND_INFO":
                JOptionPane.showMessageDialog(null, user.toString());
                break;
            default:
                break;
        }
    }
    
 
    
    /**
     * Metodo que maneja todos los eventos a realzar al cerrar la aplicacion
     */
    private void manageExit() throws IOException{
        int resp = JOptionPane.showConfirmDialog(null, "Desea salir?");
                if(resp == 0){
                    sendUser();
                    System.exit(0);
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
     * Metodo que maneja el retiro de dinero
     */
    private void manageRetire() throws IOException {
        client.getOut().writeByte(2);
        client.getOut().flush();
        double x = Double.parseDouble(JOptionPane.showInputDialog(null, "Cuanto dinero desea retirar?"));
        client.getOut().writeDouble(x);
        client.getOut().flush();
        if(client.getIn().readBoolean()){
            JOptionPane.showMessageDialog(null, "Retiro realizado satisfactoriamente");
            user.setMoney(user.getMoney()-x);
        }else{
            JOptionPane.showMessageDialog(null, "Erroe al realizar la transaccion");
        }
    }

    /**
     * Metodo que maneja el deposito de dinero
     */
    private void manageDeposit() throws IOException {
        client.getOut().writeByte(3);
        client.getOut().flush();
        double x = Double.parseDouble(JOptionPane.showInputDialog(null, "Cuanto dinero desea depositar?"));
        client.getOut().writeDouble(x);
        client.getOut().flush();
        if(client.getIn().readBoolean()){
            JOptionPane.showMessageDialog(null, "Deposito realizado satisfactoriamente");
            user.setMoney(user.getMoney()+x);
        }else{
            JOptionPane.showMessageDialog(null, "Error al realizar la transaccion");
        }
    }
}
