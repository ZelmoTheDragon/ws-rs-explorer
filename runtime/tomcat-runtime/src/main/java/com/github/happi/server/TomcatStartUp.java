package com.github.happi.server;

import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.el.ELProcessor;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import com.arjuna.ats.jta.common.jtaPropertyManager;


@WebListener
public class TomcatStartUp implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(TomcatStartUp.class.getName());

    @Inject
    private InternalService service;

    public TomcatStartUp() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        var bundle = ResourceBundle.getBundle("Messages");
        var info = String.format(
                bundle.getString("app.start"),
                bundle.getString("app.title"),
                bundle.getString("app.version"),
                LocalDateTime.now(),
                System.getProperty("java.version"),
                System.getProperty("java.home"),
                System.getProperty("java.vendor"),
                System.getProperty("java.vendor.url"),
                System.getProperty("os.name"),
                System.getProperty("os.arch"),
                System.getProperty("os.version"),
                System.getProperty("user.name"),
                System.getProperty("user.home"),
                System.getProperty("user.dir")
        );

        LOG.info(info);

        setELProcessor();
        setTransaction();

        this.service.pingDatabase();
        this.service.populateDatabase();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        var bundle = ResourceBundle.getBundle("Messages");
        var info = String.format(
                bundle.getString("app.stop"),
                bundle.getString("app.title"),
                bundle.getString("app.version"),
                LocalDateTime.now()
        );

        LOG.info(info);
    }

    private static void setTransaction() {
        LOG.config("Set transaction manager...");
        jtaPropertyManager.getJTAEnvironmentBean().setUserTransactionJNDIContext("java:comp/env/UserTransactionManager");
        jtaPropertyManager.getJTAEnvironmentBean().setTransactionManagerJNDIContext("java:comp/env/TransactionManager");
        jtaPropertyManager.getJTAEnvironmentBean().setTransactionSynchronizationRegistryJNDIContext("java:comp/env/TransactionSynchronizationRegistry");
    }

    private static void setELProcessor() {
        LOG.config("Set EL processor...");
        try {
            BeanManager beanManager = InitialContext.doLookup("java:comp/env/BeanManager");
            ELProcessor elProcessor = new ELProcessor();
            elProcessor.getELManager().addELResolver(beanManager.getELResolver());
        } catch (NamingException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
