# 项目结构

myconfig-server: 配置中心服务端，负责配置的存储、发布、同步等。
myconfig-client: 配置中心客户端，负责配置的获取、更新等。
myconfig-demo: 配置中心客户端demo。

# 知识点

## ImportBeanDefinitionRegistrar



ImportBeanDefinitionRegistrar 允许开发者以编程方式向 Spring 容器中注册 Bean，而不仅仅是通过 XML 或注解的静态配置。

```java
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

public class MyBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(MyService.class);
        registry.registerBeanDefinition("myService", beanDefinition);
    }
}


// 在配置类中使用
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MyBeanDefinitionRegistrar.class)
public class AppConfig {
}
```

典型应用场景

1. **条件性 Bean 注册**：根据特定条件动态注册 Bean。例如，基于某些配置或运行时参数决定是否注册某个 Bean。
2. **自动化配置**：在一些 Spring Boot Starter 中，通过 `ImportBeanDefinitionRegistrar` 实现自动化配置，简化应用程序的配置。
3. **跨模块 Bean 定义**：在复杂项目中，不同模块可能需要动态互相注册 Bean，通过 `ImportBeanDefinitionRegistrar` 可以实现这种需求。
4. **自定义注解处理**：开发自定义注解，并通过 `ImportBeanDefinitionRegistrar` 实现自定义注解的处理逻辑，注册相应的 Bean。



## BeanFactoryPostProcessor

`BeanFactoryPostProcessor` 是 Spring 框架中的一个扩展接口，用于在 Spring 容器初始化 Bean 之前对 Bean 定义（BeanDefinition）进行修改。`BeanFactoryPostProcessor` 提供了一个在容器实例化任何 Bean 之前进行干预的机会，因此它适用于需要对 Bean 配置进行全局修改的场景。

`BeanFactoryPostProcessor` 只有一个方法：

```java
void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
```

它适用于各种需要全局调整 Bean 配置的场景，包括属性占位符解析、动态修改 Bean 属性、注册新的 Bean 定义以及调整 Bean 初始化顺序等。

```java
 public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 应用启动时动态设置某些 Bean 的属性
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("myBean");
        beanDefinition.getPropertyValues().add("property", "newValue");
        // 根据某些条件在运行时动态添加或修改 Bean
        if (someCondition()) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MyBean.class);
            beanFactory.registerBeanDefinition("myDynamicBean", beanDefinitionBuilder.getBeanDefinition());
        }
        // 确保某个 Bean 在另一个 Bean 之前被初始化：
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("dependentBean");
        beanDefinition.setDependsOn("dependencyBean");
    }
}


```