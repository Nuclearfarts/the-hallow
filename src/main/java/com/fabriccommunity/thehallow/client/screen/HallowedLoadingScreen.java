package com.fabriccommunity.thehallow.client.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import com.mojang.blaze3d.platform.GlStateManager;

import com.fabriccommunity.thehallow.TheHallow;
import com.fabriccommunity.thehallow.registry.HallowedBlocks;

import java.util.concurrent.ThreadLocalRandom;

public class HallowedLoadingScreen extends Screen {
	private static final Identifier[] BACKGROUNDS = {
		TheHallow.id("textures/block/deceased_dirt.png"),
		TheHallow.id("textures/block/tainted_sand.png"),
		TheHallow.id("textures/block/tainted_sandstone_bottom.png"),
		TheHallow.id("textures/block/tainted_sandstone_top.png")
	};
	
	private static final String[] MESSAGES = {
		"text.thehallow.loading.spookiness",
		"text.thehallow.loading.pumpkins",
		"text.thehallow.loading.candies",
		"text.thehallow.loading.costumes",
		"text.thehallow.loading.witch_water",
		"text.thehallow.loading.pumpcowns",
	};
	
	private final Identifier backgroundTexture;
	private final String message;
	private final ItemStack pumpkinStack;
	
	private int floatingTick = 0;
	private float rotation = 0f;
	
	public HallowedLoadingScreen() {
		super(NarratorManager.EMPTY);
		this.backgroundTexture = BACKGROUNDS[ThreadLocalRandom.current().nextInt(BACKGROUNDS.length)];
		this.message = MESSAGES[ThreadLocalRandom.current().nextInt(MESSAGES.length)];
		this.pumpkinStack = new ItemStack(HallowedBlocks.TINY_PUMPKIN);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		floatingTick++;
		if (floatingTick == Integer.MAX_VALUE) {
			floatingTick = 0;
		}
		rotation += 4f;
		if (rotation >= 360f) {
			rotation = 0f;
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY, float delta) {
		renderDirtBackground(0);
		this.drawCenteredString(font, I18n.translate(message), width / 2, height / 2 - 50, 0xFFFFFF);
		
		float scale = 100f;
		GlStateManager.pushMatrix();
		GlStateManager.translatef(width / 2f, height / 2f + 65 + MathHelper.sin(floatingTick / 6.5f) * 25, 500f);
		GlStateManager.scalef(scale, scale, scale);
		GlStateManager.rotatef(180, 1, 0, 0);
		GlStateManager.rotatef(rotation, 0, 1, 0);
		
		minecraft.getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
		minecraft.getItemRenderer().renderGuiItem(pumpkinStack, 0, 0);
		
		GlStateManager.popMatrix();
	}
	
	@Override
	public void renderDirtBackground(int i) {
		// Copied mostly from renderDirtBackground()
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		minecraft.getTextureManager().bindTexture(backgroundTexture);
		int brightness = 130;
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		builder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
		builder.vertex(0, height, 0).texture(0, height / 32.0F + i).color(brightness, brightness, brightness, 255).next();
		builder.vertex(width, height, 0).texture(width / 32.0F, height / 32.0F + i).color(brightness, brightness, brightness, 255).next();
		builder.vertex(width, 0, 0).texture(width / 32.0F, i).color(brightness, brightness, brightness, 255).next();
		builder.vertex(0, 0, 0).texture(0, i).color(brightness, brightness, brightness, 255).next();
		tessellator.draw();
	}
}
