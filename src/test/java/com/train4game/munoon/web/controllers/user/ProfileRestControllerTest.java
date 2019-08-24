package com.train4game.munoon.web.controllers.user;

import com.train4game.munoon.model.User;
import com.train4game.munoon.repository.JpaUtil;
import com.train4game.munoon.service.UserService;
import com.train4game.munoon.to.UserTo;
import com.train4game.munoon.utils.JsonUtil;
import com.train4game.munoon.utils.UserUtil;
import com.train4game.munoon.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.train4game.munoon.TestUtil.*;
import static com.train4game.munoon.data.UserTestData.*;
import static com.train4game.munoon.utils.exceptions.ErrorType.VALIDATION_ERROR;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileRestController.REST_URL + "/";

    @Autowired
    public ProfileRestControllerTest(UserService userService, ModelMapper modelMapper, JpaUtil jpaUtil, CacheManager cacheManager, WebApplicationContext webApplicationContext) {
        super(userService, modelMapper, jpaUtil, cacheManager, webApplicationContext);
    }

    @Test
    void testRegistration() throws Exception {
        UserTo user = new UserTo(null, "New User", "email@email.com", "testPassword");

        ResultActions actions = mockMvc.perform(post(REST_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(user, user.getPassword())))
                .andExpect(status().isCreated());

        User created = readFromJson(actions, User.class);
        created.setName(user.getName());
        created.setEmail(user.getEmail());

        assertMatch(userService.getAll(), created, FIRST_USER, SECOND_USER);
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userAuth(FIRST_USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(FIRST_USER));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL)
                .with(userAuth(FIRST_USER)))
                .andExpect(status().isNoContent());
        assertMatch(userService.getAll(), SECOND_USER);
    }

    @Test
    void testUpdate() throws Exception {
        User expected = new User(FIRST_USER);
        UserTo updated = UserUtil.parseTo(expected);
        updated.setName("New Name");
        updated.setEmail("newemail@gmail.com");

        mockMvc.perform(put(REST_URL)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isNoContent());

        assertMatch(userService.get(FIRST_USER_ID), UserUtil.updateFromTo(expected, updated));
    }

    @Test
    void notAuthorized() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateNotUniqueEmail() throws Exception {
        User expected = new User(FIRST_USER);
        UserTo updated = UserUtil.parseTo(expected);
        updated.setEmail(SECOND_USER.getEmail());

        mockMvc.perform(put(REST_URL)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isConflict())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void registerNotUniqueEmail() throws Exception {
        UserTo user = new UserTo(null, "New user", FIRST_USER_EMAIL, "password");
        mockMvc.perform(post(REST_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(user, user.getPassword())))
                .andExpect(status().isConflict())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void registerUnsafeHtml() throws Exception {
        UserTo user = new UserTo(null, "<script>alert(123)</script>", "email@email.com", "password");

        mockMvc.perform(post(REST_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(user, user.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateUnsafeHtml() throws Exception {
        UserTo updated = UserUtil.parseTo(new User(FIRST_USER));
        updated.setName("<script>alert(123)</script>");

        mockMvc.perform(put(REST_URL)
                .with(userAuth(FIRST_USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }
}