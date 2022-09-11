package com.github.Nick;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.Nick.auth.AuthService;
import com.github.Nick.auth.AuthServlet;

import com.github.Nick.users.UserDAO;
import com.github.Nick.users.UserService;
import com.github.Nick.users.UserServlet;

public class Reimbursement {

    private static Logger logger = LogManager.getLogger(Reimbursement.class);
    public static void main(String[] args) throws LifecycleException {

        //! System.out.println("Start");
        //! DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        //! System.out.println(LocalDateTime.now().format(format));

        logger.info("Starting Reimbursement app");

        String docBase = System.getProperty("java.io.tmpdir");

        Tomcat webServer = new Tomcat();
        webServer.setBaseDir(System.getProperty(docBase));
        webServer.setPort(5000);
        webServer.getConnector(); //* server to receive request

        //* app component instantiation
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);
        AuthService authService = new AuthService(userDAO);
        UserServlet userServlet = new UserServlet(userService);
        AuthServlet authServlet = new AuthServlet(authService);

        //* Web server context and servlet config
        final String rootContext = "/Reimbursement";
        webServer.addContext(rootContext, docBase);
        webServer.addServlet(rootContext, "UserServlet", userServlet).addMapping("/users");
        webServer.addServlet(rootContext, "AuthServlet", authServlet).addMapping("/auth");
        
        //TODO complete
        //? webServer.addContext(rootContext, "ReimbursementServlet", reimbursementServlet).addMapping("/reimbursement");
        
        //* starting and awaiting web request
        webServer.start();

        logger.info("Reimbursement started");

        webServer.getServer().await();
        //! System.out.print("Web application started.");
    }
}