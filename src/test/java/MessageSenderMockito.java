import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MessageSenderMockito {
    void shouldSendRussianMessageForRussianIp() {
        // Arrange
        GeoService geoService = mock(GeoService.class);
        LocalizationService localizationService = mock(LocalizationService.class);

        when(geoService.byIp("172.123.45.67"))
                .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));

        when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.45.67");

        // Act
        String result = messageSender.send(headers);

        // Assert
        assertEquals("Добро пожаловать", result);

        // Учитываем, что locale() вызывается ДВАЖДЫ: для печати и для возврата
        verify(localizationService, times(2)).locale(Country.RUSSIA);
        verify(geoService).byIp("172.123.45.67");
    }

    @Test
    @DisplayName("Должен отправлять английский текст для американского IP")
    void shouldSendEnglishMessageForAmericanIp() {
        GeoService geoService = mock(GeoService.class);
        LocalizationService localizationService = mock(LocalizationService.class);

        when(geoService.byIp("96.123.45.67"))
                .thenReturn(new Location("New York", Country.USA, null, 0));

        when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.123.45.67");

        String result = messageSender.send(headers);

        assertEquals("Welcome", result);

        verify(localizationService, times(2)).locale(Country.USA);
        verify(geoService).byIp("96.123.45.67");
    }

    @Test
    @DisplayName("Должен отправлять английский текст по умолчанию, если IP не указан")
    void shouldSendDefaultEnglishMessageWhenNoIp() {
        GeoService geoService = mock(GeoService.class);
        LocalizationService localizationService = mock(LocalizationService.class);

        when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<>();
        // IP не добавляем намеренно

        String result = messageSender.send(headers);

        assertEquals("Welcome", result);

        // Здесь locale() вызывается только ОДИН раз (только в return)
        verify(localizationService, times(1)).locale(Country.USA);
        verify(geoService, never()).byIp(anyString());
    }
}
