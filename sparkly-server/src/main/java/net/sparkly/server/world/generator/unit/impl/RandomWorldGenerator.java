package net.sparkly.server.world.generator.unit.impl;

import net.sparkly.api.block.Material;
import net.sparkly.api.world.World;
import net.sparkly.api.world.chunk.Chunk;
import net.sparkly.api.world.chunk.ChunkSection;
import net.sparkly.server.world.chunk.SparklyChunk;
import net.sparkly.server.world.generator.unit.GenerationUnit;

import java.util.Random;
import java.util.function.Consumer;

import static net.sparkly.server.world.generator.unit.GenerationUnit.CHUNK_RANGE;
import static net.sparkly.server.world.generator.unit.GenerationUnit.CHUNK_SIZE;

public record RandomWorldGenerator(int height) implements Consumer<GenerationUnit> {
    @Override
    public void accept(GenerationUnit unit) {
        Random random = new Random();
        World world = unit.world();
        
        final int size = CHUNK_SIZE * CHUNK_RANGE * 2;
        float[][] map = new float[size][size];
        
        for (int chunkX = -CHUNK_RANGE; chunkX < CHUNK_RANGE; chunkX++) {
            for (int chunkZ = -CHUNK_RANGE; chunkZ < CHUNK_RANGE; chunkZ++) {
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    for (int z = 0; z < CHUNK_SIZE; z++) {
                        int globalX = chunkX * CHUNK_SIZE + x + CHUNK_RANGE * CHUNK_SIZE;
                        int globalZ = chunkZ * CHUNK_SIZE + z + CHUNK_RANGE * CHUNK_SIZE;
                        
                        int heightMap = (int) (random.nextDouble() * height);
                        map[globalX][globalZ] = heightMap;
                    }
                }
            }
        }
        
        float[] kernel = computeKernel(7, 2.5f);
        float[][] temp = new float[size][size];
        int stride = 1;
        
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                float sum = 0f;
                for (int k = -kernel.length + 1; k < kernel.length; k += stride) {
                    int px = x + k;
                    if (px >= 0 && px < size) {
                        sum += map[px][y] * kernel[Math.abs(k)];
                    }
                }
                temp[x][y] = sum;
            }
        }
        
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                float sum = 0f;
                for (int k = -kernel.length + 1; k < kernel.length; k += stride) {
                    int py = y + k;
                    if (py >= 0 && py < size) {
                        sum += temp[x][py] * kernel[Math.abs(k)];
                    }
                }
                map[x][y] = sum;
            }
        }
        
        for (int x = 0; x < map.length; x++) {
            for (int z = 0; z < map.length; z++) {
                map[x][z] = (float) Math.pow(map[x][z], 1.5);
            }
        }
        
        for (int chunkX = -CHUNK_RANGE; chunkX < CHUNK_RANGE; chunkX++) {
            for (int chunkZ = -CHUNK_RANGE; chunkZ < CHUNK_RANGE; chunkZ++) {
                Chunk chunk = world.chunkAt(chunkX, chunkZ);
                
                if (chunk == null) {
                    chunk = new SparklyChunk(chunkX, chunkZ);
                }
                
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    for (int z = 0; z < CHUNK_SIZE; z++) {
                        int globalX = chunkX * CHUNK_SIZE + x + CHUNK_RANGE * CHUNK_SIZE;
                        int globalZ = chunkZ * CHUNK_SIZE + z + CHUNK_RANGE * CHUNK_SIZE;
                        
                        int h = (int) map[globalX][globalZ];
                        
                        for (int y = 0; y <= h; y++) {
                            ChunkSection section = chunk.sectionAt(y >> 4);
                            section.setBlock(x, y & 15, z, Material.STONE.state());
                        }
                    }
                }
                
                world.addChunk(chunkX, chunkZ, chunk);
            }
        }
    }
    
    public static float[] computeKernel(int radius, float sigma) {
        float[] kernel = new float[radius + 1];
        float sum = 0f;
        
        for (int i = 0; i <= radius; i++) {
            float value = (float) Math.exp(-(i * i) / (2f * sigma * sigma));
            kernel[i] = value;
            sum += (i == 0) ? value : value * 2f;
        }
        
        for (int i = 0; i <= radius; i++) {
            kernel[i] /= sum;
        }
        
        return kernel;
    }
}
