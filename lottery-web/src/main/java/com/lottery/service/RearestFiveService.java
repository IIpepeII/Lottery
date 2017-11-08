package com.lottery.service;

import com.lottery.model.RearestFiveResult;

import java.text.ParseException;

/**
 * interfész a legritkábban előforduló számok kiszámítására írt service-hez
 */
public interface RearestFiveService {

    /**
     * Legritkábban előforduló számok kiszámításához írt rule futtatása
     *
     * @return result objektum
     */

    RearestFiveResult executeRule();

    /**
     * Dátum alapján filterezett legritkábban kihúzott számok kiszámításához írt rule futtatása
     *
     * @return result objektum
     */
    RearestFiveResult executeRuleFilterByDate(String from, String to) throws ParseException;

}
