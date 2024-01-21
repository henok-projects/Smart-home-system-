package com.galsie.gcs.microservicecommon.lib.gcssockets.listener.registry;
import com.galsie.gcs.microservicecommon.lib.gcssockets.handler.GCSSocketHandler;
import com.galsie.gcs.microservicecommon.lib.gcssockets.packet.Packet;
import com.galsie.gcs.microservicecommon.lib.gcssockets.GalWebSocketSession;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Slf4j
public class RegisteredOnPacketMethod {

    private Object bean;

    private Method method;

    private int getMethodParamCount(){
        return this.method.getParameterCount();
    }

    public void invokeMethod(Class<? extends GCSSocketHandler> handlerClass, GalWebSocketSession webSocketSession, Packet packet){
        int noOfParams = this.getMethodParamCount();
        try {
            if (noOfParams == 2) {
                method.invoke(bean, webSocketSession, packet);
            } else if (noOfParams == 3) {
                method.invoke(bean, handlerClass, webSocketSession, packet);
            }else{
                log.error("Failed to invoke method for the handler " + handlerClass.getName() + "  reason: Number of parameters is not 2 or 3");
            }
        }catch (Exception exception){
            log.error("Failed to invoke method for the handler " + handlerClass.getName() + " reason: " + exception);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisteredOnPacketMethod that)) return false;
        return Objects.equals(getBean(), that.getBean()) && Objects.equals(getMethod(), that.getMethod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBean(), getMethod());
    }


}
