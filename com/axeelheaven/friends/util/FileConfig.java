package com.axeelheaven.friends.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.axeelheaven.friends.Main;
import com.google.common.io.ByteStreams;

public class FileConfig {
	
	private Main plugin;

	public FileConfig(final Main main) {
		this.plugin = main;
	}
	
	public File loadConfiguration(final String resource) {
        final File folder = this.plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = this.plugin.getResourceAsStream(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }
	
}
