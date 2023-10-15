package com.qsr.customspd.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.qsr.customspd.assets.tiles.TileAsset;
import com.qsr.customspd.modding.ModManager;
import com.qsr.customspd.modding.TileMapCompiler;
import com.watabou.utils.FileUtils;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DesktopTileMapCompiler implements TileMapCompiler {
    @Override
    public void compileTileMap(String name, TileAsset[] tiles, int width, int height, int tileWidth, int tileHeight) {
        BufferedImage result = new BufferedImage(
            width,
            height,
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics tileMap = result.getGraphics();
        for (TileAsset tileAsset : tiles) {
            FileHandle handle = FileUtils.getFileHandle(
                Files.FileType.External, FileUtils.defaultPath,
                ModManager.INSTANCE.getModdedAssetFilePath(tileAsset.getPath())
            );
            if (!handle.exists()) handle = Gdx.files.internal(ModManager.INSTANCE.getModdedAssetFilePath(tileAsset.getPath()));
            int pos = tileAsset.getPos();
            try {
                tileMap.drawImage(
                    ImageIO.read(handle.read()),
                    pos % (width / tileWidth) * tileWidth,
                    pos / (width / tileWidth) * tileHeight,
                    null
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            ImageIO.write(result, "png", FileUtils.getFileHandle("temp_" + name).file());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
