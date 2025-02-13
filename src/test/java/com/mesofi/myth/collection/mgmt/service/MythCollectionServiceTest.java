package com.mesofi.myth.collection.mgmt.service;

import static com.mesofi.myth.collection.mgmt.common.TestUtils.loadFigurines;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mesofi.myth.collection.mgmt.exceptions.SourceFigurineBulkException;
import com.mesofi.myth.collection.mgmt.mappers.FigurineMapper;
import com.mesofi.myth.collection.mgmt.model.Anniversary;
import com.mesofi.myth.collection.mgmt.model.Category;
import com.mesofi.myth.collection.mgmt.model.Distribution;
import com.mesofi.myth.collection.mgmt.model.DistributionChannel;
import com.mesofi.myth.collection.mgmt.model.Distributor;
import com.mesofi.myth.collection.mgmt.model.Figurine;
import com.mesofi.myth.collection.mgmt.model.LineUp;
import com.mesofi.myth.collection.mgmt.model.Restock;
import com.mesofi.myth.collection.mgmt.model.Series;
import com.mesofi.myth.collection.mgmt.model.SourceFigurine;
import com.mesofi.myth.collection.mgmt.model.Status;
import com.mesofi.myth.collection.mgmt.repository.MythCollectionRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class MythCollectionServiceTest {
  @Mock private MythCollectionRepository repository;
  @Mock private FigurineMapper mapper;
  @Mock MultipartFile multipartFile;

  @InjectMocks private MythCollectionService service;

  @Test
  void createFigurines_whenBasicFigurines_thenThrowIOException() throws IOException {
    when(multipartFile.getInputStream()).thenThrow(IOException.class);

    // Assert
    assertThatExceptionOfType(SourceFigurineBulkException.class)
        .isThrownBy(() -> service.createFigurines(multipartFile))
        .withMessage("Unable to load figurines");
  }

  @Test
  void createFigurines_whenBasicFigurines_thenLoadThemAll() {
    String figurines = loadFigurines("basic.csv");

    MultipartFile file = new MockMultipartFile("file", "basic.csv", null, figurines.getBytes());

    Figurine figurine = new Figurine();
    figurine.setBaseName("Some Figurine");

    when(mapper.toFigure(any(SourceFigurine.class))).thenReturn(figurine);
    when(repository.save(figurine)).thenReturn(figurine);

    // Act
    List<Figurine> result = service.createFigurines(file);
    assertNotNull(result);
    assertEquals(2, result.size());

    verify(mapper, times(2)).toFigure(any(SourceFigurine.class));
    verify(repository, times(2)).save(figurine);
  }

  @Test
  void createFigurine_whenFigurinePopulated_thenCreateFigurineAndReturnSaved() {

    // Arrange
    Figurine figurineToSave =
        new Figurine(
            null, "Seiya", null, null, null, null, null, false, false, false, false, false, false,
            false, false, false, false, null, null);
    figurineToSave.setOfficialImages(List.of("abc"));
    figurineToSave.setOtherImages(List.of("def"));

    Figurine savedFigurine =
        new Figurine(
            "1", "Seiya", null, null, null, null, null, false, false, false, false, false, false,
            false, false, false, false, null, null);
    savedFigurine.setOfficialImages(List.of("abc"));
    savedFigurine.setOtherImages(List.of("def"));

    when(repository.save(figurineToSave)).thenReturn(savedFigurine);

    // Act
    Figurine result = service.createFigurine(figurineToSave);

    // Assert
    assertNotNull(result);
    assertEquals("1", result.getId());
    assertEquals("Seiya", result.getBaseName());
    assertEquals("Seiya", result.getDisplayableName());
    assertEquals(1, result.getOfficialImages().size());
    assertEquals(
        "https://imagizer.imageshack.com/v2/640x480q70/abc.jpg",
        result.getOfficialImages().getFirst());
    assertEquals(1, result.getOtherImages().size());
    assertEquals(
        "https://imagizer.imageshack.com/v2/640x480q70/def.jpg",
        result.getOtherImages().getFirst());
    verify(repository, times(1)).save(figurineToSave);
  }

  @Test
  void getAllFigurines_whenExistingFigurines_thenGetAllFigurinesWithRestocks() {
    Figurine figurine1 = new Figurine();
    Figurine figurine2 = new Figurine();

    figurine1.setBaseName("Pegasus Seiya");
    figurine1.setCategory(Category.V1);
    figurine1.setOfficialImages(List.of("abc"));
    figurine1.setOtherImages(List.of("def"));

    figurine2.setBaseName("Sea Emperor");
    figurine2.setCategory(Category.SCALE);

    // Arrange
    List<Figurine> list = new ArrayList<>();
    list.add(figurine1);
    list.add(figurine2);
    Sort sort = Sort.by(Sort.Order.asc("distributionJPY.releaseDate"));
    when(repository.findAll(sort)).thenReturn(list);

    // Act
    List<Figurine> result = service.getAllFigurines(false);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Pegasus Seiya", result.getFirst().getBaseName());
    assertEquals("Pegasus Seiya", result.getFirst().getDisplayableName());
    assertEquals(1, result.getFirst().getOfficialImages().size());
    assertEquals(
        "https://imagizer.imageshack.com/v2/640x480q70/abc.jpg",
        result.getFirst().getOfficialImages().getFirst());
    assertEquals(1, result.getFirst().getOtherImages().size());
    assertEquals(
        "https://imagizer.imageshack.com/v2/640x480q70/def.jpg",
        result.getFirst().getOtherImages().getFirst());

    assertNull(result.getFirst().getRestocks());

    assertEquals("Sea Emperor", result.get(1).getBaseName());
    assertEquals("Sea Emperor", result.get(1).getDisplayableName());
    assertNull(result.get(1).getRestocks());

    verify(repository).findAll(Sort.by(Sort.Order.asc("distributionJPY.releaseDate")));
  }

  @Test
  void getAllFigurines_whenDifferentFigurines_thenGetAllFigurinesWithoutRestocks() {
    Figurine figurine1 = new Figurine();
    Figurine figurine2 = new Figurine();

    figurine1.setBaseName("Pegasus Seiya");
    figurine1.setCategory(Category.V1);

    figurine2.setBaseName("Sea Emperor");
    figurine2.setCategory(Category.SCALE);

    // Arrange
    List<Figurine> list = new ArrayList<>();
    list.add(figurine1);
    list.add(figurine2);
    Sort sort = Sort.by(Sort.Order.asc("distributionJPY.releaseDate"));
    when(repository.findAll(sort)).thenReturn(list);

    // Act
    List<Figurine> result = service.getAllFigurines(true);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Pegasus Seiya", result.getFirst().getBaseName());
    assertEquals("Pegasus Seiya", result.getFirst().getDisplayableName());
    assertEquals(Category.V1, result.getFirst().getCategory());
    assertNull(result.get(0).getRestocks());

    assertEquals("Sea Emperor", result.get(1).getBaseName());
    assertEquals("Sea Emperor", result.get(1).getDisplayableName());
    assertEquals(Category.SCALE, result.get(1).getCategory());
    assertNull(result.get(1).getRestocks());

    verify(repository).findAll(Sort.by(Sort.Order.asc("distributionJPY.releaseDate")));
  }

  @Test
  void getAllFigurines_whenSameFigurines_thenGetAllFigurinesWithoutRestocks() {
    Figurine figurine1 = new Figurine();
    Figurine figurine2 = new Figurine();

    Distribution distributionJPY = new Distribution();
    distributionJPY.setBasePrice(new BigDecimal("12000"));
    distributionJPY.setReleaseDate(LocalDate.of(2025, 11, 2));

    Distribution distributionMXN = new Distribution();
    distributionMXN.setDistributor(new Distributor("123", "DAM"));
    distributionMXN.setBasePrice(new BigDecimal("1500"));

    figurine1.setBaseName("Pegasus Seiya");
    figurine1.setCategory(Category.V1);
    figurine1.setDistributionJPY(distributionJPY);
    figurine1.setDistributionMXN(distributionMXN);
    figurine1.setTamashiiUrl("https://tamashiiweb.com/item/1288");
    figurine1.setDistributionChannel(new DistributionChannel("123", "Stores"));
    figurine1.setRemarks("some comment");

    figurine2.setBaseName("Pegasus Seiya");
    figurine2.setCategory(Category.V1);
    figurine2.setDistributionJPY(distributionJPY);
    figurine2.setDistributionMXN(distributionMXN);
    figurine2.setTamashiiUrl("https://tamashiiweb.com/item/1288");
    figurine2.setDistributionChannel(new DistributionChannel("123", "Stores"));
    figurine2.setRemarks("some comment");

    // Arrange
    List<Figurine> list = new ArrayList<>();
    list.add(figurine1);
    list.add(figurine2);
    Sort sort = Sort.by(Sort.Order.asc("distributionJPY.releaseDate"));
    when(repository.findAll(sort)).thenReturn(list);

    // Act
    List<Figurine> result = service.getAllFigurines(true);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Pegasus Seiya", result.getFirst().getBaseName());
    assertEquals("Pegasus Seiya", result.getFirst().getDisplayableName());
    assertEquals(Category.V1, result.getFirst().getCategory());

    List<Restock> restocks = result.getFirst().getRestocks();

    assertNotNull(restocks);
    assertEquals(1, restocks.size());
    assertEquals(distributionJPY, restocks.getFirst().getDistributionJPY());
    assertEquals(distributionMXN, restocks.getFirst().getDistributionMXN());
    assertEquals("https://tamashiiweb.com/item/1288", restocks.getFirst().getTamashiiUrl());
    assertEquals(
        new DistributionChannel("123", "Stores"), restocks.getFirst().getDistributionChannel());
    assertEquals("some comment", restocks.getFirst().getRemarks());

    verify(repository).findAll(Sort.by(Sort.Order.asc("distributionJPY.releaseDate")));
  }

  @Test
  void getAllFigurines_whenMultipleFigurines_thenGetAllFigurinesWithoutRestocks() {
    Figurine figurine1 = new Figurine();
    figurine1.setId("1234567890");
    figurine1.setBaseName("Aiolia Leo");
    figurine1.setDistributionJPY(
        new Distribution(
            null,
            new BigDecimal("8000"),
            null,
            null,
            LocalDate.of(2017, 11, 28),
            LocalDate.of(2018, 4, 28),
            true));
    figurine1.setTamashiiUrl("https://tamashiiweb.com/item/12395");
    figurine1.setDistributionChannel(new DistributionChannel("123", "Stores"));
    figurine1.setLineUp(LineUp.MYTH_CLOTH_EX);
    figurine1.setSeries(Series.SAINT_SEIYA);
    figurine1.setCategory(Category.GOLD);
    figurine1.setRevival(true);

    Figurine figurine2 = new Figurine();
    figurine2.setId("1234567891");
    figurine2.setBaseName("Aiolia Leo");
    figurine2.setDistributionJPY(
        new Distribution(
            null,
            new BigDecimal("15000"),
            null,
            LocalDate.of(2024, 2, 16),
            LocalDate.of(2024, 1, 24),
            LocalDate.of(2024, 1, 24),
            true));
    figurine2.setTamashiiUrl("https://tamashiiweb.com/item/14207");
    figurine2.setDistributionChannel(new DistributionChannel("123", "Tamashii Store"));
    figurine2.setLineUp(LineUp.MYTH_CLOTH_EX);
    figurine2.setSeries(Series.SAINT_SEIYA);
    figurine2.setCategory(Category.GOLD);
    figurine2.setRevival(true);
    figurine2.setRemarks("Sold via lottery from Jan 10 to Jan 16");

    Figurine figurine3 = new Figurine();
    figurine3.setId("1234567892");
    figurine3.setBaseName("Eta Benetnasch Mime");
    figurine3.setDistributionJPY(
        new Distribution(null, null, null, LocalDate.of(2024, 11, 15), null, null, false));
    figurine3.setLineUp(LineUp.MYTH_CLOTH_EX);
    figurine3.setSeries(Series.SAINT_SEIYA);
    figurine3.setCategory(Category.ROBE);

    // Arrange
    List<Figurine> list = new ArrayList<>();
    list.add(figurine1);
    list.add(figurine2);
    list.add(figurine3);
    Sort sort = Sort.by(Sort.Order.asc("distributionJPY.releaseDate"));
    when(repository.findAll(sort)).thenReturn(list);

    // Act
    List<Figurine> result = service.getAllFigurines(true);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("1234567890", result.getFirst().getId());
    assertEquals("Aiolia Leo", result.getFirst().getBaseName());
    assertEquals("Aiolia Leo <Revival Ver.>", result.getFirst().getDisplayableName());
    assertEquals("https://tamashiiweb.com/item/12395", result.getFirst().getTamashiiUrl());
    assertEquals(LineUp.MYTH_CLOTH_EX, result.getFirst().getLineUp());
    assertEquals(Series.SAINT_SEIYA, result.getFirst().getSeries());
    assertEquals(Category.GOLD, result.getFirst().getCategory());

    List<Restock> restocks = result.getFirst().getRestocks();

    assertNotNull(restocks);
    assertEquals(1, restocks.size());
    assertEquals(new BigDecimal("15000"), restocks.getFirst().getDistributionJPY().getBasePrice());
    assertEquals(
        LocalDate.of(2024, 2, 16),
        restocks.getFirst().getDistributionJPY().getFirstAnnouncementDate());
    assertEquals(
        LocalDate.of(2024, 1, 24), restocks.getFirst().getDistributionJPY().getPreOrderDate());
    assertEquals(
        LocalDate.of(2024, 1, 24), restocks.getFirst().getDistributionJPY().getReleaseDate());
    assertEquals(true, restocks.getFirst().getDistributionJPY().getReleaseDateConfirmed());
    assertEquals("https://tamashiiweb.com/item/14207", restocks.getFirst().getTamashiiUrl());
    assertEquals("Sold via lottery from Jan 10 to Jan 16", restocks.getFirst().getRemarks());

    assertEquals("1234567892", result.get(1).getId());
    assertEquals("Eta Benetnasch Mime", result.get(1).getBaseName());
    assertEquals("Eta Benetnasch Mime", result.get(1).getDisplayableName());
    assertNull(result.get(1).getTamashiiUrl());
    assertEquals(LineUp.MYTH_CLOTH_EX, result.get(1).getLineUp());
    assertEquals(Series.SAINT_SEIYA, result.get(1).getSeries());
    assertEquals(Category.ROBE, result.get(1).getCategory());

    verify(repository).findAll(Sort.by(Sort.Order.asc("distributionJPY.releaseDate")));
  }

  @Test
  void getAllFigurines_whenMultipleFigurines_thenGetAllFigurinesOrderedByReleaseDate() {
    Figurine figurine1 = new Figurine();
    figurine1.setBaseName("Seiya");

    Figurine figurine2 = new Figurine();
    figurine2.setBaseName("Shiryu");

    Figurine figurine3 = new Figurine();
    figurine3.setBaseName("Hyoga");
    figurine3.setDistributionJPY(
        new Distribution(null, null, null, null, null, LocalDate.of(2025, 5, 1), null));

    Figurine figurine4 = new Figurine();
    figurine4.setBaseName("Shun");
    figurine4.setDistributionJPY(
        new Distribution(null, null, null, null, null, LocalDate.of(2025, 4, 1), null));

    Figurine figurine5 = new Figurine();
    figurine5.setBaseName("Ikki");
    figurine5.setDistributionJPY(
        new Distribution(null, null, null, null, null, LocalDate.of(2025, 3, 1), null));

    Figurine figurine6 = new Figurine();
    figurine6.setBaseName("Leo");
    figurine6.setDistributionJPY(
        new Distribution(null, null, null, null, null, LocalDate.of(2025, 2, 1), null));

    // Arrange
    List<Figurine> list = new ArrayList<>();
    list.add(figurine1);
    list.add(figurine2);
    list.add(figurine3);
    list.add(figurine4);
    list.add(figurine5);
    list.add(figurine6);

    Sort sort = Sort.by(Sort.Order.asc("distributionJPY.releaseDate"));
    when(repository.findAll(sort)).thenReturn(list);

    // Act
    List<Figurine> result = service.getAllFigurines(false);

    // Assert
    assertEquals(6, result.size());
    assertEquals("Seiya", result.getFirst().getBaseName());
    assertNull(result.getFirst().getDistributionJPY());

    assertEquals("Shiryu", result.get(1).getBaseName());
    assertNull(result.get(1).getDistributionJPY());

    assertEquals("Hyoga", result.get(2).getBaseName());
    assertEquals(LocalDate.of(2025, 5, 1), result.get(2).getDistributionJPY().getReleaseDate());

    assertEquals("Shun", result.get(3).getBaseName());
    assertEquals(LocalDate.of(2025, 4, 1), result.get(3).getDistributionJPY().getReleaseDate());

    assertEquals("Ikki", result.get(4).getBaseName());
    assertEquals(LocalDate.of(2025, 3, 1), result.get(4).getDistributionJPY().getReleaseDate());

    assertEquals("Leo", result.get(5).getBaseName());
    assertEquals(LocalDate.of(2025, 2, 1), result.get(5).getDistributionJPY().getReleaseDate());

    verify(repository).findAll(Sort.by(Sort.Order.asc("distributionJPY.releaseDate")));
  }

  @Test
  void getAllFigurines_whenMultipleFigurines_thenCalculateFinalPrice() {
    Figurine figurine1 = new Figurine(); // no distributionJPY
    figurine1.setBaseName("Seiya");

    Figurine figurine2 = new Figurine();
    figurine2.setBaseName("Shiryu");
    figurine2.setDistributionJPY(
        new Distribution(null, null, null, null, null, null, null)); // no basePrice

    Figurine figurine3 = new Figurine();
    figurine3.setBaseName("Hyoga");
    figurine3.setDistributionJPY(
        new Distribution(
            null, new BigDecimal("4000"), null, null, null, LocalDate.of(1995, 1, 1), null));

    Figurine figurine4 = new Figurine();
    figurine4.setBaseName("Hyoga");
    figurine4.setDistributionJPY(
        new Distribution(
            null, new BigDecimal("4000"), null, null, null, LocalDate.of(2003, 1, 1), null));

    Figurine figurine5 = new Figurine();
    figurine5.setBaseName("Hyoga");
    figurine5.setDistributionJPY(
        new Distribution(
            null, new BigDecimal("4000"), null, null, null, LocalDate.of(2015, 1, 1), null));

    Figurine figurine6 = new Figurine();
    figurine6.setBaseName("Hyoga");
    figurine6.setDistributionJPY(
        new Distribution(
            null, new BigDecimal("4000"), null, null, null, LocalDate.of(2020, 1, 1), null));

    // Arrange
    List<Figurine> list = new ArrayList<>();
    list.add(figurine1);
    list.add(figurine2);
    list.add(figurine3);
    list.add(figurine4);
    list.add(figurine5);
    list.add(figurine6);

    Sort sort = Sort.by(Sort.Order.asc("distributionJPY.releaseDate"));
    when(repository.findAll(sort)).thenReturn(list);

    // Act
    List<Figurine> result = service.getAllFigurines(false);

    // Assert
    assertEquals(6, result.size());
    assertNull(result.getFirst().getDistributionJPY());
    assertNull(result.get(1).getDistributionJPY().getFinalPrice());
    assertEquals(new BigDecimal("4400.0"), result.get(2).getDistributionJPY().getFinalPrice());
    assertEquals(new BigDecimal("4320.00"), result.get(3).getDistributionJPY().getFinalPrice());
    assertEquals(new BigDecimal("4200.00"), result.get(4).getDistributionJPY().getFinalPrice());
    assertEquals(new BigDecimal("4400.0"), result.get(5).getDistributionJPY().getFinalPrice());

    verify(repository).findAll(Sort.by(Sort.Order.asc("distributionJPY.releaseDate")));
  }

  @Test
  void getAllFigurines_whenMultipleFigurines_thenCalculateStatus() {
    Figurine figurine1 = new Figurine();
    figurine1.setBaseName("Seiya");

    LocalDate announcementDate2 = LocalDate.now().minus(6, ChronoUnit.YEARS);
    Figurine figurine2 = new Figurine();
    figurine2.setBaseName("Shiryu");
    figurine2.setDistributionJPY(
        new Distribution(null, null, null, announcementDate2, null, null, null));

    LocalDate announcementDate3 = LocalDate.now().minus(5, ChronoUnit.YEARS);
    Figurine figurine3 = new Figurine();
    figurine3.setBaseName("Hyoga");
    figurine3.setDistributionJPY(
        new Distribution(null, null, null, announcementDate3, null, null, null));

    LocalDate announcementDate4 = LocalDate.now().minus(4, ChronoUnit.YEARS);
    Figurine figurine4 = new Figurine();
    figurine4.setBaseName("Hyoga");
    figurine4.setDistributionJPY(
        new Distribution(null, null, null, announcementDate4, null, null, null));

    Figurine figurine5 = new Figurine();
    figurine5.setBaseName("Shun");
    figurine5.setDistributionJPY(
        new Distribution(null, null, null, null, null, LocalDate.of(2003, 1, 1), true));

    Figurine figurine6 = new Figurine();
    figurine6.setBaseName("Ikki");
    figurine6.setDistributionJPY(
        new Distribution(null, null, null, null, null, LocalDate.of(2015, 1, 1), true));

    Figurine figurine7 = new Figurine();
    figurine7.setBaseName("Athena");
    figurine7.setDistributionJPY(
        new Distribution(null, null, null, null, null, LocalDate.of(2020, 1, 1), false));

    // Arrange
    List<Figurine> list = new ArrayList<>();
    list.add(figurine1);
    list.add(figurine2);
    list.add(figurine3);
    list.add(figurine4);
    list.add(figurine5);
    list.add(figurine6);
    list.add(figurine7);

    Sort sort = Sort.by(Sort.Order.asc("distributionJPY.releaseDate"));
    when(repository.findAll(sort)).thenReturn(list);

    // Act
    List<Figurine> result = service.getAllFigurines(false);

    // Assert
    assertEquals(7, result.size());
    assertEquals(Status.RELEASE_TBD, result.getFirst().getStatus());
    assertEquals(Status.UNRELEASED, result.get(1).getStatus());
    assertEquals(Status.PROTOTYPE, result.get(2).getStatus());
    assertEquals(Status.PROTOTYPE, result.get(3).getStatus());
    assertEquals(Status.FUTURE_RELEASE, result.get(4).getStatus());
    assertEquals(Status.RELEASED, result.get(5).getStatus());
    assertEquals(Status.RELEASED, result.get(6).getStatus());

    verify(repository).findAll(Sort.by(Sort.Order.asc("distributionJPY.releaseDate")));
  }

  @Test
  void complementImageUrls_whenNoImages_thenReturnNoImages() {
    // Act
    List<String> result = service.complementImageUrls(null);

    // Assert
    assertNull(result);
  }

  @Test
  void complementImageUrls_whenProvidedImages_thenReturnImages() {
    // Act
    List<String> result = service.complementImageUrls(List.of("abc", "dfg", "hij.png"));

    // Assert
    assertNotNull(result);
    assertEquals(3, result.size());
    assertEquals("https://imagizer.imageshack.com/v2/640x480q70/abc.jpg", result.get(0));
    assertEquals("https://imagizer.imageshack.com/v2/640x480q70/dfg.jpg", result.get(1));
    assertEquals("https://imagizer.imageshack.com/v2/640x480q70/hij.png", result.get(2));
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
    theFigurine.setLineUp(LineUp.MYTH_CLOTH_EX);
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
    theFigurine.setLineUp(LineUp.MYTH_CLOTH_EX);
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
    theFigurine.setBaseName("Odin Aiolia");
    theFigurine.setSeries(Series.SOG);
    assertEquals("Odin Aiolia", service.calculateDisplayableName(theFigurine));

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
    theFigurine.setBaseName("Phoenix Ikki");
    theFigurine.setCategory(Category.V2);
    theFigurine.setLineUp(LineUp.MYTH_CLOTH_EX);
    theFigurine.setBroken(true);
    assertEquals("Phoenix Ikki [New Bronze Cloth]", service.calculateDisplayableName(theFigurine));

    theFigurine = new Figurine();
    theFigurine.setBaseName("God Pedestal");
    theFigurine.setSeries(Series.SOG);
    theFigurine.setSet(true);
    assertEquals("God Pedestal Set", service.calculateDisplayableName(theFigurine));

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
    theFigurine.setBaseName("Aries Shion");
    theFigurine.setOce(true);
    theFigurine.setHk(true);
    theFigurine.setSet(true);
    theFigurine.setCategory(Category.SURPLICE);
    theFigurine.setLineUp(LineUp.MYTH_CLOTH);
    assertEquals(
        "Aries Shion & The Pope Set ~Asian Edition~",
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

    theFigurine = new Figurine();
    theFigurine.setBaseName("Pegasus Seiya");
    theFigurine.setCategory(Category.V2);
    assertEquals("Pegasus Seiya", service.calculateDisplayableName(theFigurine));
  }
}
