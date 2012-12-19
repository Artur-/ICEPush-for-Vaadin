package org.vaadin.artur.icepush.client.ui;

import org.icepush.gwt.client.GWTPushContext;
import org.icepush.gwt.client.PushEventListener;
import org.vaadin.artur.icepush.ICEPush;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.user.client.ui.RootPanel;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@Connect(ICEPush.class)
public class ICEPushConnector extends AbstractExtensionConnector {

    private ICEPushListener listener = null;

    private ICEPushRpc rpc = RpcProxy.create(ICEPushRpc.class, this);

    @Override
    protected void init() {
        super.init();

        Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {

            public boolean execute() {
                if (scriptHasBeenInjected()) {
                    listener = registerPushListener(getState().getPushGroup());
                    return false;
                }

                // Keep polling until script has been injected
                return true;
            }
        }, 50);

    }

    @Override
    public ICEPushState getState() {
        return (ICEPushState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        injectScriptIfNeeded(getState().getCodeJavascriptLocation());
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

    private native boolean scriptHasBeenInjected()
    /*-{
        return (typeof $wnd.ice != 'undefined');
    }-*/;

    public class ICEPushListener extends PushEventListener {
        @Override
        public void onPushEvent() {
            VConsole.log("Push event received");
            rpc.push();
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

    @Override
    public void onUnregister() {
        super.onUnregister();

        if (listener != null) {
            unregisterPushListener(listener);
        }

    }

    @Override
    protected void extend(ServerConnector target) {
        // No need to do anything to the UIConnector we extend
    }
}
