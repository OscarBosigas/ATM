/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.server;

import java.io.Serializable;

/**
 *
 * @author USUARIO
 */
public class User implements Serializable{
    
    private String countNumber;
    private String id;
    private double money;

    /**
     * Metodo constructor
     * @param countNumber numero de cuenta del usuario
     * @param id identificacion del usuario
     * @param money dinero disponible del usuario
     */
    public User(String countNumber, String id, double money) {
        this.countNumber = countNumber;
        this.id = id;
        this.money = money;
    }

    public String getCountNumber() {
        return countNumber;
    }

    public void setCountNumber(String countNumber) {
        this.countNumber = countNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    /**
     * Metodo toString
     * @return atributos del usuario
     */
    @Override
    public String toString() {
        return "Numero de cuenta:  " + countNumber + "\n" + "ID:  " + id + "\n" + "Saldo:  " + money;
    }
    
    
}
