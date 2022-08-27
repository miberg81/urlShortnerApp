package com.example.urlshortner.service;

import com.aerospike.client.Record;
import com.aerospike.client.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AerospikeService {
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 3000;

    public static final String NAMESPACE = "test";
    public static final String SET = "urlsMap";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * @param recordKeyString key of the record in db
     * @param binKey          key of the first bin in db
     * @param binValue        value of the forst bin in db
     * @return true if saved successfully
     */
    public boolean saveSingleBin(String recordKeyString, String binKey, String binValue) {
        try (AerospikeClient client = new AerospikeClient(HOSTNAME, PORT)) {
            Key recordKey = new Key(NAMESPACE, SET, recordKeyString);
            Bin bin = new Bin(binKey, binValue);
            client.put(null, recordKey, bin);
            Record record = client.get(null, recordKey);
            log.info("Create succeeded\nKey: {}\nRecord: {}\n", recordKey.userKey, gson.toJson(record.bins));
        } catch (AerospikeException ae) {
            log.error("Create Failed\nError: {}", ae.getMessage());
        }
        return true;
    }

    /**
     * Will retrieve from the db the original long url by the short url
     *
     * @param recordKeyString key for the record in db
     * @return a record in Aerospike db
     */
    public Record getRecordByKey(String recordKeyString) {
        try (AerospikeClient client = new AerospikeClient(HOSTNAME, PORT)) {
            Key key = new Key(NAMESPACE, SET, recordKeyString);
            Record record = client.get(null, key);
            if (record != null) {
                log.error("Key: {}\nRecord: {}", key.userKey, gson.toJson(record.bins));
                return record;
            } else {
                log.error("Key: {}\nRecord not found", key.userKey);
            }
        } catch (AerospikeException ae) {
            log.error("Error: {}", ae.getMessage());
        }
        return null;
    }
}
