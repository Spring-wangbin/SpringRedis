package com.ssm.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ssm.utils.BeanHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * ----------------------------------------------------------------------------
 * ----- Confidential and Proprietary Copyright 2012 By Founder Development
 * Company, L.P. All Rights Reserved
 *
 * Project Name : Founder Common Class Name : FounderContextListener.java
 * Package : com.founder.base.listener $Id: FounderContextListener.java 8707
 * 2012-08-29 08:51:31Z yang_chaoming $ :
 */

public class FounderContextListener implements ServletContextListener {

    private static final Log logger = LogFactory
            .getLog(FounderContextListener.class);

    public void contextInitialized(ServletContextEvent event) {
        try {
            ApplicationContext applicationContext = WebApplicationContextUtils
                    .getWebApplicationContext(event.getServletContext());
            ServletContext sc = event.getServletContext();
            String appName = sc.getServletContextName();
            logger.info("[" + appName + "] init context ...");
            BeanHelper.setApplicationContext(applicationContext);
            logger.info(" - Put Spring Context to BeanHelper");

            logger.info("[" + appName + "] Init Context[Done]");
        } catch (Exception e) {
            logger.error("error detail:", e);
        }
    }

    public void contextDestroyed(ServletContextEvent arg0) {

    }

}
