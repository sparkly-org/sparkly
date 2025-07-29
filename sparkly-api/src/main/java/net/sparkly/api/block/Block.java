package net.sparkly.api.block;

import net.sparkly.api.position.Position;

public interface Block {
    Material material();
    
    Position position();
    
    char state();
}
