package com.galsie.gcs.microservicecommon.lib.galsecurity.init;

import com.galsie.gcs.microservicecommon.lib.galsecurity.request.*;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.lib.utils.StringUtils;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to find all the paths that must be authenticated by GalSecurity. it contains a map of all the paths
 * and their corresponding {@link AuthenticatedGCSRequestObject} objects. on creation its scans for methods annotated with PostMapping or GetMapping
 * and then scans for {@link AuthenticatedGCSRequests},then {@link AuthenticatedGCSRequest},then {@link UserAuthenticatedGCSRequest}
 * annotations on those methods. it then adds the path of the method to the map
 * This class is used by the {@link com.galsie.gcs.microservicecommon.lib.galsecurity.request.filters.CustomGalSecurityRequestFilter}
 * to know autoconfigured paths that need to be authenticated and their strategies
 */
@Getter
public class AuthenticatedGCSRequestAutoConfPathsProvider implements BeanPostProcessor, Ordered {

    Map<String, List<AuthenticatedGCSRequestObject>> pathsToAuthenticatedGCSRequestObjectMap = new HashMap<>();


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    /**
     * This method is called after the bean is created. it scans for methods annotated with PostMapping or GetMapping before
     * scanning for {@link AuthenticatedGCSRequests}, then @{@link AuthenticatedGCSRequest}, then {@link UserAuthenticatedGCSRequest}.
     * each of these annotations are used to create a {@link AuthenticatedGCSRequestObject} object and added to a list.
     * if the list is not empty then we add the path to the pathsToAuthenticatedGCSRequestObjectMap map
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass(); // get the bean class
        var requestMapping = AnnotationUtils.findAnnotation(beanClass, RequestMapping.class);
        // Get the base path of the rest controller (so we can join the paths all @RequestMapping methods in the controller to this base path)
        String[] basePaths = requestMapping == null ? new String[]{}: requestMapping.path();
        if (basePaths.length == 0){
            basePaths = new String[]{""};
        }
        // Loop through all the methods in the bean
        for (Method method: beanClass.getDeclaredMethods()) {
            // if the method does not have the @RequestMappingAnnotation, skip
            PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
            GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
            if (postMapping == null && getMapping == null) {
                continue;
            }
            // if the method does not have the @AuthenticatedGCSRequest annotation, skip
            List<AuthenticatedGCSRequestObject> authenticatedGCSRequestObjects = new ArrayList<>();
            var authenticatedGCSRequests = method.getAnnotation(AuthenticatedGCSRequests.class);
            if(authenticatedGCSRequests != null) {
                for (var authenticatedGCSRequest : authenticatedGCSRequests.value()) {
                    authenticatedGCSRequestObjects.add(fromAnnotation(authenticatedGCSRequest));
                }
            }
            var authenticatedGCSRequest = method.getAnnotation(AuthenticatedGCSRequest.class);
            if (authenticatedGCSRequest != null) {
                authenticatedGCSRequestObjects.add(fromAnnotation(authenticatedGCSRequest));
            }
            var userAuthenticatedGCSRequest = method.getAnnotation(UserAuthenticatedGCSRequest.class);
            if (userAuthenticatedGCSRequest != null) {
                AuthenticatedGCSRequestObject userAuthenticatedGCSRequestObject = new AuthenticatedGCSRequestObject(AuthenticationStrategy.AND, new GalSecurityAuthSessionType[]{GalSecurityAuthSessionType.USER, GalSecurityAuthSessionType.GCS_API_CLIENT});
                authenticatedGCSRequestObjects.add(userAuthenticatedGCSRequestObject);
            }
            if (authenticatedGCSRequestObjects.isEmpty()){
                continue;
            }
            String[] subPaths = postMapping != null ? postMapping.path() : getMapping.path();
            // put the path for all session types that must authenticate this path
           for(var authenticatedGCSRequestObject: authenticatedGCSRequestObjects) {
               for (String basePath : basePaths) {
                   for (String subPath : subPaths) {
                       this.auxAddPath(StringUtils.joinPaths(basePath, subPath), authenticatedGCSRequestObject);
                   }
               }
           }
        }
        return bean;
    }

    /**
     * This method takes a path and a {@link AuthenticatedGCSRequestObject} and adds to pathsForAuthSessionType
     * @param path the path to add
     * @param authenticatedGCSRequestObject the {@link AuthenticatedGCSRequestObject} to be added to list of {@link AuthenticatedGCSRequestObject} for the path
     */
    private void auxAddPath(String path, AuthenticatedGCSRequestObject authenticatedGCSRequestObject){
        if (path.isEmpty() || path.charAt(0) != '/'){
            path = "/" + path;
        }
        var list =  pathsToAuthenticatedGCSRequestObjectMap.getOrDefault(path, new ArrayList<>());
        list.add(authenticatedGCSRequestObject);
        pathsToAuthenticatedGCSRequestObjectMap.put(path, list);
    }

    public AuthenticatedGCSRequestObject fromAnnotation(AuthenticatedGCSRequest authenticatedGCSRequest){
        return new AuthenticatedGCSRequestObject(authenticatedGCSRequest.authenticationStrategy(), authenticatedGCSRequest.authSessionTypes());
    }

}
