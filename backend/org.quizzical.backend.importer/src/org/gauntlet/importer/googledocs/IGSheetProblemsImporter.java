package org.gauntlet.importer.googledocs;

import java.io.IOException;

import com.google.api.services.drive.Drive;
import com.google.api.services.sheets.v4.Sheets;

public interface IGSheetProblemsImporter {

	public Sheets getSheetsService() throws IOException;

	public Drive getDriveService() throws IOException;
}
