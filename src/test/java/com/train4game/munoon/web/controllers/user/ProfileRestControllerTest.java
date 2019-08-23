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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.train4game.munoon.TestUtil.userAuth;
import static com.train4game.munoon.data.UserTestData.*;
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
                .andExpect(status().isConflict());
    }
}