package com.example.flightapi.airport.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.example.flightapi.airport.entity.Airport;
import com.example.flightapi.airport.repository.AirportRepository;
import com.example.flightapi.airport.service.AirportService;
import com.example.flightapi.common.utils.CacheKey;
import com.example.flightapi.common.utils.RedisUtils;
import com.example.flightapi.security.config.DictionaryProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;
    private final RedisUtils redisUtils;

    public AirportServiceImpl(AirportRepository airportRepository, RedisUtils redisUtils) {
        this.airportRepository = airportRepository;
        this.redisUtils = redisUtils;
    }

    @Override
    public Airport saveAirport(Airport airport) {
        Airport savedAirport = airportRepository.save(airport);
        clearAirportCache();
        return savedAirport;
    }

    @Override
    public Airport getAirportById(String id) {
        return airportRepository.findById(id).orElse(null);
    }

    @Override
    public Airport getAirportByCode(String airportCode) {
        return airportRepository.findByAirportCode(airportCode);
    }

    @Override
    public List<Airport> getAllAirports() {
        String key = CacheKey.DICT_NAME + DictionaryProperties.airportCityList;
        // 先从Redis缓存中获取
        List<Airport> airports = redisUtils.getList(key, Airport.class);
        if (CollUtil.isEmpty(airports)) {
            // 缓存中没有，从数据库获取
            airports = airportRepository.findAll();

            // 将结果存入Redis，设置24小时过期时间
            redisUtils.set(key, airports, 1, TimeUnit.DAYS);
        }

        return airports;
    }

    @Override
    public void deleteAirport(String id) {
        airportRepository.deleteById(id);
        clearAirportCache();
    }

    /**
     * 清除机场相关的Redis缓存
     */
    private void clearAirportCache() {
        String key = CacheKey.DICT_NAME + DictionaryProperties.airportCityList;
        redisUtils.del(key);
    }

    @Override
    public Airport updateAirport(Airport airport) {
        Airport updatedAirport = airportRepository.save(airport);
        clearAirportCache();
        return updatedAirport;
    }
}
