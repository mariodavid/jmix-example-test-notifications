package com.company.jetn.test_support;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.notification.Notification;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponentProperties;

import java.util.ArrayList;
import java.util.List;

public class TestNotifications extends Notifications {

    private final List<Notification> notifications = new ArrayList<>();

    public TestNotifications(UiComponentProperties uiComponentProperties) {
        super(uiComponentProperties);
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void clear() {
        notifications.clear();
    }

    @Override
    public NotificationBuilder create(String text) {
        return new TestNotificationBuilder(text);
    }

    @Override
    public NotificationBuilder create(String text, String description) {
        return new TestNotificationBuilder(text, description);
    }

    @Override
    public NotificationBuilder create(Component component) {
        return new TestNotificationBuilder(component);
    }

    class TestNotificationBuilder extends NotificationBuilder {

        public TestNotificationBuilder(String text) {
            super(text);
        }

        public TestNotificationBuilder(String text, String description) {
            super(text, description);
        }

        public TestNotificationBuilder(Component component) {
            super(component);
        }

        @Override
        public Notification build() {
            Notification build = super.build();
            notifications.add(build);
            return build;
        }
    }
}
