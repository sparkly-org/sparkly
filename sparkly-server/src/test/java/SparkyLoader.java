import net.sparkly.api.event.Event;
import net.sparkly.server.MinecraftServer;
import net.sparkly.server.event.EventNode;
import net.sparkly.server.event.impl.PlayerChatEvent;

public class SparkyLoader {
    
    public static void main(String[] args) {
        MinecraftServer server = new MinecraftServer();
        server.start();

        EventNode<Event> handler = server.eventHandler();
        handler.addListener(PlayerChatEvent.class, event ->
            server.logger().info("{} said {}", event.player().name(), event.message()));
    }
}
