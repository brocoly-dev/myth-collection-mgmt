package com.mesofi.myth.collection.mgmt.service;

import com.mesofi.myth.collection.mgmt.model.Anniversary;
import com.mesofi.myth.collection.mgmt.model.Category;
import com.mesofi.myth.collection.mgmt.model.Figurine;
import com.mesofi.myth.collection.mgmt.model.LineUp;
import com.mesofi.myth.collection.mgmt.model.Restock;
import com.mesofi.myth.collection.mgmt.model.Series;
import com.mesofi.myth.collection.mgmt.repository.MythCollectionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MythCollectionService {

  private final MythCollectionRepository repository;

  /**
   * Creates a new figurine.
   *
   * @param figurine The figurine to be created.
   * @return The figurine created in the DB.
   */
  public Figurine createFigurine(final Figurine figurine) {
    log.info("A new figure is about to be created ...");

    Figurine created = repository.save(figurine);

    // Calculates additional information ...
    created.setDisplayableName(calculateDisplayableName(created));

    log.info("A new figure has been created with id: {}", created.getId());
    return created;
  }

  /**
   * Gets all the existing figurine.
   *
   * @param excludeRestocks Flag to exclude the restocks.
   * @return A list with all the existing figurines.
   */
  public List<Figurine> getAllFigurines(boolean excludeRestocks) {
    log.info("Retrieving all the existing figurines ...");

    List<Figurine> allFigurines =
        repository.findAll(Sort.by(Sort.Order.asc("distributionJPY.releaseDate")));

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

    List<Figurine> existingFigurines =
        allFigurinesFiltered.stream()
            .peek(figurine -> figurine.setDisplayableName(calculateDisplayableName(figurine)))
            .toList();

    log.info("Found {} figurines", existingFigurines.size());
    return existingFigurines;
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

    String name = figurine.getBaseName();

    if (figurine.isOce()) {
      if (Anniversary.A_40 != figurine.getAnniversary()) {
        if (Category.V4 != figurine.getCategory()) {
          name += " ~Original Color Edition~";
        }
      }
    }
    if (Category.V2 == figurine.getCategory()) {
      if (figurine.isBroken()) {
        name += " (New Bronze Cloth) ~Broken Version~";
      } else {
        if (Anniversary.A_40 == figurine.getAnniversary()) {
          name += " (New Bronze Cloth)";
        } else {
          name += " [New Bronze Cloth]";
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
      name += " God Cloth";
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
        name += " Saga Saga Premium Set";
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
}
