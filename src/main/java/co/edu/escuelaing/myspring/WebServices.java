/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.escuelaing.myspring;

/**
 *
 * @author cristian.forero-m
 */
public class WebServices {
    
    @RequestMapping("/hello")
    public static String helloWorld(){
        return "Hello World!";
    }
    
    @RequestMapping("/status")
    public static String status(){
        return "It's running";
    }
}
