package com.monitor.base;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author: chenjh
 * @description:
 * @create: 2018-12-25 13:42
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestBase {
    @Autowired
    private WebApplicationContext context;

    public MockMvc mockMvc;
    @Before
    public void setUp() throws Exception {
        // 构造MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
}
