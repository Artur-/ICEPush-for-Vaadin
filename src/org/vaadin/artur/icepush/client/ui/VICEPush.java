package org.vaadin.artur.icepush.client.ui;

import org.icepush.gwt.client.GWTPushContext;
import org.icepush.gwt.client.PushEventListener;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VICEPush extends Widget implements Paintable {

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-icepush";

    public static final String PUSH_GROUP = "pushGroup";

    public static final String ICEPUSH_JS_LOCATION = "codeLocation";

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

        // Must be done after push context path has been set
        injectScriptIfNeeded(uidl.getStringAttribute(ICEPUSH_JS_LOCATION));

        pushGroup = uidl.getStringAttribute(PUSH_GROUP);

        if (listener == null) {
            DeferredCommand.addCommand(new Command() {

                @Override
                public void execute() {
                    if (scriptHasBeenInjected()) {
                        listener = registerPushListener(pushGroup);
                    } else {
                        DeferredCommand.addCommand(this);
                    }

                }
            });

        }
    }

    public class ICEPushListener extends PushEventListener {
        @SuppressWarnings("deprecation")
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

    private void unregisterPushListener(ICEPushListener listener) {
        GWTPushContext context = GWTPushContext.getInstance();
        context.removePushEventListener(listener);
    }

    private boolean injectScriptIfNeeded(String codeLocation) {
        if (scriptHasBeenInjected()) {
            return false;
        }

        if (codeLocation == null) {
            // widgetset directory -> servlet
            codeLocation = "../../../code.icepush";
        }
        ScriptElement se = Document.get().createScriptElement();
        se.setId("VICEPush-code-inject");
        se.setSrc(codeLocation);
        se.setLang("JavaScript");
        RootPanel.get().getElement().appendChild(se);

        return true;
    }

    //
    private native boolean scriptHasBeenInjected()
    /*-{
        return (typeof $wnd.ice != 'undefined');
    }-*/;

    @Override
    protected void onDetach() {
        super.onDetach();
        if (listener != null)
            unregisterPushListener(listener);
    }
}
