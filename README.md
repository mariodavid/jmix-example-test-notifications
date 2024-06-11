# Jmix Example: Test Notifications

This example demonstrates how to use the `Notifications` API in UI tests.

### 1. Extend the Notification Bean from Jmix

````java
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
````

### 2. Test Configuration

Override your new Bean in the Spring Test Configuration:

````java
package com.company.jetn.test_support;

import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponentProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestNotificationConfiguration {

    @Bean("flowui_Notifications")
    public Notifications notifications(UiComponentProperties uiComponentProperties) {
        return new TestNotifications(uiComponentProperties);
    }
}
````


### 3. View that uses Notifications

````java
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
````

### 4. Use the Notifications in UI Tests

````java
package com.company.jetn;

import com.company.jetn.test_support.TestNotificationConfiguration;
import com.company.jetn.test_support.TestNotifications;
import com.company.jetn.view.MyView;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.testassist.FlowuiTestAssistConfiguration;
import io.jmix.flowui.testassist.UiTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


@UiTest
@SpringBootTest(

        properties = {
                // allow overriding the bean definition so that the
                // TestNotifications bean can replace the Framework bean
                "spring.main.allow-bean-definition-overriding=true"
        },
        classes = {
                JmixExampleTestNotificationsApplication.class,
                FlowuiTestAssistConfiguration.class,

                // add the TestNotificationConfiguration class to the test context
                // so that the TestNotifications bean can be created
                TestNotificationConfiguration.class,
        }
)
class MyViewTest {

    // use TestNotifications instead of Notifications
    // to have access to the test specific methods
    @Autowired
    TestNotifications testNotifications;

    @Autowired
    ViewNavigators viewNavigators;


    @BeforeEach
    void setUp() {
        // since it is a shared state,
        // we need to clear the notifications before each test
        testNotifications.clear();
    }

    @Test
    void when_viewIsReady_then_notificationIsDisplayed() {
        // when:
        viewNavigators.view(MyView.class).navigate();

        // then:
        // use getNotifications() to access the notifications
        // that were created in the production code
        assertThat(testNotifications.getNotifications())
                .hasSize(1)
                .first()
                .extracting(notification -> notification.getElement().toString())
                .matches(s -> s.contains("Hello from MyView"));
    }
}
````
