package com.company.jetn.view;

import com.company.jetn.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "my-view", layout = MainView.class)
@ViewController("MyView")
@ViewDescriptor("my-view.xml")
public class MyView extends StandardView {
    @Autowired
    private Notifications notifications;

    @Subscribe
    public void onReady(final ReadyEvent event) {
        notifications.create("Hello from MyView")
                .withType(Notifications.Type.SUCCESS)
                .show();
    }
}
