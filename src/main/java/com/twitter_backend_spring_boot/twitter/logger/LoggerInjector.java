package com.twitter_backend_spring_boot.twitter.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class LoggerInjector implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.getType() == Logger.class && field.isAnnotationPresent(InjectLogger.class)) {
                boolean accessible = field.canAccess(bean);
                field.setAccessible(true);
                try {
                    Logger logger = LoggerFactory.getLogger(bean.getClass());
                    field.set(bean, logger);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to inject logger into " + bean.getClass(), e);
                }
                field.setAccessible(accessible);
            }
        }

        return bean;
    }
}
