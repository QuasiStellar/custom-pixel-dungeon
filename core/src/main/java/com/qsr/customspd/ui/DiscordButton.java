package com.qsr.customspd.ui;

import com.qsr.customspd.Assets;
import com.qsr.customspd.ShatteredPixelDungeon;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.scenes.PixelScene;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;

public class DiscordButton extends Button {

    private Image image;
    private BitmapText text;

    public DiscordButton() {
        super();
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        image = new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DISCORD));
        add( image );

        text = new BitmapText(Messages.get(this, "name"), PixelScene.pixelFont);
        add(text);
    }

    @Override
    protected void layout() {
        super.layout();

        image.x = x;
        image.y = y;

        text.x = image.x + image.width + 2;
        text.y = image.y + 2;

        text.measure();
    }

    @Override
    protected void onPointerDown() {
        image.brightness( 1.2f );
        text.brightness(1.2f);
        Sample.INSTANCE.play( Assets.Sounds.CLICK );
    }

    @Override
    protected void onPointerUp() {
        image.resetColor();
        text.resetColor();
    }

    @Override
    protected void onClick() {
        ShatteredPixelDungeon.platform.openURI("https://discord.gg/QTxcTMC4bU");
    }

    public void updateSize() {
        setSize(image.width + 2 + text.width(), image.height);
    }
}
