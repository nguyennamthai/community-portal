package thai.interceptors;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class DatabaseAccessAspect {
    @AfterReturning("execution(public * org.springframework.data.repository.Repository+.save(*))")
    public void afterSaving() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Saved a record");
    }
}
