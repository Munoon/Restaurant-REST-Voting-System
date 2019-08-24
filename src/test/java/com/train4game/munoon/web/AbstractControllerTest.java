package com.train4game.munoon.web;

import com.train4game.munoon.repository.JpaUtil;
import com.train4game.munoon.service.UserService;
import com.train4game.munoon.utils.exceptions.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-db.xml"
})
@Transactional
public class AbstractControllerTest {
    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    protected final UserService userService;
    protected final ModelMapper modelMapper;
    private final JpaUtil jpaUtil;
    private final CacheManager cacheManager;
    private final WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;

    public AbstractControllerTest(UserService userService, ModelMapper modelMapper, JpaUtil jpaUtil, CacheManager cacheManager, WebApplicationContext webApplicationContext) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.jpaUtil = jpaUtil;
        this.cacheManager = cacheManager;
        this.webApplicationContext = webApplicationContext;
    }

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp() {
        cacheManager.getCache("users").clear();
        cacheManager.getCache("votes").clear();
        cacheManager.getCache("meals").clear();
        jpaUtil.clear2ndLevelCache();
    }

    public ResultMatcher errorType(ErrorType type) {
        return jsonPath("$.type").value(type.name());
    }
}
