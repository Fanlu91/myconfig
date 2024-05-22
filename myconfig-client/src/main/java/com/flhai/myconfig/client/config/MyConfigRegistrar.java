package com.flhai.myconfig.client.config;

import com.flhai.myconfig.client.value.SpringValueProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class MyConfigRegistrar implements ImportBeanDefinitionRegistrar {

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        log.info("==> register PropertySourcesProcessor");
        // avoid duplicate registration

        registerClass(registry, PropertySourcesProcessor.class);
        registerClass(registry, SpringValueProcessor.class);

    }

    private static void registerClass(BeanDefinitionRegistry registry, Class<?> aClass) {
        log.info("register class: {}", aClass.getName());
        Optional<String> first = Arrays.stream(registry.getBeanDefinitionNames()).filter(
                        x -> aClass.getName().equals(x))
                .findFirst();
        if (first.isPresent()) {
            log.info("PropertySourcesProcessor already registered");
            return;
        }
        // create bean definition
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(aClass).getBeanDefinition();
        // register bean definition
        registry.registerBeanDefinition(aClass.getName(), beanDefinition);
    }
}
