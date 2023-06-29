package com.sts15.mineprocess.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sts15.mineprocess.MineProcess;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ArcFurnaceScreen extends AbstractContainerScreen<ArcFurnaceMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(MineProcess.MOD_ID,"textures/gui/arc_furnace_gui.png");

    public ArcFurnaceScreen(ArcFurnaceMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressBar(pPoseStack, x, y);
    }

    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        if(menu.isCrafting()) {
            blit(pPoseStack, x + 130, y + 33, 176, 0, 8, menu.getScaledProgress());
        }
    }
    private void renderProgressBar(PoseStack pPoseStack, int x, int y) {
        int progressBarWidth = 14; // adjust this as needed
        int progressBarHeight = 3;  // adjust this as needed

        if(menu.isCrafting()) {
            int progress = menu.getScaledProgress() * progressBarWidth / 25;
            blit(pPoseStack, x + 96, y + 38, 176, 0, progress, progressBarHeight);
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }
}