package edu.tamu.sage;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SageApplication.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SageApplicationTests {

    @Test
    public void contextLoads() {
        assertTrue(true);
    }

}
