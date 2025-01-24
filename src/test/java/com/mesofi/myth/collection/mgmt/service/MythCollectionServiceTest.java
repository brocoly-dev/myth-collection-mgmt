package com.mesofi.myth.collection.mgmt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mesofi.myth.collection.mgmt.model.Anniversary;
import com.mesofi.myth.collection.mgmt.model.Category;
import com.mesofi.myth.collection.mgmt.model.Distribution;
import com.mesofi.myth.collection.mgmt.model.Figurine;
import com.mesofi.myth.collection.mgmt.model.LineUp;
import com.mesofi.myth.collection.mgmt.model.Series;
import com.mesofi.myth.collection.mgmt.repository.MythCollectionRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MythCollectionServiceTest {
  @Mock private MythCollectionRepository repository;

  @InjectMocks private MythCollectionService service;

  @Test
  void createFigurine_whenFigurinePopulated_thenCreateFigurineAndReturnSaved() {

    // Arrange
    Figurine figurineToSave =
        new Figurine(
            null,
            "Seiya",
            null,
            null,
            null,
            "https://tamashiiweb.com/item/000",
            null,
            null,
            null,
            null,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            null,
            null);
    Figurine savedFigurine =
        new Figurine(
            "1",
            "Seiya",
            null,
            null,
            null,
            "https://tamashiiweb.com/item/000",
            null,
            null,
            null,
            null,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            null,
            null);
    when(repository.save(figurineToSave)).thenReturn(savedFigurine);

    // Act
    Figurine result = service.createFigurine(figurineToSave);

    // Assert
    assertNotNull(result);
    assertEquals("1", result.getId());
    assertEquals("Seiya", result.getBaseName());
    assertEquals("https://tamashiiweb.com/item/000", result.getTamashiiUrl());
    verify(repository, times(1)).save(figurineToSave);
  }

  @Test
  void calculateDisplayableName_whenFigurinePopulated_thenCalculateDisplayableName() {
    Figurine theFigurine = new Figurine();
    theFigurine.setBaseName("Hades");
    theFigurine.setOce(true);
    assertEquals("Hades ~Original Color Edition~", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Phoenix Ikki");
    theFigurine.setCategory(Category.V2);
    theFigurine.setGolden(true);
    assertEquals(
        "Phoenix Ikki [New Bronze Cloth] ~Golden Limited Edition~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pisces Aphrodite");
    theFigurine.setRevival(true);
    assertEquals("Pisces Aphrodite <Revival Ver.>", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pegasus Seiya");
    theFigurine.setCategory(Category.V4);
    assertEquals("Pegasus Seiya [God Cloth]", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Sagittarius Seiya");
    theFigurine.setCategory(Category.INHERITOR);
    assertEquals(
        "Sagittarius Seiya ~Inheritor of the Gold Cloth~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Phoenix Ikki");
    theFigurine.setCategory(Category.V3);
    assertEquals(
        "Phoenix Ikki [Final Bronze Cloth]", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Aquarius Camus");
    theFigurine.setCategory(Category.SURPLICE);
    theFigurine.setRevival(true);
    theFigurine.setAnniversary(Anniversary.A_20);
    assertEquals(
        "Aquarius Camus (Surplice) <20th Revival Ver.>",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pegasus Seiya");
    theFigurine.setSeries(Series.SS_THE_BEGINNING);
    assertEquals(
        "Pegasus Seiya -Knights of the Zodiac-", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pegasus Seiya");
    theFigurine.setComic(true);
    assertEquals("Pegasus Seiya Comic Ver.", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Phoenix Ikki");
    theFigurine.setAnniversary(Anniversary.A_20);
    assertEquals(
        "Phoenix Ikki 20th Anniversary Ver.", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Goddess Athena & Saori Kido");
    theFigurine.setCategory(Category.GOD);
    theFigurine.setSet(true);
    assertEquals(
        "Goddess Athena & Saori Kido -Divine Saga Premium Set-",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Polaris Hilda");
    theFigurine.setCategory(Category.ROBE);
    theFigurine.setDistributionJPY(
        new Distribution(null, null, null, null, null, LocalDate.of(2023, 1, 1), null));
    assertEquals(
        "Polaris Hilda -The Earth Representative Of Odin-",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pegasus Seiya");
    theFigurine.setCategory(Category.V3);
    theFigurine.setGolden(true);
    assertEquals(
        "Pegasus Seiya [Final Bronze Cloth] ~Golden Limited Edition~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Gemini Saga");
    theFigurine.setGold(true);
    assertEquals("Gemini Saga Gold24", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Gemini Saga");
    theFigurine.setSeries(Series.SOG);
    assertEquals("Gemini Saga God Cloth", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Siren Sorrento");
    theFigurine.setCategory(Category.ROBE);
    assertEquals(
        "Siren Sorrento <Argard Final Battle Ver.>", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Aries Shion");
    theFigurine.setCategory(Category.SURPLICE);
    theFigurine.setSet(true);
    assertEquals(
        "Aries Shion (Surplice) & The Pope Set", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Scorpio Milo");
    theFigurine.setCategory(Category.GOLD);
    theFigurine.setSeries(Series.SAINTIA_SHO);
    assertEquals(
        "Scorpio Milo Saintia Sho Color Edition", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("The Three Mysterious Surplice");
    theFigurine.setCategory(Category.SURPLICE);
    theFigurine.setSet(true);
    assertEquals("The Three Mysterious Surplice", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Seiya");
    theFigurine.setCategory(Category.V5);
    assertEquals("Seiya Heaven Chapter", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Poseidon");
    theFigurine.setAnniversary(Anniversary.A_15);
    assertEquals("Poseidon 15th Anniversary Ver.", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Sea Emperor Poseidon");
    theFigurine.setCategory(Category.SCALE);
    theFigurine.setSet(true);
    assertEquals(
        "Sea Emperor Poseidon Imperial Throne Set", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("The Sun God Abel & Goddess");
    theFigurine.setCategory(Category.GOD);
    theFigurine.setSet(true);
    assertEquals(
        "The Sun God Abel & Goddess Memorial Set", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Dragon Shiryu");
    theFigurine.setCategory(Category.V2);
    theFigurine.setGolden(true);
    assertEquals(
        "Dragon Shiryu [New Bronze Cloth] ~Golden Limited Edition~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pegasus Cross Object Jump");
    theFigurine.setAnniversary(Anniversary.A_50);
    assertEquals(
        "Pegasus Cross Object Jump 50th Anniversary Edition",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pegasus Cross Object Jump");
    theFigurine.setAnniversary(Anniversary.A_50);
    theFigurine.setGolden(true);
    assertEquals(
        "Pegasus Cross Object Jump 50th Anniversary Edition (Gold Ver.)",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Libra Dohko");
    theFigurine.setLineUp(LineUp.DDP);
    assertEquals(
        "Guidance of the Palace of the Scales ~LIBRA DOHKO~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Broken Surplice Parts");
    theFigurine.setBroken(true);
    theFigurine.setCategory(Category.SURPLICE);
    theFigurine.setSet(true);
    assertEquals("Broken Surplice Parts Set", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pisces Aphrodite");
    theFigurine.setLineUp(LineUp.DDP);
    assertEquals(
        "Blooming Roses in the Palace of the Twin Fish ~PISCES APHRODITE~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Andromeda Shun");
    theFigurine.setLineUp(LineUp.DDP);
    assertEquals("Nebula Chain ~ANDROMEDA SHUN~", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Capricorn Shura");
    theFigurine.setLineUp(LineUp.DDP);
    assertEquals(
        "Glittering Excalibur in the Palace of the Rock Goat ~CAPRICORN SHURA~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Gemini Saga");
    theFigurine.setSet(true);
    theFigurine.setSeries(Series.SOG);
    assertEquals(
        "Gemini Saga God Cloth Saga Saga Premium Set",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Barlon Rene");
    theFigurine.setSet(true);
    theFigurine.setCategory(Category.SPECTER);
    assertEquals("Barlon Rene Complete Set", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Goddess Athena And Soldiers");
    theFigurine.setLineUp(LineUp.DDP);
    assertEquals(
        "Golden Zodiac extension set Fire clock of the Sanctuary ~GODDESS ATHENA AND SOLDIERS~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Sagittarius Aiolos");
    theFigurine.setLineUp(LineUp.DDP);
    assertEquals(
        "Commitment of Aiolos' Spirit in the Palace of the Centaur ~SAGITTARIUS AIOLOS~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Dragon Shiryu");
    theFigurine.setLineUp(LineUp.DDP);
    assertEquals(
        "Rozan Rising Dragon Blow ~DRAGON SHIRYU~", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Cancer Death Mask");
    theFigurine.setLineUp(LineUp.DDP);
    assertEquals(
        "Desperate Battle in the Palace of the Giant Crab ~CANCER DEATH MASK~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Leo Aiolia");
    theFigurine.setLineUp(LineUp.DDP);
    assertEquals(
        "Lightning in the Palace of the Lion ~LEO AIOLIA~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Phoenix Ikki");
    theFigurine.setLineUp(LineUp.DDP);
    assertEquals("PHOENIX IKKI ~Flying Phoenix~", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Virgo Shaka");
    theFigurine.setLineUp(LineUp.DDP);
    assertEquals(
        "VIRGO SHAKA ~The Temple of the Maiden~", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pegasus Seiya");
    theFigurine.setAnniversary(Anniversary.A_30);
    theFigurine.setOce(true);
    assertEquals(
        "Pegasus Seiya ~Original Color Edition~", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pegasus Seiya");
    theFigurine.setLineUp(LineUp.DDP);
    assertEquals(
        "PEGASUS SEIYA ~Pegasus Meteor Punches~", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Gemini Saga");
    theFigurine.setLineUp(LineUp.DDP);
    assertEquals("GEMINI SAGA ~the Pope's Chamber~", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Gemini Saga");
    theFigurine.setSeries(Series.SS_LEGEND_OF_SANCTUARY);
    assertEquals(
        "Gemini Saga Legend of Sanctuary Edition", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Sagittarius Aiolos");
    theFigurine.setSeries(Series.SS_LEGEND_OF_SANCTUARY);
    theFigurine.setLineUp(LineUp.SC_LEGEND);
    assertEquals("Sagittarius Aiolos", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pegasus Seiya");
    theFigurine.setCategory(Category.V2);
    theFigurine.setAnniversary(Anniversary.A_40);
    theFigurine.setOce(true);
    assertEquals(
        "Pegasus Seiya (New Bronze Cloth) ~40th Anniversary Edition~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Phoenix Ikki");
    theFigurine.setCategory(Category.V4);
    theFigurine.setAnniversary(Anniversary.A_10);
    assertEquals(
        "Phoenix Ikki God Cloth ~10th Anniversary Edition~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Phoenix Ikki");
    theFigurine.setCategory(Category.V1);
    theFigurine.setGolden(true);
    assertEquals(
        "Phoenix Ikki (Early Bronze Cloth) ~Limited Gold Phoenix~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Andromeda Shun");
    theFigurine.setCategory(Category.V1);
    theFigurine.setGolden(true);
    assertEquals(
        "Andromeda Shun (Early Bronze Cloth) ~Limited Gold Andromeda~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Steel Saint Land Cloth Daichi & Steel Saint Marin Cloth Ushio");
    theFigurine.setCategory(Category.STEEL);
    theFigurine.setSet(true);
    assertEquals(
        "Steel Saint Land Cloth Daichi & Steel Saint Marin Cloth Ushio Set",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Phoenix Ikki");
    theFigurine.setLineUp(LineUp.MYTH_CLOTH);
    theFigurine.setGolden(true);
    assertEquals("Phoenix Ikki ~Power of Gold~", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Dragon & Cygnus Black");
    theFigurine.setCategory(Category.BLACK);
    theFigurine.setSet(true);
    assertEquals("Dragon & Cygnus Black Set", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Phoenix Ikki");
    theFigurine.setCategory(Category.V1);
    assertEquals("Phoenix Ikki", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Andromeda Shun");
    theFigurine.setCategory(Category.V1);
    theFigurine.setOce(true);
    assertEquals(
        "Andromeda Shun ~Original Color Edition~", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pegasus Seiya & Goddess Athena");
    theFigurine.setOce(true);
    theFigurine.setBroken(true);
    assertEquals(
        "Pegasus Seiya & Goddess Athena ~Original Color Edition~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Dragon Shiryu");
    theFigurine.setCategory(Category.V4);
    theFigurine.setOce(true);
    assertEquals(
        "Dragon Shiryu (God Cloth) ~Original Color Edition~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Polaris Hilda");
    theFigurine.setCategory(Category.ROBE);
    theFigurine.setDistributionJPY(
        new Distribution(null, null, null, null, null, LocalDate.of(2010, 1, 1), null));
    assertEquals("Polaris Hilda", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pegasus Seiya");
    theFigurine.setCategory(Category.V2);
    theFigurine.setBroken(true);
    assertEquals(
        "Pegasus Seiya (New Bronze Cloth) ~Broken Version~",
        service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pegasus Seiya");
    theFigurine.setGold(true);
    theFigurine.setLineUp(LineUp.MYTH_CLOTH);
    assertEquals("Golden Genealogy Pegasus Seiya", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pegasus Seiya");
    theFigurine.setPlain(true);
    assertEquals("Pegasus Seiya (Plain Clothes)", service.calculateDisplayableName(theFigurine));
  }
}
