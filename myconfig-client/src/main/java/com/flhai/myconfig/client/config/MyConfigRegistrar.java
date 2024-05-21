package com.flhai.myconfig.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Optional;

public class MyConfigRegistrar implements ImportBeanDefinitionRegistrar {

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        System.out.println("==> register PropertySourcesProcessor");
        // avoid duplicate registration
        Optional<String> first = Arrays.stream(registry.getBeanDefinitionNames()).filter(
                        x -> PropertySourcesProcessor.class.getName().equals(x))
                .findFirst();
        if (first.isPresent()) {
            System.out.println("PropertySourcesProcessor already registered");
            return;
        }
        // create bean definition
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(PropertySourcesProcessor.class).getBeanDefinition();
        // register bean definition
        registry.registerBeanDefinition(PropertySourcesProcessor.class.getName(), beanDefinition);

    }
}
