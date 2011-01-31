package org.vaadin.artur.icepush;

import java.util.Map;

import javax.portlet.PortletContext;
import javax.servlet.ServletContext;

import org.icepush.PushContext;
import org.vaadin.artur.icepush.client.ui.VICEPush;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.gwt.server.PortletApplicationContext2;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.AbstractComponent;

/**
 * Server side component for the VICEPush widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.artur.icepush.client.ui.VICEPush.class)
public class ICEPush extends AbstractComponent {

    public static final String PUSH_GROUP = "ICEPush-1";
    private static String codeJavascriptLocation;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        target.addAttribute(VICEPush.PUSH_GROUP, PUSH_GROUP);
        if (codeJavascriptLocation != null) {
            target.addAttribute(VICEPush.ICEPUSH_JS_LOCATION,
                    codeJavascriptLocation);
        }

    }

    /**
     * Receive and handle events and other variable changes from the client.
     * 
     * {@inheritDoc}
     */
    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);

        // No need to handle event
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
        pushContext.push(ICEPush.PUSH_GROUP);
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
