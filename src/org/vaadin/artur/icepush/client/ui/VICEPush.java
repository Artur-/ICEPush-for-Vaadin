package org.vaadin.artur.icepush.client.ui;

import org.icepush.gwt.client.GWTPushContext;
import org.icepush.gwt.client.PushEventListener;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

/**
 * Client side widget which communicates with the server. Messages from the
 * server are shown as HTML and mouse clicks are sent to the server.
 */
public class VICEPush extends Widget implements Paintable {

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-icepush";

    public static final String PUSH_URL = "pushUrl";
    public static final String PUSH_GROUP = "pushGroup";

    /** The client side widget identifier */
    protected String paintableId;

    /** Reference to the server connection object. */
    protected ApplicationConnection client;

    private String pushGroup;

    private ICEPushListener listener;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VICEPush() {
        setElement(Document.get().createDivElement());
        getElement().getStyle().setDisplay(Display.NONE);

    }

    /**
     * Called whenever an update is received from the server
     */
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // This call should be made first.
        // It handles sizes, captions, tooltips, etc. automatically.
        if (client.updateComponent(this, uidl, true)) {
            // If client.updateComponent returns true there has been no changes
            // and we
            // do not need to update anything.
            return;
        }

        // Save reference to server connection object to be able to send
        // user interaction later
        this.client = client;

        // Save the client side identifier (paintable id) for the widget
        paintableId = uidl.getId();

        pushGroup = uidl.getStringAttribute(PUSH_GROUP);

        if (listener == null) {
            listener = registerPushListener(pushGroup);
        }
    }

    public class ICEPushListener extends PushEventListener {
        @Override
        public void onPushEvent() {
            ApplicationConnection.getConsole().log("Push event received");
            client.updateVariable(paintableId, "pushEvent", PUSH_GROUP, true);
        }
    }

    private ICEPushListener registerPushListener(String pushGroup) {
        GWTPushContext context = GWTPushContext.getInstance();
        ICEPushListener pl = new ICEPushListener();
        context.addPushEventListener(pl, pushGroup);
        return pl;
    }

}
