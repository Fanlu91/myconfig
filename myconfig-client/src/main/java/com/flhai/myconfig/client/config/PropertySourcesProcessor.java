package com.flhai.myconfig.client.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * my property sources processor
 * add my property source to environment
 */
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, EnvironmentAware, PriorityOrdered {
    Environment environment;
    private final static String MY_PROPERTY_SOURCE = "MyPropertySource";
    private final static String MY_COMPOSITE_PROPERTY_SOURCES = "MyCompositePropertySources";

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // Environment doesn't have getPropertySources method
        ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) this.environment;
        if (configurableEnvironment.getPropertySources().contains(MY_COMPOSITE_PROPERTY_SOURCES)) {
            return;
        }

        String app = configurableEnvironment.getProperty("myconfig.app", "app1");
        String env = configurableEnvironment.getProperty("myconfig.env", "dev");
        String ns = configurableEnvironment.getProperty("myconfig.ns", "public");
        String configServer = configurableEnvironment.getProperty("myconfig.configServer", "http://localhost:9129");

        ConfigMeta configMeta = new ConfigMeta(app, env, ns, configServer);
        MyConfigService configService = MyConfigService.getDefault(configMeta);

        MyPropertySource propertySource = new MyPropertySource(MY_PROPERTY_SOURCE, configService);
        CompositePropertySource composite = new CompositePropertySource(MY_COMPOSITE_PROPERTY_SOURCES);
        composite.addPropertySource(propertySource);
        configurableEnvironment.getPropertySources().addFirst(composite);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
