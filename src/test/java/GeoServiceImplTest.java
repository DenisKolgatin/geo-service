package ru.netology.geo;

import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import static org.junit.jupiter.api.Assertions.*;

class GeoServiceImplTest {

    private final GeoService geoService = new GeoServiceImpl();

    @Test
    void shouldReturnRussianLocationForRussianIp() {
        Location location = geoService.byIp("172.123.12.19");

        assertEquals(Country.RUSSIA, location.getCountry());
        assertEquals("Moscow", location.getCity());
    }

    @Test
    void shouldReturnUsaLocationForAmericanIp() {
        Location location = geoService.byIp("96.44.183.149");

        assertEquals(Country.USA, location.getCountry());
        assertEquals("New York", location.getCity());
    }

    @Test
    void shouldReturnNullForUnknownIp() {
        Location location = geoService.byIp("127.0.0.1");

        assertNull(location);
    }
}
