import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class ServerTest {
    private ChatServer server;

    @Before
    public void setUp() {
        // initialize the Server object
        server = new ChatServer();
    }

    @Test
    public void testSetCoordinatorUsername() {
        // test setting the coordinator username
        server.setCoordinatorUsername("coordinator");
        assertEquals("coordinator", server.getCoordinatorUsername());
    }

}
