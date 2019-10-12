package com.spring.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.PostConstruct;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class CommonInterceptor implements HandlerInterceptor {

    private Map<String,User> userMap = new HashMap<>();

    @PostConstruct
    public void setup() {
        User user = new User("","yeoseong_gae",28);
        userMap.put("yeoseong-gae",user);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("interceptor");
        String userId = request.getParameter("userId");

        IsAuth annotation = getAnnotation((HandlerMethod)handler, IsAuth.class);

        Auth auth = null;

        if(!ObjectUtils.isEmpty(annotation)){
            auth = annotation.isAuth();
            //NONE이면 PASS
            if(auth == Auth.AUTH){
                if(ObjectUtils.isEmpty(userMap.get(userId))){
                    log.info("auth fail");
                    throw new AuthenticationException("유효한 사용자가 아닙니다.");
                }
            }
        }
        return true;
    }

    private <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
        return Optional.ofNullable(handlerMethod.getMethodAnnotation(annotationType))
                .orElse(handlerMethod.getBeanType().getAnnotation(annotationType));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class User{
        private String id;
        private String name;
        private int age;
    }
}
