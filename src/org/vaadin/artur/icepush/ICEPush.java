package org.vaadin.artur.icepush;

import javax.portlet.PortletContext;
import javax.servlet.ServletContext;

import org.icepush.PushContext;
import org.vaadin.artur.icepush.client.ui.ICEPushRpc;
import org.vaadin.artur.icepush.client.ui.ICEPushState;

import com.vaadin.server.AbstractExtension;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServiceSession;
import com.vaadin.server.WrappedHttpSession;
import com.vaadin.server.WrappedPortletSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.UI;

/**
 * Server side extension for ICEPush. Attach to your UI class using
 * {@link #extend(UI)}
 */
public class ICEPush extends AbstractExtension {

    private static String codeJavascriptLocation;

    private ICEPushRpc rpc = new ICEPushRpc() {

        public void push() {
            // No need to do anything. The only important thing is that the
            // event is sent
        }
    };

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        getState().setCodeJavascriptLocation(codeJavascriptLocation);
    }

    @Override
    public ICEPushState getState() {
        return (ICEPushState) super.getState();
    }

    public void push() {
        UI ui = getUI();
        if (ui == null) {
            throw new RuntimeException("Must be attached to a UI to push");
        }

        // Push changes
        PushContext pushContext = getPushContext(ui.getSession());
        if (pushContext == null) {
            throw new RuntimeException(
                    "PushContext not initialized. Did you forget to use ICEPushServlet?");
        }
        pushContext.push(getState().getPushGroup());
    }

    public static synchronized PushContext getPushContext(
            VaadinServiceSession vaadinSession) {
        WrappedSession session = vaadinSession.getSession();
        // session.getAttribute(PushContext.class.getName());
        VaadinService service = vaadinSession.getService();
        if (session instanceof WrappedHttpSession) {
            ServletContext servletContext = ((WrappedHttpSession) session)
                    .getHttpSession().getServletContext();
            return (PushContext) servletContext.getAttribute(PushContext.class
                    .getName());
        } else if (session instanceof WrappedPortletSession) {
            PortletContext portletContext = ((WrappedPortletSession) session)
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

    /**
     * Enables push for the given UI
     * 
     * @param ui
     *            the target UI
     */
    public void extend(UI ui) {
        super.extend(ui);
    }
}
