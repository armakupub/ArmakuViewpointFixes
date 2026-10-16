package pzmod.armakuviewpointfixes;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import viewpoint.render.SceneData;

// IrisWeather's vertex layout, so iris/weather_vertex.glsl and the packs' gbuffers_weather read it unchanged.
// Iris space is eye-relative, with x and z opposite to Viewpoint's world.
public class Rain {
    private static final int RADIUS = 12;
    private static final float FULL = 6.0f;
    private static final float CLOSEST = 0.8f;
    private static final int LAYERS = 2;
    private static final float BELOW = 6.0f;
    private static final float ABOVE = 12.0f;
    private static final float METRES_PER_V = 4.0f;
    private static final float RAIN_RATE = 2.3f;
    private static final float SNOW_RATE = 0.28f;
    private static final float MAX_SLANT = 0.6f;
    private static final int FLOATS = 12;
    private static final int TEX_W = 128;
    private static final int TEX_H = 512;
    private static final int MAX_VERTICES = (2 * RADIUS + 1) * (2 * RADIUS + 1) * LAYERS * 6 * 2;

    private static final FloatBuffer data = BufferUtils.createFloatBuffer(MAX_VERTICES * FLOATS);
    private static int vao;
    private static int vbo;
    private static int rainTexture;
    private static int snowTexture;
    private static int rainCount;
    private static int snowCount;
    private static boolean ready;
    private static boolean broken;

    public static boolean active() {
        return !broken && Settings.RAIN.get();
    }

    public static boolean update(SceneData scene, float eyeX, float eyeZ, double time) {
        rainCount = 0;
        snowCount = 0;
        if (scene.rain <= 0.0f && scene.snow <= 0.0f) {
            return false;
        }
        if (!ready && !init()) {
            return false;
        }
        float windX = -scene.windX;
        float windZ = -scene.windZ;
        data.clear();
        rainCount = columns(scene.rain, eyeX, eyeZ, time, windX, windZ, false);
        snowCount = columns(scene.snow, eyeX, eyeZ, time, windX, windZ, true);
        data.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0L, data);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return rainCount + snowCount > 0;
    }

    private static int columns(float strength, float eyeX, float eyeZ, double time, float windX, float windZ, boolean snow) {
        if (strength <= 0.0f) {
            return 0;
        }
        int start = data.position();
        int baseX = (int) Math.floor(-eyeX);
        int baseZ = (int) Math.floor(-eyeZ);
        float fracX = -eyeX - baseX;
        float fracZ = -eyeZ - baseZ;
        float keepBase = 0.35f + 0.65f * strength;
        for (int i = -RADIUS; i <= RADIUS; ++i) {
            for (int j = -RADIUS; j <= RADIUS; ++j) {
                float x = j + 0.5f - fracX;
                float z = i + 0.5f - fracZ;
                float d = (float) Math.sqrt(x * x + z * z);
                if (d > RADIUS || d < CLOSEST) {
                    continue;
                }
                long cell = (long) (baseX + j) * 3121L + (long) (baseZ + i) * 45238971L;
                float edge = d <= FULL ? 0.0f : (d - FULL) / (RADIUS - FULL);
                float keep = keepBase * (1.0f - 0.7f * edge * edge);
                float alpha = strength * (1.0f - 0.6f * edge);
                for (int layer = 0; layer < LAYERS; ++layer) {
                    float h = hash(cell, layer * 2);
                    if (hash(cell, layer * 2 + 1) > keep) {
                        continue;
                    }
                    sheet(x, z, d, layer, h, time, windX, windZ, alpha, snow);
                }
            }
        }
        return (data.position() - start) / FLOATS;
    }

    private static void sheet(float x, float z, float d, int layer, float h, double time,
                              float windX, float windZ, float alpha, boolean snow) {
        float sideX = z / d * 0.5f;
        float sideZ = -x / d * 0.5f;
        float shift = (h - 0.5f) * 0.6f;
        float cx = x + sideX * shift * (layer == 0 ? 1.0f : -1.0f);
        float cz = z + sideZ * shift * (layer == 0 ? 1.0f : -1.0f);

        double rate = snow ? SNOW_RATE * (0.7 + 0.6 * h) : RAIN_RATE * (0.85 + 0.3 * h);
        float scroll = (float) ((-time * rate + h * 7.0 + layer * 0.5) % 1024.0);
        float fall = (float) rate * METRES_PER_V;
        float slantX = clamp(windX / fall, -MAX_SLANT, MAX_SLANT);
        float slantZ = clamp(windZ / fall, -MAX_SLANT, MAX_SLANT);
        float u0 = h + layer * 0.37f;
        if (snow) {
            u0 += 0.12f * (float) Math.sin(time * (0.6 + 0.5 * h) + h * 6.2831853);
        }

        float top = scroll;
        float bottom = scroll + (ABOVE + BELOW) / METRES_PER_V;
        float[][] corners = {
            {-1.0f, -BELOW, 0.0f, bottom},
            {1.0f, -BELOW, 1.0f, bottom},
            {1.0f, ABOVE, 1.0f, top},
            {-1.0f, ABOVE, 0.0f, top},
        };
        for (int index : new int[] {0, 1, 2, 0, 2, 3}) {
            float[] c = corners[index];
            float y = c[1];
            data.put(cx + sideX * c[0] - slantX * y).put(y).put(cz + sideZ * c[0] - slantZ * y);
            data.put(1.0f).put(1.0f).put(1.0f).put(alpha);
            data.put(c[2] + u0).put(c[3]);
            data.put(x).put(0.0f).put(z);
        }
    }

    public static void draw() {
        if (!ready) {
            return;
        }
        GL30.glBindVertexArray(vao);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        if (rainCount > 0) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, rainTexture);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, rainCount);
        }
        if (snowCount > 0) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, snowTexture);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, rainCount, snowCount);
        }
        GL30.glBindVertexArray(0);
    }

    public static void release() {
        if (!ready) {
            return;
        }
        ready = false;
        GL30.glDeleteVertexArrays(vao);
        GL15.glDeleteBuffers(vbo);
        GL11.glDeleteTextures(rainTexture);
        GL11.glDeleteTextures(snowTexture);
    }

    private static boolean init() {
        try {
            vao = GL30.glGenVertexArrays();
            vbo = GL15.glGenBuffers();
            GL30.glBindVertexArray(vao);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (long) data.capacity() * 4L, GL15.GL_DYNAMIC_DRAW);
            int stride = FLOATS * 4;
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, stride, 0L);
            GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, stride, 12L);
            GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, stride, 28L);
            GL20.glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, stride, 36L);
            for (int i = 0; i < 4; ++i) {
                GL20.glEnableVertexAttribArray(i);
            }
            GL30.glBindVertexArray(0);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL13.glActiveTexture(GL13.GL_TEXTURE31);
            rainTexture = texture(rainStreaks());
            snowTexture = texture(snowFlakes());
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            ready = true;
            return true;
        } catch (RuntimeException | LinkageError e) {
            broken = true;
            System.out.println("[ArmakuViewpointFixes] rain under shader packs off: " + e);
            return false;
        }
    }

    // Bright head at the bottom, larger v.
    private static float[] rainStreaks() {
        float[] alpha = new float[TEX_W * TEX_H];
        Random random = new Random(7L);
        for (int n = 0; n < 150; ++n) {
            int x = random.nextInt(TEX_W);
            int y = random.nextInt(TEX_H);
            int length = 28 + random.nextInt(48);
            float strength = 0.55f + 0.45f * random.nextFloat();
            boolean wide = random.nextFloat() < 0.3f;
            for (int k = 0; k < length; ++k) {
                float t = (float) k / (length - 1);
                float a = strength * t * t * (3.0f - 2.0f * t);
                int row = (y + k) % TEX_H;
                add(alpha, x, row, a);
                add(alpha, (x + 1) % TEX_W, row, a * (wide ? 0.8f : 0.25f));
                add(alpha, (x + TEX_W - 1) % TEX_W, row, a * 0.15f);
            }
        }
        return alpha;
    }

    private static float[] snowFlakes() {
        float[] alpha = new float[TEX_W * TEX_H];
        Random random = new Random(11L);
        for (int n = 0; n < 260; ++n) {
            int cx = random.nextInt(TEX_W);
            int cy = random.nextInt(TEX_H);
            float r = 1.2f + 2.0f * random.nextFloat();
            float strength = 0.6f + 0.4f * random.nextFloat();
            int reach = (int) Math.ceil(r) + 1;
            for (int dy = -reach; dy <= reach; ++dy) {
                for (int dx = -reach; dx <= reach; ++dx) {
                    float dist = (float) Math.sqrt(dx * dx + dy * dy) / r;
                    if (dist < 1.0f) {
                        float a = strength * (1.0f - dist * dist);
                        add(alpha, (cx + dx + TEX_W) % TEX_W, (cy + dy + TEX_H) % TEX_H, a);
                    }
                }
            }
        }
        return alpha;
    }

    private static void add(float[] alpha, int x, int y, float a) {
        int i = y * TEX_W + x;
        alpha[i] = Math.min(1.0f, alpha[i] + a);
    }

    private static int texture(float[] alpha) {
        ByteBuffer pixels = BufferUtils.createByteBuffer(TEX_W * TEX_H * 4);
        for (float a : alpha) {
            pixels.put((byte) -1).put((byte) -1).put((byte) -1).put((byte) Math.round(a * 255.0f));
        }
        pixels.flip();
        int id = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, TEX_W, TEX_H, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        return id;
    }

    private static float hash(long cell, int salt) {
        long l = cell * 418711L + salt * 0x9E3779B97F4A7C15L;
        l ^= l >>> 29;
        l *= 0xBF58476D1CE4E5B9L;
        l ^= l >>> 32;
        return (float) (l & 0xFFFFFFL) / 16777216.0f;
    }

    private static float clamp(float v, float lo, float hi) {
        return v < lo ? lo : Math.min(v, hi);
    }
}
