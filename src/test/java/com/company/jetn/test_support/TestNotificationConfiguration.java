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
