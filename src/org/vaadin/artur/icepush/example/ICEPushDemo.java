package org.vaadin.artur.icepush.example;

import org.vaadin.artur.icepush.ICEPush;

import com.vaadin.server.WrappedRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

public class ICEPushDemo extends UI {

    private ICEPush pusher = new ICEPush();

    @Override
    protected void init(WrappedRequest request) {
        // Add the push component
        addComponent(pusher);

        // Add a button for starting background work
        addComponent(new Button("Do stuff in the background",
                new ClickListener() {

                    public void buttonClick(ClickEvent event) {
                        addComponent(new Label(
                                "Waiting for background process to complete..."));
                        new BackgroundThread(UI.getCurrent()).start();
                    }
                }));

    }

    public class BackgroundThread extends Thread {

        private UI ui;

        public BackgroundThread(UI ui) {
            this.ui = ui;
        }

        @Override
        public void run() {
            // Simulate background work
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }

            // Update UI
            synchronized (ICEPushDemo.this) {
                ui.addComponent(new Label("All done"));
            }

            // Push the changes
            pusher.push();
        }

    }
}
