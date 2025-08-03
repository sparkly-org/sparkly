package net.sparkly.api.block;

import net.sparkly.api.position.Vector;

public interface Block {
    Material material();
    
    Vector position();
    
    int data();
    
    char state();
}
