package com.mesofi.myth.collection.mgmt.service;

import com.mesofi.myth.collection.mgmt.exceptions.FigurineNotFoundException;
import com.mesofi.myth.collection.mgmt.exceptions.SourceFigurineBulkException;
import com.mesofi.myth.collection.mgmt.mappers.FigurineMapper;
import com.mesofi.myth.collection.mgmt.model.*;
import com.mesofi.myth.collection.mgmt.repository.MythCollectionRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoLocalDate;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Contains the main logic that handles the figurines. */
@Slf4j
@Service
@AllArgsConstructor
public class MythCollectionService {

  private final MythCollectionRepository repository;
  private final FigurineMapper mapper;

  /**
   * Create a list of figurines based on the source file.
   *
   * @param file The file to be used as input.
   * @return The list of figurines.
   */
  public List<Figurine> createFigurines(final MultipartFile file) {

    try (Reader reader = new InputStreamReader(file.getInputStream())) {
      // Use OpenCSV to parse the file and map each row to a SourceFigurine object
      return new CsvToBeanBuilder<SourceFigurine>(reader)
              .withType(SourceFigurine.class) // Specify the type of object to map to
              .build()
              .parse()
              .stream()
              .map(mapper::toFigure)
              .peek(this::createFigurine)
              .toList();
    } catch (IOException e) {
      throw new SourceFigurineBulkException("Unable to load figurines");
    }
  }

  /**
   * Creates a new figurine.
   *
   * @param figurine The figurine to be created.
   * @return The figurine created in the DB.
   */
  public Figurine createFigurine(final Figurine figurine) {
    log.info("A new figure is about to be created with name [{}] ...", figurine.getBaseName());

    Figurine created = repository.save(figurine);

    // Calculates additional information ...
    populateAdditionalInfo(created);

    log.info("A new figure has been created with id: {}", created.getId());
    return created;
  }

  /**
   * Update an existing figurine.
   *
   * @param id The unique identifier for the figurine.
   * @param figurine The updated figurine.
   * @return The figurine.
   */
  public Figurine updateFigurine(String id, Figurine figurine) {
    log.info("Existing figure to be update with id: [{}] ...", id);

    Figurine existingFigurine =
        repository
            .findById(id)
            .orElseThrow(() -> new FigurineNotFoundException("Figurine not found with id: " + id));

    // Modify the document
    existingFigurine.setBaseName(figurine.getBaseName());
    existingFigurine.setLineUp(figurine.getLineUp());
    existingFigurine.setSeries(figurine.getSeries());
    existingFigurine.setCategory(figurine.getCategory());
    existingFigurine.setRevival(figurine.isRevival());
    existingFigurine.setOce(figurine.isOce());
    existingFigurine.setMetal(figurine.isMetal());
    existingFigurine.setGolden(figurine.isGolden());
    existingFigurine.setGold(figurine.isGold());
    existingFigurine.setBroken(figurine.isBroken());
    existingFigurine.setPlain(figurine.isPlain());
    existingFigurine.setHk(figurine.isHk());
    existingFigurine.setComic(figurine.isComic());
    existingFigurine.setSet(figurine.isSet());
    existingFigurine.setAnniversary(figurine.getAnniversary());
    existingFigurine.setDistributionJPY(figurine.getDistributionJPY());
    existingFigurine.setDistributionMXN(figurine.getDistributionMXN());
    existingFigurine.setTamashiiUrl(figurine.getTamashiiUrl());
    existingFigurine.setDistributionChannel(figurine.getDistributionChannel());
    existingFigurine.setOfficialImages(figurine.getOfficialImages());
    existingFigurine.setOtherImages(figurine.getOtherImages());
    existingFigurine.setRemarks(figurine.getRemarks());

    // Save the updated document
    Figurine updated = repository.save(existingFigurine);

    // Calculates additional information ...
    populateAdditionalInfo(updated);

    log.info("Existing figure has been updated with id: {}", updated.getId());
    return updated;
  }

  /**
   * Gets all the existing figurine.
   *
   * @param excludeRestocks Flag to exclude the restocks.
   * @return A list with all the existing figurines.
   */
  public List<Figurine> getAllFigurines(boolean excludeRestocks) {
    log.info("Retrieving all the existing figurines ...");

    Sort sort = Sort.by(Sort.Order.asc("distributionJPY.releaseDate"));
    List<Figurine> allFigurines = repository.findAll(sort);

    List<Figurine> allFigurinesFiltered = new ArrayList<>();
    if (excludeRestocks) {
      for (Figurine currFigurine : allFigurines) {
        if (allFigurinesFiltered.contains(currFigurine)) {
          for (Figurine currFiltered : allFigurinesFiltered) {
            if (currFiltered.equals(currFigurine)) {
              if (Objects.isNull(currFiltered.getRestocks())) {
                currFiltered.setRestocks(new ArrayList<>());
              }
              Restock restock = new Restock();
              restock.setDistributionJPY(currFigurine.getDistributionJPY());
              restock.setDistributionMXN(currFigurine.getDistributionMXN());
              restock.setTamashiiUrl(currFigurine.getTamashiiUrl());
              restock.setDistributionChannel(currFigurine.getDistributionChannel());
              restock.setRemarks(currFigurine.getRemarks());

              currFiltered.getRestocks().add(restock);
            }
          }
        } else {
          allFigurinesFiltered.add(currFigurine);
        }
      }
    } else {
      allFigurinesFiltered = allFigurines;
    }

    List<Figurine> figurineList =
        allFigurinesFiltered.stream()
            .peek(this::populateAdditionalInfo)
            .sorted(
                (f1, f2) -> {
                  if (geReleaseDate(f1).isPresent() && geReleaseDate(f2).isPresent()) {
                    return f2.getDistributionJPY()
                        .getReleaseDate()
                        .compareTo(f1.getDistributionJPY().getReleaseDate());
                  }
                  return 0;
                })
            .toList();

    log.info("Found {} figurines", figurineList.size());

    // We create one more list with sorted figurines based on the status.
    List<Figurine> list = new ArrayList<>();
    list.addAll(figurineList.stream().filter($ -> $.getStatus() == Status.RELEASE_TBD).toList());
    list.addAll(figurineList.stream().filter($ -> $.getStatus() == Status.FUTURE_RELEASE).toList());
    list.addAll(figurineList.stream().filter($ -> $.getStatus() == Status.RELEASED).toList());
    list.addAll(getFigurinesFilteredByStatus(figurineList, Status.PROTOTYPE));
    list.addAll(getFigurinesFilteredByStatus(figurineList, Status.UNRELEASED));

    return list;
  }

  private List<Figurine> getFigurinesFilteredByStatus(
      List<Figurine> figurineList, Status prototype) {
    return figurineList.stream()
        .filter(figurine -> figurine.getStatus() == prototype)
        .sorted(
            Comparator.comparing((Figurine f) -> f.getDistributionJPY().getFirstAnnouncementDate())
                .reversed())
        .toList();
  }

  /**
   * Get an existing figurine or an exception is thrown when it is not found.
   *
   * @param id The unique identifier.
   * @return The figure.
   */
  public Figurine getFigurine(String id) {
    log.info("Retrieving figure with id: [{}] ...", id);

    return repository
        .findById(id)
        .orElseThrow(() -> new FigurineNotFoundException("Figurine not found with id: " + id));
  }

  /**
   * Populates additional information for the figurine.
   *
   * @param figurine The figurine.
   */
  private void populateAdditionalInfo(Figurine figurine) {
    figurine.setDisplayableName(calculateDisplayableName(figurine));

    Distribution distJPY = figurine.getDistributionJPY();
    Optional.ofNullable(distJPY)
        .ifPresent(
            $ -> {
              if (Objects.nonNull($.getBasePrice())) {
                distJPY.setFinalPrice(calculateFinalPrice($.getBasePrice(), $.getReleaseDate()));
              }
            });

    figurine.setStatus(calculateStatus(figurine));

    figurine.setOfficialImages(complementImageUrls(figurine.getOfficialImages()));
    figurine.setOtherImages(complementImageUrls(figurine.getOtherImages()));
  }

  private Status calculateStatus(Figurine figurine) {
    Distribution jpy = figurine.getDistributionJPY();
    LocalDate releaseDate = Optional.ofNullable(jpy).map(Distribution::getReleaseDate).orElse(null);
    if (Objects.nonNull(releaseDate)) {
      return releaseDate.isBefore(LocalDate.now()) ? Status.RELEASED : Status.FUTURE_RELEASE;
    } else {
      LocalDate anncDate =
          Optional.ofNullable(jpy).map(Distribution::getFirstAnnouncementDate).orElse(null);
      if (Objects.nonNull(anncDate)) {
        Period period = Period.between(anncDate, LocalDate.now());
        return period.getYears() > 5 ? Status.UNRELEASED : Status.PROTOTYPE;
      } else {
        return Status.RELEASE_TBD;
      }
    }
  }

  /**
   * Gets the release date, if the release date is not found, then it gets an empty reference.
   *
   * @param figurine The figurine.
   * @return The release date or empty if it was not found.
   */
  private Optional<LocalDate> geReleaseDate(Figurine figurine) {
    return Optional.ofNullable(figurine.getDistributionJPY()).map(Distribution::getReleaseDate);
  }

  /**
   * Complement the image URL.
   *
   * @param images The image identifier.
   * @return The image URL.
   */
  public List<String> complementImageUrls(List<String> images) {
    return Objects.isNull(images)
        ? null
        : images.stream()
            .map(
                img -> {
                  final String URL = "https://imagizer.imageshack.com/v2/640x480q70/";
                  if (img.contains("png")) {
                    return URL + img;
                  } else {
                    return URL + img + ".jpg";
                  }
                })
            .toList();
  }

  /**
   * Based on the base price and the release date, calculates the final price for the Japan
   * figurines.
   *
   * @param basePrice The base price.
   * @param releaseDate The release date.
   * @return The final price for the figurine.
   */
  private BigDecimal calculateFinalPrice(BigDecimal basePrice, LocalDate releaseDate) {
    ChronoLocalDate april1997 = LocalDate.of(1997, 4, 1);
    ChronoLocalDate april2014 = LocalDate.of(2014, 4, 1);
    ChronoLocalDate october2019 = LocalDate.of(2019, 10, 1);

    String rate;
    if (releaseDate.isAfter(april1997) && releaseDate.isBefore(april2014)) {
      rate = ".05"; // 5%
    } else if (releaseDate.isAfter(april2014) && releaseDate.isBefore(october2019)) {
      rate = ".08"; // 8%
    } else {
      rate = ".1"; // 10%
    }
    return basePrice.add(basePrice.multiply(new BigDecimal(rate)));
  }

  /**
   * Calculates the displayable name based on the figure attributes.
   *
   * @param figurine The figurine with the all its attributes.
   * @return The displayable name.
   */
  public String calculateDisplayableName(Figurine figurine) {
    final String MYSTERIOUS = "mysterious";
    final String JUMP = "jump";
    final String SAGA = "saga";

    String name = figurine.getBaseName();

    // very specific cases.
    if (figurine.getBaseName().toLowerCase().contains("aries shion")
        && figurine.isOce()
        && figurine.isHk()
        && figurine.isSet()) {
      return name + " & The Pope Set ~Asian Edition~";
    }

    if (figurine.isOce()) {
      if (Anniversary.A_40 != figurine.getAnniversary()) {
        if (Category.V4 != figurine.getCategory()) {
          name += " ~Original Color Edition~";
        }
      }
    }
    if (Category.V2 == figurine.getCategory()) {
      if (figurine.isBroken() && LineUp.MYTH_CLOTH_EX != figurine.getLineUp()) {
        name += " (New Bronze Cloth) ~Broken Version~";
      } else {
        if (Anniversary.A_40 == figurine.getAnniversary()) {
          name += " (New Bronze Cloth)";
        } else {
          if (LineUp.MYTH_CLOTH_EX == figurine.getLineUp()) {
            name += " [New Bronze Cloth]";
          }
        }
      }
    }
    if (Category.V3 == figurine.getCategory()) {
      name += " [Final Bronze Cloth]";
    }
    if (Category.V4 == figurine.getCategory()) {
      if (figurine.isOce()) {
        name += " (God Cloth) ~Original Color Edition~";
      } else {
        if (Anniversary.A_10 == figurine.getAnniversary()) {
          name += " God Cloth";
        } else {
          name += " [God Cloth]";
        }
      }
    }
    if (Category.V5 == figurine.getCategory()) {
      name += " Heaven Chapter";
    }
    if (Category.INHERITOR == figurine.getCategory()) {
      name += " ~Inheritor of the Gold Cloth~";
    }
    if (Category.ROBE == figurine.getCategory()) {
      if (figurine.getBaseName().toLowerCase().contains("sorrento")) {
        name += " <Argard Final Battle Ver.>";
      }
      if (figurine.getBaseName().toLowerCase().contains("polaris")) {
        if (figurine.getDistributionJPY().getReleaseDate().getYear() != 2010) {
          name += " -The Earth Representative Of Odin-";
        }
      }
    }
    if (Category.SURPLICE == figurine.getCategory()) {
      if (!figurine.isBroken()) {
        if (!figurine.getBaseName().toLowerCase().contains(MYSTERIOUS)) {
          name += " (Surplice)";
        }
      }
    }
    if (Series.SS_THE_BEGINNING == figurine.getSeries()) {
      name += " -Knights of the Zodiac-";
    }
    if (Series.SS_LEGEND_OF_SANCTUARY == figurine.getSeries()) {
      if (LineUp.SC_LEGEND != figurine.getLineUp()) {
        name += " Legend of Sanctuary Edition";
      }
    }
    if (Series.SOG == figurine.getSeries()) {
      if (!((figurine.isSet() && !figurine.getBaseName().toLowerCase().contains(SAGA))
          || figurine.getBaseName().toLowerCase().contains("loki")
          || figurine.getBaseName().toLowerCase().contains("odin"))) {
        name += " God Cloth";
      }
    }
    if (Series.SAINTIA_SHO == figurine.getSeries() && Category.GOLD == figurine.getCategory()) {
      name += " Saintia Sho Color Edition";
    }

    if (figurine.isGolden()) {
      if (Category.V1 == figurine.getCategory()) {
        String initialName = name.split(" ")[0];
        name += " (Early Bronze Cloth) ~Limited Gold " + initialName + "~";
      } else {
        if (LineUp.MYTH_CLOTH == figurine.getLineUp()) {
          name += " ~Power of Gold~";
        } else {
          if (!figurine.getBaseName().toLowerCase().contains(JUMP)) {
            name += " ~Golden Limited Edition~";
          }
        }
      }
    }
    if (figurine.isComic()) {
      name += " Comic Ver.";
    }
    if (figurine.isSet()) {
      if (Category.STEEL == figurine.getCategory() || Category.BLACK == figurine.getCategory()) {
        name += " Set";
      }
      if (Category.SPECTER == figurine.getCategory()) {
        name += " Complete Set";
      }
      if (Category.GOD == figurine.getCategory()) {
        if (figurine.getBaseName().toLowerCase().contains("abel")) {
          name += " Memorial Set";
        } else {
          name += " -Divine Saga Premium Set-";
        }
      }
      if (Series.SOG == figurine.getSeries()) {
        if (figurine.getBaseName().toLowerCase().contains(SAGA)) {
          name += " Saga Saga Premium";
        }
        name += " Set";
      }
      if (Category.SCALE == figurine.getCategory()) {
        name += " Imperial Throne Set";
      }
      if (Category.SURPLICE == figurine.getCategory()) {
        if (figurine.isBroken()) {
          name += " Set";
        } else {
          if (!figurine.getBaseName().toLowerCase().contains(MYSTERIOUS)) {
            name += " & The Pope Set";
          }
        }
      }
    }

    if (figurine.isRevival()) {
      if (Objects.nonNull(figurine.getAnniversary())) {
        name += " <" + figurine.getAnniversary().getDescription() + "th Revival Ver.>";
      } else {
        name += " <Revival Ver.>";
      }
    } else {
      if (Objects.nonNull(figurine.getAnniversary())) {
        if (figurine.getBaseName().toLowerCase().contains(JUMP)) {
          name += " " + figurine.getAnniversary().getDescription() + "th Anniversary Edition";
          if (figurine.isGolden()) {
            name += " (Gold Ver.)";
          }
        } else {
          if (figurine.isOce()) {
            if (figurine.getAnniversary() == Anniversary.A_40) {
              name += " ~40th Anniversary Edition~";
            }
          } else {
            if (figurine.getAnniversary() == Anniversary.A_10) {
              name += " ~" + figurine.getAnniversary().getDescription() + "th Anniversary Edition~";
            } else {
              name += " " + figurine.getAnniversary().getDescription() + "th Anniversary Ver.";
            }
          }
        }
      }
    }

    if (figurine.isGold()) {
      if (LineUp.MYTH_CLOTH == figurine.getLineUp()) {
        name = "Golden Genealogy " + name;
      } else {
        name += " Gold24";
      }
    }
    if (figurine.isPlain()) {
      name += " (Plain Clothes)";
    }

    if (LineUp.DDP == figurine.getLineUp()) {
      if (figurine.getBaseName().toLowerCase().contains("athena")) {
        name =
            String.format(
                "Golden Zodiac extension set Fire clock of the Sanctuary ~%s~",
                figurine.getBaseName().toUpperCase());
      } else if (figurine.getBaseName().toLowerCase().contains("gemini")) {
        name = String.format("%s ~the Pope's Chamber~", figurine.getBaseName().toUpperCase());
      } else if (figurine.getBaseName().toLowerCase().contains("cancer")) {
        name =
            String.format(
                "Desperate Battle in the Palace of the Giant Crab ~%s~",
                figurine.getBaseName().toUpperCase());
      } else if (figurine.getBaseName().toLowerCase().contains("leo")) {
        name =
            String.format(
                "Lightning in the Palace of the Lion ~%s~", figurine.getBaseName().toUpperCase());
      } else if (figurine.getBaseName().toLowerCase().contains("virgo")) {
        name = String.format("%s ~The Temple of the Maiden~", figurine.getBaseName().toUpperCase());
      } else if (figurine.getBaseName().toLowerCase().contains("libra")) {
        name =
            String.format(
                "Guidance of the Palace of the Scales ~%s~", figurine.getBaseName().toUpperCase());
      } else if (figurine.getBaseName().toLowerCase().contains("sagittarius")) {
        name =
            String.format(
                "Commitment of Aiolos' Spirit in the Palace of the Centaur ~%s~",
                figurine.getBaseName().toUpperCase());
      } else if (figurine.getBaseName().toLowerCase().contains("capricorn")) {
        name =
            String.format(
                "Glittering Excalibur in the Palace of the Rock Goat ~%s~",
                figurine.getBaseName().toUpperCase());
      } else if (figurine.getBaseName().toLowerCase().contains("pisces")) {
        name =
            String.format(
                "Blooming Roses in the Palace of the Twin Fish ~%s~",
                figurine.getBaseName().toUpperCase());
      } else if (figurine.getBaseName().toLowerCase().contains("pegasus")) {
        name = String.format("%s ~Pegasus Meteor Punches~", figurine.getBaseName().toUpperCase());
      } else if (figurine.getBaseName().toLowerCase().contains("dragon")) {
        name = String.format("Rozan Rising Dragon Blow ~%s~", figurine.getBaseName().toUpperCase());
      } else if (figurine.getBaseName().toLowerCase().contains("andromeda")) {
        name = String.format("Nebula Chain ~%s~", figurine.getBaseName().toUpperCase());
      } else if (figurine.getBaseName().toLowerCase().contains("phoenix")) {
        name = String.format("%s ~Flying Phoenix~", figurine.getBaseName().toUpperCase());
      }
    }

    return name;
  }

  public List<BasicFigurine> getAllBasicFigurines(boolean excludeRestocks) {
    log.info("Retrieving all the existing figurines ...");

    Sort sort = Sort.by(Sort.Order.asc("distributionJPY.releaseDate"));
    List<Figurine> allFigurines = repository.findAll(sort);

    List<Figurine> allFigurinesFiltered = new ArrayList<>();
    if (excludeRestocks) {
      for (Figurine currFigurine : allFigurines) {
        if (allFigurinesFiltered.contains(currFigurine)) {
          for (Figurine currFiltered : allFigurinesFiltered) {
            if (currFiltered.equals(currFigurine)) {
              if (Objects.isNull(currFiltered.getRestocks())) {
                currFiltered.setRestocks(new ArrayList<>());
              }
              Restock restock = new Restock();
              restock.setDistributionJPY(currFigurine.getDistributionJPY());
              restock.setDistributionMXN(currFigurine.getDistributionMXN());
              restock.setTamashiiUrl(currFigurine.getTamashiiUrl());
              restock.setDistributionChannel(currFigurine.getDistributionChannel());
              restock.setRemarks(currFigurine.getRemarks());

              currFiltered.getRestocks().add(restock);
            }
          }
        } else {
          allFigurinesFiltered.add(currFigurine);
        }
      }
    } else {
      allFigurinesFiltered = allFigurines;
    }

    List<Figurine> figurineList =
        allFigurinesFiltered.stream()
            .peek($ -> $.setDisplayableName(calculateDisplayableName($)))
            .peek($ -> $.setStatus(calculateStatus($)))
            .sorted(
                (f1, f2) -> {
                  if (geReleaseDate(f1).isPresent() && geReleaseDate(f2).isPresent()) {
                    return f2.getDistributionJPY()
                        .getReleaseDate()
                        .compareTo(f1.getDistributionJPY().getReleaseDate());
                  }
                  return 0;
                })
            .toList();

    log.info("Found {} figurines", figurineList.size());

    // We create one more list with sorted figurines based on the status.
    List<Figurine> list = new ArrayList<>();
    list.addAll(figurineList.stream().filter($ -> $.getStatus() == Status.RELEASE_TBD).toList());
    list.addAll(figurineList.stream().filter($ -> $.getStatus() == Status.FUTURE_RELEASE).toList());
    list.addAll(figurineList.stream().filter($ -> $.getStatus() == Status.RELEASED).toList());
    list.addAll(getFigurinesFilteredByStatus(figurineList, Status.PROTOTYPE));
    list.addAll(getFigurinesFilteredByStatus(figurineList, Status.UNRELEASED));

    return list.stream().map(this::toBasicFigure).toList();
  }

  private BasicFigurine toBasicFigure(Figurine figure) {
    BasicFigurine basicFigurine = new BasicFigurine();

    basicFigurine.setId(figure.getId());
    basicFigurine.setDisplayableName(figure.getDisplayableName());
    basicFigurine.setLineUp(figure.getLineUp());
    basicFigurine.setCategory(figure.getCategory());
    basicFigurine.setStatus(figure.getStatus());

    return basicFigurine;
  }
}
