package com.galsie.gcs.serviceutilslibrary.utils.transactions;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

public class TransactionProxyCreator {
    /**
     * Creates a proxy for the given target object and applies transaction management to it.
     * This method should be used when creating a Spring bean in a @Configuration class
     * for services that require transactional management and are not being created
     * using Spring's stereotype annotations (@Service, @Component, etc.).
     *
     * @param transactionManager the PlatformTransactionManager responsible for managing transactions
     * @param target the target object to create a transactional proxy for
     * @param <T> the type of the target object
     * @return a proxy of the same type as the target object, with transaction management applied
     */
    public static <T> T createTransactionalProxy(TransactionManager transactionManager, T target) {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(transactionManager);
        transactionInterceptor.setTransactionAttributeSource(new AnnotationTransactionAttributeSource());
        var proxy = new ProxyFactory(target);
        proxy.addAdvice(transactionInterceptor);
        return (T) proxy.getProxy();
    }
}