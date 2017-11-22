package com.lottery.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.lottery.model.RawWeeklyDraw;
import com.lottery.model.WeeklyDraw;
import com.lottery.repository.WeeklyDrawJPARepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersistFromCsv {

    protected static final Logger LOGGER = LoggerFactory.getLogger(PersistFromCsv.class);
    private WeeklyDrawJPARepository wrepo;

    @Autowired
    public PersistFromCsv(WeeklyDrawJPARepository weeklyDrawJPARepository) {
        this.wrepo = weeklyDrawJPARepository;
    }

    public List populateRawWeeklyDraws(String filename) {

        List<RawWeeklyDraw> objectList = new ArrayList<>();
        try {
            objectList = loadObjectList(RawWeeklyDraw.class, filename);

        } catch (FileNotFoundException e) {
            PersistFromCsv.LOGGER.debug("nem beolvasható a fájl " + e);
        } catch (IOException e) {
            PersistFromCsv.LOGGER.debug("probléma beolvasáskor vagy nem létezik a fájl" + e);
        }

        return objectList;
    }

    public void persistAllToDB(List<RawWeeklyDraw> rawWeeklyDraws) {

        WeeklyDrawConverter converter = new WeeklyDrawConverterImpl();
        try {
            List<WeeklyDraw> weeklyDraws = converter.convertRawsToWeeklyDraws(rawWeeklyDraws);
            this.wrepo.save(weeklyDraws);
        } catch (FileNotFoundException e) {
            PersistFromCsv.LOGGER.debug("nem beolvasható a fájl" + e);
        }
    }

    public String readAndPersist() {
        persistAllToDB(this.populateRawWeeklyDraws("otoswithheader.csv"));
        return "OK";
    }

    private <T> List<T> loadObjectList(Class<T> type, String fileName) throws IOException {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withColumnSeparator(';').withHeader();
        CsvMapper mapper = new CsvMapper();
        File file = new ClassPathResource(fileName).getFile();
        try (MappingIterator<T> readValues = mapper.readerFor(type).with(bootstrapSchema).readValues(file)) {
            return readValues.readAll();
        } catch (Exception e) {
            PersistFromCsv.LOGGER.debug("Error occurred while loading object list from file " + fileName + ": " + e);
            throw new Error("");
        }
    }

}
