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

    /**
     * Mock User를 등록한다. 실무에서는 헤더나 쿠키로 넘어오는 토큰을 분석하여
     * 유효한 유저인지 체크하는 로직이 들어갈 것이다.
     */
    @PostConstruct
    public void setup() {
        User user = new User("","yeoseong_gae",28);
        userMap.put("yeoseong-gae",user);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getParameter("userId"); //이런경우는 없다. 실무에서 유저정보가 보통 헤더나 쿠키에 토큰으로 넘어온다.
        IsAuth annotation = getAnnotation((HandlerMethod)handler, IsAuth.class);

        Auth auth = null;

        if(!ObjectUtils.isEmpty(annotation)){
            auth = annotation.isAuth();
            //NONE이면 PASS
            if(auth == Auth.AUTH){
                if(ObjectUtils.isEmpty(userMap.get(userId))){
                    throw new AuthenticationException("유효한 사용자가 아닙니다."); //가장 네이밍이 좋아서 사용한 예외클래스이다. 실제는 웹 인증에 사용되지 않는 예외클래스이다.
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
