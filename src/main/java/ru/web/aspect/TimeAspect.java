package ru.web.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.web.dto.WrapperResponse;

@Component
@Slf4j
@Aspect
public class TimeAspect {
    @Around(value = "execution(*  ru.web.controllers.StudentsController.*(..))") // JoinPoint - точка внедрения (методы), PointCut - набор JoinPoint-ов
    public ResponseEntity<WrapperResponse> addTimeToResponse(ProceedingJoinPoint joinPoint) throws Throwable{
        // Advice - логика которая реализует сквозную функциональность
        long before = System.currentTimeMillis();
        Object response = joinPoint.proceed();
        long after = System.currentTimeMillis();
        return ResponseEntity.ok(WrapperResponse.builder()
                .response(((ResponseEntity)response).getBody())
                .time(after-before)
                .build());
    }
}
