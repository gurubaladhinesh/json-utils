package com.techguru.json;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techguru.json.diff.JsonDiff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class JsonMain {

    private static final Logger logger = LoggerFactory.getLogger(JsonMain.class);

    private static final String LOG_KV_PAIR = "{}: {}";

    public static void main(String[] args) throws Exception {
        if (args.length != 2 || args[0].isBlank() || args[1].isBlank()) {
            logger.error("Incorrect usage. args[0]: file path of the left json, args[1]: file path of the right json");
            return;
        }
        try {
            String leftJsonFilePath = args[0];
            String rightJsonFilePath = args[1];
            String leftJson = "";
            String rightJson = "";
            leftJson = new String(Files.readAllBytes(Paths.get(leftJsonFilePath)));
            rightJson = new String(Files.readAllBytes(Paths.get(rightJsonFilePath)));
            if (leftJson.isBlank() || rightJson.isBlank()) {
                logger.error("one of the input json file is empty");
                throw new Exception("one of the input json file is empty");
            }
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> leftJsonMap = gson.fromJson(leftJson, type);
            Map<String, Object> rightJsonMap = gson.fromJson(rightJson, type);

            Map<String, Object> leftJsonFlatMap = JsonDiff.flatten(leftJsonMap);
            Map<String, Object> rightJsonFlatMap = JsonDiff.flatten(rightJsonMap);

            MapDifference<String, Object> difference = Maps.difference(leftJsonFlatMap, rightJsonFlatMap);

            logger.info("Entries only on left\n--------------------------");
            difference.entriesOnlyOnLeft().forEach((key, value) -> logger.info(LOG_KV_PAIR, key, value));

            logger.info("\n\nEntries only on right\n--------------------------");
            difference.entriesOnlyOnRight().forEach((key, value) -> logger.info(LOG_KV_PAIR, key, value));

            logger.info("\n\nEntries differing\n--------------------------");
            difference.entriesDiffering().forEach((key, value) -> logger.info(LOG_KV_PAIR, key, value));

            logger.info("\n\nEntries in common\n--------------------------");
            difference.entriesInCommon().forEach((key, value) -> logger.info(LOG_KV_PAIR, key, value));


        } catch (IOException e) {
            String message = "exception while reading input json file";
            logger.error(message, e);
            throw new Exception(message, e);
        }
    }
}
