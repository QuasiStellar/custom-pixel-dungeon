package com.qsr.customspd.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.qsr.customspd.assets.tiles.TileAsset;
import com.qsr.customspd.modding.ModManager;
import com.qsr.customspd.modding.TileMapCompiler;
import com.watabou.utils.FileUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AndroidTileMapCompiler implements TileMapCompiler {
    @Override
    public void compileTileMap(String name, TileAsset[] tiles, int width, int height, int tileWidth, int tileHeight) {
        Bitmap tileMap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tileMap);
        for (TileAsset tileAsset : tiles) {
            FileHandle handle = FileUtils.getFileHandle(
                Files.FileType.External,
                FileUtils.defaultPath,
                ModManager.INSTANCE.getModdedAssetFilePath(tileAsset.getPath())
            );
            if (!handle.exists()) handle = Gdx.files.internal(ModManager.INSTANCE.getModdedAssetFilePath(tileAsset.getPath()));
            int pos = tileAsset.getPos();
            InputStream inputStream = handle.read();
            canvas.drawBitmap(
                BitmapFactory.decodeStream(inputStream),
                pos % (width / tileWidth) * tileWidth,
                pos / (width / tileWidth) * tileHeight,
                null
            );
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File file = FileUtils.getFileHandle("temp_" + name).file();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        tileMap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byte[] byteArray = stream.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteArray);

            fos.close();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
