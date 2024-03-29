import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class ManagerTest {
    private ChatServer server;
    private Manager manager;

    @Before
    public void setUp() {
        server = mock(ChatServer.class);
        manager = new Manager(mock(Socket.class), server);
    }

    @Test
    public void testGenerateRandomIPAddress() {
        String ipAddress = manager.generateRandomIPAddress();
        // add assertions to validate the generated IP address
        assertEquals(11, ipAddress.length()); 
    }

}
