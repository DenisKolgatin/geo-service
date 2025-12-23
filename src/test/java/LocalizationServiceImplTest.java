import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

class LocalizationServiceImplTest {

    private final LocalizationService localizationService = new LocalizationServiceImpl();

    @Test
    void shouldReturnRussianTextForRussia() {
        String message = localizationService.locale(Country.RUSSIA);

        assertEquals("Добро пожаловать", message);
    }

    @Test
    void shouldReturnEnglishTextForUsa() {
        String message = localizationService.locale(Country.USA);

        assertEquals("Welcome", message);
    }

    @Test
    void shouldReturnEnglishTextForOtherCountries() {
        String message = localizationService.locale(Country.GERMANY);

        assertEquals("Welcome", message);
    }
}
