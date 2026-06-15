package com.example.modid;

import com.example.modid.proxy.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ExampleMod.MOD_ID, name = ExampleMod.MOD_NAME, version = ExampleMod.VERSION)
public class ExampleMod {

    public static final String MOD_ID = "modid";
    public static final String MOD_NAME = "Mod Name";
    public static final String VERSION = "1.0.0";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @SidedProxy(modId = MOD_ID, clientSide = "com.example.modid.proxy.ClientProxy", serverSide = "com.example.modid.proxy.CommonProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("========================================");
        LOGGER.info("Hello From {}! (Version {})", MOD_NAME, VERSION);
        LOGGER.info("Proxy is {}", proxy);
        LOGGER.info("========================================");

        // ========== 测试代码：验证重映射是否正常工作 ==========
        runRemappingTest();

        LOGGER.info("Language: {}", Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage());
    }

    /**
     * 测试重映射是否成功
     * 如果这段代码能正常编译、打包并在游戏中运行，说明构建流程完全正确
     */
    private void runRemappingTest() {
        LOGER.info("---------- Remapping Test ----------");

        try {
            // 测试1：尝试访问 Minecraft 客户端的当前世界时间
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.world != null) {
                long worldTime = mc.world.getWorldTime();
                LOGGER.info("[PASS] Test 1: Successfully accessed world time via MCP mappings: {}", worldTime);
            } else {
                LOGGER.info("[SKIP] Test 1: No world loaded yet (this is normal during preInit)");
            }

            // 测试2：尝试获取客户端玩家对象并访问其属性（仅在客户端有效）
            EntityPlayerSP player = mc.player;
            if (player != null) {
                // 这是一个使用了 MCP 映射的字段，编译后会被重映射为混淆名
                int swimTimer = player.swimTimer;
                float fallDistance = player.fallDistance;
                boolean isSneaking = player.isSneaking();

                LOGGER.info("[PASS] Test 2: Successfully accessed player data via MCP mappings:");
                LOGGER.info("       - swimTimer = {}", swimTimer);
                LOGGER.info("       - fallDistance = {}", fallDistance);
                LOGGER.info("       - isSneaking = {}", isSneaking);
            } else {
                LOGGER.info("[SKIP] Test 2: No player available (likely on server side or world not loaded)");
            }

            // 测试3：测试一个简单的方法调用重映射
            String serverBrand = mc.getSession().getUsername();
            LOGGER.info("[PASS] Test 3: Successfully called method via MCP mapping: getUsername() = {}", serverBrand);

            LOGGER.info("---------- All tests completed! ----------");
            LOGGER.info("If you see [PASS] messages above, remapping is WORKING CORRECTLY!");
            LOGGER.info("If you see errors or crashes, please check your build configuration.");

        } catch (Exception e) {
            LOGGER.error("========================================");
            LOGGER.error("TEST FAILED: An exception occurred during remapping test!");
            LOGGER.error("This may indicate a problem with your build/remapping setup.");
            LOGGER.error("Exception details:", e);
            LOGGER.error("========================================");
        }
    }
}
