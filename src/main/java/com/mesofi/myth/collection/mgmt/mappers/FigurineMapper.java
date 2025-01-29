package com.mesofi.myth.collection.mgmt.mappers;

import com.mesofi.myth.collection.mgmt.model.Anniversary;
import com.mesofi.myth.collection.mgmt.model.Category;
import com.mesofi.myth.collection.mgmt.model.Distribution;
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
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class FigurineMapper {

  private static final DateTimeFormatter formatterMDY =
      new DateTimeFormatterBuilder()
          .appendPattern("M/d/yyyy") // Format: M/d/yyyy
          .toFormatter();

  private static final DateTimeFormatter formatterMY =
      new DateTimeFormatterBuilder()
          .appendPattern("M/yyyy") // Format: M/yyyy
          .toFormatter();

  private final List<DistributionChannel> distributionChannels;
  private final List<Distributor> distributors;

  public FigurineMapper(
      DistributionChannelService distributionChannelService,
      DistributorService distributorService) {
    this.distributors = distributorService.getAllDistributors();
    this.distributionChannels = distributionChannelService.getAllDistributionChannels();
  }

  /**
   * Maps the source figurine to the figurine.
   *
   * @param catalog The source figurine.
   * @return The actual figurine.
   */
  public Figurine toFigure(SourceFigurine catalog) {
    Figurine figurine = new Figurine();
    figurine.setBaseName(catalog.getBaseName());

    getDistribution(figurine, true).setBasePrice(toBigDecimal(catalog.getPriceJPY()));
    getDistribution(figurine, true).setFirstAnnouncementDate(toLocalDate(catalog.getAnnJPY()));
    getDistribution(figurine, true).setPreOrderDate(toLocalDate(catalog.getPreorderJPY()));
    getDistribution(figurine, true).setReleaseDate(toLocalDate(catalog.getReleaseJPY()));
    getDistribution(figurine, true).setReleaseDateConfirmed(isConfirmed(catalog.getReleaseJPY()));

    getDistribution(figurine, false).setDistributor(findDistributor(catalog.getDistributorMXN()));
    getDistribution(figurine, false).setBasePrice(toBigDecimal(catalog.getPriceMXN()));
    getDistribution(figurine, false).setPreOrderDate(toLocalDate(catalog.getPreorderMXN()));
    getDistribution(figurine, false).setReleaseDate(toLocalDate(catalog.getReleaseMXN()));
    getDistribution(figurine, false).setReleaseDateConfirmed(isConfirmed(catalog.getReleaseMXN()));

    figurine.setTamashiiUrl(StringUtils.hasLength(catalog.getLink()) ? catalog.getLink() : null);
    figurine.setDistributionChannel(findDistributionChannel(catalog.getDist()));
    figurine.setLineUp(toLineUp(catalog.getLineUp()));
    figurine.setSeries(toSeries(catalog.getSeries()));
    figurine.setCategory(toCategory(catalog.getGroup()));

    figurine.setMetal(toBoolean(catalog.getMetal()));
    figurine.setOce(toBoolean(catalog.getOce()));
    figurine.setRevival(toBoolean(catalog.getRevival()));
    figurine.setPlain(toBoolean(catalog.getPlainCloth()));
    figurine.setBroken(toBoolean(catalog.getBroken()));
    figurine.setGolden(toBoolean(catalog.getGolden()));
    figurine.setGold(toBoolean(catalog.getGold()));
    figurine.setHk(toBoolean(catalog.getHk()));
    figurine.setComic(toBoolean(catalog.getManga()));
    figurine.setSet(toBoolean(catalog.getSet()));

    figurine.setAnniversary(toAnniversary(catalog.getAnniversary()));
    figurine.setRemarks(StringUtils.hasLength(catalog.getRemarks()) ? catalog.getRemarks() : null);

    if (isDistributionEmpty(figurine.getDistributionJPY())) {
      figurine.setDistributionJPY(null);
    }
    if (isDistributionEmpty(figurine.getDistributionMXN())) {
      figurine.setDistributionMXN(null);
    }
    return figurine;
  }

  private Anniversary toAnniversary(String anniversary) {
    return switch (anniversary) {
      case "10" -> Anniversary.A_10;
      case "15" -> Anniversary.A_15;
      case "20" -> Anniversary.A_20;
      case "30" -> Anniversary.A_30;
      case "40" -> Anniversary.A_40;
      case "50" -> Anniversary.A_50;
      default -> null;
    };
  }

  private boolean toBoolean(String value) {
    return "TRUE".equals(value) ? true : false;
  }

  private Category toCategory(String group) {
    return switch (group) {
      case "Bronze Saint V1" -> Category.V1;
      case "Bronze Saint V2" -> Category.V2;
      case "Bronze Saint V3" -> Category.V3;
      case "Bronze Saint V4" -> Category.V4;
      case "Bronze Saint V5" -> Category.V5;
      case "Bronze Secondary" -> Category.SECONDARY;
      case "Black Saint" -> Category.BLACK;
      case "Steel" -> Category.STEEL;
      case "Silver Saint" -> Category.SILVER;
      case "Gold Saint" -> Category.GOLD;
      case "God Robe" -> Category.ROBE;
      case "Poseidon Scale" -> Category.SCALE;
      case "Surplice Saint" -> Category.SURPLICE;
      case "Specter" -> Category.SPECTER;
      case "Judge" -> Category.JUDGE;
      case "God" -> Category.GOD;
      case "Inheritor" -> Category.INHERITOR;
      default -> null;
    };
  }

  private LineUp toLineUp(String lineUp) {
    return switch (lineUp) {
      case "Myth Cloth EX" -> LineUp.MYTH_CLOTH_EX;
      case "Myth Cloth" -> LineUp.MYTH_CLOTH;
      case "Appendix" -> LineUp.APPENDIX;
      case "Saint Cloth Legend" -> LineUp.SC_LEGEND;
      case "Figuarts" -> LineUp.FIGUARTS;
      case "Saint Cloth Crown" -> LineUp.SC_CROWN;
      case "DD Panoramation" -> LineUp.DDP;
      case "Figuarts Zero Metallic Touch" -> LineUp.FIGUARTS_ZERO;
      default -> null;
    };
  }

  private Series toSeries(String series) {
    return switch (series) {
      case "Saint Seiya" -> Series.SAINT_SEIYA;
      case "Saintia Sho" -> Series.SAINTIA_SHO;
      case "Soul of Gold" -> Series.SOG;
      case "Saint Seiya Legend Of Sanctuary" -> Series.SS_LEGEND_OF_SANCTUARY;
      case "Saint Seiya Omega" -> Series.SS_OMEGA;
      case "The Lost Canvas" -> Series.LOST_CANVAS;
      case "Saint Seiya The Beginning" -> Series.SS_THE_BEGINNING;

      default -> null;
    };
  }

  private Distributor findDistributor(String distributor) {
    return distributors.stream()
        .filter(d -> d.getName().equals(distributor))
        .findFirst()
        .orElse(null);
  }

  private DistributionChannel findDistributionChannel(String distribution) {
    return distributionChannels.stream()
        .filter(dc -> dc.getDistribution().equals(distribution))
        .findFirst()
        .orElse(null);
  }

  private boolean isDistributionEmpty(Distribution distribution) {
    if (Objects.isNull(distribution)) {
      return true;
    } else {
      return Objects.isNull(distribution.getDistributor())
          && Objects.isNull(distribution.getBasePrice())
          && Objects.isNull(distribution.getFirstAnnouncementDate())
          && Objects.isNull(distribution.getPreOrderDate())
          && Objects.isNull(distribution.getReleaseDate())
          && Objects.isNull(distribution.getReleaseDateConfirmed());
    }
  }

  private Boolean isConfirmed(String date) {
    if (date.isEmpty()) {
      return null;
    } else {
      return !(date.length() == 6 || date.length() == 7);
    }
  }

  private BigDecimal toBigDecimal(String amount) {
    return (amount.isEmpty() || amount.equals("¥0"))
        ? null
        : new BigDecimal(amount.substring(1).replaceAll(",", ""));
  }

  private LocalDate toLocalDate(String date) {
    return date.isEmpty() ? null : tryParse(date);
  }

  private LocalDate tryParse(String input) {
    try {
      // Try the first format
      return LocalDate.parse(input, formatterMDY);
    } catch (DateTimeParseException e1) {
      try {
        // If parsing fails, try the second format
        return YearMonth.parse(input, formatterMY).atDay(1);
      } catch (DateTimeParseException e2) {
        // If both parsing attempts fail, throw an exception
        throw new IllegalArgumentException("Invalid date format: " + input);
      }
    }
  }

  private Distribution getDistribution(Figurine figurine, boolean jpy) {
    if (jpy) {
      if (Objects.isNull(figurine.getDistributionJPY())) {
        Distribution distribution = new Distribution();
        figurine.setDistributionJPY(distribution);
      }
      return figurine.getDistributionJPY();
    } else {
      if (Objects.isNull(figurine.getDistributionMXN())) {
        Distribution distribution = new Distribution();
        figurine.setDistributionMXN(distribution);
      }
      return figurine.getDistributionMXN();
    }
  }
}
