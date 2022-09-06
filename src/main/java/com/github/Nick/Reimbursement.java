package com.github.Nick;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.github.Nick.auth.AuthService;
import com.github.Nick.auth.AuthServlet;
import com.github.Nick.users.UserDAO;
import com.github.Nick.users.UserServlet;

public class Reimbursement {
    public static void main(String[] args) throws LifecycleException {
        System.out.println("Start");

        String docBase = System.getProperty("java.io.tmpdir");

        Tomcat webServer = new Tomcat();
        webServer.setBaseDir(System.getProperty(docBase));
        webServer.setPort(5000);
        webServer.getConnector(); // server to recieve request

        //app component instantiation
        UserDAO userDAO = new UserDAO();
        AuthService authService = new AuthService(userDAO);
        UserServlet userServlet = new UserServlet(userDAO);
        AuthServlet authServlet = new AuthServlet(authService);

        //Web server context and servlet config
        final String rootContext = "Reimbursement";
        webServer.addContext(rootContext, docBase);
        webServer.addServlet(rootContext, "UserServlet", userServlet).addMapping("/users");
        webServer.addServlet(rootContext, "AuthServlet", authServlet).addMapping("/auth");
        
        //starting and awaiting web request
        webServer.start();
        webServer.getServer().await();
        System.out.print("Web application started.");

        // deleted util folder
        // UserDAO userDAO = new UserDAO();
        // User loggedIn = userDAO.findUserNameByName("Tony")
        //                         .orElseThrow(() -> new RuntimeException("No user found"));
        // System.out.println("Loggin in user: " + loggedIn);
        
    }
}