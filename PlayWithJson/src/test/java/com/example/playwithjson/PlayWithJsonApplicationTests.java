package com.example.playwithjson;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "app.data-file=target/test-data/entries.json")
class PlayWithJsonApplicationTests {

    @Test
    void contextLoads() {
    }

}
