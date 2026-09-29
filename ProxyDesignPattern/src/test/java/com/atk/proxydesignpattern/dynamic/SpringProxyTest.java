package com.atk.proxydesignpattern.dynamic;

import com.atk.proxydesignpattern.service.UserService;
import com.atk.proxydesignpattern.service.UserServiceProxy;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Spring creates proxies too: @Transactional on UserServiceImpl is applied by an AOP proxy. */
@SpringBootTest
class SpringProxyTest {

    @Autowired
    @Qualifier("userServiceImpl")
    UserService realSubject;

    @Autowired
    UserService primary;

    @Test
    void springWrapsTransactionalServiceInAnAopProxy() {
        assertTrue(AopUtils.isAopProxy(realSubject));
    }

    @Test
    void handWrittenProtectionProxyIsThePrimaryBean() {
        assertInstanceOf(UserServiceProxy.class, primary);
    }
}
