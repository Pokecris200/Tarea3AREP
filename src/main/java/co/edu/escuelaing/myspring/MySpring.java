package co.edu.escuelaing.myspring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cristian.forero-m
 */
public class MySpring {

    public static void main(String[] args) throws ClassNotFoundException, IOException, URISyntaxException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvocationTargetException {
        System.out.println(args[0]);
        String className = args[0];
        Class C = Class.forName(className);
        Map<String, Method> methods = new HashMap<>();
        Method[] declaredMethods = C.getDeclaredMethods();
        //Llenado de metodos
        for (Method m : declaredMethods) {
            if (m.isAnnotationPresent(RequestMapping.class)) {
                System.out.println("añade algo");
                methods.put(m.getAnnotation(RequestMapping.class).value(), m);
            }
        }

        // Web Server Implementation
        boolean running = true;
        ServerSocket serverSocket = null;
        System.out.println(getPort());
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
            String name;
            URI path = null;
            boolean first = true;
            while ((inputLine = in.readLine()) != null) {
                if (first) {
                    first = false;
                    path = new URI(inputLine.split(" ")[1]);
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            
            // Ejecuta Metodos
            String content = "404 NOT FOUND";
            if(!methods.isEmpty()&& methods.containsKey(path.getPath())){
                content = (String) methods.get(path.getPath()).invoke(null);
            }
            
            outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
                    + content;
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();

    }
    
    private static int getPort(){
        if (System.getenv("PORT") != null){
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000;
    }

}
