package com.navercorp.pinpoint.banner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Supplier;

public class BannerSupplier implements Supplier<String> {
    private final String file;

    public BannerSupplier() {
        this("/pinpoint-banner/banner.txt");
    }

    public BannerSupplier(String file) {
        this.file = Objects.requireNonNull(file, "file");
    }

    static String readAllString(InputStream inputStream, Charset charset) throws IOException {
        try (Reader inputStreamReader = new InputStreamReader(inputStream, charset);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            StringBuilder buffer = new StringBuilder(64);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
                buffer.append(System.lineSeparator());
            }
            return buffer.toString();
        }
    }

    public String get() {
        return banner(file);
    }

    private String banner(String bannerFile) {
        try (InputStream inputStream = getClass().getResourceAsStream(bannerFile)) {
            return BannerSupplier.readAllString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("banner IO failed", e);
        }
    }
}
