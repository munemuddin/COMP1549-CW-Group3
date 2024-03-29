import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class ServerTest {
    private ChatServer server;

    @Before
    public void setUp() {
        // initializs the server object
        server = new ChatServer();
    }

    @Test
    public void testSetCoordinatorUsername() {
        // test setting the coordinator username
        server.setCoordinatorUsername("Coordinator");
        assertEquals("Coordinator", server.getCoordinatorUsername());
    }

   
}
