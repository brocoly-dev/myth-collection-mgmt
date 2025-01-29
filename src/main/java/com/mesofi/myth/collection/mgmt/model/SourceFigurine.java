package com.mesofi.myth.collection.mgmt.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SourceFigurine {
  @CsvBindByName(required = true, column = "Base Name")
  private String baseName;

  @CsvBindByName(column = "Price (JPY)")
  private String priceJPY;

  @CsvBindByName(column = "Announcement (JPY)")
  private String annJPY;

  @CsvBindByName(column = "Preorder (JPY)")
  private String preorderJPY;

  @CsvBindByName(column = "Release (JPY)")
  private String releaseJPY;

  @CsvBindByName(column = "Distributor (MXN)")
  private String distributorMXN;

  @CsvBindByName(column = "Price (MXN)")
  private String priceMXN;

  @CsvBindByName(column = "Preorder (MXN)")
  private String preorderMXN;

  @CsvBindByName(column = "Release (MXN)")
  private String releaseMXN;

  @CsvBindByName(column = "Link")
  private String link;

  @CsvBindByName(column = "Distribution")
  private String dist;

  @CsvBindByName(column = "LineUp")
  private String lineUp;

  @CsvBindByName(column = "Series")
  private String series;

  @CsvBindByName(column = "Group")
  private String group;

  @CsvBindByName(column = "Metal")
  private String metal;

  @CsvBindByName(column = "OCE")
  private String oce;

  @CsvBindByName(column = "Revival")
  private String revival;

  @CsvBindByName(column = "PlainCloth")
  private String plainCloth;

  @CsvBindByName(column = "Broken")
  private String broken;

  @CsvBindByName(column = "Golden")
  private String golden;

  @CsvBindByName(column = "Gold")
  private String gold;

  @CsvBindByName(column = "HK")
  private String hk;

  @CsvBindByName(column = "Manga")
  private String manga;

  @CsvBindByName(column = "Set")
  private String set;

  @CsvBindByName(column = "Anniversary")
  private String anniversary;

  @CsvBindByName(column = "Remarks")
  private String remarks;
}
