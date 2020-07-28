package com.techguru.json.validate;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.techguru.json.exception.JsonException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public interface JsonValidate {

    String JSON_EXTENSION = ".json";

    static void validate(File file) throws JsonException {
        if (file == null) {
            throw new JsonException("file is null");
        } else if (!file.exists()) {
            throw new JsonException("file does not exist");
        } else if (!file.isFile()) {
            throw new JsonException("input is not a file");
        } else if (!JSON_EXTENSION.equals(FilenameUtils.getExtension(file.getAbsolutePath()))) {
            throw new JsonException("file does not have json extension");
        } else if (file.length() <= 0) {
            throw new JsonException("file is empty");
        } else {
            try {
                validate(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new JsonException("exception while reading json file");
            }
        }
    }

    static void validate(String fileContent) throws JsonException {
        try {
            JsonParser.parseString(fileContent);
        } catch (JsonSyntaxException e) {
            throw new JsonException("not a valid json input");
        }
    }
}
