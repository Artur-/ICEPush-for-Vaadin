package org.vaadin.artur.icepush;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icepush.servlet.MainServlet;

import com.vaadin.terminal.gwt.server.ApplicationServlet;

public class ICEPushServlet extends ApplicationServlet {

    private MainServlet ICEPushServlet;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        ICEPushServlet = new MainServlet(servletConfig.getServletContext());
    }

    @Override
    protected void service(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        if (request.getRequestURI().endsWith(".icepush")) {
            // Push request
            try {
                ICEPushServlet.service(request, response);
            } catch (ServletException e) {
                throw e;
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // Vaadin request
            super.service(request, response);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        ICEPushServlet.shutdown();
    }
}
