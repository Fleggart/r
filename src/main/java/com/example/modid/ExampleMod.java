package com.example.modid;

import com.example.modid.proxy.IProxy;
import net.minecraft.client.Minecraft;
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
        LOGGER.info("---------- Remapping Test ----------");

        try {
            Minecraft mc = Minecraft.getMinecraft();
            
            // 测试1：访问 Minecraft 实例，验证核心映射
            if (mc != null) {
                LOGGER.info("[PASS] Test 1: Successfully accessed Minecraft.getMinecraft()");
            } else {
                LOGGER.error("[FAIL] Test 1: Minecraft.getMinecraft() returned null");
                return;
            }

            // 测试2：访问世界时间，验证世界对象映射
            if (mc.world != null) {
                long worldTime = mc.world.getWorldTime();
                LOGGER.info("[PASS] Test 2: Successfully accessed world time via MCP mappings: {}", worldTime);
            } else {
                LOGGER.info("[SKIP] Test 2: No world loaded yet (this is normal during preInit)");
            }

            // 测试3：测试 Session 对象的方法调用重映射（只使用确实存在的方法）
            String username = mc.getSession().getUsername();
            LOGGER.info("[PASS] Test 3: Successfully called method via MCP mapping: getUsername() = {}", username);

            // 测试4：测试玩家对象（如果可用）
            if (mc.player != null) {
                String playerName = mc.player.getName();
                LOGGER.info("[PASS] Test 4: Successfully accessed player: {}", playerName);
            } else {
                LOGGER.info("[SKIP] Test 4: No player available yet (this is normal during preInit)");
            }

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
