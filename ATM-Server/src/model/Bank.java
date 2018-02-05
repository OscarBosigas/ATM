/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.server.User;

/**
 *
 * @author USUARIO
 */
public class Bank {
    
    /**
     * Atributos
     */
    private List<User> listUsers;
    private double moneyAtm;

    public double getMoneyAtm() {
        return moneyAtm;
    }

    public void setMoneyAtm(double moneyAtm) {
        this.moneyAtm = moneyAtm;
    }

    public List<User> getListUsers() {
        return listUsers;
    }
    
    /**
     * Metodo constructor
     */
    public Bank(){
        this.listUsers = new ArrayList();
        this.moneyAtm = 10000;
    }
    
    /**
     * Metodo que suma el dinero de las cuentas de los clientes al de su saldo inicial
     * Es decir que para esta aplicacion al iniciar el cajero siempre tendra como minimo 10000
     */
    public void initMoney(){
        for (int i = 0; i < listUsers.size(); i++) {
            moneyAtm += listUsers.get(i).getMoney();
        }
    }
    
    public void manageRetire(User user){
        Double x = Double.parseDouble(JOptionPane.showInputDialog("Cuanto dinero desea retirar?"));
        if(x <= user.getMoney()){
            user.setMoney(user.getMoney() - x);
            JOptionPane.showMessageDialog(null, "Retiro realizado con exito");
        }else if(x > this.moneyAtm){
            JOptionPane.showMessageDialog(null, "No tenemos los recursos suficientes");
        }else{
            JOptionPane.showMessageDialog(null, "El retiro no se ha podido realizar");
        }
    }
    
    public void manageDeposit(User user){
        Double x = Double.parseDouble(JOptionPane.showInputDialog("Cuanto dinero desea depositar?"));
        user.setMoney(user.getMoney() + x);
        this.moneyAtm = this.moneyAtm + x;
        JOptionPane.showMessageDialog(null, "Deposito realizado correctamente");
    }
}
