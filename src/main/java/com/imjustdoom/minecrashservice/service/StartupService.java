package com.imjustdoom.minecrashservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.imjustdoom.minecrashservice.model.SolutionModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class StartupService implements CommandLineRunner {

    private static final Gson GSON = new GsonBuilder().create();
    private static final String USER_AGENT = "MineCrashService 1.0";

    private final SettingsService settingsService;
    private final SolutionService solutionService;

    public StartupService(SettingsService settingsService, SolutionService solutionService) {
        this.settingsService = settingsService;
        this.solutionService = solutionService;
    }

    @Override
    public void run(String... args) {

        String zipString = "https://api.github.com/repos/Eimer-Archive/MineCrashSolutions/zipball";
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-zip") && i != args.length - 1) {
                zipString = args[i + 1];
            }
        }

        try {
            byte[] zipBytes = downloadZipAsByteArray(zipString);

            String newHash = calculateZipFileHash(zipBytes);
            String lastHash = this.settingsService.getZipHash();

            System.out.println(newHash + " - " + lastHash);

            if (newHash.equals(lastHash)) {
                return;
            }

            this.settingsService.updateZipHash(newHash);

            // Just clear them all. Maybe only update changes at some point?
            this.solutionService.clearAll();
            processZipFromByteArray(zipBytes);
        } catch (IOException exception) {
            System.err.println("Unable to fetch updated solutions");
            exception.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] downloadZipAsByteArray(String fileURL) throws IOException {
        URL url = URI.create(fileURL).toURL();
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream inputStream = httpConn.getInputStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                return outputStream.toByteArray();
            }
        } else {
            throw new IOException("Failed to fetch the ZIP file. Server replied HTTP code: " + httpConn.getResponseCode());
        }
    }

    private void processZipFromByteArray(byte[] zipBytes) throws IOException {
        try (ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry = zipIn.getNextEntry();

            // Iterate through each entry in the ZIP file
            while (entry != null) {
                String name = entry.getName();
                System.out.println("Entry: " + name);

                if (name.contains("solutions/") && !entry.isDirectory()) {
                    readFileFromZip(zipIn);
                }

                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    private void readFileFromZip(ZipInputStream zipIn) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(zipIn));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        this.solutionService.addNewSolution(GSON.fromJson(builder.toString(), SolutionModel.class));
    }

    private String calculateZipFileHash(byte[] zipBytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(zipBytes);
        return bytesToHex(digest.digest());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private JsonElement sendGet(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) URI.create(url).toURL().openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            JsonElement object = GSON.fromJson(reader, JsonElement.class);
            reader.close();
            return object;
        } else {
            throw new IOException("Failed to connect to the endpoint - error " + responseCode);
        }
    }
}
