package com.techguru.json;

import com.google.common.collect.MapDifference;
import com.techguru.json.diff.JsonDiff;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class JsonMain {

    private static final Logger logger = LoggerFactory.getLogger(JsonMain.class);

    private static final String LOG_KV_PAIR = "{}: {}";

    public static void main(String[] args) throws Exception {

        MapDifference<String, Object> diff = JsonDiff.compare(IOUtils.toString(JsonMain.class.getClassLoader().getResourceAsStream("left.json"), StandardCharsets.UTF_8), IOUtils.toString(JsonMain.class.getClassLoader().getResourceAsStream("right.json"), StandardCharsets.UTF_8));

        logger.info("Entries only on left\n--------------------------");
        diff.entriesOnlyOnLeft().forEach((key, value) -> logger.info(LOG_KV_PAIR, key, value));

        logger.info("\n\nEntries only on right\n--------------------------");
        diff.entriesOnlyOnRight().forEach((key, value) -> logger.info(LOG_KV_PAIR, key, value));

        logger.info("\n\nEntries differing\n--------------------------");
        diff.entriesDiffering().forEach((key, value) -> logger.info(LOG_KV_PAIR, key, value));

        logger.info("\n\nEntries in common\n--------------------------");
        diff.entriesInCommon().forEach((key, value) -> logger.info(LOG_KV_PAIR, key, value));

    }
}
