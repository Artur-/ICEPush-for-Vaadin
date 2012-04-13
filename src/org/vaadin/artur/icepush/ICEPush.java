package org.vaadin.artur.icepush;

import javax.portlet.PortletContext;
import javax.servlet.ServletContext;

import org.icepush.PushContext;
import org.vaadin.artur.icepush.client.ui.ICEPushRpc;
import org.vaadin.artur.icepush.client.ui.ICEPushState;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.PortletApplicationContext2;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.AbstractComponent;

/**
 * Server side component for the VICEPush widget.
 */
public class ICEPush extends AbstractComponent {

    private static String codeJavascriptLocation;

    private ICEPushRpc rpc = new ICEPushRpc() {

        public void push() {
            // No need to do anything. The only important thing is that the
            // event is sent
        }
    };

    @Override
    public void updateState() {
        super.updateState();
        getState().setCodeJavascriptLocation(codeJavascriptLocation);
    }

    @Override
    public ICEPushState getState() {
        return (ICEPushState) super.getState();
    }

    public void push() {
        Application app = getApplication();
        if (app == null) {
            throw new RuntimeException(
                    "Must be attached to an application to push");
        }

        // Push changes
        PushContext pushContext = getPushContext(app.getContext());
        if (pushContext == null) {
            throw new RuntimeException(
                    "PushContext not initialized. Did you forget to use ICEPushServlet?");
        }
        pushContext.push(getState().getPushGroup());
    }

    public static synchronized PushContext getPushContext(
            ApplicationContext context) {
        if (context instanceof WebApplicationContext) {
            ServletContext servletContext = ((WebApplicationContext) context)
                    .getHttpSession().getServletContext();
            return (PushContext) servletContext.getAttribute(PushContext.class
                    .getName());
        } else if (context instanceof PortletApplicationContext2) {
            PortletContext portletContext = ((PortletApplicationContext2) context)
                    .getPortletSession().getPortletContext();
            return (PushContext) portletContext.getAttribute(PushContext.class
                    .getName());
        } else {
            throw new RuntimeException(
                    "Could not find PushContext from session");
        }
    }

    public static void setCodeJavascriptLocation(String url) {
        codeJavascriptLocation = url;
    }
}
