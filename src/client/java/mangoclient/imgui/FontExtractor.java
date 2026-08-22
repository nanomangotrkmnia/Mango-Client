package mangoclient.imgui;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FontExtractor {
	private static File fontDir;

	public static String getFontPath(String fontNameTtf) {
		if (fontDir == null) {
			fontDir = new File(System.getProperty("java.io.tmpdir"), "mangoclient_imguimc_fonts");
		}
		return new File(fontDir, fontNameTtf).getAbsolutePath();
	}

	public static void extractFont() throws Exception {
		if (fontDir == null) {
			fontDir = new File(System.getProperty("java.io.tmpdir"), "mangoclient_imguimc_fonts");
		}
		File fontFile = new File(fontDir, "arial.ttf");
		if (fontFile.exists() && fontFile.length() > 0) {
			return;
		}

		InputStream in = FontExtractor.class.getClassLoader().getResourceAsStream("assets/mangoclient/arial.ttf");
		if (in == null) {
			in = FontExtractor.class.getResourceAsStream("/assets/mangoclient/arial.ttf");
		}

		Files.createDirectories(fontDir.toPath());
		if (in == null) {
			return;
		}
		Files.copy(in, fontFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		in.close();
	}
}
