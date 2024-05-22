package com.flhai.myconfig.client.value;

import cn.kimmking.utils.FieldUtils;
import com.flhai.myconfig.client.util.PlaceholderHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 1. scan all @Value
 * 2. replace with value from myconfig-server when config changes
 */
@Slf4j
public class SpringValueProcessor implements BeanPostProcessor, ApplicationListener<EnvironmentChangeEvent>, BeanFactoryAware {

    static final PlaceholderHelper placeholderHelper = PlaceholderHelper.getInstance();
    static MultiValueMap<String, SpringValue> VALUE_HOLDER = new LinkedMultiValueMap<>();

    private ConfigurableBeanFactory beanFactory;


    /**
     * step 1
     *
     * @param bean
     * @param beanName
     * @return
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        List<Field> annotatedFieldList = FieldUtils.findAnnotatedField(bean.getClass(), Value.class);
        annotatedFieldList.forEach(field -> {
            log.info("[MYCONFIG] postProcessBeforeInitialization: bean:" + beanName + ", field:" + field.getName());
            Value value = field.getAnnotation(Value.class);
            // value might be SpEL expression
            String propertyValue = value.value();
            placeholderHelper.extractPlaceholderKeys(propertyValue).forEach(key -> {
                log.info("[MYCONFIG] find spring value: " + key);
                SpringValue springValue = new SpringValue(bean, beanName, key, propertyValue, field);
                VALUE_HOLDER.add(key, springValue);
            });
        });

        return bean;
    }


    /**
     * step 2
     * 等价于
     *
     * @EventListener public void onChange(EnvironmentChangeEvent event){}
     */
    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        log.info(VALUE_HOLDER.toString());
        event.getKeys().forEach(key -> {
            log.info("[MYCONFIG] update spring value: " + key);
            List<SpringValue> springValues = VALUE_HOLDER.get(key);
            if (springValues == null || springValues.size() == 0) {
                return;
            }
            springValues.forEach(springValue -> {
                log.info("[MYCONFIG] update spring value: " + springValue.getBeanName() + ", for key " + springValue.getField().getName());
                Object value = placeholderHelper.resolvePropertyValue(beanFactory, springValue.getBeanName(), springValue.getPlaceholder());

                log.info("[MYCONFIG] resolve placeholder: " + springValue.getPlaceholder() + " to value: " + value);
                Field field = springValue.getField();
                try {
                    field.setAccessible(true);
                    field.set(springValue.getBean(), value);
                } catch (IllegalAccessException e) {
                    log.error("[MYCONFIG] update spring value error", e);
                }
            });
        });
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
    }
}
