package com.mesofi.myth.collection.mgmt.mappers;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.mesofi.myth.collection.mgmt.model.Anniversary;
import com.mesofi.myth.collection.mgmt.model.Category;
import com.mesofi.myth.collection.mgmt.model.DistributionChannel;
import com.mesofi.myth.collection.mgmt.model.Distributor;
import com.mesofi.myth.collection.mgmt.model.Figurine;
import com.mesofi.myth.collection.mgmt.model.LineUp;
import com.mesofi.myth.collection.mgmt.model.Series;
import com.mesofi.myth.collection.mgmt.model.SourceFigurine;
import com.mesofi.myth.collection.mgmt.service.DistributionChannelService;
import com.mesofi.myth.collection.mgmt.service.DistributorService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FigurineMapperTest {
  private FigurineMapper mapper;

  @Mock private DistributionChannelService distributionChannelService;
  @Mock private DistributorService distributorService;

  @BeforeEach
  void before() {
    when(distributorService.getAllDistributors())
        .thenReturn(List.of(new Distributor("1", "DAM"), new Distributor("2", "DTM")));

    when(distributionChannelService.getAllDistributionChannels())
        .thenReturn(
            List.of(
                new DistributionChannel("1", "Stores"),
                new DistributionChannel("2", "Tamashii Web Shop"),
                new DistributionChannel("3", "Tamashii World Tour"),
                new DistributionChannel("4", "Tamashii Nations"),
                new DistributionChannel("5", "Tamashii Store"),
                new DistributionChannel("6", "Other Limited Edition")));

    mapper = new FigurineMapper(distributionChannelService, distributorService);
  }

  @Test
  void toFigure_whenSourceFigurineInvalidDate_thenThrowException() {
    // Arrange
    SourceFigurine sourceFigurine = new SourceFigurine();
    sourceFigurine.setAnnJPY("1/1");

    // Assert
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> mapper.toFigure(sourceFigurine))
        .withMessage("Invalid date format: 1/1");
  }

  @Test
  void toFigure_whenSourceFigurineBasic_thenMapBasicFigurine() {
    // Arrange
    SourceFigurine sourceFigurine = new SourceFigurine();

    // Act
    Figurine result = mapper.toFigure(sourceFigurine);

    // Assert
    assertNotNull(result);
    assertNull(result.getBaseName());
    assertNull(result.getDistributionJPY());
    assertNull(result.getDistributionMXN());
    assertNull(result.getTamashiiUrl());
    assertNull(result.getDistributionChannel());
    assertNull(result.getLineUp());
    assertNull(result.getSeries());
    assertNull(result.getCategory());
    assertFalse(result.isMetal());
    assertFalse(result.isOce());
    assertFalse(result.isRevival());
    assertFalse(result.isPlain());
    assertFalse(result.isBroken());
    assertFalse(result.isGolden());
    assertFalse(result.isGold());
    assertFalse(result.isHk());
    assertFalse(result.isComic());
    assertFalse(result.isSet());
    assertNull(result.getAnniversary());
    assertNull(result.getOfficialImages());
    assertNull(result.getOtherImages());
    assertNull(result.getRemarks());
  }

  @Test
  void toFigure_whenUnreleaseSourceFigurine_thenMapFigurine() {
    // Arrange
    SourceFigurine sourceFigurine = new SourceFigurine();
    sourceFigurine.setBaseName("Odin Seiya");
    sourceFigurine.setPriceJPY("¥0");
    sourceFigurine.setAnnJPY("");
    sourceFigurine.setPreorderJPY("");
    sourceFigurine.setReleaseJPY("");
    sourceFigurine.setDistributorMXN("");
    sourceFigurine.setPriceMXN("");
    sourceFigurine.setPreorderMXN("");
    sourceFigurine.setReleaseMXN("");
    sourceFigurine.setLink("");
    sourceFigurine.setDist("");
    sourceFigurine.setLineUp("Myth Cloth EX");
    sourceFigurine.setSeries("Saint Seiya");
    sourceFigurine.setGroup("God Robe");
    sourceFigurine.setMetal("FALSE");
    sourceFigurine.setOce("FALSE");
    sourceFigurine.setRevival("FALSE");
    sourceFigurine.setPlainCloth("FALSE");
    sourceFigurine.setBroken("FALSE");
    sourceFigurine.setGolden("FALSE");
    sourceFigurine.setGold("FALSE");
    sourceFigurine.setHk("FALSE");
    sourceFigurine.setManga("FALSE");
    sourceFigurine.setSet("FALSE");
    sourceFigurine.setAnniversary("");
    sourceFigurine.setRemarks("");
    sourceFigurine.setOfficialImages("");
    sourceFigurine.setOtherImages("");

    // Act
    Figurine result = mapper.toFigure(sourceFigurine);

    // Assert
    assertNotNull(result);
    assertEquals("Odin Seiya", result.getBaseName());
    assertNull(result.getDistributionJPY());
    assertNull(result.getDistributionMXN());
    assertNull(result.getTamashiiUrl());
    assertNull(result.getDistributionChannel());
    assertEquals(LineUp.MYTH_CLOTH_EX, result.getLineUp());
    assertEquals(Series.SAINT_SEIYA, result.getSeries());
    assertEquals(Category.ROBE, result.getCategory());
    assertFalse(result.isMetal());
    assertFalse(result.isOce());
    assertFalse(result.isRevival());
    assertFalse(result.isPlain());
    assertFalse(result.isBroken());
    assertFalse(result.isGolden());
    assertFalse(result.isGold());
    assertFalse(result.isHk());
    assertFalse(result.isComic());
    assertFalse(result.isSet());
    assertNull(result.getAnniversary());
    assertNull(result.getOfficialImages());
    assertNull(result.getOtherImages());
    assertNull(result.getRemarks());
  }

  @Test
  void toFigure_whenToBeReleasedSourceFigurine_thenMapFigurine() {
    // Arrange
    SourceFigurine sourceFigurine = new SourceFigurine();
    sourceFigurine.setBaseName("Ophiuchus Odysseus");
    sourceFigurine.setPriceJPY("¥30,000");
    sourceFigurine.setAnnJPY("11/16/2023");
    sourceFigurine.setPreorderJPY("11/15/2024");
    sourceFigurine.setReleaseJPY("6/2025");
    sourceFigurine.setDistributorMXN("");
    sourceFigurine.setPriceMXN("");
    sourceFigurine.setPreorderMXN("");
    sourceFigurine.setReleaseMXN("");
    sourceFigurine.setLink("https://tamashiiweb.com/item/15204");
    sourceFigurine.setDist("Tamashii Web Shop");
    sourceFigurine.setLineUp("Myth Cloth EX");
    sourceFigurine.setSeries("Saint Seiya");
    sourceFigurine.setGroup("Gold Saint");
    sourceFigurine.setMetal("TRUE");
    sourceFigurine.setOce("FALSE");
    sourceFigurine.setRevival("FALSE");
    sourceFigurine.setPlainCloth("FALSE");
    sourceFigurine.setBroken("FALSE");
    sourceFigurine.setGolden("FALSE");
    sourceFigurine.setGold("FALSE");
    sourceFigurine.setHk("FALSE");
    sourceFigurine.setManga("TRUE");
    sourceFigurine.setSet("FALSE");
    sourceFigurine.setAnniversary("");
    sourceFigurine.setRemarks("The hidden eight sense project");
    sourceFigurine.setOfficialImages("923/Qs7QRL");
    sourceFigurine.setOtherImages("");

    // Act
    Figurine result = mapper.toFigure(sourceFigurine);

    // Assert
    assertNotNull(result);
    assertEquals("Ophiuchus Odysseus", result.getBaseName());
    assertNotNull(result.getDistributionJPY());
    assertEquals(new BigDecimal("30000"), result.getDistributionJPY().getBasePrice());
    assertNull(result.getDistributionJPY().getDistributor());
    assertNull(result.getDistributionJPY().getFinalPrice());
    assertEquals(
        LocalDate.of(2023, 11, 16), result.getDistributionJPY().getFirstAnnouncementDate());
    assertEquals(LocalDate.of(2024, 11, 15), result.getDistributionJPY().getPreOrderDate());
    assertEquals(LocalDate.of(2025, 06, 01), result.getDistributionJPY().getReleaseDate());
    assertFalse(result.getDistributionJPY().getReleaseDateConfirmed());
    assertNull(result.getDistributionMXN());
    assertEquals("https://tamashiiweb.com/item/15204", result.getTamashiiUrl());
    assertNotNull(result.getDistributionChannel());
    assertEquals("2", result.getDistributionChannel().getId());
    assertEquals("Tamashii Web Shop", result.getDistributionChannel().getDistribution());
    assertEquals(LineUp.MYTH_CLOTH_EX, result.getLineUp());
    assertEquals(Series.SAINT_SEIYA, result.getSeries());
    assertEquals(Category.GOLD, result.getCategory());
    assertTrue(result.isMetal());
    assertFalse(result.isOce());
    assertFalse(result.isRevival());
    assertFalse(result.isPlain());
    assertFalse(result.isBroken());
    assertFalse(result.isGolden());
    assertFalse(result.isGold());
    assertFalse(result.isHk());
    assertTrue(result.isComic());
    assertFalse(result.isSet());
    assertNull(result.getAnniversary());
    assertEquals(List.of("923/Qs7QRL"), result.getOfficialImages());
    assertNull(result.getOtherImages());
    assertEquals("The hidden eight sense project", result.getRemarks());
  }

  @Test
  void toFigure_whenReleasedSourceFigurine_thenMapFigurine() {
    // Arrange
    SourceFigurine sourceFigurine = new SourceFigurine();
    sourceFigurine.setBaseName("Phoenix Ikki");
    sourceFigurine.setPriceJPY("¥15,400");
    sourceFigurine.setAnnJPY("4/21/2024");
    sourceFigurine.setPreorderJPY("4/26/2024");
    sourceFigurine.setReleaseJPY("10/26/2024");
    sourceFigurine.setDistributorMXN("DAM");
    sourceFigurine.setPriceMXN("$2,750");
    sourceFigurine.setPreorderMXN("10/29/2024");
    sourceFigurine.setReleaseMXN("11/25/2024");
    sourceFigurine.setLink("https://tamashiiweb.com/item/14874");
    sourceFigurine.setDist("Stores");
    sourceFigurine.setLineUp("Myth Cloth EX");
    sourceFigurine.setSeries("Saint Seiya");
    sourceFigurine.setGroup("Bronze Saint V3");
    sourceFigurine.setMetal("FALSE");
    sourceFigurine.setOce("FALSE");
    sourceFigurine.setRevival("FALSE");
    sourceFigurine.setPlainCloth("FALSE");
    sourceFigurine.setBroken("FALSE");
    sourceFigurine.setGolden("FALSE");
    sourceFigurine.setGold("FALSE");
    sourceFigurine.setHk("FALSE");
    sourceFigurine.setManga("FALSE");
    sourceFigurine.setSet("FALSE");
    sourceFigurine.setAnniversary("");
    sourceFigurine.setRemarks("");
    sourceFigurine.setOfficialImages("922/x738BH");
    sourceFigurine.setOtherImages("");

    // Act
    Figurine result = mapper.toFigure(sourceFigurine);

    // Assert
    assertNotNull(result);
    assertEquals("Phoenix Ikki", result.getBaseName());
    assertNotNull(result.getDistributionJPY());
    assertEquals(new BigDecimal("15400"), result.getDistributionJPY().getBasePrice());
    assertNull(result.getDistributionJPY().getDistributor());
    assertNull(result.getDistributionJPY().getFinalPrice());
    assertEquals(
        LocalDate.of(2024, 04, 21), result.getDistributionJPY().getFirstAnnouncementDate());
    assertEquals(LocalDate.of(2024, 04, 26), result.getDistributionJPY().getPreOrderDate());
    assertEquals(LocalDate.of(2024, 10, 26), result.getDistributionJPY().getReleaseDate());
    assertTrue(result.getDistributionJPY().getReleaseDateConfirmed());
    assertNotNull(result.getDistributionMXN());
    assertEquals(new BigDecimal("2750"), result.getDistributionMXN().getBasePrice());
    assertNotNull(result.getDistributionMXN().getDistributor());
    assertEquals("1", result.getDistributionMXN().getDistributor().getId());
    assertEquals("DAM", result.getDistributionMXN().getDistributor().getName());
    assertNull(result.getDistributionMXN().getFinalPrice());
    assertNull(result.getDistributionMXN().getFirstAnnouncementDate());
    assertEquals(LocalDate.of(2024, 10, 29), result.getDistributionMXN().getPreOrderDate());
    assertEquals(LocalDate.of(2024, 11, 25), result.getDistributionMXN().getReleaseDate());
    assertTrue(result.getDistributionMXN().getReleaseDateConfirmed());
    assertEquals("https://tamashiiweb.com/item/14874", result.getTamashiiUrl());
    assertNotNull(result.getDistributionChannel());
    assertEquals("1", result.getDistributionChannel().getId());
    assertEquals("Stores", result.getDistributionChannel().getDistribution());
    assertEquals(LineUp.MYTH_CLOTH_EX, result.getLineUp());
    assertEquals(Series.SAINT_SEIYA, result.getSeries());
    assertEquals(Category.V3, result.getCategory());
    assertFalse(result.isMetal());
    assertFalse(result.isOce());
    assertFalse(result.isRevival());
    assertFalse(result.isPlain());
    assertFalse(result.isBroken());
    assertFalse(result.isGolden());
    assertFalse(result.isGold());
    assertFalse(result.isHk());
    assertFalse(result.isComic());
    assertFalse(result.isSet());
    assertNull(result.getAnniversary());
    assertEquals(List.of("922/x738BH"), result.getOfficialImages());
    assertNull(result.getOtherImages());
    assertNull(result.getRemarks());
  }

  @Test
  void toFigure_whenCustomSourceFigurine_thenMapFigurine() {
    // Arrange
    SourceFigurine sourceFigurine = new SourceFigurine();
    sourceFigurine.setBaseName("Phoenix Ikki");
    sourceFigurine.setPriceJPY("¥15,400");
    sourceFigurine.setAnnJPY("4/21/2024");
    sourceFigurine.setPreorderJPY("4/26/2024");
    sourceFigurine.setReleaseJPY("10/26/2024");
    sourceFigurine.setDistributorMXN("DAM");
    sourceFigurine.setPriceMXN("$2,750");
    sourceFigurine.setPreorderMXN("10/29/2024");
    sourceFigurine.setReleaseMXN("11/25/2024");
    sourceFigurine.setLink("https://tamashiiweb.com/item/14874");
    sourceFigurine.setDist("Stores");
    sourceFigurine.setLineUp("Myth Cloth EX");
    sourceFigurine.setSeries("Saint Seiya");
    sourceFigurine.setGroup("Bronze Saint V3");
    sourceFigurine.setMetal("FALSE");
    sourceFigurine.setOce("TRUE");
    sourceFigurine.setRevival("TRUE");
    sourceFigurine.setPlainCloth("TRUE");
    sourceFigurine.setBroken("TRUE");
    sourceFigurine.setGolden("TRUE");
    sourceFigurine.setGold("TRUE");
    sourceFigurine.setHk("TRUE");
    sourceFigurine.setManga("FALSE");
    sourceFigurine.setSet("TRUE");
    sourceFigurine.setAnniversary("20");
    sourceFigurine.setRemarks("");
    sourceFigurine.setOfficialImages("922/x738BH");
    sourceFigurine.setOtherImages("922/x738BH");

    // Act
    Figurine result = mapper.toFigure(sourceFigurine);

    // Assert
    assertNotNull(result);
    assertEquals("Phoenix Ikki", result.getBaseName());
    assertNotNull(result.getDistributionJPY());
    assertEquals(new BigDecimal("15400"), result.getDistributionJPY().getBasePrice());
    assertNull(result.getDistributionJPY().getDistributor());
    assertNull(result.getDistributionJPY().getFinalPrice());
    assertEquals(
        LocalDate.of(2024, 04, 21), result.getDistributionJPY().getFirstAnnouncementDate());
    assertEquals(LocalDate.of(2024, 04, 26), result.getDistributionJPY().getPreOrderDate());
    assertEquals(LocalDate.of(2024, 10, 26), result.getDistributionJPY().getReleaseDate());
    assertTrue(result.getDistributionJPY().getReleaseDateConfirmed());
    assertNotNull(result.getDistributionMXN());
    assertEquals(new BigDecimal("2750"), result.getDistributionMXN().getBasePrice());
    assertNotNull(result.getDistributionMXN().getDistributor());
    assertEquals("1", result.getDistributionMXN().getDistributor().getId());
    assertEquals("DAM", result.getDistributionMXN().getDistributor().getName());
    assertNull(result.getDistributionMXN().getFinalPrice());
    assertNull(result.getDistributionMXN().getFirstAnnouncementDate());
    assertEquals(LocalDate.of(2024, 10, 29), result.getDistributionMXN().getPreOrderDate());
    assertEquals(LocalDate.of(2024, 11, 25), result.getDistributionMXN().getReleaseDate());
    assertTrue(result.getDistributionMXN().getReleaseDateConfirmed());
    assertEquals("https://tamashiiweb.com/item/14874", result.getTamashiiUrl());
    assertNotNull(result.getDistributionChannel());
    assertEquals("1", result.getDistributionChannel().getId());
    assertEquals("Stores", result.getDistributionChannel().getDistribution());
    assertEquals(LineUp.MYTH_CLOTH_EX, result.getLineUp());
    assertEquals(Series.SAINT_SEIYA, result.getSeries());
    assertEquals(Category.V3, result.getCategory());
    assertFalse(result.isMetal());
    assertTrue(result.isOce());
    assertTrue(result.isRevival());
    assertTrue(result.isPlain());
    assertTrue(result.isBroken());
    assertTrue(result.isGolden());
    assertTrue(result.isGold());
    assertTrue(result.isHk());
    assertFalse(result.isComic());
    assertTrue(result.isSet());
    assertEquals(Anniversary.A_20, result.getAnniversary());
    assertEquals(List.of("922/x738BH"), result.getOfficialImages());
    assertEquals(List.of("922/x738BH"), result.getOtherImages());
    assertNull(result.getRemarks());
  }

  @ParameterizedTest
  @MethodSource("provideTestDataAnniversary")
  void toFigure_whenCustomSourceFigurine_thenMapFigurine(
      String anniversarySource, Anniversary anniversary) {
    // Arrange
    SourceFigurine sourceFigurine = new SourceFigurine();
    sourceFigurine.setAnniversary(anniversarySource);

    // Act
    Figurine result = mapper.toFigure(sourceFigurine);

    // Assert
    assertNotNull(result);
    assertNull(result.getBaseName());
    assertNull(result.getDistributionJPY());
    assertNull(result.getDistributionMXN());
    assertNull(result.getTamashiiUrl());
    assertNull(result.getDistributionChannel());
    assertNull(result.getLineUp());
    assertNull(result.getSeries());
    assertNull(result.getCategory());
    assertFalse(result.isMetal());
    assertFalse(result.isOce());
    assertFalse(result.isRevival());
    assertFalse(result.isPlain());
    assertFalse(result.isBroken());
    assertFalse(result.isGolden());
    assertFalse(result.isGold());
    assertFalse(result.isHk());
    assertFalse(result.isComic());
    assertFalse(result.isSet());
    assertEquals(anniversary, result.getAnniversary());
    assertNull(result.getOfficialImages());
    assertNull(result.getOtherImages());
    assertNull(result.getRemarks());
  }

  private static Stream<Arguments> provideTestDataAnniversary() {
    return Stream.of(
        Arguments.of("10", Anniversary.A_10),
        Arguments.of("15", Anniversary.A_15),
        Arguments.of("20", Anniversary.A_20),
        Arguments.of("30", Anniversary.A_30),
        Arguments.of("40", Anniversary.A_40),
        Arguments.of("50", Anniversary.A_50));
  }

  @ParameterizedTest
  @MethodSource("provideTestDataCategory")
  void toFigure_whenCustomSourceFigurine_thenMapFigurine(String categorySource, Category category) {
    // Arrange
    SourceFigurine sourceFigurine = new SourceFigurine();
    sourceFigurine.setGroup(categorySource);

    // Act
    Figurine result = mapper.toFigure(sourceFigurine);

    // Assert
    assertNotNull(result);
    assertNull(result.getBaseName());
    assertNull(result.getDistributionJPY());
    assertNull(result.getDistributionMXN());
    assertNull(result.getTamashiiUrl());
    assertNull(result.getDistributionChannel());
    assertNull(result.getLineUp());
    assertNull(result.getSeries());
    assertEquals(category, result.getCategory());
    assertFalse(result.isMetal());
    assertFalse(result.isOce());
    assertFalse(result.isRevival());
    assertFalse(result.isPlain());
    assertFalse(result.isBroken());
    assertFalse(result.isGolden());
    assertFalse(result.isGold());
    assertFalse(result.isHk());
    assertFalse(result.isComic());
    assertFalse(result.isSet());
    assertNull(result.getAnniversary());
    assertNull(result.getOfficialImages());
    assertNull(result.getOtherImages());
    assertNull(result.getRemarks());
  }

  private static Stream<Arguments> provideTestDataCategory() {
    return Stream.of(
        Arguments.of("Bronze Saint V1", Category.V1),
        Arguments.of("Bronze Saint V2", Category.V2),
        Arguments.of("Bronze Saint V3", Category.V3),
        Arguments.of("Bronze Saint V4", Category.V4),
        Arguments.of("Bronze Saint V5", Category.V5),
        Arguments.of("Bronze Secondary", Category.SECONDARY),
        Arguments.of("Black Saint", Category.BLACK),
        Arguments.of("Steel", Category.STEEL),
        Arguments.of("Silver Saint", Category.SILVER),
        Arguments.of("Gold Saint", Category.GOLD),
        Arguments.of("God Robe", Category.ROBE),
        Arguments.of("Poseidon Scale", Category.SCALE),
        Arguments.of("Surplice Saint", Category.SURPLICE),
        Arguments.of("Specter", Category.SPECTER),
        Arguments.of("Judge", Category.JUDGE),
        Arguments.of("God", Category.GOD),
        Arguments.of("Inheritor", Category.INHERITOR));
  }

  @ParameterizedTest
  @MethodSource("provideTestDataLineUp")
  void toFigure_whenCustomSourceFigurine_thenMapFigurine(String lineUpSource, LineUp lineUp) {
    // Arrange
    SourceFigurine sourceFigurine = new SourceFigurine();
    sourceFigurine.setLineUp(lineUpSource);

    // Act
    Figurine result = mapper.toFigure(sourceFigurine);

    // Assert
    assertNotNull(result);
    assertNull(result.getBaseName());
    assertNull(result.getDistributionJPY());
    assertNull(result.getDistributionMXN());
    assertNull(result.getTamashiiUrl());
    assertNull(result.getDistributionChannel());
    assertEquals(lineUp, result.getLineUp());
    assertNull(result.getSeries());
    assertNull(result.getCategory());
    assertFalse(result.isMetal());
    assertFalse(result.isOce());
    assertFalse(result.isRevival());
    assertFalse(result.isPlain());
    assertFalse(result.isBroken());
    assertFalse(result.isGolden());
    assertFalse(result.isGold());
    assertFalse(result.isHk());
    assertFalse(result.isComic());
    assertFalse(result.isSet());
    assertNull(result.getAnniversary());
    assertNull(result.getOfficialImages());
    assertNull(result.getOtherImages());
    assertNull(result.getRemarks());
  }

  private static Stream<Arguments> provideTestDataLineUp() {
    return Stream.of(
        Arguments.of("Myth Cloth EX", LineUp.MYTH_CLOTH_EX),
        Arguments.of("Myth Cloth", LineUp.MYTH_CLOTH),
        Arguments.of("Appendix", LineUp.APPENDIX),
        Arguments.of("Saint Cloth Legend", LineUp.SC_LEGEND),
        Arguments.of("Figuarts", LineUp.FIGUARTS),
        Arguments.of("Figuarts Zero Metallic Touch", LineUp.FIGUARTS_ZERO),
        Arguments.of("Saint Cloth Crown", LineUp.SC_CROWN),
        Arguments.of("DD Panoramation", LineUp.DDP));
  }

  @ParameterizedTest
  @MethodSource("provideTestDataSeries")
  void toFigure_whenCustomSourceFigurine_thenMapFigurine(String seriesSource, Series series) {
    // Arrange
    SourceFigurine sourceFigurine = new SourceFigurine();
    sourceFigurine.setSeries(seriesSource);

    // Act
    Figurine result = mapper.toFigure(sourceFigurine);

    // Assert
    assertNotNull(result);
    assertNull(result.getBaseName());
    assertNull(result.getDistributionJPY());
    assertNull(result.getDistributionMXN());
    assertNull(result.getTamashiiUrl());
    assertNull(result.getDistributionChannel());
    assertNull(result.getLineUp());
    assertEquals(series, result.getSeries());
    assertNull(result.getCategory());
    assertFalse(result.isMetal());
    assertFalse(result.isOce());
    assertFalse(result.isRevival());
    assertFalse(result.isPlain());
    assertFalse(result.isBroken());
    assertFalse(result.isGolden());
    assertFalse(result.isGold());
    assertFalse(result.isHk());
    assertFalse(result.isComic());
    assertFalse(result.isSet());
    assertNull(result.getAnniversary());
    assertNull(result.getOfficialImages());
    assertNull(result.getOtherImages());
    assertNull(result.getRemarks());
  }

  private static Stream<Arguments> provideTestDataSeries() {
    return Stream.of(
        Arguments.of("Saint Seiya", Series.SAINT_SEIYA),
        Arguments.of("Saintia Sho", Series.SAINTIA_SHO),
        Arguments.of("Soul of Gold", Series.SOG),
        Arguments.of("Saint Seiya Legend Of Sanctuary", Series.SS_LEGEND_OF_SANCTUARY),
        Arguments.of("Saint Seiya Omega", Series.SS_OMEGA),
        Arguments.of("The Lost Canvas", Series.LOST_CANVAS),
        Arguments.of("Saint Seiya The Beginning", Series.SS_THE_BEGINNING));
  }
}
