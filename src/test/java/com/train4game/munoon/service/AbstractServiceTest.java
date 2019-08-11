package com.train4game.munoon.service;

import com.train4game.munoon.TimingExtensions;
import com.train4game.munoon.repository.JpaUtil;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.Map;

import static com.train4game.munoon.utils.ValidationUtils.getRootCause;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ExtendWith(TimingExtensions.class)
public abstract class AbstractServiceTest {
    private static final Logger log = LoggerFactory.getLogger("result");
    private static Map<String, Long> statistics = new HashMap<>();

    @Autowired
    protected CacheManager cacheManager;

    @Autowired
    protected JpaUtil jpaUtil;

    public <T extends Throwable> void validateRootCause(Runnable runnable, Class<T> exceptionClass) {
        assertThrows(exceptionClass, () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw getRootCause(e);
            }
        });
    }
}
