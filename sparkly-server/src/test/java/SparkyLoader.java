import net.sparkly.api.block.Block;
import net.sparkly.api.event.Event;
import net.sparkly.api.world.World;
import net.sparkly.server.MinecraftServer;
import net.sparkly.server.event.EventNode;
import net.sparkly.server.event.impl.PlayerChatEvent;
import net.sparkly.server.world.generator.unit.impl.RandomWorldGenerator;

public class SparkyLoader {
    
    public static void main(String[] args) {
        MinecraftServer server = new MinecraftServer();
        server.setChunkGenerator(new RandomWorldGenerator(30));
        server.start();
        
        World world = server.worlds().getFirst();
        
        for (int i = 0; i < 5; i++) {
            Block block = world.blockAt(3, i, 4);
            
            System.out.println(block.material());
        }
        
        EventNode<Event> handler = server.eventHandler();
        handler.addListener(PlayerChatEvent.class, event ->
            server.logger().info("{} said {}", event.player().name(), event.message()));
    }
}
