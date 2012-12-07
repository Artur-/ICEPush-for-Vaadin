package org.vaadin.artur.icepush.example;

import org.vaadin.artur.icepush.ICEPush;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ICEPushDemo extends UI {

    private ICEPush pusher = new ICEPush();
    private VerticalLayout layout;

    @Override
    protected void init(VaadinRequest request) {
        layout = new VerticalLayout();
        setContent(layout);
        // Add the push component
        pusher.extend(this);
        // Add a button for starting background work
        layout.addComponent(new Button("Do stuff in the background",
                new ClickListener() {

                    public void buttonClick(ClickEvent event) {
                        layout.addComponent(new Label(
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
            ui.getSession().lock();

            try {
                layout.addComponent(new Label("All done"));
            } finally {
                ui.getSession().unlock();
            }

            // Push the changes
            pusher.push();
        }

    }
}
