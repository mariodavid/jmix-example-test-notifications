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
